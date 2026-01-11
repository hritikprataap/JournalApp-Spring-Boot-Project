package net.engineeringdigest.journalapp.repositry;


import net.engineeringdigest.journalapp.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserEntryRepository extends MongoRepository<User, ObjectId>{
    User findByUserName (String userName);
    User deleteByUserName(String userName );
}
