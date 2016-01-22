package hw.happyjacket.com.happylife;

import android.content.Intent;
import android.content.SharedPreferences;
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

public class create_item extends AppCompatActivity {
    private SharedPreferences.Editor editor;
    private ArrayAdapter<String> adapter;
    private Spinner item_tag_spinner;
    private String [] tags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_item);
        setTitleBar();

        editor = getSharedPreferences("new_item", MODE_PRIVATE).edit();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, SETTINGS.tags_out);

        item_tag_spinner = (Spinner) findViewById(R.id.item_tag_spinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        item_tag_spinner.setAdapter(adapter);
        item_tag_spinner.setVisibility(View.VISIBLE);
        item_tag_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                editor.putInt("item_tag", position);
                EditText item_name = (EditText) findViewById(R.id.item_name);
                if (item_name.getText().toString().length() == 0 && tags != null) {
                    item_name.setText(tags[position]);
                } else if (tags == null) {
                    tags = SETTINGS.tags_out;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Switch aSwitch = (Switch) findViewById(R.id.item_kind_switch);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean("item_kind", isChecked);

                if (isChecked)
                    tags = SETTINGS.tags_in;
                else
                    tags = SETTINGS.tags_out;
                adapter = new ArrayAdapter<String>(create_item.this, android.R.layout.simple_spinner_item, tags);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                item_tag_spinner.setAdapter(adapter);
            }
        });
    }

    void setTitleBar() {
        TextView title = (TextView) findViewById(R.id.green_title_text);
        Button cancel = (Button) findViewById(R.id.left_btn);
        Button ok = (Button) findViewById(R.id.right_btn);
        title.setText("新建记账项目");
        cancel.setText("取消");
        ok.setText("确定");

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(create_item.this, MainActivity.class);
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText edit = (EditText) findViewById(R.id.item_name);
                String item_name = edit.getText().toString();

                edit = (EditText) findViewById(R.id.item_price);
                float price = 0;
                if (edit.getText().length() > 0)
                    price = Float.valueOf(edit.getText().toString());

                editor.putString("item_name", item_name);
                editor.putFloat("item_price", price);
                editor.commit();

                Intent intent = new Intent(create_item.this, MainActivity.class);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
