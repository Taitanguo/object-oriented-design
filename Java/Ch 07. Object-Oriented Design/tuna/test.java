package tuna;

public class test {

    public static void main(String[] args) {
        //values is a static method loop through constant enumerate
        System.out.print("love");
        for(tuna people : tuna.values()) {
            System.out.printf("%s\t%s\t%s\n", people, people.getDesc(), people.getYear());
        }
    }
}
