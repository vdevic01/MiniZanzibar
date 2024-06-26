package com.example.MiniZanzibarBack.dto;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserRegistrationDTO {
    @Email(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "Field email is not valid!")
    private String email;

    @Length(min = 6,  message = "Field password is not valid. Minimum length are 6 characters!")
    private String password;
}
