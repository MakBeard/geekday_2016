package com.makbeard.logoped;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.makbeard.logoped.R;
import com.makbeard.logoped.model.TaleModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Адаптер для сказок
 *
 * @author D.Makarov
 * Created 09.07.2016.
 */
public class TalesRecyclerViewAdapter
        extends RecyclerView.Adapter<TalesRecyclerViewAdapter.TalesViewHolder> {

    private static final String TAG = TalesRecyclerViewAdapter.class.getSimpleName();
    List<TaleModel> mDataList;
    Context mContext;

    public TalesRecyclerViewAdapter(Context context, List<TaleModel> dataList) {
        mDataList = new ArrayList<>(dataList);
        mContext = context;
    }

    @Override
    public TalesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.tale_cardview, parent, false);
        return new TalesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TalesViewHolder holder, int position) {
        holder.mTaleNameTextView.setText(mDataList.get(position).getName());
        if (!mDataList.get(position).getImageLink().equals("")) {
            // TODO: 09.07.2016 Разобраться с тормозами ленты  
/*
            Glide
                    .with(mContext)
                    .load(new File(Uri.parse(mDataList.get(position).getImageLink()).getPath()))
                    .centerCrop()
                    .into(holder.mTaleImageView);
*/

            holder.mTaleImageView.setImageURI(Uri.parse(mDataList.get(position).getImageLink()));
        }
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public void swap(List<TaleModel> newDataList) {
        mDataList.clear();
        mDataList.addAll(newDataList);
        notifyDataSetChanged();
    }

    class TalesViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tale_imageview)
        ImageView mTaleImageView;

        @BindView(R.id.tale_name_textview)
        TextView mTaleNameTextView;

        public TalesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.tale_cardview)
        protected void OnClickCardview() {
            // TODO: 09.07.2016 Обработать нажатие на сказку
            Log.d(TAG, "OnClickCardview: " + mDataList.get(getAdapterPosition()).getName());
            Log.d(TAG, "OnClickCardview: " + mDataList.get(getAdapterPosition()).getImageLink());
            /*
            Intent intent = new Intent(mContext, TalePlayerActivity.class);
            intent.putExtra(Const.EXTRA_NAME, mDataList.get(getAdapterPosition()).getName());
            mContext.startActivity(intent);
            */
        }
    }
}
