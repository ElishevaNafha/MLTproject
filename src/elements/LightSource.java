package elements;

import primitives.Color;
import primitives.Point3D;
import primitives.Vector;

/**
 * class LightSource represents a light source _insert_something_descriptive_here_
 */
public interface LightSource {


    /**
     * returns intensity of light source
     * @param p
     * @return
     */
    public Color getIntensity(Point3D p);

    /**
     *
     * @param p
     * @return
     */
    public Vector getL(Point3D p);

    /**
     * @param point
     * @return distance from lightsource to point
     */
    double getDistance(Point3D point);


}
