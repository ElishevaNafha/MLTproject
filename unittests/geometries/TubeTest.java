package geometries;

import org.junit.Test;
import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;

import static org.junit.Assert.*;

/**
 * Testing Tube
 * @author Elisheva Nafha and Eliana Rabinowitz
 */
public class TubeTest {

    /**
     * Test method for {@link Tube#getNormal(Point3D)}.
     */
    @Test
    public void getNormal() {

        // ============ Equivalence Partitions Tests ==============

        // TC01: Get a normal for a tube
        Tube t = new Tube(2, new Ray(new Point3D(0,0,0), new Vector(0,1,0)));
        Vector normal1 = t.getNormal(new Point3D(0,3,2));
        assertEquals("getNormal() wrong normal result", normal1, new Vector(0,0,1));

        // TC02: Get a normal for a tube where t = 0
        Vector normal2 = t.getNormal(new Point3D(0,0,2));
        assertEquals("getNormal() wrong normal result where t = 0", normal2, new Vector(0,0,1));
    }

    /**
     * Test method for {@link Tube#findIntersections(Ray)}.
     */
    @Test
    public void findIntersections() {
        Tube t;
        Ray r;

        // ============ Equivalence Partitions Tests ==============

        //TC01: No intersections with tube


        //TC02: One intersection with tube
        //TC03: Two intersections with tube

        // =============== Boundary Values Tests ==================


    }
}