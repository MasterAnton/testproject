package com.example.testproject;

import com.example.testproject.entity.User;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.ImageWriteException;
import org.apache.sanselan.common.IImageMetadata;
import org.apache.sanselan.formats.jpeg.JpegImageMetadata;
import org.apache.sanselan.formats.jpeg.exifRewrite.ExifRewriter;
import org.apache.sanselan.formats.tiff.write.TiffOutputSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.stream.Stream;

@Service
public class FileSystemStorageService implements StorageService {

    private final Path rootLocation;
    private final ApplicationContextProvider appContext;

    @Autowired
    public FileSystemStorageService() {
        appContext = new ApplicationContextProvider();
        this.rootLocation = Paths.get(Objects.requireNonNull(
                appContext.getApplicationContext().getEnvironment().getProperty("upload.path")
                ));
    }

    public String getHash() throws NoSuchAlgorithmException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User u = (User) auth.getPrincipal();
        String plaintext = u.getEmail()+appContext.getApplicationContext().getEnvironment().getProperty("files.hash");
        MessageDigest m = MessageDigest.getInstance("MD5");
        m.reset();
        m.update(plaintext.getBytes());
        byte[] digest = m.digest();
        BigInteger bigInt = new BigInteger(1,digest);
        String hashtext = bigInt.toString(16);
        while(hashtext.length() < 32 ){
            hashtext = "0"+hashtext;
        }
        return hashtext;
    }
    @Override
    public String store(MultipartFile file) {
        return store( file, null);
    }

    @Override
    public String store(MultipartFile file, IImageMetadata metadata) {
        String filename = null;
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + file.getOriginalFilename());
            }

            filename = getHash()+"_"+file.getOriginalFilename();
            Files.copy(file.getInputStream(), this.rootLocation.resolve(filename));
            if (metadata!=null){
                JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
                TiffOutputSet outputSet  = jpegMetadata.getExif().getOutputSet();
                new ExifRewriter().updateExifMetadataLossless(
                        file.getInputStream(),
                        new BufferedOutputStream(
                                new FileOutputStream(
                                        this.rootLocation.resolve(filename).toFile()
                                )
                        ),
                        outputSet
                );
            }
        } catch (IOException e) {
            throw new StorageException("Failed to store file " + file.getOriginalFilename(), e);
        } catch (ImageWriteException | ImageReadException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return filename;
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(this.rootLocation::relativize);
        } catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }

    }

    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if(resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageFileNotFoundException("Could not read file: " + filename);

            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public void init() {
        try {
            if (!Files.isDirectory(rootLocation))
                Files.createDirectory(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }
}