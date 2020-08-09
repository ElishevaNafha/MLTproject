package renderer;

import elements.AmbientLight;
import elements.Camera;
import elements.PointLight;
import elements.SpotLight;
import geometries.*;
import org.junit.Test;
import primitives.Color;
import primitives.Material;
import primitives.Point3D;
import primitives.Vector;
import scene.Scene;

import java.util.ArrayList;
import java.util.List;

/**
 * test the entire project by building a final picture
 *
 * @author Eliana Rabinowitz and Elisheva Nafha
 */
public class FinalPicture {

    /**
     * build final picture
     */
    @Test
    public void finalTest(){
        Scene scene = new Scene("Test scene");
        scene.setCamera(new Camera(new Point3D(0, 0, -1000), new Vector(0, 0, 1), new Vector(0, -1, 0)));
        scene.setDistance(1000);
        scene.setBackground(new Color(new java.awt.Color(70,50,50)));
        scene.setAmbientLight(new AmbientLight(new Color(java.awt.Color.WHITE), 0.15));

        scene.addGeometries(
                //floor
                new Polygon(new Material(0.5,0.5,60, 0, 0.6, 0, 0), new Color(new java.awt.Color(70,50,50)),
                        new Point3D(-600,100,-100), new Point3D(-600,100,5000), new Point3D(150, 100, 5000), new Point3D(150, 100, -100)) ,
                // blue transparent sphere
                new Sphere(new Color(java.awt.Color.BLUE), new Material(0.2, 0.2, 30, 0.6, 0), // )
                        30, new Point3D(70, 70, 700)),
                // red sphere
                new Sphere(new Color(java.awt.Color.red), new Material(0.2, 0.2, 30, 0.3, 0), // )
                        20, new Point3D(30, 80, 550)),
                // yellow sphere
                new Sphere(new Color(new java.awt.Color(160,160,0)), new Material(0.2, 0.2, 30, 0, 0), // )
                        10, new Point3D(-62, 90, 480)),
                // orange sphere
                new Sphere(new Color(new java.awt.Color(160,50,0)), new Material(0.2, 0.2, 30, 0.4, 0), // )
                        5, new Point3D(-54, 95, 450)),
                // front triangle pyramid
                new Triangle(new Color(new java.awt.Color(100, 130, 100)), new Material(0.1, 1, 10, 0, 0,0,0),
                        new Point3D(-50, 100, 500), new Point3D(50, 100, 600), new Point3D(-20, 0, 700)),
                // left side triangle pyramid
                new Triangle(new Color(new java.awt.Color(100,130,100)), new Material(0.1, 0.7, 10, 0, 0),
                        new Point3D(-50, 100, 500), new Point3D(-130, 100, 1100), new Point3D(-20, 0, 700)),
                // right side triangle pyramid
                new Triangle(new Color(new java.awt.Color(100,130,100)), new Material(0.1, 0.7, 10, 0, 0,0,0),
                        new Point3D(50, 100, 600), new Point3D(-30, 100, 1200), new Point3D(-20, 0, 700)),
                // back triangle pyramid
                new Triangle(new Color(new java.awt.Color(100,130,100)), new Material(0.1, 0.7, 10, 0, 0,0,0),
                        new Point3D(-130, 100, 1100), new Point3D(-30, 100, 1200), new Point3D(-20, 0, 700)),
                //bottom triangle
                new Polygon(new Material(0.1, 0.7, 10, 0, 0,0,0), new Color(new java.awt.Color(55, 74, 55)),
                        new Point3D(-130, 99.9, 1100), new Point3D(-50, 99.9, 500), new Point3D(50, 99.9, 600), new Point3D(-30, 99.9, 1200)),

                //mirror polygon
                new Polygon(new Material(0.1, 0.7, 10, 0, 1,1700,0), new Color(new java.awt.Color(0,0,0)),
                        new Point3D(0, 100, 1700), new Point3D(100, 100, 1700), new Point3D(100, -100, 1700), new Point3D(0, -100, 1700)), //, new Point3D(150, -100, 700)
                //ice polygon
                new Polygon(new Material(0.1, 0.7, 10, 1, 0,0,400), new Color(new java.awt.Color(50,50,50)),
                        new Point3D(-100, 100, 440), new Point3D(-62, 100, 440), new Point3D(-62, 30, 440), new Point3D(-100, 30, 440)) //, new Point3D(150, -100, 700)

        );

        scene.addLights(
                new SpotLight(new Color(180, 180, 100), //
                        new Point3D(120, -100, 0), new Vector(-1, 1, -1), 1, 4E-5, 2E-7),
                new SpotLight(new Color(250, 140, 140), //
                        new Point3D(120, -200, 500), new Vector(-1, 1, 1), 1, 4E-5, 2E-7),
                new PointLight(new Color(70, 120, 80), //
                        new Point3D(-100, -80, 300), 1, 4E-3, 6E-5)
        );

        ImageWriter imageWriter = new ImageWriter("FinalPicture", 200, 200, 600, 600);
        Render render = new Render(imageWriter, scene,100);

        render.renderImage();
        render.writeToImage();

    }
}



