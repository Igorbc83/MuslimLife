package com.muslimlife.app.adapters.holders;

import android.graphics.Typeface;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import com.muslimlife.app.R;
import com.muslimlife.app.data.model.KoranTestRes;
import com.muslimlife.app.databinding.ItemKoranPageFullBinding;
import com.muslimlife.app.utils.KoranUtil;
import com.muslimlife.app.utils.MetricsUtil;
import com.muslimlife.app.view.koran.listeners.KoranPageListener;

public class KoranHolder extends RecyclerView.ViewHolder{

    private static final String TAG = "KoranHolderTAG";
    ItemKoranPageFullBinding binding;
    KoranPageListener listener;

    KoranTestRes koranRes;

    List<Character> characters = new ArrayList<>();

    public KoranHolder(@NonNull ItemKoranPageFullBinding binding) {
        super(binding.getRoot());

        this.binding = binding;
    }

    public void initKoranPage(boolean stateFull,
                              KoranTestRes koranRes,
                              DisplayMetrics displayMetrics,
                              KoranPageListener listener,
                              int position, int lastPosition){

        this.listener = listener;
        this.koranRes = koranRes;

        if(position == 0 || position == lastPosition){
            binding.pageBorder.setVisibility(View.GONE);
            binding.koranText.setText("");
            int wight = (displayMetrics.widthPixels - MetricsUtil.convertDpToPx(30, binding.koranText.getResources())) / 3;
            int height = displayMetrics.heightPixels;
            binding.getRoot().setLayoutParams(new FrameLayout.LayoutParams(wight, height));

            checkSurah();
        }else {
            binding.koranText.setText(reversePage(koranRes.getPage()));

            Typeface typeface = ResourcesCompat.getFont(binding.koranText.getContext(), KoranUtil.getPageFont(Integer.parseInt(koranRes.getNumber())));
            binding.koranText.setTypeface(typeface);

            checkSurah();

            if (stateFull) {
                binding.pageBorder.setVisibility(View.GONE);
                //int height = displayMetrics.heightPixels - MetricsUtil.convertDpToPx(159, binding.koranText.getResources());
                binding.getRoot().setLayoutParams(new FrameLayout.LayoutParams(displayMetrics.widthPixels, ViewGroup.LayoutParams.MATCH_PARENT));

                binding.koranText.setOnTouchListener((v, event) -> {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        Layout layout = ((TextView) v).getLayout();
                        int x = (int) event.getX();
                        int y = (int) event.getY();
                        int offset = -1;
                        if (layout != null) {
                            int line = layout.getLineForVertical(y);
                            offset = layout.getOffsetForHorizontal(line, x);

                            if (offset >= 0)
                                selectLine(offset, koranRes.getSymbols());
                            //Toast.makeText(getContext(), ""+offset, Toast.LENGTH_SHORT).show();
                        }
                    }
                    return true;
                });

            } else {
                binding.pageBorder.setVisibility(View.VISIBLE);
                int wight = displayMetrics.widthPixels - MetricsUtil.convertDpToPx(80, binding.koranText.getResources());
                int height = displayMetrics.heightPixels - MetricsUtil.convertDpToPx(273, binding.koranText.getResources());
                binding.getRoot().setLayoutParams(new FrameLayout.LayoutParams(wight, ViewGroup.LayoutParams.MATCH_PARENT));

                binding.getRoot().setOnClickListener(v -> listener.selectPage(position));
            }
        }
    }

    private void selectLine(int offset, String symbols){

        characters = new ArrayList<>();

        char startSelected = binding.koranText.getText().toString().charAt(offset);
        int reverseOffset = koranRes.getPage().indexOf(startSelected);

        findEndSymbol(reverseOffset, symbols);
        findStartSymbol(reverseOffset, symbols);

        whenSurah();

        String page = reversePage(koranRes.getPage());

        Spannable spanna = new SpannableString(page);

        int ayah = 0;
        boolean ayahCheck = true;
        for(int i = 0; i < page.length(); i++){
            char pageChar = page.charAt(i);

            if(i < koranRes.getPage().length()) {
                char pageAyahChar = koranRes.getPage().charAt(i);
                if (ayahCheck && koranRes.getSymbols().contains(Character.toString(pageAyahChar))) {
                    ayah++;

                    if (characters.contains(pageAyahChar)) {
                        ayahCheck = false;
                    }
                }
            }

            if(!koranRes.getNumber().equals("591")) {
                if (characters.contains(pageChar)) {
                    spanna.setSpan(new BackgroundColorSpan(binding.koranText.getResources().getColor(R.color.koranTextBg, null)), i, i + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }else {
                if (ayah == 1 && i == 96)
                    continue;

                if (characters.contains(pageChar)) {
                    spanna.setSpan(new BackgroundColorSpan(binding.koranText.getResources().getColor(R.color.koranTextBg, null)), i, i + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }

        if(koranRes.getNumber().equals("591")){
            if(ayah == 19)
                ayah = 18;
            else if(ayah == 18)
                ayah = 19;
            else if(ayah >= 21)
                ayah -= 1;
        }

        Log.d(TAG, "select ayah: " + ayah);

        binding.koranText.setText(spanna);

        int selectedAyah = (koranRes.getStartAyah() - 1) + ayah;
        int selectedSurah = koranRes.getSurahNumber();

        String selectedData = "";

        if (KoranUtil.isMiddleSurah(koranRes.getNumber())) {
            selectedData = KoranUtil.getAyahNumberInMiddleSurah(koranRes.getNumber(), koranRes.getSymbols(), ayah - 1);
        } else if (KoranUtil.isMultiSurahLines(koranRes.getNumber())) {
            selectedData = KoranUtil.getAyahNumberInMultiSurah(koranRes.getNumber(), ayah - 1);
        }

        if(!selectedData.isEmpty()){
            String[] selectedArray = selectedData.split("-");

            selectedAyah = Integer.parseInt(selectedArray[0]);
            selectedSurah += Integer.parseInt(selectedArray[1]);
        }

        listener.ayahSelect(selectedSurah, selectedAyah);
    }

    private String reversePage(String page){
        String[] lines = page.split("\n");
        StringBuilder reversePage = new StringBuilder();
        for(String line : lines){
            StringBuilder builder = new StringBuilder(line);
            reversePage.append(builder.reverse()).append("\n");
        }

        return reversePage.toString();
    }

    private void findStartSymbol(int offset, String symbols){
        for(int i = offset - 1; i >= 0; i--) {
            char temp = koranRes.getPage().charAt(i);
            if (symbols.contains(Character.toString(temp)))
                return;

            if(temp != '\n' && temp != ' ' && temp != '\uF8DB')
                characters.add(temp);
        }
    }

    private void findEndSymbol(int offset, String symbols){
        for(int i = offset; i < koranRes.getPage().length(); i++) {
            char temp = koranRes.getPage().charAt(i);
            if(temp != '\n' && temp != ' ' && temp != '\uF8DB')
                characters.add(temp);

            if (symbols.contains(Character.toString(temp)))
                return;
        }
    }

    private void whenSurah(){
        if(KoranUtil.isTopSingleSurah(koranRes.getNumber())){
            if(!koranRes.getNumber().equals("187")) {
                characters.remove(Character.valueOf(koranRes.getPage().charAt(0)));
                characters.remove(Character.valueOf(koranRes.getPage().charAt(1)));
                characters.remove(Character.valueOf(koranRes.getPage().charAt(2)));
            }

        }else if(KoranUtil.isMiddleSurah(koranRes.getNumber())){
            String line = KoranUtil.getPageLine(koranRes.getPage(), KoranUtil.getMiddleSurahLine(koranRes.getNumber()) + 1);
            for(char c : line.toCharArray())
                characters.remove(Character.valueOf(c));
        }else if(KoranUtil.isMultiSurahLines(koranRes.getNumber())){
            for(int l : KoranUtil.getMultiSurahLines(koranRes.getNumber())){
                String line = KoranUtil.getPageLine(koranRes.getPage(), l + 1);
                for(char c : line.toCharArray())
                    characters.remove(Character.valueOf(c));
            }
        }
    }

    private void checkSurah() {
        clearSurahLine();

        if (KoranUtil.isTopSingleSurah(koranRes.getNumber())) {
            showSurahLine(1);
        } else if (KoranUtil.isMiddleSurah(koranRes.getNumber())) {
            showSurahLine(KoranUtil.getMiddleSurahLine(koranRes.getNumber()));
        } else if (KoranUtil.isMultiSurahLines(koranRes.getNumber())) {
            int[] lines = KoranUtil.getMultiSurahLines(koranRes.getNumber());

            if(lines != null) {
                for (int line : lines)
                    showSurahLine(line);
            }
        } else
            showSurahLine(-1);
    }

    private void clearSurahLine(){
        binding.surahLine1.surahView.setVisibility(View.INVISIBLE);
        binding.surahLine2.surahView.setVisibility(View.INVISIBLE);
        binding.surahLine3.surahView.setVisibility(View.INVISIBLE);
        binding.surahLine4.surahView.setVisibility(View.INVISIBLE);
        binding.surahLine5.surahView.setVisibility(View.INVISIBLE);
        binding.surahLine6.surahView.setVisibility(View.INVISIBLE);
        binding.surahLine7.surahView.setVisibility(View.INVISIBLE);
        binding.surahLine8.surahView.setVisibility(View.INVISIBLE);
        binding.surahLine9.surahView.setVisibility(View.INVISIBLE);
        binding.surahLine10.surahView.setVisibility(View.INVISIBLE);
        binding.surahLine11.surahView.setVisibility(View.INVISIBLE);
        binding.surahLine12.surahView.setVisibility(View.INVISIBLE);
        binding.surahLine13.surahView.setVisibility(View.INVISIBLE);
        binding.surahLine14.surahView.setVisibility(View.INVISIBLE);
        binding.surahLine15.surahView.setVisibility(View.INVISIBLE);
    }

    private void showSurahLine(int line){
        switch (line){
            case 1:
                binding.surahLine1.surahView.setVisibility(View.VISIBLE);
                binding.surahLine1.surahName.setText(KoranUtil.getPageLine(koranRes.getPage(), 1));
                break;
            case 2:
                binding.surahLine2.surahView.setVisibility(View.VISIBLE);
                binding.surahLine2.surahName.setText(KoranUtil.getPageLine(koranRes.getPage(), 2));
                break;
            case 3:
                binding.surahLine3.surahView.setVisibility(View.VISIBLE);
                binding.surahLine3.surahName.setText(KoranUtil.getPageLine(koranRes.getPage(), 3));
                break;
            case 4:
                binding.surahLine4.surahView.setVisibility(View.VISIBLE);
                binding.surahLine4.surahName.setText(KoranUtil.getPageLine(koranRes.getPage(), 4));
                break;
            case 5:
                binding.surahLine5.surahView.setVisibility(View.VISIBLE);
                binding.surahLine5.surahName.setText(KoranUtil.getPageLine(koranRes.getPage(), 5));
                break;
            case 6:
                binding.surahLine6.surahView.setVisibility(View.VISIBLE);
                binding.surahLine6.surahName.setText(KoranUtil.getPageLine(koranRes.getPage(), 6));
                break;
            case 7:
                binding.surahLine7.surahView.setVisibility(View.VISIBLE);
                binding.surahLine7.surahName.setText(KoranUtil.getPageLine(koranRes.getPage(), 7));
                break;
            case 8:
                binding.surahLine8.surahView.setVisibility(View.VISIBLE);
                binding.surahLine8.surahName.setText(KoranUtil.getPageLine(koranRes.getPage(), 8));
                break;
            case 9:
                binding.surahLine9.surahView.setVisibility(View.VISIBLE);
                binding.surahLine9.surahName.setText(KoranUtil.getPageLine(koranRes.getPage(), 9));
                break;
            case 10:
                binding.surahLine10.surahView.setVisibility(View.VISIBLE);
                binding.surahLine10.surahName.setText(KoranUtil.getPageLine(koranRes.getPage(), 10));
                break;
            case 11:
                binding.surahLine11.surahView.setVisibility(View.VISIBLE);
                binding.surahLine11.surahName.setText(KoranUtil.getPageLine(koranRes.getPage(), 11));
                break;
            case 12:
                binding.surahLine12.surahView.setVisibility(View.VISIBLE);
                binding.surahLine12.surahName.setText(KoranUtil.getPageLine(koranRes.getPage(), 12));
                break;
            case 13:
                binding.surahLine13.surahView.setVisibility(View.VISIBLE);
                binding.surahLine13.surahName.setText(KoranUtil.getPageLine(koranRes.getPage(), 13));
                break;
            case 14:
                binding.surahLine14.surahView.setVisibility(View.VISIBLE);
                binding.surahLine14.surahName.setText(KoranUtil.getPageLine(koranRes.getPage(), 14));
                break;
            case 15:
                binding.surahLine15.surahView.setVisibility(View.VISIBLE);
                binding.surahLine15.surahName.setText(KoranUtil.getPageLine(koranRes.getPage(), 15));
                break;
        }

    }
}
