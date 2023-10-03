package com.kpmg.cacm.api.service;

import com.kpmg.cacm.api.model.User;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.util.List;

public interface UserService {

    List<User> findAll(String groupName, Long categoryId);

    User findByUsername(String username);

    User findById(Long id);

    User save(MultipartFile file, User user);

    User update(MultipartFile file, User user);

    void deleteById(Long id);

    User getCurrentUser();

    Boolean resetPassword(Long id, String newPassword, String oldPassword);

    Boolean resetPasswordFirstAttempt(String username,String password);

    Resource getUserProfileImage(Long id) throws FileNotFoundException;
}
