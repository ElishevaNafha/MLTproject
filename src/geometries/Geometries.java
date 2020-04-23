package geometries;

import primitives.Point3D;
import primitives.Ray;
import java.util.ArrayList;
import java.util.List;

public class Geometries implements Intersectable {
    //fields
    /**
     * List of intersectables
     */
    List<Intersectable> _geometries;

    //constructors
    public Geometries() {
        this._geometries = new ArrayList();
    }

    public Geometries(Intersectable geometry) {
        this._geometries = new ArrayList();
        _geometries.add(geometry);
    }

    //functions
    public void add(Intersectable geometry)
    {
        _geometries.add(geometry);
    }

    @Override
    public List<Point3D> findIntersections(Ray ray) {
        return null;
    }
}
