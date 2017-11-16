package com.amad.homework05;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by pushparajparab on 11/15/17.
 */

public class Message implements Parcelable {
    int responseId, questionId;
    String text, choises, Response;

    public Message(){}
    protected Message(Parcel in) {
        responseId = in.readInt();
        questionId = in.readInt();
        text = in.readString();
        choises = in.readString();
        Response = in.readString();
    }

    @Override
    public String toString() {
        return "Message{" +
                "responseId=" + responseId +
                ", questionId=" + questionId +
                ", text='" + text + '\'' +
                ", choises='" + choises + '\'' +
                ", Response='" + Response + '\'' +
                '}';
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    public int getResponseId() {
        return responseId;
    }

    public void setResponseId(int responseId) {
        this.responseId = responseId;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getChoises() {
        return choises;
    }

    public void setChoises(String choises) {
        this.choises = choises;
    }

    public String getResponse() {
        return Response;
    }

    public void setResponse(String response) {
        Response = response;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(responseId);
        parcel.writeInt(questionId);
        parcel.writeString(text);
        parcel.writeString(choises);
        parcel.writeString(Response);
    }
}
