package com.muslimlife.app.adapters.holders;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.muslimlife.app.R;
import com.muslimlife.app.data.model.OtherEntity;
import com.muslimlife.app.databinding.ItemMainWorshipBinding;

public class WorshipHolder extends RecyclerView.ViewHolder{

    ItemMainWorshipBinding binding;

    public WorshipHolder(@NonNull ItemMainWorshipBinding binding) {
        super(binding.getRoot());

        this.binding = binding;
    }

    public void initWorship(OtherEntity worship){

        binding.text.setText(binding.text.getResources().getString(worship.getText()));

        binding.image
                .setImageDrawable(ResourcesCompat.getDrawable(
                        binding.image.getContext().getResources(),
                        worship.getDrawable(),
                        null));

        ViewGroup.LayoutParams lp = binding.image.getLayoutParams();
        if(worship.getDrawable() == R.drawable.ic_koran_new_2){
            lp.width = lp.height = binding.image.getResources().getDimensionPixelOffset(R.dimen.main_quran_icon_size);
        }else{
            lp.width = lp.height = binding.image.getResources().getDimensionPixelOffset(R.dimen.default_main_icon_size);
        }
        binding.image.setLayoutParams(lp);

        /*Drawable buttonDrawable = binding.image.getBackground();
        buttonDrawable = DrawableCompat.wrap(buttonDrawable);
        DrawableCompat
                .setTint(buttonDrawable,
                        ResourcesCompat
                                .getColor(
                                        binding.image.getContext().getResources(),
                                        worship.getBackgroundColor(),
                                null));
       binding.image.setBackground(ResourcesCompat.getDrawable(binding.image.getContext().getResources(),R.drawable.shadow_12128,null));*/
    }
}
