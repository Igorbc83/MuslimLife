package com.muslimlife.app.view.compass;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.os.Vibrator;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.Navigation;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.muslimlife.app.utils.Constants;
import com.muslimlife.app.utils.ServicesUtil;
import com.muslimlife.app.view.MainActivity;
import com.muslimlife.app.view.base.BaseFragment;

import com.muslimlife.app.R;
import com.muslimlife.app.databinding.FragmentQiblaBinding;

public class CompassFragment extends BaseFragment {

    private FragmentQiblaBinding binding;

    private Compass compass;
    private float currentAzimuth;
    private float degrees = 0f;
    FusedLocationProviderClient fusedLocationProviderClient;
    boolean getted;
    boolean requestSended = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentQiblaBinding.inflate(inflater, container, false);
        ((MainActivity)requireActivity()).setMenu(Constants.MENU_OFF);

        ServicesUtil.updateScore(requireActivity().getPreferences(Context.MODE_PRIVATE), Constants.SERVICE_COMPASS);

        binding.backButton.setOnClickListener(v->Navigation.findNavController(requireView()).popBackStack());
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext());
        getLocation();

        return binding.getRoot();
    }

    private void initCompass() {
        compass = new Compass(requireContext());
        compass.setListener(this::updateCompass);
        compass.start(requireContext());
    }

    private void calculateDegree(Location userLocation) {
        double myLat = userLocation.getLatitude();
        double myLng = userLocation.getLongitude();

        double kaabaLng = 39.826206;
        double kaabaLat = Math.toRadians(21.422487);

        double myLatRad = Math.toRadians(myLat);
        double longDiff = Math.toRadians(kaabaLng - myLng);

        double y = Math.sin(longDiff) * Math.cos(kaabaLat);
        double x = Math.cos(myLatRad) * Math.sin(kaabaLat) - Math.sin(myLatRad) * Math.cos(kaabaLat) * Math.cos(longDiff);
        degrees = (float) (Math.toDegrees(Math.atan2(y, x)) + 360) % 360;
    }

    public void updateCompass(float azimuth) {
        Animation anKaaba = new RotateAnimation(-(currentAzimuth) + degrees, -azimuth,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);

        currentAzimuth = (azimuth);

        anKaaba.setDuration(500);
        anKaaba.setRepeatCount(0);
        anKaaba.setFillAfter(true);

        if (currentAzimuth+1>=degrees && currentAzimuth-1<=degrees){
            Vibrator vibrator = (Vibrator) requireActivity().getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(500);
        }

        /*Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.compass_big_icon);
        Matrix matrix = new Matrix();
        matrix.postRotate(-azimuth);
        Bitmap rotatedBitmap = Bitmap.createBitmap(originalBitmap, 0, 0, originalBitmap.getWidth(), originalBitmap.getHeight(), matrix, true);*/

        binding.imageArrow.setRotation(-azimuth + (-120));
        binding.ivCompass.startAnimation(anKaaba);
        //binding.imageArrow.setImageBitmap(rotatedBitmap);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (compass != null)
            compass.start(requireContext());

    }

    @Override
    public void onPause() {
        super.onPause();

        if (compass != null)
            compass.stop();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void showError(String error) {
        Log.e("CompassFragment", error);
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
                calculateDegree(locationResult.getLocations().get(locationResult.getLocations().size()-1));
                initCompass();
                getted=true;
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
        if(!isLocationEnabled()){ //if gps is disabled
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            requireActivity().startActivity(intent);
        }
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
}
