package net.engineeringdigest.journalapp.service;

import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journalapp.entity.JournalEntry;
import net.engineeringdigest.journalapp.entity.User;
import net.engineeringdigest.journalapp.repositry.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
//using component we are actually creating a bean jo ioc me store hoga
public class  JournalEntryServices {
    @Autowired
    private JournalEntryRepository journalEntryRepository;
    @Autowired
    private UserEntryService userService;




    @Transactional
    public void saveEntry(JournalEntry journalEntry, String userName){
       try {
           User user = userService.findByUserName(userName);//find user
           journalEntry.setDate(LocalDateTime.now());//set date time
           //save the journalEntry and save it in a local variable
           JournalEntry saved = journalEntryRepository.save(journalEntry);
           user.getJournalEntries().add(saved);//save that journalEntry in the journalenties of user
           userService.saveUser(user);//finally the user is also saved in the data base
       }
       catch(Exception e){
           log.error("Error is detected",e);
           throw new RuntimeException("an Error hasbeen occured whille saving the entry.",e);
       }



    }
    public void saveEntry(JournalEntry journalEntry){
        journalEntryRepository.save(journalEntry);//finally the user is also saved in the data base




    }



    public List<JournalEntry> getall(){
        return journalEntryRepository.findAll();

    }

    public Optional<JournalEntry> getEntry(ObjectId id){
        return journalEntryRepository.findById(id);
    }

    @Transactional
    public boolean deleteById(ObjectId id, String userName){
        boolean removed =false;
        try{
            User user = userService.findByUserName(userName);
            removed= user.getJournalEntries().removeIf(x->x.getId().equals(id));//getId se journalEntry ki id milegi and if it is equal to given id as parameter then remove it
            if(removed){
                userService.saveUser(user);
                journalEntryRepository.deleteById(id);
            }
        }catch(Exception e){
            System.out.println(e);
            throw new RuntimeException("An error occured during deletion",e);
        }
     return removed;

    }

    private List<JournalEntry> journalEntries = new ArrayList<>();



}
