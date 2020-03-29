package geometries;

import primitives.Ray;
/**
 * RadialGeometry class represnts 3d shapes that are based on a radius
 * @author Eliana Rabinowitz and Elisheva Nafha
 */
public abstract class RadialGeometry implements Geometry{
    //fields
    protected double _radius;

    /**
     * RadialGeometry constructor, receives the length of the radius
     * @param radius the radius
     */
    public RadialGeometry(double radius){
        _radius = radius;
    }

    /**
     * copy constructor for RadialGeometry
     * @param other RadialGeometry to copy
     */
    public RadialGeometry(RadialGeometry other) {
        _radius=other._radius;
    }

    /**
     * getter for radius
     * @return the value of radius
     */
    public double getRadius() {
        return _radius;
    }
}
