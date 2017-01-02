package net.sf.seesea.data.postprocessing.database;

import java.util.concurrent.atomic.AtomicReference;

import org.apache.log4j.Logger;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import net.sf.seesea.content.api.ContentDetectionException;
import net.sf.seesea.content.api.IContentDetector;
import net.sf.seesea.contour.api.IContourLineGeneration;
import net.sf.seesea.data.postprocessing.process.IFilterEngine;
import net.sf.seesea.data.sync.api.IDepthDataSync;

@Component
public class UploadedData2Contours implements IUploadedData2Contours {

	private AtomicReference<IDepthDataSync> dataSync = new AtomicReference<IDepthDataSync>();
	
	private AtomicReference<IContentDetector> contentDetector = new AtomicReference<IContentDetector>();
	
	private AtomicReference<IFilterEngine> filterEngine = new AtomicReference<IFilterEngine>();

	private AtomicReference<IContourLineGeneration> contourLineGeneration = new AtomicReference<IContourLineGeneration>();
	
	public void processData() {
		IDepthDataSync depthDataSync = dataSync.get();
		if(depthDataSync != null) {
			depthDataSync.downloadFiles();
//			depthDataSync.downloadSQL();
		} else {
			Logger.getLogger(getClass()).info("Depth sync not or falsely configured. Skipping file synchronization");
		}
		
		IContentDetector contentDetector2 = contentDetector.get();
		if(contentDetector2 != null) {
			try {
				contentDetector2.setContentTypes();
			} catch (ContentDetectionException e) {
				Logger.getLogger(getClass()).error("An error occurred during content detection", e);
			}
		} else {
			Logger.getLogger(getClass()).info("Content detection not or falsely configured. Skipping content detection");
		}
		IFilterEngine filterEngine2 = filterEngine.get();
		if(filterEngine2 != null) {
			filterEngine2.filterTracks();
		} else {
			Logger.getLogger(getClass()).info("Filters not or falsely configured. Skipping filtering");
		}
		IContourLineGeneration contourLineGeneration2 = contourLineGeneration.get();
		if(contourLineGeneration2 != null) {
			contourLineGeneration2.updateContourLines(null, null, null, null);
		} else {
			Logger.getLogger(getClass()).info("Contour generation not or falsely configured. Skipping contour generation");
		} 
	}
	
	@Reference(cardinality = ReferenceCardinality.OPTIONAL, policy = ReferencePolicy.DYNAMIC)
	public void bindContentDetector(IContentDetector contentDetector) {
		this.contentDetector.set(contentDetector);
	}

	public void unbindContentDetector(IContentDetector contentDetector) {
		this.contentDetector.compareAndSet(null, contentDetector);
	}

	@Reference(cardinality = ReferenceCardinality.OPTIONAL, policy = ReferencePolicy.DYNAMIC)
	public void bindFilterEngine(IFilterEngine filterEngine) {
		this.filterEngine.set(filterEngine);
	}

	public void unbindFilterEngine(IFilterEngine filterEngine) {
		this.filterEngine.compareAndSet(null, filterEngine);
	}

	@Reference(cardinality = ReferenceCardinality.OPTIONAL, policy = ReferencePolicy.DYNAMIC)
	public void bindStreamProcessor(IContourLineGeneration contourLineGeneration) {
		this.contourLineGeneration.set(contourLineGeneration);
	}

	public void unbindStreamProcessor(IContourLineGeneration contourLineGeneration) {
		this.contourLineGeneration.compareAndSet(null, contourLineGeneration);
	}

	@Reference(cardinality = ReferenceCardinality.OPTIONAL, policy = ReferencePolicy.DYNAMIC)
	public void bindDepthSync(IDepthDataSync contourLineGeneration) {
		this.dataSync.set(contourLineGeneration);
	}

	public void unbindDepthSync(IDepthDataSync contourLineGeneration) {
		this.dataSync.compareAndSet(null, contourLineGeneration);
	}

}
