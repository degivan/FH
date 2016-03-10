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
    List<String> titles = new ArrayList<>();
    List<String> descriptions = new ArrayList<>();
    List<String> additionalInfo = new ArrayList<>();

    public ItemsAdapter(List<Place> places, Activity context) {
        super();
        this.context = context;
        Stream.of(places).forEach(p -> {
            titles.add(p.getName());
            descriptions.add(p.getAddress());
            additionalInfo.add(Double.toString(Math.round(p.getRate())) + "/5");
        });
    }

    public ItemsAdapter(Activity context, List<Dish> dishes) {
        super();
        this.context = context;
        Stream.of(dishes).forEach(d -> {
            titles.add(d.getName());
            descriptions.add(d.getAddress() + " " + d.getPlaceName());
            additionalInfo.add(Integer.toString(d.getPrice()));
        });
    }

    @Override
    public int getCount() {
        return titles.size();
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

        holder.txtViewTitle.setText(titles.get(position));
        holder.txtViewDescription.setText(descriptions.get(position));
        holder.txtViewAdditionalInfo.setText(additionalInfo.get(position));

        return convertView;
    }
}
