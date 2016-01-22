package hw.happyjacket.com.happylife;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private ItemAdapter adapter;
    private List<Item> m_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SetUpApp();

        TextView newOne = (TextView) findViewById(R.id.new_one);
        newOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("create_item");
                startActivityForResult(intent, SETTINGS.New_Item_RC);
            }
        });

        final ListView item_list = (ListView) findViewById(R.id.item_list);
        m_data = getData();
        adapter = new ItemAdapter(MainActivity.this, R.layout.item_layout, m_data);
        item_list.setAdapter(adapter);
        item_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, String.format("pos: %d", position), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent("item_detail");
                intent.putExtra("id", m_data.get(position).getId());
                startActivityForResult(intent, SETTINGS.Item_detail_RC);
            }
        });
    }

    List<Item> getData() {
        List<Item> list = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from Accounts order by id desc", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            long id = cursor.getLong(cursor.getColumnIndex("id"));
            int kind = cursor.getInt(cursor.getColumnIndex("kind"));
            float price = cursor.getFloat(cursor.getColumnIndex("price"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            list.add(new Item(id, name, price, kind, time));
            cursor.moveToNext();
        }
        cursor.close();

        return list;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case SETTINGS.New_Item_RC:
                if (resultCode == RESULT_OK) {
                    Item item = getDataFromSharePref(MainActivity.this);
                    long id = insertOneItem(dbHelper, item);
                    item.setId(id);
                    m_data.add(0, item); // 插入到最前面
                    adapter.notifyDataSetChanged();
                    Toast.makeText(MainActivity.this, "insert one", Toast.LENGTH_SHORT).show();
                }
                break;
            case SETTINGS.Item_detail_RC:
                if (resultCode == RESULT_OK) {

                }
                break;
            default:
        }
    }

    private void SetUpApp() {
        dbHelper = new DatabaseHelper(this, "Accounts.db", null, SETTINGS.CURRENT_DB_VERSION);
        dbHelper.getWritableDatabase();     // 确保已经创建好数据库文件

        Button manage = (Button) findViewById(R.id.right_btn);
        manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Button summary = (Button) findViewById(R.id.left_btn);
        summary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public static long insertOneItem(DatabaseHelper dbHelper, Item item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("kind", item.getKind());
        values.put("price", item.getPrice());
        values.put("time", item.getTime());
        values.put("name", item.getName());
        return db.insert("Accounts", null, values);
    }

    public static Item getDataFromSharePref(Context context) {
        SharedPreferences pref = context.getSharedPreferences("new_item", MODE_PRIVATE);

        String tag;
        int kind;
        if (pref.getBoolean("item_kind", false)) {
            tag = SETTINGS.tags_in[pref.getInt("item_tag", 0)];
            kind = 1;
        } else {
            tag = SETTINGS.tags_out[pref.getInt("item_tag", 0)];
            kind = 0;
        }
        String name = pref.getString("item_name", tag);

        float price = pref.getFloat("item_price", 0);
        Calendar c = Calendar.getInstance();
        String time = String.format("%d-%02d-%02d %02d:%02d:%02d",
                c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH),
                c.get(Calendar.HOUR_OF_DAY) + 1, c.get(Calendar.MINUTE), c.get(Calendar.SECOND));

        return new Item(-1, name, price, kind, time);
    }
}
