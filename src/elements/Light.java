package elements;

import primitives.Color;

/**
 * Light class represents light - all types of light inherit from this class
 * @author Eliana Rabinowitz and Elisheva Nafha
 */
public abstract class Light {
    //fields
    protected Color _intensity;

    //constructors

    /**
     * Constructor for Light class
     * @param _intensity the intensity of the light
     */
    public Light(Color _intensity) {
        this._intensity = _intensity;
    }

    /**
     * getter for intensity
     * @return intensity
     */
    public Color getIntensity(){
        return _intensity;
    }
}
