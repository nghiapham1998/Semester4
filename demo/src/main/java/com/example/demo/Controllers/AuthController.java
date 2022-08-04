package com.example.demo.Controllers;


import com.example.demo.Exception.ApiRequestException;
import com.example.demo.Payload.Request.*;
import com.example.demo.Payload.Response.JwtResponse;

import com.example.demo.domain.ERole;
import com.example.demo.domain.Role;
import com.example.demo.domain.User;
import com.example.demo.repo.RoleRepo;
import com.example.demo.repo.UserRepo;
import com.example.demo.security.JwtUtils;
import com.example.demo.service.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.view.RedirectView;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/api/auth",method = {RequestMethod.POST,RequestMethod.GET})
@AllArgsConstructor
@Validated
public class AuthController  {

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UserRepo userRepository;
    @Autowired
    RoleRepo roleRepository;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    JwtUtils jwtUtils;

    private final ResetPasswordService resetPasswordService;
    private final RegisterService registerService;

    private final ConfirmationTokenService confirmationTokenService;

    private final UserService userService;
//    @Autowired
//    UserDetailsServiceImpl userService;


    @PostMapping(value = "/signin", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        JwtResponse response = new JwtResponse(jwt,
                userDetails.getUsername(),
                userDetails.getEmail(),
                userDetails.getId(),
                roles, userDetails.getNameImage(), userDetails.getLinkImage(), userDetails.isAccountNonLocked(), userDetails.isEnabled());

        User user = userRepository.findByUsername(loginRequest.getUsername()).get();
        user.setToken(response.getAccessToken());
        userRepository.save(user);

        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "/signup")

    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) throws IOException, MessagingException, ParseException {


        String token = registerService.register(signUpRequest);

        return ResponseEntity.ok(token);
//
    }


    @GetMapping(path = "confirm")
    public RedirectView confirm(@RequestParam("token") String token)  {
        return registerService.confirmToken(token);
    }

    @PostMapping("/changeEmail")
    public String changePassword(@RequestBody ResetPasswordRequest passwordRequest) throws MessagingException {
        return resetPasswordService.changePassword(passwordRequest);
    }

    @GetMapping(path = "confirmPassword")
    public RedirectView changePassword(@RequestParam("token") String token)  {
        return resetPasswordService.confirmToken(token);
    }

    @PostMapping("/updatePassword")
    public String updatePassword( @Valid @RequestBody ResetPasswordRequest passwordRequest)  {


        resetPasswordService.updatePassword(passwordRequest);
        return "Success fully";
    }

    @GetMapping("/profile/{username}")
    public ResponseEntity<?> proFile(@PathVariable String username) {
        User u = userService.Profile(username);
        return ResponseEntity.ok(u);
    }

    @PostMapping(value = "/profile/changeImageProfile" )
    public ResponseEntity<?> changeImageProfile(  ChangeImageUserRequest request) throws IOException {
        String response = userService.ChangeImageProfile(request);
        if (response != null) {
            return ResponseEntity.ok("update succesfully");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("update not found");
    }

    @PostMapping("profile/updateProfile/{id}")
    public ResponseEntity<?> updateProfile(@PathVariable("id") Long id, @Valid @RequestBody UpdateProfile request) throws IOException {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            User u = user.get();
            u.setLastname(request.getLastname());
            u.setDescription(request.getDescription());
            u.setFullname(request.getFullname());
            u.setPhone(request.getPhone());
            u.setAddress(request.getAddress());
            u.setEmail(request.getEmail());
            u.setDateOfbirth(request.getDateOfbirth());
            u.setGender(request.getGender());
            userRepository.save(u);
            return ResponseEntity.ok(u);

        } else {
            throw new ApiRequestException( "Save change fails");
        }
    }

}

