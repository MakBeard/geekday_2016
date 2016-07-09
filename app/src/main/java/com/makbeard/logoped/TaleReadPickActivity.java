package com.makbeard.logoped;

import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.makbeard.logoped.db.DbOpenHelper;
import com.makbeard.logoped.db.tables.TalesTable;
import com.makbeard.logoped.model.TaleModel;
import com.makbeard.logoped.model.TaleModelSQLiteTypeMapping;
import com.pushtorefresh.storio.sqlite.impl.DefaultStorIOSQLite;
import com.pushtorefresh.storio.sqlite.queries.Query;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TaleReadPickActivity extends AppCompatActivity {

    private static final String TAG = TaleReadPickActivity.class.getSimpleName();
    private DefaultStorIOSQLite mDefaultStorIOSQLite;
    private TaleModel mTale;

    @BindView(R.id.text_part1_textview)
    TextView mPart1TextView;

    @BindView(R.id.text_part2_textview)
    TextView mPart2TextView;

    @BindView(R.id.text_part3_textview)
    TextView mPart3TextView;

    @BindView(R.id.text_part4_textview)
    TextView mPart4TextView;

    @BindView(R.id.image_part1_imageview)
    ImageView mImage1ImageView;

    @BindView(R.id.image_part2_imageview)
    ImageView mImage2ImageView;

    @BindView(R.id.image_part3_imageview)
    ImageView mImage3ImageView;

    @BindView(R.id.image_part4_imageview)
    ImageView mImage4ImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tale_read_pick);

        ButterKnife.bind(this);

        String taleName = getIntent().getStringExtra(Const.EXTRA_NAME);

        SQLiteOpenHelper sqLiteOpenHelper = new DbOpenHelper(this);
        mDefaultStorIOSQLite = DefaultStorIOSQLite.builder()
                .sqliteOpenHelper(sqLiteOpenHelper)
                .addTypeMapping(TaleModel.class, new TaleModelSQLiteTypeMapping())
                .build();

        mTale = loadTaleFromDb(taleName);

        mPart1TextView.setText(mTale.getTaleParts().get(0).getText());
        mPart2TextView.setText(mTale.getTaleParts().get(1).getText());
        mPart3TextView.setText(mTale.getTaleParts().get(2).getText());
        mPart4TextView.setText(mTale.getTaleParts().get(3).getText());

        mImage1ImageView.setOnClickListener(v -> pickImage(0));
        mImage2ImageView.setOnClickListener(v -> pickImage(1));
        mImage3ImageView.setOnClickListener(v -> pickImage(2));
        mImage4ImageView.setOnClickListener(v -> pickImage(3));

        Log.d(TAG, "onCreate: " + mTale.toString());
    }

    private void pickImage(int partNumber) {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .create();
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.image_picker_layout, null);
        alertDialog.setView(dialogLayout);

        alertDialog.setOnShowListener(dialog -> {
            ImageView imageView1 = (ImageView) alertDialog.findViewById(R.id.image1);
            ImageView imageView2 = (ImageView) alertDialog.findViewById(R.id.image2);
            ImageView imageView3 = (ImageView) alertDialog.findViewById(R.id.image3);
            ImageView imageView4 = (ImageView) alertDialog.findViewById(R.id.image4);

            int[] partsNumbersArray = { 0, 1, 2, 3 };
            shuffleArray(partsNumbersArray);

            if (imageView1 != null) {
                imageView1.setImageURI(Uri.parse(mTale.getTaleParts().get(partsNumbersArray[0]).getImageLink()));
                imageView1.setOnClickListener(v -> {
                    setImageToImageView(partNumber, Uri.parse(mTale.getTaleParts().get(partsNumbersArray[0]).getImageLink()));
                    alertDialog.dismiss();
                });
            }

            if (imageView2 != null) {
                imageView2.setImageURI(Uri.parse(mTale.getTaleParts().get(partsNumbersArray[1]).getImageLink()));
                imageView2.setOnClickListener(v -> {
                    setImageToImageView(partNumber, Uri.parse(mTale.getTaleParts().get(partsNumbersArray[1]).getImageLink()));
                    alertDialog.dismiss();
                });
            }

            if (imageView3 != null) {
                imageView3.setImageURI(Uri.parse(mTale.getTaleParts().get(partsNumbersArray[2]).getImageLink()));
                imageView3.setOnClickListener(v -> {
                    setImageToImageView(partNumber, Uri.parse(mTale.getTaleParts().get(partsNumbersArray[2]).getImageLink()));
                    alertDialog.dismiss();
                });
            }

            if (imageView4 != null) {
                imageView4.setImageURI(Uri.parse(mTale.getTaleParts().get(partsNumbersArray[3]).getImageLink()));
                imageView4.setOnClickListener(v -> {
                    setImageToImageView(partNumber, Uri.parse(mTale.getTaleParts().get(partsNumbersArray[3]).getImageLink()));
                    alertDialog.dismiss();
                });
            }

        });

        //Преобразуем pixels в dp
        int sizeInDp = 320;
        float scale = getResources().getDisplayMetrics().density;
        int dpAsPixels = (int) (sizeInDp * scale + 0.5f);

        alertDialog.show();
        alertDialog.getWindow().setLayout(dpAsPixels, dpAsPixels);
    }

    private void setImageToImageView(int partNumber, Uri imageUri) {
        switch (partNumber) {
            case 0:
                mImage1ImageView.setImageURI(imageUri);
                break;
            case 1:
                mImage2ImageView.setImageURI(imageUri);
                break;
            case 2:
                mImage3ImageView.setImageURI(imageUri);
                break;
            case 3:
                mImage4ImageView.setImageURI(imageUri);
                break;
        }
    }

    /**
     * Метод возвращает по имени Tale из БД
     * @return объекты Tale
     */
    private TaleModel loadTaleFromDb(String taleName) {
        return mDefaultStorIOSQLite
                .get()
                .object(TaleModel.class)
                .withQuery(
                        Query.builder()
                                .table(TalesTable.TABLE_TALE)
                                .where("name = ?")
                                .whereArgs(taleName)
                                .build())
                .prepare()
                .executeAsBlocking();
    }

    // Implementing Fisher–Yates shuffle
    static void shuffleArray(int[] ar)
    {
        // If running on Java 6 or older, use `new Random()` on RHS here
        Random rnd = ThreadLocalRandom.current();
        for (int i = ar.length - 1; i > 0; i--)
        {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            int a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }
}
