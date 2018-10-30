package com.pranay.twilio.utils;


import com.pranay.twilio.constants.Constants;
import com.pranay.twilio.pojo.MakeCallJson;
import com.twilio.Twilio;
import com.twilio.base.ResourceSet;
import com.twilio.exception.ApiException;
import com.twilio.http.HttpMethod;
import com.twilio.jwt.accesstoken.AccessToken;
import com.twilio.jwt.accesstoken.VoiceGrant;
import com.twilio.rest.api.v2010.account.*;
import com.twilio.type.PhoneNumber;
import com.twilio.twiml.voice.Number;
import com.twilio.twiml.voice.Dial;
import com.twilio.twiml.VoiceResponse;
import com.twilio.twiml.TwiMLException;
import lombok.experimental.var;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created By Pranay on 3/6/2018
 */
public class TwilioUtils implements Constants {

    final static Logger logger = LoggerFactory.getLogger(TwilioUtils.class);

    public static Call makeCall(MakeCallJson req) throws URISyntaxException {
        Twilio.init(req.getAccountSid(), req.getAuthToken());
        List<String> callbackEvents = Arrays.asList("initiated", "ringing", "answered", "completed");
        String uri=StrUtil.nonNull(req.getUri());
        Call call = null;
        try {
            call = Call
                    .creator(new PhoneNumber(req.getToNumber()), new PhoneNumber(req.getFromNumber()),
                            new URI(uri)).setRecord(true)
                    .setMethod(HttpMethod.GET).setStatusCallback("https://example.com/twilio/webhook/")
                    .setStatusCallbackMethod(HttpMethod.GET).setStatusCallbackEvent(callbackEvents).create();

            System.out.println("\n\n\n\n\n\n"+call);
        } catch (ApiException e) {
            e.printStackTrace();
        }
       return call;
    }

    public static List<String> getAllPhoneNumbers(String accountSid,String authToken){
        Twilio.init(accountSid,authToken);
        ResourceSet<IncomingPhoneNumber> numbers;
        try {
            numbers = IncomingPhoneNumber.reader().read();
        } catch (ApiException e) {
            throw new ApiException("Authentication Failed",401,"Authentication Failed",401,new Throwable());
        }
        List<String> list=new ArrayList<>();
        // Loop over numbers and print out a property for each one.
        for (IncomingPhoneNumber number : numbers) {
            System.out.println("Number: "+number.getPhoneNumber());
            list.add(number.getPhoneNumber().toString());
        }
        return list;
    }

    public static Message sendSMS(MakeCallJson req) {
        Twilio.init(req.getAccountSid(), req.getAuthToken());
        Message message = Message.creator(new PhoneNumber(req.getToNumber()), new PhoneNumber(req.getFromNumber()),
                StrUtil.nonNull(req.getMessage()))
                .setStatusCallback("https://example.com/webhook/sms")
                .create();
        System.out.println("\n\n\nMessage: "+message);
        return message;
    }

    public static List<Application> getTwilioApplication(String accountSid,String authToken) {
        Twilio.init(accountSid, authToken);
        List<Application> applicationList = new ArrayList<>();
        ResourceSet<Application> apps = Application.reader().setFriendlyName("Alore CRM Calling App").read();
        if (apps.iterator().hasNext())
            applicationList.add(apps.iterator().next());
        return applicationList;
    }

    public static void updateTwilioApplication(String accountSid,String authToken,String appSid) {
        Twilio.init(accountSid, authToken);
        Application app = Application.updater(appSid)
                .setVoiceUrl("https://example.com/your-call-webhook-url")
                .setVoiceMethod(HttpMethod.POST)
                .setSmsUrl("https://example.com/your-sms-webhook-url")
                .setSmsMethod(HttpMethod.GET)
                .update();
        System.out.println("\napp: "+app);
    }

    public static void disconectCall(String accountSid,String authToken,String callSid) {
        Twilio.init(accountSid, authToken);
        // Get an object from its sid. If you do not have a sid,
        Call cl=getCallStatusByAPI(accountSid, authToken,callSid);
        if (cl!=null && cl.getParentCallSid()!=null)
            callSid=cl.getParentCallSid();
        Call call = Call.updater(callSid)
                .setStatus(Call.UpdateStatus.COMPLETED)
                .update();
        System.out.println(call.getDateUpdated());
    }

    public static String directVoice(Map<String, String> req) {
        Twilio.init("accountSid","authToken");
        Dial.Builder dialBuilder = new Dial.Builder()
                .callerId("+18564062949");

        // wrap the phone number or client name in the appropriate TwiML verb
        // by checking if the number given has only digits and format symbols

        dialBuilder = dialBuilder.number(new com.twilio.twiml.voice.Number.Builder("your-phone-number").build());
        System.out.println("fvkjdjfgb dialBuilder: "+dialBuilder);

        VoiceResponse voiceTwimlResponse = new VoiceResponse.Builder()
                .dial(dialBuilder.build())
                .build();
        return voiceTwimlResponse.toXml();
    }

    public static Call getCallStatusByAPI(String accountSid,String authToken,String callSid){
        Twilio.init(accountSid, authToken);
        return  Call.fetcher(callSid).fetch();
    }

    public static String getRecording(String accountSid,String authToken,String callSid){
        Twilio.init(accountSid, authToken);
        ResourceSet<Recording> recordings = Recording.reader()
                .setCallSid(callSid)
                .read();
        System.out.println("recordings List: "+recordings);
        // Loop over recordings and print out a property for each one.
        for (Recording recording : recordings) {
            System.out.println("asfdgh: "+recording);
            String record=recording.getUri();
            record=record.replace(".json",".mp3");
            record="https://api.twilio.com"+record;
            return record;
        }
        return "";
    }

    public static List<String> getVerfiedCallerId(String accountSid,String authToken){
        Twilio.init(accountSid, authToken);
        ResourceSet<OutgoingCallerId> callerIds = OutgoingCallerId.reader().read();
        List<String> list=new ArrayList<>();
        // Loop over callerIds and print out a property for each one.
        for (OutgoingCallerId callerId : callerIds) {
            System.out.println("call: "+callerId.getPhoneNumber());
            list.add(callerId.getPhoneNumber().toString());
        }
        return list;
    }

    public static void redirectLiveCallForVoiceMail(String accountSid,String authToken,String callSid,String email,String message){
        Twilio.init(accountSid, authToken);
        email=email.replace("@","%40");
        Call call;
        if ("".equalsIgnoreCase(StrUtil.nonNull(message)))
            message="Please%20leave%20a%20message%20after%20the%20beep";
        try {
            call = Call.updater(callSid)
                    .setUrl("http://twimlets.com/voicemail?Email="+email+"&Message="+message).setMethod(HttpMethod.GET).update();
        } catch (Exception e) {
            call = Call.updater(callSid)
                    .setUrl("http://twimlets.com/voicemail?Email="+email+"&Message="+message).setMethod(HttpMethod.POST).update();
        }

        System.out.println(call);
    }

    public static void redirectLiveCallForForword(String accountSid, String authToken, String callSid, String callactionvalue, String useremailid,String message) {
        logger.info("Inside call forward to : "+callactionvalue);
        logger.info("Inside call sid : "+callSid);
        Twilio.init(accountSid, authToken);
        useremailid=useremailid.replace("@","%40");
        if ("".equalsIgnoreCase(StrUtil.nonNull(message)))
            message="Please%20leave%20a%20message%20after%20the%20beep";
        Call call;
        try {
            call = Call.updater(callSid)
                    .setUrl("http://twimlets.com/forward?PhoneNumber="+callactionvalue+"&FailUrl=http://twimlets.com/voicemail?Email="+useremailid+"&Message="+message+"&Transcribe=true&").setMethod(HttpMethod.GET).update();
        } catch (Exception e) {
            call = Call.updater(callSid)
                    .setUrl("http://twimlets.com/forward?PhoneNumber="+callactionvalue+"&FailUrl=http://twimlets.com/voicemail?Email="+useremailid+"&Message="+message+"&Transcribe=true&").setMethod(HttpMethod.POST).update();
        }
        System.out.println("call: "+call);
    }

    public static void redirectLiveCallForTextMessage(String accountSid, String authToken, String callSid, String callactionvalue, String fromFormatted) {
        Twilio.init(accountSid, authToken);
        List<String> list=getAllPhoneNumbers(accountSid,authToken);
        //send message
        try {
            Message.creator(new PhoneNumber(callactionvalue), new PhoneNumber(list.get(0)),
                    StrUtil.nonNull("You missed call from "+fromFormatted))
                    .create();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //disconnect call
        try {
            Call.updater(callSid)
                    .setStatus(Call.UpdateStatus.COMPLETED)
                    .update();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static NewKey createTwilioApiKey(String accountSid,String authToken ){
        Twilio.init(accountSid, authToken);
        return NewKey.creator()
                .setFriendlyName("Alore Calling App")
                .create();
    }

    public static String generateNewJWT(String twilioAccountSid,String twilioApiSecret,String twilioApiKey,String outgoingApplicationSid ){
        // Create Voice grant
        VoiceGrant grant = new VoiceGrant();
        grant.setOutgoingApplicationSid(outgoingApplicationSid);

        // Create access token
        AccessToken token = new AccessToken.Builder(
                twilioAccountSid,
                twilioApiKey,
                twilioApiSecret
        ).identity(twilioAccountSid).grant(grant).build();

        System.out.println("Twilio JWT token "+token.toJwt());
        return token.toJwt();
    }

    public static void createCallWithRecordedVoice(String accountSid,String authToken,String fromNumber,String toNumber,String recordingUrl) throws Exception {
        Twilio.init(accountSid, authToken);
        recordingUrl = URLEncoder.encode(recordingUrl, "UTF-8");
        String url = "http://twimlets.com/message?Message%5B0%5D="+recordingUrl+"&";
        System.out.println("URL: "+url);
        Call call = Call
                .creator(new PhoneNumber(toNumber), new PhoneNumber(fromNumber),
                        new URI(url))
                .create();

        System.out.println("CAll : "+call.getStatus());
    }


    }
