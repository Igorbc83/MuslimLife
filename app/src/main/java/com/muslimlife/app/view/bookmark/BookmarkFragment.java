package com.muslimlife.app.view.bookmark;

import static com.muslimlife.app.utils.Constants.MENU_OFF;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import androidx.recyclerview.widget.RecyclerView;
import com.muslimlife.app.R;
import com.muslimlife.app.data.model.Bookmark;
import com.muslimlife.app.data.model.surah.SurahRes;
import com.muslimlife.app.data.repository.UserRepository;
import com.muslimlife.app.databinding.FragmentBookmarkBinding;
import com.muslimlife.app.databinding.FragmentKoranSettingsBinding;
import com.muslimlife.app.di.viewModel.DaggerViewModelFactory;
import com.muslimlife.app.utils.Constants;
import com.muslimlife.app.view.MainActivity;
import com.muslimlife.app.view.base.BaseFragment;
import com.muslimlife.app.viewmodel.main.MainViewModel;

public class BookmarkFragment extends BaseFragment {

    private FragmentBookmarkBinding binding;
    BookmarkAdapter bookmarkAdapter;

    @Inject
    DaggerViewModelFactory viewModelFactory;
    private MainViewModel viewModel;

    SharedPreferences sharedPref;

    @Inject
    UserRepository userRepository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding =  FragmentBookmarkBinding.inflate(inflater, container, false);

        initViewModel();
        sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);

        ((MainActivity)requireActivity()).setMenu(MENU_OFF);
        binding.backButton.setOnClickListener(onClickListener);

        Set<String> bookmarkPages = getBookmarks();
        List<Bookmark> bookmarks = new ArrayList<>();

        if(!bookmarkPages.isEmpty()){
            for(String page : bookmarkPages) {
                SurahRes surah = findSurah(viewModel.userRepository.getKoranPagesList().get(Integer.parseInt(page)).getSurahNumber(), false);

                if(surah==null)
                    continue;

                String translate = surah.getTranslate().get(userRepository.getLanguage());

                if(translate == null || translate.isEmpty())
                    translate = "Нет перевода";
                bookmarks.add(new Bookmark(page, surah != null ? translate : requireContext().getString(R.string.notSura)));
            }

            BookmarkAdapter.OnItemClickChildren clickChildren = (items) -> {
                navigateToKoran(Integer.parseInt(items.getNumber()));
            };

            bookmarkAdapter = new BookmarkAdapter(bookmarks, clickChildren);
            LinearLayoutManager llm = new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false);
            binding.bookmarkRv.setLayoutManager(llm);
            binding.bookmarkRv.setAdapter(bookmarkAdapter);
        }

        return binding.getRoot();
    }

    private void navigateToKoran(int page){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(Constants.KORAN_SAVE_PAGE, page);
        editor.apply();

        Navigation.findNavController(requireView()).popBackStack(R.id.KoranFragment, false);
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(requireActivity(), viewModelFactory).get(MainViewModel.class);
    }

    View.OnClickListener onClickListener = v -> {
        switch (v.getId()){
            case R.id.back_button:
                Navigation.findNavController(requireView()).popBackStack();
                break;

        }
    };

    private SurahRes findSurah(int surah, boolean log){
        SurahRes surahRes = viewModel.userRepository.getSurahList().stream()
                .filter(findSurahRes -> findSurahRes.getNumber().equals(String.valueOf(surah)))
                .findFirst()
                .orElse(null);

        if(surahRes == null && log)
            Toast.makeText(requireContext(), requireContext().getString(R.string.notSura), Toast.LENGTH_SHORT).show();

        return surahRes;
    }

    private Set<String> getBookmarks(){
        return sharedPref.getStringSet(Constants.KORAN_MARK_PAGE, new HashSet<>());
    }

    @Override
    public void showError(String error) {
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
    }
}
