package com.makbeard.logoped;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.makbeard.logoped.model.ReadStatisticModel;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Hp on 10.07.2016.
 */
public class StatisticRecyclerViewAdapter
        extends RecyclerView.Adapter<StatisticRecyclerViewAdapter.StatisticsViewHolder> {

    LinkedList<ReadStatisticModel> mDataSet;

    public StatisticRecyclerViewAdapter(List<ReadStatisticModel> dataSet) {
        mDataSet = new LinkedList<>(dataSet);
    }

    @Override
    public StatisticsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.statistics_cardview, parent, false);
        return new StatisticsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StatisticsViewHolder holder, int position) {

        holder.taleNameTextView.setText(
                String.valueOf(mDataSet.get(position).getTaleName()));

        holder.wrongAttempsTextView.setText(
                String.valueOf(mDataSet.get(position).getWrongAttemps()));

        long spentTime = TimeUnit.MILLISECONDS.toSeconds(mDataSet.get(position).getTimeSpent());

        holder.timeSpentTextView.setText(String.valueOf(spentTime) + " секунд");
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public void swap(List<ReadStatisticModel> newDataList) {
        mDataSet.clear();
        mDataSet.addAll(newDataList);
        notifyDataSetChanged();
    }

    class StatisticsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tale_name_textview)
        TextView taleNameTextView;

        @BindView(R.id.time_spent_textview)
        TextView timeSpentTextView;

        @BindView(R.id.wrong_attemps_textview)
        TextView wrongAttempsTextView;

        public StatisticsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

