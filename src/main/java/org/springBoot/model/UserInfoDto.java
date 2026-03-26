package org.springBoot.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springBoot.entities.UserInfo;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class) //Not working need to check
@Data
//@Builder
//@AllArgsConstructor
//@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserInfoDto extends UserInfo {
    private String firstName;
    private String lastName;
    private Long phoneNumber;
    private String email;
}
