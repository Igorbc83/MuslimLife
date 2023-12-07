package com.muslimlife.app.view.auth;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;
import com.muslimlife.app.data.model.DialogInputType;
import com.muslimlife.app.data.model.Save64Res;
import com.muslimlife.app.data.model.UserLocation;
import com.muslimlife.app.data.repository.UserRepository;
import com.muslimlife.app.di.viewModel.DaggerViewModelFactory;
import com.muslimlife.app.utils.Constants;
import com.muslimlife.app.view.MainActivity;
import com.muslimlife.app.view.base.BaseFragment;
import com.muslimlife.app.viewmodel.auth.AuthViewModel;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import ru.tinkoff.decoro.MaskImpl;
import ru.tinkoff.decoro.slots.PredefinedSlots;
import ru.tinkoff.decoro.slots.Slot;
import ru.tinkoff.decoro.watchers.FormatWatcher;
import ru.tinkoff.decoro.watchers.MaskFormatWatcher;
import com.muslimlife.app.R;
import com.muslimlife.app.databinding.FragmentRegProfileBinding;

public class RegProfileFragment extends BaseFragment {

    private FragmentRegProfileBinding binding;

    @Inject
    DaggerViewModelFactory viewModelFactory;
    private AuthViewModel viewModel;

    private List<UserLocation> countryList;
    private List<UserLocation> cityList;
    private UserLocation currentCountry;
    private UserLocation currentCity;

    private DialogInputType currentInputType = DialogInputType.NAME;
    @Inject
    UserRepository userRepository;
    private File file;
    private File photoFile;
    String avatarBase64;
    String country = "";
    String city ="";

    private static final int PICK_IMAGE = 80;
    private static final int REQUEST_TAKE_PHOTO = 1445;
    public static final Slot[] RUS_PHONE_NUMBER = {
            PredefinedSlots.digit(),
            PredefinedSlots.hardcodedSlot(' ').withTags(Slot.TAG_DECORATION),
            PredefinedSlots.hardcodedSlot('(').withTags(Slot.TAG_DECORATION),
            PredefinedSlots.digit(),
            PredefinedSlots.digit(),
            PredefinedSlots.digit(),
            PredefinedSlots.hardcodedSlot(')').withTags(Slot.TAG_DECORATION),
            PredefinedSlots.hardcodedSlot(' ').withTags(Slot.TAG_DECORATION),
            PredefinedSlots.digit(),
            PredefinedSlots.digit(),
            PredefinedSlots.digit(),
            PredefinedSlots.hardcodedSlot('-').withTags(Slot.TAG_DECORATION),
            PredefinedSlots.digit(),
            PredefinedSlots.digit(),
            PredefinedSlots.hardcodedSlot('-').withTags(Slot.TAG_DECORATION),
            PredefinedSlots.digit(),
            PredefinedSlots.digit(),
    };
    private String email;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRegProfileBinding.inflate(inflater, container, false);
        ((MainActivity) requireActivity()).setMenu(Constants.MENU_OFF);
        initViewModel();
        setViewClickListeners();
        initObservablesDialog();
        initObservableInfo();
        MaskImpl mask = MaskImpl.createTerminated(RUS_PHONE_NUMBER);
        FormatWatcher watcher = new MaskFormatWatcher(mask);
        watcher.installOn(binding.etPhone);

        return binding.getRoot();
    }

    // requireActivity() позволяет взять один экземляр AuthViewModel для двух фрагментов.
    private void initViewModel() {
        viewModel = new ViewModelProvider(requireActivity(), viewModelFactory).get(AuthViewModel.class);

        viewModel.loadCountry();
        viewModel.loadCities("");
    }

    private void initObservableInfo() {
        viewModel.getIdLiveData().observe(getViewLifecycleOwner(), userId -> {
            saveUserId(userId);
            if(file!=null)
                userRepository.updateImage(userId,file).subscribe(new SingleObserver<>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onSuccess(Save64Res save64Res) {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("UploadImage", e.getMessage());
                    }
                });
                launchMainFragment();
        });
        viewModel.getUserProfileLiveData().observe(getViewLifecycleOwner(),userProfile -> {
            if (userProfile.getEmail()!=null){
                launchMainFragment();
            }
        });

        viewModel.getCountriesLiveData().observe(getViewLifecycleOwner(), result -> countryList = result);
        viewModel.getCitiesLiveData().observe(getViewLifecycleOwner(), result -> cityList = result);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String email= FirebaseAuth.getInstance().getCurrentUser().getEmail();
        binding.textEmail.setText(email);
        viewModel.checkUserInApi(email,requireActivity());


    }

    private void initObservablesDialog() {
        // Обработка результата работы фрагмента ввода данных (имя, фамилия, почта)
        requireActivity()
                .getSupportFragmentManager()
                .setFragmentResultListener(
                        Constants.KEY_DIALOG_INPUT_RESULT,
                        getViewLifecycleOwner(),
                        (requestKey, result) -> {
                            String text = result.getString(Constants.KEY_DIALOG_INPUT_RESULT_TEXT, "");
                            switch (currentInputType) {
                                case NAME:
                                    if(text.isEmpty()) {
                                        binding.textName.setText(getString(R.string.name));
                                    } else {
                                        binding.textName.setText(text);
                                    }
                                    break;
                                case LAST_NAME:
                                    if(text.isEmpty()) {
                                        binding.textSecondName.setText(getString(R.string.surname));
                                    } else {
                                        binding.textSecondName.setText(text);
                                    }
                                    break;
                                case EMAIL:
                                    if (text.isEmpty()) {
                                        binding.textEmail.setText(getString(R.string.email));
                                    } else {
                                        binding.textEmail.setText(text);
                                    }
                                    break;
                                case COUNTRY:
                                    if (text.isEmpty()) {
                                        //binding.textCountryTitle.setVisibility(View.GONE);
                                        binding.textCountry.setText(getString(R.string.location_country));
                                    } else {
                                        binding.textCountryTitle.setVisibility(View.VISIBLE);
                                        binding.textCountry.setText(text);
                                        currentCountry = result.getParcelable(Constants.KEY_DIALOG_INPUT_RESULT_USER_LOCATION);
                                    }
                                    break;
                                case CITY:
                                    if (text.isEmpty()) {
                                        binding.textCity.setText(getString(R.string.location_city));
                                    } else {
                                        binding.textCity.setText(text);
                                        currentCity = result.getParcelable(Constants.KEY_DIALOG_INPUT_RESULT_USER_LOCATION);
                                    }
                                    break;
                            }
                        }
                );
    }

    private void setViewClickListeners() {
        binding.buttonSave.setOnClickListener(v -> {

               String phoneNumber = String.format("+%s", binding.etPhone.getText().toString().replace("(", "")
                        .replace(")", "")
                        .replace(" ", "")
                        .replace("-", ""));

                String name = binding.textName.getText().toString();
                String secondName = binding.textSecondName.getText().toString();
                String email = binding.textEmail.getText().toString();

                for (UserLocation userLocation:countryList)
                    if (currentCountry!=null)
                    if(userLocation!=null && userLocation.getId().equals(currentCountry.getId())){
                        binding.textCountry.setText(userLocation.getName());
                        country = userLocation.getId();
                        break;
                    }
                    else
                        binding.textCountry.setText("");



                for (UserLocation userLocation:cityList)
                    if (currentCity!=null)
                    if(userLocation!=null && userLocation.getId().equals(currentCity.getId())){
                        binding.textCity.setText(userLocation.getName());
                        city = userLocation.getId();
                        break;
                    }
                    else{
                        binding.textCity.setText("");
                    }

                viewModel.registerUserInApi(phoneNumber, name, FirebaseAuth.getInstance().getCurrentUser().getUid(), requireActivity(), secondName, country, city, avatarBase64, email);

        });

        binding.emptyImage.setOnClickListener(v-> openGallery());

        binding.makePhoto.setOnClickListener(v-> dispatchTakePictureIntent());

        binding.country.setOnClickListener(v -> {
            currentInputType = DialogInputType.COUNTRY;
            launchDialogChooseCountry();
        });

        binding.city.setOnClickListener(v -> {
            currentInputType = DialogInputType.CITY;
            launchDialogChooseCity();
        });


    }

    private void initActivityResult() {
        requireActivity().registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == Activity.RESULT_OK) {
                        try {
                            if(result.getData() == null)
                                return;

                            final Uri imageUri = result.getData().getData();
                            final InputStream imageStream = requireActivity().getContentResolver().openInputStream(imageUri);
                            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                            showError(e.getLocalizedMessage());
                        }
                    }
                }
        );
    }


    @Override
    public void showError(String error) {
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
    }

    // TODO: 01.07.2021 Изменить на общее решение
    private void saveUserId(String userId) {
        SharedPreferences sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Constants.SP_KEY_USER_ID, userId);
        editor.apply();
    }

    /**
     * Отдел отвечающий за навигацию
     */
    private void launchMainFragment() {
        NavDirections action = RegProfileFragmentDirections.regToMain();
        Navigation.findNavController(requireView()).navigate(action);
    }

    private void launchDialogInput(DialogInputType type, String currentText) {
        NavDirections direction = RegProfileFragmentDirections.regProfileToDialogInput(type, currentText);
        Navigation.findNavController(requireView()).navigate(direction);
    }

    private void launchDialogChooseCountry() {
        if (countryList == null)
            return;

        String countryId = null;
        if (currentCountry != null)
            countryId = currentCountry.getId();

        NavDirections direction = RegProfileFragmentDirections.regToDialogChooseCountry(countryId, countryList.toArray(new UserLocation[0]),"0");
        Navigation.findNavController(requireView()).navigate(direction);
    }

    private void launchDialogChooseCity() {
        if (cityList == null || currentCountry == null)
            return;

        String cityId = null;
        if (currentCity != null)
            cityId = currentCountry.getId();

        List<UserLocation> userLocations = new ArrayList<>();
        cityList.forEach(userLocation -> {
            if(userLocation.getCountryId().equals(currentCountry.getId()))
                userLocations.add(userLocation);
        });

        NavDirections direction = RegProfileFragmentDirections.regToDialogChooseCountry(cityId, userLocations.toArray(new UserLocation[0]),"1");
        Navigation.findNavController(requireView()).navigate(direction);
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        return File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    public void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Toast.makeText(requireContext(), "Error!", Toast.LENGTH_SHORT).show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(requireContext(),
                        requireActivity().getPackageName() + ".provider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private void uploadAvatar(Bitmap bitmap){
        binding.emptyImage.setImageBitmap(bitmap);
        try {
            file = new File(requireContext().getExternalCacheDir().getPath() + "/avatar.png");
            OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            Uri imageUri = data.getData();

            if (imageUri != null) {

                Bitmap bitmap = null;
                try {
                    InputStream imageStream = requireContext().getContentResolver().openInputStream(imageUri);
                    bitmap = BitmapFactory.decodeStream(imageStream);
                }catch (Exception ex){}


                if(bitmap == null || bitmap.getHeight() == 0){
                    Toast.makeText(requireContext(), requireContext().getString(R.string.chooseOtherPhoto), Toast.LENGTH_SHORT).show();
                    return;
                }

                bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getHeight(), bitmap.getWidth(), false);
                uploadAvatar(bitmap);

            }
        } else if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            String path = photoFile.getPath();
            Bitmap bitmap = BitmapFactory.decodeFile(path);

            if (bitmap == null || bitmap.getHeight() == 0) {
                Toast.makeText(requireContext(), requireContext().getString(R.string.chooseOtherPhoto), Toast.LENGTH_SHORT).show();
                return;
            }

            bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getHeight(), bitmap.getWidth(), false);
            uploadAvatar(bitmap);
        }
    }
}
