package com.muslimlife.app.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

import com.muslimlife.app.R;

public class RoundImageView extends AppCompatImageView {

    private float radius = 0.0f;
    private Path path;
    private RectF rect;
    private TypedArray typedArray;

    public RoundImageView(Context context) {
        super(context);
        init();
    }

    public RoundImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundImageView);
        init();
    }

    public RoundImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundImageView);
        init();
    }

    private void init() {
        path = new Path();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        rect = new RectF(0, 0, this.getWidth(), this.getHeight());
        radius = typedArray.getDimension(R.styleable.RoundImageView_rect_radius, 10f);
        path.addRoundRect(rect, radius, radius, Path.Direction.CW);
        canvas.clipPath(path);
        super.onDraw(canvas);
    }
}


