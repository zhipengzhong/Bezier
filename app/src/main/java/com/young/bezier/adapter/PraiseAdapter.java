package com.young.bezier.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.young.bezier.R;

import java.util.ArrayList;

/**
 * Created by 钟志鹏 on 2017/9/9.
 */

public class PraiseAdapter extends RecyclerView.Adapter {

    private static final String TAG = "PraiseAdapter";
    private ArrayList<Boolean> mList;
    private OnPraiseListener mPraiseLinstener;

    public PraiseAdapter(ArrayList<Boolean> list) {
        mList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_praise, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((Holder) holder).bindView(position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setOnPraiseLinstener(OnPraiseListener praiseLinstener) {
        mPraiseLinstener = praiseLinstener;
    }

    class Holder extends RecyclerView.ViewHolder {

        public ImageView mPraise;

        public Holder(View itemView) {
            super(itemView);
            mPraise = (ImageView) itemView.findViewById(R.id.praise);
        }

        public void bindView(final int position) {
            mPraise.setImageResource(mList.get(position) ? R.drawable.like_blue : R.drawable.like);
            mPraise.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean element = !mList.get(position);
                    mList.set(position, element);
                    mPraise.setImageResource(mList.get(position) ? R.drawable.like_blue : R.drawable.like);
                    int[] out = new int[2];
                    mPraise.getLocationOnScreen(out);
                    if (element && mPraiseLinstener != null) {
                        mPraiseLinstener.onPraise(out[0], out[1], mPraise.getWidth(), mPraise.getHeight());
                    }
                    Log.d(TAG, "onClick: " + out[0] + "|" + out[1]);
                }
            });
        }
    }

    public interface OnPraiseListener {
        void onPraise(int x, int y, int width, int height);
    }
}
