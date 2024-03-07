package tk.alexapp.freestuffandcoupons.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import tk.alexapp.freestuffandcoupons.R;
import tk.alexapp.freestuffandcoupons.domain.ListItemInfo;
import tk.alexapp.freestuffandcoupons.domain.TabInfo;

public class ItemsArrayAdapter extends ArrayAdapter<ListItemInfo> {

    private static final String TAG = "ItemsArrayAdapter";
    private final Activity context;
    private TabInfo tabInfo;

    public ItemsArrayAdapter(Activity context, TabInfo tabInfo) {
        super(context, R.layout.list_item, tabInfo.getItemsAsArray());
        this.tabInfo = tabInfo;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(R.layout.list_item, null, true);
        }
        TextView itemLabel = (TextView) convertView.findViewById(R.id.itemLabel);
        final ListItemInfo itemInfo = tabInfo.getItemInfo(position);
        itemLabel.setText(itemInfo.getTitle());
        ImageView itemImage = (ImageView) convertView.findViewById(R.id.itemImage);
        downloadImage(itemInfo, itemImage);
        return convertView;
    }

    private void downloadImage(final ListItemInfo itemInfo, ImageView itemImage) {

//        Picasso.with(context).load(itemInfo.getThumbUrl()).into(itemImage, new Callback() {
//            @Override
//            public void onSuccess() {
//
//            }
//
//            @Override
//            public void onError() {
//                Log.e(TAG, "error on loading image: " + itemInfo.getThumbUrl());
//            }
//        });
    }
}
