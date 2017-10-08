//package battleship;
//
//import java.lang.reflect.Array;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Random;
//import java.util.Scanner;
//
//import static java.util.Arrays.asList;
//
//public class BattleShipTest {
//
//    public static void main (String args[]) {
//        Scanner input = new Scanner(System.in);
//        Random rand = new Random();
//        //new battleship
//        battleship ship = new battleship();
//        int userGuess;
//
//        boolean isAlive = true;
//        int temp = rand.nextInt(5) + 1;
//        int[] loc = {temp, ++temp, ++temp};
//        ArrayList<Integer> locations = new ArrayList<Integer>(Arrays.asList(loc));
//        ship.setLocation(locations);
//
//        while (isAlive) {
//            String result;
//            System.out.println("Enter a guess");
//            userGuess = Integer.valueOf(input.nextLine());
//            result = ship.checkGuess(userGuess);
//            if (result.equals("Kill")) {
//                isAlive = false;
//            }
//        }
//
//    }
//}
