package com.makbeard.logoped;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.makbeard.logoped.db.DbOpenHelper;
import com.makbeard.logoped.model.TaleModel;
import com.makbeard.logoped.model.TaleModelSQLiteTypeMapping;
import com.makbeard.logoped.model.TalePart;
import com.pushtorefresh.storio.sqlite.impl.DefaultStorIOSQLite;

import java.util.LinkedList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Activity добавления рассказа
 */
public class TaleCreatingActivity extends AppCompatActivity implements IFilePicker {


    private static final int GENERAL_IMAGE_CHOOSER = 0;
    private static final int TALEPART_AUDIO_CHOOSER = 1;
    private static final int TALEPART_IMAGE_CHOOSER = 2;
    private static final String TAG = TaleCreatingActivity.class.getSimpleName();
    private Uri mTaleImageURI=Uri.EMPTY;
    private int mAdapterPostitionForUpdate;

    @BindView(R.id.tale_creator_recyclerview)
    RecyclerView mTaleCreatorRecyclerView;

    @BindView(R.id.tale_image_imagebutton)
    ImageButton mTaleImageButton;

    @BindView(R.id.tale_name_edittext)
    TextView mTaleNameEditText;

    @BindView(R.id.save_tale_button)
    Button mSaveButton;

    private TaleCreatorRecyclerViewAdapter mAdapter;
    private DefaultStorIOSQLite mDefaultStorIOSQLite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tale_creating);



        ButterKnife.bind(this);

        mTaleNameEditText.setText("");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.supportsPredictiveItemAnimations();

        mTaleCreatorRecyclerView.setLayoutManager(linearLayoutManager);

        mAdapter = new TaleCreatorRecyclerViewAdapter(createEmptyTaleParts(), this);

        mTaleCreatorRecyclerView.setAdapter(mAdapter);

        SQLiteOpenHelper sqLiteOpenHelper = new DbOpenHelper(this);
        mDefaultStorIOSQLite = DefaultStorIOSQLite.builder()
                .sqliteOpenHelper(sqLiteOpenHelper)
                .addTypeMapping(TaleModel.class, new TaleModelSQLiteTypeMapping())
                .build();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GENERAL_IMAGE_CHOOSER) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                mTaleImageURI = data.getData();
                mTaleImageButton.setImageURI(mTaleImageURI);
                Log.d(TAG, "onActivityResult: GENERAL_IMAGE_CHOOSER " + data.getData());
            }
        } else if (requestCode == TALEPART_IMAGE_CHOOSER) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                mAdapter.updateImage(mAdapterPostitionForUpdate, data.getDataString());
                Log.d(TAG, "onActivityResult: TALEPART_IMAGE_CHOOSER " + data.getDataString());
            }
        } else if (requestCode == TALEPART_AUDIO_CHOOSER) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                mAdapter.updateAudio(mAdapterPostitionForUpdate, data.getDataString());
                Log.d(TAG, "onActivityResult: TALEPART_AUDIO_CHOOSER " + data.getDataString());
            }
        }
    }

    @OnClick(R.id.tale_image_imagebutton)
    protected void TaleImageButtonOnClick() {
        //Вызываем intent выбора картинки
        Intent pickIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType(Const.MIME_TYPE_IMAGE);
        startActivityForResult(pickIntent, GENERAL_IMAGE_CHOOSER);
    }

    @OnClick(R.id.save_tale_button)
    protected void TaleSaveButtonOnCick() {
        Log.d(TAG, "TaleSaveButtonOnCick: " + mTaleNameEditText.getText());
        if (!mTaleNameEditText.getText().toString().equals("")) {
            putTaleToDb(buildTaleFromActivity());
            // TODO: 09.07.2016 Изменить реакцию на очищение Activity
            mSaveButton.setEnabled(false);
            Toast.makeText(this, getString(R.string.tale_saved), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.enter_tale_name) + "!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Метод собирает рассказ из введённых данных
     */
    private TaleModel buildTaleFromActivity() {
        String taleName = mTaleNameEditText.getText().toString();
        TaleModel tale =
                new TaleModel(taleName, mTaleImageURI.toString(), mAdapter.getItemsAsTaleParts());
        return tale;
    }

    /**
     * Метод сохраняет объект TaleModel в БД
     * @param tale сохраняемый объект
     */
    private void putTaleToDb(TaleModel tale) {
        mDefaultStorIOSQLite
                .put()
                .object(tale)
                .prepare()
                .executeAsBlocking();

    }

    /**
     * Метод создания списка элемен
     * @return список объектов TaleModel
     */
    private LinkedList<TalePart> createEmptyTaleParts() {

        LinkedList<TalePart> mockLinkedList = new LinkedList<>();
        for (int j = 0; j < 4; j++) {
            mockLinkedList.add(new TalePart("","",""));
        }
        return mockLinkedList;
    }
//sdf
    /**
     * Callback метод из адаптера для выбора картинки или аудио
     * @param positition позиция в адаптере
     * @param mimeType MIME-тип
     */
    @Override
    public void pickFile(int positition, String mimeType) {

        mAdapterPostitionForUpdate = positition;

        //В зависимости от mime типа запускаем выбор контента
        switch (mimeType) {

            //Если аудио
            case Const.MIME_TYPE_AUDIO:
                Intent pickAudioIntent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                pickAudioIntent.setType(mimeType);
                startActivityForResult(pickAudioIntent, TALEPART_AUDIO_CHOOSER);
                break;

            //Если картинка
            case Const.MIME_TYPE_IMAGE:
                Intent pickImageIntent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickImageIntent.setType(mimeType);
                startActivityForResult(pickImageIntent, TALEPART_IMAGE_CHOOSER);
                break;
        }
    }
}
