package net.engineeringdigest.journalapp.repositry;


import net.engineeringdigest.journalapp.entity.ConfigJournalAppEntity;
import net.engineeringdigest.journalapp.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ConfigJournalAppRepository extends MongoRepository<ConfigJournalAppEntity, ObjectId>{

}
