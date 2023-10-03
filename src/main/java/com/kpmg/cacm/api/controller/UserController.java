package com.kpmg.cacm.api.controller;

import com.kpmg.cacm.api.dto.request.PasswordFirstAttemptRequest;
import com.kpmg.cacm.api.dto.request.PasswordUpdateRequest;
import com.kpmg.cacm.api.dto.request.UserAddRequest;
import com.kpmg.cacm.api.dto.request.UserUpdateRequest;
import com.kpmg.cacm.api.dto.response.CommonMessageResponse;
import com.kpmg.cacm.api.dto.response.EmptyResponse;
import com.kpmg.cacm.api.dto.response.PasswordUpdateResponse;
import com.kpmg.cacm.api.dto.response.UserResponse;
import org.springframework.core.io.Resource;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.FileNotFoundException;

@RestController
public interface UserController {

    @Secured("FIND_ALL_USERS")
    @GetMapping("/users")
    UserResponse findAll(
        @RequestParam(value = "group", required = false) String groupName,
        @RequestParam(value = "category", required = false) Long categoryId
    );

    @Secured("FIND_CURRENT_USER")
    @GetMapping("/users/me")
    UserResponse findCurrent();

    @Secured("FIND_USER_BY_ID")
    @GetMapping("/users/{id}")
    UserResponse findById(@PathVariable("id") Long id);

    @Secured("FIND_USER_IMAGE")
    @GetMapping(value = "users/{id}/image", produces = {"image/jpg", "image/jpeg", "image/png"})
    Resource findUserImage(@PathVariable("id") Long id) throws FileNotFoundException;

    @Secured("ADD_USER")
    @PostMapping("/users")
    EmptyResponse save(@RequestParam(value = "profileImage",required = false) MultipartFile file, UserAddRequest userAddRequest);

    @Secured("UPDATE_USER")
    @PostMapping("/users-update")
    EmptyResponse update(@RequestParam(value = "profileImage",required = false) MultipartFile file,UserUpdateRequest userUpdateRequest);

    @Secured("DELETE_USER")
    @DeleteMapping("/users/{id}")
    EmptyResponse delete(@PathVariable("id") Long userId);

    @Secured("RESET_PASSWORD_PROFILE")
    @PutMapping("/users/reset-password")
    PasswordUpdateResponse resetPassword(@Valid @RequestBody PasswordUpdateRequest passwordUpdateRequest);

    @Secured("RESET_PASSWORD_ADMIN")
    @PutMapping("/users/reset-password-admin")
    PasswordUpdateResponse resetPasswordAdmin(@Valid @RequestBody PasswordUpdateRequest passwordUpdateRequest);

    @PostMapping("/user/reset-password-first-attempt")
    EmptyResponse resetPasswordFirstAttempt(@RequestBody PasswordFirstAttemptRequest passwordFirstAttemptRequest);

    @Secured("PASSWORD_EXPIRE_GRACE_PERIOD")
    @GetMapping("/users/password-grace-period")
    CommonMessageResponse checkPasswordExpiration();

}
