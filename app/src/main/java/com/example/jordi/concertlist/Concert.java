package com.example.jordi.concertlist;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.IgnoreExtraProperties;

import java.util.Date;

@IgnoreExtraProperties
public class Concert implements Parcelable {


    private String grup;
    private String lloc;
    private String poblacio;
    private Double preu;
    private Date data;
    private String concert_id;

    public Concert (String grup, String lloc, String poblacio, Double preu, Date data){
        this.data = data;
        this.grup = grup;
        this.lloc = lloc;
        this.poblacio = poblacio;
        this.preu = preu;

    }

    public Concert(){}

    protected Concert(Parcel in){
        grup = in.readString();
        lloc = in.readString();
        poblacio = in.readString();
        preu = in.readDouble();
       //DATA ???
    }

    public static final Creator<Concert> CREATOR = new Creator<Concert>() {
        @Override
        public Concert createFromParcel(Parcel in) {
            return new Concert(in);
        }

        @Override
        public Concert[] newArray(int size) {
            return new Concert[size];
        }
    };

    public String getGrup() {
        return grup;
    }

    public void setGrup(String grup) {
        this.grup = grup;
    }

    public String getLloc() {
        return lloc;
    }

    public void setLloc(String lloc) {
        this.lloc = lloc;
    }

    public String getPoblacio() {
        return poblacio;
    }

    public void setPoblacio(String poblacio) {
        this.poblacio = poblacio;
    }

    public Double getPreu() {
        return preu;
    }

    public void setPreu(Double preu) {
        this.preu = preu;
    }

    public Date getData() {
        return data;
    }

    public String getConcert_id() {
        return concert_id;
    }

    public void setConcert_id(String concert_id) {
        this.concert_id = concert_id;
    }

    public void setData(Date data) {
        this.data = data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(grup);
        parcel.writeString(poblacio);
        parcel.writeString(lloc);
        parcel.writeDouble(preu);
    }
}
