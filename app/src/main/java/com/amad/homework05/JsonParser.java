package com.amad.homework05;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by pushparajparab on 8/27/17.
 */

public class JsonParser {

    static class JsonParse{

        static User Parse(String s) throws JSONException {
            User toReturn = new User();
            JSONObject jsonObject =new JSONObject(s);

            Log.d("obj",jsonObject.toString());
            toReturn.setStatus(jsonObject.getString("status"));
            if(toReturn.getStatus().toLowerCase().equals("ok") ) {

                toReturn.setfName(jsonObject.getString("userFname"));
                toReturn.setlName(jsonObject.getString("userLname"));
                if(jsonObject.has("token")) {
                    toReturn.setToken(jsonObject.getString("token"));
                }
                toReturn.setId(Integer.parseInt(jsonObject.getString("userId")));
                toReturn.setEmail(jsonObject.getString("userEmail"));

            }else
            {
                toReturn.setErrorMessage(jsonObject.getString("message"));
            }
            return  toReturn;
        }
    }


    static class MyMessages{

        static ArrayList<Message> Parse(String s) throws JSONException {
            ArrayList<Message> toReturn = new ArrayList<Message>();
            //User toReturn = new User();
            JSONObject jsonObject =new JSONObject(s);
            JSONArray products = jsonObject.getJSONArray("results");
            for(int i =0 ;i<products.length();i++){
                Message toAdd = new Message();
                JSONObject eachObj = products.getJSONObject(i);
                toAdd.setResponseId(eachObj.getInt("ResponseId"));
                toAdd.setQuestionId(eachObj.getInt("questionID"));
                toAdd.setText(eachObj.getString("Text"));
                toAdd.setChoises(eachObj.getString("choises"));
                if(eachObj.getString("Response")!=null)
                toAdd.setResponse(eachObj.getString("Response"));
                toReturn.add(toAdd);
            }


            return  toReturn;

        }
    }
}
