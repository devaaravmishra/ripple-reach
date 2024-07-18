package com.ripplereach.ripplereach.utilities;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

public class ImageUtil {

  public static byte[] compressImage(byte[] imageData, String formatName) throws IOException {
    ByteArrayInputStream inputStream = new ByteArrayInputStream(imageData);
    BufferedImage image = ImageIO.read(inputStream);

    ByteArrayOutputStream compressedOutputStream = new ByteArrayOutputStream();
    Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName(formatName);

    if (!writers.hasNext()) {
      return imageData;
    }

    ImageWriter writer = writers.next();
    ImageOutputStream ios = ImageIO.createImageOutputStream(compressedOutputStream);
    writer.setOutput(ios);

    ImageWriteParam param = writer.getDefaultWriteParam();

    if ("jpg".equalsIgnoreCase(formatName) || "jpeg".equalsIgnoreCase(formatName)) {
      if (param.canWriteCompressed()) {
        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        param.setCompressionQuality(0.5f);
      }
    }

    writer.write(null, new IIOImage(image, null, null), param);
    ios.close();
    writer.dispose();

    return compressedOutputStream.toByteArray();
  }
}
