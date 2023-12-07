package com.muslimlife.app.view.notification;

import static com.muslimlife.app.utils.Constants.MENU_OFF;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import com.muslimlife.app.R;
import com.muslimlife.app.data.model.NotificationRes;
import com.muslimlife.app.data.model.ReadNotificationReq;
import com.muslimlife.app.data.model.ReadNotificationRes;
import com.muslimlife.app.data.repository.UserRepository;
import com.muslimlife.app.databinding.FragmentMessageBinding;
import com.muslimlife.app.databinding.FragmentNotificationBinding;
import com.muslimlife.app.view.MainActivity;
import com.muslimlife.app.view.base.BaseFragment;

public class NotificationFragment extends BaseFragment {

    FragmentNotificationBinding binding;
    @Inject
    UserRepository userRepository;

    NotificationRes notificationRes;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentNotificationBinding.inflate(inflater, container, false);
        binding.backButton.setOnClickListener(onClickListener);
        ((MainActivity)requireActivity()).setMenu(MENU_OFF);

        notificationRes = userRepository.getNotificationRes();

        binding.notificationText.setText(notificationRes.getText());

        try {
            SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            SimpleDateFormat myFormat = new SimpleDateFormat("dd MMM kk:mm");
            fromUser.setTimeZone(TimeZone.getTimeZone("GMT"));
            String dateS = myFormat.format(fromUser.parse(notificationRes.getUpdated()));
            binding.notificationDate.setText(dateS);

            readNotification();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return binding.getRoot();
    }

    void readNotification(){
        userRepository.readNotification(new ReadNotificationReq(notificationRes.getId(),"1")).subscribe(new SingleObserver<ReadNotificationRes[]>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onSuccess(@NonNull ReadNotificationRes[] readNotificationRes) {
                notificationRes.setRead("1");
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    View.OnClickListener onClickListener = v -> {
        switch (v.getId()){
            case R.id.back_button:
                Navigation.findNavController(requireView()).popBackStack();
                break;

        }
    };


    @Override
    public void showError(String error) {
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
    }
}
