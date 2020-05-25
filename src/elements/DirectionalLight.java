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
    Vector _direction;

    //constructors

    /**
     * constructor for directional light
     * @param direction
     * @param intensity
     */
    public DirectionalLight(Color intensity, Vector direction){
        super(intensity);
        _direction = direction.normalized();
    }

    //functions
    @Override
    /**
     * getter for intensity of light at specific point
     * @return intensity of light
     */
    public Color getIntensity(Point3D p) {
        return _intensity;
    }

    /**
     * @param p
     * @return direction of light
     */
    @Override
    public Vector getL(Point3D p) {
        return _direction;
    }
}
