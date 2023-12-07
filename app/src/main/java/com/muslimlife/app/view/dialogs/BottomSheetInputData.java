package com.muslimlife.app.view.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.muslimlife.app.data.model.DialogInputType;
import com.muslimlife.app.utils.Constants;

import com.muslimlife.app.R;
import com.muslimlife.app.databinding.BottomSheetInputBinding;

public class BottomSheetInputData extends BottomSheetDialogFragment {

    private BottomSheetInputBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = BottomSheetInputBinding.inflate(inflater, container, false);

        parseArguments();
        initListeners();

        return binding.getRoot();
    }

    private void parseArguments() {
        if (getArguments() != null) {
            DialogInputType type = BottomSheetInputDataArgs.fromBundle(getArguments()).getType();
            String currentText = BottomSheetInputDataArgs.fromBundle(getArguments()).getCurrentText();

            String title = "";
            switch (type) {
                case NAME:
                    title = getString(R.string.input_type_name);
                    break;
                case LAST_NAME:
                    title = getString(R.string.input_type_second_name);
                    break;
                case EMAIL:
                    title = getString(R.string.input_type_email);
                    break;
                case PHONE_NUMBER:
                    title = getString(R.string.input_type_phone);
                    break;
            }

            binding.title.setText(title);

            if (currentText != null)
                binding.editText.setText(currentText);
        }
    }

    private void initListeners() {
        binding.dialogClose.setOnClickListener(v -> requireActivity().getOnBackPressedDispatcher().onBackPressed());

        binding.dialogSaveButton.setOnClickListener(v -> {
            if (getView() == null || Navigation.findNavController(requireActivity(), R.id.nav_host).getPreviousBackStackEntry() == null) {
                return;
            }

            Bundle bundle = new Bundle();
            bundle.putString(Constants.KEY_DIALOG_INPUT_RESULT_TEXT, binding.editText.getText().toString());

            requireActivity().getSupportFragmentManager().setFragmentResult(Constants.KEY_DIALOG_INPUT_RESULT, bundle);

            Navigation.findNavController(requireActivity(), R.id.nav_host).navigateUp();
        });
    }
}
