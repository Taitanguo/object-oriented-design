package battleship;

import java.util.ArrayList;

public class battleship {
    private ArrayList<Integer> location;

    public void setLocation(ArrayList<Integer> loc) {
        location = loc;
    }

    public String checkGuess(Integer userGuess) {
        String result = "Miss";

        int index = location.indexOf(userGuess);
        //if we hit one ship
        if(index >= 0) {
            location.remove(index);
            result = location.isEmpty() ? "Kill" : "hit";
        }

        System.out.println(result);

        return result;
    }
}
