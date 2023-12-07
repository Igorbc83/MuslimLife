package com.muslimlife.app.view.dialogs;

import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.muslimlife.app.BuildConfig;
import com.muslimlife.app.data.model.OtherEntity;
import com.muslimlife.app.data.model.azkars.AzkarsRes;
import com.muslimlife.app.data.repository.AzkarsRepository;
import com.muslimlife.app.utils.Constants;
import com.muslimlife.app.view.MainActivity;
import com.muslimlife.app.view.adapters.OtherAdapter;
import com.muslimlife.app.view.main.listeners.MainRecyclerListener;

import java.util.ArrayList;
import java.util.List;

import com.muslimlife.app.R;
import com.muslimlife.app.databinding.DialogWorshipBinding;

import javax.inject.Inject;

public class BottomWorshipDialog extends BottomSheetDialogFragment implements MainRecyclerListener {

    private DialogWorshipBinding binding;

    @Inject
    AzkarsRepository azkarsRepository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogWorshipBinding.inflate(inflater, container, false);
        Shader shader = new LinearGradient(0,0,0,binding.worshipTitelText.getLineHeight(),
                binding.getRoot().getContext().getColor(R.color.gradStart),
                binding.getRoot().getContext().getColor(R.color.gradEnd),
                Shader.TileMode.REPEAT);
        binding.worshipTitelText.getPaint().setShader(shader);
        List<OtherEntity> models = new ArrayList<>();

        if (getArguments() != null) {
            switch (getArguments().getString(Constants.MAIN_BOTTOM_DIALOG_TYPE)){
                case Constants.MAIN_BOTTOM_DIALOG_WORSHIP:
                    binding.worshipTitelText.setText(getString(R.string.worship));
                    models.add(new OtherEntity(R.id.QibleFragment, R.string.compass, R.drawable.ic_compass_new, R.color.light_white));
                    models.add(new OtherEntity(R.id.PrayerFragment, R.string.namaz, R.drawable.ic_namaz_new_2, R.color.light_white));
                    models.add(new OtherEntity(R.id.KoranFragment, R.string.koran, R.drawable.ic_koran_new_2, R.color.light_white));
                    models.add(new OtherEntity(R.id.ZakyatFragment, R.string.zakyat, R.drawable.ic_zakyat_new, R.color.light_white));
                    models.add(new OtherEntity(R.id.tasbihFragment, R.string.tasbih, R.drawable.ic_tasbih_new, R.color.light_white));
                    break;
                case Constants.MAIN_BOTTOM_DIALOG_OTHER:
                    binding.worshipTitelText.setText(getString(R.string.others));
                    binding.worshipTitelText.setVisibility(View.INVISIBLE);
                    models.add(new OtherEntity(R.id.SermonsFragment, R.string.sermons, R.drawable.ic_sermon, R.color.light_white));
                    models.add(new OtherEntity(R.id.TvFragment, R.string.video, R.drawable.ic_tv_new, R.color.light_white));
                    models.add(new OtherEntity(R.id.RadioFragment, R.string.radio, R.drawable.ic_radio_new, R.color.light_white));
                    //models.add(new OtherEntity(R.id.QuastionFragment, R.string.send_quastion, R.drawable.ic_imam_quastion_new, R.color.light_white));
                    //todo test hide diaspoars
                    //models.add(new OtherEntity(R.id.DiaspoarsFragment, R.string.diaspoars, R.drawable.ic_diaspora, R.color.light_white));
                    models.add(new OtherEntity(R.id.WallpaperFragment, R.string.wallpaper, R.drawable.ic_wallpapers, R.color.light_white));

                    /*if(BuildConfig.DEBUG) {
                        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                        models.add(new OtherEntity(
                                "https://niivt.com/mlc" + (firebaseUser != null ? ("?user_email=" + firebaseUser.getEmail()) : ""), R.string.test, R.drawable.icon_coin, R.color.light_white));
                        models.add(new OtherEntity(
                                "https://niivt.com/edu/game" + (firebaseUser != null ? ("?user_email=" + firebaseUser.getEmail()) : ""), R.string.test2, R.drawable.bg_act, R.color.light_white));
                    }*/
                    break;
                case Constants.MAIN_BOTTOM_DIALOG_AZKARS:
                    AzkarsRes azkars = ((MainActivity) requireActivity()).azkarsRepository.getAzkarsRes();

                    if (azkars != null) {
                        if(azkars.getMorning() != null && !azkars.getMorning().isEmpty())
                            models.add(new OtherEntity(Constants.AZKAR_MORNING, R.string.azkar_morning, R.drawable.ic_azkars_morning, R.color.light_white));

                        if(azkars.getEvening() != null && !azkars.getEvening().isEmpty())
                            models.add(new OtherEntity(Constants.AZKAR_EVENING, R.string.azkar_evening, R.drawable.ic_azkars_evening, R.color.light_white));

                        if(azkars.getImportant() != null && !azkars.getImportant().isEmpty())
                            models.add(new OtherEntity(Constants.AZKAR_IMPORTANT, R.string.azkar_important, R.drawable.ic_azkars_important, R.color.light_white));

                        if(azkars.getAfterNamaz() != null && !azkars.getAfterNamaz().isEmpty())
                            models.add(new OtherEntity(Constants.AZKAR_AFTER_NAMAZ, R.string.azkar_after_namaz, R.drawable.ic_azkars_after_pray, R.color.light_white));
                    }
                    break;
            }
        }

        binding.worshipGv.setAdapter(
                new OtherAdapter(
                        getArguments().getString(Constants.MAIN_BOTTOM_DIALOG_TYPE),
                        requireActivity(),
                        R.layout.item_other,
                        models,
                        this));

        binding.dialogClose.setOnClickListener(v -> dismiss());

        return binding.getRoot();
    }

    @Override
    public void selectWorship(int type) {
        Navigation.findNavController(requireActivity(), R.id.nav_host).navigate(type);
    }

    @Override
    public void selectAzkars(int type) {
        Bundle arg = new Bundle();
        arg.putInt(Constants.BUNDLE_AZKAR ,type);
        Navigation.findNavController(requireActivity(), R.id.nav_host).navigate(R.id.azkarListFragment, arg);
    }
}
