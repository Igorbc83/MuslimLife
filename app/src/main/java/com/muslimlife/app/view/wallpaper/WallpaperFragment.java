package com.muslimlife.app.view.wallpaper;

import static com.muslimlife.app.utils.Constants.MENU_OFF;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.inject.Inject;

import all.muslimlife.data.repository.WebPayRepository;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

import com.muslimlife.app.R;
import com.muslimlife.app.data.model.UserProfile;
import com.muslimlife.app.data.model.WallpaperRes;
import com.muslimlife.app.data.repository.UserRepository;
import com.muslimlife.app.databinding.FragmentWallpaperBinding;
import com.muslimlife.app.di.viewModel.DaggerViewModelFactory;
import com.muslimlife.app.utils.Constants;
import com.muslimlife.app.utils.ServicesUtil;
import com.muslimlife.app.view.MainActivity;
import com.muslimlife.app.view.base.BaseFragment;
import com.muslimlife.app.viewmodel.main.MainViewModel;

public class WallpaperFragment extends BaseFragment implements WallpaperView {

    private FragmentWallpaperBinding binding;

    @Inject
    UserRepository userRepository;

    @Inject
    WebPayRepository webPayRepository;

    @Inject
    DaggerViewModelFactory viewModelFactory;
    private MainViewModel viewModel;

    WallpaperAdapter wallpaperAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentWallpaperBinding.inflate(inflater, container, false);
        ((MainActivity)requireActivity()).setMenu(MENU_OFF);

        initViewModel();
        initObserve();

        ServicesUtil.updateScore(getActivity().getPreferences(Context.MODE_PRIVATE), Constants.SERVICE_WALLPAPER);

        binding.backButton.setOnClickListener(view-> Navigation.findNavController(requireView()).popBackStack());

        binding.bottomBar.save.setOnClickListener(v->getChecked(true));
        binding.bottomBar.share.setOnClickListener(v->getChecked(false));

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser == null || firebaseUser.isAnonymous()){
            getWallpaper("");
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
        getWallpaper(userProfile.getDescription());
    }

    private void initCoin(int count){
        binding.coinCount.setText(String.valueOf(count));

        binding.coinCount.setOnClickListener(view ->
                Navigation.findNavController(requireView()).navigate(R.id.MLCoinBillingNewFragment));
        binding.coinLogo.setOnClickListener(view ->
                Navigation.findNavController(requireView()).navigate(R.id.MLCoinBillingNewFragment));
    }

    void getWallpaper(String currentByes){
        WallpaperView view = this;
        userRepository.getWallpaper().subscribe(new SingleObserver<>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onSuccess(@NonNull WallpaperRes[] wallpaperRes) {
                wallpaperAdapter = new WallpaperAdapter(wallpaperRes, view, currentByes);
                GridLayoutManager llm = new GridLayoutManager(requireContext(), 3);
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                binding.wallpaperRv.setLayoutManager(llm);
                binding.wallpaperRv.setAdapter(wallpaperAdapter);
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

    @Override
    public void chooseWallpaper(WallpaperRes wallpaperRes) {
        userRepository.setWallpaperRes(wallpaperRes);
        Navigation.findNavController(requireView()).navigate(R.id.ImageFragment);
    }

    @Override
    public void checkMod(boolean mod) {
        binding.bottomBar.parent.setVisibility(mod?View.VISIBLE:View.GONE);
    }

    public void getChecked(boolean download){
        ArrayList<WallpaperRes> checked= wallpaperAdapter.getWalwaper();

        if(checked.size()<1){
            Toast.makeText(requireContext(), requireContext().getString(R.string.chooseOnePhoto), Toast.LENGTH_SHORT).show();
            return;
        }

        if(download)
            for (WallpaperRes wallpaperRes: checked)
                saveFile(wallpaperRes);
            else
                share(checked);

    }

    void saveFile(WallpaperRes wallpaperRes){
        try{
            String filename = wallpaperRes.getId() + "muslimlife";
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

    void share(ArrayList<WallpaperRes> wallpaper) {
        ArrayList<Uri> bitmapUri = new ArrayList<>();
        Thread thread = new Thread(() -> {
            for (int i=0; i<wallpaper.size();i++) {
            int finalI = i;
                try {
                    URL url = new URL(wallpaper.get(finalI).getImage());
                    HttpURLConnection connection = (HttpURLConnection) url
                            .openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(input);
                    String bitmapPath = MediaStore.Images.Media.insertImage(requireActivity().getContentResolver(),
                            bitmap,  requireContext().getString(R.string.shareOne), requireContext().getString(R.string.sharePhoto));
                    bitmapUri.add(Uri.parse(bitmapPath));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
            intent.setType("image/png");
            intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, bitmapUri);
            startActivity(Intent.createChooser(intent, requireContext().getString(R.string.shareOne)));
        });
        thread.start();

    }
}
