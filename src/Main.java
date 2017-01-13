import pl.steganography.Decryption;
import pl.steganography.Encryption;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

/**
 * Created by Adam on 2016-12-01.
 */
public class Main {
    public static void main(String [] args) throws IOException {

        Encryption.encrypt("image.bmp", "text");
        Decryption.decrypt("encrypted_image.bmp");
    }





}

