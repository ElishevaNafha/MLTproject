package renderer;

import elements.AmbientLight;
import elements.Camera;
import elements.SpotLight;
import geometries.*;
import org.junit.Test;
import primitives.*;
import scene.Scene;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * test class for bounding values hierarchy algorithm
 * @author Eliana Rabinowitz and Elisheva Nafha
 */
public class BoundingValuesHierarchyTests {
    /**
     * tests creation of box
     */
    @Test
    public void createBoxTest()
    {
        Geometries geometries;
        VirtualBox virtualBox;
        // ============ Equivalence Partitions Tests ==============
        //TC01: only spheres
        geometries = new Geometries();
        geometries.add(new Sphere(3, new Point3D(1,3,2)),
                new Sphere(4, new Point3D(20,-3,2)),
                new Sphere(1, new Point3D(103,32,100)));
        virtualBox = new VirtualBox(new Coordinate(-2),new Coordinate(104),new Coordinate(-7),new Coordinate(33),new Coordinate(-2),new Coordinate(101));
        assertEquals("only spheres",virtualBox,geometries.getVirtualBox());

        //TC02: only polygons
        geometries = new Geometries();
        geometries.add(new Triangle(new Point3D(1,1,1), new Point3D(2,4,1), new Point3D(55,3,8)),
                new Triangle(new Point3D(1,22,-12), new Point3D(12,4,1), new Point3D(55,33,-8)),
                new Polygon(new Point3D(1,1,1), new Point3D(1,1,5), new Point3D(1,10,5), new Point3D(1,10,1)));
        virtualBox = new VirtualBox(new Coordinate(1),new Coordinate(55),new Coordinate(1),new Coordinate(33),new Coordinate(-12),new Coordinate(8));
        assertEquals("only polygons",virtualBox,geometries.getVirtualBox());

        //TC03: both spheres and polygons
        geometries = new Geometries();
        geometries.add(new Triangle(new Point3D(1,1,1), new Point3D(2,4,1), new Point3D(55,3,8)),
                new Sphere(1, new Point3D(103,32,100)),
                new Polygon(new Point3D(1,1,1), new Point3D(1,1,5), new Point3D(1,10,5), new Point3D(1,10,1)));
        virtualBox = new VirtualBox(new Coordinate(1),new Coordinate(104),new Coordinate(1),new Coordinate(33),new Coordinate(1),new Coordinate(101));
        assertEquals("polygons and spheres",virtualBox,geometries.getVirtualBox());

        //TC04: both finite and infinite geometries
        geometries = new Geometries();
        geometries.add(new Triangle(new Point3D(1,1,1), new Point3D(2,4,1), new Point3D(55,3,8)),
                new Sphere(1, new Point3D(103,32,100)),
                new Polygon(new Point3D(1,1,1), new Point3D(1,1,5), new Point3D(1,10,5), new Point3D(1,10,1)),
                new Plane(new Point3D(1,1,1), new Point3D(2,2,2), new Point3D(342,2,5)));
        virtualBox = new VirtualBox(new Coordinate(1),new Coordinate(104),new Coordinate(1),new Coordinate(33),new Coordinate(1),new Coordinate(101));
        assertEquals("finite and infinite",virtualBox,geometries.getVirtualBox());

        //TC05: only infinite geometries
        geometries = new Geometries();
        geometries.add(new Plane(new Point3D(1,1,1), new Point3D(2,2,2), new Point3D(342,2,5)),
                new Plane(new Point3D(-123,1,98), new Point3D(2,2,2), new Point3D(342,2,5)),
                new Plane(new Point3D(1,-1,-1), new Point3D(-2,2,24), new Point3D(342,2,5)));
        assertNull("only infinite",geometries.getVirtualBox());

        //TC06: no geometries
        geometries=new Geometries();
        assertNull("no geometries",geometries.getVirtualBox());

    }

    /**
     * test flatten function from geometries
     */
    /*@Test
    public void flattenTest(){
        Sphere sphere = new Sphere(4,new Point3D(1,1,1));
        Sphere sphere2 = new Sphere(5,new Point3D(1,1,1));
        Sphere sphere3 = new Sphere(6,new Point3D(1,1,1));
        Sphere sphere4 = new Sphere(7,new Point3D(1,1,1));
        Geometries geometries = new Geometries(sphere, sphere2);
        Geometries geometries2 = new Geometries(geometries, sphere3, sphere4);
        Geometries expected = new Geometries(sphere, sphere2, sphere3, sphere4);
        geometries2.flatten();
        assertEquals("flatten test", expected, geometries2);
    }*/

    /**
     * test box's splitting process
     */
    @Test
    public void splitVirtualBoxTest(){
        Triangle triangle1 = new Triangle(new Point3D(10,10,10), new Point3D(1,3,14), new Point3D(3,2,5));
        Triangle triangle2 = new Triangle(new Point3D(-10,-10,-10), new Point3D(-5,3,1), new Point3D(0,2,-5));
        Triangle triangle3 = new Triangle(new Point3D(10,10,17), new Point3D(1,31,14), new Point3D(12,2,50));
        Triangle triangle4 = new Triangle(new Point3D(9,10,9), new Point3D(10,33,-14), new Point3D(4,2,5));
        Geometries geometries = new Geometries(triangle1,triangle2,triangle3,triangle4);
        geometries.splitVirtualBox();
        Geometries expected = new Geometries(new Geometries(triangle1,triangle2), new Geometries(triangle3,triangle4));
        assertEquals("split virtual box test",expected,geometries);
    }

    /**
     * test entire building process
     */
    @Test
    public void buildVirtualBoxesHierarchyTest(){
        // TC01:
        Triangle triangle1 = new Triangle(new Point3D(10,10,10), new Point3D(1,3,14), new Point3D(3,2,5));
        Triangle triangle2 = new Triangle(new Point3D(-10,-10,-10), new Point3D(-5,3,1), new Point3D(0,2,-5));
        Triangle triangle3 = new Triangle(new Point3D(10,10,17), new Point3D(1,31,14), new Point3D(12,2,50));
        Triangle triangle4 = new Triangle(new Point3D(9,10,9), new Point3D(10,33,-14), new Point3D(4,2,5));
        Geometries geometries = new Geometries(triangle1,triangle2,triangle3,triangle4);
        geometries.buildVirtualBoxesHierarchy();
        Geometries expected = new Geometries(new Geometries(new Geometries(triangle2),new Geometries(triangle1)), new Geometries(new Geometries(triangle3),new Geometries(triangle4)));
        assertEquals("build virtual boxes hierarchy test",expected,geometries);
        // TC02: overlapping shapes
        Sphere sphere = new Sphere(4,new Point3D(0,0,0));
        Sphere sphere2 = new Sphere(2,new Point3D(0,0,0));
        geometries = new Geometries(sphere,sphere2);
        geometries.buildVirtualBoxesHierarchy();
        expected = new Geometries(new Geometries(sphere2), new Geometries(sphere));
        assertEquals("build virtual boxes hierarchy test",expected,geometries);
    }

}
