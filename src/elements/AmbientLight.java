package elements;

import primitives.*;

/**
 * AmbientLight class represents the ambient (basic) lighting of a scene
 * @author Eliana Rabinowitz and Elisheva Nafha
 * */
public class AmbientLight {

    //fields
    private Color _intensity;

    /**
     * AmbientLight constructor
     * @param intensity ambient light's basic intensity
     * @param kA reduction factor
     */
    public AmbientLight(Color intensity, double kA) {
        _intensity = new Color(intensity.scale(kA));
    }

    /**
     * getter for ambient light's intensity
     * @return intensity of ambient light
     */
    public Color getIntensity() {
        return _intensity;
    }
}
