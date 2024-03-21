package rocky.com.example.virtualbookstore.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import rocky.com.example.virtualbookstore.module.User;
import rocky.com.example.virtualbookstore.request.userRequest;
import rocky.com.example.virtualbookstore.service.jwtService;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/auth/user")
@Slf4j
public class userController {

    @Autowired
    jwtService jwtService;


    @PostMapping("/test")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<User> getUserByEmail(@RequestBody userRequest userRequest){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ArrayList<String> authList = new ArrayList<>(authentication.getAuthorities().size());
        for( GrantedAuthority authority : authentication.getAuthorities()){
            authList.add(authority.toString());
        }
        log.info("authority {} : " ,authList);
        log.info("email is : {}",userRequest.getEmail());
       User user=  jwtService.getUserByEmail(userRequest.getEmail());
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @DeleteMapping("/user/{userid}")
    public ResponseEntity<?> deleteUserByid(@PathVariable Integer userid){
        jwtService.deleteUserByid(userid);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @PutMapping("/admin/updateuser")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<User> updateUserByEmail(@RequestBody userRequest userRequest){
        User originalUser = jwtService.getUserByEmail(userRequest.getEmail());
        if(originalUser!=null){
            User updateUser =   jwtService.updateUser(userRequest.getEmail(),"ADMIN");
            return  ResponseEntity.status(HttpStatus.OK).body(updateUser);
        }
        else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }
}
