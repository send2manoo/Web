/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.utils;

import com.mortennobel.imagescaling.AdvancedResizeOp;
import com.mortennobel.imagescaling.ResampleOp;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author anuja
 * @modified Ramesh
 * 
 */
public class PhotoToIconCreator {

    final static String path = Constant.PROFILEPIC_UPLOAD_PATH;
    final static String imagesPath = Constant.IMAGE_UPLOAD_PATH;
    final static String customFormImagesPath = Constant.CUSTOM_FORM_IMAGE_UPLOAD_PATH;

    /**
     *
     * @param image
     * @param uid
     * @return 
     * @throws java.io.IOException 
     */
    public boolean uploadImage(MultipartFile image, String uid) throws IOException {
        try {
            BufferedImage src = ImageIO.read(image.getInputStream());
            createImageFileOfSpecificSize(src, 29, 29, uid, path);
            createImageFileOfSpecificSize(src, 48, 48, uid, path);
            createImageFileOfSpecificSize(src, 45, 45, uid, path);
            createImageFileOfSpecificSize(src, 140, 140, uid, path);
            makeRoundedCornerImage(src, uid, 1000, path);
            return true;
        } catch (Exception e) {
//            e.printStackTrace();
            return false;
        }
    }

    /**
     * @author Ramesh
     * @return 
     * @throws java.io.IOException
     * @date 18-06-2014
     * @param image
     * @param uid
     */
    public boolean uploadCustomerContactImage(File image, String uid) throws IOException {
        try {
            BufferedImage src = ImageIO.read(image);
            createImageFileOfSpecificSize(src, 48, 48, uid, path);
            return true;
        } catch (Exception e) {
//            e.printStackTrace();
            return false;
        }
    }

    /**
     * @author Ramesh
     * @return 
     * @throws java.io.IOException
     * @date 18-06-2014
     * @param image
     * @param uid
     */
    public boolean uploadCustomerContactImage(MultipartFile image, String uid) throws IOException {
        try {
            BufferedImage src = ImageIO.read(image.getInputStream());
            createImageFileOfSpecificSize(src, 48, 48, uid, path);
            return true;
        } catch (Exception e) {
//            e.printStackTrace();
            return false;
        }
    }

    /**
     * @author Ramesh
     * @return 
     * @throws java.io.IOException 
     * @date 30-07-2014
     * @param image
     * @param uid
     */
    public boolean uploadCutomFormImage(File image, String uid) throws IOException {
        try {
            BufferedImage src = ImageIO.read(image);
            createImageFileOfSpecificSize(src, 240, 240, uid, customFormImagesPath);
            return true;
        } catch (Exception e) {
//            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * @author Ramesh
     * @return 
     * @throws java.io.IOException 
     * @date 30-07-2014
     * @param image
     * @param uid
     */
    public boolean uploadExpenseImage(File image, String uid) throws IOException {
        try {
            BufferedImage src = ImageIO.read(image);
            createImageFileOfSpecificSize(src, 640, 480, uid, imagesPath);
            return true;
        } catch (Exception e) {
//            e.printStackTrace();
            return false;
        }
    }

    /**
     * @author Ramesh
     * @return 
     * @throws java.io.IOException
     * @date 30-07-2014
     * @param image
     * @param uid
     */
    public boolean uploadExpenseImage(MultipartFile image, String uid) throws IOException {
        try {
            BufferedImage src = ImageIO.read(image.getInputStream());
            createImageFileOfSpecificSize(src, 640, 480, uid, imagesPath);
            return true;
        } catch (Exception e) {
//            e.printStackTrace();
            return false;
        }
    }

    /**
     *
     * @param src
     * @param w
     * @param h
     * @param uid
     * @param path
     * @return
     * @throws IOException
     */
    public boolean createImageFileOfSpecificSize(BufferedImage src, int w, int h, String uid, String path) throws IOException {
        ResampleOp resampleOp = new ResampleOp(w, h);
        resampleOp.setUnsharpenMask(AdvancedResizeOp.UnsharpenMask.VerySharp);
        BufferedImage rescaled = resampleOp.doFilter(src, null, w, h);
        ImageIO.write(rescaled, "PNG", new File(path + uid + "_" + w + "X" + h + ".png"));
        src = null;
        resampleOp = null;
        rescaled = null;
        return true;
    }

    /**
     * @author Ramesh
     * @param src
     * @date 04-06-2014
     * @param file
     * @param uid -->  image name to store the image .
     * @param cornerRadius --> how much rounded corner we need 
     * @param path --> path where we will store the image .
     * @throws IOException
     */
    public void makeRoundedCornerImage(BufferedImage src, String uid, int cornerRadius, String path) throws IOException {
        ResampleOp resampleOp = new ResampleOp(30, 30);
        resampleOp.setUnsharpenMask(AdvancedResizeOp.UnsharpenMask.VerySharp);
        BufferedImage image = resampleOp.doFilter(src, null, 30, 30);
        int w = image.getWidth();
        int h = image.getHeight();
        BufferedImage output = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2 = output.createGraphics();

        g2.setComposite(AlphaComposite.Src);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);
        g2.fill(new RoundRectangle2D.Float(0, 0, w, h, cornerRadius, cornerRadius));

        // ... then compositing the image on top,
        // using the white shape from above as alpha source
        g2.setComposite(AlphaComposite.SrcAtop);
        g2.drawImage(image, 0, 0, null);
        g2.dispose();
        ImageIO.write(output, "PNG", new File(path + uid + "_m.png"));
    }
}
