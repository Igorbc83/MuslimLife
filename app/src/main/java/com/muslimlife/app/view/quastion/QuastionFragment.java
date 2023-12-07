package com.muslimlife.app.view.quastion;

import static com.muslimlife.app.utils.Constants.IMAM_FILTER_ID;
import static com.muslimlife.app.utils.Constants.IMAM_ID;
import static com.muslimlife.app.utils.Constants.MENU_OFF;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import com.muslimlife.app.R;
import com.muslimlife.app.data.model.ImamResMain;
import com.muslimlife.app.data.repository.UserRepository;
import com.muslimlife.app.databinding.FragmentQuastionBinding;
import com.muslimlife.app.utils.Constants;
import com.muslimlife.app.utils.ServicesUtil;
import com.muslimlife.app.view.MainActivity;
import com.muslimlife.app.view.base.BaseFragment;
import com.muslimlife.app.view.dialogs.ChooseKindDialog;

public class QuastionFragment extends BaseFragment implements QuaistionView {

    @Inject
    UserRepository userRepository;

    private FragmentQuastionBinding binding;
    QuastionAdapter quastionAdapter;
    String choosedFilter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentQuastionBinding.inflate(inflater, container, false);
        ((MainActivity)requireActivity()).setMenu(MENU_OFF);

        ServicesUtil.updateScore(requireActivity().getPreferences(Context.MODE_PRIVATE), Constants.SERVICE_QUESTION);

        binding.backButton.setOnClickListener(onClickListener);
        binding.imamQuastionMessage.setOnClickListener(onClickListener);
        binding.imamQuastionFilter.setOnClickListener(onClickListener);
        getImams();

        if (getArguments() != null)
            choosedFilter=getArguments().getString(IMAM_FILTER_ID);
        else
            choosedFilter="-1";


        return binding.getRoot();
    }

    View.OnClickListener onClickListener = v -> {
        switch (v.getId()){
            case R.id.back_button:
                Navigation.findNavController(requireView()).popBackStack();
                break;
            case R.id.imam_quastion_message:
                NavDirections action= QuastionFragmentDirections.quastionFragmentToMessageFragment();
                Navigation.findNavController(requireView()).navigate(action);
                break;
            case R.id.imam_quastion_filter:
                if(userRepository.getQuastionType()!=null){
                    ChooseKindDialog chooseKindDialog = new ChooseKindDialog(userRepository.getQuastionTypeRes(), choosedFilter);
                    chooseKindDialog.show(requireActivity().getSupportFragmentManager(), "ChooseKindDialog");
                }else
                    Toast.makeText(requireContext(), requireContext().getString(R.string.filtersError), Toast.LENGTH_SHORT).show();

                break;

        }
    };

    public void getImams(){
        userRepository.getImams(requireActivity().getPreferences(Context.MODE_PRIVATE).getString(Constants.SP_KEY_USER_ID, "0")).subscribe(new SingleObserver<ImamResMain[]>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onSuccess(@NonNull ImamResMain[] imamRes) {
                setAdapter(imamRes);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setAdapter(ImamResMain[] imamRes){
        quastionAdapter=new QuastionAdapter(requireView(), imamRes, choosedFilter, this);
        LinearLayoutManager llm = new LinearLayoutManager(requireContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        binding.quastionRv.setLayoutManager(llm);
        binding.quastionRv.setAdapter(quastionAdapter);
    }

    @Override
    public void showError(String error) {
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void chooseImam(ImamResMain imamResMain) {
        Bundle bundle = new Bundle();
        bundle.putString(IMAM_ID, imamResMain.getId());
        ((MainActivity)requireActivity()).navController.navigate(R.id.ImamFragment,bundle);
    }
}
