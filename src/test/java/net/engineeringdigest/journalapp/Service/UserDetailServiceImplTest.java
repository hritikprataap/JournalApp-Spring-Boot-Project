package net.engineeringdigest.journalapp.Service;

import net.engineeringdigest.journalapp.entity.User;
import net.engineeringdigest.journalapp.repositry.UserEntryRepository;
import net.engineeringdigest.journalapp.service.UserDetailServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.Arguments;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;

import static org.mockito.Mockito.*;

@ActiveProfiles("dev")
@Disabled
public class UserDetailServiceImplTest{

    @InjectMocks
    private UserDetailServiceImpl userDetailService;

    @Mock
    private UserEntryRepository userEntryRepository;

    @BeforeEach
    void setup(){//initialise all the mocks and also inject it because3 we are not using @Autowired
        //the userEntryRepository will be initialised and also be injected in userDetailService
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void LoadUSerByUsernameTest(){
        when(userEntryRepository.findByUserName(ArgumentMatchers.anyString())).thenReturn(User.builder().userName("ram").password("vfveaa").rolls(new ArrayList<>()).build());
        UserDetails user = userDetailService.loadUserByUsername("ram");
        Assertions.assertNotNull(user);
    }
}
