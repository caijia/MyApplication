package com.example.administrator.myapplication.pic;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.administrator.myapplication.R;


/**
 * Created by cai.jia on 2017/6/19 0019
 */

public class SelectPictureActivity extends AppCompatActivity implements View.OnClickListener {

    private Button photoBtn;
    SelectFileGroupDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_picturn);
        photoBtn = (Button) findViewById(R.id.photo_btn);
        photoBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (dialog == null) {
            dialog = new SelectFileGroupDialog();
        }

        if (dialog.getDialog() == null || !dialog.getDialog().isShowing()) {
            dialog.show(getSupportFragmentManager(),null);
        }
    }
}
