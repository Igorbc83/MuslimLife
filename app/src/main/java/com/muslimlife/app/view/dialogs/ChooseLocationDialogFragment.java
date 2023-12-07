package com.muslimlife.app.view.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.Navigation;

import com.muslimlife.app.data.model.UserLocation;
import com.muslimlife.app.utils.Constants;
import com.muslimlife.app.view.adapters.LocationAdapter;

import java.util.Arrays;
import java.util.List;

import com.muslimlife.app.R;
import com.muslimlife.app.databinding.DialogChooseLocationBinding;

public class ChooseLocationDialogFragment extends DialogFragment {

    private DialogChooseLocationBinding binding;

    private LocationAdapter adapter;

    private String currentId;
    private List<UserLocation> list;
    private UserLocation selectedItem;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogChooseLocationBinding.inflate(inflater, container, false);

        initViewClickListener();
        parseArguments();
        initList();

        return binding.getRoot();
    }

    private void parseArguments() {
        if (getArguments() != null) {
            list = Arrays.asList(ChooseLocationDialogFragmentArgs.fromBundle(getArguments()).getList());
            currentId = ChooseLocationDialogFragmentArgs.fromBundle(getArguments()).getCurrentId();

            if(ChooseLocationDialogFragmentArgs.fromBundle(getArguments()).getType().equals("0"))
                binding.title.setText(R.string.location_country);
            else
                binding.title.setText(R.string.location_city);

            if (currentId != null && !list.isEmpty() && list.stream().filter(userLocation -> userLocation.getId().equals(currentId)).findFirst().isPresent()) {
                list.stream().filter(userLocation -> userLocation.getId().equals(currentId)).findFirst().get().setSelected(true);
            }
        }
    }

    private void initList() {
        if (adapter == null) {
            adapter = new LocationAdapter(position -> selectedItem = list.get(position));
        }

        binding.recyclerView.setAdapter(adapter);
        adapter.setItems(list);
    }

    private void initViewClickListener() {
        binding.countryRedyButton.setOnClickListener(v -> {
            if (getView() == null || Navigation.findNavController(requireActivity(), R.id.nav_host).getPreviousBackStackEntry() == null) {
                return;
            }

            Bundle bundle = new Bundle();
            if(selectedItem!=null){
                bundle.putString(Constants.KEY_DIALOG_INPUT_RESULT_TEXT, selectedItem.getName());
            }

            bundle.putParcelable(Constants.KEY_DIALOG_INPUT_RESULT_USER_LOCATION, selectedItem);
            requireActivity().getSupportFragmentManager().setFragmentResult(Constants.KEY_DIALOG_INPUT_RESULT, bundle);
            Navigation.findNavController(requireActivity(), R.id.nav_host).navigateUp();
        });

        binding.backButton.setOnClickListener(v -> dismiss());
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, com.muslimlife.app.R.style.FullscreenDialogTheme);
    }
}
