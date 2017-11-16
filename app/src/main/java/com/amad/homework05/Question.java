package com.amad.homework05;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by pushparajparab on 9/11/17.
 */

public class Question implements Parcelable {


    private int id;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getChoices() {
        return choices;
    }

    public void setChoices(String choices) {
        this.choices = choices;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", question='" + question + '\'' +
                ", choices='" + choices + '\'' +
                ", response='" + response + '\'' +
                '}';
    }

    private String question,choices,response;

    public Question(){};
    protected Question(Parcel in) {
        id = in.readInt();
        question = in.readString();
        choices = in.readString();
        response = in.readString();
    }


    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    //Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(question);
        dest.writeString(choices);
        dest.writeString(response);
    }
}
