package geometries;

import org.junit.Test;
import primitives.Point3D;
import primitives.Vector;

import static org.junit.Assert.*;

/**
 * Testing Plane
 * @author Elisheva Nafha and Eliana Rabinowitz
 */
public class PlaneTest {

    /**
     * Test method for {@link Plane#getNormal(Point3D)}.
     */
    @Test
    public void getNormal() {

        // ============ Equivalence Partitions Tests ==============

        // TC01: Get a normal for a plain constructed with 3 points
        Plane p1 = new Plane(new Point3D(1,1,2), new Point3D(1,1,1), new Point3D(0,1,1));
        Vector normal = p1.getNormal(new Point3D(1,5,6));
        if (!(normal.equals(new Vector(0,1,0))) && !(normal.equals(new Vector(0,-1,0))))
            fail("getNormal() Not a normal to the plane (constructed with 3 points)");

        // TC02: Get a normal for a plain constructed with a point and a normal vector
        Plane p2 = new Plane(new Point3D(1,1,2), new Vector(0,6,0));
        if (!(normal.equals(new Vector(0,1,0))) && !(normal.equals(new Vector(0,-1,0))))
            fail("getNormal() Not a normal to the plane (constructed with a point and a normal vector)");
    }
}