package com.example.liuan.screenview.adapter;

import android.app.Activity;
import android.database.DataSetObserver;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.liuan.screenview.R;
import com.example.liuan.screenview.bean.MediaItem;

import java.io.File;
import java.util.List;



/**
 * Created by 吴建利 on 2016/3/3.
 */
public class ShowImageViewPagerAdapter extends PagerAdapter implements View.OnClickListener {
    private List<MediaItem> mEntities;
    private Activity mActivity;
    private LayoutInflater mLayoutInflater;
    private ItemClick mItemClick;
    private boolean mIsVideo;

    public void setItemClick(ItemClick itemClick) {
        mItemClick = itemClick;
    }

    public ShowImageViewPagerAdapter(Activity activity, List<MediaItem> entities, boolean isVideo) {
        mActivity = activity;
        mEntities = entities;
        mLayoutInflater = activity.getLayoutInflater();
        mIsVideo = isVideo;
    }

    @Override
    public int getCount() {
        return mEntities.size();
    }

    //判断当前的当前呈现的View是不是我们最初加载的View
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    //初始化每个条目
    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        FrameLayout frameLayout = (FrameLayout) mLayoutInflater.inflate(R.layout.item_show_iamge, container, false);
        PhotoView photoView = (PhotoView) frameLayout.findViewById(R.id.pv_item_vp_activity_big_pic);
        ImageView playView = (ImageView) frameLayout.findViewById(R.id.iv_play);
        if (!mIsVideo) {
            playView.setVisibility(View.GONE);
        } else {
            playView.setVisibility(View.VISIBLE);
        }
        photoView.enable();
        photoView.setOnClickListener(this);
        playView.setOnClickListener(this);
        photoView.setTag(R.id.pos, position);
        playView.setTag(R.id.pos, position);
        Glide.with(container.getContext())
                .load(Uri.fromFile(new File(mEntities.get(position).getPath())))
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(photoView);
        container.removeView(frameLayout);
        container.addView(frameLayout);
        return frameLayout;
    }


    //销毁条目
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public void onClick(View v) {
        if (mItemClick != null) {
            mItemClick.onItemClick(v, (Integer) v.getTag(R.id.pos));
        }
    }

    public interface ItemClick {
        void onItemClick(View view, int position);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        if (observer!=null) {
            super.unregisterDataSetObserver(observer);
        }
    }
}
