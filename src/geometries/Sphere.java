package geometries;

import primitives.*;
import primitives.Color;
import primitives.Vector;

import java.util.*;
import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * Sphere class represents a sphere in 3D Cartesian coordinate system
 * @author Eliana Rabinowitz and Elisheva Nafha
 */
public class Sphere extends RadialGeometry{
    //fields
    Point3D _center;

    //constructors
    /**
     * Sphere constructor based on its radius and center point
     * @param radius the length of the radius of the sphere
     * @param center the center of the sphere
     */
    public Sphere(double radius, Point3D center){
        super(radius);
        _center = center;
        createVirtualBox();
    }

    /**
     * Sphere constructor based on its radius and center point and color
     * @param radius the length of the radius of the sphere
     * @param center the center of the sphere
     * @param emission the emission of the sphere
     */
    public Sphere(Color emission, double radius, Point3D center){
        super(emission,radius);
        _center = center;
        createVirtualBox();
    }

    /**
     * Sphere constructor based on its radius and center point and color
     * @param radius the length of the radius of the sphere
     * @param center the center of the sphere
     * @param emission the emission of the sphere
     * @param material the material of the sphere
     */
    public Sphere(Color emission, Material material, double radius, Point3D center){
        super(material, emission, radius);
        _center = center;
        createVirtualBox();
    }

    /**
     * getter for center point of sphere
     * @return center of sphere
     */
    public Point3D getCenter() {
        return _center;
    }

    //basic overrides
    @Override
    public String toString(){
        return "Radius of sphere" + _radius + ", center of sphere:" + _center.toString();
    }

    //functions
    @Override
    public Vector getNormal(Point3D point3D) {
        return point3D.subtract(_center).normalize();
    }

    @Override
    public List<GeoPoint> findIntersections(Ray ray) {
            ArrayList<GeoPoint> intersections = new ArrayList<>();
            Point3D p0 = ray.getStartPoint();
            Point3D p1, p2, a, b;
            Vector v = ray.getVector();
            double r = getRadius();
            Point3D o = getCenter();

            // boundary value - if the ray starts at the center of the sphere, regular calculation doesn't work (see below)
            if(o.equals(p0)) //For some reason you can't have a zero vector so the normal calculation doesn't work when the ray starts in the center
            {
                List<GeoPoint> temp = new ArrayList<>();
                temp.add(new GeoPoint(this, p0.add(v.scale(r))));
                return temp;
            }

            Vector u = o.subtract(p0);
            double tm = v.dotProduct(u);
            double d = Math.sqrt(u.dotProduct(u) - tm*tm);

            //if no intersections found, return null
            if(d>r)
                return null;

            double th = Math.sqrt(r*r-d*d);
            double t1 = alignZero(tm+th);
            double t2 = alignZero(tm-th);

            // if ray intersects the sphere not on the starting point
            if(t1>0)
            {
                double x = ray.getVector().getEndpoint().getX().get();
                double y = ray.getVector().getEndpoint().getY().get();
                double z = ray.getVector().getEndpoint().getZ().get();
                if (!(isZero(x * t1) && isZero(y * t1) && isZero(z * t1))) {
                    p1 = ray.getPoint(t1);
                    a = new Point3D(p1);
                    if (ray.getVector().dotProduct(a.subtract(o)) != 0)
                        intersections.add(new GeoPoint(this, p1));
                }
            }

            // if ray intersects the sphere not on the starting point and in a different point
            if((t2>0)&&(t1!=t2))
            {
                // make sure the vector created is not zero vector
                double x = ray.getVector().getEndpoint().getX().get();
                double y = ray.getVector().getEndpoint().getY().get();
                double z = ray.getVector().getEndpoint().getZ().get();
                if (!(isZero(x * t2) && isZero(y * t2) && isZero(z * t2))) {
                    p2 = ray.getPoint(t2);
                    a = new Point3D(p2);
                    if (ray.getVector().dotProduct(a.subtract(o)) != 0)
                        intersections.add(new GeoPoint(this, p2));
                }
            }
            if(intersections.isEmpty())
                return null;
            return intersections;
        }

    @Override
    public void createVirtualBox() {
        _virtualBox = new VirtualBox(new Coordinate(_center.getX().get()-_radius),
                new Coordinate(_center.getX().get()+_radius),
                new Coordinate(_center.getY().get()-_radius),
                new Coordinate(_center.getY().get()+_radius),
                new Coordinate(_center.getZ().get()-_radius),
                new Coordinate(_center.getZ().get()+_radius));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof Sphere)) return false;
        return (super.equals(obj) && _center.equals(((Sphere)obj)._center));
    }
}
