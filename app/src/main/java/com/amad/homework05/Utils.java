package com.amad.homework05;

/**
 * Created by pushparajparab on 8/28/17.
 */

public class Utils {
    private static final String BASE_URL = "http://ec2-18-216-112-134.us-east-2.compute.amazonaws.com";

    public enum Api_url {


        LOGIN(BASE_URL + "/api/SurveyAppLogin"),
        MY_MESSAGES(BASE_URL + "/api/getMyMessages"),
        SIGN_UP("http://ec2-13-59-39-123.us-east-2.compute.amazonaws.com/api/signup"),
        UPDATE_INFO("http://ec2-13-59-39-123.us-east-2.compute.amazonaws.com/api/update/myInfo");

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
