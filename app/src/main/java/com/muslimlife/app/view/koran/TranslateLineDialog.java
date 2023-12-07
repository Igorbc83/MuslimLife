package com.muslimlife.app.view.koran;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.muslimlife.app.R;
import com.muslimlife.app.databinding.DialogLineTranslateBinding;

public class TranslateLineDialog extends BottomSheetDialogFragment {

    private DialogLineTranslateBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogLineTranslateBinding.inflate(inflater, container, false);


        if (getArguments() != null) {
            String text = getArguments().getString("translate_text");
            int ayah = getArguments().getInt("translate_ayah");

            binding.lineCount.setText(requireContext().getString(R.string.ayat) + ayah);
            binding.text.setText(text);
        }

        binding.lineShare.setOnClickListener(v -> Toast.makeText(requireContext(), "share", Toast.LENGTH_SHORT).show());

        return binding.getRoot();
    }
}
