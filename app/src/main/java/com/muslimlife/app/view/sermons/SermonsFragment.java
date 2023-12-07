package com.muslimlife.app.view.sermons;

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
import androidx.recyclerview.widget.RecyclerView;

import com.muslimlife.app.data.model.parameters.SermonsResponse;
import com.muslimlife.app.data.repository.UserRepository;
import com.muslimlife.app.utils.Constants;
import com.muslimlife.app.utils.ServicesUtil;
import com.muslimlife.app.view.MainActivity;
import com.muslimlife.app.view.base.BaseFragment;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import com.muslimlife.app.databinding.FragmentSermonsBinding;

import java.util.Collections;
import java.util.List;

public class SermonsFragment extends BaseFragment {

    @Inject
    UserRepository userRepository;

    private FragmentSermonsBinding binding;
    SermonsAdapter sermonsAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSermonsBinding.inflate(inflater, container, false);
        ((MainActivity)requireActivity()).setMenu(Constants.MENU_OFF);

        ServicesUtil.updateScore(getActivity().getPreferences(Context.MODE_PRIVATE), Constants.SERVICE_SERMONS);

        binding.backButton.setOnClickListener(view-> Navigation.findNavController(requireView()).popBackStack());

        getSermons();

        return binding.getRoot();
    }

    void getSermons(){
        userRepository.getSermons().subscribe(new SingleObserver<>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onSuccess(@NonNull List<SermonsResponse> sermonsResponses) {
                startRv(sermonsResponses);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Toast.makeText(requireContext(), e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    private void startRv(List<SermonsResponse> sermonsResponses){
        Collections.reverse(sermonsResponses);
        sermonsAdapter = new SermonsAdapter(sermonsResponses, binding);
        LinearLayoutManager llmOn = new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false);
        binding.sermonsRv.setLayoutManager(llmOn);
        binding.sermonsRv.setAdapter(sermonsAdapter);
        binding.sermonsRv.setItemViewCacheSize(10);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if(sermonsAdapter != null)
            sermonsAdapter.stop();
    }

    @Override
    public void showError(String error) {
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
    }
}