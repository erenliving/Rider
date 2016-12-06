package com.erenlivingstone.rider.data.model;

/**
 * Created by Eren on 04/12/2016.
 *
 * Java POJO representing an individual Station.
 * This is used to map the JSON keys to the object by GSON.
 */

public class Station {
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
}
