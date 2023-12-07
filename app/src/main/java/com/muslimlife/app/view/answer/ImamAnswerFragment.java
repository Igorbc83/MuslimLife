package com.muslimlife.app.view.answer;

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
import com.muslimlife.app.data.model.GetAnswerRes;
import com.muslimlife.app.data.model.UpdateAnswerReq;
import com.muslimlife.app.data.model.UpdateAnswerRes;
import com.muslimlife.app.data.repository.UserRepository;
import com.muslimlife.app.utils.Constants;
import com.muslimlife.app.view.MainActivity;
import com.muslimlife.app.view.base.BaseFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import com.muslimlife.app.R;
import com.muslimlife.app.databinding.FragmentImamAnswerBinding;

public class ImamAnswerFragment extends BaseFragment {

    private FragmentImamAnswerBinding binding;

    @Inject
    UserRepository userRepository;
    GetAnswerRes answerRes;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentImamAnswerBinding.inflate(inflater, container, false);

        binding.backButton.setOnClickListener(v-> Navigation.findNavController(requireView()).popBackStack());

        ((MainActivity)requireActivity()).setMenu(Constants.MENU_OFF);

        answerRes = userRepository.getAnswerRes();


        binding.textTv.setText(answerRes.getQuestion_text());
        binding.answerName.setText(answerRes.getImam_first_name()+" "+answerRes.getImam_last_name());
        binding.answerFullText.setText(answerRes.getAnswer_text());

        Glide.with(requireContext()).load(answerRes.getImam_avater()).into(binding.avatarIv);

        updateAnswer();

        try {
            SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            SimpleDateFormat myFormat = new SimpleDateFormat("dd MMM kk:mm");
            fromUser.setTimeZone(TimeZone.getTimeZone("GMT"));
            String dateS = myFormat.format(fromUser.parse(answerRes.getQuestion_date()));
            binding.answerDateImam.setText(dateS);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            SimpleDateFormat myFormat = new SimpleDateFormat("dd MMM kk:mm");
            fromUser.setTimeZone(TimeZone.getTimeZone("GMT"));
            String dateS = myFormat.format(fromUser.parse(answerRes.getAnswer_date()));
            binding.answerDate.setText(dateS);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return binding.getRoot();
    }

    void updateAnswer(){
        userRepository.updateAnswer(new UpdateAnswerReq(answerRes.getId())).subscribe(new SingleObserver<UpdateAnswerRes>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onSuccess(@NonNull UpdateAnswerRes updateAnswerRes) {
                Log.d("id", String.valueOf(updateAnswerRes.isSuccess()));
            }

            @Override
            public void onError(@NonNull Throwable e) {
                //Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void showError(String error) {
        //Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
    }
}
