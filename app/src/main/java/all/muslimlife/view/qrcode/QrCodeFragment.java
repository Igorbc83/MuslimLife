package all.muslimlife.view.qrcode;

import static com.muslimlife.app.utils.Constants.MENU_OFF;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;

import javax.inject.Inject;

import all.muslimlife.data.model.QrCodeRes;
import all.muslimlife.data.repository.WebPayRepository;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import all.muslimlife.data.model.migrants.MigrantsRes;
import com.muslimlife.app.R;
import com.muslimlife.app.data.repository.AzkarsRepository;
import com.muslimlife.app.databinding.FragmentQrCodeBinding;
import com.muslimlife.app.utils.Constants;
import com.muslimlife.app.view.MainActivity;
import com.muslimlife.app.view.base.BaseFragment;

public class QrCodeFragment extends BaseFragment {

    @Inject
    WebPayRepository webPayRepository;

    private FragmentQrCodeBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentQrCodeBinding.inflate(inflater, container, false);
        ((MainActivity) requireActivity()).setMenu(MENU_OFF);

        initView();

        return binding.getRoot();
    }

    private void initView(){
        binding.backButton.setOnClickListener(v-> Navigation.findNavController(requireView()).popBackStack());

        webPayRepository.getQrCodeInfo(getUserId()).subscribe(new SingleObserver<>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onSuccess(@NonNull QrCodeRes qrCodeRes) {
                initQrCodeView(qrCodeRes);
            }

            @Override
            public void onError(@NonNull Throwable e) {
            }
        });
    }

    private void initQrCodeView(QrCodeRes qrCodeRes){
        if(qrCodeRes.getQrCode() != null)
            Glide.with(requireContext()).load(qrCodeRes.getQrCode()).into(binding.qrCode);

        binding.qrCodeDiscount.setText(getString(R.string.qr_code_discount, String.valueOf(qrCodeRes.getDiscount())) + " %");
        binding.qrCodeCount.setText(getString(R.string.qr_code_count, formatMoney(qrCodeRes.getTotal())));

        binding.progress.setVisibility(View.GONE);
    }

    private String getUserId() {
        SharedPreferences sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getString(Constants.SP_KEY_USER_ID, "0");
    }

    public static String formatMoney(float amount) {
        long amountInCents = Math.round(amount * 100); // Переводим в копейки и округляем
        if (amountInCents % 100 == 0) {
            DecimalFormat df = new DecimalFormat("#,##0");
            return df.format(amountInCents / 100); // Если копеек нет, вернем значение без дробной части
        } else {
            DecimalFormat df = new DecimalFormat("#,##0.00");
            return df.format(amount); // Если есть копейки, вернем значение с двумя знаками после запятой
        }
    }

    @Override
    public void showError(String error) {

    }
}
