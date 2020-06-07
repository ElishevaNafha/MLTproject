package renderer;

import elements.Camera;
import elements.LightSource;
import geometries.*;
import renderer.ImageWriter;
import primitives.*;
import scene.Scene;
import geometries.Intersectable.GeoPoint;

import java.util.ArrayList;
import java.util.List;

import static primitives.Util.alignZero;

/**
 * Render class is responsible of rendering a scene into an image.
 * @author Eliana Rabinowitz and Elisheva Nafha
 * */
public class Render {
    //constants
    private static final double DELTA = 0.1;
    private static final int MAX_CALC_COLOR_LEVEL = 10;
    private static final double MIN_CALC_COLOR_K = 0.001;

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
                if ((i == 250) && (j == 250)){
                    System.out.print("hello");
                }
                Ray ray = camera.constructRayThroughPixel(nX, nY, j, i, distance, width, height);

                // find closest intersection
                GeoPoint closestPoint = getClosestPoint(ray);

                // if no intersections found after view screen, write as background
                if (closestPoint == null)
                    _imageWriter.writePixel(j, i, background);
                else
                    _imageWriter.writePixel(j, i, calcColor(closestPoint, ray).getColor());
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
     * calculates all intersections of a ray with the scene's geometries and returns the closest point
     * to the ray's start point
     *
     * @param ray ray to find intersections with
     * @return closest intersection to ray's start point
     */
    private GeoPoint findClosestIntersection(Ray ray){
        List<GeoPoint> intersectionPoints = _scene.getGeometries().findIntersections(ray);
        GeoPoint closestPoint = null;
        double distance = Double.MAX_VALUE;
        double tempDistance;

        if (intersectionPoints == null)
            return null;

        for (GeoPoint gp : intersectionPoints) {
            tempDistance = gp.point.distance(ray.getStartPoint());
            if ((tempDistance < distance) && (tempDistance != 0)){
                closestPoint = gp;
                distance = tempDistance;
            }
        }

        return closestPoint;
    }

    /**
     * get the closest intersection point on a ray from the camera to the view plane.
     * returns the closest point behind the view plane; returns null if no point is found.
     * @param ray the ray to find intersections with
     * @return closest intersection point on the ray
     */
    public GeoPoint getClosestPoint(Ray ray) { // make private after testing

        GeoPoint closestPoint = findClosestIntersection(ray);

        //if there are no points, return null
        if (closestPoint == null)
            return null;

        // rename calculation parameters
        Camera camera = _scene.getCamera();
        Point3D p0 = camera.getLocation();
        double d = _scene.getDistance();

        // calculate distance to the intersection with the view plane to avoid intersections before view plane
        Plane viewPlane = new Plane(new Point3D(p0.add(camera.getVto().scale(d))),
                camera.getVto());
        GeoPoint screenIntersection = viewPlane.findIntersections(ray).get(0);
        double screenDistance = p0.distance(screenIntersection.point);

        // make sure intersection is not before view plane
        while ((closestPoint != null) && (ray.getStartPoint().distance(closestPoint.point) < screenDistance))
            closestPoint = findClosestIntersection(new Ray(closestPoint.point, ray.getVector()));

        return closestPoint;
    }

    /**
     * recursive auxiliary function that calculates the color of a point considering light sources, emission,
     * shadowing, reflection and refraction.
     * @param geopoint point to calculate color for
     * @param inRay view point ray
     * @param level recursion level
     * @param k reduction factor
     * @return color of point
     */
    private Color calcColor(GeoPoint geopoint, Ray inRay, int level, double k) {
        Color color = geopoint.geometry.getEmission();
        Vector v = geopoint.point.subtract(_scene.getCamera().getLocation()).normalize();
        Vector n = geopoint.geometry.getNormal(geopoint.point);
        Material material =geopoint.geometry.getMaterial();
        int nShininess = material.getNShininess();
        double kd = material.getKD();
        double ks = material.getKS();
        for (LightSource lightSource : _scene.getLights()) {
            Vector l = lightSource.getL(geopoint.point);
            if (sign(n.dotProduct(l)) ==  sign(n.dotProduct(v))) {
                if(unshaded(lightSource,l,n,geopoint)) {
                    Color lightIntensity = lightSource.getIntensity(geopoint.point);
                    color = color.add(calcDiffusive(kd, l, n, lightIntensity),
                            calcSpecular(ks, l, n, v, nShininess, lightIntensity));
                }
            }
        }

        if (level == 1) return Color.BLACK;

        double kr = geopoint.geometry.getMaterial().getKR(), kkr = k * kr;
        if (kkr > MIN_CALC_COLOR_K) {
            Ray reflectedRay = getReflectedRay(n, geopoint.point, inRay);
            GeoPoint reflectedPoint = findClosestIntersection(reflectedRay);
            if (reflectedPoint != null)
                color = color.add(calcColor(reflectedPoint, reflectedRay,
                        level - 1, kkr).scale(kr));
        }

        double kt = geopoint.geometry.getMaterial().getKT(), kkt = k * kt;
        if (kkt > MIN_CALC_COLOR_K) {
            Ray refractedRay = getRefractedRay(n, geopoint.point, inRay);
            GeoPoint refractedPoint = findClosestIntersection(refractedRay);
            if (refractedPoint != null)
                color = color.add(calcColor(refractedPoint, refractedRay, level - 1, kkt).scale(kt));
        }

        return color;
    }

    /**
     * calculates the color of a point considering light sources, emission,
     * shadowing, reflection and refraction.
     * @param geopoint point to calculate color for
     * @param inRay view point ray
     * @return color of point
     */
    private Color calcColor(GeoPoint geopoint, Ray inRay) {
        return calcColor(geopoint, inRay, MAX_CALC_COLOR_LEVEL, 1.0).add(_scene.getAmbientLight().getIntensity());
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

    /**
     * checks whether point is unshaded
     * @param l vector from light source to point
     * @param n normal to geometry from point
     * @param geopoint the point
     * @return true if unshaded, otherwise false
     */
    private boolean unshaded(LightSource light, Vector l, Vector n, GeoPoint geopoint){
        Vector lightDirection = l.scale(-1); // from point to light source
        Vector delta = n.scale(n.dotProduct(lightDirection) > 0 ? DELTA : -DELTA);
        Point3D point = geopoint.point.add(delta);
        Ray lightRay = new Ray(point, lightDirection);
        List<GeoPoint> intersections = _scene.getGeometries().findIntersections(lightRay);
        if (intersections==null)
            return true;
        double lightDistance = light.getDistance(geopoint.point);

        for (GeoPoint gp : intersections) {
            if (lightDistance>gp.point.distance(geopoint.point)){
                if (gp.geometry.getMaterial().getKT() == 0)
                    return false;
            }
        }
        return true;
    }

    /**
     * @param n normal to geometry form point
     * @param point
     * @param ray ray from camera to point
     * @return reflected ray
     */
    private Ray getReflectedRay(Vector n, Point3D point, Ray ray){
        Vector r = ray.getVector().subtract(n.scale(2*(ray.getVector().dotProduct(n)))).normalize();
        Vector delta = n.scale(r.dotProduct(n) > 0 ? DELTA : - DELTA);
        return new Ray(point.add(delta),r);
    }

    /**
     * @param point
     * @param ray ray from camera to point
     * @return refracted ray
     */
    private Ray getRefractedRay(Vector n, Point3D point, Ray ray){
        Vector delta = n.scale(ray.getVector().dotProduct(n) > 0 ? DELTA : - DELTA);
        return new Ray(point.add(delta), ray.getVector());
    }
}
