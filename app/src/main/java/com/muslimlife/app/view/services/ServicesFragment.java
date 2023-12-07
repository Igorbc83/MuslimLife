package com.muslimlife.app.view.services;

import static android.content.Context.MODE_PRIVATE;
import static com.muslimlife.app.utils.Constants.MENU_OFF;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.List;
import java.util.StringTokenizer;

import com.muslimlife.app.data.model.OtherEntity;
import com.muslimlife.app.databinding.FragmentServicesBinding;
import com.muslimlife.app.utils.ServicesUtil;
import com.muslimlife.app.view.MainActivity;
import com.muslimlife.app.view.base.BaseFragment;

public class ServicesFragment extends BaseFragment {

    private FragmentServicesBinding binding;
    private ServicesAdapter servicesAdapterOn,servicesAdapterOff;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentServicesBinding.inflate(inflater, container, false);
        ((MainActivity)requireActivity()).setMenu(MENU_OFF);
        binding.backButton.setOnClickListener(view->{
            Navigation.findNavController(requireView()).popBackStack();
        });

        sharedPreferences=requireActivity().getSharedPreferences("muslimlife.Services",MODE_PRIVATE);

        List<OtherEntity> services = ServicesUtil.getServicesNew();
        load(services);

        servicesAdapterOn = new ServicesAdapter(sharedPreferences, services, true);
        LinearLayoutManager llmOn = new LinearLayoutManager(requireContext());
        llmOn.setOrientation(LinearLayoutManager.VERTICAL);
        binding.servicesVisibleRv.setLayoutManager(llmOn);
        binding.servicesVisibleRv.setAdapter(servicesAdapterOn);

        servicesAdapterOff = new ServicesAdapter(sharedPreferences, services,false);
        LinearLayoutManager llmOff = new LinearLayoutManager(requireContext());
        llmOff.setOrientation(LinearLayoutManager.VERTICAL);
        binding.servicesInvisibleRv.setLayoutManager(llmOff);
        binding.servicesInvisibleRv.setAdapter(servicesAdapterOff);

        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void load(List<OtherEntity> services){
        String savedString = sharedPreferences.getString("muslimlife.Services", "");
        StringTokenizer st = new StringTokenizer(savedString, ",");
        for (int i = 0; i < services.size(); i++) {
            if(savedString.isEmpty())
                services.get(i).setActive(true);
            else
                services.get(i).setActive(Boolean.parseBoolean(st.nextToken()));
        }
    }

    @Override
    public void showError(String error) {
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
    }
}
