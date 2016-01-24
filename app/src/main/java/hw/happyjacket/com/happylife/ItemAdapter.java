package hw.happyjacket.com.happylife;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by jacket on 2016/1/20.
 */
public class ItemAdapter extends ArrayAdapter<Item> {
    private int resourceId;

    public ItemAdapter(Context context, int ItemResourceId, List<Item> objects) {
        super(context, ItemResourceId, objects);
        resourceId = ItemResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Item item = getItem(position);
        View view = null;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        } else {
            view = convertView;
        }
        TextView item_name = (TextView) view.findViewById(R.id.item_name_text);
        TextView item_price = (TextView) view.findViewById(R.id.item_price_text);
        TextView item_date = (TextView) view.findViewById(R.id.item_date);
        ImageView icon = (ImageView) view.findViewById(R.id.item_icon);

        item_name.setText(item.getShowName());
        item_price.setText(item.getPrice());
        item_date.setText(item.getDate());

        if (item.getKind() == 0)
            icon.setImageResource(android.R.drawable.ic_delete);
        else
            icon.setImageResource(android.R.drawable.btn_star_big_on);
        return view;
    }
}
