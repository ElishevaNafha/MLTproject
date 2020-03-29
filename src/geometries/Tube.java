package geometries;

import primitives.*;
/**
 * Tube class represents a tube in 3D Cartesian coordinate system
 * @author Eliana Rabinowitz and Elisheva Nafha
 */
public class Tube extends RadialGeometry {
    //fields
    Ray _axisRay;

    /**
     * Tube constructor
     * @param radius
     * @param axisRay
     */
    public Tube(double radius, Ray axisRay){
        super(radius);
        _axisRay = axisRay;
    }

    /**
     * getter for axis ray
     * @return axis ray
     */
    public Ray getAxisRay() {
        return _axisRay;
    }

    //basic overrides
    @Override
    public String toString(){
        return "Radius of tube" + _radius + ", axis ray of tube:" + _axisRay.toString();
    }

    //functions
    @Override
    public Vector getNormal(Point3D point3D) {
        return null;
    }
}
