package hw.happyjacket.com.happylife;

import java.util.Calendar;

/**
 * Created by jacket on 2016/1/22.
 */
public class SETTINGS {
    public static final  String [] tags_out = {"我是吃货", "交通", "买买买", "游戏娱乐", "医教", "生活用品", "投资", "其它"};
    public static final String[] tags_in = {"工资奖金", "红包", "投资回报", "惊喜/福利", "其它"};
    public static final int MAX_LEN_LINE = 10, New_Item_RC = 1, Item_detail_RC = 2, CURRENT_DB_VERSION = 1;
    public static String Item_db_file_name = "Accounts.db", Item_db_table_name = "Accounts";
    public static int Lowest_year = 2012;

    public static int getTagIndex(int kind, String name) {
        String tag[] = tags_out;
        if (kind == 1)
            tag = tags_in;
        for (int i = 0; i < tag.length; ++i) {
            if (tag[i].equals(name))
                return i;
        }
        return 0;
    }

    public static int getCurrentYear() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.YEAR);
    }

   /* public static ArrayList<String> getAllTags() {
        ArrayList<String> ans = new ArrayList<>();
        for (int i = 0; i < tags_out.length; ++i)
            ans.add(tags_out[i]);

        for (int i = 0; i < tags_in.length; ++i)
            ans.add(tags_in[i]);

        return ans;
    }*/
}
