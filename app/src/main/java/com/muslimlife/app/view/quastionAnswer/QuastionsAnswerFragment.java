package com.muslimlife.app.view.quastionAnswer;

import static com.muslimlife.app.utils.Constants.MENU_OFF;
import static com.muslimlife.app.utils.Constants.QUASTIOON_ID;

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
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import com.muslimlife.app.R;
import com.muslimlife.app.data.model.AddAnswerReq;
import com.muslimlife.app.data.model.AddAnswerRes;
import com.muslimlife.app.data.model.GetQuastionsReq;
import com.muslimlife.app.data.model.GetQuastionsRes;
import com.muslimlife.app.data.model.QuastionTypeRes;
import com.muslimlife.app.data.repository.UserRepository;
import com.muslimlife.app.databinding.FragmentQuastionAnswerBinding;
import com.muslimlife.app.utils.Constants;
import com.muslimlife.app.view.MainActivity;
import com.muslimlife.app.view.base.BaseFragment;

public class QuastionsAnswerFragment extends BaseFragment {

    @Inject
    UserRepository userRepository;

    private FragmentQuastionAnswerBinding binding;
    String id;
    GetQuastionsRes quastionsRes;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentQuastionAnswerBinding.inflate(inflater, container, false);
        ((MainActivity)requireActivity()).setMenu(MENU_OFF);

        id = requireArguments().getString(QUASTIOON_ID);

        binding.backButton.setOnClickListener(v->Navigation.findNavController(requireView()).popBackStack());

        binding.sendButton.setOnClickListener(v->{
            if(binding.answerEdit.getText().toString().isEmpty())
                Toast.makeText(requireContext(), requireContext().getString(R.string.enterText), Toast.LENGTH_SHORT).show();
            else
                sendAnswer(binding.answerEdit.getText().toString());
        });

        getQuastions();

        return binding.getRoot();
    }

    void getQuastions(){
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        GetQuastionsReq getAnswerReq = new GetQuastionsReq(sharedPref.getString(Constants.SP_KEY_USER_ID, "0"));
        userRepository.getQuastions(getAnswerReq).subscribe(new SingleObserver<GetQuastionsRes[]>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onSuccess(@NonNull GetQuastionsRes[] getQuastionsRes) {
                for(GetQuastionsRes cycle:getQuastionsRes)
                    if (cycle.getId().equals(id))
                        quastionsRes=cycle;

                if (quastionsRes!=null)
                    enterData();
                else{
                    Toast.makeText(requireContext(), "Ошибка", Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(requireView()).navigate(R.id.QuastionFragment);
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                try {
                    Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }catch (Exception ignored){}
            }
        });
    }

    void enterData(){
        Glide.with(requireContext()).load(quastionsRes.getUser_avatar()).into(binding.avatarIv);
        binding.nameTv.setText(quastionsRes.getFullName());

        try {
            SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            SimpleDateFormat myFormat = new SimpleDateFormat("dd MMM kk:mm");
            fromUser.setTimeZone(TimeZone.getTimeZone("GMT"));
            String dateS = myFormat.format(fromUser.parse(quastionsRes.getUpdated()));
            binding.dateTv.setText(dateS);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        for(QuastionTypeRes quastionTypeRes : userRepository.getQuastionTypeRes())
            if(quastionTypeRes.getId().equals(String.valueOf(quastionsRes.getType_id())))
                binding.title.setText(quastionTypeRes.getName());

        binding.textTv.setText(quastionsRes.getText());

    }

    void sendAnswer(String text){
        AddAnswerReq addAnswerReq = new AddAnswerReq(quastionsRes.getReceiver_id(), quastionsRes.getUser_id(), text, Integer.parseInt(quastionsRes.getId()));
        userRepository.setAnswer(addAnswerReq).subscribe(new SingleObserver<AddAnswerRes>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onSuccess(@NonNull AddAnswerRes addAnswerRes) {
                Toast.makeText(requireContext(), requireContext().getString(R.string.answerOk), Toast.LENGTH_SHORT).show();
                Navigation.findNavController(requireView()).popBackStack();
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void showError(String error) {
        Log.d("error", error);
    }
}
