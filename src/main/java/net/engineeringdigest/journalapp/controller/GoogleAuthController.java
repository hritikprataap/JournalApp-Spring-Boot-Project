package net.engineeringdigest.journalapp.controller;

import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journalapp.entity.User;
import net.engineeringdigest.journalapp.repositry.UserEntryRepository;
import net.engineeringdigest.journalapp.service.UserDetailServiceImpl;
import net.engineeringdigest.journalapp.service.UserEntryService;
import net.engineeringdigest.journalapp.utils.JwtUtil;
import org.apache.coyote.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/auth/google")
@Slf4j
public class GoogleAuthController {

    @Autowired
    private RestTemplate restTemplate;


    @Autowired
    private UserDetailServiceImpl userDetailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserEntryRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;


    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @GetMapping("/callback")
    public ResponseEntity<?>handleGoogleCallBack(@RequestParam String code){
       try{
           //1.Exchange Authorisation code with google
      String tokenEndPoint="https://oauth2.googleapis.com/token";
           MultiValueMap<String ,String>params=new LinkedMultiValueMap<>();
           params.add("code",code);
           params.add("client_id",clientId);
           params.add("client_secret",clientSecret);
           params.add("redirect_uri","https://developers.google.com/oauthplayground");
           params.add("grant_type","authorization_code");

           HttpHeaders headers=new HttpHeaders();
           headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
           HttpEntity<MultiValueMap<String,String>>request=new HttpEntity<>(params,headers);
           ResponseEntity<Map>tokenResponse=restTemplate.postForEntity(tokenEndPoint,request, Map.class);
           String idToken = (String) tokenResponse.getBody().get("id_token");
           String userInfoUrl = "https://oauth2.googleapis.com/tokeninfo?id_token=" + idToken;
           ResponseEntity<Map> userInfoResponse = restTemplate.getForEntity(userInfoUrl, Map.class);
           if (userInfoResponse.getStatusCode() == HttpStatus.OK) {
               Map<String, Object> userInfo = userInfoResponse.getBody();
               String email = (String) userInfo.get("email");
               UserDetails userDetails = null;
               try{
                   userDetails = userDetailService.loadUserByUsername(email);
               }catch (Exception e){
                   User user = new User();
                   user.setEmail(email);
                   user.setUserName(email);
                   user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
                   user.setRoles(Arrays.asList("USER"));
                   userRepository.save(user);
               }
               String jwtToken = jwtUtil.generateToken(email);
               return ResponseEntity.ok(Collections.singletonMap("token", jwtToken));
           }
           return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
       } catch (Exception e) {
           log.error("Exception occurred while handleGoogleCallback ", e);
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
       }

    }
}