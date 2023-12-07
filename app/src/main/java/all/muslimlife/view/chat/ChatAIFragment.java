package all.muslimlife.view.chat;

import static com.muslimlife.app.utils.Constants.MENU_OFF;
import static com.muslimlife.app.utils.Constants.MENU_OFF;
import static com.muslimlife.app.utils.Constants.OPEN_AUTH;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.inject.Inject;

import all.muslimlife.data.model.QrCodeRes;
import all.muslimlife.data.repository.ChatAIRepository;
import all.muslimlife.data.repository.WebPayRepository;
import all.muslimlife.view.chat.adapters.ChatAIAdapter;
import all.muslimlife.view.chat.adapters.ChatAIRecyclerModel;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import com.muslimlife.app.data.repository.AzkarsRepository;
import com.muslimlife.app.data.repository.UserRepository;
import com.muslimlife.app.databinding.FragmentChatAiBinding;
import com.muslimlife.app.databinding.FragmentQrCodeBinding;
import com.muslimlife.app.utils.Constants;
import com.muslimlife.app.view.MainActivity;
import com.muslimlife.app.view.base.BaseFragment;
import com.muslimlife.app.R;
import com.muslimlife.app.data.model.UserProfile;
import com.muslimlife.app.data.repository.AzkarsRepository;
import com.muslimlife.app.databinding.FragmentChatAiBinding;
import com.muslimlife.app.databinding.FragmentQrCodeBinding;
import com.muslimlife.app.di.viewModel.DaggerViewModelFactory;
import com.muslimlife.app.view.MainActivity;
import com.muslimlife.app.view.base.BaseFragment;
import com.muslimlife.app.viewmodel.main.MainViewModel;

public class ChatAIFragment extends BaseFragment {

    @Inject
    ChatAIRepository chatAIRepository;

    @Inject
    UserRepository userRepository;

    @Inject
    WebPayRepository webPayRepository;

    private FragmentChatAiBinding binding;

    private ChatAIAdapter chatAIAdapter;

    SharedPreferences sharedPref;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentChatAiBinding.inflate(inflater, container, false);
        ((MainActivity) requireActivity()).setMenu(MENU_OFF);

        sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE);

        initView();


        if(!userRepository.isSubscribe()){
            binding.coinCount.setVisibility(View.INVISIBLE);
            binding.coinLogo.setVisibility(View.INVISIBLE);
            binding.msgSend.setOnClickListener(view -> send());
        }else {
            initObserve();

            if (sharedPref.getBoolean(Constants.PREFS_CHAT_AI_INFO, true)) {
                AlertDialog alertDialog = new AlertDialog.Builder(requireContext()).create();
                alertDialog.setMessage(getString(R.string.chat_ai_mlcoin_title));
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.buy),
                        (dialog, which) -> {
                            Navigation.findNavController(requireView()).navigate(R.id.MLCoinBillingNewFragment);
                        }
                );
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.cancel),
                        (dialog, which) ->
                                dialog.dismiss()
                );
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.chat_ai_btn),
                        (dialog, which) -> {
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putBoolean(Constants.PREFS_CHAT_AI_INFO, false);
                            editor.apply();
                            dialog.dismiss();
                        }
                );
                alertDialog.show();
            }
        }

        return binding.getRoot();
    }

    private void initView(){
        binding.backButton.setOnClickListener(v-> Navigation.findNavController(requireView()).popBackStack());

        chatAIAdapter = new ChatAIAdapter(new ArrayList<>(chatAIRepository.getItems()));
        binding.msgRecycler.setAdapter(chatAIAdapter);

        if(chatAIRepository.getItems().isEmpty())
            addMessage(new ChatAIRecyclerModel(ChatAIRecyclerModel.CHAT_MSG_AI, "Ас-саляму алейкум! \uD83D\uDE4F Чем могу вам помочь?"));
    }

    private void initObserve() {
        webPayRepository.getMlCoinsLiveData().observe(getViewLifecycleOwner(), this::init);
    }

    private void init(int count){
        binding.coinCount.setText(String.valueOf(count));

        binding.coinCount.setOnClickListener(view ->
                Navigation.findNavController(requireView()).navigate(R.id.MLCoinBillingNewFragment));
        binding.coinLogo.setOnClickListener(view ->
                Navigation.findNavController(requireView()).navigate(R.id.MLCoinBillingNewFragment));

        binding.msgSend.setOnClickListener(view -> {
            if(!binding.msgEdit.getText().toString().isEmpty()) {
                boolean canBuy = webPayRepository.checkCanSpendCoin(1);

                if (!canBuy) {
                    AlertDialog alertDialog = new AlertDialog.Builder(requireContext()).create();
                    alertDialog.setMessage(getString(canBuy ? R.string.chat_ai_mlcoin_title : R.string.buy_error));
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.buy),
                            (dialog, which) -> {
                                Navigation.findNavController(requireView()).navigate(R.id.MLCoinBillingNewFragment);
                            }
                    );
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.cancel),
                            (dialog, which) ->
                                    dialog.dismiss()
                    );
                    alertDialog.show();
                } else {
                    //closeKeyboard();
                    send();
                }
            }
        });
    }

    private void send(){
        binding.msgSend.setVisibility(View.INVISIBLE);
        sendMessage(binding.msgEdit.getText().toString());
        binding.msgEdit.setText("");
    }

    private void sendMessage(String myMsg){
        addMessage(new ChatAIRecyclerModel(ChatAIRecyclerModel.CHAT_MSG_ME, myMsg));
        addMessage(new ChatAIRecyclerModel(ChatAIRecyclerModel.CHAT_MSG_LOADING, ""));

        chatAIRepository.sendMessage(myMsg).subscribe(new SingleObserver<>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onSuccess(@NonNull String msg) {
                if(!isAdded())
                    return;

                if(!msg.isEmpty()) {
                    if(!userRepository.isSubscribe())
                        buyMessage();

                    binding.msgSend.setVisibility(View.VISIBLE);
                    addMessage(new ChatAIRecyclerModel(ChatAIRecyclerModel.CHAT_MSG_AI, msg));
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
            }
        });
    }

    private void buyMessage(){
        webPayRepository.updateMLCoin(-1);

    }

    private void addMessage(ChatAIRecyclerModel msg){
        if(msg.getType() != ChatAIRecyclerModel.CHAT_MSG_LOADING)
            chatAIRepository.getItems().add(msg);

        chatAIAdapter.addMessage(msg);
        scrollToBottom();
    }

    private void scrollToBottom() {
        if (chatAIAdapter.getItemCount() > 0) {
            binding.msgRecycler.smoothScrollToPosition(chatAIAdapter.getItemCount() - 1);
        }
    }



    @Override
    public void showError(String error) {

    }
}
