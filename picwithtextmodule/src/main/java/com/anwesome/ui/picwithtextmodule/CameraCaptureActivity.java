package com.anwesome.ui.picwithtextmodule;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;


/**
 * Created by anweshmishra on 29/12/16.
 */
public class CameraCaptureActivity extends AppCompatActivity {
    private View cameraView;
    private LinearLayout linearLayout;
    private int REQUEST_CODE = 1;
    private int w = 0,h = 0;
    private ImageView resultImage;
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        initiateCaptureEvent();
    }
    public void initiateCaptureEvent() {
        resultImage = (ImageView) findViewById(R.id.result);
        linearLayout = (LinearLayout) findViewById(R.id.new_layout);
        cameraView = findViewById(R.id.capture);
        resultImage = (ImageView)findViewById(R.id.result);
        if(cameraView!=null) {
            cameraView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent,REQUEST_CODE);
                }
            });
        }
        linearLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int w = linearLayout.getWidth();
                int h = linearLayout.getHeight();
                cameraView.setX(w/2-cameraView.getWidth()/2);
                cameraView.setY(h/2-cameraView.getHeight()/2);
                resultImage.setX(w/2-resultImage.getWidth()/2);
                resultImage.setY(cameraView.getWidth()+h/2-resultImage.getHeight()/2);
            }
        });

    }

    protected   int getLayout() {
        return R.layout.camera_layout;
    }
    protected void processBitmap(Bitmap bitmap) {
        resultImage.setImageBitmap(bitmap);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if(intent.getExtras()!=null) {
            Bitmap bitmap = (Bitmap) intent.getExtras().get("data");
            if (bitmap != null) {
                final Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.camera_text);
                Button button = (Button)dialog.findViewById(R.id.done);
                final BitmapTextView imageView = (BitmapTextView) dialog.findViewById(R.id.camera_image);
                EditText editText = (EditText) dialog.findViewById(R.id.content_text);
                imageView.setBitmap(bitmap);
                editText.addTextChangedListener(new TextWatcherAdapter() {
                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        imageView.setStringText(charSequence.toString());
                        imageView.postInvalidate();
                    }
                });
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        processBitmap(view.getDrawingCache());
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        }
    }
}
