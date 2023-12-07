package com.muslimlife.app.view.diaspoarQuastion;

import static com.muslimlife.app.utils.Constants.MENU_OFF;
import static com.muslimlife.app.utils.Constants.OPEN_AUTH;
import static com.muslimlife.app.utils.Constants.SP_KEY_USER_ID;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import com.muslimlife.app.R;
import com.muslimlife.app.data.model.DiaspoarResponce;
import com.muslimlife.app.data.model.SendQuastionRes;
import com.muslimlife.app.data.model.parameters.SendQuastionReq;
import com.muslimlife.app.data.repository.UserRepository;
import com.muslimlife.app.databinding.FragmentDiaspoarQuastionBinding;
import com.muslimlife.app.view.MainActivity;
import com.muslimlife.app.view.base.BaseFragment;

public class DiaspoarQuastionFragment extends BaseFragment {


    private FragmentDiaspoarQuastionBinding binding;

    @Inject
    UserRepository userRepository;
    DiaspoarResponce diaspoarResponce;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDiaspoarQuastionBinding.inflate(inflater, container, false);
        ((MainActivity)requireActivity()).setMenu(MENU_OFF);
        binding.backButton.setOnClickListener(onClickListener);
        binding.diaspoarQustionSendButton.setOnClickListener(onClickListener);

        diaspoarResponce=userRepository.getDiaspoarResponce();

        return binding.getRoot();
    }

    View.OnClickListener onClickListener = v -> {
        switch (v.getId()){
            case R.id.back_button:
                Navigation.findNavController(requireView()).popBackStack();
                break;
            case R.id.diaspoar_qustion_send_button:
                if(binding.qustionText.getText().length()<1)
                    Toast.makeText(requireContext(), requireContext().getString(R.string.answerText), Toast.LENGTH_SHORT).show();
                else
                    sendQuastion();
                break;
        }
    };

    public void sendQuastion(){
        SharedPreferences sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE);
        String id = sharedPref.getString(SP_KEY_USER_ID, "");
        if(id.equals("")){
            Bundle bundle = new Bundle();
            bundle.putBoolean(OPEN_AUTH, false);
            Navigation.findNavController(requireView()).navigate(R.id.newAuthFragment, bundle);
            return;
        }
        SendQuastionReq sendQuastionReq = new SendQuastionReq(id, diaspoarResponce.getImam_id(), binding.qustionText.getText().toString());

        userRepository.sendQuastion(sendQuastionReq).subscribe(new SingleObserver<SendQuastionRes>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onSuccess(@NonNull SendQuastionRes sendQuastionRes) {
                Toast.makeText(requireContext(), requireContext().getString(R.string.answerMaked), Toast.LENGTH_SHORT).show();
                Navigation.findNavController(requireView()).navigate(R.id.mainFragment);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void showError(String error) {
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
    }
}
