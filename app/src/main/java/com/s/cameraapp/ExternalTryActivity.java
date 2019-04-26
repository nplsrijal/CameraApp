package com.s.cameraapp;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class ExternalTryActivity extends Activity {

    private Button button;
   private ImageView imageview;
    Bitmap bitmap;
    View view;
    ByteArrayOutputStream bytearrayoutputstream;
    File file;
    FileOutputStream fileoutputstream;

    private void checkPermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]
                    {
                            Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },0);
        }

    }
    private void loadCamera()
    {
        Intent intent =new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent,1);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 1 && resultCode==RESULT_OK){
            Bundle extras =data.getExtras();
            Bitmap imageBitmap=(Bitmap)extras.get("data");
            //imageview.setImageBitmap(imageBitmap);

            imageBitmap.compress(Bitmap.CompressFormat.PNG, 60, bytearrayoutputstream);
            String folder_main = "MEROCAMERA";
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

             File folder = new File(Environment.getExternalStorageDirectory() + "/DCIM/" + folder_main, "image" );
            if (!folder.exists()) {
                folder.mkdirs();
            }

            file = new File( Environment.getExternalStorageDirectory() + "/DCIM/MEROCAMERA/image" +"/merocamera_"+timeStamp+".png");

            try

            {
                file.createNewFile();

                fileoutputstream = new FileOutputStream(file);

                fileoutputstream.write(bytearrayoutputstream.toByteArray());

                fileoutputstream.close();

            }

            catch (Exception e)

            {

                e.printStackTrace();

            }

            Toast.makeText(ExternalTryActivity.this, "Image Saved Successfully", Toast.LENGTH_LONG).show();

        }

        }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission();

        button = (Button)findViewById(R.id.button_image);
        imageview = (ImageView)findViewById(R.id.imageview);
        bytearrayoutputstream = new ByteArrayOutputStream();

        button.setOnClickListener(new View.OnClickListener() {


            public void onClick(View v) {
                loadCamera();
            }
        });
    }

}