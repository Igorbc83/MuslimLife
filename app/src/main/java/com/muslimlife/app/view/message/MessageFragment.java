package com.muslimlife.app.view.message;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import com.muslimlife.app.data.model.GetAnswerReq;
import com.muslimlife.app.data.model.GetAnswerRes;
import com.muslimlife.app.data.model.NotificationReq;
import com.muslimlife.app.data.model.NotificationRes;
import com.muslimlife.app.data.repository.UserRepository;
import com.muslimlife.app.utils.Constants;
import com.muslimlife.app.view.MainActivity;
import com.muslimlife.app.view.base.BaseFragment;

import java.util.ArrayList;
import java.util.Arrays;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import com.muslimlife.app.R;
import com.muslimlife.app.databinding.FragmentMessageBinding;

public class MessageFragment extends BaseFragment {

    private FragmentMessageBinding binding;
    PagerAdapter myAdapter;
    int messageCount, notificationCount=0;

    NotificationRes[] mainNotificationRes=null;
    GetAnswerRes[] mainGetAnswerRes=null;

    @Inject
    UserRepository userRepository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMessageBinding.inflate(inflater, container, false);

        myAdapter = new PagerAdapter(getChildFragmentManager());

        binding.switcher.answer.setBackgroundTintList(ColorStateList.valueOf(requireContext().getColor(R.color.white)));
        binding.switcher.answer.setCardElevation(4);

        binding.switcher.notification.setBackgroundTintList(ColorStateList.valueOf(requireContext().getColor(R.color.transparent)));
        binding.switcher.notification.setCardElevation(0);

        binding.switcher.answerNotif.setVisibility(View.GONE);
        binding.switcher.notificationNotif.setVisibility(View.GONE);

        ((MainActivity)requireActivity()).setMenu(Constants.MENU_OFF);

        binding.backButton.setOnClickListener(onClickListener);
        binding.switcher.notification.setOnClickListener(onClickListener);
        binding.switcher.answer.setOnClickListener(onClickListener);

        getAnswers();

        binding.messageViewPager.setAdapter(myAdapter);
        binding.messageViewPager.setCurrentItem(0);

        binding.messageViewPager.setPageTransformer(false, (page, position) -> {
            switch ((int) position){
                case 0:
                //    ChangeView(1);
                    break;
                case 1:
                //    ChangeView(0);
                    break;
            }
        });



        return binding.getRoot();
    }

    private void ChangeView(int type){
        if(isAdded()){
            SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("Message", MODE_PRIVATE);
            if (type == 0) {
                binding.switcher.answer.setBackgroundTintList(ColorStateList.valueOf(requireContext().getColor(R.color.white)));
                binding.switcher.answer.setCardElevation(4);

                binding.switcher.notification.setBackgroundTintList(ColorStateList.valueOf(requireContext().getColor(R.color.transparent)));
                binding.switcher.notification.setCardElevation(0);

                sharedPreferences.edit().putString("Message", "Message").apply();
            } else if (type == 1) {
                binding.switcher.answer.setBackgroundTintList(ColorStateList.valueOf(requireContext().getColor(R.color.transparent)));
                binding.switcher.answer.setCardElevation(0);

                binding.switcher.notification.setBackgroundTintList(ColorStateList.valueOf(requireContext().getColor(R.color.white)));
                binding.switcher.notification.setCardElevation(4);

                sharedPreferences.edit().putString("Message", "Notification").apply();
            }
        }
    }

    View.OnClickListener onClickListener = v -> {
        switch (v.getId()){
            case R.id.back_button:
                Navigation.findNavController(requireView()).popBackStack();
                break;
            case R.id.notification:
                binding.messageViewPager.setCurrentItem(1);
                ChangeView(1);
                break;
            case R.id.answer:
                binding.messageViewPager.setCurrentItem(0);
                ChangeView(0);
                break;
        }
    };

    void getAnswers(){
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        GetAnswerReq getAnswerReq = new GetAnswerReq(sharedPref.getString(Constants.SP_KEY_USER_ID, "0"));

        if(getAnswerReq.getUser_id().equals("0")){
            Toast.makeText(requireContext(), requireContext().getString(R.string.regPlease), Toast.LENGTH_SHORT).show();
            return;
        }

        userRepository.getAnswer(getAnswerReq).subscribe(new SingleObserver<>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onSuccess(@NonNull GetAnswerRes[] getAnswerRes) {
                if(isAdded()) {
                    mainGetAnswerRes = getAnswerRes;
                    messageCount = 0;
                    for (GetAnswerRes answerRes : getAnswerRes)
                        if (answerRes.getAnswer_status().equals("1"))
                            messageCount++;

                    myAdapter.addFragment(PageFragment.StartView(getAnswerRes, "message"), "message", binding, messageCount, notificationCount);

                    getNotifications();
                    myAdapter.notifyDataSetChanged();

                    ArrayList<GetAnswerRes> messages = new ArrayList<GetAnswerRes>(Arrays.asList(getAnswerRes));

                    messages.removeIf(answer -> answer.getAnswer_status().equals("0"));

                    userRepository.setMessageReaded(messages.size() > 0);
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    void getNotifications(){
        SharedPreferences sharedPref;
        try{
            sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE);
        }catch (Exception e){
            sharedPref = ((MainActivity)requireActivity()).sharedPref;
        }
        NotificationReq notificationReq = new NotificationReq(sharedPref.getString(Constants.SP_KEY_USER_ID, "0"));

        if(notificationReq.getUser_id().equals("0")){
            Toast.makeText(requireContext(), requireContext().getString(R.string.regPlease), Toast.LENGTH_SHORT).show();
            return;
        }

        userRepository.getNotifications(notificationReq).subscribe(new SingleObserver<NotificationRes[]>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onSuccess(@NonNull NotificationRes[] notificationRes) {
                mainNotificationRes=notificationRes;
                notificationCount=0;
                for (NotificationRes notification: notificationRes)
                    if(notification.getRead().equals("0"))
                        notificationCount++;

                myAdapter.addFragment(PageFragment.StartView(notificationRes,"notification"), "notification", binding, messageCount, notificationCount);

                myAdapter.notifyDataSetChanged();
                ChangeView(0);

                ArrayList<NotificationRes> notifications = new ArrayList<NotificationRes>(Arrays.asList(notificationRes));

                notifications.removeIf(notification -> notification.getRead().equals("1"));

                userRepository.setNotifReaded(notifications.size() > 0);
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

}
