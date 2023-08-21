package com.example.testproject.utils;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class ImageHandler{
    public static MultipartFile generateThumb(MultipartFile multipartFile, String namePrefix, int targetWidth) throws IOException {
        String thumbName = namePrefix+"_"+multipartFile.getOriginalFilename();
        InputStream input = multipartFile.getInputStream();
        BufferedImage bufferedImage = ImageIO.read(input);

        int bufferWidth = bufferedImage.getWidth();
        int bufferHeight = bufferedImage.getHeight();
        if(bufferWidth<=bufferHeight){
            bufferedImage = bufferedImage.getSubimage(0, (bufferHeight-bufferWidth)/2, bufferWidth, bufferWidth);
        }
        else{
            bufferedImage = bufferedImage.getSubimage((bufferWidth-bufferHeight)/2, 0, bufferHeight, bufferHeight);
        }
        Image resultingImage = bufferedImage.getScaledInstance(targetWidth, targetWidth, Image.SCALE_SMOOTH);
        BufferedImage image = new BufferedImage(targetWidth, targetWidth, BufferedImage.TYPE_INT_RGB);
        image.getGraphics().drawImage(resultingImage, 0, 0, null);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write( image, "jpeg", baos );

        return new MockMultipartFile(thumbName,thumbName,"image/jpeg", baos.toByteArray());
    }
}
