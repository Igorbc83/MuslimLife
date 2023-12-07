package com.muslimlife.app.view.quastions;

import static com.muslimlife.app.utils.Constants.IMAM_FILTER_ID;
import static com.muslimlife.app.utils.Constants.MENU_ANSWER;
import static com.muslimlife.app.utils.Constants.QUASTIOON_ID;

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
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import com.muslimlife.app.R;
import com.muslimlife.app.data.model.GetQuastionsReq;
import com.muslimlife.app.data.model.GetQuastionsRes;
import com.muslimlife.app.data.model.QuastionModel;
import com.muslimlife.app.data.repository.UserRepository;
import com.muslimlife.app.databinding.FragmentQuastionsBinding;
import com.muslimlife.app.utils.Constants;
import com.muslimlife.app.view.MainActivity;
import com.muslimlife.app.view.base.BaseFragment;
import com.muslimlife.app.view.dialogs.ChooseKindDialog;

public class QuastionsFragment extends BaseFragment implements QuastionsView {

    @Inject
    UserRepository userRepository;
    String choosedFilter;

    private FragmentQuastionsBinding binding;
    ArrayList<QuastionModel> active=new ArrayList<>();
    ArrayList<QuastionModel> inActive=new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentQuastionsBinding.inflate(inflater, container, false);

        binding.backButton.setOnClickListener(v-> Navigation.findNavController(requireView()).popBackStack());

        ((MainActivity)requireActivity()).checkQuastions();

        ((MainActivity)requireActivity()).setMenu(MENU_ANSWER);

        if (getArguments() != null)
            choosedFilter=getArguments().getString(IMAM_FILTER_ID);
        else
            choosedFilter="-1";


        binding.imamQuastionFilter.setOnClickListener(v->{
            if(userRepository.getQuastionType()!=null){
                ChooseKindDialog chooseKindDialog = new ChooseKindDialog(userRepository.getQuastionTypeRes(), choosedFilter, true);
                chooseKindDialog.show(requireActivity().getSupportFragmentManager(), "ChooseKindDialog");
            }else
                Toast.makeText(requireContext(), requireContext().getString(R.string.filtersError), Toast.LENGTH_SHORT).show();
        });

        getQuastions();

        return binding.getRoot();
    }

    void getQuastions(){
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        GetQuastionsReq getAnswerReq = new GetQuastionsReq(sharedPref.getString(Constants.SP_KEY_USER_ID, "0"));
        QuastionsView quastionsView = this;
        userRepository.getQuastions(getAnswerReq).subscribe(new SingleObserver<GetQuastionsRes[]>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {}

            @Override
            public void onSuccess(@NonNull GetQuastionsRes[] getQuastionsRes) {
                active.clear();
                inActive.clear();

                if(getQuastionsRes.length<1){
                    Toast.makeText(requireContext(), requireContext().getString(R.string.emptyQuastion), Toast.LENGTH_SHORT).show();
                    return;
                }

                for (GetQuastionsRes quastion : getQuastionsRes)
                    if(quastion.getAnswered().equals("0"))
                        active.add(new QuastionModel(quastion));
                    else
                        inActive.add(new QuastionModel(quastion));

                if (active.size() > 0) {
                    QuastionsAdapter activeAdapter = new QuastionsAdapter(active, true,
                            userRepository.getQuastionTypeRes(), choosedFilter, quastionsView);
                    LinearLayoutManager llm = new LinearLayoutManager(requireContext());
                    llm.setOrientation(LinearLayoutManager.VERTICAL);
                    binding.rvActive.setLayoutManager(llm);
                    binding.rvActive.setNestedScrollingEnabled(false);
                    binding.rvActive.setAdapter(activeAdapter);
                }

                if (inActive.size()>0){
                    QuastionsAdapter inActiveAdapter = new QuastionsAdapter(inActive, false,
                            userRepository.getQuastionTypeRes(), choosedFilter, quastionsView);
                    LinearLayoutManager llmm = new LinearLayoutManager(requireContext());
                    llmm.setOrientation(LinearLayoutManager.VERTICAL);
                    binding.rvInActive.setLayoutManager(llmm);
                    binding.rvInActive.setNestedScrollingEnabled(false);
                    binding.rvInActive.setAdapter(inActiveAdapter);
                }
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

    @Override
    public void chooseQuastion(String id) {
        Bundle bundle = new Bundle();
        bundle.putString(QUASTIOON_ID, id);
        Navigation.findNavController(requireView()).navigate(R.id.QuastionAnswer, bundle);
    }
}
