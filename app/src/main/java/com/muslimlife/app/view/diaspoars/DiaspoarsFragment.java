package com.muslimlife.app.view.diaspoars;

import static com.muslimlife.app.utils.Constants.MENU_OFF;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import com.muslimlife.app.R;
import com.muslimlife.app.data.model.DiaspoarResponce;
import com.muslimlife.app.data.repository.UserRepository;
import com.muslimlife.app.databinding.FragmentDiasporasBinding;
import com.muslimlife.app.utils.Constants;
import com.muslimlife.app.utils.ServicesUtil;
import com.muslimlife.app.view.MainActivity;
import com.muslimlife.app.view.base.BaseFragment;

public class DiaspoarsFragment extends BaseFragment implements DiaspoarsView {

    private FragmentDiasporasBinding binding;
    DiaspoarsAdapter diaspoarsAdapter;

    @Inject
    UserRepository userRepository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDiasporasBinding.inflate(inflater, container, false);
        ((MainActivity)requireActivity()).setMenu(MENU_OFF);

        ServicesUtil.updateScore(getActivity().getPreferences(Context.MODE_PRIVATE), Constants.SERVICE_DIASPOARS);

        binding.backButton.setOnClickListener(view-> Navigation.findNavController(requireView()).popBackStack());

        getDiaspoars();

        return binding.getRoot();
    }

    void getDiaspoars(){
        DiaspoarsView view = this;
        userRepository.getDiaspoars().subscribe(new SingleObserver<DiaspoarResponce[]>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onSuccess(@NonNull DiaspoarResponce[] diaspoarRes) {
                diaspoarsAdapter = new DiaspoarsAdapter(diaspoarRes, view);
                LinearLayoutManager llm = new LinearLayoutManager(requireContext());
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                binding.diaspoarsRv.setLayoutManager(llm);
                binding.diaspoarsRv.setAdapter(diaspoarsAdapter);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Toast.makeText(getContext().getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void showError(String error) {
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setDiaspoar(DiaspoarResponce diaspoar) {
        userRepository.setDiaspoarResponce(diaspoar);
        Navigation.findNavController(requireView()).navigate(R.id.DiasporFragment);
    }
}
