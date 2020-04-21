package geometries;

import org.junit.Test;
import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;

import static org.junit.Assert.*;

/**
 * Testing Cylinder
 * @author Eliana Rabinowitz and Elisheva Nafha
 */
public class CylinderTest {

    /**
     * Test method for {@link Cylinder#getNormal(Point3D)}.
     */
    @Test
    public void getNormal() {
        Cylinder c = new Cylinder(2, new Ray(new Point3D(0,0,0), new Vector(0,0,1)), 6);

        // ============ Equivalence Partitions Tests ==============

        // TC01: Get a normal for a cylinder on the middle
        Vector normal1 = c.getNormal(new Point3D(2,0,3));
        assertEquals("getNormal() wrong normal for a point on the middle", normal1, new Vector(1,0,0));
//hi
        // TC02: Get a normal for a cylinder on the top
        Vector normal2 = c.getNormal(new Point3D(1,1,6));
        assertEquals("getNormal() wrong normal for a point on the top", normal2, new Vector(0,0,1));

        // TC03: Get a normal for a cylinder on the bottom
        Vector normal3 = c.getNormal(new Point3D(-1,0,0));
        assertEquals("getNormal() wrong normal for a point on the bottom", normal3, new Vector(0,0,-1));

        // =============== Boundary Values Tests ==================

        // TC04: Get a normal for a cylinder on the border to the top
        Vector normal4 = c.getNormal(new Point3D(2,0,6));
        assertEquals("getNormal() wrong normal for a point on the border to the top", normal4, new Vector(0,0,1));

        // TC05: Get a normal for a cylinder on the border to the bottom
        Vector normal5 = c.getNormal(new Point3D(2,0,0));
        assertEquals("getNormal() wrong normal for a point on the border to the bottom", normal5, new Vector(0,0,-1));

    }
}