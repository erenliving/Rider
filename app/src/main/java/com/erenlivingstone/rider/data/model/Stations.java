package com.erenlivingstone.rider.data.model;

import java.util.List;

/**
 * Created by Eren on 05/12/2016.
 *
 * Java POJO representing a collection of Stations.
 * Needed because BikeShareToronto API wraps its response in a JSON Object.
 */

public class Stations {
    public List<Station> stations;
}
