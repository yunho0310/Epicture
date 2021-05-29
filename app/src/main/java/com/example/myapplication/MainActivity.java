package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final int MULTIPLE_PERMISSIONS = 101;
    private File tempFile;
    private final int GET_GALLERY_IMAGE = 200;
    private static final int PICK_FROM_ALBUM = 1;
    int requestCode;
    int resultCode;
    Intent data;
    Uri uri;
    Uri photoUri;
    String strFilePath = "/epicture/images";
    String filedname = "ABCD.jpg";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        f_make_dir();

        Button btn_load = (Button) findViewById(R.id.btn_load);

        btn_load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                go_to_album();
            }
        });

        uri = (Uri) getIntent().getParcelableExtra(Intent.EXTRA_STREAM);  //갤러리 공유 목록에 내 앱 띄우기

        if (uri != null) {
            Intent intent = new Intent(this, GetPictureInfoActivity.class);
            intent.putExtra("image1", uri);
            startActivity(intent);
        }
    }




    private void go_to_album() {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_FROM_ALBUM);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FROM_ALBUM) {

            photoUri = data.getData();

            if (photoUri != null) {
                Intent intent = new Intent(this, GetPictureInfoActivity.class);
                intent.putExtra("image2", photoUri);
                startActivity(intent);
            }
        }
    }

    private boolean checkPermissions() {
        int result;
        List<String> permissionList = new ArrayList<>();
        for (String pm : permissions) {
            result = ContextCompat.checkSelfPermission(this, pm);
            if (result != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(pm);
            }
        }
        if (!permissionList.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissionList.toArray(new String[permissionList.size()]), MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }                                                        //앱 처음 실행 때 권한 부여

    File f_make_dir() {
        checkPermissions();
        File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Epicture/images");
        if (!dir.exists()) {
            if (dir.mkdirs()) {
                Toast.makeText(this, "폴더 생성에 성공했습니다", Toast.LENGTH_SHORT).show();
            }
        }
        return dir;
    }
}










