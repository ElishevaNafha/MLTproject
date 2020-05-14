package geometries;
import primitives.*;

import java.util.List;

/**
 * Intersectable interface represents geometries that can be intersected with rays
 * @author Eliana Rabinowitz and Elisheva Nafha
 */
public interface Intersectable {
    /**
     * Finds all intersections between the intersectable and a given ray
     * @param ray the ray that intersects the intersectable
     * @return a list of all found intersections
     */
    List<Point3D> findIntersections(Ray ray);
}
