package jmask;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class JMask {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String maskSource = in.nextLine();
        String outputFolder = in.nextLine();
        int cases = Integer.valueOf(in.nextLine());
        double saturation = Double.valueOf(in.nextLine());

        try {
            BufferedImage mask;
            mask = ImageIO.read(new File(maskSource));
            int[] bgColor = {255, 255, 255};
            for (int i = 0; i < cases; i++) {
                //int[] fgColor = ImagePainter.pickColor((double)i / (double)cases, saturation);
                int[] fgColor = ImagePainter.hueToRGB(((double)i / (double)cases)*360, saturation);
                //System.out.println((double)i / (double)cases);
                BufferedImage newImg = ImagePainter.maskImage(mask, bgColor, fgColor);
                ImageIO.write(newImg, "png", new File(outputFolder + "img" + (i + 1) + ".png"));
            }

            //System.out.println(mask.getType());
            //System.out.println(Integer.toBinaryString(mask.getRGB(0, 0)));
            /*int rgb = mask.getRGB(0, 0);
            int alpha = (rgb >> 24) & 0xFF;
            int red =   (rgb >> 16) & 0xFF;
            red = red << 16;
            red =   (red >> 16) & 0xFF;
            int green = (rgb >>  8) & 0xFF;
            int blue =  (rgb      ) & 0xFF;
            
            System.out.println(alpha+":"+red+":"+green+":"+blue);
            System.out.println(0xFF);
            System.out.println(Integer.toBinaryString(red << 16));*/
        } catch (IOException ex) {
            Logger.getLogger(JMask.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(-1);
        }
    }

}
