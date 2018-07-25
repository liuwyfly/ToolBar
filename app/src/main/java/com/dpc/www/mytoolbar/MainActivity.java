package com.dpc.www.mytoolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.widget.Button;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MyActivity";
    private static final int PHOTO_REQUEST = 1;
    private static final int PHOTO_CLIP = 2;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button loginBtn = (Button)findViewById(R.id.button);
        mImageView = (ImageView)findViewById(R.id.mImageView);
        loginBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                try{
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                catch (Exception ex) {
                    // 显示异常信息
                    // Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button imgBtn_1 = (Button)findViewById(R.id.button_2);
        imgBtn_1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                getPhoto();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PHOTO_REQUEST:
                if (data != null) {
                    Uri uri = data.getData();
                    Log.i(TAG, "Uri:");
                    Log.i(TAG, uri.getPath());
                    //对相册取出照片进行裁剪
                    photoClip(uri);
                }
                break;
            case PHOTO_CLIP:
                if(data != null){
                    File filepath;
                    Bundle extras = data.getExtras();
                    if (extras != null) {
                        Bitmap photo = extras.getParcelable("data");
                        String innerPath = Environment.getDataDirectory().getPath();
                        Log.i(TAG, "data path:" + innerPath);

                        File[] files = new File[1];

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                            files = getExternalFilesDirs(Environment.MEDIA_MOUNTED);
                            files = getExternalFilesDirs(null);
                            for(File file:files){
                                Log.i(TAG, file.getPath());
                            }
                        }
                        try {
                            //获得图片路径
                            filepath = UploadUtil.saveFile(photo, files[0].getPath(), "icon.jpg");
//                            Log.i(TAG, "filepath:");
//                            Log.i(TAG, filepath.getAbsolutePath());
                            //上传照片
                            // toUploadFile();
                        } catch (IOException e) {
//                            e.printStackTrace();
                        }

                        mImageView.setImageBitmap(photo);
                    }
                }
        }
    }

    /**
     * 从相册选择图片来源
     */
    private void getPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        //startActivity(intent);
        startActivityForResult(intent, PHOTO_REQUEST);
    }

    private void photoClip(Uri uri) {
        // 调用系统中自带的图片剪裁
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PHOTO_CLIP);
    }
}
