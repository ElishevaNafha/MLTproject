package geometries;
import primitives.*;

import java.util.List;

/**
 * Triangle class represents two-dimensional triangle in 3D Cartesian coordinate system
 *
 * @author Eliana Rabinowitz and ELisheva Nafha
 */
public class Triangle extends Polygon{

    //constructors
    /**
     * Triangle constructor that receives the triangle's three vertices
     *
     * @param vertex1 a vertex of the triangle
     * @param vertex2 a vertex of the triangle
     * @param vertex3 a vertex of the triangle
     */
    public Triangle (Point3D vertex1, Point3D vertex2, Point3D vertex3){
        super(vertex1, vertex2, vertex3);
    }
    /**
     * Triangle constructor that receives the triangle's three vertices and color
     * @param emission emission of the triangle
     * @param vertex1 a vertex of the triangle
     * @param vertex2 a vertex of the triangle
     * @param vertex3 a vertex of the triangle
     */
    public Triangle (Color emission, Point3D vertex1, Point3D vertex2, Point3D vertex3){
        super(emission,vertex1, vertex2, vertex3);
    }
}
