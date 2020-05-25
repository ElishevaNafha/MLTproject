package renderer;

import elements.Camera;
import elements.LightSource;
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
     * @param intersection point to calculate color for
     * @return calculated color
     */
    private Color calcColor(GeoPoint intersection) {
        Color color = _scene.getAmbientLight().getIntensity();
        color = color.add(intersection.geometry.getEmission());
        Vector v = intersection.point.subtract(_scene.getCamera().getLocation()).normalize();
        Vector n = intersection.geometry.getNormal(intersection.point);
        Material material =intersection.geometry.getMaterial();
        int nShininess = material.getNShininess();
        double kd = material.getKD();
        double ks = material.getKS();
        for (LightSource lightSource : _scene.getLights()) {
            Vector l = lightSource.getL(intersection.point);
            if (sign(n.dotProduct(l)) ==  sign(n.dotProduct(v))) {
                Color lightIntensity = lightSource.getIntensity(intersection.point);
                color = color.add(calcDiffusive(kd, l, n, lightIntensity),
                        calcSpecular(ks, l, n, v, nShininess, lightIntensity));
            }
        }
        return color;
    }

    /**
     * returns sign of a number
     * @param d number
     * @return 1 or 0 or -1, according to sign
     */
    private int sign(double d){
        if (d > 0)
            return 1;
        if (d < 0)
            return -1;
        return 0;
    }

    /**
     * calculates the effect of diffusion of light in a point on a geometry
     * @param kd diffusion factor
     * @param l vector from light source to point
     * @param n normal to geometry from point
     * @param lightIntensity intensity of light from the light source at the point
     * @return diffusive light of light source on point
     */
    private Color calcDiffusive(double kd, Vector l, Vector n, Color lightIntensity){
        return lightIntensity.scale(kd * Math.abs(l.dotProduct(n)));
    }

    /**
     * calculates the specular light from a light source in a point on a geometry
     * @param ks specular factor
     * @param l vector from light source to point
     * @param n normal to geometry from point
     * @param v vector from camera to point
     * @param nShininess the shininess factor
     * @param lightIntensity intensity of light from the light source at the point
     * @return specular light of light source on point
     */
    private Color calcSpecular(double ks, Vector l, Vector n, Vector v, int nShininess, Color lightIntensity){
        Vector r = l.subtract(n.scale(2 * l.dotProduct(n))).normalize();
        return lightIntensity.scale(ks * Math.pow(Math.max(0, -v.dotProduct(r)), nShininess));
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
