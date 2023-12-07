package com.muslimlife.app.view.main;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.Context.MODE_PRIVATE;
import static com.muslimlife.app.utils.Constants.OPEN_AUTH;
import static com.muslimlife.app.utils.Constants.IMAM_ID;
import static com.muslimlife.app.utils.Constants.MENU_MAIN;
import static com.muslimlife.app.utils.Constants.OPEN_AUTH;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.muslimlife.app.adapters.MainAdapter;
import com.muslimlife.app.adapters.RecyclerModel;
import com.muslimlife.app.data.model.DiaspoarResponce;
import com.muslimlife.app.data.model.ImamResMain;
import com.muslimlife.app.data.model.OtherEntity;
import com.muslimlife.app.data.model.PushTime;
import com.muslimlife.app.data.model.PushTimeReq;
import com.muslimlife.app.data.model.QuastionTypeRes;
import com.muslimlife.app.data.model.UserLocation;
import com.muslimlife.app.data.model.UserProfile;
import com.muslimlife.app.data.repository.AzkarsRepository;
import com.muslimlife.app.data.repository.UserRepository;
import com.muslimlife.app.di.viewModel.DaggerViewModelFactory;
import com.muslimlife.app.utils.Constants;
import com.muslimlife.app.utils.PrayerListener;
import com.muslimlife.app.utils.PrayerTimeUtil;
import com.muslimlife.app.utils.ServicesUtil;
import com.muslimlife.app.view.MainActivity;
import com.muslimlife.app.view.main.controls.MainRecyclerControl;
import com.muslimlife.app.view.main.listeners.MainRecyclerListener;
import com.muslimlife.app.viewmodel.main.MainViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import com.muslimlife.app.R;
import com.muslimlife.app.databinding.FragmentMainBinding;

import com.muslimlife.app.view.base.BaseFragment;

public class MainFragment extends BaseFragment implements MainRecyclerListener, PrayerListener {

    private FragmentMainBinding binding;

    MainRecyclerControl mainRecyclerControl;

    PrayerTimeUtil prayerTimeUtil;

    @Inject
    DaggerViewModelFactory viewModelFactory;
    private MainViewModel viewModel;

    @Inject
    UserRepository userRepository;
    @Inject
    AzkarsRepository azkarsRepository;

    UserProfile userProfile;
    FusedLocationProviderClient fusedLocationProviderClient;
    boolean getted;
    PrayerListener prayerListener = this;
    boolean requestSended = false;
    boolean flagRespGeo;

    String mainRecyclerState = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(inflater, container, false);
        userProfile = new UserProfile();
        initViewModel();
        initUserProfileObserve();

        ((MainActivity)requireActivity()).setMenu(Constants.MENU_MAIN);
        ((MainActivity) requireActivity()).checkUser();

        String savedString = requireActivity().getSharedPreferences("muslimlife.Services",MODE_PRIVATE).getString("muslimlife.Services", "");

        String[] cc = savedString.split(",");
        if(savedString.split(",").length == 11) {
            savedString += "true,";
            requireActivity().getSharedPreferences("muslimlife.Services",MODE_PRIVATE)
                    .edit()
                    .putString("muslimlife.Services", savedString)
                    .apply();
        }

        StringTokenizer st = new StringTokenizer(savedString, ",");

        mainRecyclerControl = new MainRecyclerControl(this, userRepository, azkarsRepository, ServicesUtil.getServicesActiveWithKey(st));

        binding.mainSettingsButton.setOnClickListener(v -> Navigation.findNavController(requireView()).navigate(R.id.SettingsFragment));

        binding.mainRadio.setOnClickListener(v-> Navigation.findNavController(requireView()).navigate(R.id.RadioFragment));
        binding.mainTvButton.setOnClickListener(v-> Navigation.findNavController(requireView()).navigate(R.id.TvFragment));

        binding.offServiceView.dialogServiceSetting.setOnClickListener(v ->
                Navigation.findNavController(requireView()).navigate(R.id.ServicesFragment));

        List<OtherEntity> otherEntities = ServicesUtil.getMostUsedServices(requireActivity().getPreferences(Context.MODE_PRIVATE));

        binding.testFavorite1.getRoot().setVisibility(View.GONE);
        binding.testFavorite2.getRoot().setVisibility(View.GONE);
        binding.testFavorite3.getRoot().setVisibility(View.GONE);

        if(otherEntities.size() > 0) {
            binding.testFavorite1.getRoot().setVisibility(View.VISIBLE);
            binding.testFavorite1.text.setText(getString(otherEntities.get(0).getText()));
            binding.testFavorite1.image.setImageDrawable(ResourcesCompat.getDrawable(getResources(), otherEntities.get(0).getDrawable(), null));
            binding.testFavorite1.bg.setCardBackgroundColor(requireContext().getColor(R.color.light_white));
            binding.testFavorite1.sermonsItem.setOnClickListener(v -> selectWorship(otherEntities.get(0).getType()));
        }

        if(otherEntities.size() > 1) {
            binding.testFavorite2.getRoot().setVisibility(View.VISIBLE);
            binding.testFavorite2.text.setText(otherEntities.get(1).getText());
            binding.testFavorite2.image.setImageDrawable(ResourcesCompat.getDrawable(getResources(), otherEntities.get(1).getDrawable(), null));
            binding.testFavorite2.bg.setCardBackgroundColor(requireContext().getColor(R.color.light_white));
            binding.testFavorite2.sermonsItem.setOnClickListener(v -> selectWorship(otherEntities.get(1).getType()));
        }

        if(otherEntities.size() > 2) {
            binding.testFavorite3.getRoot().setVisibility(View.VISIBLE);
            binding.testFavorite3.text.setText(otherEntities.get(2).getText());
            binding.testFavorite3.image.setImageDrawable(ResourcesCompat.getDrawable(getResources(), otherEntities.get(2).getDrawable(), null));
            binding.testFavorite3.bg.setCardBackgroundColor(requireContext().getColor(R.color.light_white));
            binding.testFavorite3.sermonsItem.setOnClickListener(v -> selectWorship(otherEntities.get(2).getType()));
        }


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext());
        getLocation();

        /*if(userRepository.isNotifReaded() || userRepository.isMessageReaded())
            binding.mainSettingsButton.setImageDrawable(requireContext().getDrawable(R.drawable.ic_settings_notification));
        else
            binding.mainSettingsButton.setImageDrawable(requireContext().getDrawable(R.drawable.ic_settings));*/

        getQuastionType();

        return binding.getRoot();
    }

    Observer<Boolean> connectionObserver = (Observer<Boolean>) this::showInternetState;

    private void initMainRecycler(){
        mainRecyclerState = "";
        //showInternetState(((MainActivity) requireActivity()).isConnected());
    }

    private void showInternetState(boolean isConnected){
        if(isConnected){

            if(!mainRecyclerState.equals("full"))
                mainRecyclerControl.getMainModels();

            binding.offInternetView.view.setVisibility(View.GONE);
            binding.offInternetView.update.setOnClickListener(view -> {
                if(userRepository.getImamResMains() == null || userRepository.getImamResMains().length == 0)
                    getImams();
                else
                    mainRecyclerControl.getMainModels();
            });

            mainRecyclerState = "full";
        }else{
            if(!mainRecyclerState.equals("internet"))
                mainRecyclerControl.getMainModelsWithNoInternet();
            binding.offInternetView.view.setVisibility(View.VISIBLE);

            mainRecyclerState = "internet";
        }
    }

    private void getImams() {
        userRepository.getImams(requireActivity().getPreferences(Context.MODE_PRIVATE).getString(Constants.SP_KEY_USER_ID, "0")).subscribe(new SingleObserver<>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onSuccess(@NonNull ImamResMain[] imamResMains) {
                userRepository.setImamResMains(imamResMains);
                mainRecyclerControl.getMainModels();
            }

            @Override
            public void onError(@NonNull Throwable e) {
            }
        });
    }

    @Override
    public void selectBarakat() {
        Navigation.findNavController(requireView()).navigate(R.id.DonationFragment);
    }

    private void initMainAlarm() {
        if(prayerTimeUtil!=null)
            prayerTimeUtil.Destroy();
        prayerTimeUtil = new PrayerTimeUtil(userProfile.getLat(), userProfile.getLon(), prayerListener, requireContext());
        prayerTimeUtil.namazTime();

        try {
            Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(userProfile.getLat(), userProfile.getLon(), 1);

            if(!addresses.isEmpty()) {
                binding.cityName.setText(addresses.get(0).getLocality());
                binding.cityName.setVisibility(View.VISIBLE);
            }else{
                binding.cityName.setVisibility(View.INVISIBLE);
            }
        } catch (IOException e) {
            binding.cityName.setVisibility(View.INVISIBLE);
        }
        //((MainActivity) requireActivity()).setAlarmDay(prayerTimeUtil);
    }

    private void initUserProfileObserve() {
        viewModel.getUserProfileLiveData().observe(getViewLifecycleOwner(), userProfile -> {
            this.userProfile = userProfile;
            SharedPreferences.Editor editor = requireActivity().getPreferences(Context.MODE_PRIVATE).edit();
            if (userProfile.getNoti_sound()!=null) editor.putInt(Constants.SELECTED_PUSH, Constants.positionSound(userProfile.getNoti_sound())).apply();
            editor.putBoolean(Constants.IS_NOTIFICATION_RECEIVE, userProfile.isPush_receive()).apply();
            /*if(userRepository.getCitiesList()!=null)
                for (UserLocation userLocation:userRepository.getCitiesList())
                    if(userLocation.getId().equals(userProfile.getCity())){
                        binding.cityName.setVisibility(View.VISIBLE);
                        binding.cityName.setText(userLocation.getName());
                        if(userProfile.getLat()<1 || userProfile.getLon()<1){
                            this.userProfile.setLat(userLocation.getLatitude());
                            this.userProfile.setLon(userLocation.getLongitude());
                            initMainAlarm();
                        }
                        break;
                    }
                    else{
                        binding.cityName.setVisibility(View.INVISIBLE);
                    }*/
        });
    }

    @Override
    public void launchBottomDialog(String type) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.MAIN_BOTTOM_DIALOG_TYPE, type);
        Navigation.findNavController(requireView()).navigate(R.id.bottomWorshipDialog, bundle);
    }

    @Override
    public void initMainRecycler(List<RecyclerModel> models) {
        if(models.size() == 2)
            binding.offServiceView.view.setVisibility(View.VISIBLE);

        initRecycler(binding.mainRecycler, models);
    }

    private void initRecycler(RecyclerView recyclerView, List<RecyclerModel> models){
        LinearLayoutManager manager = new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false);
        MainAdapter adapter = new MainAdapter(models, this);

        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void selectWorship(int type) {
        if(prayerTimeUtil!=null)
            prayerTimeUtil.Destroy();
        Navigation.findNavController(requireView()).navigate(type);
    }

    @Override
    public void selectMap(int type) {
        Bundle arg = new Bundle();
        arg.putInt("filterMap",type - 1);
        Navigation.findNavController(requireView()).navigate(R.id.mapGPlacesFragment, arg);
    }

    @Override
    public void selectImam(ImamResMain imamResMain) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.IMAM_ID, imamResMain.getId());
        Navigation.findNavController(requireView()).navigate(R.id.ImamFragment, bundle);
    }

    @Override
    public void selectDiaspoar(DiaspoarResponce diaspoarResponce){
        userRepository.setDiaspoarResponce(diaspoarResponce);
        Navigation.findNavController(requireView()).navigate(R.id.DiasporFragment);
    }

    @Override
    public void Timer(String toPrayer,String toPrayerNamaz) {
        try{
            requireActivity().runOnUiThread(() -> binding.timeToPray.setText(toPrayer+" "+requireContext().getString(R.string.mints)));
        }catch (Exception e){
            Log.e("TimerError:",e.getMessage());
        }
    }

    @Override
    public void Name(int name) {requireActivity().runOnUiThread(()->binding.prayerText.setText(requireContext().getString(name)));}

    @Override
    public void Time(String time) {
        binding.timeText.setText(time);
    }

    @Override
    public void pushTime(PushTime pushTime) {
        if(!getUserId().equals("0"))
            userRepository.pushTimeUpdate(new PushTimeReq(userProfile.getId(), pushTime)).subscribe(new SingleObserver<Boolean>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onSuccess(Boolean aBoolean) {
                    aBoolean.toString();
                }

                @Override
                public void onError(Throwable e) {
                    Log.e("asd",e.getMessage());
                }
            });
    }

    @Override
    public void onPause() {
        if(prayerTimeUtil!=null)
            prayerTimeUtil.Destroy();
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        ((MainActivity) requireActivity()).getIsConnectedData().removeObserver(connectionObserver);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if(prayerTimeUtil!=null)
            prayerTimeUtil.Destroy();
        ((MainActivity) requireActivity()).getIsConnectedData().removeObserver(connectionObserver);
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        getLocation();

        initMainRecycler();

        ((MainActivity) requireActivity()).getIsConnectedData().observe(getViewLifecycleOwner(), connectionObserver);
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(requireActivity(), viewModelFactory).get(MainViewModel.class);
        viewModel.loadUserProfile(getUserId());
    }

    private String getUserId() {
        SharedPreferences sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getString(Constants.SP_KEY_USER_ID, "0");
    }

    @Override
    public void showError(String error) {
        Log.e("MainFragmentError:",error);
    }

    public void getQuastionType(){
        userRepository.getQuastionType().subscribe(new SingleObserver<>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onSuccess(@NonNull QuastionTypeRes[] quastionTypeRes) {
                userRepository.setQuastionTypeRes(quastionTypeRes);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.e("getQuastionType", e.getMessage());
            }
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
            if(!getted){
                if (userProfile==null)
                    userProfile = new UserProfile();
                userProfile.setLat(locationResult.getLocations().get(locationResult.getLocations().size()-1).getLatitude());
                userProfile.setLon(locationResult.getLocations().get(locationResult.getLocations().size()-1).getLongitude());

                initMainAlarm();
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

    @Override
    public void selectAzkars(int type) {
        Bundle arg = new Bundle();
        arg.putInt(Constants.BUNDLE_AZKAR ,type);
        Navigation.findNavController(requireView()).navigate(R.id.azkarListFragment, arg);
    }

    @Override
    public void selectMigrants() {
        Navigation.findNavController(requireView()).navigate(R.id.migrantListFragment);
    }

    @Override
    public void selectChatAI() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser == null || firebaseUser.isAnonymous()){
            Bundle bundle = new Bundle();
            bundle.putBoolean(OPEN_AUTH, false);
            Navigation.findNavController(requireView()).navigate(R.id.newAuthFragment, bundle);
        }else {
            Navigation.findNavController(requireView()).navigate(R.id.chatAIFragment);
        }
    }
}
