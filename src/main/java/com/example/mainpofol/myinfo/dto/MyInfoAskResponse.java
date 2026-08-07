package com.example.mainpofol.myinfo.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyInfoAskResponse {

    private final String question;
    private final List<String> selectedFiles;
    private final String tableOfContentsResponse;
    private final String answer;
}
