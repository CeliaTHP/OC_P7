package com.openclassrooms.oc_p7.service;

import com.openclassrooms.oc_p7.model.User;

import java.util.ArrayList;
import java.util.Random;

public class DummyWorkmateGenerator {


    private final static ArrayList<User> DUMMY_WORKMATES = new ArrayList<User>(10) {
        {
            /*https://i.pravatar.cc/150?u=a042581f4e29026704d

             */
            add(new User("Maeve", "Pink", "pinkMaeve@dummy.com", "PinkFood", getRandomPic()));
            add(new User("Ash", "Red", "redAsh@dummy.com", "RedFood", getRandomPic()));
            add(new User("Pip", "Yellow", "yellowPip@dummy.com", "YellowFood", getRandomPic()));
            add(new User("Seris", "Purple", "purpleSeris@dummy.com", "PurpleFood", getRandomPic()));
            add(new User("Cassie", "Orange", "orangeCassie@dummy.com", "OrangeFood", getRandomPic()));
            add(new User("Atlas", "Blue", "blueAtlas@dummy.com", "BlueFood", getRandomPic()));
            add(new User("Koga", "Grey", "greyKoga@dummy.com", "GreyFood", getRandomPic()));
            add(new User("Buck", "Brown", "brownBuck@dummy.com", "BrownFood", getRandomPic()));
            add(new User("Io", "White", "whiteIok@dummy.com", "WhiteFood", getRandomPic()));
            add(new User("Zhin", "Black", "blackZhink@dummy.com", "BlackFood", getRandomPic()));

        }
    };

    public static ArrayList<User> generateWorkmates() {
        return DUMMY_WORKMATES;
    }

    public static String getRandomPic() {
       return "https://i.pravatar.cc/150?img=" + new Random().nextInt((70) + 1);
    }

}
