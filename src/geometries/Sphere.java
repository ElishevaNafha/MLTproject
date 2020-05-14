package geometries;

import primitives.*;
import primitives.Vector;
import static java.lang.System.out;

import java.awt.*;
import java.util.*;
import java.util.List;

import static primitives.Util.alignZero;

/**
 * Sphere class represents a sphere in 3D Cartesian coordinate system
 * @author Eliana Rabinowitz and Elisheva Nafha
 */
public class Sphere extends RadialGeometry {
    //fields
    Point3D _center;

    /**
     * Sphere constructor based on its radius and center point
     * @param radius the length of the radius of the sphere
     * @param center the center of the sphere
     */
    public Sphere(double radius, Point3D center){
        super(radius);
        _center = center;
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
    public List<Point3D> findIntersections(Ray ray)
        {
            ArrayList<Point3D> intersections = new ArrayList<>();
            Point3D p0 = ray.getStartPoint();
            Point3D p1, p2, a, b;
            Vector v = ray.getVector();
            double r = getRadius();
            Point3D o = getCenter();

            // boundary value - if the ray starts at the center of the sphere, regular calculation doesn't work (see below)
            if(o.equals(p0)) //For some reason you can't have a zero vector so the normal calculation doesn't work when the ray starts in the center
            {
                return List.of(p0.add(v.scale(r)));
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
                p1 = ray.getPoint(t1);
                a = new Point3D(p1);
                if(ray.getVector().dotProduct(a.subtract(o))!=0)
                 intersections.add(p1);
            }

            // if ray intersects the sphere not on the starting point and in a different point
            if((t2>0)&&(t1!=t2))
            {
                p2 = ray.getPoint(t2);
                a = new Point3D(p2);
                if(ray.getVector().dotProduct(a.subtract(o))!=0)
                    intersections.add(p2);
            }
            if(intersections.isEmpty())
                return null;
            return intersections;
        }
    }
