import elements.Camera;
import geometries.*;
import org.junit.Test;
import primitives.*;
import elements.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Testing integration of different functions
 * @author Eliana Rabinowitz and Elisheva Nafha
 */
public class integrationTests {
    /**
     * Test method for
     * integration of findIntersections and constructRayThroughPixel for sphere.
     */
    @Test
    public void findIntersectionsConstructRayThroughPixelSphere() {
        Camera camera = new Camera(new Point3D(0,0,0), new Vector(0, 0, 1), new Vector(0, -1, 0));
        Sphere sphere;
        Ray ray;
        int result, expected;

        //TC01:
        sphere = new Sphere(1, new Point3D(0,0,3));
        expected = 2;
        result = 0;
        for(int i =0;i<3;i++)
        {
            for(int j=0;j<3;j++)
            {
                ray = camera.constructRayThroughPixel(3,3,i,j,1,3,3);
                if(sphere.findIntersections(ray)!=null)
                    result += (sphere.findIntersections(ray)).size();
            }
        }
        assertEquals("TC01", expected, result);
        //TC02:
        sphere = new Sphere(2.5, new Point3D(0,0,2.5));
        camera = new Camera(new Point3D(0,0,-0.5),new Vector(0, 0, 1), new Vector(0, -1, 0));
        expected = 18;
        result = 0;
        for(int i =0;i<3;i++)
        {
            for(int j=0;j<3;j++)
            {
                ray = camera.constructRayThroughPixel(3,3,i,j,1,3,3);
                if(sphere.findIntersections(ray)!=null)
                    result += (sphere.findIntersections(ray)).size();
            }
        }
        assertEquals("TC02", expected, result);

        //TC03:
        sphere = new Sphere(2, new Point3D(0,0,2));
        camera = new Camera(new Point3D(0,0,-0.5),new Vector(0, 0, 1), new Vector(0, -1, 0));
        expected = 10;
        result = 0;
        for(int i =0;i<3;i++)
        {
            for(int j=0;j<3;j++)
            {
                ray = camera.constructRayThroughPixel(3,3,i,j,1,3,3);
                if(sphere.findIntersections(ray)!=null)
                    result += (sphere.findIntersections(ray)).size();
            }
        }
        assertEquals("TC03", expected, result);

        //TC04:
        sphere = new Sphere(4, new Point3D(0,0,2));
        camera = new Camera(new Point3D(0,0,-0.5),new Vector(0, 0, 1), new Vector(0, -1, 0));
        expected = 9;
        result = 0;
        for(int i =0;i<3;i++)
        {
            for(int j=0;j<3;j++)
            {
                ray = camera.constructRayThroughPixel(3,3,i,j,1,3,3);
                if(sphere.findIntersections(ray)!=null)
                    result += (sphere.findIntersections(ray)).size();
            }
        }
        assertEquals("TC04", expected, result);

        //TC05:
        sphere = new Sphere(0.5, new Point3D(0,0,-1));
        camera = new Camera(new Point3D(0,0,-0.5),new Vector(0, 0, 1), new Vector(0, -1, 0));
        expected = 0;
        result = 0;
        for(int i =0;i<3;i++)
        {
            for(int j=0;j<3;j++)
            {
                ray = camera.constructRayThroughPixel(3,3,i,j,1,3,3);
                if(sphere.findIntersections(ray)!=null)
                    result += (sphere.findIntersections(ray)).size();
            }
        }
        assertEquals("TC05", expected, result);
    }


    /**
     * Test method for
     * integration of findIntersections and constructRayThroughPixel for plane.
     */
    @Test
    public void findIntersectionsConstructRayThroughPixelPlane() {
        Camera camera = new Camera(new Point3D(0,0,0), new Vector(0, 0, 1), new Vector(0, -1, 0));
        Plane plane;
        Ray ray;
        int result, expected;
        //TC01:
        plane = new Plane(new Point3D(0,0,3),new Vector(0, 0, 1));
        expected = 9;
        result = 0;
        for(int i =0;i<3;i++)
        {
            for(int j=0;j<3;j++)
            {
                ray = camera.constructRayThroughPixel(3,3,i,j,1,3,3);
                if(plane.findIntersections(ray)!=null)
                    result += (plane.findIntersections(ray)).size();
            }
        }
        assertEquals("TC01", expected, result);

        //TC02:
        plane = new Plane(new Point3D(0,0,3), new Vector(1.5,1,1));
        expected = 6;
        result = 0;
        for(int i =0;i<3;i++)
        {
            for(int j=0;j<3;j++)
            {
                ray = camera.constructRayThroughPixel(3,3,i,j,1,3,3);
                if(plane.findIntersections(ray)!=null)
                    result += (plane.findIntersections(ray)).size();
            }
        }
        assertEquals("TC02", expected, result);

        //TC03:
        plane = new Plane(new Point3D(0,0,3), new Vector(1,1,1));
        expected = 6;
        result = 0;
        for(int i =0;i<3;i++)
        {
            for(int j=0;j<3;j++)
            {
                ray = camera.constructRayThroughPixel(3,3,i,j,1,3,3);
                if(plane.findIntersections(ray)!=null)
                    result += (plane.findIntersections(ray)).size();
            }
        }
        assertEquals("TC03", expected, result);
    }

    /**
     * Test method for
     * integration of findIntersections and constructRayThroughPixel for triangle.
     */
    @Test
    public void findIntersectionsConstructRayThroughPixelTriangle() {
        Camera camera = new Camera(new Point3D(0,0,0), new Vector(0, 0, 1), new Vector(0, -1, 0));
        Triangle triangle;
        Ray ray;
        int result, expected;
        //TC01:
        triangle = new Triangle(new Point3D(0,-1,2), new Point3D(1,1,2), new Point3D(-1,1,2));
        expected = 1;
        result = 0;
        for(int i =0;i<3;i++)
        {
            for(int j=0;j<3;j++)
            {
                ray = camera.constructRayThroughPixel(3,3,i,j,1,3,3);
                if(triangle.findIntersections(ray)!=null)
                    result += (triangle.findIntersections(ray)).size();
            }
        }
        assertEquals("TC01", expected, result);

        //TC02:
        triangle = new Triangle(new Point3D(0,-20,2), new Point3D(1,1,2), new Point3D(-1,1,2));
        expected = 2;
        result = 0;
        for(int i =0;i<3;i++)
        {
            for(int j=0;j<3;j++)
            {
                ray = camera.constructRayThroughPixel(3,3,i,j,1,3,3);
                if(triangle.findIntersections(ray)!=null)
                    result += (triangle.findIntersections(ray)).size();
            }
        }
        assertEquals("TC02", expected, result);

    }


}
