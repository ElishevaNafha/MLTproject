package geometries;
import primitives.*;

import java.util.List;

/**
 * Intersectable interface represents geometries that can be intersected with rays
 * @author Eliana Rabinowitz and Elisheva Nafha
 */
public abstract class Intersectable {

    /**
     * A virtual box surrounding the intersectable object
     */
    protected VirtualBox _virtualBox;

    /**
     * create virtual box around the intersectable and assigns it to virtualBox
     */
    abstract protected void createVirtualBox();

    /**
     * getter for virtualBox
     * @return virtualBox
     */
    public VirtualBox getVirtualBox() {
        if (_virtualBox == null)
            createVirtualBox();
        return _virtualBox;
    }

    /**
     * Finds all intersections between the intersectable and a given ray
     * @param ray the ray that intersects the intersectable
     * @return a list of all found intersections
     */
    abstract List<GeoPoint> findIntersections(Ray ray);

    /**
     * class GeoPoint represents a point with the geometry it is part of
     */
    public static class GeoPoint {
        public Geometry geometry;
        public Point3D point;

        /**
         * constructor for GeoPoint
         * @param geometry the geometry the point is part of
         * @param point a 3d point
         */
        public GeoPoint(Geometry geometry, Point3D point) {
            this.geometry = geometry;
            this.point = point;
        }
        //basic overrides
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (!(obj instanceof GeoPoint)) return false;
            GeoPoint oth = (GeoPoint) obj;
            return geometry.equals(oth.geometry) && point.equals(oth.point);
        }
    }
}

