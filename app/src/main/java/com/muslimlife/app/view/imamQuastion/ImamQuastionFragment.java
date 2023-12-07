package com.muslimlife.app.view.imamQuastion;

import static com.muslimlife.app.utils.Constants.IMAM_FILTER_ID;
import static com.muslimlife.app.utils.Constants.IMAM_ID;
import static com.muslimlife.app.utils.Constants.IMAM_QUASTIONS;
import static com.muslimlife.app.utils.Constants.MENU_OFF;
import static com.muslimlife.app.utils.Constants.OPEN_AUTH;
import static com.muslimlife.app.utils.Constants.SP_KEY_USER_ID;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import com.muslimlife.app.R;
import com.muslimlife.app.data.model.QuastionTypeRes;
import com.muslimlife.app.data.model.SendQuastionRes;
import com.muslimlife.app.data.model.parameters.SendQuastionReq;
import com.muslimlife.app.data.repository.UserRepository;
import com.muslimlife.app.databinding.FragmentImamQuastionBinding;
import com.muslimlife.app.view.MainActivity;
import com.muslimlife.app.view.base.BaseFragment;
import com.muslimlife.app.view.dialogs.ChooseKindDialog;

public class ImamQuastionFragment extends BaseFragment {

    private FragmentImamQuastionBinding binding;

    @Inject
    UserRepository userRepository;

    String imamId;
    String typeId;
    ArrayList<String> types;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentImamQuastionBinding.inflate(inflater, container, false);
        ((MainActivity)requireActivity()).setMenu(MENU_OFF);

        binding.backButton.setOnClickListener(onClickListener);
        binding.imamQuastionKindChoose.setOnClickListener(onClickListener);
        binding.imamQuastionSendButton.setOnClickListener(onClickListener);

        imamId = requireArguments().getString(IMAM_ID);

        typeId = requireArguments().getString(IMAM_FILTER_ID);
        if(typeId!=null)
            for (QuastionTypeRes type : userRepository.getQuastionTypeRes())
                if(type.getId().equals(typeId))
                    binding.imamQuastionKind.setText(type.getName());

        types = requireArguments().getStringArrayList(IMAM_QUASTIONS);
        Log.e("TYPES", "onCreateView:"+types );
        if (types!=null) userRepository.saveTypes(types);
        return binding.getRoot();
    }

    View.OnClickListener onClickListener = v -> {
        NavDirections direction=null;
        switch (v.getId()){
            case R.id.back_button:
                Bundle bundle = new Bundle();
                bundle.putString(IMAM_ID, imamId);
                Navigation.findNavController(requireView()).popBackStack();
                break;
            case R.id.imam_quastion_kind_choose:
                if(userRepository.getQuastionType()!=null){
                    QuastionTypeRes[] quastionType =userRepository.getQuastionTypeRes();
                    ArrayList<QuastionTypeRes> quastionTypesFiltred = new ArrayList<>();

                    for(int i=0;i<userRepository.getTypes().size();i++)
                        for (QuastionTypeRes quastionTypeRes : quastionType)
                            if (quastionTypeRes.getId().equals(userRepository.getTypes().get(i)))
                                quastionTypesFiltred.add(quastionTypeRes);

                    ChooseKindDialog chooseKindDialog = new ChooseKindDialog(quastionTypesFiltred.toArray(new QuastionTypeRes[0]), true, imamId);
                    chooseKindDialog.show(requireActivity().getSupportFragmentManager(), "ChooseKindDialog");
                }else
                    Toast.makeText(requireContext(), requireContext().getString(R.string.filtersError), Toast.LENGTH_SHORT).show();
                break;
            case R.id.imam_quastion_send_button:
                if(typeId!=null)
                    if(binding.imamQuastionText.getText().toString().length()>0)
                        sendQuestion();
                    else
                        Toast.makeText(requireContext(), requireContext().getString(R.string.enterTextQuastion), Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(requireContext(), requireContext().getString(R.string.chooseCategory), Toast.LENGTH_SHORT).show();
                break;
        }

        if(direction!=null)
            Navigation.findNavController(requireView()).navigate(direction);
    };

    public void sendQuestion(){
        SharedPreferences sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE);
        String id = sharedPref.getString(SP_KEY_USER_ID, "0");
        if(id.equals("")){
            Bundle bundle = new Bundle();
            bundle.putBoolean(OPEN_AUTH, false);
            Navigation.findNavController(requireView()).navigate(R.id.newAuthFragment, bundle);
            return;
        }
        SendQuastionReq sendQuastionReq = new SendQuastionReq(id, imamId, binding.imamQuastionText.getText().toString(), Integer.parseInt(typeId));
        userRepository.sendQuastion(sendQuastionReq).subscribe(new SingleObserver<>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onSuccess(@NonNull SendQuastionRes sendQuastionRes) {
                Toast.makeText(requireContext(), requireContext().getString(R.string.answerMaked), Toast.LENGTH_SHORT).show();
                ((MainActivity) requireActivity()).navController.navigate(R.id.mainFragment);
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
