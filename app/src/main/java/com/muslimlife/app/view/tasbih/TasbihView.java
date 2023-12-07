package com.muslimlife.app.view.tasbih;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import androidx.core.view.GestureDetectorCompat;
import com.muslimlife.app.R;

import java.lang.reflect.Array;

public class TasbihView extends View {
    private float[][] arrayFloat;
    private Bitmap bitmap;
    private float c;
    public float downX;
    private float endHeight;
    private GestureDetectorCompat gestureDetectorCompat;
    public float h;
    public float i;
    private float j;
    private float k;
    public Boolean o;
    public boolean onDown = false;
    public float onDownGetX;
    public Boolean isPosition;
    private Paint paint;
    private Path path;
    private PathMeasure pathMeasure;
    private float pathMeasureLength;
    private float startHeight;
    private ValueAnimator valueAnimator;

    TasbihControl tasbihControl;

    private class Listener extends GestureDetector.SimpleOnGestureListener {
        private Listener() {
        }

        @Override
        public final boolean onDown(MotionEvent motionEvent) {
            TasbihView.this.onDownGetX = motionEvent.getX();
            TasbihView tasbihView = TasbihView.this;
            tasbihView.downX = tasbihView.onDownGetX;
            if (TasbihView.this.onDown) {
                TasbihView.this.onDown = false;
            }
            TasbihView.this.o = null;
            TasbihView.this.isPosition = null;
            return true;
        }

        @Override
        public final boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            TasbihView.this.checkAnimation();
            return true;
        }

        @Override
        public final boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            boolean z = false;
            if (!TasbihView.this.onDown) {
                TasbihView.this.h = motionEvent2.getX() - TasbihView.this.onDownGetX;
            } else if (TasbihView.this.o != null && TasbihView.this.isPosition == TasbihView.this.o
                    && ((!TasbihView.this.isPosition && TasbihView.this.downX < TasbihView.this.onDownGetX)
                    || (TasbihView.this.isPosition && TasbihView.this.downX > TasbihView.this.onDownGetX))) {
                TasbihView.this.onDown = false;
                TasbihView.this.h = motionEvent2.getX() - TasbihView.this.onDownGetX;
            }
            if (TasbihView.this.h == 0.0f) {
                return true;
            }
            int i = (Float.compare(f, 0.0f));
            TasbihView.this.isPosition = i <= 0;
            TasbihView.this.downX = motionEvent2.getX();
            if (TasbihView.this.o == null) {
                if (i <= 0) {
                    z = true;
                }
                TasbihView.this.o = z;
            }
            if (TasbihView.this.isPosition != TasbihView.this.o && ((!TasbihView.this.isPosition && TasbihView.this.downX <= TasbihView.this.onDownGetX) || (TasbihView.this.isPosition && TasbihView.this.downX >= TasbihView.this.onDownGetX))) {
                TasbihView.this.i = 0.0f;
                TasbihView.this.onDown = true;
            }
            if (TasbihView.this.h < (-TasbihView.this.i)) {
                TasbihView tasbihView = TasbihView.this;
                tasbihView.h = -tasbihView.i;
            } else if (TasbihView.this.h > TasbihView.this.i) {
                TasbihView tasbihView2 = TasbihView.this;
                tasbihView2.h = tasbihView2.i;
            }
            TasbihView.this.invalidate();
            return true;
        }

        @Override
        public final boolean onSingleTapUp(MotionEvent motionEvent) {
            TasbihView.this.startAnimation(true, false);
            return true;
        }

        @Override
        public final boolean onDoubleTap(MotionEvent motionEvent) {
            TasbihView.this.startAnimation(true, false);
            return true;
        }
    }

    public TasbihView(Context context) {
        super(context);
    }

    public TasbihView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public TasbihView(Context context, AttributeSet attributeSet, int i2) {
        super(context, attributeSet, i2);
    }

    @Override
    public void onSizeChanged(int i2, int i3, int i4, int i5) {
        super.onSizeChanged(i2, i3, i4, i5);
        float f = (float) i3;
        this.startHeight = (2.0f * f) / 3.0f;
        this.endHeight = f / 3.0f;
    }

    @Override
    public void onDraw(Canvas canvas) {
        float[][] fArr;
        if (this.arrayFloat == null) {
            Paint paint2 = new Paint(1);
            this.paint = paint2;
            paint2.setColor(-12303292);
            paint2.setColor(Color.parseColor("#59009F"));
            this.paint.setStyle(Paint.Style.STROKE);
            this.paint.setStrokeWidth(1.0f);
            this.path = new Path();
            Point point = new Point(0, (int) this.startHeight);
            Point point2 = new Point(getWidth() * 2, (int) this.startHeight);
            Point point3 = new Point(getWidth(), (int) this.endHeight);
            double calculate = calculate(point3, point);
            double calculate2 = calculate(point, point2);
            float calculate3 = (float) (((calculate * calculate2) * calculate(point2, point3)) / (((double) ((this.startHeight - this.endHeight) * 2.0f)) * calculate2));
            float degrees = (float) Math.toDegrees(Math.atan2((((float) point3.y) + calculate3) - ((float) point.y), point3.x - point.x));
            this.path.arcTo(
                    new RectF(
                            ((float) point3.x) - calculate3, (float) point3.y,
                            ((float) point3.x) + calculate3,
                            ((float) point3.y) + (calculate3 * 2.0f)),
                    180.0f + degrees,
                    90.0f - degrees,
                    true);
            this.arrayFloat = (float[][]) Array.newInstance(Float.TYPE, 9, 2);
            PathMeasure pathMeasure2 = new PathMeasure(this.path, false);
            this.pathMeasure = pathMeasure2;
            this.pathMeasureLength = pathMeasure2.getLength();
            this.gestureDetectorCompat = new GestureDetectorCompat(getContext(), new Listener());
        }
        canvas.drawPath(this.path, this.paint);
        int i2 = 0;
        while (true) {
            fArr = this.arrayFloat;
            if (i2 >= fArr.length) {
                break;
            }
            a(i2);
            float[][] fArr2 = this.arrayFloat;
            if (fArr2[i2][1] > 0.0f) {
                Bitmap bitmap2 = this.bitmap;
                float f = fArr2[i2][0];
                float f2 = this.c;
                canvas.drawBitmap(bitmap2, f - f2, fArr2[i2][1] - f2, null);
            }
            i2++;
        }
        if (fArr[0][1] == 0.0f) {
            a(0);
            Bitmap bitmap3 = this.bitmap;
            float[][] fArr3 = this.arrayFloat;
            float f3 = fArr3[0][0];
            float f4 = this.c;
            canvas.drawBitmap(bitmap3, f3 - f4, fArr3[0][1] - f4, null);
        }
        if (this.i == 0.0f) {
            float[][] fArr4 = this.arrayFloat;
            float f5 = fArr4[5][0] - fArr4[4][0];
            this.i = f5;
            float f6 = this.pathMeasureLength;
            this.j = (f6 - (6.0f * 78.0f)) / f5;
            this.k = 78.0f / f5;
        }
    }

    public final void startAnimation(final boolean z, final boolean z2) {
        float f;
        if (z2) {
            f = 0.0f;
        } else {
            f = this.i;
            if (!z) {
                f = -f;
            }
        }
        int abs = (int) Math.abs(((f - this.h) * 250.0f) / this.i);

        ValueAnimator valueAnimator2 = this.valueAnimator;
        if (valueAnimator2 != null && valueAnimator2.isRunning()) {
            this.valueAnimator.end();
        }
        ValueAnimator ofFloat = ValueAnimator.ofFloat(this.h, f);
        this.valueAnimator = ofFloat;
        ofFloat.setInterpolator(new DecelerateInterpolator());
        this.valueAnimator.setDuration(abs);
        this.valueAnimator.addUpdateListener(this::animationSet);
        this.valueAnimator.addListener(new Animator.AnimatorListener() {

            @Override
            public final void onAnimationRepeat(Animator animator) {
            }

            @Override
            public final void onAnimationStart(Animator animator) {
                if (z2) {
                    return;
                }
                tasbihControl.onBead(z);
            }

            @Override
            public final void onAnimationEnd(Animator animator) {
                TasbihView.this.h = 0.0f;
                TasbihView.this.invalidate();
            }

            @Override
            public final void onAnimationCancel(Animator animator) {
                TasbihView.this.h = 0.0f;
                TasbihView.this.invalidate();
            }
        });
        this.valueAnimator.start();
    }

    public void animationSet(ValueAnimator valueAnimator2) {
        this.h = (Float) valueAnimator2.getAnimatedValue();
        invalidate();
    }

    private void a(int i2) {
        float f;
        float f2;
        float f3;
        if (i2 <= 4) {
            f = 78.0f * (((float) i2) - 0.5f);
        } else {
            f = this.pathMeasureLength + (78.0f * (((float) i2) - 7.5f));
        }
        float f4 = this.h;
        if (f4 != 0.0f) {
            if ((i2 != 4 || f4 <= 0.0f) && (i2 != 5 || this.h >= 0.0f)) {
                f3 = this.h;
                f2 = this.k;
            } else {
                f3 = this.h;
                f2 = this.j;
            }
            f += f3 * f2;
        }
        if (f < 0.0f) {
            if (i2 + 3 < this.arrayFloat.length) {
                a(i2, false);
            }
        } else if (f <= this.pathMeasureLength) {
            this.pathMeasure.getPosTan(f, this.arrayFloat[i2], null);
        } else if (i2 - 2 >= 0) {
            a(i2, true);
        }
    }

    private void a(int i2, boolean z) {
        if (z) {
            float[][] fArr = this.arrayFloat;
            if (fArr[i2][1] == 0.0f) {
                fArr[i2][1] = (fArr[i2 - 1][1] * 2.0f) - fArr[i2 - 2][1];
            }
            float[][] fArr2 = this.arrayFloat;
            fArr2[i2][0] = (fArr2[i2 - 1][0] * 2.0f) - fArr2[i2 - 2][0];
            return;
        }
        int i3 = i2 + 2;
        int i4 = i2 + 3;
        int i5 = i2 + 1;
        float[][] fArr3 = this.arrayFloat;
        fArr3[i2][1] = (((fArr3[i5][1] - fArr3[i3][1]) * 2.0f) - (fArr3[i3][1] - fArr3[i4][1])) + fArr3[i5][1];
        fArr3[i2][0] = (fArr3[i5][0] + (fArr3[i4][0] - fArr3[i3][0])) - ((fArr3[i3][0] - fArr3[i5][0]) * 2.0f);
    }

    public void initMain(TasbihControl tasbihControl) {
        this.tasbihControl = tasbihControl;

        Bitmap decodeResource = BitmapFactory.decodeResource(getResources(), R.drawable.img_bead_new);
        this.bitmap = decodeResource;
        this.bitmap = Bitmap.createScaledBitmap(decodeResource, (int) 78.0f, (int) 78.0f, true);
        this.c = 78.0f / 2.0f;
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        try {
            if (this.gestureDetectorCompat.onTouchEvent(motionEvent)) {
                return true;
            }
            if (motionEvent.getAction() == 1 && this.o != null) {
                checkAnimation();
            }
            return super.onTouchEvent(motionEvent);
        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }
    }

    public final void checkAnimation() {
        if (this.onDown) {
            this.i = 0.0f;
        }
        float f = this.h;
        if (f != 0.0f) {
            float f2 = this.i;
            boolean z = false;
            if (f <= (-f2)) {
                tasbihControl.onBead(false);
                this.h = 0.0f;
                invalidate();
                return;
            }
            boolean z2 = true;
            if (f >= f2) {
                tasbihControl.onBead(true);
                this.h = 0.0f;
                invalidate();
                return;
            }
            Boolean bool = this.isPosition;
            if (bool == null || bool) {
                Boolean bool2 = this.o;
                if (bool2 != null && !bool2) {
                    z = true;
                }
                startAnimation(true, z);
                return;
            }
            Boolean bool3 = this.o;
            if (bool3 == null || !bool3) {
                z2 = false;
            }
            startAnimation(false, z2);
        }
    }

    public double calculate(Point point, Point point2) {
        return Math.sqrt(Math.pow(point.x - point2.x, 2.0d) + Math.pow(point.y - point2.y, 2.0d));
    }
}

