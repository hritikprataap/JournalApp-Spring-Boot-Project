package net.engineeringdigest.journalapp.service;

import net.engineeringdigest.journalapp.API.respone.WeatherResponse;
import net.engineeringdigest.journalapp.Cache.AppCache;
import net.engineeringdigest.journalapp.Constants.PlaceHolders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;



@Service
public class WeatherService {

    @Value("${weather.api.key}")
    private String apiKey; //here we are abstracting the api key via value annotATION


    @Autowired
    private AppCache appCache;

    @Autowired
    private RestTemplate restTemplate;

    public WeatherResponse getWeather(String city){

        String FinalApi = appCache.App_Cache.get(AppCache.keys.WEATHER_API.toString()).replace(PlaceHolders.City, city).replace(PlaceHolders.API_KEY, apiKey);
        ResponseEntity<WeatherResponse> response = restTemplate.exchange(FinalApi, HttpMethod.GET, null, WeatherResponse.class);
        WeatherResponse body = response.getBody();
        return body;
    }

}