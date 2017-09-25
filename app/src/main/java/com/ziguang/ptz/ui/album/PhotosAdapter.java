package com.ziguang.ptz.ui.album;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Surface;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.ziguang.ptz.core.data.Head;
import com.ziguang.ptz.core.data.Media;
import com.ziguang.ptz.core.data.Span;
import com.ziguang.ptz.utils.DeviceInfoUtils;
import com.ziguang.ptz.utils.TimeUtils;
import com.ziguang.ptz.utils.UIUtils;
import com.ziguang.ptz.widget.PhotoLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaoliangtai on 2017/9/12.
 */

public class PhotosAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = PhotosAdapter.class.getSimpleName();

    private static final int TYPE_CONTENT = 1;

    private static final int TYPE_HEAD = 3;

    private List<Span> mDataList = new ArrayList<>();

    private int mCellSize;

    private Context mContext;


    public PhotosAdapter(Context context, int rotation) {
        mContext = context;
        if (rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270) {
            mCellSize = (DeviceInfoUtils.getScreenWidth() - UIUtils.dip2px(context, 18.5f)) / 8;
        } else if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180) {
            mCellSize = (DeviceInfoUtils.getScreenWidth() - UIUtils.dip2px(context, 18.5f)) / 4;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_HEAD:
                int width = DeviceInfoUtils.getScreenWidth();
                int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 36, parent.getResources().getDisplayMetrics());
                TextView dateTextView = new TextView(parent.getContext());
                dateTextView.setPadding((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, parent.getResources().getDisplayMetrics()), 0, 0, 0);
                RecyclerView.LayoutParams textParam = new RecyclerView.LayoutParams(width, height);
                dateTextView.setGravity(Gravity.CENTER_VERTICAL);
                dateTextView.setTextColor(Color.parseColor("#80ffffff"));
                dateTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                dateTextView.setLayoutParams(textParam);
                return new HeadViewHolder(dateTextView);
            case TYPE_CONTENT:
                PhotoLayout item = new PhotoLayout(parent.getContext());
                RecyclerView.LayoutParams photoParam = new RecyclerView.LayoutParams(-1, mCellSize);
                item.setLayoutParams(photoParam);
                return new ContentViewHolder(item);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case TYPE_HEAD:
                Head head = (Head) mDataList.get(position);
                String date = TimeUtils.applyFriendlyDate(mContext, head.getDate());
                date = date == null ? TimeUtils.getDate(head.getDate() * 1000) : date;
                ((HeadViewHolder) holder).head.setText(date);
                break;
            case TYPE_CONTENT:
                Media media = (Media) mDataList.get(position);
                PhotoLayout item = ((ContentViewHolder) holder).content;
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(mCellSize, mCellSize);
                item.getPhotoImageView().setLayoutParams(params);
                item.getPhotoImageView().setScaleType(ImageView.ScaleType.CENTER_CROP);
                String path = media.getPath() == null ? "" : media.getPath();
                if (media.getMediaType() == Media.TYPE_IMAGE) {
                    item.showAsPhoto();
                    DraweeController controller = getDraweeController(item, path);
                    item.getPhotoImageView().setController(controller);
                } else if (media.getMediaType() == Media.TYPE_VIDEO) {
                    item.showAsVideo(TimeUtils.getDurationChinese((int) media.getDuration()));
                    DraweeController controller = getDraweeController(item, path);
                    item.getPhotoImageView().setController(controller);
                }
                break;
            default:
                break;
        }
    }

    private DraweeController getDraweeController(PhotoLayout item, String path) {
        ImageRequest req = ImageRequestBuilder.newBuilderWithSource(
                Uri.fromFile(new File(path)))
                .setResizeOptions(new ResizeOptions(mCellSize, mCellSize))
                .build();
        return Fresco.newDraweeControllerBuilder()
                .setControllerListener(new BaseControllerListener<ImageInfo>())
                .setOldController(item.getPhotoImageView().getController())
                .setImageRequest(req)
                .setAutoPlayAnimations(false)
                .build();
    }

    @Override
    public int getItemViewType(int position) {
        Span span = mDataList.get(position);
        return span.getSpan() == Span.SPAN_CONTENT ? TYPE_CONTENT : TYPE_HEAD;
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }


    public void addAll(List<Span> dataList) {
        if (dataList == null) {
            return;
        }
        mDataList.clear();
        mDataList.addAll(dataList);
        notifyDataSetChanged();
    }

    static class HeadViewHolder extends RecyclerView.ViewHolder {
        TextView head;
        public HeadViewHolder(TextView itemView) {
            super(itemView);
            head = itemView;
        }
    }

    static class ContentViewHolder extends RecyclerView.ViewHolder {
        PhotoLayout content;
        public ContentViewHolder(PhotoLayout itemView) {
            super(itemView);
            content = itemView;
        }
    }
}
