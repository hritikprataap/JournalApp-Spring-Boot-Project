package net.engineeringdigest.journalapp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journalapp.API.respone.WeatherResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class RedisSevice {
    @Autowired
    private RedisTemplate redisTemplate;

    //we are here
    // net.engineeringdigest.journalapp.service.RedisSevice

//    Request: "Give me the data for key weather_Mumbai as a WeatherResponse object."
//
//    Redis: "Here is the raw data I have."
//
//    Mapper: "I will convert this raw data back into a Java object format."
//
//    Result: You receive a usable Java object to work with in your Controller.
    public <T> T get(String key, Class<T> entityClass) {
        try {
            Object o = redisTemplate.opsForValue().get(key);
            if (o == null) {
                return null; // Return null if key doesn't exist in Redis
            }
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(o.toString(), entityClass);
        } catch (Exception e) {
            log.error("Exception ", e);
            return null;
        }
    }
    //ttl=time to live

    //we set a key corosponding a object for a particular time
    //cache kitne der ke liye karana hai is ttl
    public void set(String key, Object o,Long ttl){
        try{
            ObjectMapper objectMapper=new ObjectMapper();
            String jsonValue=objectMapper.writeValueAsString(o);
           redisTemplate.opsForValue().set(key,jsonValue,ttl, TimeUnit.SECONDS);

        }catch(Exception e){
            log.error("Exception ",e);
        }


    }
}
