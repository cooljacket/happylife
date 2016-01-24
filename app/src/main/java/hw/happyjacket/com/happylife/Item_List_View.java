package hw.happyjacket.com.happylife;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jacket on 2016/1/23.
 */
public class Item_List_View {
    private Context m_context;
    private DatabaseHelper dbHelper;
    private List<Item> m_data;
    private ListView item_list;
    private View m_view;
    private ItemAdapter m_adapter;

    Item_List_View(Context context, View view, int resource_id, int year) {
        dbHelper = new DatabaseHelper(context, "Accounts.db", null, SETTINGS.CURRENT_DB_VERSION);
        dbHelper.getWritableDatabase();     // 确保已经创建好数据库文件
        m_context = context;
        m_view = view;
        m_data = new ArrayList<>();
        setData(year);
        item_list = (ListView) m_view.findViewById(resource_id);
        m_adapter = new ItemAdapter(context, R.layout.item_layout, m_data);
        item_list.setAdapter(m_adapter);
    }

    public int notifyDataSetChanged(int year) {
        setData(year);
        m_adapter.notifyDataSetChanged();
        return m_data.size();
    }

    public void setListener(AdapterView.OnItemClickListener a) {
        item_list.setOnItemClickListener(a);
    }

    public long getSelectedId(int position) {
        return m_data.get(position).getId();
    }

    private void setData(int year) {
        m_data.clear();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(String.format("select * from Accounts where time like '%d%%' order by id desc", year), null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            long id = cursor.getLong(cursor.getColumnIndex("id"));
            int kind = cursor.getInt(cursor.getColumnIndex("kind"));
            float price = cursor.getFloat(cursor.getColumnIndex("price"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String tag = cursor.getString(cursor.getColumnIndex("tag"));
            m_data.add(new Item(id, name, price, kind, time, tag));
            cursor.moveToNext();
        }
        cursor.close();
    }

    public float getTotalCost(int kind) {
        float ans = 0;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(String.format("select sum(price) from Accounts where kind=%d", kind), null);
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            ans = cursor.getFloat(cursor.getColumnIndex("sum(price)"));
        }
        cursor.close();
        return ans;
    }

    public void exportItems() {
        String fileName = m_context.getExternalFilesDir(null) + File.separator + "HappyLife账目表.txt";
        String text = "id\tkind\tprice\ttime\tname\ttag\t\n";

        FileWriter fw;
        try {
            fw = new FileWriter(fileName, false);
            fw.write(text, 0, text.length());

            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery(String.format("select * from Accounts"), null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                long id = cursor.getLong(cursor.getColumnIndex("id"));
                int kind = cursor.getInt(cursor.getColumnIndex("kind"));
                float price = cursor.getFloat(cursor.getColumnIndex("price"));
                String time = cursor.getString(cursor.getColumnIndex("time"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String tag = cursor.getString(cursor.getColumnIndex("tag"));

                text = String.format("%d\t%d\t%#.2f\t%s\t%s\t%s\n", id, kind, price, time, name, tag);
                fw.write(text, 0, text.length());

                cursor.moveToNext();
            }
            cursor.close();

            fw.close();
            Toast.makeText(m_context, "导出文件路径为：" + fileName, Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("ExternalStorage", "Error writing " + fileName);
            Toast.makeText(m_context, "Failed to open file " + fileName, Toast.LENGTH_SHORT).show();
        }

       /* SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(String.format("select * from Accounts"), null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            long id = cursor.getLong(cursor.getColumnIndex("id"));
            int kind = cursor.getInt(cursor.getColumnIndex("kind"));
            float price = cursor.getFloat(cursor.getColumnIndex("price"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String tag = cursor.getString(cursor.getColumnIndex("tag"));

            text = String.format("%d\t%d\t%#.2f\t%s\t%s\t%s\n", id, kind, price, time, name, tag);


            cursor.moveToNext();
        }
        cursor.close();*/

    }
}
