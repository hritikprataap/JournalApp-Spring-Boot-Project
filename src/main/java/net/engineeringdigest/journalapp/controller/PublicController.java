package net.engineeringdigest.journalapp.controller;

import net.engineeringdigest.journalapp.entity.User;
import net.engineeringdigest.journalapp.service.UserEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
public class PublicController {

    @Autowired
    private UserEntryService userEntryService;

 @GetMapping("/health-check")//ye function map ho jaega is endpoint se /health check
 //matlb ki ye jo function hai healthcheck wo map ho jaega upr getmapping ke sth wale string ke sath
 public String healthcheck(){
     return "ok";
 }

    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody User user) {
        userEntryService.saveNewUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
