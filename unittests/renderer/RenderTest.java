package renderer;

import org.junit.Test;
import static org.junit.Assert.*;
import elements.*;
import primitives.*;
import geometries.*;
import primitives.Color;
import renderer.*;
import scene.Scene;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * test rendering image
 *
 * @author Elisheva Nafha and Eliana Rabinowitz
 */
public class RenderTest {

    /**
     * Produce a scene with basic 3D model and render it into a jpeg image with a
     * grid
     */
    @Test
    public void testRenderImage() {
        Scene scene = new Scene("Test scene");
        scene.setCamera(new Camera(new Point3D(0,0,-1000), new Vector(0, 0, 1), new Vector(0, -1, 0)));
        scene.setDistance(1100);
        scene.setBackground(new Color(75, 127, 90));
        scene.setAmbientLight(new AmbientLight(new Color(255, 191, 191), 1));

        scene.addGeometries(new Sphere(50, new Point3D(-200, 200, 100)));

        scene.addGeometries(
                new Triangle(new Point3D(100, 0, 100), new Point3D(0, 100, 100), new Point3D(100, 100, 100)),
                new Triangle(new Point3D(100, 0, 100), new Point3D(0, -100, 100), new Point3D(100, -100, 100)),
                new Triangle(new Point3D(-100, 0, 100), new Point3D(0, 100, 100), new Point3D(-100, 100, 100)),
                new Triangle(new Point3D(-100, 0, 100), new Point3D(0, -100, 100), new Point3D(-100, -100, 100)));

        ImageWriter imageWriter = new ImageWriter("base render test", 500, 500, 500, 500);
        Render render = new Render(imageWriter, scene);

        render.renderImage();
        render.printGrid(50, java.awt.Color.BLUE);
        render.writeToImage();
    }

    @Test
    public void testGetClosestPoint() {
        Ray ray = new Ray(new Point3D(0,0,0), new Vector(0,0,1));
        Scene scene = new Scene("hello");
        scene.setCamera(new Camera(Point3D.ZERO, new Vector(0,0,1), new Vector(0,-1,0)));
        scene.setDistance(2);
        Render render = new Render(new ImageWriter("hello", 4,4,4,4), scene);

        // ============ Equivalence Partitions Tests ==============

        //TC01: no points to check
        assertNull("getClosestPoint: doesn't return null when there're no points", render.getClosestPoint(ray));

        //TC02: one point to check
        scene.addGeometries(new Plane(new Point3D(0,0,3), new Vector(0,0,1)));
        render = new Render(new ImageWriter("hello", 4,4,4,4), scene);
        assertEquals("getClosestPoint: wrong point when there's one point", new Point3D(0,0,3),
                render.getClosestPoint(ray).point);

        //TC03: more than one point to check
        scene.addGeometries(new Plane(new Point3D(0,0,4), new Vector(0,0,1)),
                new Plane(new Point3D(0,0,5), new Vector(0,0,1)));
        render = new Render(new ImageWriter("hello", 4,4,4,4), scene);
        assertEquals("getClosestPoint: wrong point when there's more than one point", new Point3D(0,0,3),
                render.getClosestPoint(ray).point);

        // =============== Boundary Values Tests ==================

        //TC04: there are two closest points
        scene.addGeometries(new Sphere(2, new Point3D(0,0,5)));
        render = new Render(new ImageWriter("hello", 4,4,4,4), scene);
        assertEquals("getClosestPoint: wrong point when there're two closest points", new Point3D(0,0,3),
                render.getClosestPoint(ray).point);

        //TC05: intersection before view plane, only one point
        scene = new Scene("hello");
        scene.setCamera(new Camera(Point3D.ZERO, new Vector(0,0,1), new Vector(0,-1,0)));
        scene.setDistance(2);
        scene.addGeometries(new Plane(new Point3D(0,0,1), new Vector(0,0,1)));
        render = new Render(new ImageWriter("hello", 4,4,4,4), scene);
        assertNull("getClosestPoint: wrong point when there's a point before view plane, only one point",
                render.getClosestPoint(ray));

        //TC06: some intersections before view plane, no points after it
        scene.addGeometries(new Plane(new Point3D(0,0,1.5), new Vector(0,0,1)));
        render = new Render(new ImageWriter("hello", 4,4,4,4), scene);
        assertNull("getClosestPoint: wrong point when there're some intersections before view plane, no points after it",
                render.getClosestPoint(ray));

        //TC07: intersections before view plane and intersections after it
        scene.addGeometries(new Plane(new Point3D(0,0,3), new Vector(0,0,1)));
        render = new Render(new ImageWriter("hello", 4,4,4,4), scene);
        assertEquals("getClosestPoint: wrong point when there's a point before view plane, more than one point",
                new Point3D(0,0, 3), render.getClosestPoint(ray).point);

        //TC08: intersection on view plane
        scene = new Scene("hello");
        scene.setCamera(new Camera(Point3D.ZERO, new Vector(0,0,1), new Vector(0,-1,0)));
        scene.setDistance(2);
        scene.addGeometries(new Plane(new Point3D(0,0,2), new Vector(0,0,1)),
                new Plane(new Point3D(0,0,3), new Vector(0,0,1)));
        render = new Render(new ImageWriter("hello", 4,4,4,4), scene);
       assertEquals("getClosestPoint: wrong point when there's an intersection on view plane",
                new Point3D(0,0,2), render.getClosestPoint(ray).point);
    }

    /**
     * test generation of a beam of rays - prints a matrix showing the distribution of the rays in the circle
     */
    /*@Test
    public void testGetSampleRays(){
        int matrix[][]= new int[20][20];
        Ray ray = new Ray(new Point3D(10,10,0), new Vector(0,0,1));
        double distance = 10;
        double radius = 10;
        int numRays = 100;
        Render render = new Render(new ImageWriter("hi", 4,4,4,4), new Scene("testscene"));
        List<Ray> rays = render.getSampleRays(ray,numRays,radius,distance);
        Plane plane = new Plane(new Point3D(0,0,10),new Vector(0,0,1));
        List<Point3D> intersections = new ArrayList<>();
        System.out.println(rays.size());
        Point3D temp;
        for (Ray x : rays) {
            temp = plane.findIntersections(x).get(0).point;
            matrix[(int) Math.floor(temp.getX().get())][(int) Math.floor(temp.getY().get())]++;
        }
        for( int i=0;i<20;i++){
            for(int j=0; j<20;j++){
                System.out.print(matrix[i][j]);
                System.out.print(" ");
            }
            System.out.println();
        }
    }*/


}