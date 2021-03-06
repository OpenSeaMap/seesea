
/**
Copyright (c) 2013-2015, Jens Kübler
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
package net.sf.seesea.data.io.postgis;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

/**
 * Utility class to dump the generated lowest astronomical tide values from the database 
 *
 */
public class LatDump {

	public void x(Connection connection) throws SQLException, IOException {
		Map<String,Map<String, Float>> contents = new HashMap<String, Map<String,Float>>();
		Statement statement = connection.createStatement();
		ResultSet execute = statement.executeQuery("SELECT ST_X(geom),ST_Y(geom),dbs from (Select (ST_DumpPoints(the_geom)).*,dbs FROM LAT ) as g");
		while(execute.next()) {
			String lat = execute.getString(2);
			String lon = execute.getString(1);
			Float m = execute.getFloat(3);
			Map<String, Float> lats = contents.get(lat);
			if(lats == null) {
				lats = new HashMap<String, Float>();
				contents.put(lat, lats);
			}
			lats.put(lon, m);
		}
		
		try(FileOutputStream fileOutputStream = new FileOutputStream("C:/pv4/LAT.ser");
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
			for(int i = -1440 ; i < 1440; i++ ) {
				for(int j = 720; j > -720 ; j--) {
					NumberFormat format = NumberFormat.getInstance(Locale.ENGLISH);
					float lon = 0.125f * i;
					float lat = 0.125f * j;
					String lonString = format.format(lon);
					String latString = format.format(lat);
					System.out.println(latString + ":" + lonString);
					Map<String, Float> lats = contents.get(latString);
					if(lats == null) {
						objectOutputStream.writeFloat(0.0f);
					} else {
						Float float1 = lats.get(lonString);
						if(float1 == null) {
							objectOutputStream.writeFloat(0.0f);
						} else {
							objectOutputStream.writeFloat(float1);
						}
					}
				}
				objectOutputStream.flush();
			}
			
		}
		
	}
	
	public static void main(String args[]) throws FileNotFoundException, IOException, SQLException {
		Properties properties = new Properties();
		properties.load(new FileInputStream("config.cfg"));
		Connection connection = PostgresConnectionFactory.getDBConnection(properties, "gistest"); //$NON-NLS-1$
		LatDump x = new LatDump();
		x.x(connection);
	}

	
}
