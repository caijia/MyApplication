package com.example.administrator.myapplication.fileprovider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.myapplication.R;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by cai.jia on 2016/12/30 0030
 */

public class FileSelectorActivity extends AppCompatActivity {

    private static final String AUTHORITY = "com.example.administrator.myapplication.fileprovider";

    private File textFile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_selector_file);

        File file = new File(getFilesDir(), "images");
        System.out.println(file.getPath());
        file.mkdirs();

        textFile = new File(file, "mytext.txt");
        if (!textFile.exists()) {
            try {
                textFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (textFile.exists() && textFile.length() == 0) {
            System.out.println("write");
            writeText();
        }

        TextView shareText = (TextView) findViewById(R.id.share_file_tv);
        shareText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                responseFile();
            }
        });
    }

    private void writeText() {
        String s = "share a txt file";
        BufferedOutputStream out = null;
        BufferedInputStream in = null;
        try {
            out = new BufferedOutputStream(new FileOutputStream(textFile));
            in = new BufferedInputStream(new ByteArrayInputStream(s.getBytes()));
            int len ;
            byte[]buffer = new byte[1024];
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (in != null) {
                    in.close();
                }

                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void responseFile() {
        Intent resultIntent =
                new Intent("com.example.myapp.ACTION_RETURN_FILE");
        Uri fileUrl = FileProvider.getUriForFile(FileSelectorActivity.this, AUTHORITY, textFile);
        resultIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        resultIntent.setDataAndType(fileUrl, getContentResolver().getType(fileUrl));
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
