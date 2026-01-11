package net.engineeringdigest.journalapp.entity;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import net.engineeringdigest.journalapp.entity.JournalEntry;

import java.util.ArrayList;
import java.util.List;


// impppppppp
//Controller always returns Entities

@Document(collection = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
//using data anotation we can directly get getters,setters,allagrconstructor,noargcontructors and etc
public class User {
    @Id
    private ObjectId id;
    @Indexed(unique = true)//automatically create nhi hoga for this we have to do a entry inn resource->application
    //such that all the username must be unique
    @NonNull
    //this field must not be nim null
    private String userName;
    @NonNull
    private String password;
    @DBRef //use to create link between user and journal_entries
    //it only holds id not the full content
    private List<JournalEntry> journalEntries=new ArrayList<>();
    private List<String>rolls;


}//using lombok we can resduce boiler plate code
