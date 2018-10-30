package com.pranay.twilio.pojo;

import lombok.Data;

import java.util.List;

/**
 * Created By Pranay on 10/30/2018
 */
@Data
public class MakeCallJson {
    private String accountSid;
    private String authToken;
    private Long companyId;
    private Long userId;
    private String fromNumber;
    private String toNumber;
    private String uri;
    private String message;
    private List<SavePhonesJson> phoneList;
    private String callSid;
    private String missCallAction;
    private String missCallValue;
    private String voiceMessage;
    private Integer timeoutTime;
    private Integer pageNo;
    private String appSid;
}
