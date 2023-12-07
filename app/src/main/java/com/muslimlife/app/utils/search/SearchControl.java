package com.muslimlife.app.utils.search;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.google.android.material.button.MaterialButton;

import java.util.Objects;

import com.muslimlife.app.R;


public class SearchControl {

    private SearchListener listener;
    private View searchView;
    private EditText searchEdit;
    private MaterialButton clear;

    public SearchControl(SearchListener listener, View searchView){
        this.listener = listener;
        this.searchView = searchView;
        this.searchEdit = searchView.findViewById(R.id.search_edit);

        initSearch();
    }

    private void initSearch(){
        clear.setVisibility(View.GONE);

        searchEdit.setOnKeyListener((v, keyCode, event) -> {
            if (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                try {
                    searchEdit.clearFocus();
                    closeKeyboard();
                } catch (Exception e){
                    e.printStackTrace();
                }
                return true;
            }
            return false;
        });

        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (searchEdit.toString().isEmpty()){
                    showClear(false);
                } else {
                    showClear(true);
                }
                listener.initSearch(searchEdit.getText().toString());
            }
        });

        clear.setOnClickListener(view -> {
            searchEdit.setText("");
            searchEdit.clearFocus();
            closeKeyboard();
        });
    }

    private void showClear(boolean show){
        if (show){
            clear.setVisibility(View.VISIBLE);
        } else {
            clear.setVisibility(View.GONE);
        }
    }

    public void closeKeyboard() {
        try {
            View view = searchView;
            InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            Objects.requireNonNull(imm).hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public String getSearch(){return searchEdit.getText().toString();}
}
