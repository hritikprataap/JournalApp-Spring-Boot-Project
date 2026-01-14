package net.engineeringdigest.journalapp.repositry;

import net.engineeringdigest.journalapp.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class UserRepositoryImpl {
    @Autowired
    private MongoTemplate mongoTemplate;

    public List<User> getUserForSA(){

        //the basic objective is to fijnd the users who have opted for the sentimental reprt and there emails exsist
        Query query=new Query();
        query.addCriteria(Criteria.where("email").regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"));

        query.addCriteria(Criteria.where("sentimentalAnalysis").is(true));
        //query.addCriteria(Criteria.where("userName").nin("Rajat","Shanu"));
        //say u want to blacklist crtain user so u can use this


        //Criteria criteria=new Criteria();
        //adding operators
        //query.addCriteria(criteria.andOperator(Criteria.where("email").exists(true).ne(null).ne(""),Criteria.where("sentimentalAnalysis").is(true)));
        List<User> users = mongoTemplate.find(query, User.class);
        return users;

    }
}
