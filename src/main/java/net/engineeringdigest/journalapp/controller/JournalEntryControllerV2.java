package net.engineeringdigest.journalapp.controller;

import net.engineeringdigest.journalapp.entity.JournalEntry;
import net.engineeringdigest.journalapp.entity.User;
import net.engineeringdigest.journalapp.service.JournalEntryServices;

import net.engineeringdigest.journalapp.service.UserEntryService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;

@RestController//means this class is a component but also provides som e special kind of functionalities matlb ki wo ioc me jaega hio ye wlai class
@RequestMapping("/journal")//iske karn andr ke sare methods ko ek base mil gaya ki jo bhi maping hogi usme journal to rahega hi

public class  JournalEntryControllerV2 {
@Autowired
//autowire ka use karke ham direct bean ka use car rahe
private JournalEntryServices journalEntryService;//

    @Autowired
    private UserEntryService userService;


    @GetMapping
    //iska add rahega /journal/abc
    public ResponseEntity<?> getAllJournalEntriesOfUser(){//ye ek method hai matlb ki function hai
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //all the username are stored in seqcontholder and obtain it and get its name
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);//find user
        List <JournalEntry>all=user.getJournalEntries();//the all list will contain journalEntries and id is saved in the user
        if(all!=null&&!all.isEmpty()){
            return new ResponseEntity<>(all,HttpStatus.OK);
 }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @PostMapping
    //localhost:8080/journal POST
    //agr postman pe post ki call hogi to yaha pe aa jaeg a
    public ResponseEntity<JournalEntry >createEntryUser(@RequestBody JournalEntry myEntry){
        try {//use of exception aND RESPONSE ENTITY
            //we are posting journalentries for a particular user

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            //all the username are stored in seqcontholder and obtain it and get its name
            String userName = authentication.getName();

            journalEntryService.saveEntry(myEntry,userName);
            return new ResponseEntity<>(myEntry, HttpStatus.CREATED);
        }
        catch(Exception e){
             return new ResponseEntity<>(myEntry, HttpStatus.BAD_REQUEST);
        }
    }
   @GetMapping("id/{myId}")
   //myId is a path variable means ki abhi agr apn get ki call maar rahe the to sare entries dikha de raha tha by now hame agr specific dekhna hai to ye use karege
    public ResponseEntity <JournalEntry>getJournalEntryBy(@PathVariable ObjectId myId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
       //all the username are stored in seqcontholder and obtain it and get its name
       String userName = authentication.getName();
       User user = userService.findByUserName(userName);//find user
       List<JournalEntry> collect = user.getJournalEntries().stream().filter(x -> x.getId().equals(myId)).collect(Collectors.toList());
       //user ki journal entries ki list aae hogi to usse required id ko nikal kar collect wali list me daal dege
       if(!collect.isEmpty()){
           Optional<JournalEntry> journalEntry=journalEntryService.getEntry(myId);
           if(journalEntry.isPresent()){//use of Response entity
               return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
           }
           //path variable jo hai wo map me search karega ki kiski id 2 hai and jab usse 2 mil jaega to wo usse dikha dega
       }

      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping ("id/{myId}")
    //yaha pe dekho to dono ka path same ban raha hai to ye chalega hi nahi to iski verb chnage karni padegi

    public ResponseEntity<?> deleteJournalEntryById(@PathVariable ObjectId myId){//?ye lagane se koe aur object se bhi khel sakte hain
        //here we have to delete the journalEntries o
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //all the username are stored in seqcontholder and obtain it and get its name
        String userName = authentication.getName();
        boolean removed=journalEntryService.deleteById(myId,userName);
        if(removed){
     return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    else{
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    }

    @PutMapping("id/{myId}")
    public ResponseEntity<?> updateJournalbyId(@PathVariable ObjectId myId,@RequestBody JournalEntry newEntry) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //all the username are stored in seqcontholder and obtain it and get its name
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);//find user
        List<JournalEntry> collect = user.getJournalEntries().stream().filter(x -> x.getId().equals(myId)).collect(Collectors.toList());
        //user ki journal entries ki list aae hogi to usse required id ko nikal kar collect wali list me daal dege
        if (!collect.isEmpty()) {
            Optional<JournalEntry> journalEntry = journalEntryService.getEntry(myId);
            if (journalEntry.isPresent()) {//use of Response entity
                JournalEntry old = journalEntry.get();
                old.setTitle(newEntry.getTitle() != null && !newEntry.getTitle().equals("") ? newEntry.getTitle() : old.getTitle());
                old.setContent(newEntry.getContent() != null && !newEntry.getContent().equals("") ? newEntry.getContent() : old.getContent());
                journalEntryService.saveEntry(old);
                return new ResponseEntity<>(old, HttpStatus.OK);

            }


            //path variable jo hai wo map me search karega ki kiski id 2 hai and jab usse 2 mil jaega to wo usse dikha dega
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}








