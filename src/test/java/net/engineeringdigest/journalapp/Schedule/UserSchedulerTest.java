package net.engineeringdigest.journalapp.Schedule;

import net.engineeringdigest.journalapp.schedular.UserScheduler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserSchedulerTest {
    @Autowired
    private UserScheduler userScheduler;

    @Test
    public void testFetchUserAndSendSaMail(){
          userScheduler.fetchUserAndSendSaMails();
    }
}
