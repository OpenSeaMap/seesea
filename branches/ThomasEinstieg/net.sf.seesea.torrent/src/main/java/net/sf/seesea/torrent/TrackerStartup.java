package net.sf.seesea.torrent;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.function.Consumer;
import java.util.function.Predicate;

import com.turn.ttorrent.tracker.TrackedTorrent;
import com.turn.ttorrent.tracker.Tracker;

public class TrackerStartup {

	public static void main(String[] args) throws IOException {

		String dir = args[0];
		final String torrentDir = dir + "/torrent";

		InetSocketAddress torrentHost = new InetSocketAddress("192.168.178.50", 37101);
		final Tracker tracker = new Tracker(torrentHost);

		try {
			Files.list(Paths.get(torrentDir)).parallel().filter(new Predicate<Path>() {

				@Override
				public boolean test(Path t) {
					return t.toFile().getName().endsWith(".torrent");
				}
			}).forEach(new Consumer<Path>() {

				@Override
				public void accept(Path t) {
					try {
						if(t.toFile().length() > 0) {
							tracker.announce(TrackedTorrent.load(t.toFile()));
						}
					} catch (NoSuchAlgorithmException | IOException e) {
						// TODO Auto-generated catch block
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
