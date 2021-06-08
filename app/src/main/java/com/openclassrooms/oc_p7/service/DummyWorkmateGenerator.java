package com.openclassrooms.oc_p7.service;

import com.openclassrooms.oc_p7.model.Workmate;

import java.util.ArrayList;
import java.util.Random;

public class DummyWorkmateGenerator {


    private final static ArrayList<Workmate> DUMMY_WORKMATES = new ArrayList<Workmate>(10) {
        {
            /*https://i.pravatar.cc/150?u=a042581f4e29026704d

             */
            add(new Workmate("Maeve", "pinkMaeve@dummy.com", "https://i.pravatar.cc/150?img=1"));
            add(new Workmate("Ash", "redAsh@dummy.com", "https://i.pravatar.cc/150?img=2"));
            add(new Workmate("Pip", "yellowPip@dummy.com", "https://i.pravatar.cc/150?img=3"));
            add(new Workmate("Seris", "purpleSeris@dummy.com", "https://i.pravatar.cc/150?img=4"));
            add(new Workmate("Cassie", "orangeCassie@dummy.com", "https://i.pravatar.cc/150?img=5"));
            add(new Workmate("Atlas", "blueAtlas@dummy.com", "https://i.pravatar.cc/150?img=6"));
            add(new Workmate("Koga", "greyKoga@dummy.com", "https://i.pravatar.cc/150?img=7"));
            add(new Workmate("Buck", "brownBuck@dummy.com", "https://i.pravatar.cc/150?img=8"));
            add(new Workmate("Io", "whiteIo@dummy.com", "https://i.pravatar.cc/150?img=9"));
            add(new Workmate("Zhin", "blackZhin@dummy.com", "https://i.pravatar.cc/150?img=10"));

        }
    };

    public static ArrayList<Workmate> generateWorkmates() {
        return DUMMY_WORKMATES;
    }


    private static String getRandomPic() {
        return "https://i.pravatar.cc/150?img=" + new Random().nextInt((70) + 1);
    }

}
