package net.engineeringdigest.journalapp.Service;


import net.engineeringdigest.journalapp.entity.User;
import net.engineeringdigest.journalapp.repositry.UserEntryRepository;
import net.engineeringdigest.journalapp.service.UserEntryService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Disabled //this is done so that here only the application feels to run so the
public class UserServiceTests {
    @Autowired
    private UserEntryRepository userEntryRepository;

    @Autowired
    private UserEntryService userEntryService;

    @Disabled
    @Test
    public void testFindByUserName(){
        assertNotNull(userEntryRepository.findByUserName("hritik"));
        User user = userEntryRepository.findByUserName("hritik");
        assertTrue(user.getJournalEntries().isEmpty());

    }
    @BeforeEach
    void setup(){
        //if u want that before starting any test u wnat run this method then use this annotation
    }

    @AfterAll
    static void setupp(){
//after every method ye chlega
    }

    @Disabled//used to disable any particuar test
    @ParameterizedTest
    @CsvSource({
            "1,1,3",
            "3,4,7"
    })
    public void test(int a,int b,int expected){
        assertEquals(expected,a+b);
    }


//    @Disabled
//    @ParameterizedTest
//    @CsvSource({
//            "ram",
//            "rahul",
//            "hritik"
//    })
//    public void testFindByUserName(String name){
//        assertNotNull(userEntryRepository.findByUserName(name),"failed for: "+name);
//
//
//    }

    @ParameterizedTest
    @ArgumentsSource(UserArgumentProvider.class)
    public void testSaveNewUser(User user){
        assertTrue(userEntryService.saveNewUser(user),"failed for: "+user);
    }



}
