package net.sf.seesea.content.tika;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.easymock.EasyMock;
import org.junit.Test;
import org.osgi.framework.FrameworkUtil;

import net.sf.seesea.content.api.ContentDetectionException;
import net.sf.seesea.content.impl.ContentDetector;
import net.sf.seesea.track.api.IStreamProcessor;
import net.sf.seesea.track.api.IStreamProcessorDetection;
import net.sf.seesea.track.api.ITrackPersistence;
import net.sf.seesea.track.api.data.CompressionType;
import net.sf.seesea.track.api.data.ITrackFile;
import net.sf.seesea.track.api.data.ProcessingState;
import net.sf.seesea.track.api.exception.RawDataEventException;
import net.sf.seesea.track.api.exception.TrackPerssitenceException;
import net.sf.seesea.track.model.SimpleTrackFile;

public class ContentDetectorTest {

	
	@Test
	public void testX() throws ContentDetectionException, TrackPerssitenceException {
		List<ITrackFile> tracks = new ArrayList<ITrackFile>();
		tracks.add(new SimpleTrackFile());
		
		ITrackPersistence trackPersistence = EasyMock.createNiceMock(ITrackPersistence.class);
		EasyMock.expect(trackPersistence.getTrackFiles2Process()).andReturn(tracks);
		EasyMock.expectLastCall().anyTimes();
		EasyMock.replay(trackPersistence);
		
		ContentDetector contentDetector = new ContentDetector();
		contentDetector.bindTrackPersistence(trackPersistence);
//		contentDetector.activate(properties);
		contentDetector.setContentTypes();
	}
	
	@Test
	public void testGZFiles() throws TrackPerssitenceException, ContentDetectionException, IOException, RawDataEventException {
		List<ITrackFile> tracks = new ArrayList<ITrackFile>();
		SimpleTrackFile simpleTrackFile = new SimpleTrackFile();
		tracks.add(simpleTrackFile);
		URL trackURL = FrameworkUtil.getBundle(ContentDetector.class).findEntries("/res/gz/000", "31.dat", false).nextElement();
		simpleTrackFile.setTrackURL(trackURL);
		simpleTrackFile.setTrackId(31);
		
		ITrackPersistence trackPersistence = EasyMock.createNiceMock(ITrackPersistence.class);
		EasyMock.expect(trackPersistence.getTrackFiles2Process()).andReturn(tracks);
		EasyMock.expectLastCall().anyTimes();
		EasyMock.replay(trackPersistence);

		IStreamProcessor streamProcessor = EasyMock.createNiceMock(IStreamProcessor.class);
		EasyMock.replay(streamProcessor);
		
		IStreamProcessorDetection streamProcessorDetection = EasyMock.createNiceMock(IStreamProcessorDetection.class);
		EasyMock.expect(streamProcessorDetection.detectStreamProcessorEnblock(EasyMock.<InputStream>anyObject(), EasyMock.anyBoolean())).andReturn(streamProcessor);
		EasyMock.expectLastCall().anyTimes();
		EasyMock.replay(streamProcessorDetection);

		
		ContentDetector contentDetector = new ContentDetector();
		contentDetector.bindTrackPersistence(trackPersistence);
		contentDetector.bindStreamProcessor(streamProcessorDetection);
		HashMap<String, Object> properties = new HashMap<String, Object>();
		properties.put("basedir", "C:/Users/jens/openseamap4/ws/net.sf.seesea.content.tika.int.test/res/gz");
		contentDetector.activate(properties);
		contentDetector.setContentTypes();
		
		assertEquals(ProcessingState.PREPROCESSED, simpleTrackFile.getUploadState());
		assertEquals(CompressionType.GZ, simpleTrackFile.getCompression());
		
	}

}
