package geometries;

import primitives.Coordinate;
import primitives.Point3D;
import primitives.Ray;

import java.util.ArrayList;
import java.util.List;

import static primitives.Util.isZero;

/**
 * class VirtualBox represents the edges of the finite geometry
 * @author Eliana Rabinowitz and Elisheva Nafha
 */
public class VirtualBox {
    private Coordinate _lowX, _highX, _lowY, _highY, _lowZ, _highZ;
    private List<Intersectable> _infiniteGeometries;

    //constructors

    /**
     * default constructor for VirtualBox
     * All Values are initialized as 0
     */
    public VirtualBox(){
        _infiniteGeometries = new ArrayList<>();
    }

    /**
     * Constructor for VirtualBox
     * @param _lowX
     * @param _highX
     * @param _lowY
     * @param _highY
     * @param _lowZ
     * @param _highZ
     */
    public VirtualBox(Coordinate _lowX, Coordinate _highX, Coordinate _lowY, Coordinate _highY, Coordinate _lowZ, Coordinate _highZ) {
        this();
        this._lowX = _lowX;
        this._highX = _highX;
        this._lowY = _lowY;
        this._highY = _highY;
        this._lowZ = _lowZ;
        this._highZ = _highZ;
    }


    /**
     * getter for lowX
     * @return lowX
     */
    public Coordinate get_lowX() {
        return _lowX;
    }

    /**
     * getter for highX
     * @return highX
     */
    public Coordinate get_highX() {
        return _highX;
    }

    /**
     * getter for lowY
     * @return lowY
     */
    public Coordinate get_lowY() {
        return _lowY;
    }

    /**
     * getter for highY
     * @return highY
     */
    public Coordinate get_highY() {
        return _highY;
    }

    /**
     * getter for lowZ
     * @return lowZ
     */
    public Coordinate get_lowZ() {
        return _lowZ;
    }

    /**
     * getter for highZ
     * @return highZ
     */
    public Coordinate get_highZ() {
        return _highZ;
    }

    /**
     * setter for lowX
     * @param _lowX
     */
    public void set_lowX(Coordinate _lowX) {
        this._lowX = _lowX;
    }
    /**
     * setter for highX
     * @param _highX
     */
    public void set_highX(Coordinate _highX) {
        this._highX = _highX;
    }
    /**
     * setter for lowY
     * @param _lowY
     */
    public void set_lowY(Coordinate _lowY) {
        this._lowY = _lowY;
    }
    /**
     * setter for highY
     * @param _highY
     */
    public void set_highY(Coordinate _highY) {
        this._highY = _highY;
    }
    /**
     * setter for lowZ
     * @param _lowZ
     */
    public void set_lowZ(Coordinate _lowZ) {
        this._lowZ = _lowZ;
    }
    /**
     * setter for highZ
     * @param _highZ
     */
    public void set_highZ(Coordinate _highZ) {
        this._highZ = _highZ;
    }

    /**
     * checks whether a ray intersects the virtual box
     * @param ray the ray o check intersection with
     * @return whether there is an intersection
     */
    public boolean hasIntersection(Ray ray){
        double Ox = ray.getStartPoint().getX().get();
        double Oy = ray.getStartPoint().getY().get();
        double Oz = ray.getStartPoint().getZ().get();
        double Dx = ray.getVector().getEndpoint().getX().get();
        double Dy = ray.getVector().getEndpoint().getY().get();
        double Dz = ray.getVector().getEndpoint().getZ().get();

        double temp;

        double tmin = (_lowX.get() - Ox) / Dx;
        double tmax = (_highX.get() - Ox) / Dx;
        if (tmin > tmax){
            temp = tmin;
            tmin = tmax;
            tmax = temp;
        }

        double tymin = (_lowY.get() - Oy) / Dy;
        double tymax = (_highY.get() - Oy) / Dy;
        if (tymin > tymax){
            temp = tymin;
            tymin = tymax;
            tymax = temp;
        }

        if((tmin > tymax) || (tymin > tmax))
            return false;

        if (tymin > tmin)
            tmin = tymin;

        if (tmax < tymax)
            tmax = tymax;

        double tzmin = (_lowZ.get() - Oz) / Dz;
        double tzmax = (_highZ.get() - Oz) / Dz;
        if (tzmin > tzmax){
            temp = tzmin;
            tzmin = tzmax;
            tzmax = temp;
        }

      if ((tmin > tzmax) || (tzmin > tmax))
            return false;

        if (tzmin > tmin)
            tmin = tzmin;

        if (tzmax < tmax)
            tmax = tzmax;

        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof VirtualBox)) return false;
        return (isZero(_lowX.get()-((VirtualBox)obj)._lowX.get()) &&
                isZero(_lowY.get()-((VirtualBox)obj)._lowY.get()) &&
                isZero(_lowZ.get()-((VirtualBox)obj)._lowZ.get()) &&
                isZero(_highX.get()-((VirtualBox)obj)._highX.get()) &&
                isZero(_highY.get()-((VirtualBox)obj)._highY.get()) &&
                isZero(_highZ.get()-((VirtualBox)obj)._highZ.get()));
    }

    /**
     * getter for infiniteGeometries
     * @return infiniteGeometries
     */
    public List<Intersectable> get_infiniteGeometries() {
        return _infiniteGeometries;
    }

    /**
     * adds an infinite geometry to the infinite geometries list
     * @param geometry infinite geometry to add
     */
    public void addInfiniteGeometry(Geometry geometry){
        _infiniteGeometries.add(geometry);
    }

    /**
     * appends a list of infinite geometries to the infinite geometries list
     * @param geometries infinite geometries list to add
     */
    public void addInfiniteGeometries(List<Intersectable> geometries){
        _infiniteGeometries.addAll(geometries);
    }
}
