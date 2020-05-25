package scene;

import primitives.*;
import elements.*;
import geometries.*;

import java.util.List;

/**
 * Scene class represents a scene made of a collection of geometries in 3D Cartesian coordinate system,
 * lighting, a camera and a view plane.
 * @author Eliana Rabinowitz and Elisheva Nafha
 * */
public class Scene {

    //fields
    private String _name;
    private Color _background;
    private AmbientLight _ambientLight;
    private Geometries _geometries;
    private Camera _camera;
    private double _distance;
    private List<LightSource> _lights;

    /**
     * Scene constructor
     * @param name scene's name
     */
    public Scene(String name) {
        _name = name;
        _geometries = new Geometries();
    }

    //getters
    /**
     * getter for scene's name
     * @return scene's name
     */
    public String getName() {
        return _name;
    }

    /**
     * getter for scene's background color
     * @return scene's background color
     */
    public Color getBackground() {
        return _background;
    }

    /**
     * getter for scene's ambient light
     * @return scene's ambient light
     */
    public AmbientLight getAmbientLight() {
        return _ambientLight;
    }

    /**
     * getter for scene's geometries collection
     * @return scene's geometries collection
     */
    public Geometries getGeometries() {
        return _geometries;
    }

    /**
     * getter for scene's camera
     * @return scene's camera
     */
    public Camera getCamera() {
        return _camera;
    }

    /**
     * getter for scene's distance between the camera and the view plane
     * @return scene's distance between the camera and the view plane
     */
    public double getDistance() {
        return _distance;
    }

    /**
     * getter for scene's list of light sources
     * @return list of light sources
     */
    public List<LightSource> getLights() {
        return _lights;
    }

    //setters
    /**
     * setter for scene's background color
     * @param background scene's background color
     */
    public void setBackground(Color background) {
        _background = background;
    }

    /**
     * setter for scene's ambient light
     * @param ambientLight scene's ambient light
     */
    public void setAmbientLight(AmbientLight ambientLight) {
        _ambientLight = ambientLight;
    }

    /**
     * setter for scene's camera
     * @param camera scene's camera
     */
    public void setCamera(Camera camera) {
        _camera = camera;
    }

    /**
     * setter for scene's distance between the camera and the view plane
     * @param distance scene's distance between the camera and the view plane
     */
    public void setDistance(double distance) {
        _distance = distance;
    }

    //functions
    /**
     * add a geometries to the scene's geometries
     * @param geometries new geometries
     */
    public void addGeometries(Intersectable... geometries){
        _geometries.add(geometries);
    }

    /**
     * adds lights to scene's list of light sources
     * @param lights lights to add
     */
    public void addLights(LightSource... lights) {
        for (LightSource light:lights) {
            _lights.add(light);
        }
    }
}
