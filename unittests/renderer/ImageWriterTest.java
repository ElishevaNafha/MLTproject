package renderer;

import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.*;

/**
 * test imageWriter
 * @author Eliana Rabinowitz and Elisheva Nafha
 */
public class ImageWriterTest {

    /**
     * tests write pixel
     */
    @Test
    public void writePixel() {
        ImageWriter imageWriter = new ImageWriter("image writer test", 1600, 1000, 800, 500);
        Color background = new Color(133,200,165);
        Color grid = new Color(255,110,100);
        for (int i = 0; i < 500; i++){
            for (int j = 0; j < 800; j++){
                if ((i % 50 == 0) || (j % 50 == 0))
                    imageWriter.writePixel(j, i, grid);
                else
                    imageWriter.writePixel(j, i, background);
            }
        }
        imageWriter.writeToImage();
    }
}