package com.kpmg.cacm.api.service.impl;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import com.kpmg.cacm.api.service.FileStorageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Slf4j
@Service
public class FileStorageServiceImpl implements FileStorageService {

    @Override
    public Resource loadFileAsResource(final String path) throws FileNotFoundException{
        try {
            return new UrlResource(Paths.get(path).toAbsolutePath().normalize().toUri());
        } catch (final MalformedURLException exception) {
            FileStorageServiceImpl.log.error("File not found at {}", path, exception);
            throw new FileNotFoundException("File not found at " + path);
        }
    }

    @Override
    public String saveFile(final MultipartFile file, final String fileName, final String filePath) {
        String fileNameWithExt = null;
        if (file != null && !file.isEmpty()) {
            fileNameWithExt = fileName + '.' + FilenameUtils.getExtension(file.getOriginalFilename());
            try {
                Files.write(Paths.get(filePath + fileNameWithExt), file.getBytes());
            } catch (final IOException exception) {
                FileStorageServiceImpl.log.error("Error saving file", exception);
            }
        }
        return fileNameWithExt;
    }

    @Override
    public void saveImageAsJpg(final MultipartFile file, final String fileName, final String filePath) {
        if (file != null && !file.isEmpty()) {
            if(file.getContentType()!=null && "image".equals(file.getContentType().split("/")[0])){
                try {
                    final BufferedImage image = ImageIO.read(file.getInputStream());
                    final BufferedImage result = new BufferedImage(
                        image.getWidth(),
                        image.getHeight(),
                        BufferedImage.TYPE_INT_RGB
                    );
                    result.createGraphics().drawImage(image, 0, 0, Color.WHITE, null);
                    ImageIO.write(
                        result,
                        "jpg",
                        new FileOutputStream(filePath + fileName)
                    );
                } catch (final IOException exception) {
                    FileStorageServiceImpl.log.error("Error writing profile image to the storage.", exception);
                }
            } else {
                FileStorageServiceImpl.log.error("File is not a valid image");
            }
        }
    }
}
