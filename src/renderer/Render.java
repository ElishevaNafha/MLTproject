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
import java.util.Random;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * Render class is responsible of rendering a scene into an image.
 * @author Eliana Rabinowitz and Elisheva Nafha
 * */
public class Render {
    //constants
    private static final int MAX_CALC_COLOR_LEVEL = 10;
    private static final double MIN_CALC_COLOR_K = 0.001;
    private static final int MAX_GENERATED_RAYS_FACTOR = 10;
    private static final double SAMPLE_RAYS_CIRCLE_RADIUS = 10;

    //multithreading stuff
    private int _threads = 1;
    private final int SPARE_THREADS = 0;
    private boolean _print = false;
    //fields
    /**
     * the render's image writer
     */
    private ImageWriter _imageWriter;
    /**
     * scene to render
     */
    private Scene _scene;
    /**
     * amount of sample rays to use for image improvements
     */
    private int _numSampleRays;

    /**
     * Render constructor
     *
     * @param imageWriter render's image writer
     * @param scene scene to render
     */
    public Render(ImageWriter imageWriter, Scene scene) {
        this(imageWriter, scene, 1);
    }

    /**
     * Render constructor
     *
     * @param imageWriter render's image writer
     * @param scene scene to render
     * @param numSampleRays number of sample rays to generate for beams of rays
     */
    public Render(ImageWriter imageWriter, Scene scene, int numSampleRays) {
        _imageWriter = imageWriter;
        _scene = scene;
        _numSampleRays = numSampleRays;
    }


    /**
     * renders the image
     */
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

        final Pixel thePixel = new Pixel(nY, nX);
     /*   for (int i = 0; i < nY; i++){
            for (int j = 0; j < nX; j++){
                Ray ray = camera.constructRayThroughPixel(nX, nY, j, i, distance, width, height);

                // find closest intersection
                GeoPoint closestPoint = getClosestPoint(ray);

                // if no intersections found after view screen, write as background
                if (closestPoint == null)
                    _imageWriter.writePixel(j, i, background);
                else
                    _imageWriter.writePixel(j, i, calcColor(closestPoint, ray).getColor());
            }
        }*/
        // Generate threads
        Thread[] threads = new Thread[_threads];
        for (int i = _threads - 1; i >= 0; --i) {
            threads[i] = new Thread(() -> {
                Pixel pixel = new Pixel();
                while (thePixel.nextPixel(pixel)) {
                    Ray ray = camera.constructRayThroughPixel(nX, nY, pixel.col, pixel.row, distance, width, height);
                    // find closest intersection
                    GeoPoint closestPoint = getClosestPoint(ray);
                    // if no intersections found after view screen, write as background
                    if (closestPoint == null)
                        _imageWriter.writePixel(pixel.col, pixel.row, background);
                    else
                        _imageWriter.writePixel(pixel.col, pixel.row, calcColor(closestPoint, ray).getColor());
                }
            });
        }

        // Start threads
        for (Thread thread : threads) thread.start();

        // Wait for all threads to finish
        for (Thread thread : threads) try { thread.join(); } catch (Exception e) {}
        if (_print) System.out.printf("\r100%%\n");
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
                double ktr = transparency(lightSource, l, n, geopoint);
                if (ktr * k > MIN_CALC_COLOR_K) {
                    Color lightIntensity = lightSource.getIntensity(geopoint.point).scale(ktr);
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
                color = color.add(calcSampleRays(reflectedRay, SAMPLE_RAYS_CIRCLE_RADIUS, geopoint.geometry.getMaterial().getKGS(),
                        level - 1, kkr).scale(kr));
        }

        double kt = geopoint.geometry.getMaterial().getKT(), kkt = k * kt;
        if (kkt > MIN_CALC_COLOR_K) {
            Ray refractedRay = getRefractedRay(n, geopoint.point, inRay);
            GeoPoint refractedPoint = findClosestIntersection(refractedRay);
            if (refractedPoint != null) {
                if(geopoint.geometry.getMaterial().getKGS()==0){

                }
                color = color.add(calcSampleRays(refractedRay, SAMPLE_RAYS_CIRCLE_RADIUS, geopoint.geometry.getMaterial().getKDG(),
                        level - 1, kkt).scale(kt));
            }
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
     * calculates reflection or refraction based on a beam of rays
     *
     * @param base base reflection / refraction ray
     * @param radius radius of sample rays' virtual circle
     * @param distance distance between point and sample rays' virtual circle
     * @param recursionLevel recursion level
     * @param k reduction factor (kkt / kkr)
     * @return average effect of transparency / reflectivity using a beam of rays
     */
    private Color calcSampleRays(Ray base, double radius, double distance,
                                 int recursionLevel,double k){

        // get all sample rays
        List<Ray> sampleRays = getSampleRays(base,radius,distance);
        Color total = new Color(0,0,0);

        // sum effect of all rays
        GeoPoint closestIntersection;
        Color tempColor = new Color(0, 0, 0);
        for (Ray ray:sampleRays) {
            closestIntersection = findClosestIntersection(ray);
            if (closestIntersection != null)
                tempColor = calcColor(closestIntersection, ray, recursionLevel, k);
            total = total.add(tempColor);
        }

        // reduce effect to average effect and return it
        // if one ray
        if (distance == 0)
            return total;
        // if NUM_SAMPLE_RAYS rays
        return total.reduce(_numSampleRays+1);
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
        if (l.dotProduct(n) == 0)
            return Color.BLACK;
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
        double dp = l.dotProduct(n);
        if ((dp == 0) || (isZero(dp * n.getEndpoint().getX().get()) &&
                isZero(dp * n.getEndpoint().getY().get()) && isZero(dp * n.getEndpoint().getZ().get())))
            return Color.BLACK;
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
     * checks how much of the light reaching the point from a light source is blocked by other bodies
     * @param light current light source
     * @param l vector from light source to point
     * @param n normal to geometry from point
     * @param geopoint the point
     * @return amount of light reaching the point from the light source
     */
    private double transparency(LightSource light, Vector l, Vector n, GeoPoint geopoint) {
        Vector lightDirection = l.scale(-1); // from point to light source
        Ray lightRay = new Ray(geopoint.point, lightDirection, n);
        List<GeoPoint> intersections = _scene.getGeometries().findIntersections(lightRay);
        if (intersections==null)
            return 1.0;
        double lightDistance = light.getDistance(geopoint.point);
        double ktr = 1.0;
        for (GeoPoint gp : intersections) {
            if (alignZero(gp.point.distance(geopoint.point) - lightDistance) <= 0) {
                ktr *= gp.geometry.getMaterial().getKT();
                if (ktr < MIN_CALC_COLOR_K)
                    return 0.0;
            }
        }
        return ktr;
    }

    /**
     * calculates ray that is reflected from a point on a geometry
     * @param n normal to geometry form point
     * @param point
     * @param ray ray from camera to point
     * @return reflected ray
     */
    private Ray getReflectedRay(Vector n, Point3D point, Ray ray){
        Vector r = ray.getVector().subtract(n.scale(2*(ray.getVector().dotProduct(n)))).normalize();
        return new Ray(point, r, n);
    }

    /**
     * calculates ray that is refracted from a point on a geometry
     * @param point
     * @param ray ray from camera to point
     * @return refracted ray
     */
    private Ray getRefractedRay(Vector n, Point3D point, Ray ray){
        return new Ray(point, ray.getVector(), n);
    }

    /**
     * generates a beam of rays surrounding a base ray. the rays are generated randomly.
     * @param base base ray
     * @param radius radius of the circle the rays are generated through
     * @param distance distance of the circle from the ray's start
     * @return a list of rays
     */
    private List<Ray> getSampleRays(Ray base,  double radius, double distance){

        List<Ray> sampleRays = new ArrayList<>();

        // if not wanted sample rays
        if(distance==0){
            sampleRays.add(base);
            return sampleRays;
        }

        // --- name parameters ---
        Point3D p0 = base.getStartPoint();
        Vector v = base.getVector();
        Point3D p = base.getStartPoint().add(base.getVector().scale(distance));

        // --- create relative "Vup" and "Vright" vectors on the plane that the circle is on ---

        //generate a non parallel vector to base vector
        Vector helpVector = v.add(new Vector(1,0,0));

        Vector relativeVup = v.crossProduct(helpVector).normalize();
        Vector relativeVright = v.crossProduct(relativeVup);

        // --- create random rays ---
        // the rays are generated randomly within a square surrounding the circle. only rays that are generated in the
        // circle are inserted to the sample rays list. if not found numRays rays within a large amount of attempts, we
        // stop the loop and return those that we found

        int numRaysCurrent = 1; // total number of iterations (including base ray)
        int numSampleRaysCurrent = 1; // number of rays in the list (including base ray)
        double x, y;
        Ray tempRay;
        Point3D tempPoint;
        Vector tempVector;

        // generate rays until numRays is reached or max number of trials reached
        while ((numSampleRaysCurrent < _numSampleRays) && (numRaysCurrent < MAX_GENERATED_RAYS_FACTOR * _numSampleRays)){
            // generate random coordinates on the square surrounding the circle (from 0 to 2 * radius)
            x = Math.random() * radius * 2;
            y = Math.random() * radius * 2;
            // convert the relative coordinates to real coordinates (similarly to constructRayThroughPixel from camera)
            tempPoint = p.add(relativeVright.scale(x - radius)).add(relativeVup.scale(y - radius));
            // check that the point is in the circle
            if (tempPoint.distanceSquared(p) < radius * radius) {
                tempVector = tempPoint.subtract(p0);
                tempRay = new Ray(p0, tempVector);
                sampleRays.add(tempRay);
                numSampleRaysCurrent++;
            }
            numRaysCurrent++;
        }

        sampleRays.add(base);
        return sampleRays;
    }



    /**
     * Pixel is an internal helper class whose objects are associated with a Render object that
     * they are generated in scope of. It is used for multithreading in the Renderer and for follow up
     * its progress.<br/>
     * There is a main follow up object and several secondary objects - one in each thread.
     * @author Dan
     *
     */
    private class Pixel {
        private long _maxRows = 0;
        private long _maxCols = 0;
        private long _pixels = 0;
        public volatile int row = 0;
        public volatile int col = -1;
        private long _counter = 0;
        private int _percents = 0;
        private long _nextCounter = 0;

        /**
         * The constructor for initializing the main follow up Pixel object
         * @param maxRows the amount of pixel rows
         * @param maxCols the amount of pixel columns
         */
        public Pixel(int maxRows, int maxCols) {
            _maxRows = maxRows;
            _maxCols = maxCols;
            _pixels = maxRows * maxCols;
            _nextCounter = _pixels / 100;
            if (Render.this._print) System.out.printf("\r %02d%%", _percents);
        }

        /**
         *  Default constructor for secondary Pixel objects
         */
        public Pixel() {}

        /**
         * Internal function for thread-safe manipulating of main follow up Pixel object - this function is
         * critical section for all the threads, and main Pixel object data is the shared data of this critical
         * section.<br/>
         * The function provides next pixel number each call.
         * @param target target secondary Pixel object to copy the row/column of the next pixel
         * @return the progress percentage for follow up: if it is 0 - nothing to print, if it is -1 - the task is
         * finished, any other value - the progress percentage (only when it changes)
         */
        private synchronized int nextP(Pixel target) {
            ++col;
            ++_counter;
            if (col < _maxCols) {
                target.row = this.row;
                target.col = this.col;
                if (_counter == _nextCounter) {
                    ++_percents;
                    _nextCounter = _pixels * (_percents + 1) / 100;
                    return _percents;
                }
                return 0;
            }
            ++row;
            if (row < _maxRows) {
                col = 0;
                if (_counter == _nextCounter) {
                    ++_percents;
                    _nextCounter = _pixels * (_percents + 1) / 100;
                    return _percents;
                }
                return 0;
            }
            return -1;
        }

        /**
         * Public function for getting next pixel number into secondary Pixel object.
         * The function prints also progress percentage in the console window.
         * @param target target secondary Pixel object to copy the row/column of the next pixel
         * @return true if the work still in progress, -1 if it's done
         */
        public boolean nextPixel(Pixel target) {
            int percents = nextP(target);
            if (percents > 0)
                if (Render.this._print) System.out.printf("\r %02d%%", percents);
            if (percents >= 0)
                return true;
            if (Render.this._print) System.out.printf("\r %02d%%", 100);
            return false;
        }
    }

    /**
     * Set multithreading <br>
     * - if the parameter is 0 - number of coress less 2 is taken
     *
     * @param threads number of threads
     * @return the Render object itself
     */
    public Render setMultithreading(int threads) {
        if (threads < 0)
            throw new IllegalArgumentException("Multithreading patameter must be 0 or higher");
        if (threads != 0)
            _threads = threads;
        else {
            int cores = Runtime.getRuntime().availableProcessors() - SPARE_THREADS;
            if (cores <= 2)
                _threads = 1;
            else
                _threads = cores;
        }
        return this;
    }


}
