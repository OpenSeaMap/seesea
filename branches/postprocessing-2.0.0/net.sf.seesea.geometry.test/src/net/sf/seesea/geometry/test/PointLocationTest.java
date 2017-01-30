package net.sf.seesea.geometry.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import net.sf.seesea.geometry.impl.Point;
import net.sf.seesea.geometry.impl.Triangle;
import net.sf.seesea.geometry.impl.math.LocationResult;
import net.sf.seesea.geometry.impl.math.PointLocation;
import net.sf.seesea.geometry.impl.math.PointTriangleLocation;

public class PointLocationTest {

	@Test
	public void testLocation() {
		Triangle t1 = new Triangle(new Point(50, 50), new Point(1, 1), new Point(100, 1));
		Point point = new Point(50,25);
		
		PointLocation pointLocation = new PointLocation();
		LocationResult locationResult = pointLocation.getLocation(t1, point);
		
		assertEquals(PointTriangleLocation.INTRIANGLE, locationResult.getPointTriangleLocation());
		
	}

	@Test
	public void testOnEdge() {
		Triangle t1 = new Triangle(new Point(50, 50), new Point(1, 1), new Point(50, 1));
		Point point = new Point(25,25);
		
		PointLocation pointLocation = new PointLocation();
		LocationResult locationResult = pointLocation.getLocation(t1, point);
		
		assertEquals(PointTriangleLocation.ONEDGE, locationResult.getPointTriangleLocation());
	}

	@Test
	public void testOnEdge2() {
		Triangle t1 = new Triangle(new Point(50, 50), new Point(1, 1), new Point(50, 1));
		Point point = new Point(25,1);
		
		PointLocation pointLocation = new PointLocation();
		LocationResult locationResult = pointLocation.getLocation(t1, point);
		
		assertEquals(PointTriangleLocation.ONEDGE, locationResult.getPointTriangleLocation());
	}

	@Test
	public void testOnEdge3() {
		Triangle t1 = new Triangle(new Point(50, 50), new Point(1, 1), new Point(50, 1));
		Point point = new Point(50,25);
		
		PointLocation pointLocation = new PointLocation();
		LocationResult locationResult = pointLocation.getLocation(t1, point);
		
		assertEquals(PointTriangleLocation.ONEDGE, locationResult.getPointTriangleLocation());
	}

	@Test
	public void testOnVertex1() {
		Triangle t1 = new Triangle(new Point(50, 50), new Point(1, 1), new Point(50, 1));
		Point point = new Point(1,1);
		
		PointLocation pointLocation = new PointLocation();
		LocationResult locationResult = pointLocation.getLocation(t1, point);
		
		assertEquals(PointTriangleLocation.ONVERTEX, locationResult.getPointTriangleLocation());
	}

	@Test
	public void testOnVertex2() {
		Triangle t1 = new Triangle(new Point(50, 50), new Point(1, 1), new Point(50, 1));
		Point point = new Point(50,50);
		
		PointLocation pointLocation = new PointLocation();
		LocationResult locationResult = pointLocation.getLocation(t1, point);
		
		assertEquals(PointTriangleLocation.ONVERTEX, locationResult.getPointTriangleLocation());
	}

	@Test
	public void testOnVertex3() {
		Triangle t1 = new Triangle(new Point(50, 50), new Point(1, 1), new Point(50, 1));
		Point point = new Point(50,1);
		
		PointLocation pointLocation = new PointLocation();
		LocationResult locationResult = pointLocation.getLocation(t1, point);
		
		assertEquals(PointTriangleLocation.ONVERTEX, locationResult.getPointTriangleLocation());
	}

	@Test
	public void testOutOfTriangleEdge1() {
		Triangle t1 = new Triangle(new Point(50, 50), new Point(1, 1), new Point(50, 1));
		Point point = new Point(0,0);
		
		PointLocation pointLocation = new PointLocation();
		LocationResult locationResult = pointLocation.getLocation(t1, point);
		
		assertEquals(PointTriangleLocation.OUTOFTRIANGLE, locationResult.getPointTriangleLocation());
	}

	@Test
	public void testOutOfTriangleEdge2() {
		Triangle t1 = new Triangle(new Point(50, 50), new Point(1, 1), new Point(50, 1));
		Point point = new Point(51,51);
		
		PointLocation pointLocation = new PointLocation();
		LocationResult locationResult = pointLocation.getLocation(t1, point);
		
		assertEquals(PointTriangleLocation.OUTOFTRIANGLE, locationResult.getPointTriangleLocation());
	}

	@Test
	public void testOutOfTriangleEdge3() {
		Triangle t1 = new Triangle(new Point(50, 50), new Point(1, 1), new Point(50, 1));
		Point point = new Point(50,0);
		
		PointLocation pointLocation = new PointLocation();
		LocationResult locationResult = pointLocation.getLocation(t1, point);
		
		assertEquals(PointTriangleLocation.OUTOFTRIANGLE, locationResult.getPointTriangleLocation());
	}

	@Test
	public void testOutOfTriangleEdge4() {
		Triangle t1 = new Triangle(new Point(50, 50), new Point(1, 1), new Point(50, 1));
		Point point = new Point(50,51);
		
		PointLocation pointLocation = new PointLocation();
		LocationResult locationResult = pointLocation.getLocation(t1, point);
		
		assertEquals(PointTriangleLocation.OUTOFTRIANGLE, locationResult.getPointTriangleLocation());
	}

	@Test
	public void testOutOfTriangleEdge5() {
		Triangle t1 = new Triangle(new Point(50, 50), new Point(1, 1), new Point(50, 1));
		Point point = new Point(0,1);
		
		PointLocation pointLocation = new PointLocation();
		LocationResult locationResult = pointLocation.getLocation(t1, point);
		
		assertEquals(PointTriangleLocation.OUTOFTRIANGLE, locationResult.getPointTriangleLocation());
	}

	@Test
	public void testOutOfTriangleEdge6() {
		Triangle t1 = new Triangle(new Point(50, 50), new Point(1, 1), new Point(50, 1));
		Point point = new Point(51,1);
		
		PointLocation pointLocation = new PointLocation();
		LocationResult locationResult = pointLocation.getLocation(t1, point);
		
		assertEquals(PointTriangleLocation.OUTOFTRIANGLE, locationResult.getPointTriangleLocation());
	}

	
	@Test
	public void testOutOfTriangleFarApartEdge1() {
		Triangle t1 = new Triangle(new Point(50, 50), new Point(1, 1), new Point(50, 1));
		Point point = new Point(-124,100);
		
		PointLocation pointLocation = new PointLocation();
		LocationResult locationResult = pointLocation.getLocation(t1, point);
		
		assertEquals(PointTriangleLocation.OUTOFTRIANGLE, locationResult.getPointTriangleLocation());
	}

	@Test
	public void testOutOfTriangleFarApartEdge2() {
		Triangle t1 = new Triangle(new Point(50, 50), new Point(1, 1), new Point(50, 1));
		Point point = new Point(124,100);
		
		PointLocation pointLocation = new PointLocation();
		LocationResult locationResult = pointLocation.getLocation(t1, point);
		
		assertEquals(PointTriangleLocation.OUTOFTRIANGLE, locationResult.getPointTriangleLocation());
	}

	@Test
	public void testOutOfTriangleFarApartEdge3() {
		Triangle t1 = new Triangle(new Point(50, 50), new Point(1, 1), new Point(50, 1));
		Point point = new Point(10,100);
		
		PointLocation pointLocation = new PointLocation();
		LocationResult locationResult = pointLocation.getLocation(t1, point);
		
		assertEquals(PointTriangleLocation.OUTOFTRIANGLE, locationResult.getPointTriangleLocation());
	}
	
	@Test
	public void testOutOfTriangleFarApartEdge4() {
		Triangle t1 = new Triangle(new Point(50, 50), new Point(1, 1), new Point(50, 1));
		Point point = new Point(25,50);
		
		PointLocation pointLocation = new PointLocation();
		LocationResult locationResult = pointLocation.getLocation(t1, point);
		
		assertEquals(PointTriangleLocation.OUTOFTRIANGLE, locationResult.getPointTriangleLocation());
	}

	@Test
	public void testOutOfTriangleFarApartEdge5() {
		Triangle t1 = new Triangle(new Point(50, 50), new Point(1, 1), new Point(50, 1));
		Point point = new Point(60,25);
		
		PointLocation pointLocation = new PointLocation();
		LocationResult locationResult = pointLocation.getLocation(t1, point);
		
		assertEquals(PointTriangleLocation.OUTOFTRIANGLE, locationResult.getPointTriangleLocation());
	}

	@Test
	public void testOutOfTriangleFarApartEdge6() {
		Triangle t1 = new Triangle(new Point(50, 50), new Point(1, 1), new Point(50, 1));
		Point point = new Point(25,0);
		
		PointLocation pointLocation = new PointLocation();
		LocationResult locationResult = pointLocation.getLocation(t1, point);
		
		assertEquals(PointTriangleLocation.OUTOFTRIANGLE, locationResult.getPointTriangleLocation());
	}

	@Test
	public void testOutOfTriangleCloseEdge1() {
		Triangle t1 = new Triangle(new Point(50, 50), new Point(1, 1), new Point(50, 1));
		Point point = new Point(1.000000000000001D,1.000000000000001D);
		
		PointLocation pointLocation = new PointLocation();
		LocationResult locationResult = pointLocation.getLocation(t1, point);
		
		assertEquals(PointTriangleLocation.ONEDGE, locationResult.getPointTriangleLocation());
	}

	@Test
	public void testOutOfTriangleCloseEdge2() {
		Triangle t1 = new Triangle(new Point(50, 50), new Point(1, 1), new Point(50, 1));
		Point point = new Point(1.000000000000002D,1.000000000000001D);
							    
		PointLocation pointLocation = new PointLocation();
		LocationResult locationResult = pointLocation.getLocation(t1, point);
		
		assertEquals(PointTriangleLocation.INTRIANGLE, locationResult.getPointTriangleLocation());
	}
	
}
