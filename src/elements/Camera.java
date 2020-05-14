package elements;

import primitives.*;

import static primitives.Util.alignZero;

/**
 * Camera class represents a view point on 3D Cartesian coordinate system
 * @author Eliana Rabinowitz and Elisheva Nafha
 */
public class Camera {

    //fields
    private Point3D _location;
    private Vector _Vto;
    private Vector _Vup;
    private Vector _Vright;

    /**
     * Camera constructor based on location point and two axis vectors.
     * The vectors must be orthogonal.
     *
     * @param location camera's location on the 3D Cartesian coordinate system
     * @param Vto vector for Z axis
     * @param Vup vector for Y axis
     * @throws IllegalArgumentException in case that Vto and Vup are not orthogonal
     */
    public Camera(Point3D location, Vector Vto, Vector Vup) {
        _location = location;
        if (Vto.dotProduct(Vup) != 0)
            throw new IllegalArgumentException("Vto and Vup have to be orthogonal");
        _Vto = Vto.normalized();
        _Vup = Vup.normalized();
        _Vright = Vto.crossProduct(Vup).normalize();
    }

    /**
     * getter for the camera's location
     * @return camera's location
     */
    public Point3D getLocation() {
        return _location;
    }

    /**
     * getter for the Vto vector - Z axis
     * @return Vto vector
     */
    public Vector getVto() {
        return _Vto;
    }

    /**
     * getter for the Vup vector - Y axis
     * @return Vup vector
     */
    public Vector getVup() {
        return _Vup;
    }

    /**
     * getter for the Vright vector - X axis
     * @return Vright vector
     */
    public Vector getVright() {
        return _Vright;
    }

    /**
     * This method constructs a ray from the camera (location) through a pixel (i,j) in the scene's view plane.
     *
     * @param nX number of pixels on view plane's width
     * @param nY number of pixels on view plane's height
     * @param j y coordinate for requested pixel on view plane
     * @param i x coordinate for requested pixel on view plane
     * @param screenDistance distance from camera (location) to view plane
     * @param screenWidth width of view plane
     * @param screenHeight height of view plane
     * @return ray from the camera through the requested pixel
     */
    public Ray constructRayThroughPixel (int nX, int nY,
                                         int j, int i, double screenDistance,
                                         double screenWidth, double screenHeight){
        //create view plane
        Point3D P0 = _location;
        Point3D Pc = P0.add(_Vto.scale(screenDistance));
        double Ry = screenHeight / nY;
        double Rx = screenWidth / nX;

        //create pixel
        double Yi = alignZero(((i - nY / 2.0) * Ry) + (Ry / 2));
        double Xj = alignZero(((j - nX / 2.0) * Rx) + (Rx / 2));
        Point3D Pij = new Point3D(Pc);
        if (Xj != 0)
            Pij = Pij.add(_Vright.scale(Xj));
        if (Yi != 0)
            Pij = Pij.add(_Vup.scale(-Yi));

        //create ray
        Vector Vij = Pij.subtract(P0);
         return new Ray(P0, Vij);
    }
}
