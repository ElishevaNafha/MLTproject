package elements;

import primitives.Color;
import primitives.Point3D;
import primitives.Vector;

/**
 * PointLight class models omni-directional point source (such as a bulb)
 * @author Eliana Rabinowitz and Elisheva Nafha
 */
public class PointLight extends Light implements LightSource {
    //fields
    protected Point3D _position;
    protected double _kC, _kQ, _kL;

    //constructors

    /**
     * constructor for point light
     * @param position position of light
     * @param kC attenuation factor
     * @param kQ attenuation factor
     * @param kL attenuation factor
     * @param intensity intensity of light at source
     */
    public PointLight(Color intensity, Point3D position, double kC, double kQ, double kL){
        super(intensity);
        _position = position;
        _kC = kC;
        _kL = kL;
        _kQ = kQ;
    }

    /**
     * @param p the point for which the intensity is being calculated
     * @return intensity of light source at point p
     */
    @Override
    public Color getIntensity(Point3D p) {
        double distance = p.distance(_position);
        double attenuation = 1/(_kC+ distance*_kL + distance*distance*_kQ);
        return _intensity.scale(attenuation);
    }

    /**
     * @param p
     * @return vector from light source to point p
     */
    @Override
    public Vector getL(Point3D p) {
        Vector L = new Vector(p.subtract(_position));
        return L.normalize();
    }
}
