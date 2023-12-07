package com.muslimlife.app.view.koran;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import com.muslimlife.app.R;
import com.muslimlife.app.view.koran.listeners.KoranPageListener;

public class DynamicSeekBarView extends LinearLayout implements SeekBar.OnSeekBarChangeListener {
    private CustomSeekBar seekBar;
    private View arrow;
    private View tvInfo;
    private EditText pageEdit;
    private FrameLayout llInfo;

    private KoranPageListener listener;

    public DynamicSeekBarView(Context context) {
        super(context);
        init(context, null);
    }

    public DynamicSeekBarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public DynamicSeekBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dynamic_seek_bar_view, this, false);
        seekBar = view.findViewById(R.id.seekBar);
        arrow = view.findViewById(R.id.arrow);
        tvInfo = view.findViewById(R.id.tvInfo);
        pageEdit = view.findViewById(R.id.page_edit);
        llInfo = view.findViewById(R.id.llInfo);
        seekBar.setOnSeekBarChangeListener(this);
        pageEdit.setText(String.valueOf(seekBar.getProgress() + 1));

        pageEdit.setOnEditorActionListener(
                (v, actionId, event) -> {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        try {
                            int position = Integer.parseInt(pageEdit.getText().toString()) - 1;

                            if(position < 0)
                                position = 0;

                            if(position > seekBar.getMax())
                                position = seekBar.getMax();

                            pageEdit.setText(String.valueOf(position + 1));
                            listener.rollPage(position);
                        }catch (Exception ignored){}
                        return true;
                    }
                    return false;
                });

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DynamicSeekBarView, 0, 0);
            int thumbSize = a.getDimensionPixelSize(R.styleable.DynamicSeekBarView_dsbv_thumbSize,
                    getResources().getDimensionPixelSize(R.dimen.default_thumb_size));
            int thumbDrawable = a.getResourceId(R.styleable.DynamicSeekBarView_dsbv_thumbDrawable, 0);
            int progressDrawable = a.getResourceId(R.styleable.DynamicSeekBarView_dsbv_progressDrawable, 0);
            int progressColor = a.getResourceId(R.styleable.DynamicSeekBarView_dsbv_progressColor, 0);
            final int progressBackgroundColor = a.getResourceId(R.styleable.DynamicSeekBarView_dsbv_progressBackgroundColor, 0);
            int infoSize = a.getDimensionPixelSize(R.styleable.DynamicSeekBarView_dsbv_infoSize, 0);
            int infoTextSize = a.getDimensionPixelSize(R.styleable.DynamicSeekBarView_dsbv_infoTextSize, 0);
            int infoTextColor = a.getResourceId(R.styleable.DynamicSeekBarView_dsbv_infoTextColor, 0);
            int infoRadius = a.getInt(R.styleable.DynamicSeekBarView_dsbv_infoRadius, 0);
            int infoBackgroundColor = a.getResourceId(R.styleable.DynamicSeekBarView_dsbv_infoBackgroundColor, 0);
            int maxValueSeekBar = a.getInt(R.styleable.DynamicSeekBarView_dsbv_max, 100);
            final int progressValueSeekBar = a.getInt(R.styleable.DynamicSeekBarView_dsbv_progress, 0);
            boolean isHideInfo = a.getBoolean(R.styleable.DynamicSeekBarView_dsbv_isHideInfo, false);
            String initInfoString = a.getString(R.styleable.DynamicSeekBarView_dsbv_infoInitText);

            if(thumbDrawable!=0) {
                seekBar.setThumb(thumbDrawable, thumbSize);
            }


            PorterDuff.Mode mMode = PorterDuff.Mode.SRC_ATOP;

            if(progressDrawable!=0){
                seekBar.setProgressDrawable(ResourcesCompat.getDrawable(getResources(), progressDrawable, null));
            }else {
                LayerDrawable layerDrawable = (LayerDrawable) seekBar.getProgressDrawable();
                if (progressColor != 0) {
                    Drawable progress = layerDrawable.findDrawableByLayerId(android.R.id.progress);
                    progress.setColorFilter(getColorValue(progressColor), mMode);
                    layerDrawable.setDrawableByLayerId(android.R.id.progress, progress);
                }

                if (progressBackgroundColor != 0) {
                    Drawable background = layerDrawable.findDrawableByLayerId(android.R.id.background);
                    background.setColorFilter(getColorValue(progressBackgroundColor), mMode);
                    layerDrawable.setDrawableByLayerId(android.R.id.background, background);
                }
            }

            seekBar.setMax(maxValueSeekBar);
            seekBar.setProgress(progressValueSeekBar);

            if (!isHideInfo) {
                llInfo.setVisibility(View.VISIBLE);
                if (infoSize != 0) {
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tvInfo.getLayoutParams();
                    params.width = infoSize;
                    tvInfo.setLayoutParams(params);
                }

                if (infoTextSize != 0) {
                    pageEdit.setTextSize(TypedValue.COMPLEX_UNIT_PX, infoTextSize);
                }

                if (infoTextColor != 0) {
                    pageEdit.setTextColor(getColorValue(infoTextColor));
                }

                if (infoBackgroundColor != 0) {
                    GradientDrawable shape = new GradientDrawable();
                    shape.setShape(GradientDrawable.RECTANGLE);
                    if (infoRadius != 0)
                        shape.setCornerRadius(infoRadius);
                    shape.setColor(getColorValue(infoBackgroundColor));
                    tvInfo.setBackground(shape);
                    arrow.getBackground().setColorFilter(getColorValue(infoBackgroundColor), mMode);
                }


                new Handler().postDelayed(() -> setInfoPosition(), 500);

                if (!TextUtils.isEmpty(initInfoString)) {
                    pageEdit.setText(initInfoString);
                } else {
                    pageEdit.setText(String.valueOf(progressValueSeekBar + 1));
                }
            } else {
                llInfo.setVisibility(View.GONE);
            }

        }

        this.addView(view);
    }

    public void setProgress(int value){
        seekBar.setOnSeekBarChangeListener(null);
        seekBar.setProgress(value);
        seekBar.setOnSeekBarChangeListener(this);
        setInfoPosition();
    }


    public void setMax(int max){
        seekBar.setMax(max);
    }

    private int getColorValue(int resId) {
        return ResourcesCompat.getColor(getResources(), resId, null);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
        setInfoPosition();

        if(listener != null)
            listener.rollPage(progress);
    }

    public void setInfoPosition() {
        arrow.setX(getSeekBarThumbPosX(seekBar) - (arrow.getWidth() >> 1));
        tvInfo.setX(getTimePosition(seekBar, tvInfo));
        pageEdit.setText(String.valueOf(seekBar.getProgress() + 1));
    }

    private int getTimePosition(SeekBar seekBar, View viewInfo) {
        int thumbPos = getSeekBarThumbPosX(seekBar);
        int seekBarWidth = seekBar.getWidth();
        if (thumbPos + viewInfo.getWidth() / 2 >= seekBarWidth) {
            return seekBarWidth - viewInfo.getWidth();
        } else if (thumbPos - viewInfo.getWidth() / 2 <= seekBar.getPaddingLeft()) {
            return (int) seekBar.getX();
        } else {
            return thumbPos - viewInfo.getWidth() / 2;
        }
    }

    private int getStepValue(int progress) {
        int stepSize = 1;
        return (Math.round(progress / stepSize)) * stepSize;
    }

    private int getSeekBarThumbPosX(SeekBar seekBar) {
        //Ltr
        /*int width = seekBar.getWidth() - seekBar.getPaddingLeft() - seekBar.getPaddingRight();
        int thumbPosX = seekBar.getPaddingLeft() + width * seekBar.getProgress() / seekBar.getMax();
        return thumbPosX;*/

        //Rtl
        int width = seekBar.getWidth() - seekBar.getPaddingLeft() - seekBar.getPaddingRight();
        int thumbPosX = (seekBar.getPaddingLeft() + seekBar.getPaddingRight() + width) - (seekBar.getPaddingLeft() + width * seekBar.getProgress() / seekBar.getMax());
        return thumbPosX;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    public void setListener(KoranPageListener listener) {
        this.listener = listener;
    }
}
