package com.ziguang.ptz.ui.album;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ziguang.ptz.core.data.Span;

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



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }


    static class HeadViewHolder extends RecyclerView.ViewHolder {
        TextView head;
        public HeadViewHolder(TextView itemView) {
            super(itemView);
            head = itemView;
        }
    }

    static class ContentViewHolder extends RecyclerView.ViewHolder {

        public ContentViewHolder(View itemView) {
            super(itemView);
        }
    }
}
