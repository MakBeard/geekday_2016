package com.makbeard.logoped;

import android.content.Intent;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.makbeard.logoped.db.DbOpenHelper;
import com.makbeard.logoped.model.TaleModel;
import com.makbeard.logoped.model.TaleModelSQLiteTypeMapping;
import com.makbeard.logoped.model.TalePart;
import com.pushtorefresh.storio.sqlite.impl.DefaultStorIOSQLite;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.LinkedList;

public class SplashActivity extends AppCompatActivity {

    private DefaultStorIOSQLite mDefaultStorIOSQLite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SQLiteOpenHelper sqLiteOpenHelper = new DbOpenHelper(this);
        mDefaultStorIOSQLite = DefaultStorIOSQLite.builder()
                .sqliteOpenHelper(sqLiteOpenHelper)
                .addTypeMapping(TaleModel.class, new TaleModelSQLiteTypeMapping())
                .build();

        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    loadAssetTale("tale1.json");
                    loadAssetTale("tale2.json");
                    loadAssetTale("tale3.json");
                    sleep(1000);  //Delay of 3 seconds
                } catch (Exception e) {
                }
                handler.sendEmptyMessage(0);
            }
        };
        t.start();

    }

    private Handler handler = new Handler() {


        @Override
        public void handleMessage(Message msg) {

            Intent i = new Intent(SplashActivity.this,
                    MainActivity.class);
            startActivity(i);
            finish();

        }
    };

    private void loadAssetTale(String taleJsonFile) {

        String jsonString = loadJSONFileFromAssets(taleJsonFile);

        Gson gson = new Gson();
        Type type = new TypeToken<TaleModel>() {
        }.getType();
        TaleModel tale = gson.fromJson(
                jsonString,
                type
        );
        tale.setImageLink(copyAsset(tale.getImageLink()).toString());

        LinkedList<TalePart> parts = tale.getTaleParts();
        for (int i = 0; i<parts.size(); i++) {
            TalePart part = parts.get(i);
            part.setImageLink(copyAsset(tale.getTaleParts().get(i).getImageLink()).toString());
            part.setAudioLink(copyAsset(tale.getTaleParts().get(i).getAudioLink()).toString());
            parts.set(i, part);
        }
        tale.setTaleParts(parts);

        mDefaultStorIOSQLite
                .put()
                .object(tale)
                .prepare()
                .executeAsBlocking();

//        // get input stream
//        InputStream ims = null;
//        try {
//            ims = getAssets().open("tale.json");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        String
//        // load image as Drawable
//        Drawable d = Drawable.createFromStream(ims, null);
//        // set image to ImageView
//        mImage.setImageDrawable(d);


    }

    private Uri copyAsset(String filename) {
        AssetManager assetManager = getAssets();


        if (filename != null) {
            InputStream in = null;
            OutputStream out = null;
            File outFile = new File(getExternalFilesDir(null), filename);

            try {
                in = assetManager.open(filename);
                out = new FileOutputStream(outFile);
                copyFile(in, out);
            } catch(IOException e) {
                Log.e("tag", "Failed to copy asset file: " + filename, e);
            }
            finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        // NOOP
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        // NOOP
                    }
                }
                return Uri.fromFile(outFile);
            }
        }

        return null;
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }

    private String loadJSONFileFromAssets(String jsonFile) {
        StringBuilder buf = new StringBuilder();
        BufferedReader in = null;

        try {
            InputStream json = getAssets().open(jsonFile);
            in = new BufferedReader(new InputStreamReader(json, "UTF-8"));
            String str;
            while ((str = in.readLine()) != null) {
                buf.append(str);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        }
        return buf.toString();
    }




}