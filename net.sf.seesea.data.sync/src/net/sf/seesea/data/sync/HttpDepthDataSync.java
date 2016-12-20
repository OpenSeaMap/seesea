/**
Copyright (c) 2016, Jens Kübler
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in the
      documentation and/or other materials provided with the distribution.
    * Neither the name of the <organization> nor the
      names of its contributors may be used to endorse or promote products
      derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */
package net.sf.seesea.data.sync;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.sql.DataSource;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.moxy.json.MoxyJsonFeature;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import net.sf.seesea.data.io.postgis.ScriptRunner;
import net.sf.seesea.data.sync.api.IDepthDataSync;

@Component
public class HttpDepthDataSync implements IDepthDataSync {

	/** user for api access */
	private String user;
	/** password for api access */
	private String password;
	/** do certificate check */
	private Boolean checkSSLCertificate;
	/** url for API access */
	private String apiURL;

	/** target storage location */
	private File storageLocation;

	/** limit download to certain users. not used */
	private List<String> users;

	/** lowest track id */
	private Long lowerBound;
	/** highest track id */
	private Long upperBound;
	private DataSource uploadDataSource;

	@Override
	public boolean downloadFiles() {

		SSLContext sslcontext;
		try {
			Client client;
			if (!checkSSLCertificate) {
				sslcontext = SSLContext.getInstance("TLS");
				sslcontext.init(null, new TrustManager[] { new X509TrustManager() {
					public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
					}

					public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
					}

					public X509Certificate[] getAcceptedIssuers() {
						return new X509Certificate[0];
					}

				} }, new java.security.SecureRandom());
				client = ClientBuilder.newBuilder().sslContext(sslcontext).hostnameVerifier((s1, s2) -> true).build();
			} else {
				client = ClientBuilder.newClient();
			}

			HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic(user, password);
			client.register(MoxyJsonFeature.class);
			client.register(feature);
			client.property(ClientProperties.CONNECT_TIMEOUT, 10000);
			LoggingFilter loggingFilter = new LoggingFilter();
			client.register(loggingFilter);

			WebTarget apiURLPath = client.target(apiURL);
			WebTarget fullApiPath = apiURLPath;
			Builder trackRequest = fullApiPath.request(MediaType.APPLICATION_JSON);
			trackRequest.property(ClientProperties.READ_TIMEOUT, 600000); // ten
																			// minute
																			// timeout
			Response response = trackRequest.get();
			if (response.getStatus() == 200) {
				List<Track> readEntity = response.readEntity(new GenericType<List<Track>>() {
				});
				for (Track track : readEntity) {
					if (track.containertrack == 0) {
						if ((lowerBound == null && upperBound == null)
								|| (lowerBound >= track.id && upperBound <= track.id)) {
							Logger.getLogger(getClass()).info("Downloading track id " + track.id);
							File file = getFile(storageLocation, track.id);
							if (!file.exists() || file.length() != 0L) {
								try {
									WebTarget downloadPath = fullApiPath.path(Long.toString(track.id)).path("download");
									Builder request = downloadPath.request();
									request.property(ClientProperties.READ_TIMEOUT, 10000);
									request.property(ClientProperties.REQUEST_ENTITY_PROCESSING, "CHUNKED");
									Response downloadResponse = request.get();
									InputStream is = downloadResponse.readEntity(InputStream.class);
									FileOutputStream fos = new FileOutputStream(file);
									IOUtils.copy(is, fos);
									fos.flush();
									fos.close();
								} catch (ProcessingException e) {
									Logger.getLogger(getClass())
											.error("Failed to download file " + track.containertrack, e);
								}
							}
						}

					}
				}

				return true;
			}

		} catch (NoSuchAlgorithmException | KeyManagementException |

		IOException e)

		{
			Logger.getLogger(getClass()).error("Failed to synchronize files", e);
		}

		return false;

	}

	@Activate
	public void activate(Map<String, Object> config) throws IOException {
		user = (String) config.get("user");
		password = (String) config.get("password");
		checkSSLCertificate = Boolean.valueOf("checkSSLCertificate");
		apiURL = (String) config.get("apiURL"); // "https://depth.openseamap.org:8443";
//		URL url = new URL(apiURL);

		storageLocation = new File((String) config.get("storageLocation"));
		if (!storageLocation.exists()) {
			boolean mkdirs = storageLocation.mkdirs();
			if (!mkdirs) {
				throw new IOException("Failed to create directory:" + storageLocation.getAbsolutePath()); //$NON-NLS-1$
			}
		}
		String trackrange = (String) config.get("trackRange");
		if (trackrange != null && trackrange.trim().length() > 0) {
			List<String> ranges = Arrays.asList(trackrange.split(","));
			lowerBound = Long.valueOf(ranges.get(0));
			upperBound = Long.valueOf(ranges.get(1));
		}

		String usersX = (String) config.get("users");
		if (usersX != null && usersX.trim().length() > 0) {
			users = Arrays.asList(usersX.split(","));
		} else {
			users = Collections.<String> emptyList();
		}
	}

	/**
	 * 
	 * @param trackId
	 * @return the file to create or null if the directory could not be created
	 * @throws IOException
	 */
	private File getFile(File storageLocation, Long trackId) throws IOException {
		Long dirNumber = trackId / 100L * 100L;
		// String fileDirectoryConfig = ".";
		File fileDirectory = new File(storageLocation + File.separator + dirNumber.toString());
		String trackIDString = trackId.toString();
		if (!fileDirectory.exists()) {
			boolean mkdirs = fileDirectory.mkdirs();
			if (!mkdirs) {
				throw new IOException("Failed to create directory" + fileDirectory.getAbsolutePath()); //$NON-NLS-1$
			}
		}
		if (fileDirectory.exists()) {
			return new File(fileDirectory, trackIDString + ".dat");
		}
		throw new IOException("Failed to create directory" + fileDirectory.getAbsolutePath()); //$NON-NLS-1$
	}

	public void downloadSQL() {
		File sqlFile = new File(storageLocation, "dumpAll.sql.gz");

		try {
			URL u = new URL("http://depth.openseamap.org/dumpAll.sql.gz");
			try (InputStream is = u.openStream()) {
				FileOutputStream fos = new FileOutputStream(sqlFile);
				IOUtils.copy(is, fos);
				fos.flush();
				fos.close();
			} catch (IOException ioe) {
				Logger.getLogger(getClass()).error("Failed to read stream", ioe);
			}
		} catch (MalformedURLException mue) {
			Logger.getLogger(getClass()).error("Failed to parse URL", mue);
		}
		try(Connection connection = uploadDataSource.getConnection()) {
			try(GZIPInputStream gzipInputStream = new GZIPInputStream(new FileInputStream(sqlFile))) {
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(gzipInputStream));
				ScriptRunner runner = new ScriptRunner(connection, true, false);
				runner.runScript(bufferedReader);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	@Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.DYNAMIC, target = "(db=userData)")
	public synchronized void bindDepthConnection(DataSource dataSource) {
		this.uploadDataSource = dataSource;
	}

	public synchronized void unbindDepthConnection(DataSource connection) {
		this.uploadDataSource = null;
	}


}
