package com.openclassrooms.oc_p7.services.utils;

public class OnWorkmateQueryEvent {

    //Notifies when a query happens from workmate list with event bus

    private String queryForWorkmate = null;

    public String getQueryForWorkmate() {
        return queryForWorkmate;
    }

    public void setQueryForWorkmate(String queryForWorkmate) {
        this.queryForWorkmate = queryForWorkmate;
    }


}
