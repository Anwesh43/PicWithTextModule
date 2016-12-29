package com.anwesome.ui.picwithtextmodule;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anweshmishra on 29/12/16.
 */
public class BitmapTextView extends View {
    private List<MessageBody> messageBodies = new ArrayList<>();
    private class MessageBody {
        private String text;
        private float x,y;
        public MessageBody(String text,float x,float y) {
            this.text = text;
            this.x = x;
            this.y = y;
        }
        public void takeUp() {
            this.y -= (AppConstants.TEXT_SIZE+1);
        }
        public void draw(Canvas canvas,Paint paint) {
            paint.setColor(Color.WHITE);

            canvas.drawText(text,x,y,paint);
        }
        public int hashCode() {
            return text.hashCode()+(int)x+(int)y;
        }
    }
    private Bitmap bitmap;
    private String text = "";
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    public BitmapTextView(Context context, AttributeSet attrs) {
        super(context,attrs);
        setDrawingCacheEnabled(true);
    }
    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
    public void onDraw(Canvas canvas) {
        if(bitmap!=null && text!=null) {
            messageBodies = new ArrayList<>();
            float scaleX = (canvas.getWidth()*1.0f)/bitmap.getWidth(),scaleY = (canvas.getHeight()*1.0f)/bitmap.getHeight();
            canvas.save();
            canvas.translate(canvas.getWidth() / 2, canvas.getHeight() / 2);
            canvas.scale(scaleX,scaleY);
            canvas.drawBitmap(bitmap, -bitmap.getWidth() / 2, -bitmap.getHeight() / 2, paint);
            canvas.restore();
            paint.setTextSize(AppConstants.TEXT_SIZE);
            String tokens[] = text.split(" ");
            float x = canvas.getWidth() / 2, y = canvas.getHeight() / 2, w = canvas.getWidth();
            String msg = "";
            int token_index = 0;
            for (String token : tokens) {
                if (paint.measureText(msg + token) >= canvas.getWidth()) {
                    MessageBody messageBody = new MessageBody(msg, x, y - (AppConstants.TEXT_SIZE + 1));
                    messageBodies.add(messageBody);
                    for (int i = 0; i < messageBodies.size() - 1; i++) {
                        MessageBody prevMessageBody = messageBodies.get(i);
                        prevMessageBody.takeUp();
                    }
                    msg = token+" ";
                    x = w / 2;
                } else {
                    msg = msg +token+" ";
                    x = w / 2 - paint.measureText(msg) / 2;
                }
                paint.setTextSize(AppConstants.TEXT_SIZE);
                paint.setColor(Color.WHITE);
                if(token_index == tokens.length-1) {
                    canvas.drawText(msg, x, y, paint);
                }
                token_index++;
            }
            for (MessageBody messageBody : messageBodies) {
                messageBody.draw(canvas, paint);
            }
        }

    }
    public void setStringText(String text) {
        this.text = text;
    }

}
