package net.engineeringdigest.journalapp.schedular;

import net.engineeringdigest.journalapp.Cache.AppCache;
import net.engineeringdigest.journalapp.entity.JournalEntry;
import net.engineeringdigest.journalapp.model.SentimentData;
import net.engineeringdigest.journalapp.entity.User;
import net.engineeringdigest.journalapp.enums.Sentiment;
import net.engineeringdigest.journalapp.repositry.UserRepositoryImpl;
import net.engineeringdigest.journalapp.service.EmailService;
import net.engineeringdigest.journalapp.service.SentimentAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UserScheduler {
    @Autowired
    private EmailService emailService;
    @Autowired
    private UserRepositoryImpl userRepository;

    @Autowired
    private SentimentAnalysisService sentimentAnalysisService;

    @Autowired
    private AppCache appCache;

    @Autowired
    private KafkaTemplate<String,SentimentData>kafkaTemplate;


    @Scheduled(cron ="0 0 9 * * SUN")
    //@Scheduled(cron="0 * * ? * *")
    //this is a cron expression given such that this functin is automaticlally called on sunday
    public void fetchUserAndSendSaMails(){
        //fetch the list of users opted for sentimental analysis
        List<User> users = userRepository.getUserForSA();
        for(User user:users){
            List<JournalEntry> journalEntries = user.getJournalEntries();
            List<Sentiment> sentiments = journalEntries.stream().filter(x -> x.getDate().isAfter(LocalDateTime.now().minus(7, ChronoUnit.DAYS))).map(x -> x.getSentiment()).collect(Collectors.toList());
            //we have filtered the list of last 7 days and then we only want sentiment(from enum) so list of string
            Map<Sentiment,Integer> SentimentCount=new HashMap<>();
                    for(Sentiment sentiment:sentiments){
                        SentimentCount.put(sentiment,SentimentCount.getOrDefault(sentiment,0)+1);
                    }
            Sentiment mostFrequentSentiment = null;
            int maxCount = 0;
            for (Map.Entry<Sentiment, Integer> entry : SentimentCount.entrySet()) {
                if (entry.getValue() > maxCount) {
                    maxCount = entry.getValue();
                    mostFrequentSentiment = entry.getKey();
                }
            }
            if (mostFrequentSentiment != null) {
                SentimentData sentimentData = SentimentData.builder().email(user.getEmail()).sentiment("Sentiment for last 7 days " + mostFrequentSentiment).build();
                try{
                    kafkaTemplate.send("weekly-sentiments", sentimentData.getEmail(), sentimentData);
                }catch (Exception e){
                    //by chnace if our kafka cloud doesn't work then this is the fallback handling
                    emailService.sendEmail(sentimentData.getEmail(), "Sentiment for previous week", sentimentData.getSentiment());
                }
            }
        }
    }


    //schedule the appCache to autmaticlly run after 10 mins
    @Scheduled(cron = "0 0/10 * * * *")
    public void clearAppCache(){
        appCache.init();
    }



}
