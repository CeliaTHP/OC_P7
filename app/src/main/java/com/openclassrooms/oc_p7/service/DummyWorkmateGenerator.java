package com.openclassrooms.oc_p7.service;

import com.openclassrooms.oc_p7.model.User;

import java.util.ArrayList;

public class DummyWorkmateGenerator {


    private final static ArrayList<User> DUMMY_WORKMATES = new ArrayList<User>(5) {
        {
            add(new User("Maeve", "Pink", "pinkMaeve@dummy.com", "PinkFood"));
            add(new User("Ash", "Red", "redAsh@dummy.com", "RedFood"));
            add(new User("Pip", "Yellow", "yellowPip@dummy.com", "YellowFood"));
            add(new User("Seris", "Purple", "purpleSeris@dummy.com", "PurpleFood"));
            add(new User("Cassie", "Orange", "orangeCassie@dummy.com", "OrangeFood"));

        }
    };

    public static ArrayList<User> generateWorkmates() {
        return DUMMY_WORKMATES;
    }

}
