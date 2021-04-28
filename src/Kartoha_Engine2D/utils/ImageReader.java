package Kartoha_Engine2D.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class ImageReader {
    public static BufferedImage read(String path) throws IOException {
        return ImageIO.read(Objects.requireNonNull(ImageReader.class.getClassLoader().getResourceAsStream(path)));
    }
}
