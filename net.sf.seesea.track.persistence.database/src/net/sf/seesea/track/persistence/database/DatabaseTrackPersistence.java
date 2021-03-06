package net.sf.seesea.track.persistence.database;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Formatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import net.sf.seesea.data.io.IDataWriter;
import net.sf.seesea.data.io.WriterException;
import net.sf.seesea.geometry.impl.Point3D;
import net.sf.seesea.model.core.physx.CompositeMeasurement;
import net.sf.seesea.track.api.ITrackFileDecompressor;
import net.sf.seesea.track.api.ITrackPersistence;
import net.sf.seesea.track.api.data.CompressionType;
import net.sf.seesea.track.api.data.IContainedTrackFile;
import net.sf.seesea.track.api.data.ITrackFile;
import net.sf.seesea.track.api.data.ProcessingState;
import net.sf.seesea.track.api.exception.TrackPerssitenceException;
import net.sf.seesea.track.model.DepthSensor;
import net.sf.seesea.track.model.GzipTrackFile;
import net.sf.seesea.track.model.SimpleTrackFile;
import net.sf.seesea.track.model.VesselConfiguration;
import net.sf.seesea.track.model.ZipEntryTrackFile;

@Component(configurationPolicy = ConfigurationPolicy.REQUIRE)
public class DatabaseTrackPersistence implements ITrackPersistence {

	private String basedir;

	private boolean fullprocess;

	private DecimalFormat format = new DecimalFormat("#####000"); //$NON-NLS-1$

	private DecimalFormat fileFormat = new DecimalFormat("#######0"); //$NON-NLS-1$

	private Set<String> filterTrackIds;

	private Set<String> whitelistUsers;

	private Set<String> blacklistUsers;

	private DataSource uploadDataSource;

	private List<IDataWriter> dataWriters = new CopyOnWriteArrayList<>();

	public void activate(Map<String, Object> properties) {
		basedir = (String) properties.get("basedir");
		fullprocess = Boolean.valueOf((String) properties.get("fullprocess"));

		whitelistUsers = getValues("whitelistUsers", properties);
		blacklistUsers = getValues("blacklistUsers", properties);
		filterTrackIds = getValues("processTrackIds", properties);

	}

	@Override
	public void resetAnalyzedData() throws TrackPerssitenceException {
		try (Connection sourceConnection = uploadDataSource.getConnection()) {
			// should we reprocess data marked for reprocessing
			Set<String> reprocessTrackFiles = getTrackFilesToReprocess(sourceConnection);
			if (!reprocessTrackFiles.isEmpty()) {
				resetAnalyzedData(sourceConnection, reprocessTrackFiles);
			}

			// should we reset data for either the processTrackIds or everything
			// TODO: erase only whitelist / blacklist users
			if (fullprocess) { // $NON-NLS-1$ //$NON-NLS-2$
				resetAllAnalyzedData(sourceConnection);
			}
		} catch (SQLException | WriterException e) {
			throw new TrackPerssitenceException(e);
		}
	}

	@Override
	public void storePreprocessingStates(Collection<ITrackFile> trackFiles) throws TrackPerssitenceException {
		try (Connection sourceConnection = uploadDataSource.getConnection();
				Statement statement = sourceConnection.createStatement();
				PreparedStatement updateTrackFileStatement = sourceConnection.prepareStatement(
						"UPDATE user_tracks SET filetype=?, compression=?, upload_state=?WHERE track_id = ?");
				PreparedStatement setUploadStateStatement = sourceConnection
						.prepareStatement("UPDATE user_tracks SET upload_state= ? WHERE track_id = ?");) {
			// long i = 0;
			for (ITrackFile iTrackFile : trackFiles) {
				// i += 1L;
				net.sf.seesea.track.api.data.ProcessingState processingState = iTrackFile.getUploadState();
				if (processingState != null) {
					switch (processingState) {
					case PREPROCESSED:
						updateTrackFileStatement.setString(1, iTrackFile.getFileType());
						updateTrackFileStatement.setString(2, iTrackFile.getCompression().getMimeType());
						updateTrackFileStatement.setInt(3, processingState.ordinal());
						updateTrackFileStatement.setLong(4, iTrackFile.getTrackId());
						updateTrackFileStatement.addBatch();
						// if (i % 20 == 0) {
						// updateTrackFileStatement.executeBatch();
						// updateTrackFileStatement =
						// sourceConnection.prepareStatement("UPDATE user_tracks
						// SET
						// filetype=?, compression=?, upload_state=? WHERE
						// track_id
						// = ?"); //$NON-NLS-1$
						// }
						break;
					case FILE_CORRUPT:
					case FILE_CONTENT_UNKNOWN:
					case FILE_NODATA:
					case FILE_PROCESSED:
						setUploadStateStatement.setInt(1, processingState.ordinal());
						setUploadStateStatement.setLong(2, iTrackFile.getTrackId());
						// execute it right away since we expect this does not
						// happen very often
						setUploadStateStatement.execute();
						break;
					default:
						break;
					}
				} else {
					Logger.getLogger(getClass()).error(
							"Track has no processing state " + iTrackFile.getTrackId() + ":" + iTrackFile.toString());
				}
				// if (iTrackFile.getTrackFiles().size() == 1) {
				// // use the current track id for information
				// updateTrackFileStatement.setString(1,
				// iTrackFile.getFileType());
				// updateTrackFileStatement.setString(2,
				// iTrackFile.getCompression().getMimeType());
				// updateTrackFileStatement.setInt(3,
				// iTrackFile.getUploadState().ordinal());
				// updateTrackFileStatement.setLong(4, iTrackFile.getTrackId());
				// updateTrackFileStatement.addBatch();
				// } else
				if (iTrackFile.getTrackFiles().size() > 0) {
					for (ITrackFile track : iTrackFile.getTrackFiles()) {
						try (PreparedStatement createTrackFileStatement = sourceConnection.prepareStatement(
								"INSERT INTO user_tracks (track_id, user_name, file_ref, upload_state,  filetype, compression, containertrack) VALUES(nextval('user_tracks_track_id_seq'), ?, ?, ?, ?, ?, ?)")) {
							createTrackFileStatement.setString(1, track.getUsername());
							createTrackFileStatement.setString(2, ((IContainedTrackFile) track).getTrackQualifier());
							createTrackFileStatement.setInt(3, ProcessingState.PREPROCESSED.ordinal());
							createTrackFileStatement.setString(4, track.getFileType());
							createTrackFileStatement.setString(5, track.getCompression().getMimeType());
							createTrackFileStatement.setLong(6, iTrackFile.getTrackId());
							createTrackFileStatement.execute();
						}
					}
					// if (!iTrackFile.getTrackFiles().isEmpty()) {
					// createTrackFileStatement.executeBatch();
					// }
					// don't store format type for container track
					updateTrackFileStatement.setString(1, null);
					updateTrackFileStatement.setString(2, iTrackFile.getCompression().getMimeType());
					updateTrackFileStatement.setInt(3, ProcessingState.PREPROCESSED.ordinal());
					updateTrackFileStatement.setLong(4, iTrackFile.getTrackId());
					updateTrackFileStatement.addBatch();
				}
			}
			updateTrackFileStatement.executeBatch();
		} catch (SQLException e) {
			throw new TrackPerssitenceException("Failed to persist track preprocessing states", e);
		}

	}
	
	
	@Override
	public Map<String, List<ITrackFile>> getUser2PreprocessedTracks() throws TrackPerssitenceException {
		EnumSet<ProcessingState> preprocessed = EnumSet.of(ProcessingState.PREPROCESSED);
		return getUser2PostprocessTrackCluster(preprocessed);
	}

	@Override
	public Map<String, Map<String, List<ITrackFile>>> getUser2TimeClusteredTracks() throws TrackPerssitenceException {
		EnumSet<ProcessingState> preprocessed = EnumSet.of(ProcessingState.CLUSTERED);
		Map<String, List<ITrackFile>> user2PostprocessTrackCluster = getUser2PostprocessTrackCluster(preprocessed);
		Map<String, List<ITrackFile>> clusterId2TrackFile = new HashMap<>();
		for (Entry<String, List<ITrackFile>> user2TrackFile : user2PostprocessTrackCluster.entrySet()) {
			List<ITrackFile> trackFiles = user2TrackFile.getValue();
			for (ITrackFile trackFile : trackFiles) {
				String clusterUUID = trackFile.getClusterUUID();
				List<ITrackFile> trackFilesX = clusterId2TrackFile.get(clusterUUID);
				if(trackFilesX == null) {
					trackFilesX = new ArrayList<>();
				}
				
			}
			
		}
		return null;
	}

	@Override
	public Map<String, List<ITrackFile>> getUser2NoTimeTracksTracks() throws TrackPerssitenceException {
		return null;
	}


	public Map<String, List<ITrackFile>> getUser2PostprocessTrackCluster(Set<ProcessingState> processingStates) throws TrackPerssitenceException {
		Map<String, List<ITrackFile>> user2Tracks = new HashMap<String, List<ITrackFile>>();
		DecimalFormat format = new DecimalFormat("000"); //$NON-NLS-1$

		try (Connection connection = uploadDataSource.getConnection();
				Statement usersStatement = connection.createStatement();
				ResultSet userSet = usersStatement.executeQuery(
						"SELECT user_name FROM user_profiles WHERE user_name IN (SELECT DISTINCT user_name FROM user_tracks)")) {
			while (userSet.next()) {
				List<ITrackFile> orderedFiles = new ArrayList<ITrackFile>();
				String user = userSet.getString(1);
				String sha1Username = encryptUser(user);
				Map<Long, VesselConfiguration> vesselConfigurationList = new HashMap<>();
				try (PreparedStatement getUsersVesselConfigurations = connection.prepareStatement(
						"SELECT id, name, description FROM vesselconfiguration WHERE user_name = ?");) {
					getUsersVesselConfigurations.setString(1, user);
					try (ResultSet vesselConfigurations = getUsersVesselConfigurations.executeQuery()) {
						while (vesselConfigurations.next()) {
							VesselConfiguration vesselConfiguration = new VesselConfiguration();
							long id = vesselConfigurations.getLong(1);

							try (Statement gpsStatement = connection.createStatement();
									ResultSet gpsOffsets = gpsStatement.executeQuery(
											"SELECT vesselconfigid, sensorid, x,y,z FROM sbassensor WHERE vesselconfigid = '"
													+ id + "'")) {
								while (gpsOffsets.next()) {
									Point3D offset = new Point3D(gpsOffsets.getDouble(3), gpsOffsets.getDouble(4),
											gpsOffsets.getDouble(5));
									vesselConfiguration.getGpsSensorOffsets().put(gpsOffsets.getString(2), offset);
								}
							}

							try (Statement depthStatement = connection.createStatement();
									ResultSet depthOffsets = depthStatement.executeQuery(
											"SELECT vesselconfigid, sensorid, x,y,z,offsetkeel,offsettype FROM depthsensor WHERE vesselconfigid = '" //$NON-NLS-1$
													+ id + "'")) { //$NON-NLS-1$
								while (depthOffsets.next()) {
									DepthSensor offset = new DepthSensor(depthOffsets.getDouble(3),
											depthOffsets.getDouble(4), depthOffsets.getDouble(5),
											depthOffsets.getDouble(6), depthOffsets.getString(7));
									vesselConfiguration.getDepthSensorOffsets().put(depthOffsets.getString(2), offset);
								}
							}
							vesselConfigurationList.put(id, vesselConfiguration);
						}
					}

					ResultSet singleUserTrackFiles = null;
					PreparedStatement userTracksStatement = connection.prepareStatement(
							"SELECT track_id, filetype, compression, file_ref, vesselconfigid, upload_state, clusteruuid, clusterseq FROM user_tracks  " //$NON-NLS-1$
									+ "WHERE (user_tracks.user_name = ? OR user_tracks.user_name = ?) "
									+ "AND upload_state = ANY(?) " + //$NON-NLS-1$ //$NON-NLS-2$
									"AND containertrack IS NULL " + //$NON-NLS-1$
									"AND track_id NOT IN (SELECT containertrack FROM user_tracks WHERE containertrack IS NOT NULL)"); //$NON-NLS-1$
					userTracksStatement.setString(1, user);
					userTracksStatement.setString(2, sha1Username);
					
					List<Integer> stateInts = new ArrayList<>(processingStates.size());
					for (ProcessingState processingState : processingStates) {
						stateInts.add(processingState.getIndex());
					}
					userTracksStatement.setArray(3, connection.createArrayOf("integer", processingStates.toArray()));
					singleUserTrackFiles = userTracksStatement.executeQuery();

					while (singleUserTrackFiles.next()) {
						long id = singleUserTrackFiles.getLong("track_id"); //$NON-NLS-1$
						String trackFile = basedir + "/" //$NON-NLS-1$
								+ format.format((id / 100) * 100) + "/" + id + ".dat"; //$NON-NLS-1$ //$NON-NLS-2$
						String compression = singleUserTrackFiles.getString("compression"); //$NON-NLS-1$
						String fileType = singleUserTrackFiles.getString("filetype"); //$NON-NLS-1$
						Long vesselConfigId = singleUserTrackFiles.getLong("vesselconfigid"); //$NON-NLS-1$
						ProcessingState processingState = ProcessingState
								.forCode(singleUserTrackFiles.getInt("upload_state"));

						if (compression == null) {
							SimpleTrackFile trackFileX = new SimpleTrackFile();
							trackFileX.setTrackId(id);
							trackFileX.setFileReference(trackFile);
							trackFileX.setCompression(CompressionType.NONE);
							trackFileX.setFileType(fileType);
							trackFileX.setName(singleUserTrackFiles.getString("file_ref"));
							trackFileX.setVesselConfiguration(vesselConfigurationList.get(vesselConfigId));
							trackFileX.setUploadState(processingState);
							orderedFiles.add(trackFileX);
						} else {
							CompressionType compressionType = CompressionType.getCompressionType(compression);
							switch (compressionType) {
							case GZ:
								GzipTrackFile trackFileX = new GzipTrackFile();
								trackFileX.setTrackId(id);
								trackFileX.setFileReference(trackFile);
								trackFileX.setCompression(compressionType);
								trackFileX.setFileType(fileType);
								trackFileX.setName(singleUserTrackFiles.getString("file_ref"));
								trackFileX.setVesselConfiguration(vesselConfigurationList.get(vesselConfigId));
								trackFileX.setUploadState(processingState);
								orderedFiles.add(trackFileX);
								break;
							case ZIP:
								try {

									ZipFile zipFile = new ZipFile(trackFile);
									ZipEntryTrackFile trackFileY = new ZipEntryTrackFile(zipFile,
											zipFile.entries().nextElement());
									trackFileY.setTrackId(id);
									trackFileY.setCompression(compressionType);
									trackFileY.setFileType(fileType);
									trackFileY.setName(singleUserTrackFiles.getString("file_ref"));
									trackFileY.setVesselConfiguration(vesselConfigurationList.get(vesselConfigId));
									trackFileY.setUploadState(processingState);
									List<IContainedTrackFile> unzippedFiles2 = new ArrayList<>();
									for (ITrackFileDecompressor trackFileDecompressor : trackFileDecompressors) {
										long id2 = trackFileY.getTrackId();
										String trackFile2 = MessageFormat.format("{0}/{1}/{2}.dat", basedir, //$NON-NLS-1$
												format.format((id2 / 100) * 100),
												fileFormat.format(id));
										File file = new File(trackFile2);
										unzippedFiles2 = trackFileDecompressor.getTracks(compressionType, file);
										orderedFiles.addAll(unzippedFiles2);
									}
									if (unzippedFiles2.size() == 0) {
										orderedFiles.add(trackFileY);
									}

								} catch (IllegalArgumentException e) {
									Logger.getLogger(getClass()).warn("Failed to determine charset for track id:" + id,
											e);
									ZipFile zipFile = new ZipFile(trackFile, Charset.forName("ISO_8859_1")); //$NON-NLS-1$
									ZipEntryTrackFile trackFileY = new ZipEntryTrackFile(zipFile,
											zipFile.entries().nextElement());
									trackFileY.setTrackId(id);
									trackFileY.setCompression(compressionType);
									trackFileY.setFileType(fileType);
									trackFileY.setName(singleUserTrackFiles.getString("file_ref"));
									trackFileY.setVesselConfiguration(vesselConfigurationList.get(vesselConfigId));
									trackFileY.setUploadState(processingState);
									List<IContainedTrackFile> unzippedFiles2 = new ArrayList<>();
									for (ITrackFileDecompressor trackFileDecompressor : trackFileDecompressors) {
										long id2 = trackFileY.getTrackId();
										String trackFile2 = MessageFormat.format("{0}/{1}/{2}.dat", basedir, //$NON-NLS-1$
												format.format((id2 / 100) * 100),
												fileFormat.format(id));
										File file = new File(trackFile2);
										unzippedFiles2 = trackFileDecompressor.getTracks(compressionType, file);
										orderedFiles.addAll(unzippedFiles2);
									}
									if (unzippedFiles2.size() == 0) {
										orderedFiles.add(trackFileY);
									}
								}
							default:
								break;
							}
						}
					}

					PreparedStatement containerTrackUserStatement = connection.prepareStatement(
							"SELECT track_id, filetype, compression, vesselconfigid, clusteruuid, clusterseq FROM user_tracks " + //$NON-NLS-1$
									"WHERE (user_name = ? OR user_name = ?) " + //$NON-NLS-1$ //$NON-NLS-2$
																				// //$NON-NLS-3$
									"AND upload_state = ANY(?) " + //$NON-NLS-1$ //$NON-NLS-2$
									"AND containertrack IS NULL " + //$NON-NLS-1$
									"AND track_id IN (SELECT containertrack FROM user_tracks WHERE containertrack IS NOT NULL)"); //$NON-NLS-1$
					containerTrackUserStatement.setString(1, user);
					containerTrackUserStatement.setString(2, sha1Username);
					containerTrackUserStatement.setArray(3, connection.createArrayOf("integer", processingStates.toArray()));
					ResultSet mutliTrackFiles = containerTrackUserStatement.executeQuery();
					while (mutliTrackFiles.next()) {
						long id = mutliTrackFiles.getLong("track_id"); //$NON-NLS-1$
						String trackFile = basedir + "/" //$NON-NLS-1$
								+ format.format((id / 100) * 100) + "/" + id + ".dat"; //$NON-NLS-1$ //$NON-NLS-2$
						String compression = mutliTrackFiles.getString("compression"); //$NON-NLS-1$
						Long vesselConfigId = mutliTrackFiles.getLong("vesselconfigid"); //$NON-NLS-1$
						PreparedStatement compressedFilesStatement = connection
								.prepareStatement("SELECT track_id, filetype, compression, file_ref FROM user_tracks " + //$NON-NLS-1$
										"WHERE (user_name = ? OR user_name = ?) " + //$NON-NLS-1$ //$NON-NLS-2$
																					// //$NON-NLS-3$
										"AND upload_state = ANY(?) " + //$NON-NLS-1$ //$NON-NLS-2$
										"AND containertrack = ? " + //$NON-NLS-1$ //$NON-NLS-2$
										"ORDER BY track_id"); //$NON-NLS-1$
						compressedFilesStatement.setString(1, user);
						compressedFilesStatement.setString(2, sha1Username);
						compressedFilesStatement.setArray(3, connection.createArrayOf("integer", processingStates.toArray()));
						compressedFilesStatement.setLong(4, id);
						ResultSet compressedTracks = compressedFilesStatement.executeQuery();
						while (compressedTracks.next()) {
							String uncompressedTrackFile = compressedTracks.getString("file_ref"); //$NON-NLS-1$
							String mimeType = compressedTracks.getString("filetype"); //$NON-NLS-1$
							CompressionType compressionType = CompressionType.getCompressionType(compression);
							switch (compressionType) {
							case ZIP:
								try {
									ZipFile zipFile = new ZipFile(trackFile);
									ZipEntry entry = zipFile.getEntry(uncompressedTrackFile);
									if (entry != null) {
										ZipEntryTrackFile zippedTrack = new ZipEntryTrackFile(zipFile, entry);
										zippedTrack.setFileType(mimeType);
										zippedTrack.setTrackId(compressedTracks.getLong("track_id"));
										zippedTrack.setName(compressedTracks.getString("file_ref"));
										zippedTrack.setVesselConfiguration(vesselConfigurationList.get(vesselConfigId));

										orderedFiles.add(zippedTrack);
									} else {
										Logger.getLogger(getClass())
												.warn("Zip entry could not be found. Potentially wrong zip encoding");
									}
								} catch (IllegalArgumentException e) {
									Logger.getLogger(getClass()).warn("Failed to determine charset for track id:" + id,
											e);
									ZipFile zipFile = new ZipFile(trackFile, Charset.forName("ISO_8859_1")); //$NON-NLS-1$
									ZipEntry entry = zipFile.getEntry(uncompressedTrackFile);
									if (entry != null) {
										ZipEntryTrackFile zippedTrack = new ZipEntryTrackFile(zipFile, entry);
										zippedTrack.setFileType(mimeType);
										zippedTrack.setTrackId(compressedTracks.getLong("track_id"));
										zippedTrack.setName(compressedTracks.getString("file_ref"));
										orderedFiles.add(zippedTrack);
									} else {
										Logger.getLogger(getClass())
												.warn("Zip entry could not be found. Potentially wrong zip encoding");
									}
								}
								break;
							case GZ:
								GzipTrackFile gzipTrackFile = new GzipTrackFile();
								gzipTrackFile.setCompression(compressionType);
								gzipTrackFile.setFileType(mimeType);
								gzipTrackFile.setTrackId(compressedTracks.getLong("track_id"));
								gzipTrackFile.setName(compressedTracks.getString("file_ref"));
								orderedFiles.add(gzipTrackFile);
								break;
							case SEVEN_ZIP:
								break;
							case TAR:
								break;
							case TARGZ:
								break;
							case RAR:
								break;

							default:
								break;
							}
						}

					}
				}
				if (!orderedFiles.isEmpty()) {
					user2Tracks.put(user, orderedFiles);
				}
			}
		} catch (SQLException | IOException e1) {
			throw new TrackPerssitenceException(e1);
		}

		return user2Tracks;
	}

	// FIXME readd whitelist and blacklist users
	@Override
	public List<ITrackFile> getTrackFiles2Process() throws TrackPerssitenceException {
		List<ITrackFile> tracks = new ArrayList<ITrackFile>();
		try (Connection sourceConnection = uploadDataSource.getConnection();
				Statement statement = sourceConnection.createStatement();
				ResultSet userTrackFiles = statement.executeQuery(
						"SELECT track_id, user_name FROM user_tracks WHERE upload_state = '1' AND filetype IS NULL AND compression is NULL");) {
			while (userTrackFiles.next()) {
				SimpleTrackFile simpleTrackFile = new SimpleTrackFile();
				long id = userTrackFiles.getLong("track_id"); //$NON-NLS-1$
				simpleTrackFile.setTrackId(id);

				// FIXME: filter in SQL query
				if (filterTrackIds != null && !filterTrackIds.isEmpty()
						&& !filterTrackIds.contains(Long.toString(id))) {
					continue;
				}
				String username = userTrackFiles.getString("user_name"); //$NON-NLS-1$
				if (!whitelistUsers.isEmpty() && !whitelistUsers.contains(username)) {
					continue;
				}
				if (blacklistUsers.contains(username)) {
					continue;
				}
				simpleTrackFile.setUsername(username);
				String trackFile = MessageFormat.format("{0}/{1}/{2}.dat", basedir, format.format((id / 100) * 100), //$NON-NLS-1$
						fileFormat.format(id));
				simpleTrackFile.setFileReference(trackFile);
				tracks.add(simpleTrackFile);
			}
			return tracks;
		} catch (SQLException e) {
			throw new TrackPerssitenceException("Failed to retrieve user tracks", e);
		}
	}

	private Set<String> getTrackFilesToReprocess(Connection connection) throws TrackPerssitenceException {
		Set<String> trackIDs = new HashSet<String>();
		try (Statement getTracksToReprocessStatement = connection.createStatement();
				ResultSet resultSet = getTracksToReprocessStatement
						.executeQuery("SELECT track_id, user_name FROM user_tracks WHERE upload_state = '"
								+ ProcessingState.REPROCESS.ordinal() + "'")) {
			while (resultSet.next()) {
				Long trackId = resultSet.getLong(1);
				String user = resultSet.getString(2);
				if (blacklistUsers.contains(user)) {
					continue;
				}
				if (!whitelistUsers.isEmpty() && !whitelistUsers.contains(user)) {
					continue;
				}

				trackIDs.add(trackId.toString());
			}
		} catch (SQLException e) {
			throw new TrackPerssitenceException("Failed to query tracks to reprocess", e);
		}
		return trackIDs;
	}

	private void resetAllAnalyzedData(Connection connection) throws SQLException, WriterException {
		try (PreparedStatement updateTrackFileStatement = connection.prepareStatement(
				"UPDATE user_tracks SET filetype=?, compression=?, upload_state=? WHERE upload_state != '" //$NON-NLS-1$
						+ ProcessingState.UPLOAD_INCOMPLETE.ordinal() + "'");
				PreparedStatement statement = connection
						.prepareStatement("DELETE FROM user_tracks WHERE containertrack IS NOT NULL")) {

			updateTrackFileStatement.setString(1, null);
			updateTrackFileStatement.setString(2, null);
			updateTrackFileStatement.setInt(3, ProcessingState.UPLOAD_COMPLETE.ordinal());
			updateTrackFileStatement.execute();

			// delete derived tracks from zip files
			statement.execute(); // $NON-NLS-1$
			for (IDataWriter dataWriter : dataWriters) {
				dataWriter.clearAllOutput();
			}
		}
	}

	/**
	 * cleans the existing database. This may either happen selective given a
	 * certain track id or by cleaning every analyzed piece of data.
	 * 
	 * @param connection
	 * @param processTrackIds
	 * @param filterProperties
	 * @throws SQLException
	 * @throws WriterException
	 */
	private void resetAnalyzedData(Connection connection, Set<String> processTrackIds)
			throws SQLException, WriterException {

		// FIXME: delete filter data

		// a particular set of track ids is to be processed
		if (!processTrackIds.isEmpty()) {
			// Statement statement = connection.createStatement();

			// ResultSet resultSet = statement
			// .executeQuery("SELECT track_id FROM user_tracks WHERE
			// upload_state != '" + ProcessingState.UPLOAD_INCOMPLETE.ordinal()
			// + "'"); //$NON-NLS-1$ //$NON-NLS-2$
			for (String trackIDString : processTrackIds) {
				// while (resultSet.next()) {
				Long trackId = Long.parseLong(trackIDString);
				try (PreparedStatement deletestatement = connection
						.prepareStatement("DELETE FROM user_tracks WHERE containertrack = ?");
						PreparedStatement containedTracks = connection
								.prepareStatement("SELECT track_id FROM user_tracks WHERE containertrack = ?")) {
					containedTracks.setLong(1, trackId);
					try (ResultSet containedTracksIds = containedTracks.executeQuery()) {
						while (containedTracksIds.next()) {
							long containedId = containedTracksIds.getLong(1);
							for (IDataWriter dataWriter : dataWriters) {
								dataWriter.clearOutput(containedId);
							}
						}
					}
					// delete derived tracks from zip files
					deletestatement.setLong(1, trackId);
					deletestatement.execute();
					for (IDataWriter dataWriter : dataWriters) {
						dataWriter.clearOutput(trackId);
					}
				}

				PreparedStatement updateTrackFileStatement = connection.prepareStatement(
						"UPDATE user_tracks SET filetype=?, compression=?, upload_state=? WHERE track_id=?");
				updateTrackFileStatement.setString(1, null);
				updateTrackFileStatement.setString(2, null);
				updateTrackFileStatement.setInt(3, ProcessingState.UPLOAD_COMPLETE.ordinal());
				updateTrackFileStatement.setLong(4, trackId);
				updateTrackFileStatement.execute();
			}

		} else {
			// reset all track files that have been completed

		}
	}

	// private void cleanDeadData(Connection connection) {
	// Statement statement;
	// try {
	// statement = connection.createStatement();
	// statement.execute("DELETE FROM user_tracks WHERE file_ref IS NULL AND
	// upload_state != 0"); //$NON-NLS-1$
	// } catch (SQLException e) {
	// Logger.getLogger(getClass()).error("Failed to delete entries with no file
	// reference", e); //$NON-NLS-1$
	// }
	// }

	@Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.DYNAMIC, target = "(db=userData)")
	public synchronized void bindDepthConnection(DataSource dataSource) {
		this.uploadDataSource = dataSource;
	}

	public synchronized void unbindDepthConnection(DataSource connection) {
		this.uploadDataSource = null;
	}

	@Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
	public void bindDataWriter(IDataWriter dataWriter) {
		dataWriters.add(dataWriter);
	}

	public void unbindDataWriter(IDataWriter dataWriter) {
		dataWriters.remove(dataWriter);
	}

	private Set<String> getValues(String usertype, Map<String, Object> _properties) {
		String processUsers = (String) _properties.get(usertype);
		Set<String> whitelistUsers = new HashSet<String>();
		if (processUsers != null && !processUsers.isEmpty()) {
			String[] users = processUsers.split(","); //$NON-NLS-1$
			for (String user : users) {
				whitelistUsers.add(user.trim());
			}
		}
		return whitelistUsers;
	}

	private String encryptUser(String password) {
		String sha1 = ""; //$NON-NLS-1$
		try {
			MessageDigest crypt = MessageDigest.getInstance("SHA-1"); //$NON-NLS-1$
			crypt.reset();
			crypt.update(password.getBytes("UTF-8")); //$NON-NLS-1$
			sha1 = byteToHex(crypt.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return sha1;
	}

	private String byteToHex(final byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b); //$NON-NLS-1$
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}

	@Override
	public List<CompositeMeasurement> getNearByPoints() {
		// TODO Auto-generated method stub
		return null;
	}

	private List<ITrackFileDecompressor> trackFileDecompressors = new CopyOnWriteArrayList<ITrackFileDecompressor>();;

	@Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
	public void bindTrackFileDecompressors(ITrackFileDecompressor trackFileDecompressor) {
		trackFileDecompressors.add(trackFileDecompressor);
	}

	public void unbindTrackFileDecompressors(ITrackFileDecompressor trackFileDecompressor) {
		trackFileDecompressors.remove(trackFileDecompressor);
	}

	@Override
	public void storeTrackCluster(Collection<ITrackFile> trackFiles) throws TrackPerssitenceException {
		try (Connection sourceConnection = uploadDataSource.getConnection();
				Statement statement = sourceConnection.createStatement();
				PreparedStatement updateTrackFileStatement = sourceConnection.prepareStatement(
						"UPDATE user_tracks SET clusteruuid=?, clustersequence=? WHERE track_id = ?");) {
			for (ITrackFile iTrackFile : trackFiles) {
				// i += 1L;
				net.sf.seesea.track.api.data.ProcessingState processingState = iTrackFile.getUploadState();
				if (processingState != null) {
					switch (processingState) {
					case PREPROCESSED:

						updateTrackFileStatement.setString(1, iTrackFile.getClusterUUID());
						updateTrackFileStatement.setInt(2, iTrackFile.getSequenceNumber());
						updateTrackFileStatement.setLong(3, iTrackFile.getTrackId());
						updateTrackFileStatement.addBatch();
						break;
					default:
						break;
					}
				} else {
					Logger.getLogger(getClass()).error(
							"Track has no processing state " + iTrackFile.getTrackId() + ":" + iTrackFile.toString());
				}
			}
			updateTrackFileStatement.executeBatch();
		} catch (SQLException e) {
			throw new TrackPerssitenceException("Failed to persist track preprocessing states", e);
		}
	}

}