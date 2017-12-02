package com.example.liuan.screenview.adapter;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.daimajia.androidanimations.library.YoYo;
import com.example.liuan.screenview.R;
import com.example.liuan.screenview.activity.AddActivity;
import com.example.liuan.screenview.activity.EditActivity;
import com.example.liuan.screenview.activity.ShowMediaActivity;
import com.example.liuan.screenview.bean.MediaEntity;
import com.example.liuan.screenview.bean.MediaItem;
import com.example.liuan.screenview.utils.AndroidUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.halfbit.pinnedsection.PinnedSectionListView;

import static com.example.liuan.screenview.fragment.PhotosFragment.PHOTO_EDIT_REQUEST_CODE;


public class ItemChildPhotoAdapter extends BaseAdapter implements PinnedSectionListView.PinnedSectionListAdapter, View.OnClickListener, View.OnLongClickListener {

    private List<MediaEntity> objects = new ArrayList<>();
    private Map<String, List<MediaItem>> mMap;

    private Context context;
    private Fragment mFragment;
    private Activity mActivity;
    private LayoutInflater layoutInflater;
    private boolean mIsEditor;
    private boolean mIsVideo;
    private YoYo.YoYoString mYoYoString;
    private ValueAnimator mValueAnimator;
    private boolean mIsShow = false;
    public FrameLayout mAdItemView;

    public void setClickListener(OnItemClickListener clickListener) {
        mClickListener = clickListener;
    }

    private OnItemClickListener mClickListener;

    public ItemChildPhotoAdapter(Context context, List<MediaEntity> list, Map<String, List<MediaItem>> map, boolean isEditor, boolean isVideo) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        objects = list;
        mMap = map;
        mIsEditor = isEditor;
        mIsVideo = isVideo;
    }

    public void setActivity(Activity activity) {
        mActivity = activity;
    }

    public void setFragment(Fragment fragment) {
        mFragment = fragment;
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position) != null) {
            return getItem(position).getType();
        }
        return -1;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public MediaEntity getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        ViewHolder viewHolder = null;
        ViewHolderAd viewHolderAd = null;
        ViewHolderTitle viewHolderTitle = null;
        if (convertView == null) {
            switch (type) {
                case 0:
                    convertView = layoutInflater.inflate(R.layout.item_child_photo_title, parent, false);
                    viewHolderTitle = new ViewHolderTitle(convertView);
                    convertView.setTag(viewHolderTitle);
                    break;
                case 1:
                    convertView = layoutInflater.inflate(R.layout.item_child_photo, parent, false);
                    viewHolder = new ViewHolder(convertView);
                    convertView.setTag(viewHolder);
                    break;
//                case 2:
//                    convertView = layoutInflater.inflate(R.layout.item_ad_view_2, parent, false);
//                    viewHolderAd = new ViewHolderAd(convertView);
//                    convertView.setTag(viewHolder);
//                    break;
            }

        } else {
            switch (type) {
                case 0:
                    viewHolderTitle = (ViewHolderTitle) convertView.getTag();
                    break;
                case 1:
                    viewHolder = (ViewHolder) convertView.getTag();
                    break;
//                case 2:
//                    viewHolderAd = (ViewHolderAd) convertView.getTag();
//                    break;
            }
        }
        switch (type) {
            case 0:
                initTitleView(getItem(position), viewHolderTitle, position);
                break;
            case 1:
                initView(getItem(position), viewHolder);
                break;
//            case 2:
//                initViewAd(convertView, viewHolderAd);
//                if (viewHolderAd != null) {
//                    mAdItemView = viewHolderAd.mAdView;
//                }
//                break;
        }
        return convertView;
    }
//
//    private void initViewAd(View convertView, final ViewHolderAd viewHolderAd) {
//        if (!mIsShow) {
//            if (mAdItemView != null) {
//                mAdItemView.getLayoutParams().height = 0;
//                mAdItemView.requestLayout();
//            }
//            mIsShow = true;
//        }
//    }

//    public void starAnimator(final FrameLayout adFrameLayout) {
//        mValueAnimator = ValueAnimator.ofFloat(0, context.getResources().getDimension(R.dimen.native_2_height));
//        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                float h = (Float) mValueAnimator.getAnimatedValue();
//                adFrameLayout.getLayoutParams().height = (int) h;
//                adFrameLayout.requestLayout();
//            }
//        });
//        mValueAnimator.addListener(new AnimatorListenerAdapter() {
//                                       @Override
//                                       public void onAnimationEnd(Animator animation) {
//                                           super.onAnimationEnd(animation);
//                                           if (AdAppHelper.getInstance(context).isNativeLoaded((Integer) adFrameLayout.getTag(R.id.ad_which)) || AdAppHelper.getInstance(getApplicationContext()).getRecommendNativeView(150) != null) {
//                                               FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                                               layoutParams.gravity = Gravity.CENTER;
//                                               View adView = null;
//                                               if (AdAppHelper.getInstance(getApplicationContext()).isNativeLoaded((Integer) adFrameLayout.getTag(R.id.ad_which))) {
//                                                   adView = AdAppHelper.getInstance(getApplicationContext()).getNative((Integer) adFrameLayout.getTag(R.id.ad_which));
//                                               } else {
//                                                   adView = AdAppHelper.getInstance(getApplicationContext()).getRecommendNativeView(150);
//                                               }
//                                               if (adView != null) {
//                                                   adFrameLayout.removeAllViews();
//                                                   adFrameLayout.addView(adView, layoutParams);
//                                                   adFrameLayout.setVisibility(adView.VISIBLE);
//                                               }
//
//                                               if (mYoYoString != null) {
//                                                   mYoYoString.stop(true);
//                                               }
//                                               mYoYoString = YoYo.with(Techniques.FadeInDown)
//                                                       .duration(300)
//                                                       .withListener(new AnimatorListenerAdapter() {
//                                                           @Override
//                                                           public void onAnimationEnd(Animator animation) {
//                                                               super.onAnimationEnd(animation);
//                                                               notifyDataSetChanged();
//                                                           }
//                                                       })
//                                                       .interpolate(new AccelerateDecelerateInterpolator())
//                                                       .playOn(adFrameLayout);
//                                           }
//                                       }
//                                   }
//
//        );
//        mValueAnimator.setDuration(200);
//        adFrameLayout.setVisibility(View.VISIBLE);
//        mValueAnimator.start();
//    }
//
//    public void onlyAddAdView(FrameLayout adFrameLayout) {
//        if (AdAppHelper.getInstance(context).isNativeLoaded((Integer) adFrameLayout.getTag(R.id.ad_which)) || AdAppHelper.getInstance(getApplicationContext()).getRecommendNativeView(150) != null) {
//            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            layoutParams.gravity = Gravity.CENTER;
//            View adView = null;
//            if (AdAppHelper.getInstance(getApplicationContext()).isNativeLoaded((Integer) adFrameLayout.getTag(R.id.ad_which))) {
//                adView = AdAppHelper.getInstance(getApplicationContext()).getNative((Integer) adFrameLayout.getTag(R.id.ad_which));
//            } else {
//                adView = AdAppHelper.getInstance(getApplicationContext()).getRecommendNativeView(150);
//            }
//            if (adView != null) {
//                adFrameLayout.removeAllViews();
//                adFrameLayout.addView(adView, layoutParams);
//                adFrameLayout.setVisibility(adView.VISIBLE);
//            }
////                View adView = AdAppHelper.getInstance(getApplicationContext()).getNative((Integer) adFrameLayout.getTag(R.id.ad_which));
////                if (adView != null) {
////                    adFrameLayout.removeAllViews();
////                    adFrameLayout.addView(adView, layoutParams);
////                    adFrameLayout.setVisibility(adView.VISIBLE);
////                }
//        }
//    }

    private void initTitleView(MediaEntity item, ViewHolderTitle viewHolderTitle, int position) {
        viewHolderTitle.tvTitle.setText(item.getTitle() + "(" + mMap.get(item.getParentPath()).size() + ")");
        viewHolderTitle.titleLayout.setOnClickListener(this);
        viewHolderTitle.titleLayout.setTag(R.id.pos, position);
        if (mIsEditor) {
            viewHolderTitle.selectView.setVisibility(View.VISIBLE);
            item.setSelect(false);
            viewHolderTitle.selectView.setSelected(false);
            for (MediaItem photoItem : mMap.get(item.getParentPath())) {
                if (photoItem.isSelect()) {
                    viewHolderTitle.selectView.setSelected(true);
                    item.setSelect(true);
                    break;
                }
            }
            viewHolderTitle.selectView.setTag(R.id.entity, item);
            viewHolderTitle.selectView.setOnClickListener(this);
        } else {
            viewHolderTitle.selectView.setVisibility(View.GONE);
        }
    }


    private void initView(MediaEntity item, ViewHolder viewHolder) {
        List<MediaItem> list = item.getPhotoItems();
        if (mIsVideo) {
            viewHolder.time1.setVisibility(View.VISIBLE);
            viewHolder.time2.setVisibility(View.VISIBLE);
            viewHolder.time3.setVisibility(View.VISIBLE);
        } else {
            viewHolder.time1.setVisibility(View.GONE);
            viewHolder.time2.setVisibility(View.GONE);
            viewHolder.time3.setVisibility(View.GONE);
        }
//        if (!mIsEditor) {
        viewHolder.ivSelect1.setVisibility(View.GONE);
        viewHolder.ivSelect2.setVisibility(View.GONE);
        viewHolder.ivSelect3.setVisibility(View.GONE);
//        }
        viewHolder.iv1.setOnClickListener(this);
        viewHolder.iv2.setOnClickListener(this);
        viewHolder.iv3.setOnClickListener(this);
        viewHolder.iv1.setOnLongClickListener(this);
        viewHolder.iv2.setOnLongClickListener(this);
        viewHolder.iv3.setOnLongClickListener(this);
        if (list.size() == 1) {
            setImageSrc(list.get(0), viewHolder.iv1);
            viewHolder.iv1.setTag(R.id.entity, list.get(0));
            viewHolder.iv1.setBackgroundResource(R.drawable.iv_selector);
            viewHolder.ivSelect1.setVisibility(list.get(0).isSelect() ? View.VISIBLE : View.GONE);
            viewHolder.iv2.setImageDrawable(null);
            viewHolder.iv2.setTag(R.id.entity, null);
            viewHolder.iv2.setBackgroundDrawable(null);
            viewHolder.ivSelect2.setVisibility(View.GONE);
            viewHolder.iv3.setImageDrawable(null);
            viewHolder.iv3.setTag(R.id.entity, null);
            viewHolder.iv3.setBackgroundDrawable(null);
            viewHolder.ivSelect3.setVisibility(View.GONE);
            viewHolder.iv2.setTag(R.id.url, null);
            viewHolder.iv3.setTag(R.id.url, null);
            viewHolder.iv1.setEnabled(true);
            viewHolder.iv2.setEnabled(false);
            viewHolder.iv3.setEnabled(false);
            if (mIsVideo) {
                viewHolder.time1.setText(AndroidUtils.getVideoTime(list.get(0).getDuring()));
                viewHolder.time1.setVisibility(View.VISIBLE);
                viewHolder.time2.setVisibility(View.GONE);
                viewHolder.time3.setVisibility(View.GONE);
            }

        } else if (list.size() == 2) {
            setImageSrc(list.get(0), viewHolder.iv1);
            viewHolder.iv1.setTag(R.id.entity, list.get(0));
            viewHolder.iv1.setBackgroundResource(R.drawable.iv_selector);
//            viewHolder.ivSelect1.setSelected(list.get(0).isSelect());
            viewHolder.ivSelect1.setVisibility(list.get(0).isSelect() ? View.VISIBLE : View.GONE);

            setImageSrc(list.get(1), viewHolder.iv2);
            viewHolder.iv2.setTag(R.id.entity, list.get(1));
            viewHolder.iv2.setBackgroundResource(R.drawable.iv_selector);
//            viewHolder.ivSelect2.setSelected(list.get(1).isSelect());
            viewHolder.ivSelect2.setVisibility(list.get(1).isSelect() ? View.VISIBLE : View.GONE);

            viewHolder.iv3.setImageDrawable(null);
            viewHolder.iv3.setTag(R.id.entity, null);
            viewHolder.iv3.setBackgroundDrawable(null);
            viewHolder.ivSelect3.setVisibility(View.GONE);
            viewHolder.iv3.setTag(R.id.url, null);
            viewHolder.iv1.setEnabled(true);
            viewHolder.iv2.setEnabled(true);
            viewHolder.iv3.setEnabled(false);
            if (mIsVideo) {
                viewHolder.time1.setText(AndroidUtils.getVideoTime(list.get(0).getDuring()));
                viewHolder.time1.setVisibility(View.VISIBLE);
                viewHolder.time2.setText(AndroidUtils.getVideoTime(list.get(1).getDuring()));
                viewHolder.time2.setVisibility(View.VISIBLE);
                viewHolder.time3.setVisibility(View.GONE);
            }
        } else if (list.size() == 3) {
            setImageSrc(list.get(0), viewHolder.iv1);
            viewHolder.iv1.setTag(R.id.entity, list.get(0));
            viewHolder.iv1.setBackgroundResource(R.drawable.iv_selector);
//            viewHolder.ivSelect1.setSelected(list.get(0).isSelect());
            viewHolder.ivSelect1.setVisibility(list.get(0).isSelect() ? View.VISIBLE : View.GONE);

            setImageSrc(list.get(1), viewHolder.iv2);
            viewHolder.iv2.setTag(R.id.entity, list.get(1));
            viewHolder.iv2.setBackgroundResource(R.drawable.iv_selector);
//            viewHolder.ivSelect2.setSelected(list.get(1).isSelect());
            viewHolder.ivSelect2.setVisibility(list.get(1).isSelect() ? View.VISIBLE : View.GONE);

            setImageSrc(list.get(2), viewHolder.iv3);
            viewHolder.iv3.setTag(R.id.entity, list.get(2));
            viewHolder.iv3.setBackgroundResource(R.drawable.iv_selector);
//            viewHolder.ivSelect3.setSelected(list.get(2).isSelect());
            viewHolder.ivSelect3.setVisibility(list.get(2).isSelect() ? View.VISIBLE : View.GONE);

            viewHolder.iv1.setEnabled(true);
            viewHolder.iv2.setEnabled(true);
            viewHolder.iv3.setEnabled(true);
            if (mIsVideo) {
                viewHolder.time1.setText(AndroidUtils.getVideoTime(list.get(0).getDuring()));
                viewHolder.time1.setVisibility(View.VISIBLE);
                viewHolder.time2.setText(AndroidUtils.getVideoTime(list.get(1).getDuring()));
                viewHolder.time2.setVisibility(View.VISIBLE);
                viewHolder.time3.setText(AndroidUtils.getVideoTime(list.get(2).getDuring()));
                viewHolder.time3.setVisibility(View.VISIBLE);
            }
        }
    }

    public void setImageSrc(MediaItem item, ImageView imageView) {
        if (item != null && !TextUtils.isEmpty(item.getPath()) && !item.getPath().equals(imageView.getTag(R.id.url))) {
            Glide.with(context)
                    .load(Uri.fromFile(new File(item.getPath())))
                    .asBitmap()
                    .centerCrop()
                    .placeholder(R.drawable.ic_photos)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(imageView);
            imageView.setTag(R.id.url, item.getPath());
        }
    }

    @Override
    public boolean isItemViewTypePinned(int viewType) {
        return viewType == 0;
    }

    @Override
    public void onClick(View v) {
        MediaItem tag = null;
        if (v.getTag(R.id.entity) instanceof MediaItem) {
            tag = (MediaItem) v.getTag(R.id.entity);
        }
        switch (v.getId()) {
            case R.id.iv_1:
            case R.id.iv_2:
            case R.id.iv_3:
                if (tag != null) {
                    if (mIsEditor) {
                        tag.setSelect(!tag.isSelect());
                        if (context instanceof EditActivity) {
                            ((EditActivity) context).addSelectItem(tag);
                        //    Firebase.getInstance(context).logEvent(EditActivity.CATEGORY, EditActivity.ACTION, (tag.isSelect() ? "选中" : "未选中") + (mIsVideo ? "视频" : "图片"));
                        } else if (context instanceof AddActivity) {
                            ((AddActivity) context).addSelectItem(tag);
                      //      Firebase.getInstance(context).logEvent(AddActivity.CATEGORY, AddActivity.ACTION, (tag.isSelect() ? "选中" : "未选中") + (mIsVideo ? "视频" : "图片"));
                        }
                        notifyDataSetChanged();
                    } else {
                        if (mMap.containsKey(tag.getRealParentPath())) {
                            Intent intent = new Intent(context, ShowMediaActivity.class);
                            intent.putExtra("type", mIsVideo);
                            intent.putExtra("lock", true);
                            try {
                                intent.putParcelableArrayListExtra("entity", (ArrayList<? extends Parcelable>) mMap.get(tag.getRealParentPath()));
                                intent.putExtra("position", mMap.get(tag.getRealParentPath()).indexOf(tag));
                                if (mFragment != null) {
                                    mFragment.startActivityForResult(intent, PHOTO_EDIT_REQUEST_CODE);
                                } else if (mActivity != null) {
                                    mActivity.startActivityForResult(intent, PHOTO_EDIT_REQUEST_CODE);
                                } else {
                                    context.startActivity(intent);
                                }
                            } catch (Exception e) {
                                List<MediaItem> items = mMap.get(tag.getRealParentPath());
                                int pos = items.indexOf(tag);
                                int count = 20;
                                int start = 0;
                                int end = 0;
                                if (pos + count / 2 > items.size() - 1) {
                                    end = items.size();
                                } else {
                                    end = pos + count / 2;
                                }
                                if (pos - count / 2 < 0) {
                                    start = 0;
                                } else {
                                    start = pos - count / 2;
                                }
                                ArrayList<MediaItem> list = new ArrayList<>();
                                list.addAll(items.subList(start, end));
                                pos = list.indexOf(tag);
                                if (pos < 0) {
                                    pos = 0;
                                }
                                intent.putParcelableArrayListExtra("entity", list);
                                intent.putExtra("position", pos);
                                if (mFragment != null) {
                                    mFragment.startActivityForResult(intent, PHOTO_EDIT_REQUEST_CODE);
                                } else if (mActivity != null) {
                                    mActivity.startActivityForResult(intent, PHOTO_EDIT_REQUEST_CODE);
                                } else {
                                    context.startActivity(intent);
                                }
//                                e.printStackTrace();
                            }
                          //  Firebase.getInstance(context).logEvent(mIsVideo ? VideosFragment.CATEGORY : PhotosFragment.CATEGORY, PhotosFragment.ACTION, "详细浏览" + (mIsVideo ? "视频" : "图片"));

                        }
                    }
                }
                break;
            case R.id.iv_select:
                if (mIsEditor) {
                    MediaEntity entity = (MediaEntity) v.getTag(R.id.entity);
                    if (entity != null) {
                        for (MediaItem item : mMap.get(entity.getParentPath())) {
                            item.setSelect(!v.isSelected());
                            if (context instanceof EditActivity) {
                                ((EditActivity) context).addSelectItem(item);
                              //  Firebase.getInstance(context).logEvent(EditActivity.CATEGORY, EditActivity.ACTION, (item.isSelect() ? "全选" : "全不选") + (mIsVideo ? "视频" : "图片"));
                            } else if (context instanceof AddActivity) {
                                ((AddActivity) context).addSelectItem(item);
                           //     Firebase.getInstance(context).logEvent(AddActivity.CATEGORY, AddActivity.ACTION, (item.isSelect() ? "全选" : "全不选") + (mIsVideo ? "视频" : "图片"));

                            }
                        }
                    }
                }
                notifyDataSetInvalidated();
                break;
            case R.id.ll_title:
                if (mClickListener != null) {
                    mClickListener.onItemClick(v, (Integer) v.getTag(R.id.pos));
                }
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (mIsEditor) {
            MediaItem entity = (MediaItem) v.getTag(R.id.entity);
            if (mMap.containsKey(entity.getRealParentPath())) {
                Intent intent = new Intent(context, ShowMediaActivity.class);
                intent.putExtra("type", mIsVideo);
                intent.putExtra("lock", false);
                try {
                    intent.putParcelableArrayListExtra("entity", (ArrayList<? extends Parcelable>) mMap.get(entity.getRealParentPath()));
                    intent.putExtra("position", mMap.get(entity.getRealParentPath()).indexOf(entity));
                    if (mFragment != null) {
                        mFragment.startActivityForResult(intent, PHOTO_EDIT_REQUEST_CODE);
                    } else if (mActivity != null) {
                        mActivity.startActivityForResult(intent, PHOTO_EDIT_REQUEST_CODE);
                    } else {
                        context.startActivity(intent);
                    }
                } catch (Exception e) {
                    List<MediaItem> items = mMap.get(entity.getRealParentPath());
                    int pos = items.indexOf(entity);
                    int count = 20;
                    int start = 0;
                    int end = 0;
                    if (pos + count / 2 > items.size() - 1) {
                        end = items.size();
                    } else {
                        end = pos + count / 2;
                    }
                    if (pos - count / 2 < 0) {
                        start = 0;
                    } else {
                        start = pos - count / 2;
                    }
                    ArrayList<MediaItem> list = new ArrayList<>();
                    list.addAll(items.subList(start, end));
                    pos = list.indexOf(entity);
                    if (pos < 0) {
                        pos = 0;
                    }
                    intent.putParcelableArrayListExtra("entity", list);
                    intent.putExtra("position", pos);
                    if (mFragment != null) {
                        mFragment.startActivityForResult(intent, PHOTO_EDIT_REQUEST_CODE);
                    } else if (mActivity != null) {
                        mActivity.startActivityForResult(intent, PHOTO_EDIT_REQUEST_CODE);
                    } else {
                        context.startActivity(intent);
                    }
                }
             //   Firebase.getInstance(context).logEvent(EditActivity.CATEGORY, EditActivity.ACTION, "详细浏览_" + (mIsVideo ? "视频" : "图片"));

            }
        }
        return true;
    }

    static class ViewHolder {
        @Bind(R.id.iv_1)
        ImageView iv1;
        @Bind(R.id.iv_2)
        ImageView iv2;
        @Bind(R.id.iv_3)
        ImageView iv3;
        @Bind(R.id.iv_select_1)
        ImageView ivSelect1;
        @Bind(R.id.iv_select_2)
        ImageView ivSelect2;
        @Bind(R.id.iv_select_3)
        ImageView ivSelect3;
        @Bind(R.id.tv_time1)
        TextView time1;
        @Bind(R.id.tv_time2)
        TextView time2;
        @Bind(R.id.tv_time3)
        TextView time3;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static class ViewHolderTitle {
        @Bind(R.id.tv_title)
        TextView tvTitle;
        @Bind(R.id.iv_select)
        ImageView selectView;
        @Bind(R.id.ll_title)
        LinearLayout titleLayout;

        ViewHolderTitle(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static class ViewHolderAd {
        @Bind(R.id.ad_view)
        FrameLayout mAdView;

        ViewHolderAd(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        if (observer != null) {
            super.unregisterDataSetObserver(observer);
        }
    }
}
