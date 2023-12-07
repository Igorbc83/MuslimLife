package com.muslimlife.app.view.prayer;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.muslimlife.app.data.model.PushTime;
import com.muslimlife.app.data.model.UserLocation;
import com.muslimlife.app.data.model.UserProfile;
import com.muslimlife.app.data.repository.UserRepository;
import com.muslimlife.app.di.viewModel.DaggerViewModelFactory;
import com.muslimlife.app.utils.Constants;
import com.muslimlife.app.utils.PrayerListener;
import com.muslimlife.app.utils.PrayerTimeUtil;
import com.muslimlife.app.utils.ServicesUtil;
import com.muslimlife.app.view.MainActivity;
import com.muslimlife.app.view.base.BaseFragment;
import com.muslimlife.app.viewmodel.main.MainViewModel;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import com.muslimlife.app.R;
import com.muslimlife.app.databinding.FragmentPrayerBinding;

public class PrayerFragment extends BaseFragment implements PrayerListener {


    private FragmentPrayerBinding binding;
    PrayerAdapter prayerAdapter;
    PrayerTimeUtil prayerTimeUtil;

    UserProfile userProfile;

    @Inject
    UserRepository userRepository;

    @Inject
    DaggerViewModelFactory viewModelFactory;
    private MainViewModel viewModel;
    FusedLocationProviderClient fusedLocationProviderClient;
    PrayerListener prayerListener = this;
    boolean requestSended = false;
    Context context;
    boolean flagRespGeo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPrayerBinding.inflate(inflater, container, false);
        ((MainActivity)requireActivity()).setMenu(Constants.MENU_OFF);
        binding.backButton.setOnClickListener(onClickListener);
        binding.currentDate.setText(requireContext().getString(R.string.today) + " " + OffsetDateTime.now().getDayOfMonth() + " " + getMonth());

        initViewModel();
        //initUserProfileObserve();

        ServicesUtil.updateScore(requireActivity().getPreferences(Context.MODE_PRIVATE), Constants.SERVICE_NAMAZ);
        context = requireContext();
        userProfile = new UserProfile();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext());

        getLocation();

        binding.prayerLink.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(
                    getString(R.string.namaz_link).replace("<u>", "")
                            .replace("</u>", "")
                    ));
            startActivity(browserIntent);
        });

        return binding.getRoot();
    }

    private String getMonth(){
        return getResources().getStringArray(R.array.months)[Calendar.getInstance().getTime().getMonth()];
    }


    private void initPrayerList(){
        try {
            int[] names=prayerTimeUtil.names;
            ArrayList<String> prayerTimes = prayerTimeUtil.prayerTimes;
            int current = prayerTimeUtil.getCurrentPrayer();

            prayerAdapter = new PrayerAdapter(requireContext(),prayerTimes,names,current);

            LinearLayoutManager llm = new LinearLayoutManager(requireContext());
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            binding.prayerRv.setLayoutManager(llm);
            binding.prayerRv.setAdapter(prayerAdapter);

        }catch (Exception e){
            Log.e("CatchThreadErrorPrayer",e.getMessage());
        }
    }

    View.OnClickListener onClickListener = v -> {
        switch (v.getId()){
            case R.id.back_button:
                Navigation.findNavController(requireView()).popBackStack();
                break;
        }
    };

    @Override
    public void showError(String error) {
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void Timer(String toPrayer,String toPrayerNamaz) {
        requireActivity().runOnUiThread(() ->  {
            if(toPrayerNamaz!=null && !toPrayerNamaz.isEmpty() && !toPrayerNamaz.equals("00:00"))
                binding.blockTime.setVisibility(View.VISIBLE);
            binding.prayerTime.setText(toPrayerNamaz);
        });

    }

    @Override
    public void Name(int name) {
        requireActivity().runOnUiThread(() -> {
            binding.prayerName.setText(name);
            binding.prayerName.setText(binding.prayerName.getText() + " ");
        });
    }

    @Override
    public void Time(String time) {

    }

    @Override
    public void pushTime(PushTime pushTime) {

    }

    @Override
    public void onDestroy() {
        if(prayerTimeUtil!=null)
            prayerTimeUtil.Destroy();
        super.onDestroy();
    }

    @Override
    public void onPause() {
        if(prayerTimeUtil!=null)
            prayerTimeUtil.Destroy();
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        getLocation();
    }

    // TODO: 01.07.2021 Измненить на общее решение
    private String getUserId() {
        SharedPreferences sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getString(Constants.SP_KEY_USER_ID, "0");
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(requireActivity(), viewModelFactory).get(MainViewModel.class);
        viewModel.loadUserProfile(getUserId());
    }

    private void initUserProfileObserve() {
        viewModel.getUserProfileLiveData().observe(getViewLifecycleOwner(), userProfile -> {
            /*for (UserLocation userLocation:userRepository.getCitiesList())
                if(userLocation.getId().equals(userProfile.getCity())){
                    binding.prayerCityTv.setVisibility(View.VISIBLE);
                    binding.prayerCityTv.setText(userLocation.getName());
                    if(userProfile.getLat()<1 || userProfile.getLon()<1){
                        this.userProfile.setLat(userLocation.getLatitude());
                        this.userProfile.setLon(userLocation.getLongitude());

                        if(prayerTimeUtil!=null)
                            prayerTimeUtil.Destroy();


                        if (context!=null){
                            prayerTimeUtil = new PrayerTimeUtil(this.userProfile.getLat(),  this.userProfile.getLon(), prayerListener, context);
                            prayerTimeUtil.namazTime();
                            initPrayerList();
                        }
                    }
                    break;
                }
                else{
                    binding.prayerCityTv.setVisibility(View.INVISIBLE);
                }*/
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            getLocation();
        else if(requestCode == 1)
            Toast.makeText(requireContext(),requireContext().getString(R.string.locus_location_resolution_title) , Toast.LENGTH_SHORT).show();

    }

    private final LocationCallback locationCallback= new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            userProfile.setLat(locationResult.getLocations().get(locationResult.getLocations().size()-1).getLatitude());
            userProfile.setLon(locationResult.getLocations().get(locationResult.getLocations().size()-1).getLongitude());

            if(prayerTimeUtil!=null)
                prayerTimeUtil.Destroy();

            if (context!=null){
                try {
                    Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
                    List<Address> addresses = geocoder.getFromLocation(userProfile.getLat(), userProfile.getLon(), 1);

                    if(!addresses.isEmpty()) {
                        binding.prayerCityTv.setText(addresses.get(0).getLocality());
                        binding.prayerCityTv.setVisibility(View.VISIBLE);
                    }else{
                        binding.prayerCityTv.setVisibility(View.INVISIBLE);
                    }
                } catch (IOException e) {
                    binding.prayerCityTv.setVisibility(View.INVISIBLE);
                }

                prayerTimeUtil = new PrayerTimeUtil(userProfile.getLat(), userProfile.getLon(), prayerListener, context);
                prayerTimeUtil.namazTime();
                initPrayerList();
            }
        }
    };

    private void getLocation(){
        turnGPSOn();
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)
            fusedLocationProviderClient.requestLocationUpdates(new LocationRequest(), locationCallback, Looper.getMainLooper());
        else if(!requestSended){
            ActivityCompat.requestPermissions(requireActivity(),new String[]{ACCESS_FINE_LOCATION},1);
            requestSended=true;
        }else
            Toast.makeText(requireContext(),requireContext().getString(R.string.locus_location_resolution_title) , Toast.LENGTH_SHORT).show();
    }

    private void turnGPSOn(){
        if(!isLocationEnabled() && !flagRespGeo){ //if gps is disabled
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            requireActivity().startActivity(intent);
            flagRespGeo=true;
        }
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
}
