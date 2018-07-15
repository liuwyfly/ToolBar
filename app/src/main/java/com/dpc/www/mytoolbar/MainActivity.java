package com.dpc.www.mytoolbar;

import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button loginBtn = (Button)findViewById(R.id.button);
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

    /**
     * 从相册选择图片来源
     */
    private void getPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        startActivity(intent);
        //startActivityForResult(intent, PHOTO_REQUEST);
    }
}
