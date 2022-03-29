package me.yeonnex.userservice.vo;

import lombok.Data;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class RequestUser {

    @NotNull(message = "Email cannot be null")
    @Email
    private String email;

    @NotNull(message = "Name cannot be null")
    private String name;

    @NotNull(message = "Password cannot be null")
    private String pwd;

}
