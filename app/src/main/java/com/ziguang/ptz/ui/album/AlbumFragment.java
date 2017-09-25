package com.ziguang.ptz.ui.album;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;

import com.ziguang.ptz.R;
import com.ziguang.ptz.base.ViewPagerFragment;
import com.ziguang.ptz.core.data.Directory;
import com.ziguang.ptz.core.data.Head;
import com.ziguang.ptz.core.data.Media;
import com.ziguang.ptz.core.data.MediaLocalDataSource;
import com.ziguang.ptz.core.data.MediaRepository;
import com.ziguang.ptz.core.data.MediaSystemDataSource;
import com.ziguang.ptz.core.data.Span;
import com.ziguang.ptz.rx.NCSubscriber;
import com.ziguang.ptz.utils.UIUtils;
import com.ziguang.ptz.widget.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by zhaoliangtai on 2017/9/12.
 */

public class AlbumFragment extends ViewPagerFragment {

    private static final String TAG = AlbumFragment.class.getSimpleName();

    private int mContentType;

    private MediaRepository mMediaRepository;

    private SwipeRefreshLayout mRefreshLayout;

    private RecyclerView mRecyclerView;

    private GridLayoutManager mLayoutManager;

    private CompositeSubscription mCompositeSubscription;

    private PhotosAdapter mPhotosAdapter;

    private int mRotation = -1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMediaRepository = MediaRepository.getInstance(MediaSystemDataSource.getInstance(),
                MediaLocalDataSource.getInstance());
        mCompositeSubscription = new CompositeSubscription();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_album, null);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.refresh_layout);
        initGridView();
        return rootView;
    }

    private void initGridView() {
        int offsetPadding = UIUtils.dip2px(getContext(), 2);
        int gridPadding = UIUtils.dip2px(getContext(), 8);
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(gridPadding, 3));
        mRecyclerView.setPadding(offsetPadding, offsetPadding, offsetPadding, offsetPadding);
        mRotation = getActivity().getWindowManager().getDefaultDisplay().getRotation();
        if (mRotation == Surface.ROTATION_90 || mRotation == Surface.ROTATION_270) {
            mLayoutManager = new GridLayoutManager(getContext(), 6);
        } else if (mRotation == Surface.ROTATION_0 || mRotation == Surface.ROTATION_180) {
            mLayoutManager = new GridLayoutManager(getContext(), 3);
        }
        mPhotosAdapter = new PhotosAdapter(getContext(), mRotation);
        mRecyclerView.setAdapter(mPhotosAdapter);
    }

    public void setContentType(int contentType) {
        mContentType = contentType;
    }



    @Override
    public void refresh() {
        Log.i(TAG, "refresh");
        mRefreshLayout.setRefreshing(true);
        switch (mContentType) {
            case Media.TYPE_BOTH:
                loadMedias();
                break;
            case Media.TYPE_IMAGE:
                loadImages();
                break;
            case Media.TYPE_VIDEO:
                loadVideos();
                break;
            default:
                loadMedias();
                break;
        }
    }

    private void loadVideos() {
        mCompositeSubscription.clear();
        Subscription subscriber = mMediaRepository.getVideos()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NCSubscriber<Directory>() {
                    @Override
                    public void doOnNext(Directory directory) {
                        if (null == directory) {
                            return;
                        }
                        showPhotos(directory.getMedias());
                    }

                    @Override
                    public void doOnError(Throwable e) {
                        e.printStackTrace();
                    }
                });
        mCompositeSubscription.add(subscriber);
    }

    private void loadImages() {
        mCompositeSubscription.clear();
        Subscription subscription = mMediaRepository.getImages()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NCSubscriber<Directory>() {
                    @Override
                    public void doOnNext(Directory directory) {
                        if (null == directory) {
                            return;
                        }
                        showPhotos(directory.getMedias());
                    }

                    @Override
                    public void doOnError(Throwable e) {
                        e.printStackTrace();
                    }
                });
        mCompositeSubscription.add(subscription);
    }

    private void loadMedias() {
        mCompositeSubscription.clear();
        Subscription subscription = mMediaRepository.getMedias()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NCSubscriber<Directory>() {
                    @Override
                    public void doOnNext(Directory directory) {
                        if (null == directory) {
                            return;
                        }
                        showPhotos(directory.getMedias());
                    }

                    @Override
                    public void doOnError(Throwable e) {
                        e.printStackTrace();
                    }
                });
        mCompositeSubscription.add(subscription);
    }

    private void showPhotos(Map<String, List<Media>> mediaGroup) {
        if (mediaGroup == null) {
            return;
        }
        final List<Span> dataList = new ArrayList<>();
        for (Map.Entry<String, List<Media>> entry : mediaGroup.entrySet()) {
            Head head = new Head();
            head.setDate(entry.getValue().get(0).getDate());
            dataList.add(head);
            List<Media> values = entry.getValue();
            dataList.addAll(values);
        }
        mPhotosAdapter.addAll(dataList);
        mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (mRotation == Surface.ROTATION_90 || mRotation == Surface.ROTATION_270) {
                    return dataList.get(position).getSpan() == Span.SPAN_HEAD ? 6 : 1;
                } else if (mRotation == Surface.ROTATION_0 || mRotation == Surface.ROTATION_180) {
                    return dataList.get(position).getSpan();
                }
                return dataList.get(position).getSpan();
            }
        });
        mRecyclerView.setLayoutManager(mLayoutManager);
        if (mRefreshLayout.isRefreshing())
            mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void stopRefresh() {
        Log.i(TAG, "stopRefresh");
        mCompositeSubscription.clear();
        if (mRefreshLayout.isRefreshing())
            mRefreshLayout.setRefreshing(false);
    }
}
