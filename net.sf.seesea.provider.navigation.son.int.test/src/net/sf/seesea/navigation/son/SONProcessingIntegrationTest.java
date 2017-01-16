package net.sf.seesea.navigation.son;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.eclipse.core.runtime.FileLocator;
import org.junit.Test;

import net.sf.seesea.model.core.physx.Measurement;
import net.sf.seesea.track.api.IMeasurmentProcessor;
import net.sf.seesea.track.api.data.ITrackFile;
import net.sf.seesea.track.api.exception.ProcessingException;

public class SONProcessingIntegrationTest {

	/**
	 * tests the integration between decompressor and trackfile processor play nicely
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ProcessingException
	 */
	@Test
	public void testDecompressionTrackFileProcessorIntegration() throws FileNotFoundException, IOException, ProcessingException {
		URL nextElement = SONActivator.getContext().getBundle().findEntries("res", "9171.dat", true).nextElement();
		URL fileURL = FileLocator.resolve(nextElement);
		File file = new File(fileURL.getFile());
		ZipFile zipFile = new ZipFile(file);
		Enumeration<? extends ZipEntry> entries = zipFile.entries();
		List<ZipEntry> zipEntires = new ArrayList<ZipEntry>();
		while(entries.hasMoreElements()) {
			ZipEntry archiveEntry = entries.nextElement();
			zipEntires.add(archiveEntry);
		}
		
		SONTrackFileDecompressor sonTrackFIleDecompressor = new SONTrackFileDecompressor();
		List<ITrackFile> unzippedFiles = sonTrackFIleDecompressor.getUnzippedFiles(zipFile, zipEntires, "UTF-8");

		SONTrackFileProcessor sonTrackFileProcessor = new SONTrackFileProcessor();
		final List<Measurement> measurements = new ArrayList<>();
		sonTrackFileProcessor.setMeasurementProcessor(new IMeasurmentProcessor() {
			
			@Override
			public void processMeasurements(List<Measurement> results, String messageType, ITrackFile trackfile)
					throws ProcessingException {
				measurements.addAll(results);
				
			}
			
			@Override
			public void finish() throws ProcessingException {
				
			}
		});
		for (ITrackFile trackFile : unzippedFiles) {
			sonTrackFileProcessor.processFile(trackFile);
		}
		assertFalse(measurements.isEmpty());
		assertEquals(60411, measurements.size());
	}
	
}
