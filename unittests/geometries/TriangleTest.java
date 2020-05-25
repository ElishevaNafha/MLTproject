package geometries;

import org.junit.Test;
import primitives.*;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Testing Triangle
 * @author Elisheva Nafha and Eliana Rabinowitz
 */
public class TriangleTest {

    /**
     * Test method for {@link Triangle#getNormal(Point3D)}.
     */
    @Test
    public void getNormal()
    {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Get a normal for a triangle
        Triangle t = new Triangle(new Point3D(0,2,2), new Point3D(0,2,0), new Point3D(2,2,2));
        Vector normal = t.getNormal(new Point3D(1,2,1.5));
        assertEquals("getNormal() wrong normal result", normal, new Vector(0,-1,0));
    }

    /**
     * Test method for {@link Triangle#findIntersections(Ray)}.
     */
    @Test
    public void findIntersections() {
        Triangle t = new Triangle(new Point3D(-1,-5,0), new Point3D(-1,5,0), new Point3D(-1, 0, 10));
        Ray r;
        List<Point3D> expected;
        List<Intersectable.GeoPoint> result;

        // ============ Equivalence Partitions Tests ==============

        // **** Group: ray intersects triangle
        //TC01: ray intersects with triangle
        r = new Ray(new Point3D(0,0,5), new Vector(-1,-1,0));
        expected = new ArrayList<Point3D>();
        expected.add(new Point3D(-1,-1,5));
        result = t.findIntersections(r);
        assertEquals("findIntersections() doesn't work where ray intersects triangle", expected, result);

        // **** Group: ray does not intersect triangle
        //TC02: ray passes against triangle's edge
        r = new Ray(new Point3D(0,0,-5), new Vector(-1,0,0));
        result = t.findIntersections(r);
        assertNull("findIntersections() doesn't work where ray passes against triangle's edge", result);

        //TC03: ray passes against triangle's vertex
        r = new Ray(new Point3D(0,7,0), new Vector(-1,0,-1));
        result = t.findIntersections(r);
        assertNull("findIntersections() doesn't work where ray passes against triangle's vertex", result);

        // =============== Boundary Values Tests ==================

        // **** Group: ray begins before plane
        //TC04: ray intersects with triangle's edge
        r = new Ray(new Point3D(1,0,0), new Vector(-1,0,0));
        result = t.findIntersections(r);
        assertNull("findIntersections() doesn't work where ray intersects with triangle's edge", result);

        //TC05: ray intersects with triangle's vertex
        r = new Ray(new Point3D(0,0,10), new Vector(-1,0,0));
        result = t.findIntersections(r);
        assertNull("findIntersections() doesn't work where ray intersects with triangle's vertex", result);

        //TC06: ray intersects with the continuation of triangle's edge
        r = new Ray(new Point3D(0,-1,12), new Vector(-1,0,0));
        result = t.findIntersections(r);
        assertNull("findIntersections() doesn't work where ray intersects with the continuation of triangle's edge", result);

        // **** Group: ray begins on plane
        // TC07: ray begins on the triangle
        r = new Ray(new Point3D(-1,1,5), new Vector(-1,0,0));
        result = t.findIntersections(r);
        assertNull("findIntersections() doesn't work where ray begins on the triangle", result);
    }
}