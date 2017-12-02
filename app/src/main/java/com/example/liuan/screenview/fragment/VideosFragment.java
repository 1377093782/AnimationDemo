package com.example.liuan.screenview.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.liuan.screenview.BaseFragment;
import com.example.liuan.screenview.R;
import com.example.liuan.screenview.activity.AddActivity;
import com.example.liuan.screenview.activity.EditActivity;
import com.example.liuan.screenview.adapter.ItemChildPhotoAdapter;
import com.example.liuan.screenview.adapter.ItemParentPhotoAdapter;
import com.example.liuan.screenview.bean.MediaEntity;
import com.example.liuan.screenview.bean.MediaItem;
import com.example.liuan.screenview.utils.MediaVaultUtil;
import com.litesuits.common.assist.Check;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.halfbit.pinnedsection.PinnedSectionListView;


public class VideosFragment extends BaseFragment implements View.OnClickListener {

    public static final int LOADING_FINISH_WHAT = 100;
    public static final int VIDEO_EDIT_REQUEST_CODE = 200;
    public static final int VIDEO_ADD_REQUEST_CODE = 300;
    public static final String CATEGORY = "视频界面";
    public static final String ACTION = "点击";

    @Bind(R.id.iv_no_data)
    ImageView mIvNoData;
    @Bind(R.id.rl_no_data)
    RelativeLayout mRlNoData;
    @Bind(R.id.lv_parent)
    ListView mLvParent;
    @Bind(R.id.rl_parent)
    RelativeLayout mRlParent;
    @Bind(android.R.id.list)
    PinnedSectionListView mList;
    @Bind(R.id.rl_child)
    RelativeLayout mRlChild;
    @Bind(R.id.add)
    ImageView mAdd;
    @Bind(R.id.tv_no_date)
    TextView mTvNoDate;
    @Bind(R.id.rl_root)
    RelativeLayout mRlRoot;

    private View mView;
    public static List<MediaEntity> mMediaEntities = new ArrayList<>();
    public static Map<String, List<MediaItem>> mMap = new HashMap<>();
    private ExecutorService mExecutorService = Executors.newFixedThreadPool(2);
    private LoadingTask mLoadingTask;
    private ItemChildPhotoAdapter mChildPhotoAdapter;
    private ItemParentPhotoAdapter mParentPhotoAdapter;
    private Context mContext;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == LOADING_FINISH_WHAT) {
                Object[] objects = (Object[]) msg.obj;
                if (objects.length == 2) {
                    setData((List<MediaEntity>) objects[0], (Map<String, List<MediaItem>>) objects[1]);

                }
            }
        }
    };
    private boolean mIsEdit = false;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.edit, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_edit) {
            if (mMediaEntities == null || mMediaEntities.size() == 0 || mMap == null || mMap.size() == 0) {
                Toast.makeText(getContext(), R.string.no_data_string, Toast.LENGTH_SHORT).show();
                return true;
            }
            Intent intent = new Intent(getContext(), EditActivity.class);
            intent.putExtra("from", VideosFragment.class.getSimpleName());
            startActivityForResult(intent, VIDEO_EDIT_REQUEST_CODE);
           // Firebase.getInstance(mContext).logEvent(VideosFragment.CATEGORY, VideosFragment.ACTION, "编辑");

        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == VIDEO_EDIT_REQUEST_CODE || requestCode == VIDEO_ADD_REQUEST_CODE) && resultCode == Activity.RESULT_OK) {
            if (mLoadingTask != null) {
                mLoadingTask.cancel(true);
            }
            if (!mExecutorService.isShutdown()) {
                mLoadingTask = new LoadingTask(getActivity(), mHandler);
                mLoadingTask.executeOnExecutor(mExecutorService);
            }
            mIsEdit = true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_photos, container, false);
        ButterKnife.bind(this, mView);
        mTvNoDate.setText(R.string.hide_your_private_videos);
        showEmpty();
        return mView;
    }

    //设置数据
    public void setData(List<MediaEntity> photoEntities, Map<String, List<MediaItem>> map) {
        if (photoEntities != null && photoEntities.size() > 0 && map != null && map.size() > 0) {
            mMediaEntities.clear();
            mMediaEntities.addAll(photoEntities);
            mMap.clear();
            mMap.putAll(map);
            initChildView();
            initParentView();
            showChild(null);
        } else {
            if (!Check.isEmpty(photoEntities)) {
                mMediaEntities.clear();
                mMediaEntities.addAll(photoEntities);
                initChildView();
                showChild(null);
                showOneData();
            } else {
                showEmpty();
            }
        }
    }

    private void initParentView() {
        mParentPhotoAdapter = new ItemParentPhotoAdapter(mContext, mMap);
        mLvParent.setAdapter(mParentPhotoAdapter);
        mLvParent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String key = mParentPhotoAdapter.getItem(position);
                if (mMap.get(key) != null && mMap.get(key).size() > 0) {
                    showChild(mMap.get(key).get(0).getRealParentPath());
                } else {
                    showChild(null);
                }
            }
        });
    }

    //设置listView
    public void initChildView() {
        mChildPhotoAdapter = new ItemChildPhotoAdapter(mContext, mMediaEntities, mMap, false, true);
        mChildPhotoAdapter.setFragment(this);
        mList.setAdapter(mChildPhotoAdapter);
        mChildPhotoAdapter.setClickListener(new ItemChildPhotoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (mChildPhotoAdapter.getItemViewType(position) == 0) {
                    //标题，显示父目录
                    MediaEntity entity = mChildPhotoAdapter.getItem(position);
                    if (mMap.containsKey(entity.getParentPath())) {
                        if (mParentPhotoAdapter != null) {
                            showParent(mParentPhotoAdapter.getViewPosition(entity.getParentPath()));
                        }
                    } else {
                        showParent(0);
                    }
                }
            }

            @Override
            public void onItemLongClick(View view, int positon) {

            }
        });
        if (mIsEdit) {
//            addAdView(false);
            mIsEdit = false;
        }
    }

    //显示空数据
    public void showEmpty() {
        if (mRlNoData!=null) {
            mRlNoData.setVisibility(View.VISIBLE);
        }
        if (mRlChild!=null) {
            mRlChild.setVisibility(View.GONE);
        }
        if (mRlParent!=null) {
            mRlParent.setVisibility(View.GONE);
        }
    }

    public void showOneData() {
        if (mRlNoData!=null) {
            mRlNoData.setVisibility(View.VISIBLE);
        }
        if (mRlChild!=null) {
            mRlChild.setVisibility(View.VISIBLE);
        }
        if (mRlParent!=null) {
            mRlParent.setVisibility(View.GONE);
        }
    }

    //显示父目录
    public void showParent(int position) {
        if (mRlParent!=null) {
            mRlParent.setVisibility(View.VISIBLE);
        }
        if (mLvParent!=null) {
            mLvParent.setSelection(position);
        }
        if (mRlNoData!=null) {
            mRlNoData.setVisibility(View.GONE);
        }
        if (mRlChild!=null) {
            mRlChild.setVisibility(View.GONE);
        }
    }

    //显示子目录
    public void showChild(String parentPath) {
        if (mRlChild!=null) {
            mRlChild.setVisibility(View.VISIBLE);
        }
        if (mList!=null) {
            mList.setSelection(getParentPosition(parentPath));
        }
        if (mRlParent!=null) {
            mRlParent.setVisibility(View.GONE);
        }
        if (mRlNoData!=null) {
            mRlNoData.setVisibility(View.GONE);
        }
    }

    //根据父目录路径找到childlistview对应的第一条位置
    public int getParentPosition(String parentPath) {
        int position = 0;
        if (!TextUtils.isEmpty(parentPath) && mMediaEntities != null && mMediaEntities.size() > 0) {
            for (int i = 0; i < mMediaEntities.size(); i++) {
                if (mMediaEntities.get(i).getType() == 0) {
                    continue;
                }
                if (mMediaEntities.get(i).getPhotoItems() != null
                        && mMediaEntities.get(i).getPhotoItems().size() > 0
                        && parentPath.equals(mMediaEntities.get(i).getPhotoItems().get(0).getRealParentPath())) {
                    return i;
                }
            }
        }
        return position;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mLoadingTask = new LoadingTask(getContext().getApplicationContext(), mHandler);
        mLoadingTask.executeOnExecutor(mExecutorService);
        mAdd.setOnClickListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mLoadingTask != null) {
            mLoadingTask.cancel(true);
        }
        mExecutorService.shutdown();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        ButterKnife.unbind(this);
        mHandler.removeMessages(LOADING_FINISH_WHAT);
        ButterKnife.unbind(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add) {
            Intent intent = new Intent(getActivity(), AddActivity.class);
            intent.putExtra("type", true);
            startActivityForResult(intent, VIDEO_ADD_REQUEST_CODE);
            //Firebase.getInstance(mContext).logEvent(CATEGORY, ACTION, "添加video");
        }
    }

    private class LoadingTask extends AsyncTask<Void, Void, List<MediaEntity>> {
        private Context mContext;
        private List<MediaEntity> mList = new ArrayList<>();
        //private List<PhotoParentItem> mParentList = new ArrayList<>();
        //private List<String> mPaths = new ArrayList<>();
        private HashMap<String, List<MediaItem>> mMap = new HashMap<>();
        private Handler mHandler;

        public LoadingTask(Context context, Handler handler) {
            mContext = context;
            mHandler = handler;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<MediaEntity> doInBackground(Void... params) {
            if (mContext != null && mHandler != null) {
                List<MediaItem> list = MediaVaultUtil.getEncryptedVideo(mContext);
//                MediaEntity mediaEntity = new MediaEntity();
//                mediaEntity.setType(2);
//                mList.add(0, mediaEntity);
                if (list != null && list.size() > 0) {
//                    System.out.println("list.size() = " + list.size());
                    mList.clear();
                    mMap.clear();
//                    mList.add(0, mediaEntity);
                    MediaEntity photoEntity = null;
                    MediaEntity titleEntity = null;
                    List<MediaItem> mapList = null;
                    for (MediaItem item : list) {
                        item.setSelect(false);
                        String realParentPath = item.getRealParentPath();
                        if (mMap.containsKey(realParentPath)) {
                            mMap.get(realParentPath).add(item);
                            //已经添加过目录
                            if (photoEntity.isFull()) {
                                //满了，下一行添加
                                photoEntity = new MediaEntity();
                                photoEntity.setType(1);
                                photoEntity.addPhotoItem(item);
                                photoEntity.setSelect(false);
                                mList.add(photoEntity);
                            } else {
                                //没满，继续向该行添加
                                photoEntity.setSelect(false);
                                photoEntity.addPhotoItem(item);
                            }
                        } else {
                            //发现新的目录
                            titleEntity = new MediaEntity();
                            titleEntity.setType(0);
                            titleEntity.setTitle(item.getRealParentName());
                            titleEntity.setCount(1);
                            titleEntity.setParentPath(item.getRealParentPath());
                            titleEntity.setSelect(false);
                            mList.add(titleEntity);
                            photoEntity = new MediaEntity();
                            photoEntity.setType(1);
                            photoEntity.addPhotoItem(item);
                            photoEntity.setSelect(false);
                            mList.add(photoEntity);
                            mapList = new ArrayList<>();
                            mapList.add(item);
                            mMap.put(realParentPath, mapList);
                        }
                    }
                }
            }
            return mList;
        }

        @Override
        protected void onPostExecute(List<MediaEntity> list) {
            super.onPostExecute(list);
            if (mHandler != null) {
                Message message = mHandler.obtainMessage();
                Object[] objects = new Object[2];
                objects[0] = mList;
                objects[1] = mMap;
                message.obj = objects;
                message.what = PhotosFragment.LOADING_FINISH_WHAT;
                mHandler.sendMessage(message);
            }
        }
    }

//    public void addAdView(final boolean isVideoShow) {
//        int time = 500;
//        if (isVideoShow) {
//            time = 0;
//        }
//        View view = AdAppHelper.getInstance(getApplicationContext()).getRecommendNativeView(150);
//        if (AdAppHelper.getInstance(getApplicationContext()).isNativeLoaded(NativeAdType.APP_LIST) || view != null) {
//            if (mRlChild != null) {
//                mRlChild.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (mChildPhotoAdapter != null && mChildPhotoAdapter.mAdItemView != null) {
//                            mChildPhotoAdapter.mAdItemView.setTag(R.id.ad_which, NativeAdType.APP_LIST);
//                            if (!isVideoShow) {
//                                mChildPhotoAdapter.starAnimator(mChildPhotoAdapter.mAdItemView);
//                            } else {
//                                mChildPhotoAdapter.onlyAddAdView(mChildPhotoAdapter.mAdItemView);
//                            }
//                        }
//                    }
//                }, time);
//            }
//            if (mIsEdit && AdAppHelper.getInstance(getApplicationContext()).isNativeLoaded(NativeAdType.APP_LIST)) {
//                Firebase.getInstance(getApplicationContext()).logEvent("广告", "广告准备好", "Videos编辑界面native");
//            }
//        } else {
//            if (mIsEdit) {
//                Firebase.getInstance(getApplicationContext()).logEvent("广告", "广告没准备好", "Videos编辑界面native");
//            }
//        }
//        if (mIsEdit) {
//            Firebase.getInstance(getApplicationContext()).logEvent("广告", "show", "Videos编辑界面native");
//        }
//    }
}
