package hw.happyjacket.com.happylife;

/**
 * Created by jacket on 2016/1/22.
 */
public class SETTINGS {
    public static final  String [] tags_out = {"我是吃货", "交通", "买买买", "游戏娱乐", "医教", "生活用品", "投资", "其它"};
    public static final String[] tags_in = {"工资奖金", "红包", "投资回报", "惊喜/福利", "其它"};
    public static final int MAX_LEN_LINE = 12, New_Item_RC = 1, Item_detail_RC = 2, CURRENT_DB_VERSION = 1;

    public static int getTagIndex(int kind, String name) {
        String tag[] = tags_out;
        if (kind == 1)
            tag = tags_in;
        for (int i = 0; i < tag.length; ++i)
            if (tag[i] == name)
                return i;
        return 0;
    }
}
