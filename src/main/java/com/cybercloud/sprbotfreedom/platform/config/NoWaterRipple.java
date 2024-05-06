package com.cybercloud.sprbotfreedom.platform.config;
import com.google.code.kaptcha.GimpyEngine;
import com.google.code.kaptcha.util.Configurable;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

/**
 * 验证码图片生成
 */
public class NoWaterRipple extends Configurable implements GimpyEngine {
		public NoWaterRipple(){}

		@Override
		public BufferedImage getDistortedImage(BufferedImage baseImage) {
			BufferedImage distortedImage = new BufferedImage(baseImage.getWidth(), baseImage.getHeight(), 2);
			Graphics2D graphics = (Graphics2D)distortedImage.getGraphics();
			graphics.drawImage(baseImage, 0, 0, (Color)null, (ImageObserver)null);
			graphics.dispose();
			return distortedImage;
		}
	}
