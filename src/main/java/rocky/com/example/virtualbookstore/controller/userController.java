package rocky.com.example.virtualbookstore.controller;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import rocky.com.example.virtualbookstore.module.User;
import rocky.com.example.virtualbookstore.request.authenticationRequest;
import rocky.com.example.virtualbookstore.request.userRequest;
import rocky.com.example.virtualbookstore.request.userSearchRequest;
import rocky.com.example.virtualbookstore.request.userUpdateRequest;
import rocky.com.example.virtualbookstore.service.Impl.userServiceImpl;
import rocky.com.example.virtualbookstore.service.jwtService;
import rocky.com.example.virtualbookstore.service.userService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/auth/user")
@Slf4j
@Validated
public class userController {

    @Autowired
    @Qualifier("GetUserDetail")
    public userService userService;


    @GetMapping("/finduser")
    public ResponseEntity<Page<User>> getUserByEmail(
                                                //condition
                                               @RequestParam(required = false) String search,
                                               //pagetation
                                               @RequestParam(defaultValue = "10") @Max(100) Integer limit,
                                               @RequestParam(defaultValue = "0") @Min(0) @Max(100) Integer offset,
                                               @RequestParam(defaultValue = "createDate") String orderBy,
                                               @RequestParam(defaultValue = "DESC") String sortMethod) throws ParseException {
        userSearchRequest userSearchRequest = new userSearchRequest();
        userSearchRequest.setSearch(search);
        userSearchRequest.setLimit(limit);
        userSearchRequest.setOffset(offset);
        userSearchRequest.setOrderBy(orderBy);
        userSearchRequest.setSortMethod(Sort.Direction.fromString(sortMethod));
       Page<User> result = userService.findAllUser(userSearchRequest);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @DeleteMapping("/deleteUser/{username}")
    public ResponseEntity<?> deleteUserByid(@PathVariable String username){
        userService.deleteUser(username);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @PutMapping("/admin/modifyauthority")
    public ResponseEntity<User> updateUserByEmail(@RequestBody userRequest userRequest){
        User originalUser = userService.findUserByEmail(userRequest.getEmail());
        if(originalUser!=null){
            User updateUser =   userService.modifyUserAuthority(userRequest.getEmail());
            return  ResponseEntity.status(HttpStatus.OK).body(updateUser);
        }
        else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

    @PutMapping("/resetPassword")
    public ResponseEntity<Date> updateUserPassword(@RequestBody userUpdateRequest userUpdateRequest){
        Date updateTime = userService.updateUserPassword(userUpdateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(updateTime);
    }
}
