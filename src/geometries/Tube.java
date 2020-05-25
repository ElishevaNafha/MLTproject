package geometries;

import primitives.*;

import java.util.List;

/**
 * Tube class represents a tube in 3D Cartesian coordinate system
 * @author Eliana Rabinowitz and Elisheva Nafha
 */
public class Tube extends RadialGeometry {
    //fields
    Ray _axisRay;

    /**
     * Tube constructor
     * @param radius is tube's radius
     * @param axisRay is tube's direction and a point on the axis
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
        Vector v = _axisRay.getVector().normalized();
        Point3D p0 = _axisRay.getStartPoint();
        double t = v.dotProduct(point3D.subtract(p0));
        Point3D o = p0;
        if (t != 0)
             o = o.add(v.scale(t));
        return point3D.subtract(o).normalized();
    }

    @Override
    public List<GeoPoint> findIntersections(Ray ray) {
        return null;
    }
}
