package com.example.mainpofol.myinfo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MyInfoAskRequest {

    @NotBlank(message = "question is required")
    private String question;
}
