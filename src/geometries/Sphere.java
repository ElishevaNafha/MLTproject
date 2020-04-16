package geometries;

import primitives.*;

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
        return null;
    }
}
