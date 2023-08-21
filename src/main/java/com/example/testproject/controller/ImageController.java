package com.example.testproject.controller;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.testproject.ApplicationContextProvider;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


@Controller
@RequestMapping("api/images")
public class ImageController {
    @GetMapping("/{filename:.+}")
    public void getImage(@PathVariable String filename, HttpServletResponse response) throws IOException {
        ApplicationContextProvider appContext = new ApplicationContextProvider();
        File initialFile = new File(appContext.getApplicationContext().getEnvironment().getProperty("upload.path")+filename);
        InputStream in = new FileInputStream(initialFile);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        IOUtils.copy(in, response.getOutputStream());
    }
}
