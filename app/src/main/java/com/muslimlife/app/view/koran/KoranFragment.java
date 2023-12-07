package com.muslimlife.app.view.koran;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.muslimlife.app.BuildConfig;
import com.muslimlife.app.adapters.KoranAdapter;
import com.muslimlife.app.data.model.ReadersRes;
import com.muslimlife.app.data.model.surah.AyahRes;
import com.muslimlife.app.data.model.surah.SurahRes;
import com.muslimlife.app.data.repository.UserRepository;
import com.muslimlife.app.di.viewModel.DaggerViewModelFactory;
import com.muslimlife.app.utils.Constants;
import com.muslimlife.app.utils.MetricsUtil;
import com.muslimlife.app.utils.ServicesUtil;
import com.muslimlife.app.view.MainActivity;
import com.muslimlife.app.view.base.BaseFragment;
import com.muslimlife.app.view.bookmark.SnapHelperOneByOne;
import com.muslimlife.app.view.koran.listeners.KoranPageListener;
import com.muslimlife.app.viewmodel.main.MainViewModel;

import com.muslimlife.app.R;
import com.muslimlife.app.databinding.FragmentKoranBinding;

public class KoranFragment extends BaseFragment implements KoranPageListener {

    private static final String TAG = "KoranFragment";

    public static final int BOTTOM_MENU_PLAYER = 1;
    public static final int BOTTOM_MENU_PAGER = 2;
    public static final int BOTTOM_MENU_HIDE = 3;

    @Inject
    UserRepository userRepository;

    private FragmentKoranBinding binding;

    private KoranAdapter adapter;

    MediaPlayer mediaPlayer;
    Thread mediaThread = null;

    int currentSurah = 1, currentAyah = 1;

    @Inject
    DaggerViewModelFactory viewModelFactory;
    private MainViewModel viewModel;

    SharedPreferences sharedPref;

    Set<String> bookmarks;

    private boolean subState = false;

    @SuppressLint("CheckResult")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentKoranBinding.inflate(inflater, container, false);

        binding.progress.setOnClickListener(v -> { });

        initViewModel();
        initUserProfileObserve();

        sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);

        ServicesUtil.updateScore(sharedPref, Constants.SERVICE_KORAN);

        ((MainActivity) requireActivity()).setMenu(Constants.MENU_OFF);
        binding.koranHome.setOnClickListener(onClickListener);
        binding.koranSettings.setOnClickListener(onClickListener);
        binding.koranBookmark.setOnClickListener(onClickListener);

        bookmarks = getBookmarks();

        /*viewModel.userRepository.getSurahListMutableLiveData().observe(getViewLifecycleOwner(), surahs -> {
            if(surahs != null && !surahs.isEmpty()){

            }
        });*/

        viewModel.userRepository.getKoranPagesMutableLiveData().observe(getViewLifecycleOwner(), koranPages -> {
            if(koranPages != null && !koranPages.isEmpty()){
                setRecycler();
            }
        });

        return binding.getRoot();
    }

    private void setRecycler(){
        try{
            LinearLayoutManager manager = new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false);
            adapter = new KoranAdapter(viewModel.userRepository.getKoranPagesList(), MetricsUtil.getDisplayMetrics(requireContext()), this);

            binding.koranRecycler.setLayoutManager(manager);
            binding.koranRecycler.setAdapter(adapter);
            binding.koranRecycler.setOverScrollMode(View.OVER_SCROLL_NEVER);

            LinearSnapHelper linearSnapHelper = new SnapHelperOneByOne();
            linearSnapHelper.attachToRecyclerView(binding.koranRecycler);

            binding.progress.setVisibility(View.GONE);

            init();
        }catch (Exception e){
            Log.e("setRecycler", e.getMessage());
        }
    }

    private void init(){

        binding.koranRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NotNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE){
                    int page = getCurrentPage();

                    if(page > 0  && page < viewModel.userRepository.getKoranPagesList().size() - 1) {
                        if (bookmarks.contains(String.valueOf(page)))
                            binding.koranBookmark.setImageResource(R.drawable.ic_bookmark_check);
                        else
                            binding.koranBookmark.setImageResource(R.drawable.ic_bookmark);

                        SurahRes surahRes = findSurah(viewModel.userRepository.getKoranPagesList().get(page).getSurahNumber(), false);

                        if(surahRes != null)
                            binding.koranTitle.setText(getTranslate(surahRes.getTranslate()));
                        else
                            binding.koranTitle.setText(String.valueOf(viewModel.userRepository.getKoranPagesList().get(page).getSurahNumber()));

                        binding.pageType.setText(viewModel.userRepository.getKoranPagesList().get(page).getNumber());
                        binding.seekPage.setProgress(page);

                        savePage();
                    }
                }
            }
        });

        int p = getSavedPage();

        SurahRes surahRes = findSurah(viewModel.userRepository.getKoranPagesList().get(p).getSurahNumber(), false);

        if(surahRes != null)
            binding.koranTitle.setText(getTranslate(surahRes.getTranslate()));
        else
            binding.koranTitle.setText(String.valueOf(viewModel.userRepository.getKoranPagesList().get(p).getSurahNumber()));

        binding.pageType.setText(String.valueOf(p));

        binding.pageType.setOnClickListener(v -> {
            if(mediaPlayer != null)
                mediaPlayer.stop();

            adapter.setStateFull(!adapter.isStateFull());
        });
        
        binding.seekPage.setListener(this);

        binding.seekPage.setMax(603);   

        binding.play.setOnClickListener(v -> {
            if(subState)
                linePlay();
            else
                showSubDialog();
        });

        binding.playerClose.setOnClickListener(v -> {
            if(mediaPlayer != null){
                mediaPlayer.stop();
                selectMenu(BOTTOM_MENU_HIDE);
            }
        });

        binding.playerPlay.setOnClickListener(v -> {
            if(mediaPlayer != null){
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    binding.playerPlay.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_play, null));
                }else{
                    mediaPlayer.start();
                    binding.playerPlay.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_pause, null));
                }
            }
        });

        binding.seekPage.setProgress(p);
        binding.koranRecycler.scrollToPosition(p);
    }

    private void savePage(){
        int position = getCurrentPage();

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(Constants.KORAN_SAVE_PAGE, position > 0 ? position : 1);
        editor.apply();
    }

    private int getSavedPage(){
        return sharedPref.getInt(Constants.KORAN_SAVE_PAGE, 1);
    }

    private void setBookmark(String page) {

        if (bookmarks.contains(page)) {
            bookmarks.remove(page);
            binding.koranBookmark.setImageResource(R.drawable.ic_bookmark);
        } else {
            bookmarks.add(page);
            binding.koranBookmark.setImageResource(R.drawable.ic_bookmark_check);
        }

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putStringSet(Constants.KORAN_MARK_PAGE, bookmarks);
        editor.apply();
    }

    private Set<String> getBookmarks(){
        return sharedPref.getStringSet(Constants.KORAN_MARK_PAGE, new HashSet<>());
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(requireActivity(), viewModelFactory).get(MainViewModel.class);
    }

    private void initUserProfileObserve() {
        viewModel.getUserProfileLiveData().observe(getViewLifecycleOwner(), userProfile -> {
            subState = userProfile.isSubscribed();
        });
    }

    private String readJSONDataFromFile(int surah) throws IOException{

        InputStream inputStream = null;
        StringBuilder builder = new StringBuilder();

        try {

            String jsonString = null;
            inputStream = getResources().openRawResource(surah);
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(inputStream, "UTF-8"));

            while ((jsonString = bufferedReader.readLine()) != null) {
                builder.append(jsonString);
            }

        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return new String(builder);
    }

    private int getCurrentPage(){
        int page=0;
        if(binding.koranRecycler.getLayoutManager()!=null)
            page = ((LinearLayoutManager) binding.koranRecycler.getLayoutManager())
                .findFirstVisibleItemPosition();

        return Math.max(page, 0);
    }

    View.OnClickListener onClickListener = v -> {
        NavDirections action=null;
        switch (v.getId()){
            case R.id.koran_home:
                savePage();
                Navigation.findNavController(requireView()).popBackStack(R.id.mainFragment, false);
                break;
            case R.id.koran_settings:
                action = KoranFragmentDirections.actionKoranFragmentToKoranSettingsFragment();
                break;
            case R.id.koran_bookmark:
                setBookmark(String.valueOf(getCurrentPage()));
                break;
        }
        if(action!=null)
            Navigation.findNavController(requireView()).navigate(action);
    };

    @Override
    public void showError(String error) {
    }

    @Override
    public void selectPage(int position) {
        if(adapter != null){
            adapter.setStateFull(true);
            LinearLayoutManager manager = new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false);
            binding.koranRecycler.setLayoutManager(manager);
            binding.koranRecycler.scrollToPosition(position);
            binding.pageType.setText(String.valueOf(position));
        }
    }

    @Override
    public void selectReader() {
        Navigation.findNavController(requireView()).navigate(R.id.ReaderFragment);
    }

    @Override
    public void rollPage(int position) {
        closeKeyboard();
        binding.koranRecycler.scrollToPosition(position + 1);
    }

    @Override
    public void stateChange(boolean stateFull) {
        if(stateFull)
            selectMenu(BOTTOM_MENU_HIDE);
        else
            selectMenu(BOTTOM_MENU_PAGER);
    }

    @Override
    public void ayahSelect(int surah, int ayah) {
        currentAyah = ayah;
        currentSurah = surah;

        binding.play.setVisibility(View.VISIBLE);

        int currentReader = sharedPref.getInt(Constants.KORAN_CURRENT_READER, 0);
        ReadersRes reader = userRepository.getKoranReaders()[currentReader];

        KoranLineMenuDialog dialog = new KoranLineMenuDialog(this, reader, subState);
        dialog.show(requireActivity().getSupportFragmentManager(), "KoranLineMenuDialog");
    }

    public void selectMenu(int type){
        binding.pageSelectorView.setVisibility(View.GONE);
        binding.playerView.setVisibility(View.GONE);

        switch (type){
            case BOTTOM_MENU_PAGER:
                binding.pageSelectorView.setVisibility(View.VISIBLE);
                binding.seekPage.setProgress(getCurrentPage() - 1);
                break;
            case BOTTOM_MENU_PLAYER:
                binding.playerView.setVisibility(View.VISIBLE);

                try{
                    int page = getCurrentPage();
                    SurahRes surahRes = findSurah(viewModel.userRepository.getKoranPagesList().get(page).getSurahNumber(), false);
                    binding.playerSyrahName.setText(getTranslate(surahRes.getTranslate()));
                }catch (Exception ignored){}
                break;
        }
    }

    @Override
    public void linePlay() {

        mediaThread = new Thread(() -> {
            try {
                if (mediaPlayer != null)
                    mediaPlayer.stop();

                showPlayerProgress(true);

                AyahRes ayahRes = findAyah(currentSurah, currentAyah, true);

                if (ayahRes != null && ayahRes.getAudios() != null && ayahRes.getAudios().size() > 0) {
                    requireActivity().runOnUiThread(() ->
                            selectMenu(BOTTOM_MENU_PLAYER));

                    String audioUrl = ayahRes.getAudios().get(0).getAudio();

                    mediaPlayer = new MediaPlayer();

                    mediaPlayer.setAudioAttributes(
                            new AudioAttributes
                                    .Builder()
                                    .setLegacyStreamType(AudioManager.STREAM_MUSIC)
                                    .build());

                    mediaPlayer.setOnPreparedListener(mediaPlayer ->
                            showPlayerProgress(false));

                    mediaPlayer.setDataSource(audioUrl);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } else {
                    requireActivity().runOnUiThread(() -> selectMenu(BOTTOM_MENU_HIDE));
                }
            }catch (Exception ex){
                requireActivity().runOnUiThread(() -> selectMenu(BOTTOM_MENU_HIDE));
            }
        });
        mediaThread.start();
    }

    private void showPlayerProgress(boolean show) {
        requireActivity().runOnUiThread(() ->
                binding.playerProgress.setVisibility(show ? View.VISIBLE : View.GONE));
    }

    private AyahRes findAyah(int surah, int ayah, boolean audio) {

        SurahRes findSurah = findSurah(surah, true);
        String koranReaderId = sharedPref.getString(Constants.KORAN_CURRENT_READER_ID, "1");

        if (findSurah != null) {
            AyahRes ayahRes = findSurah.getAyahs().stream()
                    .filter(findAyahRes -> findAyahRes.getNumber().equals(String.valueOf(ayah)))
                    .findFirst().orElse(null);

            if (ayahRes == null)
                Toast.makeText(requireContext(), requireContext().getString(R.string.ayatEmpty), Toast.LENGTH_SHORT).show();
            else if (ayahRes.getAudios() != null && audio) {
                for (int i = 0; i < ayahRes.getAudios().size(); i++) {
                    if (ayahRes.getAudios().get(i).getReaderId().equals(koranReaderId) && !ayahRes.getAudios().get(i).getAudio().isEmpty()) {
                        return ayahRes;
                    } /*else {
                        String res = ayahRes.getAudios().get(i).getReaderName();
                        Toast.makeText(requireContext(), "Выберите этого чтеца : " + res, Toast.LENGTH_SHORT).show();
                    }*/
                }

                requireActivity().runOnUiThread(() ->
                        Toast.makeText(requireContext(), "Озвучки нет.", Toast.LENGTH_SHORT).show());

                return null;
            }
            return ayahRes;
        }

        return null;
    }

    private SurahRes findSurah(int surah, boolean log){
        SurahRes surahRes = viewModel.userRepository.getSurahList().stream()
                .filter(findSurahRes -> findSurahRes.getNumber().equals(String.valueOf(surah)))
                .findFirst()
                .orElse(null);

        if(surahRes == null && log)
            Toast.makeText(requireContext(), requireContext().getString(R.string.notSura), Toast.LENGTH_SHORT).show();

        return surahRes;
    }

    @Override
    public void lineTranslate() {
        Bundle bundle = new Bundle();

        AyahRes ayahRes = findAyah(currentSurah, currentAyah, false);

        if (ayahRes != null) {
            bundle.putInt("translate_ayah", currentAyah);
            //todo добавить выбор перевода из настроек

            bundle.putString("translate_text", getTranslate(ayahRes.getTranslate()));
            Navigation.findNavController(requireView()).navigate(R.id.translateLineDialog, bundle);
        }
    }

    @Override
    public void lineShare() {
        AyahRes ayahRes = findAyah(currentSurah, currentAyah, true);

        if(ayahRes != null) {
            if(ayahRes.getAudios().get(0).getAudio().isEmpty()){
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");

                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Сура " + currentSurah + " Аят " +
                        currentAyah + "\n" + ayahRes.getTranslate().get("ar") + "\n\n" + getTranslate(ayahRes.getTranslate()));
                startActivity(Intent.createChooser(sharingIntent, requireContext().getString(R.string.shareOne)));
            }else{
                new Thread(() -> {
                    try {
                        Context context = requireContext();
                        if(context!=null){
                            InputStream inputStream = new URL(ayahRes.getAudios().get(0).getAudio()).openStream();
                            Files.copy(inputStream, Paths.get(context.getExternalCacheDir().getPath() + "/ayah.mp3"), StandardCopyOption.REPLACE_EXISTING);

                            Uri newUri  = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", new File(context.getExternalCacheDir().getPath() + "/ayah.mp3"));
                            Intent intent = new Intent(Intent.ACTION_SEND);
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            intent.putExtra(Intent.EXTRA_STREAM, newUri);
                            intent.setType("audio/mp3");
                            intent.setType("text/plain");
                            intent.putExtra(android.content.Intent.EXTRA_TEXT, "Сура " + currentSurah + " Аят " +
                                    currentAyah + "\n" + ayahRes.getTranslate().get("ar") + "\n\n" + getTranslate(ayahRes.getTranslate()));
                            context.startActivity(Intent.createChooser(intent, requireContext().getString(R.string.shareOne)));
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }).start();
            }
        }
    }

    private String getTranslate(Map<String, String> translates) {
        String translate = translates.get(userRepository.getLanguage());
        return (translate != null && !translate.isEmpty()) ? translate : "Нет перевода";
    }

    @Override
    public void onPause() {
        if(mediaPlayer != null)
            mediaPlayer.stop();

        super.onPause();
    }

    @Override
    public void onDestroy() {
        if(mediaPlayer != null)
            mediaPlayer.stop();

        if(mediaThread != null && mediaThread.isAlive())
            mediaThread.interrupt();

        //savePage();

        super.onDestroy();
    }

    @Override
    public void showSubDialog(){
        AlertDialog alertDialog = new AlertDialog.Builder(requireContext()).create();
        alertDialog.setTitle(getString(R.string.sub));
        alertDialog.setMessage(getString(R.string.alert_sub_desc));
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.sub),
                (dialog, which) ->
                        Navigation.findNavController(requireView()).navigate(R.id.subRuStoreFragment)
        );
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.cancel),
                (dialog, which) ->
                        dialog.dismiss()
        );
        alertDialog.show();
    }
}
