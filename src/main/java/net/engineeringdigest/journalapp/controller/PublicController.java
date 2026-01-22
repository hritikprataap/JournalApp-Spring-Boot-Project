package net.engineeringdigest.journalapp.controller;

import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journalapp.entity.User;
import net.engineeringdigest.journalapp.service.UserDetailServiceImpl;
import net.engineeringdigest.journalapp.service.UserEntryService;
import net.engineeringdigest.journalapp.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
@Slf4j
public class PublicController {

    @Autowired
    private UserDetailServiceImpl userDetailService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserEntryService userEntryService;

    @Autowired
    private JwtUtil jwtUtil;


 @GetMapping("/health-check")//ye function map ho jaega is endpoint se /health check
 //matlb ki ye jo function hai healthcheck wo map ho jaega upr getmapping ke sth wale string ke sath
 public String healthcheck(){
     return "ok";
 }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
     try{
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword()));
         UserDetails userDetails =userDetailService.loadUserByUsername(user.getUserName());
         String jwt = jwtUtil.generateToken(userDetails.getUsername());
         return new ResponseEntity<>(jwt,HttpStatus.CREATED);
     }catch(Exception e){
          log.error("Exception occured while createAuthenticationToken",e);
          return new ResponseEntity<>("Incorrect username or password",HttpStatus.BAD_REQUEST);



    }}

    @PostMapping("/signup")
    public ResponseEntity<Void> sighnUp(@RequestBody User user) {
        userEntryService.saveNewUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
