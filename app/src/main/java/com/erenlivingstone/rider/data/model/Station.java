package com.erenlivingstone.rider.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Eren on 04/12/2016.
 *
 * Java POJO representing an individual Station.
 * This is used to map the JSON keys to the object by GSON.
 */

public class Station implements Parcelable {

    int id;
    String stationName;
    int availableDocks;
    int totalDocks;
    double latitude;
    double longitude;
    String statusValue;
    int statusKey;
    String status;
    int availableBikes;
    String stAddress1;
    String stAddress2;
    String city;
    String postalCode;
    String location;
    String altitude;
    boolean testStation;
    String lastCommunicationTime;
    String landmark;
    boolean is_renting;

    private Station(Parcel in) {
        id = in.readInt();
        stationName = in.readString();
        availableDocks = in.readInt();
        totalDocks = in.readInt();
        latitude = in.readDouble();
        longitude = in.readDouble();
        statusValue = in.readString();
        statusKey = in.readInt();
        status = in.readString();
        availableBikes = in.readInt();
        stAddress1 = in.readString();
        stAddress2 = in.readString();
        city = in.readString();
        postalCode = in.readString();
        location = in.readString();
        altitude = in.readString();
        testStation = in.readByte() != 0;
        lastCommunicationTime = in.readString();
        landmark = in.readString();
        is_renting = in.readByte() != 0;
    }

    public static final Creator<Station> CREATOR = new Creator<Station>() {
        @Override
        public Station createFromParcel(Parcel in) {
            return new Station(in);
        }

        @Override
        public Station[] newArray(int size) {
            return new Station[size];
        }
    };

    @Override
    public int describeContents() {
        return this.hashCode();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(stationName);
        parcel.writeInt(availableDocks);
        parcel.writeInt(totalDocks);
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
        parcel.writeString(statusValue);
        parcel.writeInt(statusKey);
        parcel.writeString(status);
        parcel.writeInt(availableBikes);
        parcel.writeString(stAddress1);
        parcel.writeString(stAddress2);
        parcel.writeString(city);
        parcel.writeString(postalCode);
        parcel.writeString(location);
        parcel.writeString(altitude);
        parcel.writeByte((byte) (testStation ? 1 : 0));
        parcel.writeString(lastCommunicationTime);
        parcel.writeString(landmark);
        parcel.writeByte((byte) (is_renting ? 1 : 0));
    }

    public LatLng getLocation() {
        return new LatLng(latitude, longitude);
    }

    public int getAvailableBikes() {
        return availableBikes;
    }

    public String getLastCommunicationTime() {
        return lastCommunicationTime;
    }
}
