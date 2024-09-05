package com.cybercloud.sprbotfreedom.platform.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.io.File;
import java.io.IOException;

/**
 * @author liuyutang
 * @date 2024/9/5
 */
public class ImageUtil {
    public static void main(String[] args) throws Exception {

        // 截全屏幕保存成文件
        fullScreenShotAsFile("E:\\screenshot\\","sceenshot0","jpg");

        File imageFile = new File("E:\\screenshot\\sceenshot0.jpg");
        // 自动打开图片
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
            Desktop.getDesktop().open(imageFile);
        }

    }

    /**
     * 指定屏幕区域截图，返回截图的BufferedImage对象
     * @param x
     * @param y
     * @param width
     * @param height
     * @return
     */
    public BufferedImage getScreenShot(int x, int y, int width, int height) {
        BufferedImage bfImage = null;
        try {
            Robot robot = new Robot();
            bfImage = robot.createScreenCapture(new Rectangle(x, y, width, height));
        } catch (AWTException e) {
            e.printStackTrace();
        }
        return bfImage;
    }

    /**
     * 全屏幕区域截图，返回截图的BufferedImage对象
     * @return
     */
    public BufferedImage getFullScreenShot() {
        BufferedImage bfImage = null;
        try {
            Robot robot = new Robot();
            // 获取当前显示器屏幕大小
            Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
            bfImage = robot.createScreenCapture(new Rectangle(0, 0, (int) d.getWidth(), (int) d.getHeight()));
        } catch (AWTException e) {
            e.printStackTrace();
        }
        return bfImage;
    }

    /**
     * 指定屏幕区域截图，保存到指定目录
     * @param x
     * @param y
     * @param width
     * @param height
     * @param savePath - 文件保存路径
     * @param fileName - 文件保存名称
     * @param format - 文件格式
     */
    public void screenShotAsFile(int x, int y, int width, int height, String savePath, String fileName, String format) {
        try {
            Robot robot = new Robot();
            BufferedImage bfImage = robot.createScreenCapture(new Rectangle(x, y, width, height));
            File path = new File(savePath);
            File file = new File(path, fileName+ "." + format);
            ImageIO.write(bfImage, format, file);
        } catch (AWTException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 全屏幕区域截图，保存到指定目录
     * @param savePath - 文件保存路径
     * @param fileName - 文件保存名称
     * @param format - 文件格式
     */
    public static void fullScreenShotAsFile (String savePath,String fileName,String format) {
        try {
            Robot robot = new Robot();
            // 获取当前显示器屏幕大小
            Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
            //拷贝屏幕到一个BufferedImage对象screenshot
            BufferedImage bfImage = robot.createScreenCapture(new Rectangle(0, 0, (int) d.getWidth(), (int) d.getHeight()));
            File path = new File(savePath);
            File file = new File(path, fileName+ "." + format);
            ImageIO.write(bfImage, format, file);
        } catch (AWTException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * BufferedImage图片剪裁
     * @param srcBfImg - 被剪裁的BufferedImage
     * @param x - 左上角剪裁点X坐标
     * @param y - 左上角剪裁点Y坐标
     * @param width - 剪裁出的图片的宽度
     * @param height - 剪裁出的图片的高度
     * @return 剪裁得到的BufferedImage
     */
    public BufferedImage cutBufferedImage(BufferedImage srcBfImg, int x, int y, int width, int height) {
        BufferedImage cutedImage = null;
        CropImageFilter cropFilter = new CropImageFilter(x, y, width, height);
        Image img = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(srcBfImg.getSource(), cropFilter));
        cutedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = cutedImage.getGraphics();
        g.drawImage(img, 0, 0, null);
        g.dispose();
        return cutedImage;
    }
}
