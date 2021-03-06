package net.sf.seesea.data.postprocessing.filter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.easymock.EasyMock;
import org.junit.Test;

import net.sf.seesea.data.postprocessing.process.FilterException;
import net.sf.seesea.data.postprocessing.process.IFileTypeProcessingFactory;
import net.sf.seesea.data.postprocessing.process.IFilter;
import net.sf.seesea.data.postprocessing.process.StatisticsException;
import net.sf.seesea.model.core.geo.MeasuredPosition3D;
import net.sf.seesea.model.core.physx.Measurement;
import net.sf.seesea.track.api.ITrackFileProcessor;
import net.sf.seesea.track.api.data.ITrackFile;
import net.sf.seesea.track.api.data.SensorDescriptionUpdateRate;
import net.sf.seesea.track.api.exception.ProcessingException;
import net.sf.seesea.track.model.SimpleTrackFile;

public class FilterControllerTest {

	public FilterControllerTest() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @throws FilterException
	 */
	@Test
	public void testNoEngineRunOnEmptyFileFormat() throws FilterException {
		List<ITrackFile> trackFiles = new ArrayList<ITrackFile>();
		trackFiles.add(new SimpleTrackFile());
		
		FilterController filterController = new FilterController();
		filterController.process(trackFiles, true);
	}

	/**
	 * @throws FilterException
	 * @throws StatisticsException 
	 */
	@Test
	public void testNoEngineRunOnEmptyFileFormat2() throws FilterException, StatisticsException {
		List<ITrackFile> trackFiles = new ArrayList<ITrackFile>();
		SimpleTrackFile simpleTrackFile = new SimpleTrackFile();
		simpleTrackFile.setFileType("application/x-nmea0183");
		trackFiles.add(simpleTrackFile);

		ITrackFileProcessor statsPreprocessor = EasyMock.createNiceMock(ITrackFileProcessor.class);
		Set<SensorDescriptionUpdateRate<Measurement>> bestRates = new HashSet<SensorDescriptionUpdateRate<Measurement>>();
		SensorDescriptionUpdateRate positionUpdateRate = new SensorDescriptionUpdateRate<MeasuredPosition3D>(MeasuredPosition3D.class, "sensorIDA", "GLL", 1000L, 1);
		bestRates.add(positionUpdateRate);
		
//		EasyMock.expect(statsPreprocessor.getBestSensors()).andReturn(bestRates);
		EasyMock.replay(statsPreprocessor);

		IFileTypeProcessingFactory processingFactory = EasyMock.createNiceMock(IFileTypeProcessingFactory.class);
		EasyMock.expect(processingFactory.createLocationPreProcessor(EasyMock.<ITrackFile>anyObject())).andReturn(statsPreprocessor);
		EasyMock.replay(processingFactory);

		
		FilterController filterController = new FilterController();
		filterController.bindFileTypeProcessingFactory(processingFactory);
		filterController.process(trackFiles, true);
	}

	@Test
	public void testEngineRunOn2Files() throws FilterException, StatisticsException, ProcessingException {
		List<ITrackFile> trackFiles = new ArrayList<ITrackFile>();
		SimpleTrackFile simpleTrackFile = new SimpleTrackFile();
		simpleTrackFile.setFileType("application/x-nmea0183");
		simpleTrackFile.setAbsoluteTimeMeasurements(true);
		SimpleTrackFile simpleTrackFile2 = new SimpleTrackFile();
		simpleTrackFile2.setFileType("application/x-nmea0183");
		trackFiles.add(simpleTrackFile2);
		simpleTrackFile2.setAbsoluteTimeMeasurements(true);

		ITrackFileProcessor statsPreprocessor = EasyMock.createNiceMock(ITrackFileProcessor.class);
		EasyMock.expect(statsPreprocessor.hasAbsoluteTime()).andReturn(true);
		Set<SensorDescriptionUpdateRate<Measurement>> bestRates = new HashSet<SensorDescriptionUpdateRate<Measurement>>();
		SensorDescriptionUpdateRate positionUpdateRate = new SensorDescriptionUpdateRate<MeasuredPosition3D>(MeasuredPosition3D.class, "sensorIDA", "GLL", 1000L, 1);
		bestRates.add(positionUpdateRate);
		
//		EasyMock.expect(statsPreprocessor.getBestSensors()).andReturn(bestRates);
		EasyMock.replay(statsPreprocessor);

		IFileTypeProcessingFactory processingFactory = EasyMock.createNiceMock(IFileTypeProcessingFactory.class);
		EasyMock.expect(processingFactory.createLocationPreProcessor(EasyMock.<ITrackFile>anyObject())).andReturn(statsPreprocessor).anyTimes();
		EasyMock.replay(processingFactory);

		IFilter filter = EasyMock.createNiceMock(IFilter.class);
		EasyMock.expect(filter.requiresAbsoluteTime()).andReturn(true);
		filter.finish();
		EasyMock.replay(filter);
		
		FilterController filterController = new FilterController();
		filterController.bindFileTypeProcessingFactory(processingFactory);
		filterController.bindFilter(filter);
		filterController.process(trackFiles, false);
		EasyMock.verify(filter);
	}
	
}
