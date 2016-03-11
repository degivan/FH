package spbau.mit.divan.foodhunter.activities;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.annimon.stream.Stream;

import java.util.ArrayList;
import java.util.List;

import spbau.mit.divan.foodhunter.R;
import spbau.mit.divan.foodhunter.dishes.Dish;
import spbau.mit.divan.foodhunter.dishes.Place;

public class ItemsAdapter extends BaseAdapter {
    Activity context;
    List<Item> items = new ArrayList<>();

    static class Item {
        private String title;
        private String description;
        private String additionalInfo;

        public Item(String title, String description, String additionalInfo) {
            this.title = title;
            this.description = description;
            this.additionalInfo = additionalInfo;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public String getAdditionalInfo() {
            return additionalInfo;
        }
    }

    public ItemsAdapter(List<Place> places, Activity context) {
        super();
        this.context = context;
        for (Place p: places) {
            items.add(new Item(p.getName(), p.getAddress(), Double.toString(Math.round(p.getRate())) + "/5"));
        }
    }

    public ItemsAdapter(Activity context, List<Dish> dishes) {
        super();
        this.context = context;
        for (Dish d: dishes) {
            items.add(new Item(d.getName(), d.getAddress() + " " + d.getPlaceName(), Integer.toString(d.getPrice())));
        }
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder {
        TextView txtViewTitle;
        TextView txtViewDescription;
        TextView txtViewAdditionalInfo;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = context.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.searchlistitem_row, null);
            holder = new ViewHolder();
            holder.txtViewTitle = (TextView) convertView.findViewById(R.id.text1);
            holder.txtViewDescription = (TextView) convertView.findViewById(R.id.text2);
            holder.txtViewAdditionalInfo = (TextView) convertView.findViewById(R.id.text3);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtViewTitle.setText(items.get(position).getTitle());
        holder.txtViewDescription.setText(items.get(position).getDescription());
        holder.txtViewAdditionalInfo.setText(items.get(position).getAdditionalInfo());

        return convertView;
    }
}
