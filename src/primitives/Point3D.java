package primitives;

/**
 * Class Point3D is the basic class representing a 3-Dimensional point of Euclidean geometry in Cartesian
 * 3-Dimensional coordinate system.
 * @author Eliana Rabinowitz and Elisheva Nafha
 */

public class Point3D {

    //fields
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
     * Zero point, static and constant
     */
    static final public Point3D ZERO = new Point3D(0,0,0);

    //constructors
    /**
     * Point3D constructor receiving 3 coordinate values
     *
     * @param x coordinate _x value
     * @param y coordinate _y value
     * @param z coordinate _z value
     */
    public Point3D(Coordinate x, Coordinate y, Coordinate z) {
        _x = new Coordinate(x);
        _y = new Coordinate(y);
        _z = new Coordinate(z);
    }

    /**
     * Point3D constructor receiving 3 values for coordinates
     *
     * @param x coordinate _x value
     * @param y coordinate _y value
     * @param z coordinate _z value
     */
    public Point3D(double x, double y, double z) {
        _x = new Coordinate(x);
        _y = new Coordinate(y);
        _z = new Coordinate(z);
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

    //getters
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

    //basic overrides
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof Point3D)) return false;
        Point3D oth = (Point3D)obj;
        return _x.equals(oth._x) && _y.equals(oth._y) && _z.equals(oth._z);
    }

    @Override
    public String toString() {
        return "(" + _x.toString() + "," + _y.toString() + "," + _z.toString() + ")";
    }

    //methods
    /**
     * subtracts two point as two vectors, creating a vector between them
     *
     * @param p second point
     * @return vector from this point to p
     */ //what about zero vector?
    public Vector subtract(Point3D p){
        //create Point3D from subtraction
        Point3D endpoint = new Point3D(_x.get() - p._x.get(), _y.get() - p._y.get(), _z.get() - p._z.get());
        return new Vector(endpoint);
    }

    /**
     * sums up a point and a vector, creating a new point
     *
     * @param v vector
     * @return sum of this point and v
     */
    public Point3D add(Vector v){
        //create Point3D from addition
        return new Point3D(_x.get() + v.getEndpoint().getX().get(), _y.get() + v.getEndpoint().getY().get(), _z.get() + v.getEndpoint().getZ().get());
    }

    /**
     * calculates squared distance between two points
     *
     * @param p second point
     * @return squared distance between this point and p
     */
    public double distanceSquared(Point3D p){
        return ((_x.get() - p._x.get()) * (_x.get() - p._x.get())) +
                ((_y.get() - p._y.get()) * (_y.get() - p._y.get())) +
                ((_z.get() - p._z.get()) * (_z.get() - p._z.get()));
    }

    /**
     * calculates distance between two points
     *
     * @param p second point
     * @return distance between this point and p
     */
    public double distance(Point3D p){
        return Math.sqrt(distanceSquared(p));
    }
}
