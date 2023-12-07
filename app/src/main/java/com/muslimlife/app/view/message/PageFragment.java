package com.muslimlife.app.view.message;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.muslimlife.app.data.model.GetAnswerRes;
import com.muslimlife.app.data.model.NotificationRes;
import com.muslimlife.app.data.repository.UserRepository;
import com.muslimlife.app.view.base.BaseFragment;

import javax.inject.Inject;

import com.muslimlife.app.R;
import com.muslimlife.app.databinding.FragmentViewpagerBinding;

public class PageFragment extends BaseFragment implements AnswerView {

    private FragmentViewpagerBinding binding;
    GetAnswerRes[] getAnswerRes;
    NotificationRes[] notificationRes;
    String type;
    @Inject
    UserRepository userRepository;

    public PageFragment(GetAnswerRes[] getAnswerRes, String type){
        this.getAnswerRes=getAnswerRes;
        this.type=type;
    }


    public PageFragment(){

    }

    public PageFragment(NotificationRes[] notificationRes, String type){
        this.notificationRes=notificationRes;
        this.type=type;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentViewpagerBinding.inflate(inflater, container, false);


        if(notificationRes!=null && type.equals("notification")){
            NotificationAdapter notificationAdapter = new NotificationAdapter(this, notificationRes);
            LinearLayoutManager llm = new LinearLayoutManager(requireContext());
            llm.setOrientation(LinearLayoutManager.VERTICAL);

            binding.viewPagerRv.setLayoutManager(llm);
            binding.viewPagerRv.setAdapter(notificationAdapter);
        }
        if(getAnswerRes!=null && type.equals("message")){
            AnswersAdapter answersAdapter = new AnswersAdapter(this, getAnswerRes);
            LinearLayoutManager llm = new LinearLayoutManager(requireContext());
            llm.setOrientation(LinearLayoutManager.VERTICAL);

            binding.viewPagerRv.setLayoutManager(llm);
            binding.viewPagerRv.setAdapter(answersAdapter);
        }


        return binding.getRoot();
    }

    public static PageFragment StartView(GetAnswerRes[] getAnswerRes, String type){

        return new PageFragment(getAnswerRes, type);
    }

    public static PageFragment StartView(NotificationRes[] notificationRes, String type){

        return new PageFragment(notificationRes, type);
    }

    public static PageFragment StartView(){

        return new PageFragment();
    }

    @Override
    public void showError(String error) {
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void chooseAnswer(GetAnswerRes getAnswerRes) {
        userRepository.setAnswerRes(getAnswerRes);
        Navigation.findNavController(requireView()).navigate(R.id.ImamAnswerFragment);
    }

    @Override
    public void chooseNotification(NotificationRes notificationRes) {
        userRepository.setNotificationRes(notificationRes);
        Navigation.findNavController(requireView()).navigate(R.id.NotificationFragment);
    }
}
