package com.amad.homework05;

/**
 * Created by pushparajparab on 8/28/17.
 */

public class Utils {
    private static final String BASE_URL = "http://ec2-18-216-112-134.us-east-2.compute.amazonaws.com";

    public enum Api_url {


        LOGIN(BASE_URL + "/api/SurveyAppLogin"),
        MY_MESSAGES(BASE_URL + "/api/getMyMessages"),
        SUBMIT_RESPONSE(BASE_URL + "/api/submitResponse"),
        SUBMIT_TOKEN(BASE_URL + "/api/deviceRegister"),
        LOG_OUT(BASE_URL + "/api/logout"),
        GET_SUBSCRIPTION(BASE_URL + "/api/getSubscrition"),
        SUBSCRIBE(BASE_URL + "/api/subscribe")
        ;

        private final String text;
        private Api_url(final String text) {
            this.text = text;
        }
        @Override
        public String toString() {
            return text;
        }
    }
}
