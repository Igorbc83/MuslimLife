package com.muslimlife.app.view.donation;

import static com.muslimlife.app.utils.Constants.MENU_OFF;
import static com.muslimlife.app.utils.Constants.TINKOF_DESCRIPTION;
import static com.muslimlife.app.utils.Constants.TINKOF_KEY;
import static com.muslimlife.app.utils.Constants.TINKOF_TERMINAL;
import static com.muslimlife.app.utils.Constants.TINKOF_TITLE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;

import java.util.Random;

import ru.tinkoff.acquiring.sdk.AcquiringSdk;
import ru.tinkoff.acquiring.sdk.TinkoffAcquiring;
import ru.tinkoff.acquiring.sdk.localization.AsdkSource;
import ru.tinkoff.acquiring.sdk.localization.Language;
import ru.tinkoff.acquiring.sdk.models.DarkThemeMode;
import ru.tinkoff.acquiring.sdk.models.enums.CheckType;
import ru.tinkoff.acquiring.sdk.models.options.CustomerOptions;
import ru.tinkoff.acquiring.sdk.models.options.FeaturesOptions;
import ru.tinkoff.acquiring.sdk.models.options.OrderOptions;
import ru.tinkoff.acquiring.sdk.models.options.screen.PaymentOptions;
import ru.tinkoff.acquiring.sdk.utils.Money;
//import ru.tinkoff.acquiring.sdk.utils.SampleAcquiringTokenGenerator;
import com.muslimlife.app.R;
import com.muslimlife.app.databinding.FragmentDonationBinding;
import com.muslimlife.app.databinding.FragmentSettingsBinding;
import com.muslimlife.app.utils.Constants;
import com.muslimlife.app.view.MainActivity;
import com.muslimlife.app.view.base.BaseFragment;

public class DonationFragment extends BaseFragment {

    private FirebaseUser firebaseUser;
    TinkoffAcquiring tinkoffAcquiring;

    private FragmentDonationBinding binding;
    private int count=0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDonationBinding.inflate(inflater, container, false);
        binding.backButton.setOnClickListener(onClickListener);
        ((MainActivity)requireActivity()).setMenu(MENU_OFF);
        binding.donationSendButton.setOnClickListener(onClickListener);
        binding.summ100.donationItem.setOnClickListener(onClickListener);
        binding.summ200.donationItem.setOnClickListener(onClickListener);
        binding.summ500.donationItem.setOnClickListener(onClickListener);
        binding.summ1000.donationItem.setOnClickListener(onClickListener);

        binding.summ100.donationItemText.setText("100");
        binding.summ200.donationItemText.setText("200");
        binding.summ500.donationItemText.setText("500");
        binding.summ1000.donationItemText.setText("1000");

        tinkoffAcquiring = new TinkoffAcquiring(requireContext(), TINKOF_TERMINAL, TINKOF_KEY);
        //AcquiringSdk.AsdkLogger.setDeveloperMode(true);
        AcquiringSdk.AsdkLogger.setDebug(true);
        //AcquiringSdk.Companion.setTokenGenerator(new SampleAcquiringTokenGenerator("6tnvuilmnxhadthx"));

        getLifecycle().addObserver(binding.youtubePlayerView);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        binding.youtubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                youTubePlayer.loadVideo("0twbX_FnggE", 0);
                youTubePlayer.pause();
            }
        });

        return binding.getRoot();
    }

    private PaymentOptions createPaymentOptions() {
        OrderOptions orderOptions = new OrderOptions();
        orderOptions.setAmount(Money.ofRubles(count));
        orderOptions.setTitle(TINKOF_TITLE);
        orderOptions.setDescription(TINKOF_DESCRIPTION);
        orderOptions.setOrderId(String.valueOf(new Random().nextInt()));
        orderOptions.setRecurrentPayment(false);

        CustomerOptions customerOptions = new CustomerOptions();
        customerOptions.setCustomerKey(getUserId());
        customerOptions.setCheckType(CheckType.HOLD.toString());
        if(firebaseUser != null && !firebaseUser.isAnonymous()){
            customerOptions.setEmail(firebaseUser.getEmail());
        }

        FeaturesOptions featuresOptions = new FeaturesOptions();
        featuresOptions.setLocalizationSource(new AsdkSource(Language.RU));
        featuresOptions.setHandleCardListErrorInSdk(true);
        featuresOptions.setUseSecureKeyboard(true);
        //featuresOptions.setCameraCardScanner(new CameraCardIOScanner());
        featuresOptions.setFpsEnabled(true);
        featuresOptions.setDarkThemeMode(DarkThemeMode.AUTO);
        //featuresOptions.setTheme(R.style.Theme_MyApplication);
        featuresOptions.setUserCanSelectCard(true);

        PaymentOptions paymentOptions = new PaymentOptions();
        paymentOptions.setOrder(orderOptions);
        paymentOptions.setCustomer(customerOptions);
        paymentOptions.setFeatures(featuresOptions);

        return paymentOptions;
    }

    private void startPurchase(){
        tinkoffAcquiring.openPaymentScreen(this, createPaymentOptions(),8888);
    }

    private String getUserId() {
        SharedPreferences sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getString(Constants.SP_KEY_USER_ID, "0");
    }

    @Override
    public void showError(String error) {
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
    }


    View.OnClickListener onClickListener = v -> {
        switch (v.getId()){
            case R.id.back_button:
                Navigation.findNavController(requireView()).popBackStack();
                break;
            case R.id.donation_send_button:
                if(count!=0)
                    startPurchase();
                else
                    Toast.makeText(requireContext(),requireContext().getString(R.string.chooseSumm),Toast.LENGTH_SHORT).show();
                break;
            case R.id.summ_100:
                donation_choose(100);
                count=100;
                break;
            case R.id.summ_200:
                donation_choose(200);
                count=200;
                break;
            case R.id.summ_500:
                donation_choose(500);
                count=500;
                break;
            case R.id.summ_1000:
                donation_choose(1000);
                count=1000;
                break;
        }
    };

    void donation_choose(int choosed){
        binding.donationSum.setText(String.valueOf(choosed));
        binding.summ100.donationItem.setBackground(requireContext().getDrawable(R.drawable.donation_bg));
        binding.summ200.donationItem.setBackground(requireContext().getDrawable(R.drawable.donation_bg));
        binding.summ500.donationItem.setBackground(requireContext().getDrawable(R.drawable.donation_bg));
        binding.summ1000.donationItem.setBackground(requireContext().getDrawable(R.drawable.donation_bg));
        switch (choosed){
            case 100:
                binding.summ100.donationItem.setBackground(requireContext().getDrawable(R.drawable.sub_on_bg));
                break;
            case 200:
                binding.summ200.donationItem.setBackground(requireContext().getDrawable(R.drawable.sub_on_bg));
                break;
            case 500:
                binding.summ500.donationItem.setBackground(requireContext().getDrawable(R.drawable.sub_on_bg));
                break;
            case 1000:
                binding.summ1000.donationItem.setBackground(requireContext().getDrawable(R.drawable.sub_on_bg));
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (resultCode) {
            case Activity.RESULT_OK: {
                if (requestCode == 8888 && data != null) {
                    //long Id = data.getLongExtra(TinkoffAcquiring.EXTRA_PAYMENT_ID, -1);
                    //String Id = data.getStringExtra(TinkoffAcquiring.EXTRA_REBILL_ID);
                    Toast.makeText(requireContext(), R.string.thx, Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case TinkoffAcquiring.RESULT_ERROR: {
                if (data != null) {
                    Throwable throwable = (Throwable) data.getSerializableExtra(TinkoffAcquiring.EXTRA_ERROR);
                    Toast.makeText(requireContext(), throwable.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
