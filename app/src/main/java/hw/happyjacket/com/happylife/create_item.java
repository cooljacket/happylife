package hw.happyjacket.com.happylife;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Calendar;

public class Create_item extends AppCompatActivity {
    protected Spinner item_tag_spinner;
    protected Switch item_kind_switch;
    protected EditText item_name_text, item_price_text;
    protected int item_kind = 0, selected_tag = 0;

    protected String [] tags;
    protected ArrayAdapter<String> adapter;
    protected DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_item);
        setTheView(false);

        dbHelper = new DatabaseHelper(Create_item.this, SETTINGS.Item_db_file_name, null, SETTINGS.CURRENT_DB_VERSION);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, SETTINGS.tags_out);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        item_tag_spinner = (Spinner) findViewById(R.id.item_tag_spinner);
        item_tag_spinner.setAdapter(adapter);
        item_tag_spinner.setVisibility(View.VISIBLE);
        item_tag_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_tag = position;

                if (item_name_text.getText().toString().length() == 0 && tags != null) {
                    item_name_text.setText(tags[position]);
                } else if (tags == null) {
                    tags = SETTINGS.tags_out;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        item_kind_switch = (Switch) findViewById(R.id.item_kind_switch);
        item_kind_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tags = SETTINGS.tags_in;
                    item_kind = 1;
                } else {
                    tags = SETTINGS.tags_out;
                    item_kind = 0;
                }
                adapter = new ArrayAdapter<>(Create_item.this, android.R.layout.simple_spinner_item, tags);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                item_tag_spinner.setAdapter(adapter);
            }
        });
    }

    protected void register(int id) {

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

    protected void setTheView(boolean isChild) {
        item_name_text = (EditText) findViewById(R.id.item_name);
        item_price_text = (EditText) findViewById(R.id.item_price);

        if (!isChild) {
            TextView title = (TextView) findViewById(R.id.green_title_text);
            Button cancel = (Button) findViewById(R.id.left_btn);
            Button ok = (Button) findViewById(R.id.right_btn);
            title.setText("新建记账项目");
            cancel.setText("取消");
            ok.setText("确定");

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Create_item.this, MainActivity.class);
                    setResult(RESULT_CANCELED, intent);
                    finish();
                }
            });

            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Item item = getTheInputItem();
                    long id = insertOneItem(dbHelper, item);
                    item.setId(id);

                    // back to the main activity
                    Intent intent = new Intent(Create_item.this, MainActivity.class);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
        }
    }

    protected Item getTheInputItem() {
        String item_name = item_name_text.getText().toString();

        String tag;
        if (item_kind == 0)
            tag = SETTINGS.tags_out[selected_tag];
        else
            tag = SETTINGS.tags_in[selected_tag];

        float price = 0;
        if (item_price_text.getText().length() > 0)
            price = Float.valueOf(item_price_text.getText().toString());

        Calendar c = Calendar.getInstance();
        String time = String.format("%d-%02d-%02d %02d:%02d:%02d",
                c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH),
                c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), c.get(Calendar.SECOND));

        // look care that the id is unsure now(before inserting into the db)
        // you should correct id value after inserting it into the db
        return new Item(-1, item_name, price, item_kind, time, tag);
    }
}