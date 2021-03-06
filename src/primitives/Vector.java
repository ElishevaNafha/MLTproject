package primitives;

/**
 * Class Vector is the basic class representing a vector of Euclidean geometry in Cartesian
 * 3-Dimensional coordinate system.
 * @author Eliana Rabinowitz and Elisheva Nafha
 */

public class Vector {

    //fields
    /**
     * Vector's endpoint
     */
    private Point3D _endpoint;

    //constructors
    /**
     * Vector constructor receiving 3 coordinates for the endpoint
     *
     * @throws IllegalArgumentException in case of initialization with the zero vector
     *
     * @param x vector's endpoint's x coordinate
     * @param y vector's endpoint's y coordinate
     * @param z vector's endpoint's z coordinate
     */
    public Vector(Coordinate x, Coordinate y, Coordinate z) throws IllegalArgumentException {
        Point3D point = new Point3D(x, y, z);
        if (point.equals(Point3D.ZERO))
            throw new IllegalArgumentException("Cannot initialize vector to be zero vector");
        _endpoint = point;
    }

    /**
     * Vector constructor receiving 3 values for the coordinates of the endpoint
     *
     * @throws IllegalArgumentException in case of initialization with the zero vector
     *
     * @param x vector's endpoint's x coordinate
     * @param y vector's endpoint's y coordinate
     * @param z vector's endpoint's z coordinate
     */
    public Vector(double x, double y, double z) throws IllegalArgumentException {
        Point3D point = new Point3D(x, y, z);
        if (point.equals(Point3D.ZERO))
            throw new IllegalArgumentException("Cannot initialize vector to be zero vector");
        _endpoint = point;
    }

    /**
     * Vector constructor receiving a vector's endpoint value
     *
     *  @throws IllegalArgumentException in case of initialization with the zero vector
     *
     * @param endpoint vector's endpoint value
     */
    public Vector(Point3D endpoint) throws IllegalArgumentException {
        if (endpoint.equals(Point3D.ZERO))
            throw new IllegalArgumentException("Cannot initialize vector to be zero vector");
        _endpoint = endpoint;
    }

    /**
     * Copy constructor for Vector
     *
     * @param other new Vector
     */
    public Vector(Vector other) {
        _endpoint = other._endpoint;
    }

    //getters
    /**
     * Vector's endpoint value getter
     *
     * @return endpoint value
     */
    public Point3D getEndpoint() {
        return _endpoint;
    }

    //methods

    /**
     * subtracts two vectors
     *
     * @param v second vector
     * @return the vector created from the subtraction of the second vector from this vector
     */
    public Vector subtract(Vector v){
        return _endpoint.subtract(v._endpoint);
    }

    /**
     * sums two vectors
     *
     * @param v second vector
     * @return the vector created from the sum of the two vectors
     */ //what about zero vector?
    public Vector add(Vector v){
        Point3D point = _endpoint.add(v);
        return new Vector(point);
    }

    /**
     * scales the vactor
     *
     * @param a is the scaler
     * @return the scaled vector
     */
    public Vector scale(double a){
        return new Vector(_endpoint.getX().get() * a, _endpoint.getY().get() * a, _endpoint.getZ().get() * a);
    }

    /**
     * calculates dot product between two vectors
     *
     * @param v second vector
     * @return dot product result
     */
    public double dotProduct(Vector v){
        double x = v._endpoint.getX().get() * _endpoint.getX().get();
        double y = v._endpoint.getY().get() * _endpoint.getY().get();
        double z = v._endpoint.getZ().get() * _endpoint.getZ().get();
        return x + y + z;
    }

    /**
     * calculates cross product between two vectors
     *
     * @param v second vector
     * @return cross product result
     */
    public Vector crossProduct(Vector v){
        double x  = (_endpoint.getY().get() * v._endpoint.getZ().get()) - (_endpoint.getZ().get() * v._endpoint.getY().get());
        double y  = (_endpoint.getZ().get() * v._endpoint.getX().get()) - (_endpoint.getX().get() * v._endpoint.getZ().get());
        double z  = (_endpoint.getX().get() * v._endpoint.getY().get()) - (_endpoint.getY().get() * v._endpoint.getX().get());
        return new Vector(x, y, z);
    }

    /**
     * calculates vector's squared length
     *
     * @return vector's squared length
     */
    public double lengthSquared(){
        return dotProduct(this);
    }

    /**
     * calculates vector's length
     *
     * @return vector's length
     */
    public double length(){
        return Math.sqrt(lengthSquared());
    }

    /**
     * normalizes the vector
     *
     * @return this vector after normalization
     */
    public Vector normalize(){
        double length = length();
        double x = _endpoint.getX().get() / length;
        double y = _endpoint.getY().get() / length;
        double z = _endpoint.getZ().get() / length;
        _endpoint = new Point3D(x, y, z);
        return this;
    }

    /**
     * calculates the normalized vector
     *
     * @return normalized vector
     */
    public Vector normalized(){
        Vector v = new Vector(this);
        v.normalize();
        return v;
    }

    //basic overrides
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof Vector)) return false;
        Vector oth = (Vector)obj;
        return _endpoint.equals(oth._endpoint);
    }

    @Override
    public String toString() {
        return _endpoint.toString();
    }
}
