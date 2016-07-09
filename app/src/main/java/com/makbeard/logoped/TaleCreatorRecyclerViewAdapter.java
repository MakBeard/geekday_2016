package com.makbeard.logoped;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.makbeard.logoped.model.TalePart;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Адаптер для создания сказки
 *
 * Created 09.07.2016.
 */
public class TaleCreatorRecyclerViewAdapter
        extends RecyclerView.Adapter<TaleCreatorRecyclerViewAdapter.TaleCreatorViewHolder> {

    LinkedList<TalePart> mDataSet;

    public TaleCreatorRecyclerViewAdapter(LinkedList<TalePart> dataSet) {
        mDataSet = new LinkedList<>(dataSet);
    }

    @Override
    public TaleCreatorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.talepart_cardview, parent, false);
        return new TaleCreatorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TaleCreatorViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public LinkedList<TalePart> getItemsAsTaleParts() {
        return mDataSet;
    }

    class TaleCreatorViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.choose_audio_button)
        Button chooseAudioButton;

        @BindView(R.id.talepart_imagebutton)
        ImageView talePartImageView;

        @BindView(R.id.talepart_text_edittext)
        EditText talePartEditText;

        public TaleCreatorViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
