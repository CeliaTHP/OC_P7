package com.openclassrooms.oc_p7.service;

import com.openclassrooms.oc_p7.model.Workmate;

import java.util.ArrayList;
import java.util.Random;

public class DummyWorkmateGenerator {


    private final static ArrayList<Workmate> DUMMY_WORKMATES = new ArrayList<Workmate>(10) {
        {
            /*https://i.pravatar.cc/150?u=a042581f4e29026704d

             */
            add(new Workmate("Maeve", "pinkMaeve@dummy.com", getRandomPic()));
            add(new Workmate("Ash", "redAsh@dummy.com", getRandomPic()));
            add(new Workmate("Pip", "yellowPip@dummy.com", getRandomPic()));
            add(new Workmate("Seris", "purpleSeris@dummy.com", getRandomPic()));
            add(new Workmate("Cassie", "orangeCassie@dummy.com", getRandomPic()));
            add(new Workmate("Atlas", "blueAtlas@dummy.com", getRandomPic()));
            add(new Workmate("Koga", "greyKoga@dummy.com", getRandomPic()));
            add(new Workmate("Buck", "brownBuck@dummy.com", getRandomPic()));
            add(new Workmate("Io", "whiteIo@dummy.com", getRandomPic()));
            add(new Workmate("Zhin", "blackZhin@dummy.com", getRandomPic()));

        }
    };

    public static ArrayList<Workmate> generateWorkmates() {
        return DUMMY_WORKMATES;
    }


    private static String getRandomPic() {
        return "https://i.pravatar.cc/150?img=" + new Random().nextInt((70) + 1);
    }

}
