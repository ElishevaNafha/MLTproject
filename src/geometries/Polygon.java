package geometries;

import java.util.*;

import primitives.*;
import primitives.Vector;

import static primitives.Util.*;

/**
 * Polygon class represents two-dimensional polygon in 3D Cartesian coordinate
 * system
 *
 * @author Dan
 */
public class Polygon extends Geometry{
    /**
     * List of polygon's vertices
     */
    protected List<Point3D> _vertices;
    /**
     * Associated plane in which the polygon lays
     */
    protected Plane _plane;

    //constructors
    /**
     * Polygon constructor based on vertices list. The list must be ordered by edge
     * path. The polygon must be convex.
     *
     * @param vertices list of vertices according to their order by edge path
     * @throws IllegalArgumentException in any case of illegal combination of
     *                                  vertices:
     *                                  <ul>
     *                                  <li>Less than 3 vertices</li>
     *                                  <li>Consequent vertices are in the same
     *                                  point
     *                                  <li>The vertices are not in the same
     *                                  plane</li>
     *                                  <li>The order of vertices is not according
     *                                  to edge path</li>
     *
     *                                  <li>Three consequent vertices lay in the
     *                                  same line (180&#176; angle between two
     *                                  consequent edges)</li>
     *                                  <li>The polygon is concave (not convex)</li>
     *                                  </ul>
     */
    public Polygon(Point3D... vertices) {
        ctorsAssistant(vertices);
    }

    /**
     * Polygon constructor based on vertices list and color. The list must be ordered by edge
     * path. The polygon must be convex.
     *
     * @param emission geometry's emission
     * @param vertices list of vertices according to their order by edge path
     * @throws IllegalArgumentException in any case of illegal combination of
     *                                  vertices:
     *                                  <ul>
     *                                  <li>Less than 3 vertices</li>
     *                                  <li>Consequent vertices are in the same
     *                                  point
     *                                  <li>The vertices are not in the same
     *                                  plane</li>
     *                                  <li>The order of vertices is not according
     *                                  to edge path</li>
     *
     *                                  <li>Three consequent vertices lay in the
     *                                  same line (180&#176; angle between two
     *                                  consequent edges)</li>
     *                                  <li>The polygon is concave (not convex)</li>
     *                                  </ul>
     */
    public Polygon(Color emission,Point3D... vertices) {
        super(emission);
        ctorsAssistant(vertices);
    }

    /**
     * Polygon constructor based on vertices list, material and color. The list must be ordered by edge
     * path. The polygon must be convex.
     *
     * @param material polygon's material
     * @param emission polygon's emission
     * @param vertices list of vertices according to their order by edge path
     * @throws IllegalArgumentException in any case of illegal combination of
     *                                  vertices:
     *                                  <ul>
     *                                  <li>Less than 3 vertices</li>
     *                                  <li>Consequent vertices are in the same
     *                                  point
     *                                  <li>The vertices are not in the same
     *                                  plane</li>
     *                                  <li>The order of vertices is not according
     *                                  to edge path</li>
     *
     *                                  <li>Three consequent vertices lay in the
     *                                  same line (180&#176; angle between two
     *                                  consequent edges)</li>
     *                                  <li>The polygon is concave (not convex)</li>
     *                                  </ul>
     */
    public Polygon(Material material, Color emission,Point3D... vertices) {
        super(emission, material);
        ctorsAssistant(vertices);
    }

    /**
     * Polygon constructor helper based on vertices list. The list must be ordered by edge
     * path. The polygon must be convex.
     *
     * @param vertices list of vertices according to their order by edge path
     * @throws IllegalArgumentException in any case of illegal combination of
     *                                  vertices:
     *                                  <ul>
     *                                  <li>Less than 3 vertices</li>
     *                                  <li>Consequent vertices are in the same
     *                                  point
     *                                  <li>The vertices are not in the same
     *                                  plane</li>
     *                                  <li>The order of vertices is not according
     *                                  to edge path</li>
     *
     *                                  <li>Three consequent vertices lay in the
     *                                  same line (180&#176; angle between two
     *                                  consequent edges)</li>
     *                                  <li>The polygon is concave (not convex)</li>
     *                                  </ul>
     */
    private void ctorsAssistant(Point3D... vertices){
        if (vertices.length < 3)
            throw new IllegalArgumentException("A polygon can't have less than 3 vertices");
        _vertices = new LinkedList<>();
        Collections.addAll(_vertices, vertices);
        // Generate the plane according to the first three vertices and associate the
        // polygon with this plane.
        // The plane holds the invariant normal (orthogonal unit) vector to the polygon
        _plane = new Plane(vertices[0], vertices[1], vertices[2]);
        if (vertices.length == 3) return; // no need for more tests for a Triangle

        Vector n = _plane.getNormal();

        // Subtracting any subsequent points will throw an IllegalArgumentException
        // because of Zero Vector if they are in the same point
        Vector edge1 = vertices[vertices.length - 1].subtract(vertices[vertices.length - 2]);
        Vector edge2 = vertices[0].subtract(vertices[vertices.length - 1]);

        // Cross Product of any subsequent edges will throw an IllegalArgumentException
        // because of Zero Vector if they connect three vertices that lay in the same
        // line.
        // Generate the direction of the polygon according to the angle between last and
        // first edge being less than 180 deg. It is hold by the sign of its dot product
        // with
        // the normal. If all the rest consequent edges will generate the same sign -
        // the
        // polygon is convex ("kamur" in Hebrew).
        boolean positive = edge1.crossProduct(edge2).dotProduct(n) > 0;
        for (int i = 1; i < vertices.length; ++i) {
            // Test that the point is in the same plane as calculated originally
            if (!isZero(vertices[i].subtract(vertices[0]).dotProduct(n)))
                throw new IllegalArgumentException("All vertices of a polygon must lay in the same plane");
            // Test the consequent edges have
            edge1 = edge2;
            edge2 = vertices[i].subtract(vertices[i - 1]);
            if (positive != (edge1.crossProduct(edge2).dotProduct(n) > 0))
                throw new IllegalArgumentException("All vertices must be ordered and the polygon must be convex");
        }
        createVirtualBox();
    }

    //getters
    /**
     * getter for the plane in which the polygon lays
     * @return plane
     */
    public Plane get_plane() {
        return _plane;
    }
    /**
     * getter for the polygon's vertices
     * @return polygon's vertices
     */
    public List<Point3D> get_vertices() {
        return _vertices;
    }

    //basic overrides
    @Override
    public String toString(){
        return "Vertices:"+_vertices.toString();
    }

    @Override
    public Vector getNormal(Point3D point) {
        return _plane.getNormal();
    }

    @Override
    public List<GeoPoint> findIntersections(Ray ray) {
        //find intersection with plane
        List<GeoPoint> intersection = (new Plane(_vertices.get(0), _vertices.get(1), _vertices.get(2))).findIntersections(ray);
        //if ray is parallel to plane, return null
        if (intersection == null)
            return null;

        //create list for V1, v2, ..., Vn
        List<Vector> V = new ArrayList<>();

        //create list for N1, N2, ..., Nn
        List<Vector> N = new ArrayList<>();

        Point3D p0 = ray.getStartPoint();
        List<Point3D> p = _vertices;
        Vector v = ray.getVector();
        int n = _vertices.size();

        //calculate Vi values
        for (int i = 0; i < n; i++){
            V.add(p.get(i).subtract(p0));
        }

        //calculate Ni values
        for (int i = 0; i < n - 1; i++){
            N.add(V.get(i).crossProduct(V.get(i + 1)).normalize());
        }
        N.add(V.get(n - 1).crossProduct(V.get(0)).normalize());

        //check Ni's signs
        boolean sameSign = true;
        double VN0 = alignZero(v.dotProduct(N.get(0)));
        if (VN0 == 0) {
            sameSign = false;
        }
        else {
            boolean positive = VN0 > 0;
            if (positive) {
                for (int i = 1; i < n; i++) {
                    if (alignZero(v.dotProduct(N.get(i))) <= 0){
                        sameSign = false;
                        break;
                    }
                }
            }
            else {
                for (int i = 1; i < n; i++) {
                    if (alignZero(v.dotProduct(N.get(i))) >= 0){
                        sameSign = false;
                        break;
                    }
                }
            }
        }

        if(sameSign){
            for (GeoPoint gp:intersection) {
                gp.geometry = this;
            }
            return intersection;
        }
        return null;
    }

    @Override
    public void createVirtualBox() {
        VirtualBox virtualBox = new VirtualBox();
        List<Double> xVals = new ArrayList<>();
        List<Double> yVals = new ArrayList<>();
        List<Double> zVals = new ArrayList<>();
        for (Point3D vertex: _vertices) {
            xVals.add(vertex.getX().get());
            yVals.add(vertex.getY().get());
            zVals.add(vertex.getZ().get());
        }
        virtualBox.set_lowX(new Coordinate(Collections.min(xVals)));
        virtualBox.set_lowY(new Coordinate(Collections.min(yVals)));
        virtualBox.set_lowZ(new Coordinate(Collections.min(zVals)));
        virtualBox.set_highX(new Coordinate(Collections.max(xVals)));
        virtualBox.set_highY(new Coordinate(Collections.max(yVals)));
        virtualBox.set_highZ(new Coordinate(Collections.max(zVals)));
        _virtualBox = virtualBox;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof Polygon)) return false;
        for(int i = 0; i < _vertices.size(); i++) {
            if (!(_vertices.get(i).equals(((Polygon) obj)._vertices.get(i))))
                return false;
        }
        return true;
    }
}
