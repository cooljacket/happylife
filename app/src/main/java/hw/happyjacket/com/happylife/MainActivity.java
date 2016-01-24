package hw.happyjacket.com.happylife;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private Item_List_View item_list;
    private int current_year;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SetUpApp();

        TextView newOne = (TextView) findViewById(R.id.new_one);
        newOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("Create_item");
                startActivityForResult(intent, SETTINGS.New_Item_RC);
            }
        });

        item_list = new Item_List_View(this, getWindow().getDecorView(), R.id.item_list, current_year);
        item_list.setListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, String.format("pos: %d", position), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent("Update_item");
                intent.putExtra("id", item_list.getSelectedId(position));
                startActivityForResult(intent, SETTINGS.Item_detail_RC);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case SETTINGS.New_Item_RC:
                if (resultCode == RESULT_OK) {
                    item_list.notifyDataSetChanged(current_year);
                    Toast.makeText(MainActivity.this, "insert one", Toast.LENGTH_SHORT).show();
                }
                break;
            case SETTINGS.Item_detail_RC:
                if (resultCode == RESULT_OK) {
                    item_list.notifyDataSetChanged(current_year);
                    Toast.makeText(MainActivity.this, "update one", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    private void SetUpApp() {
        dbHelper = new DatabaseHelper(this, "Accounts.db", null, SETTINGS.CURRENT_DB_VERSION);
        dbHelper.getWritableDatabase();     // 确保已经创建好数据库文件

        current_year = SETTINGS.getCurrentYear();

        Button summary = (Button) findViewById(R.id.right_btn);
        summary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ViewMoreItem.class);
                startActivity(intent);
            }
        });

        Button more = (Button) findViewById(R.id.left_btn);
        //more.setBackgroundResource(R.drawable.notes_btn);
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "More Functions", Toast.LENGTH_SHORT).show();
            }
        });
    }
}