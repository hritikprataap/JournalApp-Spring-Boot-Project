package net.engineeringdigest.journalapp.controller;

import net.engineeringdigest.journalapp.Cache.AppCache;
import net.engineeringdigest.journalapp.entity.User;
import net.engineeringdigest.journalapp.service.UserEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")


public class AdminController {
    @Autowired
    private UserEntryService userEntryService;


    @Autowired
    public AppCache appCache;

    @GetMapping("clear_app_cache")
    public void ClearAppCache(){
        appCache.init();

    }


    @GetMapping("/all-users")
    public ResponseEntity<?>getAllUsers(){
        List<User> getall = userEntryService.getall();
        if(getall!=null&&!getall.isEmpty()){
            return new ResponseEntity<>(getall, HttpStatus.OK);
        }
        return new ResponseEntity<>( HttpStatus.NOT_FOUND);
    }

     @PostMapping("/create-new-admin")
    public void createUser(@RequestBody User user){
        userEntryService.saveAdmin(user);
     }





}


