package com.muslimlife.app.view.reader;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.muslimlife.app.data.repository.UserRepository;
import com.muslimlife.app.utils.Constants;
import com.muslimlife.app.view.MainActivity;
import com.muslimlife.app.view.base.BaseFragment;

import javax.inject.Inject;

import com.muslimlife.app.databinding.FragmentReaderBinding;

public class ReaderFragment extends BaseFragment {

    private FragmentReaderBinding binding;

    @Inject
    UserRepository userRepository;
    SharedPreferences sharedPref;
    ReaderAdapter readerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentReaderBinding.inflate(inflater, container, false);
        ((MainActivity)requireActivity()).setMenu(Constants.MENU_OFF);
        binding.backButton.setOnClickListener(view->Navigation.findNavController(requireView()).popBackStack());

        binding.readerRedyButton.setOnClickListener(view->{
            Navigation.findNavController(requireView()).popBackStack();
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt(Constants.KORAN_CURRENT_READER, readerAdapter.getCurrentReader()).apply();
            editor.putString(Constants.KORAN_CURRENT_READER_ID, readerAdapter.getCurrentReaderId()).apply();
        });

        sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE);

        getReaders();
        return binding.getRoot();
    }

    private void getReaders(){
        /*userRepository.getReaders().subscribe(new SingleObserver<ReadersRes[]>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onSuccess(@NonNull ReadersRes[] readersRes) {

                userRepository.setKoranReaders(readersRes);
                int currentReader=sharedPref.getInt(KORAN_CURRENT_READER, 0);
                readerAdapter= new ReaderAdapter(readersRes,currentReader);
                LinearLayoutManager llm = new LinearLayoutManager(requireContext());
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                binding.readerRv.setLayoutManager(llm);
                binding.readerRv.setAdapter(readerAdapter);
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }
        });*/

        if(userRepository.getKoranReaders() != null) {
            int currentReader = sharedPref.getInt(Constants.KORAN_CURRENT_READER, 0);
            readerAdapter = new ReaderAdapter(userRepository.getKoranReaders(), currentReader);
            LinearLayoutManager llm = new LinearLayoutManager(requireContext());
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            binding.readerRv.setLayoutManager(llm);
            binding.readerRv.setAdapter(readerAdapter);
        }
    }

    @Override
    public void showError(String error) {
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
    }
}
