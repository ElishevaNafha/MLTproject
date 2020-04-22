package geometries;

import org.junit.Test;
import primitives.*;
import java.util.List;

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

    /**
     * Test method for {@link geometries.Sphere#findIntersections(primitives.Ray)}.
     */
    @Test
    public void testFindIntersections() {
        Sphere sphere = new Sphere(1d, new Point3D(1, 0, 0));
        Point3D p1, p2;
        List<Point3D> result;

        // ============ Equivalence Partitions Tests ==============
        // TC01: Ray's line is outside the sphere (0 points)
        assertEquals("Ray's line out of sphere", null,
                sphere.findIntersections(new Ray(new Point3D(-1, 0, 0), new Vector(1, 1, 0))));

        // TC02: Ray starts before and crosses the sphere (2 points)
        p1 = new Point3D(0.0651530771650466, 0.355051025721682, 0);
        p2 = new Point3D(1.53484692283495, 0.844948974278318, 0);
        result = sphere.findIntersections(new Ray(new Point3D(-1, 0, 0),
                new Vector(3, 1, 0)));
        assertEquals("Wrong number of points", 2, result.size());
        assertTrue("Ray crosses sphere", (List.of(p1, p2) == result)||(List.of(p2,p1)==result));

        // TC03: Ray starts inside the sphere (1 point)
        p1 = new Point3D(2, 0, 0);
        result = sphere.findIntersections(new Ray(new Point3D(1.5, 0, 0),
                new Vector(1, 0, 0)));
        assertEquals("Wrong number of points", 1, result.size());
        assertEquals("Ray begins in sphere", List.of(p1), result);

        // TC04: Ray starts after the sphere (0 points)
        assertEquals("Ray starts after sphere", null,
                sphere.findIntersections(new Ray(new Point3D(3, 0, 0), new Vector(1, 0, 0))));

        // =============== Boundary Values Tests ==================
        // **** Group: Ray's line crosses the sphere (but not the center)
        // TC05: Ray starts at sphere and goes inside (1 points)
        p1 = new Point3D(1, 1, 0);
        result = sphere.findIntersections(new Ray(new Point3D(2, 0, 0),
                new Vector(-1, 1, 0)));
        assertEquals("Wrong number of points", 1, result.size());
        assertEquals("Ray starts at sphere and goes inside", List.of(p1), result);

        // TC06: Ray starts at sphere and goes outside (0 points)
        assertEquals("Ray starts at sphere and goes outside", null,
                sphere.findIntersections(new Ray(new Point3D(2, 0, 0), new Vector(1, -1, 0))));

        // **** Group: Ray's line goes through the center
        // TC07: Ray starts before the sphere (2 points)
        p1 = new Point3D(2, 0, 0);
        p2 = new Point3D(0, 0, 0);
        result = sphere.findIntersections(new Ray(new Point3D(3, 0, 0),
                new Vector(-1, 0, 0)));
        assertEquals("Wrong number of points", 2, result.size());
        assertTrue("Ray starts before sphere", (List.of(p1, p2) == result)||(List.of(p2,p1)==result));

        // TC08: Ray starts at sphere and goes inside (1 points)
        p1 = new Point3D(0, 0, 0);
        result = sphere.findIntersections(new Ray(new Point3D(2, 0, 0),
                new Vector(-1, 0, 0)));
        assertEquals("Wrong number of points", 1, result.size());
        assertEquals("Ray starts at sphere and enters", List.of(p1), result);

        // TC09: Ray starts inside (1 points)
        p1 = new Point3D(2, 0, 0);
        result = sphere.findIntersections(new Ray(new Point3D(1.5, 0, 0),
                new Vector(1, 0, 0)));
        assertEquals("Wrong number of points", 1, result.size());
        assertEquals("Ray begins in sphere", List.of(p1), result);

        // TC10: Ray starts at the center (1 points)
        p1 = new Point3D(2, 0, 0);
        result = sphere.findIntersections(new Ray(new Point3D(1, 0, 0),
                new Vector(1, 0, 0)));
        assertEquals("Wrong number of points", 1, result.size());
        assertEquals("Ray begins in sphere's center", List.of(p1), result);

        // TC11: Ray starts at sphere and goes outside (0 points)
        assertEquals("Ray starts at sphere and goes out ", null,
                sphere.findIntersections(new Ray(new Point3D(2, 0, 0), new Vector(1, 0, 0))));

        // TC12: Ray starts after sphere (0 points)
        assertEquals("Ray starts after sphere", null,
                sphere.findIntersections(new Ray(new Point3D(-3, 0, 0), new Vector(-1, 0, 0))));

        // **** Group: Ray's line is tangent to the sphere (all tests 0 points)
        // TC13: Ray starts before the tangent point
        result = sphere.findIntersections(new Ray(new Point3D(2, 1, 0),
                new Vector(-1, 0, 0)));
        assertEquals("Ray starts before tangent point", null, result);

        // TC14: Ray starts at the tangent point
        assertEquals("Ray's starts at tangent point", null,
                sphere.findIntersections(new Ray(new Point3D(1, 1, 0), new Vector(-1, 0, 0))));

        // TC15: Ray starts after the tangent point
        assertEquals("Ray's starts after tangent point", null,
                sphere.findIntersections(new Ray(new Point3D(-1, 1, 0), new Vector(-1, 0, 0))));

        // **** Group: Special cases
        // TC16: Ray's line is outside, ray is orthogonal to ray start to sphere's center line
    }


}