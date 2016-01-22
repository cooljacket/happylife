package hw.happyjacket.com.happylife;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class item_detail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        setTitleBar();

        Item item = getTheItem();
        if (item.getId() != -1) {
            setTheView(item);
        } else {
            Toast.makeText(item_detail.this, "加载出错，请返回主界面重试", Toast.LENGTH_SHORT).show();
        }
    }

    private void setTitleBar() {
        TextView title = (TextView) findViewById(R.id.green_title_text);
        Button back = (Button) findViewById(R.id.left_btn);
        Button ok = (Button) findViewById(R.id.right_btn);
        title.setText("编辑记账项目");
        back.setText("返回");
        ok.setText("删除");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(item_detail.this, MainActivity.class);
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(item_detail.this, MainActivity.class);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private void setTheView(Item item) {
        EditText edit = (EditText) findViewById(R.id.item_name);
        edit.setText(item.getName());
        edit = (EditText) findViewById(R.id.item_price);
        edit.setText(item.getPrice());

        Switch aSwitch = (Switch) findViewById(R.id.item_kind_switch);
        if (item.getKind() == 1)
            aSwitch.setChecked(true);
        else
            aSwitch.setChecked(false);

        Spinner spinner = (Spinner) findViewById(R.id.item_tag_spinner);
        spinner.setSelection(SETTINGS.getTagIndex(item.getKind(), item.getName()));
    }

    private Item getTheItem() {
        Intent intent = getIntent();
        long id = intent.getLongExtra("id", -1);
        if (id != -1) {
            DatabaseHelper dbHelper = new DatabaseHelper(this, "Accounts.db", null, SETTINGS.CURRENT_DB_VERSION);
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery(String.format("select * from Accounts where id = %d", id), null);
            cursor.moveToFirst();
            if (!cursor.isAfterLast()) {
                int kind = cursor.getInt(cursor.getColumnIndex("kind"));
                float price = cursor.getFloat(cursor.getColumnIndex("price"));
                String time = cursor.getString(cursor.getColumnIndex("time"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                cursor.close();
                return new Item(id, name, price, kind, time);
            }
            cursor.close();
        }
        return new Item(-1, "", 0, 0, "");
    }
}
