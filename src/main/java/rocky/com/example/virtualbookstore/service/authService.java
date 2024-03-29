package rocky.com.example.virtualbookstore.service;

import org.springframework.http.ResponseEntity;
import rocky.com.example.virtualbookstore.module.User;
import rocky.com.example.virtualbookstore.request.authenticationRequest;
import rocky.com.example.virtualbookstore.request.authenticationResponse;
import rocky.com.example.virtualbookstore.request.registerRequest;

public interface authService {

     User register(registerRequest request);

    authenticationResponse authenticate( authenticationRequest request);
}
