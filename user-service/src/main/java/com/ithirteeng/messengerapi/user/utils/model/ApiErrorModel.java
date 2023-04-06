package com.ithirteeng.messengerapi.user.utils.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ApiErrorModel {

    private String errorMessage;

    private int statusCode;
}
