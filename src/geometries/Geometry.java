package geometries;

import primitives.*;

/**
 * Geometry interface represents a geometry in 3D Cartesian coordinate system
 * @author Eliana Rabinowitz and Elisheva Nafha
 */
public abstract class Geometry extends Intersectable {

    //fields
    /**
     * geometry's emission (color)
     */
    protected Color _emission;
    /**
     * geometry's material
     */
    protected Material _material;

    /**
     * Constructor for Geometry
     * @param emission geometry's emission
     */
    public Geometry(Color emission) {
        _emission = emission;
        _material = new Material(0,0,0);
    }

    /**
     * default constructor for Geometry. Gives emission the value BLACK
     */
    public Geometry() {
        _emission = Color.BLACK;
        _material = new Material(0,0,0);
    }

    /**
     * Constructor for Geometry
     * @param emission geometry's emission
     * @param material geometry's material
     */
    public Geometry(Color emission, Material material) {
        _emission = emission;
        _material = material;
    }

    /**
     * getter for emission of geometry
     * @return emission
     */
    public Color getEmission() {
        return _emission;
    }

    /**
     * getter for material of geometry
     * @return material
     */
    public Material getMaterial() {
        return _material;
    }

    /**
     * finds a normal to the geometry at the given point
     *
     * @param point3D a point on the shape
     * @return the normal to the shape from the point given
     */
    public abstract Vector getNormal(Point3D point3D);
}
