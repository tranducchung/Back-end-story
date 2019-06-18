package com.codegym.controller;

<<<<<<< HEAD
import com.codegym.config.MyConstants;
import com.codegym.message.request.LoginForm;
import com.codegym.message.request.SignUpForm;
import com.codegym.message.response.JwtResponse;
import com.codegym.message.response.ResponseMessage;
import com.codegym.model.ConfirmationToken;
=======
import com.codegym.payload.request.LoginForm;
import com.codegym.payload.request.SignUpForm;
import com.codegym.payload.response.JwtResponse;
import com.codegym.payload.response.ResponseMessage;
>>>>>>> nbthanh
import com.codegym.model.Role;
import com.codegym.model.RoleName;
import com.codegym.model.User;
import com.codegym.repository.ConfirmationTokenRepository;
import com.codegym.repository.RoleRepository;
import com.codegym.repository.UserRepository;
import com.codegym.security.jwt.JwtProvider;
import com.codegym.security.service.UserPrinciple;
<<<<<<< HEAD
import com.codegym.service.ConfirmationTokenService;
import com.codegym.service.RoleService;
import com.codegym.service.SendEmailService;
=======
import com.codegym.service.RoleService;
>>>>>>> nbthanh
import com.codegym.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthRestAPI {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
<<<<<<< HEAD
    private ConfirmationTokenService confirmationTokenService;

    @Autowired
    private SendEmailService sendEmailService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    UserService userService;

    @Autowired
    RoleService roleService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtProvider jwtProvider;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginForm loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateJwtToken(authentication);
        UserPrinciple userDetails = (UserPrinciple) authentication.getPrincipal();

        return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getUsername(), userDetails.getEmail(),userDetails.getAuthorities()));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpForm signUpRequest) {
        if (userService.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity<>(new ResponseMessage("Fail -> Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }

        if (userService.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity<>(new ResponseMessage("Fail -> Email is already in use!"),
                    HttpStatus.BAD_REQUEST);
        }

        // Creating user's account
        User user = new User(signUpRequest.getName(), signUpRequest.getUsername(), signUpRequest.getEmail(),
                passwordEncoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        strRoles.forEach(role -> {
            switch (role) {
                case "admin":
                    Role adminRole = roleService.findByName(RoleName.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
                    roles.add(adminRole);

                    break;

                default:
                    Role userRole = roleService.findByName(RoleName.ROLE_USER)
                            .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
                    roles.add(userRole);
            }
        });

        user.setRoles(roles);
        user.setActive(0);
        userService.save(user);

        // create confirmtoken when save user success

        ConfirmationToken confirmationToken = new ConfirmationToken();
        confirmationToken.setUser(user);
        Date createDate = new Date();
        confirmationToken.setCreateDate(createDate);
        // createToken

        String token = UUID.randomUUID().toString();
        confirmationToken.setConfirmationToken(token);

        System.out.println("confirmationToken = " + confirmationToken);
        confirmationTokenService.save(confirmationToken);
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(user.getEmail());
        simpleMailMessage.setSubject("Complete Registration!");
        simpleMailMessage.setFrom(MyConstants.MY_EMAIL);
        simpleMailMessage.setText("Click to confirm email and active your account: " +
                "http://localhost:8080/api/auth/confirm-email?token="+confirmationToken.getConfirmationToken());

        sendEmailService.sendEmail(simpleMailMessage);
        userService.save(user);

        return new ResponseEntity<>(new ResponseMessage("User registered successfully!"), HttpStatus.OK);
    }

    @GetMapping("/confirm-email")
    public String confirmEmail(@RequestParam("token") String token) {
        ConfirmationToken confirmToken = confirmationTokenService.findByToken(token);

        Optional<User> user = userService.findByEmail(confirmToken.getUser().getEmail());

        if( confirmToken != null ) {
            user.get().setActive(1);
            userService.save(user.get());
            return "Confirm email success!";
        }
        return " confirm email fail! ";
    }
}
