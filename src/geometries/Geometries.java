package geometries;

import primitives.Point3D;
import primitives.Ray;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Geometries class represents a collection of geometries in 3D Cartesian coordinate system
 * @author Eliana Rabinowitz and Elisheva Nafha
 */
public class Geometries implements Intersectable {
    //fields
    /**
     * List of intersectables
     */
    List<Intersectable> _geometries;

    //constructors

    /**
     * Geometries default constructor
     */
    public Geometries() {
        this._geometries = new ArrayList<>();
    }

    /**
     * Geometries constructor
     *
     * @param geometries a collection of geometries
     */
    public Geometries(Intersectable... geometries) {
        this._geometries = new ArrayList<>();
        Collections.addAll(_geometries, geometries);
    }

    //functions

    /**
     * add geometries to the geometries collection
     *
     * @param geometries new geometries
     */
    public void add(Intersectable... geometries)
    {
        Collections.addAll(_geometries, geometries);
    }

    @Override
    public List<Point3D> findIntersections(Ray ray) {
        List<Point3D> intersections = new ArrayList<>();
        List<Point3D> result;
        for(Intersectable geometry : this._geometries)
        {
            result = geometry.findIntersections(ray);
            if(result!=null)
                intersections.addAll(result);
        }
        if(intersections.isEmpty())
            return null;
        return intersections;
    }
}
