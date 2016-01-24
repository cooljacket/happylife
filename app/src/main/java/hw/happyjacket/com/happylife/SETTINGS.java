package hw.happyjacket.com.happylife;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

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

    public static String aboutMe =
            "作者：陈榕涛\n"
            + "完成日期：2016-01-24\n\n"
            + "功能简介：\n这个app是用来记账的，有基础的统计功能，可以新建、修改、删除账目，支持导出数据到本地，支持余额查看。\n\n"
            + "使用说明简介：\n"
            + "1）新建：右下角的新建按钮；\n"
            + "2）修改：点击列表中相应的行即可，删除也是点击这里进入的；\n"
            + "3）统计：右上角的统计按钮。\n\n"
            + "后续版本：可能会推出网页版，支持同步到服务器，不怕数据丢失；可能会支持自定义标签。";

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

    public static long insertOneItem(DatabaseHelper dbHelper, Item item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("kind", item.getKind());
        values.put("price", item.getPrice());
        values.put("time", item.getTime());
        values.put("name", item.getName());
        values.put("tag", item.getTag());
        return db.insert("Accounts", null, values);
    }
}
