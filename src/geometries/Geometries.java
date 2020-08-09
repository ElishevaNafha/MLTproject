package geometries;

import primitives.Coordinate;
import primitives.Point3D;
import primitives.Ray;

import javax.naming.OperationNotSupportedException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static primitives.Util.isZero;

/**
 * Geometries class represents a collection of geometries in 3D Cartesian coordinate system
 * @author Eliana Rabinowitz and Elisheva Nafha
 */
public class Geometries extends Intersectable {

    //fields
    /**
     * a list of the geometries included in the geometries object
     */
    private List<Intersectable> _geometries;

    //constructors
    /**
     * Geometries default constructor
     */
    public Geometries() {
        this._geometries = new ArrayList<>();
    }

    /**
     * Geometries constructor
     * @param geometries a collection of geometries
     */
    public Geometries(Intersectable... geometries) {
        this._geometries = new ArrayList<>();
        Collections.addAll(_geometries, geometries);
        createVirtualBox();
    }

    /**
     * Geometries constructor
     * @param geometries a list of geometries
     */
    public Geometries(List<Intersectable> geometries) {
        this._geometries = new ArrayList<>(geometries);
        createVirtualBox();
    }

    //functions

    /**
     * add geometries to the geometries collection
     * @param geometries new geometries
     */
    public void add(Intersectable... geometries) {
        Collections.addAll(_geometries, geometries);
        createVirtualBox();
    }

    /**
     * add geometries to the geometries collection
     * @param geometries list of new geometries
     */
    public void add(List<Intersectable> geometries) {
        _geometries.addAll(geometries);
        createVirtualBox();
    }

    /**
     * removes a geometry from the geometries collection
     * @param geometry geometry to remove
     */
    public void remove(Intersectable geometry) {
        if (_geometries.contains(geometry)) {
            _geometries.remove(geometry);
            createVirtualBox();
        }
    }

    //bounding values hierarchy functions

    /**
     * returns only the geometries that might intersect with the ray
     * @param ray ray to find geometries for
     * @return geometries that might intersect the ray
     */
    public Geometries getIntersectionBoxes(Ray ray) {
        // get a list of intersected boxes
        Geometries intersectionBoxes = new Geometries(getIntersectionBoxesList(ray));
        // add all unboxed geometries (infinite geometries)
        intersectionBoxes.add( _virtualBox.get_infiniteGeometries());
        return intersectionBoxes;
    }

    /**
     * returns only the geometries that might intersect with the ray. sub function of getIntersectionBoxes
     * @param ray ray to find geometries for
     * @return a list of geometries that might intersect the ray
     */
    private List<Intersectable> getIntersectionBoxesList(Ray ray) {

        List<Intersectable> intersectionBoxes = new ArrayList<>();
        // if ray intersects the virtual box, check intersections with inner boxes
        if (getVirtualBox().hasIntersection(ray)) {

            // if there is only one geometry in the box (elementary box), return its box
            if (_geometries.size() == 1) {
                intersectionBoxes = _geometries;
            }

            // else, find all elementary boxes in the hierarchy
            else {
                intersectionBoxes.addAll(((Geometries) _geometries.get(0)).getIntersectionBoxesList(ray));
                intersectionBoxes.addAll(((Geometries) _geometries.get(1)).getIntersectionBoxesList(ray));
            }
        }
        return intersectionBoxes;
    }

    /**
     * create a virtual boxes hierarchy
     */
    public void buildVirtualBoxesHierarchy() {
        flatten();
        recursiveBuildVirtualBoxesHierarchy();
    }

    /**
     * create a virtual boxes hierarchy recursively. sub function of buildVirtualBoxesHierarchy
     */
    private void recursiveBuildVirtualBoxesHierarchy() {
        // if virtual box is elementary
        if (_geometries.size() <= 1)
            return;
        splitVirtualBox();
        // build hierarchy for low box
        ((Geometries)_geometries.get(0)).recursiveBuildVirtualBoxesHierarchy();
        // build hierarchy for high box
        ((Geometries)_geometries.get(1)).recursiveBuildVirtualBoxesHierarchy();
    }

    /**
     * splits the virtual box into two inner virtual boxes by splitting the geometries list.
     * the direction of the split (axis x, y, or z) is chosen to be the one that splits the geometries most equally.
     */
    public void splitVirtualBox() {
        double midX = (getVirtualBox().get_highX().get() + getVirtualBox().get_lowX().get()) / 2;
        double midY = (getVirtualBox().get_highY().get() + getVirtualBox().get_lowY().get()) / 2;
        double midZ = (getVirtualBox().get_highZ().get() + getVirtualBox().get_lowZ().get()) / 2;

        List<Geometries> geometriesX = split('x', midX);
        List<Geometries> geometriesY = split('y', midY);
        List<Geometries> geometriesZ = split('z', midZ);
        _geometries = new ArrayList<>();

        int differenceX = Math.abs(((Geometries)geometriesX.get(0))._geometries.size() - ((Geometries)geometriesX.get(1))._geometries.size());
        int differenceY = Math.abs(((Geometries)geometriesY.get(0))._geometries.size() - ((Geometries)geometriesY.get(1))._geometries.size());
        int differenceZ = Math.abs(((Geometries)geometriesZ.get(0))._geometries.size() - ((Geometries)geometriesZ.get(1))._geometries.size());

        double min = Math.min(differenceX, differenceY);
        min = Math.min(differenceZ, min);
        if (min == differenceX)
            Collections.addAll(_geometries, geometriesX.get(0), geometriesX.get(1));
        else if (min == differenceY)
            Collections.addAll(_geometries, geometriesY.get(0), geometriesY.get(1));
        else if (min == differenceZ)
            Collections.addAll(_geometries, geometriesZ.get(0), geometriesZ.get(1));
    }

    /**
     * splits the virtual box into two inner virtual boxes by splitting the geometries list.
     * @param axis the direction of the split
     * @param mid the middle of the virtual box according to the chosen axis
     * @return a list that holds the 'lower' box as first element and the 'higher' box as the second element
     */
    private List<Geometries> split(char axis, double mid){
        Geometries left = new Geometries();
        Geometries right = new Geometries();
        double high, low;

        for (Intersectable geometry:_geometries) {
            // if is a finite geometry
            if (geometry.getVirtualBox() != null) {
                // find geometry's box's boundaries
                if (axis == 'x') {
                    high = geometry.getVirtualBox().get_highX().get();
                    low = geometry.getVirtualBox().get_lowX().get();
                } else if (axis == 'y') {
                    high = geometry.getVirtualBox().get_highY().get();
                    low = geometry.getVirtualBox().get_lowY().get();
                } else {
                    high = geometry.getVirtualBox().get_highZ().get();
                    low = geometry.getVirtualBox().get_lowZ().get();
                }

                // place geometry
                if (placeGeometry(high, low, mid) == -1)
                    left.add(geometry);
                else
                    right.add(geometry);
            }
        }

        // in case the split created an empty box, separate one geometry to another box.
        // the geometry chosen is the one that fills the whole box in the chosen axis direction.
        Intersectable toMove;
        if (left._geometries.isEmpty()) {
            if (axis == 'x')
                toMove = right._geometries.stream().min(Comparator.comparingDouble(x -> x._virtualBox.get_lowX().get())).get();
            else if (axis == 'y')
                toMove = right._geometries.stream().min(Comparator.comparingDouble(x -> x._virtualBox.get_lowY().get())).get();
            else
                toMove = right._geometries.stream().min(Comparator.comparingDouble(x -> x._virtualBox.get_lowZ().get())).get();
            left.add(toMove);
            right.remove(toMove);
        }
        else if (right._geometries.isEmpty()) {
            if (axis == 'x')
                toMove = left._geometries.stream().max(Comparator.comparingDouble(x -> x._virtualBox.get_highX().get())).get();
            else if (axis == 'y')
                toMove = left._geometries.stream().max(Comparator.comparingDouble(x -> x._virtualBox.get_highY().get())).get();
            else
                toMove = left._geometries.stream().max(Comparator.comparingDouble(x -> x._virtualBox.get_highZ().get())).get();
            right.add(toMove);
            left.remove(toMove);
        }

        List<Geometries> geometries = new ArrayList<>();
        Collections.addAll(geometries, left, right);
        return geometries;
    }

    /**
     * places a geometry in the 'low' or the 'high' virtual box.
     * @param high high edge of the geometry's virtual box
     * @param low low edge of the geometry's virtual box
     * @param mid middle of original virtual box
     * @return 1 if geometry is at the 'high' virtual box, -1 if geometry is at the 'low' virtual box
     */
    private int placeGeometry(double high, double low, double mid){
        if (high < mid)
            return -1;
        if (low > mid)
            return 1;
        if (high-mid > mid-low)
            return 1;
        return -1;

    }

    /**
     * flattens geometries to a list of single geometries
     */
    private void flatten(){
        _geometries = flatten(_geometries);
    }

    /**
     * recursive flatten function that flattens a list of intersectables to a list of single geometries. sub function of flatten
     * @param geometries list to flatten
     * @return flatten list
     */
    private List<Intersectable> flatten(List<Intersectable> geometries){
        List<Intersectable> newGeometries = new ArrayList<>();
        for (Intersectable geometry:geometries) {
            if(geometry instanceof Geometries){
                newGeometries.addAll(flatten(((Geometries) geometry)._geometries));
            }
            else
                newGeometries.add(geometry);
        }
        return newGeometries;
    }

    // overrides

    @Override
    public List<GeoPoint> findIntersections(Ray ray) {
        List<GeoPoint> intersections = new ArrayList<>();
        List<GeoPoint> result;
        for(Intersectable geometry : this._geometries)
        {
            result = geometry.findIntersections(ray);
            if(result!=null)
                intersections.addAll(result);
        }
        if(intersections.isEmpty())
            return null;
        return intersections;
    }

    @Override
    protected void createVirtualBox() {
        VirtualBox virtualBox = new VirtualBox(new Coordinate(Double.MAX_VALUE),new Coordinate(-Double.MAX_VALUE),
                new Coordinate(Double.MAX_VALUE),new Coordinate(-Double.MAX_VALUE),
                new Coordinate(Double.MAX_VALUE),new Coordinate(-Double.MAX_VALUE));
        VirtualBox temp;
        boolean hasFinite = false;
        for (Intersectable geometry: _geometries) {
            // if geometry is finite
            if(geometry.getVirtualBox()!=null){
                // then box contains at least one geometry
                hasFinite = true;
                // change the box to fit the new geometry
                temp = ((Intersectable) geometry).getVirtualBox();
                virtualBox.addInfiniteGeometries(temp.get_infiniteGeometries());
                if(temp.get_lowX().get()<virtualBox.get_lowX().get()){
                    virtualBox.set_lowX(temp.get_lowX());
                }
                if(temp.get_lowY().get()<virtualBox.get_lowY().get()){
                    virtualBox.set_lowY(temp.get_lowY());
                }
                if(temp.get_lowZ().get()<virtualBox.get_lowZ().get()){
                    virtualBox.set_lowZ(temp.get_lowZ());
                }
                if(temp.get_highX().get()>virtualBox.get_highX().get()){
                    virtualBox.set_highX(temp.get_highX());
                }
                if(temp.get_highY().get()>virtualBox.get_highY().get()){
                    virtualBox.set_highY(temp.get_highY());
                }
                if(temp.get_highZ().get()>virtualBox.get_highZ().get()){
                    virtualBox.set_highZ(temp.get_highZ());
                }
            }
            // else, add geometry to the infinite geometries list
            else
                virtualBox.addInfiniteGeometry((Geometry)geometry);
        }
        _virtualBox = hasFinite ? virtualBox : null;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof Geometries)) return false;
        for (int i  = 0; i < _geometries.size(); i++) {
            if (!_geometries.get(i).equals(((Geometries)obj)._geometries.get(i)))
                return false;
        }
        return true;
    }

}
