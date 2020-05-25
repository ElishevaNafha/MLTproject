package renderer;

import org.junit.Test;
import static org.junit.Assert.*;
import elements.*;
import primitives.*;
import geometries.*;
import renderer.*;
import scene.Scene;

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
        Scene scene = new Scene("hello");
        scene.setCamera(new Camera(Point3D.ZERO, new Vector(0,0,1), new Vector(0,-1,0)));
        scene.setDistance(2);
        Render render = new Render(new ImageWriter("hello", 4,4,4,4), scene);

        List<Intersectable.GeoPoint> points;

        // ============ Equivalence Partitions Tests ==============

        //TC01: more than one point to check
        points = new ArrayList<>();
        Plane plane = new Plane(new Point3D(3,3,3), new Point3D(5,5,5), new Point3D(4,4,4));
        Collections.addAll(points, new Intersectable.GeoPoint(plane,new Point3D(3,3,3)), new Intersectable.GeoPoint(plane,new Point3D(4,4,4)),new Intersectable.GeoPoint(plane,new Point3D(5,5,5)));
        assertEquals("getClosestPoint: wrong point when there's more than one point", new Point3D(3,3,3), render.getClosestPoint(points));

        //TC02: one point to check
        points = new ArrayList<>();
        plane = new Plane(new Point3D(1,3,4), new Point3D(5,5,5), new Point3D(4,4,4));
        points.add(new Intersectable.GeoPoint(plane,new Point3D(1,3,4)));
        assertEquals("getClosestPoint: wrong point when there's one point", new Point3D(1,3,4), render.getClosestPoint(points));

        //TC03: no points to check
        assertNull("getClosestPoint: doesn't return null when there're no points", render.getClosestPoint(null));

        // =============== Boundary Values Tests ==================

        //TC04: there are two closest points
        points = new ArrayList<>();
        plane = new Plane(new Point3D(1,3,4), new Point3D(5,5,5), new Point3D(3,3,3));
        Collections.addAll(points, new Intersectable.GeoPoint(plane, new Point3D(3,3,3)),new Intersectable.GeoPoint(plane, new Point3D(4,4,4)),new Intersectable.GeoPoint(plane, new Point3D(3,3,3)));
        assertEquals("getClosestPoint: wrong point when there're two closest points", new Point3D(3,3,3), render.getClosestPoint(points));

        //TC05: intersection before view plane, more than one point
        points = new ArrayList<>();
        plane = new Plane(new Point3D(1,1,1), new Point3D(4,4,4), new Point3D(3,3,3));
        Collections.addAll(points,new Intersectable.GeoPoint(plane, new Point3D(1,1,1)),new Intersectable.GeoPoint(plane, new Point3D(4,4,4)),new Intersectable.GeoPoint(plane, new Point3D(3,3,3)));
        assertEquals("getClosestPoint: wrong point when there's a point before view plane, more than one point",
                new Point3D(3,3, 3), render.getClosestPoint(points));

        //TC06: intersection before view plane, only one point
        points = new ArrayList<>();
        plane = new Plane(new Point3D(1,1,1), new Point3D(4,4,4), new Point3D(3,3,3));
        points.add(new Intersectable.GeoPoint(plane,new Point3D(1,1,1)));
        assertNull("getClosestPoint: wrong point when there's a point before view plane, only one point",
                render.getClosestPoint(points));

        //TC07: some intersections before view plane, no points after it
        points = new ArrayList<>();
        plane = new Plane(new Point3D(1,1,1), new Point3D(1.5,1.5,1.5), new Point3D(3,3,3));
        Collections.addAll(points, new Intersectable.GeoPoint(plane,new Point3D(1,1,1)), new Intersectable.GeoPoint(plane, new Point3D(1.5,1.5,1.5)));
        assertNull("getClosestPoint: wrong point when there're some intersections before view plane, no points after it",
                render.getClosestPoint(points));

        //TC08: intersection on view plane
        points = new ArrayList<>();
        plane = new Plane(new Point3D(1,1,1), new Point3D(2,2,2), new Point3D(3,3,3));
        points.add(new Intersectable.GeoPoint(plane,new Point3D(2,2,2)));
        assertEquals("getClosestPoint: wrong point when there's an intersection on view plane",
                new Point3D(2,2,2), render.getClosestPoint(points));
    }

    /**
     * Produce a picture of a sphere lighted by a directional light
     */
    @Test
    public void sphereDirectional() {
        Scene scene = new Scene("Test scene");
        scene.setCamera(new Camera(new Point3D(0, 0, -1000), new Vector(0, 0, 1), new Vector(0, -1, 0)));
        scene.setDistance(1000);
        scene.setBackground(Color.BLACK);
        scene.setAmbientLight(new AmbientLight(Color.BLACK, 0));

        scene.addGeometries(
                new Sphere(new Color(java.awt.Color.BLUE), new Material(0.5, 0.5, 100), 50, new Point3D(0, 0, 50)));

        scene.addLights(new DirectionalLight(new Color(500, 300, 0), new Vector(1, -1, 1)));

        ImageWriter imageWriter = new ImageWriter("sphereDirectional", 150, 150, 500, 500);
        Render render = new Render(imageWriter, scene);

        render.renderImage();
        render.writeToImage();
    }

    /**
     * Produce a picture of a sphere lighted by a point light
     */
    @Test
    public void spherePoint() {
        Scene scene = new Scene("Test scene");
        scene.setCamera(new Camera(new Point3D(0, 0, -1000), new Vector(0, 0, 1), new Vector(0, -1, 0)));
        scene.setDistance(1000);
        scene.setBackground(Color.BLACK);
        scene.setAmbientLight(new AmbientLight(Color.BLACK, 0));

        scene.addGeometries(
                new Sphere(new Color(java.awt.Color.BLUE), new Material(0.5, 0.5, 100), 50, new Point3D(0, 0, 50)));

        scene.addLights(new PointLight(new Color(500, 300, 0), new Point3D(-50, 50, -50), 1, 0.00001, 0.000001));

        ImageWriter imageWriter = new ImageWriter("spherePoint", 150, 150, 500, 500);
        Render render = new Render(imageWriter, scene);

        render.renderImage();
        render.writeToImage();
    }

    /**
     * Produce a picture of a sphere lighted by a spot light
     */
    @Test
    public void sphereSpot() {
        Scene scene = new Scene("Test scene");
        scene.setCamera(new Camera(new Point3D(0, 0, -1000), new Vector(0, 0, 1), new Vector(0, -1, 0)));
        scene.setDistance(1000);
        scene.setBackground(Color.BLACK);
        scene.setAmbientLight(new AmbientLight(Color.BLACK, 0));

        scene.addGeometries(
                new Sphere(new Color(java.awt.Color.BLUE), new Material(0.5, 0.5, 100), 50, new Point3D(0, 0, 50)));

        scene.addLights(new SpotLight(new Color(500, 300, 0), new Point3D(-50, 50, -50),
                new Vector(1, -1, 2), 1, 0.00001, 0.00000001));

        ImageWriter imageWriter = new ImageWriter("sphereSpot", 150, 150, 500, 500);
        Render render = new Render(imageWriter, scene);

        render.renderImage();
        render.writeToImage();
    }

    /**
     * Produce a picture of a sphere lighted by a spot light
     */
    @Test
    public void sphereMultipleLights() {
        Scene scene = new Scene("Test scene");
        scene.setCamera(new Camera(new Point3D(0, 0, -1000), new Vector(0, 0, 1), new Vector(0, -1, 0)));
        scene.setDistance(1000);
        scene.setBackground(Color.BLACK);
        scene.setAmbientLight(new AmbientLight(Color.BLACK, 0));

        scene.addGeometries(
                new Sphere(new Color(java.awt.Color.BLUE), new Material(0.5, 0.5, 100), 50, new Point3D(0, 0, 50)));

        scene.addLights(new DirectionalLight(new Color(100, 300, 50), new Vector(1, 1, 1)));
        scene.addLights(new PointLight(new Color(300, 150, 50), new Point3D(-50, 50, -50),
                1, 0.0001, 0.00001));
        scene.addLights(new SpotLight(new Color(250, 250, 0), new Point3D(-50, 50, -50),
                new Vector(1, -1, 2), 1, 0.00001, 0.00001));

        ImageWriter imageWriter = new ImageWriter("sphereMultipleLights", 150, 150, 500, 500);
        Render render = new Render(imageWriter, scene);

        render.renderImage();
        render.writeToImage();
    }

    /**
     * Produce a picture of a two triangles lighted by a directional light
     */
    @Test
    public void trianglesDirectional() {
        Scene scene = new Scene("Test scene");
        scene.setCamera(new Camera(new Point3D(0, 0, -1000), new Vector(0, 0, 1), new Vector(0, -1, 0)));
        scene.setDistance(1000);
        scene.setBackground(Color.BLACK);
        scene.setAmbientLight(new AmbientLight(new Color(java.awt.Color.WHITE), 0.15));

        scene.addGeometries(
                new Triangle(Color.BLACK, new Material(0.8, 0.2, 300),
                        new Point3D(-150, 150, 150), new Point3D(150, 150, 150), new Point3D(75, -75, 150)),
                new Triangle(Color.BLACK, new Material(0.8, 0.2, 300),
                        new Point3D(-150, 150, 150), new Point3D(-70, -70, 50), new Point3D(75, -75, 150)));

        scene.addLights(new DirectionalLight(new Color(300, 150, 150), new Vector(0, 0, 1)));

        ImageWriter imageWriter = new ImageWriter("trianglesDirectional", 200, 200, 500, 500);
        Render render = new Render(imageWriter, scene);

        render.renderImage();
        render.writeToImage();
    }

    /**
     * Produce a picture of a two triangles lighted by a point light
     */
    @Test
    public void trianglesPoint() {
        Scene scene = new Scene("Test scene");
        scene.setCamera(new Camera(new Point3D(0, 0, -1000), new Vector(0, 0, 1), new Vector(0, -1, 0)));
        scene.setDistance(1000);
        scene.setBackground(Color.BLACK);
        scene.setAmbientLight(new AmbientLight(new Color(java.awt.Color.WHITE), 0.15));

        scene.addGeometries(
                new Triangle(Color.BLACK, new Material(0.5, 0.5, 300),
                        new Point3D(-150, 150, 150), new Point3D(150, 150, 150), new Point3D(75, -75, 150)),
                new Triangle(Color.BLACK, new Material(0.5, 0.5, 300),
                        new Point3D(-150, 150, 150), new Point3D(-70, -70, 50), new Point3D(75, -75, 150)));

        scene.addLights(new PointLight(new Color(500, 250, 250),
                new Point3D(10, 10, 130),
                1, 0.0005, 0.0005));

        ImageWriter imageWriter = new ImageWriter("trianglesPoint", 200, 200, 500, 500);
        Render render = new Render(imageWriter, scene);

        render.renderImage();
        render.writeToImage();
    }

    /**
     * Produce a picture of a two triangles lighted by a spot light
     */
    @Test
    public void trianglesSpot() {
        Scene scene = new Scene("Test scene");
        scene.setCamera(new Camera(new Point3D(0, 0, -1000), new Vector(0, 0, 1), new Vector(0, -1, 0)));
        scene.setDistance(1000);
        scene.setBackground(Color.BLACK);
        scene.setAmbientLight(new AmbientLight(new Color(java.awt.Color.WHITE), 0.15));

        scene.addGeometries(
                new Triangle(Color.BLACK, new Material(0.5, 0.5, 300),
                        new Point3D(-150, 150, 150), new Point3D(150, 150, 150), new Point3D(75, -75, 150)),
                new Triangle(Color.BLACK, new Material(0.5, 0.5, 300),
                        new Point3D(-150, 150, 150), new Point3D(-70, -70, 50), new Point3D(75, -75, 150)));

        scene.addLights(new SpotLight(new Color(500, 250, 250),
                new Point3D(10, 10, 130), new Vector(-2, 2, 1),
                1, 0.0001, 0.000005));

        ImageWriter imageWriter = new ImageWriter("trianglesSpot", 200, 200, 500, 500);
        Render render = new Render(imageWriter, scene);

        render.renderImage();
        render.writeToImage();
    }

    /**
     * Produce a picture of a two triangles lighted by multiple light sources
     */
    @Test
    public void trianglesMultipleLightSources() {
        Scene scene = new Scene("Test scene");
        scene.setCamera(new Camera(new Point3D(0, 0, -1000), new Vector(0, 0, 1), new Vector(0, -1, 0)));
        scene.setDistance(1000);
        scene.setBackground(Color.BLACK);
        scene.setAmbientLight(new AmbientLight(new Color(java.awt.Color.WHITE), 0.15));

        scene.addGeometries(
                new Triangle(Color.BLACK, new Material(0.8, 0.2, 300),
                        new Point3D(-150, 150, 150), new Point3D(150, 150, 150), new Point3D(75, -75, 150)),
                new Triangle(Color.BLACK, new Material(0.8, 0.2, 300),
                        new Point3D(-150, 150, 150), new Point3D(-70, -70, 50), new Point3D(75, -75, 150)));

        scene.addLights(new DirectionalLight(new Color(150, 75, 150), new Vector(0, 0, 1)));
        scene.addLights(new SpotLight(new Color(180, 130, 100),
                new Point3D(2, 18, 134), new Vector(-2, 2, 1),
                1, 0.0001, 0.000005));
        scene.addLights(new PointLight(new Color(190, 180, 120),
                new Point3D(60, 40, 130),
                1, 0.0005, 0.0005));

        ImageWriter imageWriter = new ImageWriter("triangleMultipleLightSources", 200, 200, 500, 500);
        Render render = new Render(imageWriter, scene);

        render.renderImage();
        render.writeToImage();
    }






    @Test
    public void testCalcColor() {
        //not necessary meanwhile
    }
}