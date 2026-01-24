package net.engineeringdigest.journalapp.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class  UserDto {
    @NotEmpty
    private String userName;
    private String email;
    private boolean sentimentalAnalysis;
    @NotEmpty
    private String password;
}
