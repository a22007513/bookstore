package rocky.com.example.virtualbookstore.service;

import org.springframework.http.ResponseEntity;
import rocky.com.example.virtualbookstore.request.authenticationRequest;
import rocky.com.example.virtualbookstore.request.authenticationResponse;
import rocky.com.example.virtualbookstore.request.registerRequest;

public interface authService {

     authenticationResponse register( registerRequest request);

    authenticationResponse authenticate( authenticationRequest request);
}
