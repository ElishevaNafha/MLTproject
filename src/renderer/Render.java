package renderer;

import elements.Camera;
import geometries.*;
import renderer.ImageWriter;
import primitives.*;
import scene.Scene;
import geometries.Intersectable.GeoPoint;

import java.util.List;

/**
 * Render class is responsible of rendering a scene into an image.
 * @author Eliana Rabinowitz and Elisheva Nafha
 * */
public class Render {

    //fields
    private ImageWriter _imageWriter;
    private Scene _scene;

    /**
     * Render constructor
     *
     * @param imageWriter render's image writer
     * @param scene scene to render
     */
    public Render(ImageWriter imageWriter, Scene scene) {
        _imageWriter = imageWriter;
        _scene = scene;
    }

    public void renderImage() {

        // calculation parameters
        Camera camera = _scene.getCamera();
        Intersectable geometries = _scene.getGeometries();
        java.awt.Color background = _scene.getBackground().getColor();
        int nX = _imageWriter.getNx();
        int nY = _imageWriter.getNy();
        double height = _imageWriter.getHeight();
        double width = _imageWriter.getWidth();
        double distance = _scene.getDistance();

        for (int i = 0; i < nY; i++){
            for (int j = 0; j < nX; j++){
                Ray ray = camera.constructRayThroughPixel(nX, nY, j, i, distance, width, height);

                // find all ray's intersections
                List<GeoPoint> intersectionPoints = geometries.findIntersections(ray);

                // find closest intersection
                GeoPoint closestPoint = getClosestPoint(intersectionPoints);

                // if no intersections found after view screen, write as background
                if (closestPoint == null)
                    _imageWriter.writePixel(j, i, background);
                else
                    _imageWriter.writePixel(j, i, calcColor(closestPoint).getColor());
            }
        }
    }

    /**
     * gather all images data to write the image
     */
    public void writeToImage(){
        _imageWriter.writeToImage();
    }

    /**
     * get the closest intersection point on the ray behind the view plane; returns null if no point is found
     * @param intersectionPoints all intersection points on the ray
     * @return closest intersection point on the ray
     */
    public GeoPoint getClosestPoint(List<GeoPoint> intersectionPoints) { // make private after testing

        //if there are no points, return null
        if (intersectionPoints == null)
            return null;

        // rename calculation parameters
        Camera camera = _scene.getCamera();
        Point3D p0 = camera.getLocation();
        double d = _scene.getDistance();

        // calculate distance to the intersection with the view plane to avoid intersections before view plane
        Ray ray = new Ray(p0, new Vector(intersectionPoints.get(0).point.subtract(p0)));
        Plane viewPlane = new Plane(new Point3D(p0.add(camera.getVto().scale(d))),
                                    camera.getVto());
        GeoPoint screenIntersection = viewPlane.findIntersections(ray).get(0);
        double screenDistance = p0.distance(screenIntersection.point);

        // find closest point
        // if there's an intersection on the view plane, return it
        for (GeoPoint p:intersectionPoints) {
            if(p.point.equals(screenIntersection.point))
                return p;
        }
      //if (intersectionPoints.contains(screenIntersection))
       //     return screenIntersection;
        // initialize to screen's intersection
        GeoPoint closestPoint = screenIntersection;
        double dis = screenDistance;
        // calculate
        double tempDis;
        for (GeoPoint point: intersectionPoints) {
            tempDis = p0.distance(point.point);
            // if the intersection is after the view screen
            if (tempDis > screenDistance){
                // if the current point is the first point found after the view screen
                if (dis == screenDistance) {
                    closestPoint = point;
                    dis = tempDis;
                }
                // if not the first point found after the view screen
                else{
                    if (tempDis < dis){
                        closestPoint = point;
                        dis = tempDis;
                    }
                }
            }
        }

        // if no point found after the screen, return null
        if (dis == screenDistance)
            return null;

        return closestPoint;
    }

    /**
     * calculates the color of a point considering emission and lighting
     *
     * @param point point to calculate color for
     * @return calculated color
     */
    public Color calcColor(GeoPoint point) { // make private after testing
        return _scene.getAmbientLight().getIntensity().add(point.geometry.getEmission());
    }

    /**
     * prints a grid over the image
     * @param interval grid's interval
     * @param color grid's color
     */
    public void printGrid(int interval, java.awt.Color color){ // make private after testing
        int nX = _imageWriter.getNx();
        int nY = _imageWriter.getNy();

        for (int i = 0; i < nY; i++){
            for (int j = 0; j < nX; j++){
                if ((i % interval == 0) || (j % interval == 0))
                    _imageWriter.writePixel(j, i, color);
            }
        }
    }
}
