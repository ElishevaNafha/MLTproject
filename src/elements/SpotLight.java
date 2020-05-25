package elements;

import primitives.Color;
import primitives.Point3D;
import primitives.Vector;

/**
 * SpotLight class models point light source with direction (such as a luxo lamp)
 * @author Eliana Rabinowitz nd Elisheva Nafha
 */
public class SpotLight extends PointLight {
    //fields
    Vector _direction;

    //constructors

    /**
     * constructor for SpotLight
     * @param position position of light
     * @param kC attenuation factor
     * @param kQ attenuation factor
     * @param kL attenuation factor
     * @param intensity intensity of light at source
     * @param direction direction of light
     */
    public SpotLight(Color intensity, Point3D position, Vector direction, double kC, double kQ, double kL){
        super(intensity, position, kC, kQ, kL);
        _direction = direction.normalized();
    }

    //functions
    /**
     * @param p the point for which the intensity is being calculated
     * @return intensity of light source at point p
     */
    @Override
    public Color getIntensity(Point3D p) {
        Double d = _direction.dotProduct(getL(p));
        if(d>0)
            return super.getIntensity(p).scale(d);
        else
            return Color.BLACK;
    }
}
