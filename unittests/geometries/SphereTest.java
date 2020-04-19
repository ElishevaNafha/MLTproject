package geometries;

import org.junit.Test;
import primitives.Point3D;
import primitives.Vector;

import static org.junit.Assert.*;

/**
 * Testing Sphere
 * @author Elisheva Nafha and Eliana Rabinowitz
 */
public class SphereTest {

    /**
     * Test method for {@link Sphere#getNormal(Point3D)}.
     */
    @Test
    public void getNormal() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: There is only one test case for a certain point on the sphere
        Sphere s = new Sphere(2, new Point3D(0,0,0));
        assertEquals("getNormal() wrong normal to point", s.getNormal(new Point3D(0,-2,0)), new Vector(0,-1,0));
    }
}