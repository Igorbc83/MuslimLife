package com.muslimlife.app.view.base;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.Objects;

import dagger.android.support.DaggerFragment;

public abstract class BaseFragment extends DaggerFragment {

    String TAG = getClass().getSimpleName();

    public abstract void showError(String error);

    public void openKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        view.requestFocus();
        Objects.requireNonNull(imm).showSoftInput(view, 0);
    }

    public void closeKeyboard() {
        if(getActivity() != null) {
            View view = getActivity().getCurrentFocus();
            if (view instanceof EditText) {
                InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                Objects.requireNonNull(imm).hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }
}
