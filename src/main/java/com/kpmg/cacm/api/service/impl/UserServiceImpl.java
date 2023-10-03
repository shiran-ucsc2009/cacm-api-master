package com.kpmg.cacm.api.service.impl;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import com.kpmg.cacm.api.exceptions.PreConditionFailedException;
import com.kpmg.cacm.api.model.PasswordHistory;
import com.kpmg.cacm.api.model.User;
import com.kpmg.cacm.api.repository.spring.CategoryRepository;
import com.kpmg.cacm.api.repository.spring.PasswordHistoryRepository;
import com.kpmg.cacm.api.repository.spring.UserGroupRepository;
import com.kpmg.cacm.api.repository.spring.UserRepository;
import com.kpmg.cacm.api.security.SpringSecurityUserDetails;
import com.kpmg.cacm.api.service.FileStorageService;
import com.kpmg.cacm.api.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordHistoryRepository passwordHistoryRepository;

    private final UserGroupRepository userGroupRepository;

    private final CategoryRepository categoryRepository;

    private final FileStorageService fileStorageService;

    private final PasswordEncoder passwordEncoder;

    @Value("${cacm.config.user.profile-image-path}")
    private String userProfileImagePath;

    @Value("${ad.intergration}")
    private String IS_AD_TRUE;

    @Value("${cacm.config.user.expiry-date}")
    private int ExpiryDays;

    @Autowired
    public UserServiceImpl(
            final UserRepository userRepository,
            final PasswordHistoryRepository passwordHistoryRepository,
            final UserGroupRepository userGroupRepository,
            final CategoryRepository categoryRepository,
            FileStorageService fileStorageService,
            final PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordHistoryRepository = passwordHistoryRepository;
        this.userGroupRepository = userGroupRepository;
        this.categoryRepository = categoryRepository;
        this.fileStorageService = fileStorageService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> findAll(final String groupName, final Long categoryId) {
        return this.userRepository.findAllByUserGroupAndCategoryAndDeletedFalse(
            this.userGroupRepository.findByNameAndDeletedFalse(groupName),
            this.categoryRepository.findAllByIdAndDeletedFalse(categoryId)
        );
    }

    @Override
    public User findByUsername(final String username) {
        return this.userRepository.findByUsernameAndDeletedFalse(username);
    }

    @Override
    public User findById(final Long id) {
        return this.userRepository.findByIdAndDeletedFalse(id);
    }

    @Override
    public User save(final MultipartFile file, User user) {
        PasswordHistory passwordHistory = new PasswordHistory();
        LocalDate expiryDate = LocalDate.now().plusDays(ExpiryDays);
        if (this.findByUsername(user.getUsername()) != null) {
            throw new PreConditionFailedException("Username already exists");
        }
        user.setExpiryDate(expiryDate);
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        user = this.userRepository.save(user);

//        save password history
        passwordHistory.setUser(user);
        passwordHistory.setPassword(user.getPassword());
        this.passwordHistoryRepository.save(passwordHistory);

        this.fileStorageService.saveImageAsJpg(file, user.getId() + ".jpg", this.userProfileImagePath);
        return user;
    }

    @Override
    public User update(final MultipartFile file, final User user) {
        if(!this.findByUsername(user.getUsername()).getId().equals(user.getId())) {
            throw new PreConditionFailedException("Username already exists");
        }
        this.fileStorageService.saveImageAsJpg(file, user.getId() + ".jpg", this.userProfileImagePath);
        return this.userRepository.save(user);
    }

    @Override
    public void deleteById(final Long id) {
        final User user = this.findById(id);
        user.setDeleted(true);
        user.setDeletionToken(new Date().getTime());
        this.userRepository.save(user);
    }

    @Override
    public User getCurrentUser() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final User loggedUser;
        if (authentication!=null && authentication.isAuthenticated()) {
            if(IS_AD_TRUE.equals("True")) {
                loggedUser= (User) authentication.getPrincipal();
            }else{
                loggedUser = ((SpringSecurityUserDetails) SecurityContextHolder.getContext().getAuthentication()
                        .getPrincipal()).getUser();
            }
        } else {
            loggedUser = new User();
            loggedUser.setUsername("SYSTEM");
            loggedUser.setName("SYSTEM");
        }
        return loggedUser;
    }

    @Override
    public Boolean resetPassword(final Long id, final String newPassword, final String oldPassword) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User loggedUser = ((SpringSecurityUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        PasswordHistory passwordHistory = new PasswordHistory();
        LocalDate expiryDate = LocalDate.now().plusDays(ExpiryDays);
        final User user = this.findById(id);
        if("".equals(oldPassword)) { // When Admin password reset
            user.setPassword(this.passwordEncoder.encode(newPassword));
            user.setExpiryDate(expiryDate);
            this.userRepository.save(user);
            return true;

        }else{

            if (this.passwordEncoder.matches(oldPassword, user.getPassword()) && checkPasswordHistory(user,newPassword)) {
                user.setPassword(this.passwordEncoder.encode(newPassword));
                user.setExpiryDate(expiryDate);
                this.userRepository.save(user);

//              Save password history table
                passwordHistory.setUser(user);
                passwordHistory.setPassword(user.getPassword());
                this.passwordHistoryRepository.save(passwordHistory);
                return true;

            } else {
                throw new PreConditionFailedException("Password does not match");
            }
        }
    }

    public Boolean checkPasswordHistory(User user, String password) {
        List<PasswordHistory> passwordHistories = this.passwordHistoryRepository.findAllByUser(user);

        for (PasswordHistory passwordHistory1 : passwordHistories) {
            if (this.passwordEncoder.matches(password, passwordHistory1.getPassword())) {
                throw new PreConditionFailedException("Enter different password");
            }
        }

        return true;
    }

    @Override
    public Boolean resetPasswordFirstAttempt(String username, String password) {

        final User user = this.userRepository.findByUsernameAndDeletedFalse(username);
        List<PasswordHistory> passwordHistories = this.passwordHistoryRepository.findAllByUser(user);

        for (PasswordHistory passwordHistory1 : passwordHistories) {
            if (this.passwordEncoder.matches(password, passwordHistory1.getPassword())) {
                throw new PreConditionFailedException("Enter different password");
            }
        }


        PasswordHistory passwordHistory1 = new PasswordHistory();
        if (!user.getUsername().equals(username)) {
            throw new PreConditionFailedException("User Invalid");
        }

        user.setExpiryDate(LocalDate.now().plusDays(ExpiryDays));
        user.setPassword(this.passwordEncoder.encode(password));
        user.setFirstAttempt(true);
        this.userRepository.save(user);

        passwordHistory1.setUser(user);
        passwordHistory1.setPassword(user.getPassword());
        this.passwordHistoryRepository.save(passwordHistory1);
        return true;
    }

    @Override
    public Resource getUserProfileImage(final Long id) throws FileNotFoundException {
        return this.fileStorageService.loadFileAsResource(this.userProfileImagePath + id + ".jpg");
    }

}
