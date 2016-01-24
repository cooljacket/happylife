package hw.happyjacket.com.happylife;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

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
            Item item = new Item(id, name, price, kind, time, tag);
            cursor.moveToNext();
        }
        cursor.close();
    }
}
