package net.sf.seesea.data.postprocessing.filter;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.log4j.Logger;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import net.sf.seesea.data.postprocessing.process.FilterException;
import net.sf.seesea.data.postprocessing.process.IFilterController;
import net.sf.seesea.data.postprocessing.process.IFilterEngine;
import net.sf.seesea.data.postprocessing.process.ITrackClustering;
import net.sf.seesea.data.postprocessing.process.TrackClusterResult;
import net.sf.seesea.gauge.GaugeUpdateException;
import net.sf.seesea.gauge.IGaugeValueUpdater;
import net.sf.seesea.track.api.ITrackPersistence;
import net.sf.seesea.track.api.data.ITrackFile;
import net.sf.seesea.track.api.data.ProcessingState;
import net.sf.seesea.track.api.exception.TrackPerssitenceException;

/**
 * The Filter Engine processes track cluster results. It stores results to
 * persistent storage and triggers filter runs for a single cluster with a
 * filter controller. Since this is a convenient place to update candidate
 * gauges this done as well.
 * 
 * @author Jens
 *
 */
@Component(configurationPolicy = ConfigurationPolicy.REQUIRE)
public class FilterEngine implements IFilterEngine {

	private AtomicReference<ITrackPersistence> trackPersistenceAR = new AtomicReference<ITrackPersistence>();

	private AtomicReference<IFilterController> filterControllerAR = new AtomicReference<IFilterController>();

	private AtomicReference<IGaugeValueUpdater> gaugeValueUpdaterAR = new AtomicReference<IGaugeValueUpdater>();;

	private AtomicReference<ITrackClustering> trackClusteringAR = new AtomicReference<ITrackClustering>();;

	public void activate(Map<String, Object> config) {
		preprocessRun = Boolean.valueOf((String) config.get("preprocessRun"));
	}

	public void clusterTracks() {
		ITrackPersistence trackPersistence = trackPersistenceAR.get();
		ITrackClustering trackClustering = trackClusteringAR.get();
		IGaugeValueUpdater gaugeValueUpdater = gaugeValueUpdaterAR.get();
		Map<String, List<ITrackFile>> user2PostprocessTrackCluster;
		try {
			user2PostprocessTrackCluster = trackPersistence.getUser2PreprocessedTracks();
			for (Entry<String, List<ITrackFile>> user2TrackListEntry : user2PostprocessTrackCluster.entrySet()) {
				// String user = user2TrackListEntry.getKey();
				List<ITrackFile> trackFiles = user2TrackListEntry.getValue();
				TrackClusterResult trackClusterResult = trackClustering.classifyTracks(trackFiles);

				try {
					// those measurements that have no time need to be
					// processed separately
					for (ITrackFile abstractTrackFile : trackClusterResult.getNoTimeMeasurementFiles()) {
						String clusterUUID = UUID.randomUUID().toString();
						List<ITrackFile> singleTrackList = new ArrayList<ITrackFile>();
						singleTrackList.add(abstractTrackFile);
						abstractTrackFile.setUploadState(ProcessingState.CLUSTERED);
						abstractTrackFile.setCluster(clusterUUID);
						// encode no time measurement as -1
						abstractTrackFile.setClusterSequenceNumber(-1);
					}
					trackPersistence.storeTrackCluster(trackClusterResult.getNoTimeMeasurementFiles());
				} catch (TrackPerssitenceException e1) {
					Logger.getLogger(getClass()).error("Failed to store track files", e1);
				}

				// those measurements that logically form a single track that is
				// spread across several files
				for (List<ITrackFile> clusterOfTrackFiles : trackClusterResult.getOrderedTrackFiles()) {
					String clusterUUID = UUID.randomUUID().toString();
					try {
						if (!clusterOfTrackFiles.isEmpty()) {
							List<ITrackFile> trackFilesCluster = new ArrayList<ITrackFile>(clusterOfTrackFiles);
							if (gaugeValueUpdater != null) {
								try {
									gaugeValueUpdater.updateGaugeValues4Track(trackFilesCluster);
								} catch (GaugeUpdateException e) {
									Logger.getLogger(getClass()).error("Failed to update gauge", e);
								}
							}
							int i = 0;
							for (ITrackFile iTrackFile : trackFilesCluster) {
								iTrackFile.setUploadState(ProcessingState.CLUSTERED);
								iTrackFile.setCluster(clusterUUID);
								iTrackFile.setClusterSequenceNumber(i++);
							}
						}

						// mark as clustered
						trackPersistence.storeTrackCluster(clusterOfTrackFiles);
					} catch (TrackPerssitenceException e) {
						Logger.getLogger(getClass()).error("Failed to store cluster result", e);
					}
				}
			}
		} catch (TrackPerssitenceException e3) {
			Logger.getLogger(getClass()).error("Failed to retrieve tracks from track persistence", e3);
			return;
		}

	}

	public void filterNoTime() throws TrackPerssitenceException {
		ITrackPersistence trackPersistence = trackPersistenceAR.get();
		IFilterController filterController = filterControllerAR.get();
		Map<String, List<ITrackFile>> user2PostprocessTrackCluster = trackPersistence.getUser2NoTimeTracksTracks();
		for (Entry<String, List<ITrackFile>> user2TrackListEntry : user2PostprocessTrackCluster.entrySet()) {
			String user = user2TrackListEntry.getKey();
			List<ITrackFile> trackFiles = user2TrackListEntry.getValue();
			try {
				// those measurements that have no time need to be
				// processed separately
				for (ITrackFile abstractTrackFile : trackFiles) {
					List<ITrackFile> singleTrackList = new ArrayList<ITrackFile>();
					singleTrackList.add(abstractTrackFile);
					filterController.process(singleTrackList, false);
					abstractTrackFile.setUploadState(ProcessingState.FILE_PROCESSED);
				}
				trackPersistence.storePreprocessingStates(trackFiles);
			} catch (TrackPerssitenceException | FilterException e1) {
				Logger.getLogger(getClass()).error("Problem during filtering:", e1);
			}
		}
	}

	@Override
	public void filterTracks() {
		try {
			filterNoTime();
			filterTimeTracks();
		} catch (TrackPerssitenceException e3) {
			Logger.getLogger(getClass()).error("Failed to retrieve tracks from track persistence", e3);
			return;
		}
	}

	public void filterTimeTracks() throws TrackPerssitenceException {
		ITrackPersistence trackPersistence = trackPersistenceAR.get();
		IFilterController filterController = filterControllerAR.get();
		Map<String, Map<String, List<ITrackFile>>> postprocessTrackCluster = trackPersistence.getUser2TimeClusteredTracks();
		for (Entry<String, Map<String, List<ITrackFile>>> user2TrackListEntry : postprocessTrackCluster.entrySet()) {
			String user = user2TrackListEntry.getKey();
			Map<String, List<ITrackFile>> trackFiles = user2TrackListEntry.getValue();
			// those measurements that logically form a single track that is
			// spread across several files
			for (Entry<String, List<ITrackFile>> clusterId2TrackFiles : trackFiles.entrySet()) {
				List<ITrackFile> clusterOfTrackFiles = clusterId2TrackFiles.getValue();
				try {
					if (!clusterOfTrackFiles.isEmpty()) {
						List<ITrackFile> trackFilesCluster = new ArrayList<ITrackFile>(clusterOfTrackFiles);
						filterController.process(trackFilesCluster, true);
						int i = 0;
						for (ITrackFile iTrackFile : trackFilesCluster) {
							iTrackFile.setUploadState(ProcessingState.FILE_PROCESSED);
						}
					}
					trackPersistence.storePreprocessingStates(clusterOfTrackFiles);
				} catch (FilterException | TrackPerssitenceException e) {
					Logger.getLogger(getClass()).error("Problem during filtering:", e);
				}
			}
		}

	}

	private boolean preprocessRun;

	@Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.DYNAMIC)
	public void bindTrackPersistence(ITrackPersistence trackPersistence) {
		trackPersistenceAR.set(trackPersistence);
	}

	public void unbindTrackPersistence(ITrackPersistence trackPersistence) {
		trackPersistenceAR.compareAndSet(null, trackPersistence);
	}

	@Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.DYNAMIC)
	public void bindFilterController(IFilterController trackPersistence) {
		filterControllerAR.set(trackPersistence);
	}

	public void unbindFilterController(IFilterController trackPersistence) {
		filterControllerAR.compareAndSet(null, trackPersistence);
	}

	@Reference(cardinality = ReferenceCardinality.OPTIONAL, policy = ReferencePolicy.DYNAMIC)
	public void bindGaugeValueUpdater(IGaugeValueUpdater gaugeValueUpdater) {
		gaugeValueUpdaterAR.set(gaugeValueUpdater);
	}

	public void unbindGaugeValueUpdater(IGaugeValueUpdater gaugeValueUpdater) {
		gaugeValueUpdaterAR.compareAndSet(null, gaugeValueUpdater);
	}

	@Reference(cardinality = ReferenceCardinality.OPTIONAL, policy = ReferencePolicy.DYNAMIC)
	public void bindITrackClustering(ITrackClustering gaugeValueUpdater) {
		trackClusteringAR.set(gaugeValueUpdater);
	}

	public void unbindITrackClustering(ITrackClustering gaugeValueUpdater) {
		trackClusteringAR.compareAndSet(null, gaugeValueUpdater);
	}

}
