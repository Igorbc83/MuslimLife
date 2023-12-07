package com.muslimlife.app.view.sub_ru_store;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.muslimlife.app.R;
import com.muslimlife.app.data.model.UserProfile;
import com.muslimlife.app.data.model.parameters.LoadUserDataParameters;
import com.muslimlife.app.data.repository.UserRepository;
import com.muslimlife.app.databinding.FragmentSubRustoreBinding;
import com.muslimlife.app.di.viewModel.DaggerViewModelFactory;
import com.muslimlife.app.utils.Constants;
import com.muslimlife.app.view.MainActivity;
import com.muslimlife.app.view.base.BaseFragment;
import com.muslimlife.app.viewmodel.main.MainViewModel;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import ru.rustore.sdk.billingclient.RuStoreBillingClient;
import ru.rustore.sdk.billingclient.model.product.Product;

public class SubRuStoreFragment extends BaseFragment implements RuStoreListener {

    private FragmentSubRustoreBinding binding;

    @Inject
    UserRepository userRepository;

    @Inject
    public RuStoreBillingClient ruStoreBillingClient;

    private RuStoreModule ruStoreModule;

    @Inject
    DaggerViewModelFactory viewModelFactory;
    private MainViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSubRustoreBinding.inflate(inflater, container, false);
        ((MainActivity)requireActivity()).setMenu(Constants.MENU_OFF);

        initViewModel();
        initUserProfileObserve();

        binding.premisions.setText("В дополнение к бесплатным функциям приложения Вы получаете:\n\n" +
                "• Коран с аудиоозвучкой профессиональными чтецами (без подписки Вы можете только читать текст, а с подпиской - слушать!)\n\n" +
                "• Азкары с аудиоозвучкой (без подписки Вы можете только читать текст)\n\n" +
                "• Возможность бесплатно задавать вопросы искуственному интеллекту (без подписки каждый вопрос стоит 1 монетку mlcoin)\n\n" +
                "• Вы получаете максимальную скидку в сети халяльных заведений (кафе, рестораны, магазины) «Проверено, МуслимЛайф» - 6% (без подписки Вам доступна скидка от 3,5%, чтобы получить 6%, общий счёт Ваших заказов долже составить не менее 300 000 руб.).\n" +
                "Скидка 6% полностью окупает месячную подписку за один-два похода в кафе! Вы поели на 600 руб., заплатили с учетом скидки 6% 564 руб. - 36 руб. экономии с одного обеда! 10 раз по 600 рублей (это средний чек во многих халяльных кафе) - и Вы сэкономите 360 руб., заплатив за подписку всего 49 руб.");

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE);
        String id = sharedPref.getString(Constants.SP_KEY_USER_ID, "");
        if(!id.isEmpty()) {
            ruStoreModule = new RuStoreModule(ruStoreBillingClient, this);
            ruStoreModule.checkPurchases();

            userRepository.getUserProfile(new LoadUserDataParameters(id)).subscribe(new SingleObserver<>() {
                @Override
                public void onSubscribe(@NonNull Disposable d) {
                }

                @SuppressLint("UseCompatLoadingForDrawables")
                @Override
                public void onSuccess(@NonNull UserProfile userProfile) {
                    requireActivity().runOnUiThread(() -> {
                        init(userProfile);
                    });
                }

                @Override
                public void onError(@NonNull Throwable e) {
                }
            });
        }else{
            Navigation.findNavController(requireView()).navigate(R.id.newAuthFragment);
        }
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(requireActivity(), viewModelFactory).get(MainViewModel.class);
    }

    private void initUserProfileObserve() {
        viewModel.getUserProfileLiveData().observe(getViewLifecycleOwner(), this::init);
    }

    private void init(UserProfile userProfile){
        binding.nameTv.setText(userProfile.getFullName());
        if(userProfile.getAvatar() == null || userProfile.getAvatar().isEmpty())
            binding.avatarIv.setImageDrawable(requireContext().getDrawable(R.drawable.ic_empty_photo));
        else
            Glide.with(requireContext()).load(userProfile.getAvatar()).override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).into(binding.avatarIv);

        if(userProfile.isSubscribed()) {
            binding.supportSendButton.setBackgroundColor(requireContext().getColor(R.color.gray));
            binding.supportSendButton.setText(R.string.sub_cancle_rustore);
            binding.subStatus.setVisibility(View.GONE);
            binding.info.setVisibility(View.VISIBLE);

            //todo test hide sub logic
            binding.supportSendButton.setOnClickListener(v -> {
                try {
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName("ru.vk.store", "ru.vk.store.app.MainActivity"));
                    startActivity(intent);
                }catch (Exception ex){
                    Toast.makeText(requireContext(), "Ошибка, зайдите пожалуйста в приложение RuStore самостоятельно", Toast.LENGTH_SHORT).show();
                }
            });

            //test
            /*binding.supportSendButton.setOnClickListener(view -> {
                ((MainActivity) requireActivity()).setSubscribe(false);
            });*/
        }else{
            binding.supportSendButton.setBackgroundColor(requireContext().getColor(R.color.dark_blue));
            binding.supportSendButton.setText(R.string.subPrice);
            binding.info.setVisibility(View.GONE);

            //todo test hide sub logic
            binding.supportSendButton.setOnClickListener(v->ruStoreModule.purchaseProduct());
            //test
            /*binding.supportSendButton.setOnClickListener(view -> {
                ((MainActivity) requireActivity()).setSubscribe(true);
            });*/
        }
    }

    @Override
    public void showError(String error) {
        showProgress(false);
        binding.error.setText(error);
    }

    @Override
    public void checkPurchases(boolean state) {
        if(state){
            binding.error.setText("");
            binding.supportSendButton.setVisibility(View.VISIBLE);
        }else{
            binding.supportSendButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void showProgress(boolean show) {
        binding.progress.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void cancelPurchases() {
        ((MainActivity) requireActivity()).setSubscribe(false);
    }

    @Override
    public void confirmedPurchases() {
        ((MainActivity) requireActivity()).setSubscribe(true);
    }

    @Override
    public void getRuStoreProducts(List<Product> products) {
        StringBuilder temp = new StringBuilder();
        for(Product product : products){
            temp.append(product.getTitle()).append("\n");
        }
    }
}
