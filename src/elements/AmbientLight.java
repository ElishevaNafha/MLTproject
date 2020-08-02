package elements;

import primitives.*;

/**
 * AmbientLight class represents the ambient (basic) lighting of a scene
 * @author Eliana Rabinowitz and Elisheva Nafha
 * */
public class AmbientLight extends Light{


    /**
     * AmbientLight constructor
     * @param intensity ambient light's basic intensity
     * @param kA reduction factor
     */
    public AmbientLight(Color intensity, double kA) {
        super(intensity.scale(kA));
    }

}
