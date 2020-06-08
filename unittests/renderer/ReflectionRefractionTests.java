/**
 *
 */
package renderer;

import geometries.Plane;
import geometries.Polygon;
import org.junit.Test;

import elements.*;
import geometries.Sphere;
import geometries.Triangle;
import primitives.*;
import renderer.*;
import scene.Scene;

/**
 * Tests for reflection and transparency functionality, test for partial shadows
 * (with transparency)
 *
 * @author dzilb
 *
 */
public class ReflectionRefractionTests {

    /**
     * Produce a picture of a sphere lighted by a spot light
     */
    @Test
    public void twoSpheres() {
        Scene scene = new Scene("Test scene");
        scene.setCamera(new Camera(new Point3D(0, 0, -1000), new Vector(0, 0, 1), new Vector(0, -1, 0)));
        scene.setDistance(1000);
        scene.setBackground(Color.BLACK);
        scene.setAmbientLight(new AmbientLight(Color.BLACK, 0));

        scene.addGeometries(
                new Sphere(new Color(java.awt.Color.BLUE), new Material(0.4, 0.3, 100, 0.3, 0), 50,
                        new Point3D(0, 0, 50)),
                new Sphere(new Color(java.awt.Color.RED), new Material(0.5, 0.5, 100), 25, new Point3D(0, 0, 50)));

        scene.addLights(new SpotLight(new Color(1000, 600, 0), new Point3D(-100, 100, -500), new Vector(-1, 1, 2), 1,
                0.0004, 0.0000006));

        ImageWriter imageWriter = new ImageWriter("twoSpheres", 150, 150, 500, 500);
        Render render = new Render(imageWriter, scene);

        render.renderImage();
        render.writeToImage();
    }

    /**
     * Produce a picture of a sphere lighted by a spot light
     */
    @Test
    public void twoSpheresOnMirrors() {
        Scene scene = new Scene("Test scene");
        scene.setCamera(new Camera(new Point3D(0, 0, -10000), new Vector(0, 0, 1), new Vector(0, -1, 0)));
        scene.setDistance(10000);
        scene.setBackground(Color.BLACK);
        scene.setAmbientLight(new AmbientLight(new Color(255, 255, 255), 0.1));

        scene.addGeometries(
                new Sphere(new Color(0, 0, 100), new Material(0.25, 0.25, 20, 0.5, 0), 400, new Point3D(-950, 900, 1000)),
                new Sphere(new Color(100, 20, 20), new Material(0.25, 0.25, 20), 200, new Point3D(-950, 900, 1000)),
                new Triangle(new Color(20, 20, 20), new Material(0, 0, 0, 0, 1), new Point3D(1500, 1500, 1500),
                        new Point3D(-1500, -1500, 1500), new Point3D(670, -670, -3000)),
                new Triangle(new Color(20, 20, 20), new Material(0, 0, 0, 0, 0.5), new Point3D(1500, 1500, 1500),
                        new Point3D(-1500, -1500, 1500), new Point3D(-1500, 1500, 2000)));

        scene.addLights(new SpotLight(new Color(1020, 400, 400),  new Point3D(-750, 750, 150),
                new Vector(-1, 1, 4), 1, 0.00001, 0.000005));

        ImageWriter imageWriter = new ImageWriter("twoSpheresMirrored", 2500, 2500, 500, 500);
        Render render = new Render(imageWriter, scene);

        render.renderImage();
        render.writeToImage();
    }

    /**
     * Produce a picture of a two triangles lighted by a spot light with a partially transparent Sphere
     *  producing partial shadow
     */
    @Test
    public void trianglesTransparentSphere() {
        Scene scene = new Scene("Test scene");
        scene.setCamera(new Camera(new Point3D(0, 0, -1000), new Vector(0, 0, 1), new Vector(0, -1, 0)));
        scene.setDistance(1000);
        scene.setBackground(Color.BLACK);
        scene.setAmbientLight(new AmbientLight(new Color(java.awt.Color.WHITE), 0.15));

        scene.addGeometries( //
                new Triangle(Color.BLACK, new Material(0.5, 0.5, 60), //
                        new Point3D(-150, 150, 115), new Point3D(150, 150, 135), new Point3D(75, -75, 150)), //
                new Triangle(Color.BLACK, new Material(0.5, 0.5, 60), //
                        new Point3D(-150, 150, 115), new Point3D(-70, -70, 140), new Point3D(75, -75, 150)), //
                new Sphere(new Color(java.awt.Color.BLUE), new Material(0.2, 0.2, 30, 0.6, 0), // )
                        30, new Point3D(60, -50, 50)));

        scene.addLights(new SpotLight(new Color(700, 400, 400), //
                new Point3D(60, -50, 0), new Vector(0, 0, 1), 1, 4E-5, 2E-7));

        ImageWriter imageWriter = new ImageWriter("shadow with transparency", 200, 200, 600, 600);
        Render render = new Render(imageWriter, scene);

        render.renderImage();
        render.writeToImage();
    }

    @Test
    public void complexPicture(){
        Scene scene = new Scene("Test scene");
        scene.setCamera(new Camera(new Point3D(0, 0, -1000), new Vector(0, 0, 1), new Vector(0, -1, 0)));
        scene.setDistance(1000);
        scene.setBackground(Color.BLACK);
        scene.setAmbientLight(new AmbientLight(new Color(java.awt.Color.WHITE), 0.15));

        scene.addGeometries(
                //floor
                new Plane(new Material(0.5,0.5,60, 0, 0.6), new Color(new java.awt.Color(70,50,50)),
                        new Point3D(0, 100,0), new Vector(0,-1,0)),
                // front wall
                new Plane(new Material(0.5,0.5,60, 0,0), new Color(new java.awt.Color(120,120,120)),
                        new Point3D(0, 0,5000), new Vector(0,0,-1)),
                // side wall
                new Plane(new Material(0.5,0.5,60, 0, 0), new Color(new java.awt.Color(120,120,120)),
                        new Point3D(150, 0,0), new Vector(-1,0,0)),
                // blue transparent sphere
                new Sphere(new Color(java.awt.Color.BLUE), new Material(0.2, 0.2, 30, 0.6, 0), // )
                        30, new Point3D(70, 70, 700)),
                // red sphere
                new Sphere(new Color(java.awt.Color.red), new Material(0.2, 0.2, 30, 0.3, 0), // )
                        20, new Point3D(30, 80, 550)),
                // red sphere
                new Sphere(new Color(new java.awt.Color(160,160,0)), new Material(0.2, 0.2, 30, 0, 0), // )
                        10, new Point3D(-62, 90, 480)),
                // red sphere
                new Sphere(new Color(new java.awt.Color(160,50,0)), new Material(0.2, 0.2, 30, 0.4, 0), // )
                        5, new Point3D(-54, 95, 450)),
                // front triangle pyramid
                new Triangle(new Color(new java.awt.Color(100,130,100)), new Material(0.1, 0.7, 10, 0, 0),
                        new Point3D(-50, 100, 500), new Point3D(50, 100, 600), new Point3D(-20, 0, 700)),
                // left side triangle pyramid
                new Triangle(new Color(new java.awt.Color(100,130,100)), new Material(0.1, 0.7, 10, 0, 0),
                        new Point3D(-50, 100, 500), new Point3D(-130, 100, 1100), new Point3D(-20, 0, 700)),
                // right side triangle pyramid
                new Triangle(new Color(new java.awt.Color(100,130,100)), new Material(0.1, 0.7, 10, 0, 0),
                        new Point3D(50, 100, 600), new Point3D(-30, 100, 1200), new Point3D(-20, 0, 700)),
                new Polygon(new Material(0.1, 0.7, 10, 0, 1), new Color(new java.awt.Color(0,0,0)),
                        new Point3D(0, 100, 1700), new Point3D(100, 100, 1700), new Point3D(100, -100, 1700), new Point3D(0, -100, 1700)) //, new Point3D(150, -100, 700)
                );

        scene.addLights(
                new SpotLight(new Color(220, 220, 150), //
                    new Point3D(120, -100, 0), new Vector(-1, 1, -1), 1, 4E-5, 2E-7),
                new SpotLight(new Color(300, 180, 180), //
                        new Point3D(120, -200, 500), new Vector(-1, 1, 1), 1, 4E-5, 2E-7)

                );

        ImageWriter imageWriter = new ImageWriter("complex step 7", 200, 200, 600, 600);
        Render render = new Render(imageWriter, scene);

        render.renderImage();
        render.writeToImage();

    }
}
