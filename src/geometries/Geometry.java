package geometries;

import primitives.*;


public interface Geometry {
    /**
     * @param point3D a point on the shape
     * @return the normal to the shape from the point given
     */
    public Vector getNormal(Point3D point3D);
}
