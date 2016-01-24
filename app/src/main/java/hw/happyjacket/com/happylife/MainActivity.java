package hw.happyjacket.com.happylife;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private Item_List_View item_list;
    private int current_year;
    private SlidingMenu mMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slide_main_layout);
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


    public void toggleMenu(View view)
    {
        mMenu.toggle();
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

        mMenu = (SlidingMenu) findViewById(R.id.id_menu);
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
                toggleMenu(getWindow().getDecorView());
            }
        });

        RelativeLayout savings = (RelativeLayout) findViewById(R.id.do_savings);
        savings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float in = item_list.getTotalCost(1);
                float out = item_list.getTotalCost(0);
                Toast.makeText(MainActivity.this, "当前余额为：" + (in-out), Toast.LENGTH_LONG).show();
            }
        });

        RelativeLayout export = (RelativeLayout) findViewById(R.id.do_export_home);
        export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item_list.exportItems();
            }
        });

        RelativeLayout about = (RelativeLayout) findViewById(R.id.say_about_me);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("关于");
                dialog.setMessage(SETTINGS.aboutMe);
                dialog.setPositiveButton("嗯，朕知道了~", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    public static void save(Context m_context, String fileName, String text) {
        FileWriter fw;
        try {
            fw = new FileWriter(fileName, true);
            fw.write(text, 0, text.length());
            fw.close();
            Toast.makeText(m_context, "数据文件保存在：" + fileName, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("ExternalStorage", "Error writing " + fileName);
            Toast.makeText(m_context, "Failed to open file " + fileName, Toast.LENGTH_SHORT).show();
        }
    }
}