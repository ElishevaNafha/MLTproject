package primitives;

import org.junit.Test;
import primitives.Vector;

import static java.lang.System.out;
import static org.junit.Assert.*;
import static primitives.Util.isZero;

/**
 * Unit tests for primitives.Vector class
 * @author Elisheva Nafha and Eliana Rabinowitz
 */

public class VectorTest {

    /**
     * Test method for {@link Vector#subtract(Vector)}.
     */
    @Test
    public void subtract() {
        Vector v1 = new Vector(0, 3, -2);
        Vector v2 = new Vector(1,3,4);
        Vector v3 = new Vector(0, 3, -2);

        // ============ Equivalence Partitions Tests ==============

        //TC01: Test subtraction between two vectors
        assertEquals("subtract() wrong result", v1.subtract(v2), new Vector(-1,0,-6));

        // =============== Boundary Values Tests ==================

        //TC02: Test subtraction between identical vectors
        try{
            v1.subtract(v3);
            fail("subtract() for identical vectors does not throw an exception");
        } catch (Exception e) {}
    }

    /**
     * Test method for {@link Vector#add(Vector)}.
     */
    @Test
    public void add() {
        Vector v1 = new Vector(0, 3, -2);
        Vector v2 = new Vector(1,3,4);
        Vector v3 = new Vector(-1,-3,-4);

        // ============ Equivalence Partitions Tests ==============

        //TC01: Test addition between two vectors
        assertEquals("add() wrong result", v1.add(v2), new Vector(1,6,2));

        // =============== Boundary Values Tests ==================

        //TC02: Test addition between opposite vectors
        try{
            v2.add(v3);
            fail("add() for opposite vectors does not throw an exception");
        } catch (Exception e) {}

    }

    /**
     * Test method for {@link Vector#scale(double)}.
     */
    @Test
    public void scale() {
        Vector v = new Vector(1,2,3);

        // ============ Equivalence Partitions Tests ==============

        //Test scaling of a vector
        //TC01: positive scalar
        assertEquals("scale() wrong result", v.scale(3), new Vector(3,6,9));
        //TC02: negative scalar
        assertEquals("scale() wrong result", v.scale(-1), new Vector(-1,-2,-3));

        // =============== Boundary Values Tests ==================

        //TC03: Test scaling by zero
        try{
            v.scale(0);
            fail("scale() with scalar 0 does not throw an exception");
        } catch (Exception e) {}
    }

    /**
     * Test method for {@link Vector#dotProduct(Vector)}.
     */
    @Test
    public void dotProduct() {
        Vector v1 = new Vector(1, 2, 3);
        Vector v2 = new Vector(-2, -4, -6);
        Vector v3 = new Vector(0, 3, -2);
        Vector v4 = new Vector(1,3,4);
        Vector v5 = new Vector(2,4,6);

        // ============ Equivalence Partitions Tests ==============

        //TC01: Test dot-product result for orthogonal vectors (expected zero)
        assertTrue("dotProduct() for orthogonal vectors is not zero", isZero(v1.dotProduct(v3)));

        //TC02: Test dot-product result for vectors with blunt angle between them
        assertTrue("dotProduct() for vectors with blunt angle wrong value", isZero(v2.dotProduct(v4) + 38));

        //TC03: Test dot-product result for vectors with sharp angle between them
        assertTrue("dotProduct() for vectors with sharp angle wrong value", isZero(v1.dotProduct(v4) - 19));

        // =============== Boundary Values Tests ==================

        //TC04: Test dot-product result for vectors with same direction
        assertTrue("dotProduct() for vectors with same direction wrong value", isZero(v1.dotProduct(v5) - 28));

        //TC05: Test dot-product result for vectors with opposite direction
        assertTrue("dotProduct() for vectors with opposite direction wrong value", isZero(v1.dotProduct(v2) + 28));
    }

    /**
     * Test method for {@link Vector#crossProduct(Vector)}.
     */
    @Test
    public void crossProduct() {
        Vector v1 = new Vector(1, 2, 3);
        Vector v2 = new Vector(-2, -4, -6);

        // ============ Equivalence Partitions Tests ==============
        Vector v3 = new Vector(0, 3, -2);
        Vector vr = v1.crossProduct(v3);

        // TC01: Test that length of cross-product is proper (orthogonal vectors taken for simplicity)
        assertEquals("crossProduct() wrong result length", v1.length() * v3.length(), vr.length(), 0.00001);

        // TC02:  Test cross-product result orthogonality to its operands
        assertTrue("crossProduct() result is not orthogonal to 1st operand", isZero(vr.dotProduct(v1)));
        assertTrue("crossProduct() result is not orthogonal to 2nd operand", isZero(vr.dotProduct(v3)));

        // =============== Boundary Values Tests ==================
        // TC03: test zero vector from cross-product of co-lined vectors
        try {
            v1.crossProduct(v2);
            fail("crossProduct() for parallel vectors does not throw an exception");
        } catch (Exception e) {}
    }

     /**
     * Test method for {@link Vector#lengthSquared()}.
     */
    @Test
    public void lengthSquared() {
        Vector v = new Vector(1, 2, 3);

        // ============ Equivalence Partitions Tests ==============

        // TC01: simple length calculation
        assertTrue(isZero(v.lengthSquared() - 14));

        // =============== Boundary Values Tests ==================
        // None
    }

    /**
     * Test method for {@link Vector#length()}.
     */
    @Test
    public void length() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Simple vector length calculation
        assertTrue(isZero(new Vector(0, 3, 4).length() - 5));

        // =============== Boundary Values Tests ==================
        // None
    }

    /**
     * Test method for {@link Vector#normalize()}.
     */
    @Test
    public void normalize() {
        Vector v = new Vector(1,2,3);

        // ============ Equivalence Partitions Tests ==============

        // normalize a vector
        v.normalize();
        // TC01: Check that the vector is in length 1
        assertEquals("normalize() not in length 1", v.length(), 1, 0.00001);
        // TC02: Check that the vector's direction didn't change
        Vector w = new Vector(1,2,3);
        assertEquals("normalize() wrong vector's direction", v.scale(w.length()), w);

        // =============== Boundary Values Tests ==================

        // TC03: normalize a vector with length 1
        Vector u = new Vector(v);
        v.normalize();
        assertEquals("normalize() wrong result for vector with length 1", v, u);
    }

    /**
     * Test method for {@link Vector#normalized()}.
     */
    @Test
    public void normalized() {
        Vector v = new Vector(1,2,3);

        // ============ Equivalence Partitions Tests ==============

        //normalize a vector
        // TC01: check that normalized vector is in length 1
        assertEquals("normalized() not in length 1", v.normalized().length(), 1, 0.00001);
        // TC02: Check that normalized vector is at the same direction as the original
        assertEquals("normalized() wrong vector's direction", v.normalized().scale(v.length()), v);

        // =============== Boundary Values Tests ==================

        // TC03: normalize a vector with length 1
        Vector u = new Vector(0,1,0);
        assertEquals("normalized() wrong result for vector with length 1", u.normalized(), u);
    }
}