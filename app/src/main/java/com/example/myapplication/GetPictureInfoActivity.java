    package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import static java.sql.DriverManager.println;


public class GetPictureInfoActivity extends AppCompatActivity
{
    Uri uri_from_main;
    Spinner spinner;
    JSONObject photo_info_object;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_picture_info);


        if (getIntent().getParcelableExtra("image1") != null) {
            uri_from_main = getIntent().getParcelableExtra("image1");
        }                           //공유버튼으로 불러운 이미지 uri에 넣기


        if (getIntent().getParcelableExtra("image2") != null) {
            uri_from_main = getIntent().getParcelableExtra("image2");
        }                           //버튼으로 불러운 이미지 uri에 넣기


        ImageView imageView = (ImageView)findViewById(R.id.imageView);                              //imageview에 사진 띄우기
        try {

            Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(), uri_from_main);
            imageView.setImageBitmap(bm);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }                 //~


        Button btn_date = (Button) findViewById(R.id.btn_date);
        btn_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        Button btn_add_map = (Button) findViewById(R.id.btn_add_map);
        btn_add_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
         });


        spinner = findViewById(R.id.spinner);

        ArrayAdapter monthAdapter = ArrayAdapter.createFromResource(this, R.array.category, android.R.layout.simple_spinner_dropdown_item);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(monthAdapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });


        Button btn_done = (Button) findViewById(R.id.btn_done);

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photo_info_object = new JSONObject();
                try {
                    EditText name = (EditText)findViewById(R.id.name);
//                    EditText date = (EditText)findViewById(R.id.date);
                    EditText place = (EditText)findViewById(R.id.place);

                    String s_name = name.getText().toString();
//                    String s_date = date.getText().toString();
                    String s_place = place.getText().toString();

                    photo_info_object.put("name", s_name);
//                    photo_info_object.put("date", s_date);
                    photo_info_object.put("place", s_place);

                }
                catch (JSONException e){
                }
                System.out.println(photo_info_object);
            }
        });

        toString();

        Button btn_cancel = (Button) findViewById(R.id.btn_cancel);                                 //cancel버튼 누르면 MainActivity로 돌아가기
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

}
