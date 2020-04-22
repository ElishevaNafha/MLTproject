package geometries;

import primitives.*;

import java.util.List;

/**
 * Cylinder class represents a cylinder in 3D Cartesian coordinate system
 * @author Eliana Rabinowitz and Elisheva Nafha
 */
public class Cylinder extends Tube{
    //fields
    protected double _height;

    /**
     * Cylinder constructor
     * @param radius is cylinder's radius
     * @param axisRay is cylinder's axis direction and a point on its axis
     * @param height is cylinder's height
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
        Vector v = _axisRay.getVector().normalized();
        Point3D p0 = _axisRay.getStartPoint();

        //check whether point is on base
        Vector u = point3D.subtract(p0);
        if ((v.dotProduct(u) == 0) && (u.length() <= _radius))
            return v.scale(-1);

        //check whether point is on top
        u = point3D.subtract(p0.add(v.scale(_height)));
        if ((v.dotProduct(u) == 0) && (u.length() <= _radius))
            return v;

        //else, the point is on the middle
        return super.getNormal(point3D);
    }
    @Override
    public List<Point3D> findIntersections(Ray ray) {
        return null;}
}
