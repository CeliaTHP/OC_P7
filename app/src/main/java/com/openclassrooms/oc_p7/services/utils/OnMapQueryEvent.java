package com.openclassrooms.oc_p7.services.utils;

public class OnMapQueryEvent {

    //Notifies when a query happens from map with event bus

    private String queryForMap = null;

    public String getQueryForMap() {
        return queryForMap;
    }

    public void setQueryForMap(String queryForMap) {
        this.queryForMap = queryForMap;
    }


}
