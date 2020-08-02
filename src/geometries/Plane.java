package geometries;
import primitives.*;
import static primitives.Util.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Plane class represents a plane in 3D Cartesian coordinate system
 * @author Eliana Rabinowitz and Elisheva Nafha
 */
public class Plane extends Geometry {
    //fields
    /**
     * reference point on plane
     */
    protected Point3D _point;
    /**
     * normal to plane
     */
    protected Vector _normal;

    //constructors
    /**
     * Plane constructor receiving 3 points on the plane
     *
     * @param point1 a point on the plane
     * @param point2 a point on the plane
     * @param point3 a point on the plane
     */
    public Plane(Point3D point1, Point3D point2, Point3D point3){
        _point = point1;

        //calculate normal
        Vector v1 = point2.subtract(point1);
        Vector v2 = point3.subtract(point1);
        _normal = v1.crossProduct(v2).normalize();

    }
    /**
     * Plane constructor receiving a point and the normal vector to the plane
     *
     * @param point a point on the plane
     * @param normal the normal to the plane
     */
    public Plane(Point3D point, Vector normal){
        _point = point;
        _normal = normal.normalized();
    }
    /**
     * Plane constructor receiving 3 points on the plane and color
     *@param emission the emission of the plane
     * @param point1 a point on the plane
     * @param point2 a point on the plane
     * @param point3 a point on the plane
     */
    public Plane(Color emission,Point3D point1, Point3D point2, Point3D point3){
        super(emission);
        _point = point1;
        //calculate normal
        Vector v1 = point2.subtract(point1);
        Vector v2 = point3.subtract(point1);
        _normal = v1.crossProduct(v2).normalize();
    }

    /**
     * Plane constructor receiving a point and the normal vector to the plane and color
     * @param emission the emission of the plane
     * @param point a point on the plane
     * @param normal the normal to the plane
     */
    public Plane(Color emission,Point3D point, Vector normal){
        super(emission);
        _point = point;
        _normal = normal.normalized();
    }

    /**
     * Plane constructor receiving 3 points on the plane and color
     * @param material the material of the plane
     * @param emission the emission of the plane
     * @param point1 a point on the plane
     * @param point2 a point on the plane
     * @param point3 a point on the plane
     */
    public Plane(Material material, Color emission,Point3D point1, Point3D point2, Point3D point3){
        super(emission, material);
        _point = point1;
        //calculate normal
        Vector v1 = point2.subtract(point1);
        Vector v2 = point3.subtract(point1);
        _normal = v1.crossProduct(v2).normalize();
    }

    /**
     * Plane constructor receiving a point and the normal vector to the plane and color
     * @param material the material of the plane
     * @param emission the emission of the plane
     * @param point a point on the plane
     * @param normal the normal to the plane
     */
    public Plane(Material material, Color emission, Point3D point, Vector normal){
        super(emission, material);
        _point = point;
        _normal = normal.normalized();
    }

    //getters
    /**
     * getter for plane's normal
     * @return normal to the plane
     */
    public Vector getNormal() {
        return _normal;
    }
    /**
     * getter for plane's reference point
     * @return a point on the plane
     */
    public Point3D getPoint() {
        return _point;
    }

    //basic overrides
    @Override
    public String toString(){
        return "Normal to plane:" + _normal.toString() + ", reference point:" + _point.toString();
    }

    //functions
    @Override
    public Vector getNormal(Point3D point3D) {
        return _normal;
    }

    @Override
    public List<GeoPoint> findIntersections(Ray ray) {
        //check if ray is parallel to plane
        if (alignZero(ray.getVector().dotProduct(_normal)) == 0)
            return null;

        //rename calculation parameters
        Vector N = _normal;
        Point3D Q0 = _point;
        Vector V = ray.getVector();
        Point3D P0 = ray.getStartPoint();

        //calculate t
        double t;
        try
        { t = alignZero(N.dotProduct(Q0.subtract(P0))) / N.dotProduct(V); }
        catch (IllegalArgumentException e)
        { return null; }

        //check if intersection point is on the plane or on the ray's negative side
        if (t <= 0)
            return null;
        double x = ray.getVector().getEndpoint().getX().get();
        double y = ray.getVector().getEndpoint().getY().get();
        double z = ray.getVector().getEndpoint().getZ().get();
        if (isZero(x * t) && isZero(y * t) && isZero(z * t))
            return null;

        //return intersection point
        List<GeoPoint> intersection = new ArrayList<>();
        intersection.add(new GeoPoint(this,ray.getPoint(t)));
        return intersection;
    }

    @Override
    public void createVirtualBox() {
        //Plane is an infinite geometry and therefore doesn't have edges and can't have a virtual box
        _virtualBox = null;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof Plane)) return false;
        return (_point.equals(((Plane)obj)._point) && (_normal.equals(((Plane)obj)._normal)));
    }

}
