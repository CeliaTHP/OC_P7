package com.openclassrooms.oc_p7.services.utils;

public class OnDestinationChangedEvent {

    //Notifies when the focused fragment changes with event bus

    private String destinationDisplayName;

    public int getDestinationInt() {
        switch (destinationDisplayName) {
            case ("com.openclassrooms.oc_p7:id/navigation_map"):
                return 0;
            case ("com.openclassrooms.oc_p7:id/navigation_list"):
                return 1;
            default:
                return 2;
        }
    }

    public void setDestinationDisplayName(String destinationDisplayName) {
        this.destinationDisplayName = destinationDisplayName;
    }
}
