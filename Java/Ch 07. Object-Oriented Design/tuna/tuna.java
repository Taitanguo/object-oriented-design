package tuna;

public enum tuna {
    //those enumeration are constance and also objects
    yilin("first", "20"),
    qianawng("stange", "22"),
    xiaorouhu("pretty", "21");

    private final String desc;
    private final String year;
    //constructor
    tuna(String description, String birthday) {
        desc = description;
        year = birthday;
    }

    public String getDesc() {
        return desc;
    }

    public String getYear() {
        return year;
    }
}
