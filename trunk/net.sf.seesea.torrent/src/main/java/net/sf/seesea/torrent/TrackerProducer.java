package net.sf.seesea.torrent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

import com.turn.ttorrent.common.Torrent;
import com.turn.ttorrent.tracker.TrackedTorrent;
import com.turn.ttorrent.tracker.Tracker;

/**
 * Hello world!
 *
 */
public class TrackerProducer {
	public static void main(String[] args)
			throws NoSuchAlgorithmException, InterruptedException, IOException, URISyntaxException {

		String dir = args[0];
		InetSocketAddress externalServer = new InetSocketAddress("5adbwbq4feu2urke.myfritz.net", 37101);

		final URL url = new URL("http", externalServer.getAddress().getCanonicalHostName(), externalServer.getPort(),
				Tracker.ANNOUNCE_URL);
		InetSocketAddress torrentHost = new InetSocketAddress("192.168.178.45", 37101);
		final Tracker tracker = new Tracker(torrentHost);

		String depth = dir + "/uploads";
		final String torrentDir = dir + "/torrent";
		File file = new File(torrentDir);
		if (file.exists()) {
			file.mkdirs();
		}

			try {
				Files.list(Paths.get(depth)).parallel().filter(new Predicate<Path>() {

					@Override
					public boolean test(Path t) {
						return t.toFile().isDirectory();
					}
				}).forEach(new Consumer<Path>() {

					@Override
					public void accept(Path t) {
						try {
							File[] listFiles = t.toFile().listFiles();
							if (listFiles.length > 0) {
								Torrent torrent = Torrent.create(t.toFile(), Arrays.asList(t.toFile().listFiles()),
										url.toURI(), "OpenSeaMap Depth Project");
								File torrentFile = new File(
										torrentDir + File.separator + t.toFile().getName() + ".torrent");
								if (!torrentFile.exists()) {
									try (FileOutputStream fos = new FileOutputStream(torrentFile)) {
										torrent.save(fos);
										fos.close();
									}
								}
								System.out.println("Announcing:" + torrentFile);
								tracker.announce(TrackedTorrent.load(torrentFile));
							}
						} catch (NoSuchAlgorithmException | IOException | InterruptedException | URISyntaxException e) {
							e.printStackTrace();
						}
					}
				});
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		tracker.start();

	}
}
