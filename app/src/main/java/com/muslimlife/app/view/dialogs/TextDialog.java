package com.muslimlife.app.view.dialogs;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.muslimlife.app.data.repository.UserRepository;
import com.muslimlife.app.utils.Constants;

import javax.inject.Inject;

import com.muslimlife.app.R;
import com.muslimlife.app.databinding.DialogTextBinding;

public class TextDialog extends BottomSheetDialogFragment {

    private DialogTextBinding binding;
    @Inject
    UserRepository userRepository;

    View.OnClickListener onClickListener = v -> {
        switch (v.getId() ){
            case R.id.dialog_close:
                dismiss();
                break;
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogTextBinding.inflate(inflater, container, false);

        binding.text.setText(Html.fromHtml(getArguments().getString(Constants.FOND_TEXT), Html.FROM_HTML_MODE_COMPACT));
        binding.dialogProfileChange.setText(getArguments().getString(Constants.FOND_TITLE));
        if (getArguments().getBoolean(Constants.FOND_CLOSE)) binding.dialogClose.setVisibility(View.VISIBLE);
        else binding.dialogClose.setVisibility(View.GONE);
        binding.dialogClose.setOnClickListener(onClickListener);

        return binding.getRoot();
    }
}
