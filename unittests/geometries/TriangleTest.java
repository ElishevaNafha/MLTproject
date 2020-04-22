package geometries;

import org.junit.Test;
import primitives.Point3D;
import primitives.Vector;

import static org.junit.Assert.*;

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

    @Test
    public void findIntersections() {
        // ============ Equivalence Partitions Tests ==============
        //TC01:



        // =============== Boundary Values Tests ==================
        // **** Group: ray begins before plane
        //TC03: ray intersects with triangle's edge
        //TC04: ray intersects with triangle's vertex
        //TC05: ray intersects with the continuation of triangle's edge


    }
}