package com.example.demo.service;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

import com.example.demo.Exception.ApiRequestException;
import com.example.demo.Payload.Request.ChangeImageUserRequest;
import com.example.demo.Payload.Request.SignupRequest;
import com.example.demo.domain.ConfirmationToken;
import com.example.demo.domain.ERole;
import com.example.demo.domain.Role;
import com.example.demo.domain.User;
import com.example.demo.repo.RoleRepo;
import com.example.demo.repo.UserRepo;
import com.example.demo.service.CloudBinary.CloudBinaryService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;


@Service

public class UserService {
    @Autowired
    UserRepo userRepository;

    @Autowired
    RoleRepo roleRepository;
    @Autowired
    private    ConfirmationTokenService confirmationTokenService;

     @Autowired
    PasswordEncoder encoder;

    @Value("${javadocfast.cloudinary.folder.image}")
    private String image;

    private Map<String, String> options = new HashMap<>();

    @Autowired
    private CloudBinaryService cloudinaryService;

    public String signUpUser(@NotNull SignupRequest signUpRequest) throws IOException {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new ApiRequestException("Error: Username is already taken!");
        }
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw  new ApiRequestException("Error: Email is already in use!");
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()),false,false);
        Set<String> strRoles = signUpRequest.getRoles();
        Set<Role> roles = new HashSet<>();
        if (strRoles == null) {

            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new ApiRequestException("Error: Role is not found."));
            roles.add(userRole);
        } else {
                if (strRoles.contains("ROLE_ADMIN")) {
                    Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                            .orElseThrow(() -> new ApiRequestException("Error: Role is not found."));
                    roles.add(adminRole);
                    user.setEnable(true);
                    user.setLocked(true);

                } else if (strRoles.contains("ROLE_MODERATOR")) {
                    Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                            .orElseThrow(() -> new ApiRequestException("Error: Role is not found."));
                    roles.add(modRole);
                    user.setEnable(true);
                    user.setLocked(true);
                }else {
                    Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                            .orElseThrow(() -> new ApiRequestException("Error: Role is not found."));
                    roles.add(userRole);
                }

        }


//        MultipartFile multipartFile = signUpRequest.getImage();
//        Map<String, String> options = new HashMap<>();
//        options.put("folder", image);
//
//        Map result = cloudinaryService.upload(multipartFile, options);
//        String linkImg = result.get("url").toString();
//        String nameImg = result.get("public_id").toString();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        user.setRoles(roles);
        user.setLinkImage("https://res.cloudinary.com/tphcm/image/upload/v1651378139/AvatarDefault/Anh-avatar-dep-chat-lam-hinh-dai-dien_bpyymd.jpg");
        user.setNameImage("v1651378139/AvatarDefault/Anh-avatar-dep-chat-lam-hinh-dai-dien_bpyymd.jpg");
        user.setAddress(signUpRequest.getAddress());
        user.setPhone(signUpRequest.getPhone());
        user.setFullname(signUpRequest.getFullname());
        user.setLastname(signUpRequest.getLastname());
        user.setDescription(signUpRequest.getDescription());
        user.setGender(signUpRequest.getGender());
        user.setDateOfbirth(signUpRequest.getDateOfbirth());
        user.setProvince(signUpRequest.getProvince());
        userRepository.save(user);
        String token = UUID.randomUUID().toString();


            ConfirmationToken confirmationToken = new ConfirmationToken(
                    token,
                    LocalDateTime.now(),
                    LocalDateTime.now().plusMinutes(15),
                    user
            );
            confirmationTokenService.ConfirmationToken(confirmationToken);
           return token;

    }


    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }



    public List<User> getUsers() {
        return userRepository.findAll();
    }


    public void addToRoleUser(String username, ERole e) {
        User user = userRepository.findByUsername(username).get();
        Set<Role> roles01 = new HashSet<>();
        Role roleAdmin01 = roleRepository.findByName(e).get();
        roles01.add(roleAdmin01);
        user.getRoles().addAll(roles01);
    }


    public User Profile(String username){
        return userRepository.findByUsername(username).get();
    }

    public int enableAppUser(String email) {
        return userRepository.enableAppUser(email);
    }

    public String ChangeImageProfile(ChangeImageUserRequest request) throws IOException {
        if (!userRepository.existsByUsername(request.getUsername())) {
            throw new ApiRequestException("Error: Username is already taken!");
        }
        User user = userRepository.findByUsername(request.getUsername()).get();
        MultipartFile multipartFile = request.getImage();

        if (multipartFile != null) {
            BufferedImage bufferedImage = ImageIO.read(multipartFile.getInputStream());
            if (bufferedImage == null) {
                throw new   ApiRequestException("Error: Invalid image");
            }

            // Folder To Save Avatar
            options.put("folder", image);

            if (user.getNameImage() != null) {
                // Delete Old Avatar
                cloudinaryService.delete(user.getNameImage(), options);
            }

            // Update New Avatar
            Map result = cloudinaryService.upload(multipartFile, options);
            user.setLinkImage(result.get("url").toString());
            user.setNameImage(result.get("public_id").toString());
        }

        // Update row of table User in Database
        userRepository.save(user);
        return "Update succesfully";
    }

}
