package com.example.liuan.screenview.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.liuan.screenview.R;
import com.example.liuan.screenview.bean.MediaItem;
import com.litesuits.common.assist.Check;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ItemParentPhotoAdapter extends BaseAdapter {

    private List<String> objects;
    private Map<String, List<MediaItem>> mMap;

    private Context context;
    private LayoutInflater layoutInflater;

    public ItemParentPhotoAdapter(Context context, Map<String, List<MediaItem>> map) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        mMap = map;
        objects = new ArrayList<>(mMap.keySet());

    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public String getItem(int position) {
        return objects.get(position);
    }

    public int getViewPosition(String s) {
        if (!TextUtils.isEmpty(s) && objects != null && objects.size() > 0) {
            for (int i = 0; i < objects.size(); i++) {
                if (s.equals(objects.get(i))) {
                    return i;
                }
            }
        }
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_parent_photo, null);
            convertView.setTag(new ViewHolder(convertView));
        }
        initializeViews((String) getItem(position), (ViewHolder) convertView.getTag());
        return convertView;
    }

    private void initializeViews(String object, ViewHolder holder) {
        //TODO implement
        String title = "---";
        try {
            if (!Check.isEmpty(object)) {
                title = new File(object).getName() + "(" + mMap.get(object).size() + ")";
            }
        } catch (Exception e) {
            title = "----" + "(" + mMap.get(object).size() + ")";
        }
        holder.tvTitle.setText(title);
        if (mMap.get(object) != null && mMap.get(object).size() > 0) {
            Glide.with(context)
                    .load(Uri.fromFile(new File(mMap.get(object).get(0).getPath())))
                    .asBitmap()
                    .centerCrop()
                    .placeholder(R.drawable.ic_photos)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(holder.iv);
        }
    }

    static class ViewHolder {
        @Bind(R.id.iv)
        ImageView iv;
        @Bind(R.id.tv_title)
        TextView tvTitle;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        if (observer!=null) {
            super.unregisterDataSetObserver(observer);
        }
    }
}
