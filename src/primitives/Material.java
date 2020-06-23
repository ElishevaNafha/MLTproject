package primitives;

public class Material {
    //fields
    double _kD; //diffusion factor
    double _kS; //specular factor
    double _kT; //transparency factor
    double _kR; //reflective factor
    double _kGS; //glossiness factor
    double _kDG; //Diffusive glass factor
    int _nShininess;

    //constructors
    /**
     * constructor for material
     * @param _kD
     * @param _kS
     * @param _nShininess
     */
    public Material(double _kD, double _kS, int _nShininess) {
        this(_kD,_kS,_nShininess,0,0);
    }

    /**
     * constructor for material
     * @param _kD diffusion
     * @param _kS specular
     * @param _nShininess shininess
     * @param _kT transparency
     * @param _kR reflectivity
     */
    public Material(double _kD, double _kS, int _nShininess, double _kT, double _kR) {
        this(_kD, _kS, _nShininess, _kT, _kR, 0, 0);
    }

    /**
     * constructor for material
     * @param _kD diffusion
     * @param _kS specular
     * @param _nShininess shininess
     * @param _kT transparency
     * @param _kR reflectivity
     * @param _kGS glossy surface effect
     * @param _kDG diffuse glass effect
     */
    public Material(double _kD, double _kS, int _nShininess, double _kT, double _kR, double _kGS, double _kDG) {
        this._kR= _kR;
        this._kT= _kT;
        this._kD = _kD;
        this._kS = _kS;
        this._nShininess = _nShininess;
        this._kGS = _kGS;
        this._kDG = _kDG;
    }

    //getters

    /**
     * getter for diffusion factor
     * @return kD
     */
    public double getKD() {
        return _kD;
    }

    /**
     * getter for specular factor
     * @return kS
     */
    public double getKS() {
        return _kS;
    }

    /**
     * getter for transparency factor
     * @return kT
     */
    public double getKT() {
        return _kT;
    }

    /**
     * getter for reflective factor
     * @return kR
     */
    public double getKR() {
        return _kR;
    }

    /**
     * getter for shininess factor
     * @return nShininess
     */
    public int getNShininess() {
        return _nShininess;
    }

    /**
     * getter for glossiness factor
     * @return kGS
     */
    public double getKGS() {
        return _kGS;
    }

    /**
     * getter for diffuse glass factor
     * @return kDG
     */
    public double getKDG() {
        return _kDG;
    }
}
