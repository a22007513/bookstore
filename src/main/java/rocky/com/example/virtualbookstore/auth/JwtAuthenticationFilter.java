package rocky.com.example.virtualbookstore.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import rocky.com.example.virtualbookstore.service.Impl.userServiceImpl;
import rocky.com.example.virtualbookstore.service.jwtService;


import java.io.IOException;

@Component
@Slf4j
//JwtAuthFilter for filter each request trigger(can also implement filter interface)
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private jwtService jwtService;


    //DI userServiceImpl class causer we want
    @Autowired
    private userServiceImpl userService;

    //check jwt token
    @Override
    protected void doFilterInternal(
                                   @NonNull HttpServletRequest request,
                                   @NonNull HttpServletResponse response,
                                   @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        //user send the request with token will add header with Authorization
        String authHeader = request.getHeader("Authorization");
        String jwt = null;
        String userEmail = null;
        if(authHeader!=null && authHeader.startsWith("Bearer ")){
            jwt = authHeader.substring(7);
            userEmail = jwtService.extractUserName(jwt);
        }
        //jwt=token  7 means ignore header value (Bearer )

        //check user is authentication or not(second condition false) if not getting user from DB
        if(userEmail!=null && SecurityContextHolder.getContext().getAuthentication()==null) {
            //get user info from DB by username
            UserDetails userDetails = this.userService.loadUserByUsername(userEmail);
            log.info("{}",userDetails);
            //check token valid or not(search jwt subject vs userdetail username query from DB)
            if (jwtService.isTokenValid(jwt,userDetails)){
                //package userDetail(info) and authorites (role) as token
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                //add UPAT token in SecurityContextHolder
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        //go to next filter or execute request
        filterChain.doFilter(request,response);
    }
}
