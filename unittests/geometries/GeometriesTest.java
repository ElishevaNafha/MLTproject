package geometries;
import static java.lang.System.out;
import org.junit.Test;
import primitives.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Testing Sphere
 * @author Elisheva Nafha and Eliana Rabinowitz
 */
public class GeometriesTest {

    /**
     * Test method for {@link Geometries#findIntersections(Ray)} (Point3D)}.
     */
    @Test
    public void findIntersections() {
        List<Point3D> result;
        Geometries geometries;
        Sphere sphere;
        Plane plane;
        Triangle triangle;
        Ray ray = new Ray(new Point3D(0,0,1), new Vector(1,0,0));
        //TC01: The list of geometries is empty
        geometries = new Geometries();
        result = geometries.findIntersections(ray);
        assertNull("List of geometries is empty", result);

        //TC02: No geometry intersects the ray
        sphere = new Sphere(1, new Point3D(3,3,3));
        plane =  new Plane(new Point3D(0,0,2),new Point3D(1,0,2), new Point3D(0,1,2));
        triangle = new Triangle(new Point3D(50,50,50), new Point3D(45,45,45), new Point3D(40,40,40));
        geometries = new Geometries();
        geometries.add(sphere);
        geometries.add(plane);
        geometries.add(triangle);
        result = geometries.findIntersections(ray);
        assertNull("No geometry intersects with ray", result);

        //TC03: One geometry intersects the ray
        sphere = new Sphere(1, new Point3D(3,0,1));
        plane =  new Plane(new Point3D(0,0,2),new Point3D(1,0,2), new Point3D(0,1,2));
        triangle = new Triangle(new Point3D(50,50,50), new Point3D(45,45,45), new Point3D(40,40,40));
        geometries = new Geometries();
        geometries.add(sphere);
        geometries.add(plane);
        geometries.add(triangle);
        result = geometries.findIntersections(ray);
        assertEquals("One geometry intersects with ray", result.size(),2);

        //TC04: Some (but not all) geometries intersect the ray
        sphere = new Sphere(1, new Point3D(3,0,1));
        plane =  new Plane(new Point3D(0,0,2),new Point3D(1,0,2), new Point3D(0,1,2));
        triangle = new Triangle(new Point3D(1,1,0), new Point3D(1,-1,0), new Point3D(0,0,4));
        geometries = new Geometries();
        geometries.add(sphere);
        geometries.add(plane);
        geometries.add(triangle);
        result = geometries.findIntersections(ray);
        assertEquals("One geometry intersects with ray", result.size(),3);

        //TC05: All geometries intersect the ray
        sphere = new Sphere(1, new Point3D(3,0,1));
        plane =  new Plane(new Point3D(1,1,0), new Point3D(1,-1,0), new Point3D(0,0,4));
        triangle = new Triangle(new Point3D(1,1,0), new Point3D(1,-1,0), new Point3D(0,0,4));
        geometries = new Geometries();
        geometries.add(sphere);
        geometries.add(plane);
        geometries.add(triangle);
        result = geometries.findIntersections(ray);
        assertEquals("One geometry intersects with ray", result.size(),4);
    }
}
