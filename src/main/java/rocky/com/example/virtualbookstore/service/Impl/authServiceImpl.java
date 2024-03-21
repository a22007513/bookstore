package rocky.com.example.virtualbookstore.service.Impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import rocky.com.example.virtualbookstore.module.User;
import rocky.com.example.virtualbookstore.repository.userRepository;
import rocky.com.example.virtualbookstore.request.authenticationRequest;
import rocky.com.example.virtualbookstore.request.authenticationResponse;
import rocky.com.example.virtualbookstore.request.registerRequest;
import rocky.com.example.virtualbookstore.service.authService;
import rocky.com.example.virtualbookstore.service.jwtService;

import java.util.Date;

@Component
@Slf4j
public class authServiceImpl implements authService {

    @Autowired
    private userRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private jwtService jwtService;


    @Override
    public authenticationResponse register(registerRequest request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("ROLE_USER");
        Date now = new Date();
        user.setCreateDate(now);
        user.setLastModifyDate(now);
        userRepository.save(user);
        String token = jwtService.createToken(user.getEmail());
        authenticationResponse response = authenticationResponse.builder()
                .token(token).build();
        return response;
    }

    @Override
    public authenticationResponse authenticate(authenticationRequest request) {
        //authenticate method if pass means user input password and username is correct
       Authentication authentication = authenticationManager.authenticate(new  UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
            )
        );
       log.info("authentication or not",authentication.isAuthenticated());
        if(authentication.isAuthenticated()){
            String token = jwtService.createToken(request.getEmail());
            authenticationResponse response = authenticationResponse.builder()
                    .token(token).build();
            return response;
        }
        else {
            throw new UsernameNotFoundException("Username Invalid");
        }
    }
}
