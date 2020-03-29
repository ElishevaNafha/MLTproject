package geometries;

import primitives.*;

/**
 * Cylinder class represents a cylinder in 3D Cartesian coordinate system
 * @author Eliana Rabinowitz and Elisheva Nafha
 */
public class Cylinder extends Tube{
    //fields
    protected double _height;

    /**
     * Cylinder constructor
     * @param radius
     * @param axisRay
     * @param height
     */
    public Cylinder(double radius, Ray axisRay, double height){
        super(radius, axisRay);
        _height=height;
    }

    /**
     * getter for the height of the cylinder
     * @return height of the cylinder
     */
    public double getHeight() {
        return _height;
    }

    //basic overrides
    @Override
    public String toString(){
        return "Radius of cylinder" + _radius + ", axis ray of cylinder:" + _axisRay.toString()+ ", height of cylinder"+_height;
    }

    //functions
    @Override
    public Vector getNormal(Point3D point3D) {
        return null;
    }
}
