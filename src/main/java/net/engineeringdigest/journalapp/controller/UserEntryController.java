package net.engineeringdigest.journalapp.controller;

import net.engineeringdigest.journalapp.entity.User;
import net.engineeringdigest.journalapp.repositry.UserEntryRepository;
import net.engineeringdigest.journalapp.service.UserEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class  UserEntryController {

    @Autowired
    private UserEntryService userEntryService;

    @Autowired
    private UserEntryRepository userEntryRepository;
    // 🔒 GET ALL USERS (requires auth)
//    @GetMapping
//    public List<User> getAll() {
//        return userEntryService.getall();
//    }

    // 🔒 GET USER BY USERNAME (requires auth)
    @GetMapping("/by-username/{userName}")
    public ResponseEntity<User> getByUserName(@PathVariable String userName) {
        User user = userEntryService.findByUserName(userName);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(user);
    }




    // 🔒 UPDATE USER (requires auth)
    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody User user) {
         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //all the username are stored in seqcontholder and obtain it and get its name
        String userName = authentication.getName();
        User userInDb = userEntryService.findByUserName(userName);
        userInDb.setUserName(user.getUserName());
        userInDb.setPassword(user.getPassword());
        userEntryService.saveNewUser(userInDb);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    public ResponseEntity<?>deleteuUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        userEntryRepository.deleteByUserName(authentication.getName());
         return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
