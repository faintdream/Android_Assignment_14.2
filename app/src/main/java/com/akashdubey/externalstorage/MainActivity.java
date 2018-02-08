package com.akashdubey.externalstorage;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Permission;

public class MainActivity extends AppCompatActivity {


    String state;                           //store state
    static int permissions;                 //store permission
    Bitmap image;                           //store elon musk photo
    Button checkPermission,transfer;        //button objects





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //storing the result of checkSelfPermission in permission
        permissions= ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        transfer=(Button)findViewById(R.id.transferBtnBtn);

        //storing the phpt in image using proper method from BitmapFactory
        image= BitmapFactory.decodeResource(getResources(),R.drawable.elon_musk);


        //Ask for permission if permission has not been granted yet.
        if ( permissions != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                //some explanation here
                // i donty have time to do this now
            } else {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},100);
            }
        }

            checkPermission=(Button)findViewById(R.id.checkPermissionBtn);

        //enable/disable transfer button based on permission status
        checkPermission.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            boolean result=isExternalStorageRW();
           if(result){
               transfer.setEnabled(true);
           }else{
               transfer.setEnabled(false);
           }
        }
    });


    // here we actually transfer the file
    transfer.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            // getting the external public  Pictures dir path
            File file= new File(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)));

            // adding own folder name
            File destDir=new File(file.toString()+"/CUSTOM");

            //creating the folder if not exist
            if(!destDir.exists()){
                destDir.mkdir();
            }
            try {
                //chossing a file name for dest file
                File destFile= new File(destDir,"dest.png");

                //creating a fileoutputstream  on given path
                FileOutputStream fileOutputStream= new FileOutputStream(destFile);

                //compressing the JPEG image to PNG and passing to object of FileOutputStream
                image.compress(Bitmap.CompressFormat.PNG,100,fileOutputStream);

                // once everything done do flush and close.
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            Toast.makeText(MainActivity.this, "Photo has been created on SD card", Toast.LENGTH_SHORT).show();
        }


    });

    }

    // while i havent used this method , i will keep it , as it is nice to have and
    // i dont want to search again if i need to , i suck i know...
    // but that's fine as long as it helps :0)
    boolean isExternalStorageRW(){
        state= Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state)){
            return true;
        }
        return false;
    }
}
