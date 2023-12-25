package com.example.samplestickerapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;

public class GradientTextView extends androidx.appcompat.widget.AppCompatTextView {

    private LinearGradient gradient;
    private Paint paint;

    public GradientTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (gradient == null) {
            int[] colors = {Color.parseColor("#FF00FF"), Color.parseColor("#FFFF00")}; // Define your gradient colors here
            float x0 = 0;
            float x1 = getWidth();
            gradient = new LinearGradient(x0, 0, x1, 0, colors, null, Shader.TileMode.CLAMP);
            paint.setShader(gradient);
        }
        super.onDraw(canvas);
    }
}
