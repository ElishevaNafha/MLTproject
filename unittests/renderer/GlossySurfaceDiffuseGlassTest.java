package renderer;

import elements.AmbientLight;
import elements.Camera;
import elements.SpotLight;
import geometries.Plane;
import geometries.Polygon;
import geometries.Sphere;
import geometries.Triangle;
import org.junit.Test;
import primitives.Color;
import primitives.Material;
import primitives.Point3D;
import primitives.Vector;
import scene.Scene;

/**
 * test rendering images with glossy surfaces and diffuse glasses
 *
 * @author Eliana Rabinowitz and Elisheva Nafha
 */
public class GlossySurfaceDiffuseGlassTest {

    @Test
    public void complexPicture(){
        Scene scene = new Scene("Test scene");
        scene.setCamera(new Camera(new Point3D(0, 0, -1000), new Vector(0, 0, 1), new Vector(0, -1, 0)));
        scene.setDistance(1000);
        scene.setBackground(new Color(new java.awt.Color(232, 203, 148)));
        scene.setAmbientLight(new AmbientLight(new Color(java.awt.Color.WHITE), 0.15));

        scene.addGeometries(
                //floor
                new Plane(new Material(0.5,0.5,60, 0, 0), new Color(new java.awt.Color(70,50,50)),
                        new Point3D(0, 100,0), new Vector(0,-1,0)),
                // front wall
                //new Plane(new Material(0.5,0.5,60, 0,0), new Color(new java.awt.Color(120,120,120)),
                  //      new Point3D(0, 0,5000), new Vector(0,0,-1)),
                // side wall
                //new Plane(new Material(0.5,0.5,60, 0, 0), new Color(new java.awt.Color(120,120,120)),
                        //new Point3D(150, 0,0), new Vector(-1,0,0)),
                // blue transparent sphere
                new Sphere(new Color(java.awt.Color.blue), new Material(0.2, 0.2, 30, 0, 0, 30,30), // )
                        30, new Point3D(70, 70, 700)),
                // red sphere
                new Sphere(new Color(java.awt.Color.red), new Material(0.2, 0.2, 30, 0, 0), // )
                        15, new Point3D(30, 85, 550)),
                //new Polygon(new Material(0.3, 0.2, 30, 1, 0, 0,100), Color.BLACK,
                  //      new Point3D(30, 100, 500), new Point3D(70, 100, 500), new Point3D(70, 20, 500), new Point3D(30, 20, 500)),
                new Polygon(new Material(0.05, 0.9, 30, 0, 1, 60,0), Color.BLACK,
                        new Point3D(-30, 100, 550), new Point3D(130, 100, 920), new Point3D(130, 20, 920), new Point3D(-30, 20, 550))

        // front triangle pyramid
                //new Triangle(new Color(new java.awt.Color(100,130,100)), new Material(0.1, 0.7, 10, 0, 0),
                  //      new Point3D(-50, 100, 500), new Point3D(50, 100, 600), new Point3D(-20, 0, 700)),
                // left side triangle pyramid
                //new Triangle(new Color(new java.awt.Color(100,130,100)), new Material(0.1, 0.7, 10, 0, 0),
                  //      new Point3D(-50, 100, 500), new Point3D(-130, 100, 1100), new Point3D(-20, 0, 700)),
                // right side triangle pyramid
                //new Triangle(new Color(new java.awt.Color(100,130,100)), new Material(0.1, 0.7, 10, 0, 0),
                  //      new Point3D(50, 100, 600), new Point3D(-30, 100, 1200), new Point3D(-20, 0, 700))

        );

        scene.addLights(
               // new SpotLight(new Color(220, 220, 150), //
                 //       new Point3D(120, -100, 0), new Vector(-1, 1, -1), 1, 4E-5, 2E-7),
                new SpotLight(new Color(400, 260, 260), //
                        new Point3D(0, -200, 200), new Vector(1, 1, 0), 1, 4E-5, 2E-7)

        );

        ImageWriter imageWriter = new ImageWriter("GlossyDiffusiveTest", 200, 200, 600, 600);
        Render render = new Render(imageWriter, scene);

        render.renderImage();
        render.writeToImage();

    }

}
