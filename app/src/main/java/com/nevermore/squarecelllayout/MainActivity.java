package com.nevermore.squarecelllayout;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

public class MainActivity extends AppCompatActivity implements CellLayout.OnItemClickListener{

    private CellLayout cell;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cell = (CellLayout) findViewById(R.id.cellLayout);

        cell.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(ViewGroup parent, View view, final int position, long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示")
                .setMessage("删除第"+position+"位置的格子？")
                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cell.removeViewAt(position);
                    }
                })
                .setPositiveButton("取消",null)
                .show();
    }
}
