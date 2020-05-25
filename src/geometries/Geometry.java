package geometries;

import primitives.*;

/**
 * Geometry interface represents a geometry in 3D Cartesian coordinate system
 * @author Eliana Rabinowitz and Elisheva Nafha
 */
public abstract class Geometry implements Intersectable {
    /**
     * finds a normal to the geometry at the given point
     *
     * @param point3D a point on the shape
     * @return the normal to the shape from the point given
     */
    protected Color _emission;

    /**
     * Constructor for Geometry
     * @param emission
     */
    public Geometry(Color emission) {
        this._emission = emission;
    }

    /**
     * default constructor for Geometry. Gives emission the value BLACK
     */
    public Geometry() {
        this._emission = Color.BLACK;
    }

    /**
     * getter for emission of geometry
     * @return emission
     */
    public Color getEmission() {
        return _emission;
    }

    public abstract Vector getNormal(Point3D point3D);
}
