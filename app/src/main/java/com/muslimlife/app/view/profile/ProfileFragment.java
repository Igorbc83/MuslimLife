package com.muslimlife.app.view.profile;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
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
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.muslimlife.app.data.model.DialogInputType;
import com.muslimlife.app.data.model.Save64Res;
import com.muslimlife.app.data.model.UserLocation;
import com.muslimlife.app.data.model.UserProfile;
import com.muslimlife.app.data.repository.UserRepository;
import com.muslimlife.app.di.viewModel.DaggerViewModelFactory;
import com.muslimlife.app.utils.Constants;
import com.muslimlife.app.view.MainActivity;
import com.muslimlife.app.view.base.BaseFragment;
import com.muslimlife.app.viewmodel.profile.ProfileViewModel;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import all.muslimlife.utils.ResizeImage;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import ru.tinkoff.decoro.MaskImpl;
import ru.tinkoff.decoro.slots.PredefinedSlots;
import ru.tinkoff.decoro.slots.Slot;
import ru.tinkoff.decoro.watchers.FormatWatcher;
import ru.tinkoff.decoro.watchers.MaskFormatWatcher;
import com.muslimlife.app.R;
import com.muslimlife.app.databinding.FragmentProfileBinding;

public class ProfileFragment extends BaseFragment {

    private FragmentProfileBinding binding;

    private DialogInputType currentInputType = DialogInputType.NAME;

    @Inject
    DaggerViewModelFactory viewModelFactory;
    private ProfileViewModel viewModel;

    private List<UserLocation> countryList;
    private List<UserLocation> cityList;
    private UserLocation currentCountry;
    private String currentCountryStroke;
    private UserLocation currentCity;
    String avatarBase64;
    @Inject
    UserRepository userRepository;

    private UserProfile userProfile;

    private File photoFile;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        ((MainActivity)requireActivity()).setMenu(Constants.MENU_OFF);
        countryList = userRepository.getCountriesList();
        cityList = userRepository.getCitiesList();
        initObservablesDialog();
        setViewClickListener();
        initViewModel();
        initUserProfileObserve();
        MaskImpl mask = MaskImpl.createTerminated(RUS_PHONE_NUMBER);
        FormatWatcher watcher = new MaskFormatWatcher(mask);
        watcher.installOn(binding.textPhone);
        return binding.getRoot();
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(requireActivity(), viewModelFactory).get(ProfileViewModel.class);
        viewModel.loadCountry();
        viewModel.loadCities("");
    }

    private void initUserProfileObserve() {
        if(viewModel!=null){
            viewModel.getUserProfileLiveData().observe(getViewLifecycleOwner(), userProfile -> {
                this.userProfile = userProfile;
                binding.textName.setText(userProfile.getName());
                String phone = userProfile.getPhone();
                if(phone!=null && phone.length()>11)
                    binding.textPhone.setText(phone.substring(0,2) +" ("+phone.substring(2,5)+") "+phone.substring(5,8)+"-"+phone.substring(8,10)+"-"+phone.substring(10,12));
                binding.textSecondName.setText(userProfile.getLastName());
                binding.textEmail.setText(userProfile.getEmail());

                if(countryList!=null)
                    for (UserLocation userLocation:countryList)
                        if(userLocation.getId().equals(userProfile.getCountry())){
                            currentCountryStroke=userLocation.getName();
                            binding.textCountry.setText(userLocation.getName());
                            break;
                        }
                        else{
                            currentCountryStroke="";
                            binding.textCountry.setText("");
                        }

                if(cityList!=null)
                    for (UserLocation userLocation:cityList)
                        if(userLocation.getId().equals(userProfile.getCity())){
                            binding.textCity.setText(userLocation.getName());
                            break;
                        }
                        else{
                            binding.textCity.setText("");
                        }

                if(userProfile.getAvatar()==null)
                    binding.emptyImage.setImageDrawable(requireContext().getDrawable(R.drawable.ic_empty_photo));
                else
                    Glide.with(requireContext()).load(userProfile.getAvatar()).into(binding.emptyImage);
            });

            viewModel.getCountriesLiveData().observe(getViewLifecycleOwner(), result -> {
                countryList = result;
                if (userProfile.getCountry() != null && countryList != null && currentCountryStroke != null)
                    for (UserLocation country : countryList)
                        if (currentCountryStroke.equals(country.getName()))
                            currentCountry = country;
            });
            viewModel.getCitiesLiveData().observe(getViewLifecycleOwner(), result -> cityList = result);
        }
    }

    private void initObservablesDialog() {
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
                                        userProfile.setName(text);
                                    }
                                    break;
                                case LAST_NAME:
                                    if(text.isEmpty()) {
                                        binding.textSecondName.setText(getString(R.string.surname));
                                    } else {
                                        binding.textSecondName.setText(text);
                                        userProfile.setLastName(text);
                                    }
                                    break;
                                case EMAIL:
                                    if(text.isEmpty()) {
                                        binding.textEmail.setText(getString(R.string.email));
                                    } else {
                                        binding.textEmail.setText(text);
                                        userProfile.setEmail(text);
                                    }
                                    break;
                                case PHONE_NUMBER:
                                    if(text.isEmpty()) {
                                        binding.textPhone.setText(getString(R.string.phone));
                                    } else {
                                        binding.textPhone.setText(text);
                                        userProfile.setPhone(text);
                                    }
                                    break;
                                case COUNTRY:
                                    if(text.isEmpty()) {
                                        binding.textCountry.setText(getString(R.string.location_country));
                                    } else {
                                        currentCountry = result.getParcelable(Constants.KEY_DIALOG_INPUT_RESULT_USER_LOCATION);
                                        userProfile.setCountry(currentCountry.getId());
                                        currentCountryStroke = currentCountry.getName();
                                        binding.textCountry.setText(currentCountryStroke);
                                        userProfile.setCity("");
                                        binding.textCity.setText(getString(R.string.location_city));
                                    }
                                    break;
                                case CITY:
                                    if(text.isEmpty()) {
                                        binding.textCity.setText(getString(R.string.location_city));
                                    } else {
                                        currentCity = result.getParcelable(Constants.KEY_DIALOG_INPUT_RESULT_USER_LOCATION);
                                        binding.textCity.setText(currentCity.getName());
                                        userProfile.setCity(currentCity.getId());
                                    }
                                    break;
                            }
                            viewModel.updateProfileData(userProfile);
                        }
                );
    }

    private void setViewClickListener() {
        initObservablesDialog();

        binding.textinputHelperText.setTypeface(Typeface.createFromAsset(requireContext().getAssets(),"montserrat_black.ttf"));
        binding.nameMot.setTypeface(Typeface.createFromAsset(requireContext().getAssets(),"montserrat_black.ttf"));
        binding.surnameLayout.setTypeface(Typeface.createFromAsset(requireContext().getAssets(),"montserrat_black.ttf"));

        binding.countryLayout.setOnClickListener(v -> {
            currentInputType = DialogInputType.COUNTRY;
            launchDialogChooseCountry();
        });

        binding.cityLayout.setOnClickListener(v -> {
            currentInputType = DialogInputType.CITY;
            launchDialogChooseCity();
        });

        binding.backButton.setOnClickListener(v-> Navigation.findNavController(requireView()).popBackStack());

        binding.profileExit.setOnClickListener(v-> {
            Toast.makeText(requireContext(), requireContext().getString(R.string.exitOk), Toast.LENGTH_SHORT).show();
            FirebaseAuth.getInstance().signOut();
            SharedPreferences sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE);
            sharedPref.edit().remove(Constants.SP_KEY_USER_PHONE).apply();
            sharedPref.edit().remove(Constants.SP_KEY_USER_EMAIL).apply();
            sharedPref.edit().remove(Constants.IS_USER).apply();
            sharedPref.edit().putString(Constants.SP_KEY_USER_ID,"0").apply();
            startAuthFragment();
        });

        binding.saveButton.setOnClickListener(v->{
            userProfile.setName(String.valueOf(binding.textName.getText()));
            userProfile.setLastName(String.valueOf(binding.textSecondName.getText()));
            userProfile.setEmail(String.valueOf(binding.textEmail.getText()));
            String phoneNumber = String.format("+%s", binding.textPhone.getText().toString().replace("(", "")
                    .replace(")", "")
                    .replace(" ", "")
                    .replace("-", ""));
            userProfile.setPhone(phoneNumber);
            viewModel.updateProfileData(userProfile);
            requireActivity().getPreferences(Context.MODE_PRIVATE).edit().putString(Constants.COUNTRY, userProfile.getCountry()).apply();
            Navigation.findNavController(requireView()).popBackStack();
        } );

        binding.emptyImage.setOnClickListener(v-> openGallery());

        binding.makePhoto.setOnClickListener(v-> dispatchTakePictureIntent());
    }

    @Override
    public void showError(String error) {
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
    }

    /**
     * Отдел отвечающий за навигацию
     */
    private void launchDialogInput(DialogInputType type, String currentText) {
        if (getView() == null) {
            return;
        }

        NavDirections direction = ProfileFragmentDirections.profileToDialogInput(type, currentText);
        Navigation.findNavController(getView()).navigate(direction);
    }

    private void launchDialogChooseCountry() {
        if (countryList == null)
            return;

        String countryId = null;

        if(currentCountryStroke!=null)
            for (UserLocation country:countryList){
                country.setSelected(false);
                if(currentCountryStroke.equals(country.getName()))
                    countryId = country.getId();
            }

        NavDirections direction = ProfileFragmentDirections.profileToDialogChooseCountry(countryId, countryList.toArray(new UserLocation[0]),"0");
        Navigation.findNavController(requireView()).navigate(direction);
    }

    private void launchDialogChooseCity() {
        if (cityList == null || currentCountry == null) {
            return;
        }

        String cityId = null;
        if (currentCity != null) {
            cityId = currentCity.getId();
        }

        List<UserLocation> userLocations = new ArrayList<>();
        cityList.forEach(userLocation -> {
            if (userLocation.getCountryId().equals(currentCountry.getId()))
                userLocations.add(userLocation);
        });

        NavDirections direction = ProfileFragmentDirections.profileToDialogChooseCountry(cityId, userLocations.toArray(new UserLocation[0]),"1");
        Navigation.findNavController(requireView()).navigate(direction);
    }

    private void startAuthFragment() {
        NavDirections action = ProfileFragmentDirections.actionProfileFragmentToNewAuthFragment();
        Navigation.findNavController(requireView()).navigate(action);
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        return File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
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
                Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void sendPhoto(Bitmap image){
        try {
            Bitmap resizedImage = ResizeImage.compressBitmap(image);
            File file = new File(requireContext().getExternalCacheDir().getPath() + "/avatar.png");
            OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
            resizedImage.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.close();
            String id = viewModel.getUserProfileLiveData().getValue().getId();
            userRepository.updateImage(id,file).subscribe(new SingleObserver<>() {
                @Override
                public void onSubscribe(Disposable d) {
                }

                @Override
                public void onSuccess(Save64Res save64Res) {
                    Glide.with(requireContext()).load(save64Res.getAvatar()).into(binding.emptyImage);
                    userProfile.setAvatar(save64Res.getAvatar());
                    viewModel.updateProfileData(userProfile);
                }

                @Override
                public void onError(Throwable e) {
                    Log.e("UploadImage", e.getMessage());
                }
            });
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
                }catch (Exception ex){
                    Log.e("ImageError",ex.getMessage());
                }


                if(bitmap == null || bitmap.getHeight() == 0){
                    Toast.makeText(requireContext(), requireContext().getString(R.string.chooseOtherPhoto), Toast.LENGTH_SHORT).show();
                    return;
                }

                //bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getHeight(), bitmap.getWidth(), false);
                sendPhoto(bitmap);

            }
        } else if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            String path = photoFile.getPath();
            Bitmap bitmap = BitmapFactory.decodeFile(path);

            if(bitmap == null || bitmap.getHeight() == 0){
                Toast.makeText(requireContext(), requireContext().getString(R.string.chooseOtherPhoto), Toast.LENGTH_SHORT).show();
                return;
            }

            //bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getHeight(), bitmap.getWidth(), false);
            sendPhoto(bitmap);

        }
    }
}
