package rocky.com.example.virtualbookstore.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rocky.com.example.virtualbookstore.module.User;
import rocky.com.example.virtualbookstore.request.authenticationRequest;
import rocky.com.example.virtualbookstore.request.authenticationResponse;
import rocky.com.example.virtualbookstore.request.registerRequest;
import rocky.com.example.virtualbookstore.service.Impl.userServiceImpl;
import rocky.com.example.virtualbookstore.service.authService;

@RestController
@RequestMapping("/api/auth/v1")
@Slf4j
public class authController {

    @Autowired
    public authService authService;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody registerRequest request){
       User user = authService.register(request);
        return ResponseEntity.status(200).body(user);
    }

    @PostMapping("/authenticate")

    public ResponseEntity<authenticationResponse> authenticate(@RequestBody authenticationRequest request){
        authenticationResponse authenticationResponse = authService.authenticate(request);
        //Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //ArrayList<String> authList = new ArrayList<>(authentication.getAuthorities().size());
        //for( GrantedAuthority authority : authentication.getAuthorities()){
        //    authList.add(authority.toString());
        //}
        //log.info("authority {} : " ,authList);
        //log.info("email is : {}",request.getEmail());
        return ResponseEntity.status(200).body(authenticationResponse);
    }

    @GetMapping("/test")
    public String justTest(){
        return "just test";
    }

}
