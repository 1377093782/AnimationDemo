package com.example.liuan.screenview.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.liuan.screenview.LockActivity;
import com.example.liuan.screenview.view.MyViewPager;
import com.example.liuan.screenview.R;
import com.example.liuan.screenview.adapter.ShowImageViewPagerAdapter;
import com.example.liuan.screenview.bean.MediaItem;
import com.example.liuan.screenview.utils.MediaVaultUtil;

import java.io.File;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ShowMediaActivity extends LockActivity implements View.OnClickListener {

    @Bind(R.id.viewpager)
    MyViewPager mViewpager;
    @Bind(R.id.iv_back)
    ImageView mIvBack;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.ll_title)
    LinearLayout mLlTitle;
    @Bind(R.id.iv_unlock)
    ImageView mIvUnlock;
    @Bind(R.id.iv_delete)
    ImageView mIvDelete;
    @Bind(R.id.ll_bottom)
    LinearLayout mLlBottom;
    private ShowImageViewPagerAdapter mAdapter;
    private ArrayList<MediaItem> mList = new ArrayList<>();
    private int mPosition = 0;
    private boolean mIsVideo;
    private boolean mIsInstagram = false;
    private boolean mIsLock = false;
    private MediaItem mNowMediaItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        if (!intent.hasExtra("entity") || intent.getParcelableArrayListExtra("entity") == null) {
            finish();
            return;
        }
        mIsInstagram = intent.getBooleanExtra("instagram", false);
        mIsLock = intent.getBooleanExtra("lock", false);
        mList = intent.getParcelableArrayListExtra("entity");
        if (intent.hasExtra("position")) {
            mPosition = intent.getIntExtra("position", 0);
        }
        if (intent.hasExtra("type")) {
            mIsVideo = intent.getBooleanExtra("type", false);
        }
        if (!mIsInstagram) {
            if (mIsLock) {
                mIvUnlock.setImageResource(R.drawable.ic_unlock);
            } else {
                mIvUnlock.setImageResource(R.drawable.ic_lock);
            }
            mIvUnlock.setOnClickListener(this);
            mIvDelete.setOnClickListener(this);
        }
        mIvBack.setImageResource(R.drawable.ic_arrow_selector);
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mNowMediaItem = mList.get(mPosition);
        mTvTitle.setText(mList.get(mPosition).getRealName());
        mAdapter = new ShowImageViewPagerAdapter(this, mList, mIsVideo);
        mViewpager.setAdapter(mAdapter);
        mViewpager.setCurrentItem(mPosition);
        mViewpager.setOffscreenPageLimit(2);
        mAdapter.setItemClick(new ShowImageViewPagerAdapter.ItemClick() {
            @Override
            public void onItemClick(View view, int position) {
                switch (view.getId()) {
                    case R.id.pv_item_vp_activity_big_pic:
                        mLlTitle.setVisibility(mLlTitle.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                        if (!mIsInstagram) {
                            mLlBottom.setVisibility(mLlBottom.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                        }
                        break;
                    case R.id.iv_play:
                        if (mIsVideo) {
                            Intent intent = new Intent("android.intent.action.VIEW");
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("oneshot", 0);
                            intent.putExtra("configchange", 0);
                            Uri uri = Uri.fromFile(new File(mList.get(position).getPath()));
                            intent.setDataAndType(uri, "video/*");
                            ShowMediaActivity.this.startActivity(intent);
                        }
                        break;
                }
            }
        });

        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTvTitle.setText(mList.get(position).getRealName());
                mNowMediaItem = mList.get(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if (mNowMediaItem == null) {
            return;
        }
        switch (v.getId()) {
            case R.id.iv_unlock:
                if (mIsLock) {
                    if (mIsVideo) {
                        MediaVaultUtil.unlockVideo(this, mNowMediaItem);
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        MediaVaultUtil.unlockPhoto(this, mNowMediaItem);
                        setResult(RESULT_OK);
                        finish();
                    }
                } else {
                    if (mIsVideo) {
                        MediaVaultUtil.lockVideo(this, mNowMediaItem);
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        MediaVaultUtil.lockPhoto(this, mNowMediaItem);
                        setResult(RESULT_OK);
                        finish();
                    }
                }
                break;
            case R.id.iv_delete:
                MediaVaultUtil.trashPhoto(mNowMediaItem);
                setResult(RESULT_OK);
                finish();
                break;
        }

    }
}
