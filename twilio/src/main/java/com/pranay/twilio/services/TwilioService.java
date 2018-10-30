package com.pranay.twilio.services;

import com.pranay.twilio.pojo.MakeCallJson;
import com.pranay.twilio.pojo.UniversalResponse;
import com.pranay.twilio.utils.TwilioUtils;
import com.twilio.rest.api.v2010.account.Call;
import com.twilio.rest.api.v2010.account.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;

/**
 * Created By Pranay on 10/30/2018
 */
@Service
public class TwilioService {
    private final static Logger logger = LoggerFactory.getLogger(TwilioService.class);


    public void makeCall(MakeCallJson req) throws URISyntaxException {
        Call call = TwilioUtils.makeCall(req);
    }

    public void sendSMS(MakeCallJson req) {
        Message message = TwilioUtils.sendSMS(req);
    }

    public UniversalResponse getAllTwilioPhoneNumber(String accountSid, String authToken) {
        UniversalResponse response = new UniversalResponse();
        response.setList(TwilioUtils.getAllPhoneNumbers(accountSid,authToken));
        return response;
    }

    public UniversalResponse getAllTwilioCallingApplication(String accountSid, String authToken) {
        UniversalResponse response = new UniversalResponse();
        response.setList(TwilioUtils.getTwilioApplication(accountSid,authToken));
        return response;
    }

    public void updateApplication(MakeCallJson req) {
        TwilioUtils.updateTwilioApplication(req.getAccountSid(),req.getAccountSid(),req.getAppSid());
    }

    public void disconnectCall(MakeCallJson req) {
        TwilioUtils.disconectCall(req.getAccountSid(),req.getAccountSid(),req.getAppSid());
    }

    public UniversalResponse getCallStatus(String accountSid, String authToken, String callSid) {
        UniversalResponse<Call> response = new UniversalResponse<>();
        response.setObject(TwilioUtils.getCallStatusByAPI(accountSid,authToken,callSid));
        return response;
    }

    public UniversalResponse getTwilioVerifiedCallerId(String accountSid, String authToken) {
        UniversalResponse response = new UniversalResponse();
        response.setList(TwilioUtils.getVerfiedCallerId(accountSid,authToken));
        return response;
    }
}
