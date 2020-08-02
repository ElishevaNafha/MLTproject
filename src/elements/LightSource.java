package elements;

import primitives.Color;
import primitives.Point3D;
import primitives.Vector;

/**
 * class LightSource represents a light source in the scene
 */
public interface LightSource {


    /**
     * returns intensity of light source
     * @param p point to calculate intensity at
     * @return intensity of light source
     */
    public Color getIntensity(Point3D p);

    /**
     * returns vector from lights source to point
     * @param p point to calculate intensity at
     * @return vector from light source to point
     */
    public Vector getL(Point3D p);

    /**
     * @param point point to calculate distance for
     * @return distance from light source to point
     */
    double getDistance(Point3D point);
}
