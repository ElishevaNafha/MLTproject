package elements;

import primitives.Color;
import primitives.Point3D;
import primitives.Vector;

/**
 * class DirectionalLight implements a light source that has a direction and is far away (for example the sun)
 * @author Eliana Rabinowitz and Elisheva Nafha
 */
public class DirectionalLight extends Light implements LightSource {
    //fields
    /**
     * light's direction
     */
    Vector _direction;

    //constructors

    /**
     * constructor for directional light
     * @param direction light direction
     * @param intensity light intensity
     */
    public DirectionalLight(Color intensity, Vector direction){
        super(intensity);
        _direction = direction.normalized();
    }

    //functions
    @Override
    public Color getIntensity(Point3D p) {
        return _intensity;
    }

    @Override
    public Vector getL(Point3D p) {
        return _direction;
    }

    @Override
    public double getDistance(Point3D point) {
        return Double.POSITIVE_INFINITY;
    }
}
