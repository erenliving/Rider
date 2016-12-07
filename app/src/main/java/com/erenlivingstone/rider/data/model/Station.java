package com.erenlivingstone.rider.data.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Eren on 04/12/2016.
 *
 * Java POJO representing an individual Station.
 * This is used to map the JSON keys to the object by GSON.
 */

public class Station implements Parcelable {
    int id;
    String name;
    String terminalName;
    String lastCommWithServer; // timestamp
    int lat;
    int lng;
    boolean installed;
    boolean locked;
    String installDate; // timestamp
    String removalDate; // timestamp
    boolean temporary;
    boolean isPublic;
    int nbBikes;
    int nbEmptyDocks;
    String latestUpdateTime; // timestamp


    private Station(Parcel in) {
        id = in.readInt();
        name = in.readString();
        terminalName = in.readString();
        lastCommWithServer = in.readString();
        lat = in.readInt();
        lng = in.readInt();
        installed = in.readByte() != 0;
        locked = in.readByte() != 0;
        installDate = in.readString();
        removalDate = in.readString();
        temporary = in.readByte() != 0;
        isPublic = in.readByte() != 0;
        nbBikes = in.readInt();
        nbEmptyDocks = in.readInt();
        latestUpdateTime = in.readString();
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
        parcel.writeString(name);
        parcel.writeString(terminalName);
        parcel.writeString(lastCommWithServer);
        parcel.writeInt(lat);
        parcel.writeInt(lng);
        parcel.writeByte((byte) (installed ? 1 : 0));
        parcel.writeByte((byte) (locked ? 1 : 0));
        parcel.writeString(installDate);
        parcel.writeString(removalDate);
        parcel.writeByte((byte) (temporary ? 1 : 0));
        parcel.writeByte((byte) (isPublic ? 1 : 0));
        parcel.writeInt(nbBikes);
        parcel.writeInt(nbEmptyDocks);
        parcel.writeString(latestUpdateTime);
    }
}
