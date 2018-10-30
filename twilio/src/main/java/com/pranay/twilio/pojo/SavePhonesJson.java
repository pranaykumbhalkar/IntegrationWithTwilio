package com.pranay.twilio.pojo;

import lombok.Data;

/**
 * Created By Pranay on 10/30/2018
 */

@Data
public class SavePhonesJson {
    private String phone;
    private String callTwiML;
    private String messageTwiML;
    private Integer selected;
    private Long phoneId;
}
