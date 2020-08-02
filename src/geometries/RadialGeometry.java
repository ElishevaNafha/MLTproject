package geometries;

import primitives.Color;
import primitives.Coordinate;
import primitives.Material;
import primitives.Ray;

import static primitives.Util.isZero;

/**
 * RadialGeometry class represents 3d shapes that are based on a radius
 * @author Eliana Rabinowitz and Elisheva Nafha
 */
public abstract class RadialGeometry extends Geometry{
    //fields
    protected double _radius;

    //constructors
    /**
     * RadialGeometry constructor, receives the length of the radius
     * @param radius the radius
     */
    public RadialGeometry(double radius){
        _radius = radius;
    }

    /**
     * RadialGeometry constructor, receives the length of the radius and color
     * @param radius the radius
     * @param emission the emission of the geometry
     */
    public RadialGeometry(Color emission, double radius){
        super(emission);
        _radius = radius;
    }

    /**
     * RadialGeometry constructor, receives the length of the radius and color
     * @param radius the radius
     * @param emission the emission of the geometry
     * @param material the material of the geometry
     */
    public RadialGeometry(Material material, Color emission, double radius){
        super(emission, material);
        _radius = radius;
    }

    /**
     * copy constructor for RadialGeometry with color
     * @param other RadialGeometry to copy
     */
    public RadialGeometry(RadialGeometry other) {
        super(other._emission, other._material);
        _radius = other._radius;
    }

    /**
     * getter for radius
     * @return the value of radius
     */
    public double getRadius() {
        return _radius;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof RadialGeometry)) return false;
        return isZero(_radius - ((RadialGeometry)obj)._radius);
    }
}
