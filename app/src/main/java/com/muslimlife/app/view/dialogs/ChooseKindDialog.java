package com.muslimlife.app.view.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.muslimlife.app.data.model.QuastionTypeRes;
import com.muslimlife.app.utils.Constants;
import com.muslimlife.app.view.MainActivity;
import com.muslimlife.app.view.adapters.ChooseKindAdapter;

import com.muslimlife.app.R;
import com.muslimlife.app.databinding.DialogChooseKindBinding;

public class ChooseKindDialog extends DialogFragment {

    private DialogChooseKindBinding binding;
    ChooseKindAdapter adapter;
    String choosedFilter="-1";
    QuastionTypeRes[] quastionTypeRes;
    boolean send, quastions;
    String imamId;

    public ChooseKindDialog(QuastionTypeRes[] quastionTypeRes, String choosedFilter){
        this.quastionTypeRes=quastionTypeRes;   this.choosedFilter=choosedFilter;
    }

    public ChooseKindDialog(QuastionTypeRes[] quastionTypeRes, String choosedFilter, boolean quastions){
        this.quastionTypeRes=quastionTypeRes;   this.choosedFilter=choosedFilter;
        this.quastions=quastions;
    }

    public ChooseKindDialog(QuastionTypeRes[] quastionTypeRes, boolean send, String imamId){
        this.quastionTypeRes=quastionTypeRes;
        this.send=send;
        this.imamId=imamId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogChooseKindBinding.inflate(inflater, container, false);

        binding.backButton.setOnClickListener(v-> dismiss());
        NavHostFragment.findNavController(this);
        if (send) binding.tvQuestion.setText(getString(R.string.question_subject));

        binding.kindRedyButton.setOnClickListener(v->{
            if(send){
                choosedFilter = adapter.getChoosed_pos();
                Bundle bundle = new Bundle();
                bundle.putString(Constants.IMAM_ID, imamId);
                bundle.putString(Constants.IMAM_FILTER_ID, choosedFilter);
                ((MainActivity)requireActivity()).navController.navigate(R.id.ImamQuastionFragment,bundle);
                dismiss();
            }else if(quastions){
                choosedFilter = adapter.getChoosed_pos();
                Bundle bundle = new Bundle();
                bundle.putString(Constants.IMAM_FILTER_ID, choosedFilter);
                ((MainActivity)requireActivity()).navController.navigate(R.id.AnswersFragment,bundle);
                dismiss();
            }else{
                choosedFilter = adapter.getChoosed_pos();
                Bundle bundle = new Bundle();
                bundle.putString(Constants.IMAM_FILTER_ID, choosedFilter);
                ((MainActivity)requireActivity()).navController.navigate(R.id.QuastionFragment,bundle);
                dismiss();
            }

        });

        adapter=new ChooseKindAdapter(quastionTypeRes, choosedFilter);
        LinearLayoutManager llm = new LinearLayoutManager(requireContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        binding.rvQuastionKind.setLayoutManager(llm);
        binding.rvQuastionKind.setAdapter(adapter);

        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL,
                android.R.style.Theme_Material_Light_NoActionBar_Fullscreen);

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

}
