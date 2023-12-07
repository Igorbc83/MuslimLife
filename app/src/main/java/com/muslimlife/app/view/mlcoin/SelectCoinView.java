package com.muslimlife.app.view.mlcoin;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.muslimlife.app.R;
import com.muslimlife.app.databinding.ItemDonationTypeBinding;

public class SelectCoinView extends FrameLayout {

    private ItemDonationTypeBinding binding;

    public SelectCoinView(@NonNull Context context) {
        super(context);

        init();
    }

    public SelectCoinView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public SelectCoinView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        binding = ItemDonationTypeBinding.inflate(inflater);

        this.addView(binding.getRoot());
    }

    public void setText(String text){
        binding.donationItemText.setText(text);
    }

    public void setSelected(boolean selected){
        binding.donationItem.setBackgroundTintList(
                ColorStateList.valueOf(
                        binding.getRoot().getResources().getColor(selected ? R.color.light_white : R.color.gray_block, null)));

        binding.donationItemText.setTextColor(
                binding.getRoot().getResources().getColor(selected ? R.color.black : R.color.gray, null));
    }
}
