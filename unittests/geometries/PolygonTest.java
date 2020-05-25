package geometries;

import org.junit.Test;

import static org.junit.Assert.*;

import geometries.*;
import primitives.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Testing Polygons
 * @author Dan
 */
public class PolygonTest {

    /**
     * Test method for
     * {@link geometries.Polygon#Polygon(Point3D...)}.
     */
    @Test
    public void testPolygon() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Correct concave quadrangular with vertices in correct order
        try {
            new Polygon(new Point3D(0, 0, 1), new Point3D(1, 0, 0),
                    new Point3D(0, 1, 0), new Point3D(-1, 1, 1));
        } catch (IllegalArgumentException e) {
            fail("Failed constructing a correct polygon");
        }

        // TC02: Wrong vertices order
        try {
            new Polygon(new Point3D(0, 0, 1), new Point3D(0, 1, 0),
                    new Point3D(1, 0, 0), new Point3D(-1, 1, 1));
            fail("Constructed a polygon with wrong order of vertices");
        } catch (IllegalArgumentException e) {}

        // TC03: Not in the same plane
        try {
            new Polygon(new Point3D(0, 0, 1), new Point3D(1, 0, 0),
                    new Point3D(0, 1, 0), new Point3D(0, 2, 2));
            fail("Constructed a polygon with vertices that are not in the same plane");
        } catch (IllegalArgumentException e) {}

        // TC04: Concave quadrangular
        try {
            new Polygon(new Point3D(0, 0, 1), new Point3D(1, 0, 0),
                    new Point3D(0, 1, 0), new Point3D(0.5, 0.25, 0.5));
            fail("Constructed a concave polygon");
        } catch (IllegalArgumentException e) {}

        // =============== Boundary Values Tests ==================

        // TC10: Vertex on a side of a quadrangular
        try {
            new Polygon(new Point3D(0, 0, 1), new Point3D(1, 0, 0),
                    new Point3D(0, 1, 0), new Point3D(0, 0.5, 0.5));
            fail("Constructed a polygon with vertex on a side");
        } catch (IllegalArgumentException e) {}

        // TC11: Last point = first point
        try {
            new Polygon(new Point3D(0, 0, 1), new Point3D(1, 0, 0),
                    new Point3D(0, 1, 0), new Point3D(0, 0, 1));
            fail("Constructed a polygon with vertex on a side");
        } catch (IllegalArgumentException e) {}

        // TC12: Collocated points
        try {
            new Polygon(new Point3D(0, 0, 1), new Point3D(1, 0, 0),
                    new Point3D(0, 1, 0), new Point3D(0, 1, 0));
            fail("Constructed a polygon with vertex on a side");
        } catch (IllegalArgumentException e) {}

    }

    /**
     * Test method for {@link geometries.Polygon#getNormal(primitives.Point3D)}.
     */
    @Test
    public void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: There is a simple single test here
        Polygon pl = new Polygon(new Point3D(0, 0, 1), new Point3D(1, 0, 0), new Point3D(0, 1, 0),
                new Point3D(-1, 1, 1));
        double sqrt3 = Math.sqrt(1d / 3);
        assertEquals("Bad normal to triangle", new Vector(sqrt3, sqrt3, sqrt3), pl.getNormal(new Point3D(0, 0, 1)));
    }

    /**
     * Test method for {@link Polygon#findIntersections(Ray)}.
     */
    @Test
    public void findIntersections() {
        Polygon p = new Polygon(new Point3D(-1,-5,0), new Point3D(-1,-5,5), new Point3D(-1, 5, 5), new Point3D(-1,5,0));
        Ray r;
        List<Point3D> expected;
        List<Intersectable.GeoPoint> result;

        // ============ Equivalence Partitions Tests ==============

        // **** Group: ray intersects polygon
        //TC01: ray intersects with polygon
        r = new Ray(new Point3D(0,0,2), new Vector(-1,-1,0));
        expected = new ArrayList<Point3D>();
        expected.add(new Point3D(-1,-1,2));
        result = p.findIntersections(r);
        assertEquals("findIntersections() doesn't work where ray intersects polygon", expected, result);

        // **** Group: ray does not intersect polygon
        //TC02: ray passes against polygon's edge
        r = new Ray(new Point3D(0,0,-5), new Vector(-1,0,0));
        result = p.findIntersections(r);
        assertNull("findIntersections() doesn't work where ray passes against polygon's edge", result);

        //TC03: ray passes against polygon's vertex
        r = new Ray(new Point3D(0,7,0), new Vector(-1,0,-1));
        result = p.findIntersections(r);
        assertNull("findIntersections() doesn't work where ray passes against polygon's vertex", result);

        // =============== Boundary Values Tests ==================

        // **** Group: ray begins before plane
        //TC04: ray intersects with polygon's edge
        r = new Ray(new Point3D(1,0,0), new Vector(-1,0,0));
        result = p.findIntersections(r);
        assertNull("findIntersections() doesn't work where ray intersects with polygon's edge", result);

        //TC05: ray intersects with polygon's vertex
        r = new Ray(new Point3D(0,5,5), new Vector(-1,0,0));
        result = p.findIntersections(r);
        assertNull("findIntersections() doesn't work where ray intersects with polygon's vertex", result);

        //TC06: ray intersects with the continuation of polygon's edge
        r = new Ray(new Point3D(0,6,5), new Vector(-1,0,0));
        result = p.findIntersections(r);
        assertNull("findIntersections() doesn't work where ray intersects with the continuation of polygon's edge", result);

        // **** Group: ray begins on plane
        // TC07: ray begins on the polygon
        r = new Ray(new Point3D(-1,1,3), new Vector(-1,0,0));
        result = p.findIntersections(r);
        assertNull("findIntersections() doesn't work where ray begins on the polygon", result);
    }

}