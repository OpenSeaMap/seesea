package net.sf.seesea.data.io.postgis;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.easymock.EasyMock;
import org.junit.Test;

import net.sf.seesea.data.io.WriterException;
import net.sf.seesea.model.core.geo.Depth;
import net.sf.seesea.model.core.geo.GeoFactory;
import net.sf.seesea.model.core.geo.Latitude;
import net.sf.seesea.model.core.geo.Longitude;
import net.sf.seesea.model.core.geo.MeasuredPosition3D;
import net.sf.seesea.model.core.geo.RelativeDepthMeasurementPosition;
import net.sf.seesea.model.core.physx.Measurement;

public class PostInsertGISWriterTest {

	@Test
	public void testWriteSingleData() throws SQLException, WriterException {
		PostInsertGISWriter postInsertGISWriter = new PostInsertGISWriter();
		
		List<String> tables = new ArrayList<String>();
		tables.add("table1");
		tables.add("table2");
		tables.add("table3");
		tables.add("table4");
		Map<String, Object> properties = new HashMap<String,Object>();
		properties.put("outputTables", "table1,table2,table3,table4");

		PreparedStatement statement = EasyMock.createNiceMock(PreparedStatement.class);
		int [] result = new int[] {1};
		EasyMock.expect(statement.executeBatch()).andReturn(result);
		EasyMock.expectLastCall().anyTimes();
		EasyMock.replay(statement);

		Connection connection = EasyMock.createNiceMock(Connection.class);
		EasyMock.expect(connection.prepareStatement(EasyMock.anyString())).andReturn(statement);
		EasyMock.expectLastCall().anyTimes();
		EasyMock.expect(connection.createStatement()).andReturn(statement);
		EasyMock.expectLastCall().anyTimes();
		EasyMock.replay(connection);
		
		DataSource dataSource = EasyMock.createNiceMock(DataSource.class);
		EasyMock.expect(dataSource.getConnection()).andReturn(connection);
		EasyMock.expectLastCall().anyTimes();
		EasyMock.replay(dataSource);

		Date date = Calendar.getInstance().getTime();
		
		GeoFactory geoFactory = GeoFactory.eINSTANCE;
		MeasuredPosition3D geoPosition = geoFactory.createMeasuredPosition3D();
		Latitude latitude = GeoFactory.eINSTANCE.createLatitude();
		latitude.setDecimalDegree(54.2);
		Longitude longitude = GeoFactory.eINSTANCE.createLongitude();
		longitude.setDecimalDegree(13.14);
		geoPosition.setLatitude(latitude);
		geoPosition.setLongitude(longitude);
		geoPosition.setTime(date);
		geoPosition.setValid(true);
		
		Depth depth = GeoFactory.eINSTANCE.createDepth();
		depth.setMeasurementPosition(RelativeDepthMeasurementPosition.TRANSDUCER);
		depth.setDepth(13.2);
		depth.setValid(true);

		List<Measurement> measurements = new ArrayList<Measurement>();
		measurements.add(geoPosition);
		measurements.add(depth);
		
		postInsertGISWriter.bindOutputConnection(dataSource);
		postInsertGISWriter.activate(properties);
		postInsertGISWriter.write(measurements, true, 1L);
		postInsertGISWriter.closeOutput();
	}

}
