package hw.happyjacket.com.happylife;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ViewMoreItem extends AppCompatActivity {
    private Item_List_View item_list;
    private int current_year = SETTINGS.getCurrentYear();
    private ArrayList<Integer> all_years;
    private Spinner years_spinner;
    private ArrayAdapter<Integer> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_more_item);
        setTitleBar();
        prepareYearData();

        item_list = new Item_List_View(this, getWindow().getDecorView(), R.id.item_list_more, current_year);
        item_list.setListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent("Update_item");
                intent.putExtra("id", item_list.getSelectedId(position));
                startActivityForResult(intent, SETTINGS.Item_detail_RC);
            }
        });

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, all_years);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        years_spinner = (Spinner) findViewById(R.id.all_years);
        years_spinner.setAdapter(adapter);
        years_spinner.setVisibility(View.VISIBLE);
        years_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                current_year = all_years.get(position);
                int size = item_list.notifyDataSetChanged(current_year);
                if (size == 0)
                    Toast.makeText(ViewMoreItem.this, String.format("%d年暂时木有记录~", current_year), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case SETTINGS.Item_detail_RC:
                if (resultCode == RESULT_OK) {
                    item_list.notifyDataSetChanged(current_year);
                }
                break;
            default:
        }
    }

    private void prepareYearData() {
        all_years = new ArrayList<>();
        for (int i = current_year; i >= SETTINGS.Lowest_year; --i) {
            all_years.add(i);
        }
    }

    private void setTitleBar() {
        TextView title = (TextView) findViewById(R.id.green_title_text);
        title.setText("查看所有账目");

        Button back = (Button) findViewById(R.id.left_btn);
        back.setText("返回");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewMoreItem.this, MainActivity.class);
                startActivity(intent);
            }
        });

        Button summary = (Button) findViewById(R.id.right_btn);
        summary.setText("统计收支");
        summary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = "收入——\n";
                float in = 0, out = 0;

                for (int i = 0; i < SETTINGS.tags_in.length; ++i) {
                    float price = getTagCost(current_year, SETTINGS.tags_in[i]);
                    if (price > 0) {
                        in += price;
                        msg += SETTINGS.tags_in[i] + "：" + price + "\n";
                    }
                }

                msg += String.format("收入总计：%#.2f元\n\n支出——\n", in);
                for (int i = 0; i < SETTINGS.tags_out.length; ++i) {
                    float price = getTagCost(current_year, SETTINGS.tags_out[i]);
                    if (price > 0) {
                        out += price;
                        msg += SETTINGS.tags_out[i] + "：" + price + "\n";
                    }
                }

                msg += String.format("支出总计：%#.2f元", out);

                AlertDialog.Builder dialog = new AlertDialog.Builder(ViewMoreItem.this);
                dialog.setTitle(String.format("%d年度的开支情况如下：", current_year));
                if (in > 0 || out > 0)
                    dialog.setMessage(msg);
                else
                    dialog.setMessage("暂时木有记录");
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

    private float getTagCost(int year, String tag) {
        float sum = 0;

        DatabaseHelper dbHelper = new DatabaseHelper(this, "Accounts.db", null, SETTINGS.CURRENT_DB_VERSION);;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(String.format("select price from Accounts where time like '%d%%' and tag='%s'", year, tag), null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            sum += cursor.getFloat(cursor.getColumnIndex("price"));
            cursor.moveToNext();
        }
        cursor.close();

        return sum;
    }
}