package jmask;

import java.awt.image.BufferedImage;

public class ImagePainter {

    /*
    function rgb()
	local h = os.time()/86400+10000
	local s = h%1*3
	local r = .5*math.pi*(s%1)
	local R,G,B = 0,0,0
	if s<1 then
		R,G,B = 1,1-math.cos(r),1-math.sin(r)
	elseif s<2 then
		R,G,B = 1-math.sin(r),1,1-math.cos(r)
	else
		R,G,B = 1-math.cos(r),1-math.sin(r),1
	end
	R=math.floor(R*255+0.5)
	G=math.floor(G*255+0.5)
	B=math.floor(B*255+0.5)
	return R,G,B
    end
     */
    public static int[] hueToRGB(double hue, double saturation) {
        hue = hue%360;
        int mid = (int) ((hue % 60) * 4);
        int state = (int) Math.floor(hue / 60);
        if (state % 2 == 1) {
            mid = 255 - mid;
        }

        int r = 0;
        int g = 0;
        int b = 0;

        switch (state) {
            case 0://0
                r = 255;
                g = mid;
                b = 0;
                break;
            case 1://60
                r = mid;
                g = 255;
                b = 0;
                break;
            case 2://120
                r = 0;
                g = 255;
                b = mid;
                break;
            case 3://180
                r = 0;
                g = mid;
                b = 255;
                break;
            case 4://240
                r = mid;
                g = 0;
                b = 255;
                break;
            default://300
                r = 255;
                g = 0;
                b = mid;
                break;
        }

        int ri = (int) (r * saturation);
        int bi = (int) (b * saturation);
        int gi = (int) (g * saturation);

        System.out.println(ri + ":" + bi + ":" + gi);

        int[] c = {ri, bi, gi};

        return c;
    }

    @Deprecated
    public static int[] pickColor(double t, double saturation) {
        double h = t;
        double s = h % 1 * 3;
        double radius = .5 * Math.PI * (s % 1);

        double r;
        double b;
        double g;

        if (s < 1) {
            r = 1 - Math.sin(radius);
            b = 1;
            g = 1 - Math.cos(radius);
        } else if (s < 2) {
            r = 1;
            b = 1 - Math.cos(radius);
            g = 1 - Math.sin(radius);
        } else {
            r = 1 - Math.cos(radius);
            b = 1 - Math.sin(radius);
            g = 1;
        }

        int ri = (int) (r * 255 * saturation);
        int bi = (int) (b * 255 * saturation);
        int gi = (int) (g * 255 * saturation);

        System.out.println(s + "\t\t:" + ri + ":" + bi + ":" + gi);

        int[] c = {ri, bi, gi};

        return c;
    }

    public static int[] intToComponents(int argb) {
        int[] a = {
            (argb >> 24) & 0xFF,
            (argb >> 16) & 0xFF,
            (argb >> 8) & 0xFF,
            argb & 0xFF
        };
        return a;
    }

    public static int componentsToInt(int a, int r, int g, int b) {
        return (a << 24) + (r << 16) + (g << 8) + b;
    }

    public static int componentsToInt(int r, int g, int b) {
        return (r << 16) + (g << 8) + b;
    }

    public static BufferedImage maskImage(BufferedImage mask, int[] bgColor, int[] fgColor) {
        BufferedImage outImg = new BufferedImage(mask.getWidth(), mask.getHeight(), BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < mask.getHeight(); y++) {
            for (int x = 0; x < mask.getWidth(); x++) {
                int[] newc = {
                    0, 0, 0
                };

                int a = (mask.getRGB(x, y) >> 24) & 0xFF;//Alpha component

                if (a == 0) {
                    outImg.setRGB(x, y, componentsToInt(bgColor[0], bgColor[1], bgColor[2]));
                } else {
                    //System.out.println(x + ":" + y + ":" + a);

                    //System.out.println(a+":"+(double)a / 255);
                    //a = (int) (a * 1.1);
                    double fgOpacity = (double) a / 255;
                    double bgOpacity = 1 - fgOpacity;

                    //System.out.println(fgOpacity+":"+bgOpacity);
                    newc[0] = (int) (fgColor[0] * fgOpacity + bgColor[0] * bgOpacity);
                    newc[1] = (int) (fgColor[1] * fgOpacity + bgColor[1] * bgOpacity);
                    newc[2] = (int) (fgColor[2] * fgOpacity + bgColor[2] * bgOpacity);

                    //System.out.println(newc[0] + ":" + newc[1] + ":" + newc[2]);
                    outImg.setRGB(x, y, componentsToInt(0, newc[0], newc[1], newc[2]));
                    //outImg.setRGB(x, y, componentsToInt(0, 255, 0, 0));
                }
            }
        }

        return outImg;
    }

}
