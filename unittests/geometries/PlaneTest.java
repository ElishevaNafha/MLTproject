package geometries;

import org.junit.Test;
import primitives.*;
import java.util.List;
import java.awt.*;
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

    /**
     * Test method for {@link geometries.Plane#findIntersections(primitives.Ray)}.
     */
    @Test
    public void findIntersections() {
        Plane plane = new Plane(new Point3D(0,0,1), new Point3D(1,0,1), new Point3D(0,1,1));
        Point3D p;
        List<Point3D> result;
        // ============ Equivalence Partitions Tests ==============
        //Ray is neither orthogonal nor parallel to the plane
        //TC01: Ray intersects the plane
        p = new Point3D(1,0,1);
        Ray ray  = new Ray(new Point3D(0,0,2), new Vector(1,0,-1));
        result = plane.findIntersections(ray);
        assertEquals("Ray intersects the plane", result,p);

        //TC02: Ray doesn't intersect the plane
        ray = new Ray(new Point3D(1,0,0), new Vector(1,0,-1));
        result = plane.findIntersections(ray);
        assertEquals("Ray doesn't intersect plane", null, result);

        // =============== Boundary Values Tests ==================
        // **** Group: Ray is parallel to the plane
        //TC03: Ray is in plane
        ray = new Ray(new Point3D(1,0,1), new Vector(1,0,0));
        result = plane.findIntersections(ray);
        assertEquals("Ray lies in plane", null, result);//WHAT IS ACTUALLY EXPECTED WHEN RAY LIES IN PLANE?

        //TC04: ray isn't in plane
        ray = new Ray(new Point3D(1,0,2), new Vector(1,0,0));
        result = plane.findIntersections(ray);
        assertEquals("Ray doesn't lie in plane", null, result);

        // **** Group: Ray is orthogonal to plane
        //TC05: Ray starts before plane and crosses it
        p = new Point3D(1,0,1);
        ray = new Ray(new Point3D(1,0,2), new Vector(0,0,-1));
        result = plane.findIntersections(ray);
        assertEquals("Ray starts before plane and intersects it", p, result);

        //TC06: Ray starts at plane
        ray = new Ray(new Point3D(1,0,1), new Vector(0,0,-1));
        result = plane.findIntersections(ray);
        assertEquals("Ray starts at plane", null, result);

        //TC07: Ray starts after plane
        ray = new Ray(new Point3D(1,0,0), new Vector(0,0,-1));
        result = plane.findIntersections(ray);
        assertEquals("Ray starts after plane", null, result);

        // **** Group: Ray is neither orthogonal nor parallel to the plane
        //TC08: Ray begins at the plane
        ray = new Ray(new Point3D(1,0,1), new Vector(1,0,-1));
        result = plane.findIntersections(ray);
        assertEquals("Ray starts at plane", null, result);
        //TC09: Ray begins at plane's reference point
        ray = new Ray(new Point3D(0,0,1), new Vector(1,0,-1));
        result = plane.findIntersections(ray);
        assertEquals("Ray starts at plane's reference point", null, result);

    }
}