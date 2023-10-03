package com.kpmg.cacm.api.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;

public interface FileStorageService {

    Resource loadFileAsResource(String fileName) throws FileNotFoundException;

    String saveFile(MultipartFile file, String fileName, String filePath);

    void saveImageAsJpg(MultipartFile file, String fileName, String filePath);
}
