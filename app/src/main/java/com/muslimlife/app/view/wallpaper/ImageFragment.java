package com.muslimlife.app.view.wallpaper;

import static com.muslimlife.app.utils.Constants.MENU_OFF;
import static com.muslimlife.app.utils.Constants.OPEN_AUTH;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Target;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.inject.Inject;

import all.muslimlife.data.repository.WebPayRepository;
import com.muslimlife.app.R;
import com.muslimlife.app.data.model.UserProfile;
import com.muslimlife.app.data.model.WallpaperRes;
import com.muslimlife.app.data.repository.UserRepository;
import com.muslimlife.app.databinding.FragmentImageBinding;
import com.muslimlife.app.databinding.FragmentWallpaperBinding;
import com.muslimlife.app.di.viewModel.DaggerViewModelFactory;
import com.muslimlife.app.view.MainActivity;
import com.muslimlife.app.view.base.BaseFragment;
import com.muslimlife.app.viewmodel.main.MainViewModel;

public class ImageFragment extends BaseFragment {

    private FragmentImageBinding binding;

    @Inject
    UserRepository userRepository;

    @Inject
    WebPayRepository webPayRepository;

    @Inject
    DaggerViewModelFactory viewModelFactory;
    private MainViewModel viewModel;

    WallpaperRes wallpaperRes;

    private UserProfile userProfile;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentImageBinding.inflate(inflater, container, false);
        ((MainActivity)requireActivity()).setMenu(MENU_OFF);

        initViewModel();
        initObserve();

        binding.backButton.setOnClickListener(view-> Navigation.findNavController(requireView()).popBackStack());

        wallpaperRes= userRepository.getWallpaperRes();

        Glide.with(requireContext()).load(wallpaperRes.getImage()).into(binding.image);

        binding.bottomBar.save.setOnClickListener(v->saveFile());
        binding.bottomBar.share.setOnClickListener(v->share());

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser == null || firebaseUser.isAnonymous()){
            binding.progress.setVisibility(View.GONE);
            binding.buyBtn.setVisibility(View.VISIBLE);
            binding.buyBtn.setOnClickListener(view -> {
                FirebaseAuth.getInstance().signOut();
                Bundle bundle = new Bundle();
                bundle.putBoolean(OPEN_AUTH, false);
                Navigation.findNavController(requireView()).navigate(R.id.newAuthFragment, bundle);
            });
        }

        return binding.getRoot();
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(requireActivity(), viewModelFactory).get(MainViewModel.class);
    }

    private void initObserve() {
        viewModel.getUserProfileLiveData().observe(getViewLifecycleOwner(), this::init);
        webPayRepository.getMlCoinsLiveData().observe(getViewLifecycleOwner(), this::initCoin);
    }

    private void init(UserProfile userProfile){
        binding.progress.setVisibility(View.GONE);

        this.userProfile = userProfile;

        binding.progress.setOnClickListener(view -> {});

        if(userProfile.getDescription().contains("/" + wallpaperRes.getId() + "/")){
            binding.bottomBar.getRoot().setVisibility(View.VISIBLE);
            binding.buyBtn.setVisibility(View.GONE);
        }else{
            binding.bottomBar.getRoot().setVisibility(View.INVISIBLE);
            binding.buyBtn.setVisibility(View.VISIBLE);
        }

        binding.buyBtn.setOnClickListener(view -> {
            boolean canBuy = webPayRepository.checkCanSpendCoin(3);

            AlertDialog alertDialog = new AlertDialog.Builder(requireContext()).create();
            alertDialog.setMessage(getString(canBuy ? R.string.buy_desc : R.string.buy_error));
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.buy),
                    (dialog, which) -> {
                        if (canBuy) {
                            buyImage();
                        } else {
                            Navigation.findNavController(requireView()).navigate(R.id.MLCoinBillingNewFragment);
                        }
                    }
            );
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.cancel),
                    (dialog, which) ->
                            dialog.dismiss()
            );
            alertDialog.show();
        });
    }

    private void initCoin(int count){
        binding.coinCount.setText(String.valueOf(count));

        binding.coinCount.setOnClickListener(view ->
                Navigation.findNavController(requireView()).navigate(R.id.MLCoinBillingNewFragment));
        binding.coinLogo.setOnClickListener(view ->
                Navigation.findNavController(requireView()).navigate(R.id.MLCoinBillingNewFragment));
    }

    private void buyImage(){
        binding.progress.setVisibility(View.VISIBLE);
        userProfile.setDescription(userProfile.getDescription() + "/" + wallpaperRes.getId() + "/");
        webPayRepository.updateMLCoin(-3);
    }

    void saveFile(){
        try{
            String filename = wallpaperRes.getId();
            DownloadManager dm = (DownloadManager) requireActivity().getSystemService(Context.DOWNLOAD_SERVICE);
            Uri downloadUri = Uri.parse(wallpaperRes.getImage());
            DownloadManager.Request request = new DownloadManager.Request(downloadUri);
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                    .setAllowedOverRoaming(false)
                    .setTitle(filename)
                    .setMimeType("image/jpeg") // Your file type. You can use this code to download other file types also.
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, File.separator + filename + ".jpg");
            dm.enqueue(request);
            Toast.makeText(requireContext(), getString(R.string.download_started), Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    void share(){
        binding.progress.setVisibility(View.VISIBLE);
        new Thread(() -> {
            try {


                URL url = new URL(wallpaperRes.getImage());
                HttpURLConnection connection = (HttpURLConnection) url
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(input);
                if(getActivity()!=null){
                    String bitmapPath = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(),
                            bitmap, requireContext().getString(R.string.shareOne), requireContext().getString(R.string.sharePhoto));
                    Uri bitmapUri = Uri.parse(bitmapPath);

                    requireActivity().runOnUiThread(() -> binding.progress.setVisibility(View.GONE));

                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("image/png");
                    intent.putExtra(Intent.EXTRA_STREAM, bitmapUri);
                    startActivity(Intent.createChooser(intent, requireContext().getString(R.string.shareOne)));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public void showError(String error) {
        Log.d("Error", error);
    }
}
