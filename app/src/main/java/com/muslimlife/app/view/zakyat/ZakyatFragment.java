package com.muslimlife.app.view.zakyat;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import com.muslimlife.app.data.GetFondsReq;
import com.muslimlife.app.data.model.GetFondsRes;
import com.muslimlife.app.data.repository.UserRepository;
import com.muslimlife.app.utils.Constants;
import com.muslimlife.app.utils.ServicesUtil;
import com.muslimlife.app.view.MainActivity;
import com.muslimlife.app.view.base.BaseFragment;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import com.muslimlife.app.R;
import com.muslimlife.app.databinding.FragmentZakyatBinding;

public class ZakyatFragment extends BaseFragment {

    private FragmentZakyatBinding binding;
    int[]currentMoney={0,0,0,0};

    @Inject
    UserRepository userRepository;

    String title, text;

    View.OnClickListener onClickListener = v -> {
        switch (v.getId()){
            case R.id.back_button:
                Navigation.findNavController(requireView()).popBackStack();
                break;
            case R.id.zakyat_send:
                Toast.makeText(requireContext(),requireContext().getString(R.string.payOk),Toast.LENGTH_SHORT).show();
                //TODO: прикрутить переход на страницу оплаты
                break;
            case R.id.zakyat_set:
                Navigation.findNavController(requireView()).navigate(R.id.zakyatSettings);
                break;
            case R.id.zakyat_inf:
                if(text==null) {
                    Toast.makeText(requireContext(), requireContext().getString(R.string.infoBlock), Toast.LENGTH_SHORT).show();
                    break;
                }
                Bundle bundle = new Bundle();
                bundle.putString(Constants.FOND_TEXT, text);
                bundle.putString(Constants.FOND_TITLE, title);
                bundle.putBoolean(Constants.FOND_CLOSE, true);
                Navigation.findNavController(requireView()).navigate(R.id.textDialog, bundle);
                break;
        }
    };

    @Override
    public void showError(String error) {
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentZakyatBinding.inflate(inflater, container, false);
        binding.backButton.setOnClickListener(onClickListener);
        ((MainActivity)requireActivity()).setMenu(Constants.MENU_OFF);

        ServicesUtil.updateScore(getActivity().getPreferences(Context.MODE_PRIVATE), Constants.SERVICE_ZAKYAT);

        getFonds();

        binding.payCard.setOnClickListener(view ->
                Navigation.findNavController(requireView()).navigate(R.id.zakyatInfoFragment));

        binding.zakyatCash.addTextChangedListener(new TextWatcher(){
            private String current = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {   }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    Double number;
                    binding.zakyatCash.removeTextChangedListener(this);
                    String cleanString = s.toString().replaceAll("\\s+", "");
                    if (!cleanString.isEmpty()) number = Double.parseDouble(cleanString);
                    else number = 0.0;
                    NumberFormat df = new DecimalFormat("#,###,###");
                    current = (df.format(number).replaceAll(",", " "));
                    binding.zakyatCash.setText(current);
                    binding.zakyatCash.setSelection(current.length());
                    binding.zakyatCash.addTextChangedListener(this);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                try{
                    if(!s.toString().isEmpty()){
                        currentMoney[0]=Integer.parseInt(s.toString().replaceAll("\\s+", ""));
                        rasch(currentMoney);
                    }else{
                        currentMoney[0]=0;
                        rasch(currentMoney);
                    }
                }catch (Exception e){
                    Log.e("Error","parseInt");
                }
            }
        });
        binding.zakyatMoneyInBank.addTextChangedListener(new TextWatcher(){
            private String current = "";
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {   }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    Double number;
                    binding.zakyatMoneyInBank.removeTextChangedListener(this);
                    String cleanString = s.toString().replaceAll("\\s+", "");
                    Log.e("TAG",cleanString);
                    if (!cleanString.isEmpty()) number = Double.parseDouble(cleanString);
                    else number = 0.0;
                    NumberFormat df = new DecimalFormat("#,###,###");
                    current = (df.format(number).replaceAll(",", " "));
                    binding.zakyatMoneyInBank.setText(current);
                    binding.zakyatMoneyInBank.setSelection(current.length());
                    binding.zakyatMoneyInBank.addTextChangedListener(this);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                try{
                    if(!s.toString().isEmpty()){
                        currentMoney[1]=Integer.parseInt(s.toString().replaceAll("\\s+", ""));
                        rasch(currentMoney);
                    }else{
                        currentMoney[1]=0;
                        rasch(currentMoney);
                    }
                }catch (Exception e){
                    Log.e("Error","parseInt");
                }
            }
        });
        binding.zakyatGoods.addTextChangedListener(new TextWatcher(){
            private String current = "";
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    Double number;
                    binding.zakyatGoods.removeTextChangedListener(this);
                    String cleanString = s.toString().replaceAll("\\s+", "");
                    if (!cleanString.isEmpty()) number = Double.parseDouble(cleanString);
                    else number = 0.0;
                    NumberFormat df = new DecimalFormat("#,###,###");
                    current = (df.format(number).replaceAll(",", " "));
                    binding.zakyatGoods.setText(current);
                    binding.zakyatGoods.setSelection(current.length());
                    binding.zakyatGoods.addTextChangedListener(this);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                try{
                    if(!s.toString().isEmpty()){
                        currentMoney[2]=Integer.parseInt(s.toString().replaceAll("\\s+", ""));
                        rasch(currentMoney);
                    }else{
                        currentMoney[2]=0;
                        rasch(currentMoney);
                    }
                }catch (Exception e){
                    Log.e("Error","parseInt");
                }
            }
        });
        binding.zakyatGold.addTextChangedListener(new TextWatcher(){
            private String current = "";
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {   }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    Double number;
                    binding.zakyatGold.removeTextChangedListener(this);
                    String cleanString = s.toString().replaceAll("\\s+", "");
                    if (!cleanString.isEmpty()) number = Double.parseDouble(cleanString);
                    else number = 0.0;
                    NumberFormat df = new DecimalFormat("#,###,###");
                    current = (df.format(number).replaceAll(",", " "));
                    binding.zakyatGold.setText(current);
                    binding.zakyatGold.setSelection(current.length());
                    binding.zakyatGold.addTextChangedListener(this);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                try{
                    if(!s.toString().isEmpty()){
                        currentMoney[3]=Integer.parseInt(s.toString().replaceAll("\\s+", ""));
                        rasch(currentMoney);
                    }else{
                        currentMoney[3]=0;
                        rasch(currentMoney);
                    }
                }catch (Exception e){
                    Log.e("Error","parseInt");
                }
            }
        });



        binding.zakyatSend.setOnClickListener(onClickListener);
        binding.zakyatSet.setOnClickListener(onClickListener);
        binding.zakyatInf.setOnClickListener(onClickListener);

        //TODO: Выводить выбранный фонд
        return binding.getRoot();
    }

    void getFonds(){
        userRepository.getFonds(new GetFondsReq("1")).subscribe(new SingleObserver<GetFondsRes[]>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onSuccess(@NonNull GetFondsRes[] getFondsRes) {
                if(getFondsRes.length<1)
                    Toast.makeText(requireContext(), requireContext().getString(R.string.infoBlock), Toast.LENGTH_SHORT).show();
                else {
                    text=getFondsRes[0].getDescription();
                    title=getFondsRes[0].getName();
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    void rasch(int[] money){
        int sum=0;
        for (int i=0; i<money.length;i++) sum+=money[i];

        if(sum!=0){
            binding.zakyatSumm.setText(requireContext().getString(R.string.yourZakyat)+" "+String.format("%.2f",sum*0.025)+" "+requireContext().getString(R.string.rubl));
        }
    }
}