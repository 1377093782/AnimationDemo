package com.example.liuan.screenview.activity;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.liuan.screenview.BaseFragment;
import com.example.liuan.screenview.R;
import com.example.liuan.screenview.adapter.FragmentViewPagerAdapter;
import com.example.liuan.screenview.fragment.PhotosFragment;
import com.example.liuan.screenview.fragment.VideosFragment;
import com.example.liuan.screenview.view.MyViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PrivateVaultActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tl_am_tabs)
    TabLayout tlAmTabs;
    @Bind(R.id.app_bar)
    AppBarLayout appBar;
    @Bind(R.id.top_shadow)
    View topShadow;
    @Bind(R.id.vp_am_viewpager)
    MyViewPager vpAmViewpager;
    private int[] tabIcons = {
            R.drawable.ic_photos_selector,
            R.drawable.ic_videos_selector};
    private List<BaseFragment> mFragments;
    private ArrayList<String> mTitles;
    private FragmentViewPagerAdapter mViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD); //关闭锁屏  不需要额外权限
        setContentView(R.layout.private_vault_layout);
        ButterKnife.bind(this);
        initFragment();
        initNaviBar();


    }

    private void initNaviBar() {
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);



    }

    private void initFragment() {
        mFragments = new ArrayList<>();
        mFragments.add(new PhotosFragment());
        mFragments.add(new VideosFragment());
//        mFragments.add(new FingerprintFragment());
        mTitles = new ArrayList<>();

        mTitles.add(getString(R.string.photos));
        mTitles.add(getString(R.string.videos));
//        mTitles.add(getString(R.string.fingerprint));
        mViewPagerAdapter = new FragmentViewPagerAdapter(getSupportFragmentManager(), mFragments, mTitles);
        vpAmViewpager.setAdapter(mViewPagerAdapter);
        tlAmTabs.setupWithViewPager(vpAmViewpager);
        tlAmTabs.getTabAt(0).setCustomView(getTabView(0));
        tlAmTabs.getTabAt(1).setCustomView(getTabView(1));
        vpAmViewpager.setOffscreenPageLimit(2);
        vpAmViewpager.setCurrentItem(1);
        vpAmViewpager.setCurrentItem(0);
        vpAmViewpager.addOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
    public View getTabView(int position) {
        View view = LayoutInflater.from(this).inflate(R.layout.item_tab, null);
        TextView txt_title = (TextView) view.findViewById(R.id.txt_title);
        txt_title.setText(mTitles.get(position));
        txt_title.setVisibility(View.GONE);
        ImageView img_title = (ImageView) view.findViewById(R.id.img_title);
        img_title.setImageResource(tabIcons[position]);
//        img_title.setVisibility(View.GONE);
        return view;
    }
}
