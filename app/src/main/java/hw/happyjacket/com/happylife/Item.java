package hw.happyjacket.com.happylife;

/**
 * Created by jacket on 2016/1/20.
 */
public class Item {
    private long id;
    private float price;
    private String name, time;
    private int kind;

    Item(long id, String name, float price, int kind, String time) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.kind = kind;
        this.time = time;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getDate() {
        if (time.length() >= 10)
            return time.substring(0, 10);
        return time;
    }

    public String getPrice() {
        return "" + price;
    }

    public String getShowName() {
        String n = name;
        int ori_len = name.length() * 2;
        if (ori_len > SETTINGS.MAX_LEN_LINE)
            n = name.substring(0, 4) + "... ";
        while (ori_len < SETTINGS.MAX_LEN_LINE) {
            n += " ";
            ori_len += 1;
        }
        return  n;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

    public int getKind() {
        return kind;
    }
}
