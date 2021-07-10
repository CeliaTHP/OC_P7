package com.openclassrooms.oc_p7.services.dummies;

import com.openclassrooms.oc_p7.models.Workmate;

import java.util.ArrayList;
import java.util.Random;

public class DummyWorkmateGenerator {


    private final static ArrayList<Workmate> DUMMY_WORKMATES = new ArrayList<Workmate>(10) {
        {
            /*https://i.pravatar.cc/150?u=a042581f4e29026704d

             */
            add(new Workmate("1", "Maeve", "pinkMaeve@dummy.com", "https://i.pravatar.cc/150?img=1"));
            add(new Workmate("2", "Ash", "redAsh@dummy.com", "https://i.pravatar.cc/150?img=2"));
            add(new Workmate("3", "Pip", "yellowPip@dummy.com", "https://i.pravatar.cc/150?img=3"));
            add(new Workmate("4", "Seris", "purpleSeris@dummy.com", "https://i.pravatar.cc/150?img=4"));
            add(new Workmate("5", "Cassie", "orangeCassie@dummy.com", "https://i.pravatar.cc/150?img=5"));
            add(new Workmate("6", "Atlas", "blueAtlas@dummy.com", "https://i.pravatar.cc/150?img=6"));
            add(new Workmate("7", "Koga", "greyKoga@dummy.com", "https://i.pravatar.cc/150?img=7"));
            add(new Workmate("8", "Buck", "brownBuck@dummy.com", "https://i.pravatar.cc/150?img=8"));
            add(new Workmate("9", "Io", "whiteIo@dummy.com", "https://i.pravatar.cc/150?img=9"));
            add(new Workmate("10", "Zhin", "blackZhin@dummy.com", "https://i.pravatar.cc/150?img=10"));

        }
    };

    public static ArrayList<Workmate> generateWorkmates() {

        DUMMY_WORKMATES.get(1).setRestaurantId("ChIJZRd-EQRA5kcRnyzZQ1QWzVw");
        DUMMY_WORKMATES.get(1).setRestaurantName("KFC");

        DUMMY_WORKMATES.get(5).setRestaurantId("ChIJZRd-EQRA5kcRnyzZQ1QWzVw");
        DUMMY_WORKMATES.get(5).setRestaurantName("KFC");


        DUMMY_WORKMATES.get(3).setRestaurantId("ChIJ0TTGVAFA5kcRb6VXirND0hg");
        DUMMY_WORKMATES.get(3).setRestaurantName("McDonalds");


        return DUMMY_WORKMATES;
    }


    private static String getRandomPic() {
        return "https://i.pravatar.cc/150?img=" + new Random().nextInt((70) + 1);
    }

}
