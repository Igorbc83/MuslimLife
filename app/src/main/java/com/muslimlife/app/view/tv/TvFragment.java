package com.muslimlife.app.view.tv;

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

import com.muslimlife.app.data.model.TvRes;
import com.muslimlife.app.utils.Constants;
import com.muslimlife.app.utils.ServicesUtil;
import com.muslimlife.app.view.MainActivity;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

import com.muslimlife.app.data.repository.UserRepository;
import com.muslimlife.app.databinding.FragmentTvBinding;

import com.muslimlife.app.view.base.BaseFragment;

public class TvFragment extends BaseFragment {

    private FragmentTvBinding binding;

    @Inject
    UserRepository userRepository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTvBinding.inflate(inflater, container, false);
        ((MainActivity)requireActivity()).setMenu(Constants.MENU_OFF);

        ServicesUtil.updateScore(getActivity().getPreferences(Context.MODE_PRIVATE), Constants.SERVICE_TV);

        binding.backButton.setOnClickListener(view->{
            Navigation.findNavController(requireView()).popBackStack();
        });


        getTv();


        return binding.getRoot();
    }

    private void getTv() {
        userRepository.getTv().subscribe(new SingleObserver<TvRes[]>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onSuccess(@NonNull TvRes[] tvRes) {
                TvAdapter tvAdapter = new TvAdapter(tvRes);
                LinearLayoutManager llm = new LinearLayoutManager(requireContext());
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                binding.tvRv.setLayoutManager(llm);
                binding.tvRv.setAdapter(tvAdapter);
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }
        });
    }

    @Override
    public void showError(String error) {
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
    }
}
