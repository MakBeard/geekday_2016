package com.makbeard.logoped;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.makbeard.logoped.R;
import com.makbeard.logoped.model.TaleModel;

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
            Glide
                    .with(mContext)
                    .load(mDataList.get(position).getImageLink())
                    .into(holder.mTaleImageView);
        } else {

            Glide
                    .with(mContext)
                    .load("https://img-fotki.yandex.ru/get/5111/2839712.33/0_14bfb3_737f575d_orig")
                    .into(holder.mTaleImageView);
            /*Picasso
                    .with(mContext)
                    .load(R.drawable.placeholder)
                   // .resize(220,250)
                    .into(holder.mTaleImageView);*/

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
            /*
            Intent intent = new Intent(mContext, TalePlayerActivity.class);
            intent.putExtra(Const.EXTRA_NAME, mDataList.get(getAdapterPosition()).getName());
            mContext.startActivity(intent);
            */
        }
    }
}
