package primitives;

/**
 * Class Ray is the basic class representing a vector with a start point of Euclidean geometry in Cartesian
 * 3-Dimensional coordinate system.
 * @author Eliana Rabinowitz and Elisheva Nafha
 */
public class Ray {

    private static final double DELTA = 0.1;

    //fields
    /**
     * Ray's start point
     */
    private Point3D _startpoint;

    /**
     * Ray's vector
     */
    private Vector _vector;

    //constructors
    /**
     * Ray constructor receiving a start point and a direction vector
     *
     * @param point ray's start point
     * @param vector ray's direction vector
     */
    public Ray(Point3D point, Vector vector) {
        if (vector.length() != 1)
            vector.normalize();
        _startpoint = point;
        _vector = vector;
    }

    public Ray(Point3D head, Vector direction, Vector normal){
        Vector delta = normal.scale(direction.dotProduct(normal) > 0 ? DELTA : - DELTA);
        _startpoint = head.add(delta);
        _vector = direction;
    }

    /**
     * Copy constructor for Ray
     *
     * @param other
     */
    public Ray(Ray other) {
        _startpoint = other._startpoint;
        _vector = other._vector;
    }

    //getters
    /**
     * Ray's start point value getter
     *
     * @return start point's value
     */
    public Point3D getStartPoint() {
        return _startpoint;
    }

    /**
     * Ray's direction vector value getter
     *
     * @return direction vector's value
     */
    public Vector getVector() {
        return _vector;
    }

    //basic overrides
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof Ray)) return false;
        Ray oth = (Ray)obj;
        return _startpoint.equals(oth._startpoint) && _vector.equals(oth._vector);
    }

    @Override
    public String toString() {
        return "starts at " + _startpoint.toString() + ", in direction " + _vector.toString();
    }

    //methods
    public Point3D getPoint(double t){
        return _startpoint.add(_vector.scale(t));
    }
}
