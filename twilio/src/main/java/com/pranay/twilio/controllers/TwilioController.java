package com.pranay.twilio.controllers;

import com.pranay.twilio.pojo.MakeCallJson;
import com.pranay.twilio.pojo.UniversalResponse;
import com.pranay.twilio.services.TwilioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created By Pranay on 10/30/2018
 */

@RestController
@RequestMapping("/twilio")
public class TwilioController {
    private final static Logger logger = LoggerFactory.getLogger(TwilioController.class);

    @Autowired
    private TwilioService twilioService;

    @RequestMapping(value = "/phone-numbers/{accountSid}/{authToken}", method = RequestMethod.GET)
    public ResponseEntity<?> getAllTwilioPhoneNumber(@PathVariable("accountSid") String accountSid,
                                                     @PathVariable("authToken") String authToken) throws Exception {
        logger.info("Get all twilio numbers request: "+accountSid);
        UniversalResponse response = twilioService.getAllTwilioPhoneNumber(accountSid,authToken);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/callerid/{accountSid}/{authToken}", method = RequestMethod.GET)
    public ResponseEntity<?> getTwilioVerifiedCallerId(@PathVariable("accountSid") String accountSid,
                                                     @PathVariable("authToken") String authToken) throws Exception {
        logger.info("Get all twilio numbers request: "+accountSid);
        UniversalResponse response = twilioService.getTwilioVerifiedCallerId(accountSid,authToken);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/application/{accountSid}/{authToken}", method = RequestMethod.GET)
    public ResponseEntity<?> getAllTwilioCallingApplication(@PathVariable("accountSid") String accountSid,
                                                            @PathVariable("authToken") String authToken) throws Exception {
        logger.info("Get all twilio calling application request: "+accountSid);
        UniversalResponse response = twilioService.getAllTwilioCallingApplication(accountSid,authToken);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/application", method = RequestMethod.PUT)
    public ResponseEntity<?> updateApplication(@RequestBody MakeCallJson req) throws Exception {
        logger.info("Twilio update application request request: "+req);
        twilioService.updateApplication(req);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @RequestMapping(value = "/call", method = RequestMethod.POST)
    public ResponseEntity<?> makeCall(@RequestBody MakeCallJson req) throws Exception {
        logger.info("Twilio make Call request: "+req);
        twilioService.makeCall(req);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @RequestMapping(value = "/call/status/{accountSid}/{authToken}/{callSid}", method = RequestMethod.GET)
    public ResponseEntity<?> getCallStatus(@PathVariable("accountSid") String accountSid,
                                           @PathVariable("authToken") String authToken,
                                           @PathVariable("callSid") String callSid) throws Exception {
        logger.info("Twilio get call status request: ");
        UniversalResponse response = twilioService.getCallStatus(accountSid,authToken,callSid);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/call/disconnect", method = RequestMethod.POST)
    public ResponseEntity<?> disconnectCall(@RequestBody MakeCallJson req) throws Exception {
        logger.info("Twilio disconnect make Call request: "+req);
        twilioService.disconnectCall(req);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @RequestMapping(value = "/sms", method = RequestMethod.POST)
    public ResponseEntity<?> sendSMS(@RequestBody MakeCallJson req) throws Exception {
        logger.info("Twilio send sms request: "+req);
        twilioService.sendSMS(req);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }



}
