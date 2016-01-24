package hw.happyjacket.com.happylife;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Update_item extends Create_item {
    private Item ori_item;
    private long item_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleBar2();

        Intent intent = getIntent();
        item_id = intent.getLongExtra("id", -1);
        ori_item = getTheOriItem(item_id);
        if (item_id != -1) {
            setTheView2(ori_item);
        } else {
            Toast.makeText(Update_item.this, "加载出错，请返回主界面重试", Toast.LENGTH_LONG).show();
        }
    }

    private void setTitleBar2() {
        TextView title = (TextView) findViewById(R.id.green_title_text);
        title.setText("编辑记账项目");

        Button back = (Button) findViewById(R.id.left_btn);
        back.setText("返回");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Update_item.this, MainActivity.class);
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });

        Button update_item = (Button) findViewById(R.id.right_btn);
        update_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Update_item.this, MainActivity.class);
                Item item = getTheInputItem();
                item.setId(item_id);

                if (ori_item.different(item)) {
                    long number_of_rows_affectd = updateTheItem(dbHelper, item);

                    if (number_of_rows_affectd == 0)
                        setResult(RESULT_CANCELED, intent);
                    else
                        setResult(RESULT_OK, intent);
                } else {
                    setResult(RESULT_CANCELED, intent);
                }

                finish();
            }
        });


        Button delete_item = (Button) findViewById(R.id.sure_to_delete);
        delete_item.setVisibility(View.VISIBLE);
        title.setText("编辑记账项目");
        back.setText("返回");
        delete_item.setText("删除");
        delete_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(Update_item.this);
                dialog.setTitle("删除账目");
                dialog.setMessage("真的要删除这一项吗？？？");
                dialog.setCancelable(true);
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.setPositiveButton("确认删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        int id = db.delete(SETTINGS.Item_db_table_name, "id = ?", new String[]{"" + item_id});

                        Intent intent = new Intent(Update_item.this, MainActivity.class);
                        if (id <= 0)
                            setResult(RESULT_CANCELED, intent);
                        else
                            setResult(RESULT_OK, intent);
                        dialog.dismiss();
                        finish();
                    }
                });
                dialog.show();
            }
        });
    }

    private void setTheView2(Item item) {
        item_name_text.setText(item.getName());
        item_price_text.setText(item.getPrice());

        if (item.getKind() == 1) {
            item_kind_switch.setChecked(true);
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, SETTINGS.tags_in);
        }
        else {
            item_kind_switch.setChecked(false);
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, SETTINGS.tags_out);
        }

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        item_tag_spinner.setAdapter(adapter);
        item_tag_spinner.setVisibility(View.VISIBLE);
        item_tag_spinner.setSelection(SETTINGS.getTagIndex(item.getKind(), item.getTag()));
    }

    private Item getTheOriItem(long id) {
        if (id != -1) {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery(String.format("select * from Accounts where id = %d", id), null);
            cursor.moveToFirst();
            if (!cursor.isAfterLast()) {
                int kind = cursor.getInt(cursor.getColumnIndex("kind"));
                float price = cursor.getFloat(cursor.getColumnIndex("price"));
                String time = cursor.getString(cursor.getColumnIndex("time"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String tag = cursor.getString(cursor.getColumnIndex("tag"));
                cursor.close();

                return new Item(id, name, price, kind, time, tag);
            }
            cursor.close();
        }
        return new Item(-1, "", 0, 0, "", "");
    }

    public static long updateTheItem(DatabaseHelper dbHelper, Item item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("kind", item.getKind());
        values.put("price", item.getPrice());
        values.put("time", item.getTime());
        values.put("name", item.getName());
        values.put("tag", item.getTag());
        return db.update("Accounts", values, "id = ?", new String[]{"" + item.getId()});
    }
}
