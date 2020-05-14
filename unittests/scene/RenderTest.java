package scene;

import org.junit.Test;
import static org.junit.Assert.*;
import elements.*;
import primitives.*;
import geometries.*;
import renderer.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RenderTest {

    /**
     * Produce a scene with basic 3D model and render it into a jpeg image with a
     * grid
     */
    @Test
    public void testRenderImage() {
        Scene scene = new Scene("Test scene");
        scene.setCamera(new Camera(Point3D.ZERO, new Vector(0, 0, 1), new Vector(0, -1, 0)));
        scene.setDistance(100);
        scene.setBackground(new Color(75, 127, 90));
        scene.setAmbientLight(new AmbientLight(new Color(255, 191, 191), 1));

        scene.addGeometries(new Sphere(50, new Point3D(0, 0, 100)));

        scene.addGeometries(
                new Triangle(new Point3D(100, 0, 100), new Point3D(0, 100, 100), new Point3D(100, 100, 100)),
                new Triangle(new Point3D(100, 0, 100), new Point3D(0, -100, 100), new Point3D(100, -100, 100)),
                new Triangle(new Point3D(-100, 0, 100), new Point3D(0, 100, 100), new Point3D(-100, 100, 100)),
                new Triangle(new Point3D(-100, 0, 100), new Point3D(0, -100, 100), new Point3D(-100, -100, 100)));

        ImageWriter imageWriter = new ImageWriter("base render test", 500, 500, 500, 500);
        Render render = new Render(imageWriter, scene);

        render.renderImage();
        render.printGrid(50, java.awt.Color.YELLOW);
        render.writeToImage();
    }

    @Test
    public void testGetClosestPoint() {
        Scene scene = new Scene("hello");
        scene.setCamera(new Camera(Point3D.ZERO, new Vector(0,0,1), new Vector(0,-1,0)));
        scene.setDistance(2);
        Render render = new Render(new ImageWriter("hello", 4,4,4,4), scene);

        List<Point3D> points;

        // ============ Equivalence Partitions Tests ==============

        //TC01: more than one point to check
        points = new ArrayList<>();
        Collections.addAll(points, new Point3D(3,3,3), new Point3D(5,5,5), new Point3D(4,4,4));
        assertEquals("getClosestPoint: wrong point when there's more than one point", new Point3D(3,3,3), render.getClosestPoint(points));

        //TC02: one point to check
        points = new ArrayList<>();
        points.add(new Point3D(1,3,4));
        assertEquals("getClosestPoint: wrong point when there's one point", new Point3D(1,3,4), render.getClosestPoint(points));

        //TC03: no points to check
        assertNull("getClosestPoint: doesn't return null when there're no points", render.getClosestPoint(null));

        // =============== Boundary Values Tests ==================

        //TC04: there are two closest points
        points = new ArrayList<>();
        Collections.addAll(points, new Point3D(3,3,3), new Point3D(4,4,4), new Point3D(3,3,3));
        assertEquals("getClosestPoint: wrong point when there're two closest points", new Point3D(3,3,3), render.getClosestPoint(points));

        //TC05: intersection before view plane, more than one point
        points = new ArrayList<>();
        Collections.addAll(points, new Point3D(1,1,1), new Point3D(4,4,4), new Point3D(3,3,3));
        assertEquals("getClosestPoint: wrong point when there's a point before view plane, more than one point",
                new Point3D(3,3, 3), render.getClosestPoint(points));

        //TC06: intersection before view plane, only one point
        points = new ArrayList<>();
        points.add(new Point3D(1,1,1));
        assertNull("getClosestPoint: wrong point when there's a point before view plane, only one point",
                render.getClosestPoint(points));

        //TC07: some intersections before view plane, no points after it
        points = new ArrayList<>();
        Collections.addAll(points, new Point3D(1,1,1), new Point3D(1.5,1.5,1.5));
        assertNull("getClosestPoint: wrong point when there're some intersections before view plane, no points after it",
                render.getClosestPoint(points));

        //TC08: intersection on view plane
        points = new ArrayList<>();
        points.add(new Point3D(2,2,2));
        assertEquals("getClosestPoint: wrong point when there's an intersection on view plane",
                new Point3D(2,2,2), render.getClosestPoint(points));
    }

    @Test
    public void testCalcColor() {
        //not necessary meanwhile
    }
}