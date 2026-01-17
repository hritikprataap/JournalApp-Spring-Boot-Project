package net.engineeringdigest.journalapp.controller;

import net.engineeringdigest.journalapp.API.respone.WeatherResponse;
import net.engineeringdigest.journalapp.entity.User;
import net.engineeringdigest.journalapp.repositry.UserEntryRepository;
import net.engineeringdigest.journalapp.service.UserEntryService;
import net.engineeringdigest.journalapp.service.WeatherService;
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
   
//    @GetMapping
//    public List<User> getAll() {
//        return userEntryService.getall();
// }
    @Autowired
    private WeatherService weatherService;


    
    @GetMapping
    public ResponseEntity<?> greeting() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
         WeatherResponse weatherResponse = weatherService.getWeather("Mumbai");
        String greeting = "";
        if (weatherResponse != null) {
            greeting = ", Weather feels like " + weatherResponse.getCurrent().getFeelslike();
        }
        return new ResponseEntity<>("Hi " + authentication.getName() + greeting, HttpStatus.OK);
    }




    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody User user) {
         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //all the username are stored in secontholder and obtain it and get its name
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
