package com.example.ip_mobileapp.Model;

import android.os.Build;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Message
{
    private String Continut;
    private String Data;
    private String sender_id;

    public Message() {
    }

    public Message(String continut, String data, String sender_id) {
        Continut = continut;
        Data = data;
        this.sender_id = sender_id;
    }

    @Override
    public String toString() {
        return "Message{" +
                "Continut='" + Continut + '\'' +
                ", Data='" + Data + '\'' +
                ", sender_id='" + sender_id + '\'' +
                '}';
    }

    public String getContinut() {
        return Continut;
    }

    public void setContinut(String continut) {
        Continut = continut;
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }
}
