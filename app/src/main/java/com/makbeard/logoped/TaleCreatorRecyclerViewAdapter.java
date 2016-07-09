package com.makbeard.logoped;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.makbeard.logoped.model.TalePart;

import java.util.LinkedList;

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
    IFilePicker iFilePicker;

    public TaleCreatorRecyclerViewAdapter(LinkedList<TalePart> dataSet, IFilePicker iFilePicker) {
        mDataSet = new LinkedList<>(dataSet);
        this.iFilePicker = iFilePicker;
    }

    @Override
    public TaleCreatorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.talepart_cardview, parent, false);
        return new TaleCreatorViewHolder(view, new MyCustomEditTextListener());
    }

    @Override
    public void onBindViewHolder(TaleCreatorViewHolder holder, int position) {

        //Обрабатываем изменение текста
        holder.myCustomEditTextListener.updatePosition(position);
        holder.talePartEditText.setText(mDataSet.get(position).getText());

        if (!mDataSet.get(position).getImageLink().equals("")) {
            holder.talePartImageButton.setImageURI(
                    Uri.parse(mDataSet.get(position).getImageLink()));
        }

        if (!mDataSet.get(position).getAudioLink().equals("")) {
            int lastSlash = mDataSet.get(position).getAudioLink().lastIndexOf("/");
            holder.audioTitleTextView.setText(
                    mDataSet.get(position).getAudioLink().substring(lastSlash + 1));
        }

        holder.chooseAudioButton.setOnClickListener(
                v -> iFilePicker.pickFile(position, Const.MIME_TYPE_AUDIO));

        holder.talePartImageButton.setOnClickListener(
                v -> iFilePicker.pickFile(position, Const.MIME_TYPE_IMAGE));


    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    /**
     * Метод возвращает части рассказа как список
     * @return LinkedList частей рассказа
     */
    public LinkedList<TalePart> getItemsAsTaleParts() {
        return mDataSet;
    }

    /**
     * Метод обновляет данные об аудио
     * @param position позиция данных
     * @param audioLink ссылка на аудио
     */
    public void updateAudio(int position, String audioLink) {
        mDataSet.get(position).setAudioLink(audioLink);
        notifyItemChanged(position);
    }

    /**
     * Метод обновляет данные о картинке
     * @param position позиция данных
     * @param imageLink ссылка на картинку
     */
    public void updateImage(int position, String imageLink) {
        mDataSet.get(position).setImageLink(imageLink);
        notifyItemChanged(position);
    }

    class TaleCreatorViewHolder extends RecyclerView.ViewHolder {

        public MyCustomEditTextListener myCustomEditTextListener;

        @BindView(R.id.choose_audio_button)
        Button chooseAudioButton;

        @BindView(R.id.talepart_imagebutton)
        ImageView talePartImageButton;

        @BindView(R.id.talepart_text_edittext)
        EditText talePartEditText;

        @BindView(R.id.audio_textview)
        TextView audioTitleTextView;

        public TaleCreatorViewHolder(View itemView, MyCustomEditTextListener myCustomEditTextListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            //Подключаем отдельный TextWatcher
            this.myCustomEditTextListener = myCustomEditTextListener;
            talePartEditText.addTextChangedListener(myCustomEditTextListener);
        }
    }

    // we make TextWatcher to be aware of the position it currently works with
    // this way, once a new item is attached in onBindViewHolder, it will
    // update current position MyCustomEditTextListener, reference to which is kept by ViewHolder
    private class MyCustomEditTextListener implements TextWatcher {
        private int position;

        public void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            // no op
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            mDataSet.get(position).setText(charSequence.toString());
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // no op
        }
    }
}
