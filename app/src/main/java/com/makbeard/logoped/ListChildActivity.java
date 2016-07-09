package com.makbeard.logoped;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ListChildActivity extends AppCompatActivity {

    private ArrayAdapter<String> adapter;
    private String selectedItem;
    private final Context context = this;
    @BindView(R.id.buttonlistchild) Button buttonlistchild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_child);
        ButterKnife.bind(this);




        ListView listView = (ListView) findViewById(R.id.listview);

final ArrayAdapter adapter=
        ArrayAdapter.createFromResource(this,R.array.child,android.R.layout.simple_list_item_1);

        assert listView != null;
        listView.setAdapter(adapter);


        AdapterView.OnItemLongClickListener itemLongClickListener = new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = parent.getItemAtPosition(position).toString();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Вы действительно хотите удалить"+selectedItem+"?" );
                builder.setCancelable(false);
                builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        adapter.remove(selectedItem);
                        adapter.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
                return true;
            }
        };
    }

    @OnClick(R.id.buttonlistchild)
    protected void OnClickAddTale(){
        startActivity(new Intent(ListChildActivity.this,TaleCreatingActivity.class));
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

    }
}
