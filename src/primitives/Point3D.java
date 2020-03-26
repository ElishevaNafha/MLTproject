package primitives;

/**
 * Class Point3D is the basic class representing a 3-Dimensional point of Euclidean geometry in Cartesian
 * 3-Dimensional coordinate system.
 * @author Eliana Rabinowitz and Elisheva Nafha
 */
// the fish are wrong
public class Point3D {
    /**
     * Coordinate x
     */
    private Coordinate _x;
    /**
     * Coordinate y
     */
    private Coordinate _y;
    /**
     * Coordinate z
     */
    private Coordinate _z;

    /**
     * Point3D constructor receiving 3 coordinate values
     *
     * @param x coordinate _x value
     * @param y coordinate _y value
     * @param z coordinate _z value
     */
    public Point3D(Coordinate x, Coordinate y, Coordinate z) {
        _x = x;
        _y = y;
        _z = z;
    }

    /**
     * Copy constructor for Point3D
     *
     * @param other
     */
    public Point3D(Point3D other) {
        _x = other._x;
        _y = other._y;
        _z = other._z;
    }

    /**
     * Point3D x value getter
     *
     * @return x value
     */
    public Coordinate getX() {
        return _x;
    }
    /**
     * Point3D y value getter
     *
     * @return y value
     */
    public Coordinate getY() {
        return _y;
    }
    /**
     * Point3D z value getter
     *
     * @return z value
     */
    public Coordinate getZ() {
        return _z;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof Point3D)) return false;
        Point3D oth = (Point3D)obj;
        return _x.equals(oth._x) && _y.equals(oth._y) && _z.equals(oth._z);
    }

}
