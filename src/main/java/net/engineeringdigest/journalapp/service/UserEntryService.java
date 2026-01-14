package net.engineeringdigest.journalapp.service;

import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journalapp.entity.User;
import net.engineeringdigest.journalapp.repositry.UserEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j//u can directly use this annotation from lombok as u didnt require to give that logger ins tance
//using component we are actually creating a bean jo ioc me store hoga
public class UserEntryService {
    @Autowired
    private UserEntryRepository userEntryRepository;

    private static final PasswordEncoder passwordEncoder =new BCryptPasswordEncoder();

    //slf4j is logging abstraction framework
    //whit the help of this we can talk to the loggerback instance
    //implementation khud nahi karta logback karega yaha

 //   private static final Logger logger= LoggerFactory.getLogger(UserEntryService.class);
    //use that call in logger in which u are working rn


    public void saveUser(User user) {
        userEntryRepository.save(user);
    }

    public boolean saveNewUser(User user) {
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles((Arrays.asList("USER")));
            userEntryRepository.save(user);
            return true;
        }
        catch (Exception e) {
            log.info("Error occured for {} :",user.getUserName(),e);
            log.error("hahahahahahhaha");
            log.warn("hahahahahahhaha");
            log.debug("hahahahahahhaha");
            log.trace("hahahahahahhaha");
         return false;
        }
    }

    public void saveAdmin(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList("USER","ADMIN"));
        userEntryRepository.save(user);
    }





    public List<User> getall(){
        return userEntryRepository.findAll();

    }

    public Optional<User> getEntry(ObjectId id){
        return userEntryRepository.findById(id);
    }
    public void deleteById(ObjectId id){
        userEntryRepository.deleteById(id);
    }


    public User findByUserName(String userName){
        return userEntryRepository.findByUserName(userName);
    }

}
