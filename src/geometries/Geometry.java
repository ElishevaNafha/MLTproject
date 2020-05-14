package geometries;

import primitives.*;

/**
 * Geometry interface represents a geometry in 3D Cartesian coordinate system
 * @author Eliana Rabinowitz and Elisheva Nafha
 */
public interface Geometry extends Intersectable {
    /**
     * finds a normal to the geometry at the given point
     *
     * @param point3D a point on the shape
     * @return the normal to the shape from the point given
     */
    Vector getNormal(Point3D point3D);
}
