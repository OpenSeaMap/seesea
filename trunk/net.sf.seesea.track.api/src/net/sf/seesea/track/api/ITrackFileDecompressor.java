package net.sf.seesea.track.api;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import net.sf.seesea.track.api.data.CompressionType;
import net.sf.seesea.track.api.data.IContainedTrackFile;
import net.sf.seesea.track.api.data.ITrackFile;

/**
 * this is a format specific decompressor. 
 *
 */
public interface ITrackFileDecompressor {

	List<IContainedTrackFile> getUnzippedFiles(ZipFile file, List<ZipEntry> zipEntries, String encoding);

	List<IContainedTrackFile> getTracks(CompressionType compressionType, File file) throws ZipException, IOException; 
	
}
