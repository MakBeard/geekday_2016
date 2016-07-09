package com.makbeard.logoped;

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
import android.widget.ImageButton;
import android.widget.TextView;

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
public class TaleCreatingActivity extends AppCompatActivity {

    private static final int GENERAL_IMAGE_CHOOSER = 0;
    private static final String TAG = TaleCreatingActivity.class.getSimpleName();
    Uri mTaleImageURI;

    @BindView(R.id.tale_creator_recyclerview)
    RecyclerView mTaleCreatorRecyclerView;

    @BindView(R.id.tale_image_imagebutton)
    ImageButton mTaleImageButton;

    @BindView(R.id.tale_name_edittext)
    TextView mTaleNameEditText;

    TaleCreatorRecyclerViewAdapter mAdapter;
    private DefaultStorIOSQLite mDefaultStorIOSQLite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tale_creating);

        ButterKnife.bind(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.supportsPredictiveItemAnimations();

        mTaleCreatorRecyclerView.setLayoutManager(linearLayoutManager);

        mAdapter = new TaleCreatorRecyclerViewAdapter(createEmptyTaleParts());

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
                Log.d(TAG, "onActivityResult: " + data.getData());
            }
        }
    }

    @OnClick(R.id.tale_image_imagebutton)
    protected void TaleImageButtonOnClick() {
        //Вызываем intent выбора картинки
        Intent pickIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");
        startActivityForResult(pickIntent, GENERAL_IMAGE_CHOOSER);
    }

    @OnClick(R.id.save_tale_button)
    protected void TaleSaveButtonOnCick() {
        putTaleToDb(buildTaleFromActivity());
    }

    /**
     * Метод собирает рассказ из введённых данных
     */
    private TaleModel buildTaleFromActivity() {
        String taleName = mTaleNameEditText.getText().toString();
        TaleModel tale =
                new TaleModel(taleName, mTaleImageURI.toString(), mAdapter.getItemsAsTaleParts());
        Log.d(TAG, "buildTaleFromActivity: " + tale.toString());
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
}
