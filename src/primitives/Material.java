package primitives;

public class Material {
    //fields
    double _kD;
    double _kS;
    int _nShininess;

    //constructors
    /**
     * constructor for material
     * @param _kD
     * @param _kS
     * @param _nShininess
     */
    public Material(double _kD, double _kS, int _nShininess) {
        this._kD = _kD;
        this._kS = _kS;
        this._nShininess = _nShininess;
    }

    //getters
    public double getKD() {
        return _kD;
    }

    public double getKS() {
        return _kS;
    }

    public int getNShininess() {
        return _nShininess;
    }
}
