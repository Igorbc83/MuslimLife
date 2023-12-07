package com.muslimlife.app.view.map;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.maps.android.clustering.ClusterManager;
import com.muslimlife.app.data.model.MapClusterItem;
import com.muslimlife.app.data.model.PlacesRes;
import com.muslimlife.app.utils.Constants;
import com.muslimlife.app.view.MainActivity;

import java.util.ArrayList;
import java.util.Arrays;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import com.muslimlife.app.R;

import com.muslimlife.app.data.repository.UserRepository;
import com.muslimlife.app.databinding.FragmentMapBinding;

import com.muslimlife.app.view.base.BaseFragment;

public class MapFragment extends BaseFragment implements MapView, OnMapReadyCallback, GoogleMap.OnMyLocationChangeListener,
        GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnCameraMoveListener {

    @Inject
    UserRepository userRepository;

    private FragmentMapBinding binding;
    GoogleMap map;
    Location userLocation;
    CameraPosition cameraPosition;
    float currentZoom = 15;
    boolean firstOpen = true;

    String[] filter;

    BottomSheetDialog dialogFull;

    BottomSheetDialog dialogSmall;

    private ClusterManager<MapClusterItem> clusterManager;

    Dialog dialogSearch;

    MapSearchAdapter adapter;

    PlacesRes[] placesRes;

    MapAdapter adapterFilter;

    Marker userLocationMarker;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMapBinding.inflate(inflater, container, false);
        ((MainActivity) requireActivity()).setMenu(Constants.MENU_MAP);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager()
                        .findFragmentById(R.id.map);


        if (mapFragment != null)
            mapFragment.getMapAsync(this);


        binding.currentLocation.setOnClickListener(onClickListener);
        binding.plusButton.setOnClickListener(onClickListener);
        binding.minusButton.setOnClickListener(onClickListener);
        binding.backButton.setOnClickListener(onClickListener);
        binding.searchBar.searchEdit.setKeyListener(null);
        binding.searchBar.searchEdit.setOnClickListener(onClickListener);
        binding.backButton.setOnClickListener(onClickListener);

        filter = new String[]{requireContext().getString(R.string.all),
                requireContext().getString(R.string.eatPoint),
                requireContext().getString(R.string.pointChurch),
                requireContext().getString(R.string.pointCafe),
                requireContext().getString(R.string.pointRestaurants),
                requireContext().getString(R.string.pointHalal)};

        adapterFilter = new MapAdapter(filter, this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.mapRv.setLayoutManager(layoutManager);
        binding.mapRv.setAdapter(adapterFilter);

        dialogFull = new BottomSheetDialog(requireContext());
        dialogSmall = new BottomSheetDialog(requireContext());
        dialogSearch = new Dialog(requireContext(), R.style.FullScreenDialog);

        return binding.getRoot();
    }

    View.OnClickListener onClickListener = v -> {
        switch (v.getId()) {
            case R.id.backButton:
                Navigation.findNavController(requireView()).popBackStack();
                break;
            case R.id.plusButton:
                currentZoom++;
                if (cameraPosition != null)
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(cameraPosition.target, currentZoom));
                break;
            case R.id.minusButton:
                currentZoom--;
                if (cameraPosition != null)
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(cameraPosition.target, currentZoom));
                break;
            case R.id.currentLocation:
                onMyLocationButtonClick();
                break;
            case R.id.search_edit:
                dialogSearchShow();
                break;
        }
    };


    @Override
    public void changeFilter(int position) {
        if(placesRes != null && placesRes.length > 0) {
            ArrayList<PlacesRes> placesResArrayList = new ArrayList<>(Arrays.asList(placesRes));
            switch (position) {
                case 0:
                    break;
                case 1:
                    placesResArrayList.removeIf(obj -> (!obj.type.equals("2") && !obj.type.equals("3")));
                    break;
                case 2:
                    placesResArrayList.removeIf(obj -> (!obj.type.equals("4") && !obj.type.equals("5")));
                    break;
                case 3:
                    placesResArrayList.removeIf(obj -> !obj.type.equals("2"));
                    break;
                case 4:
                    placesResArrayList.removeIf(obj -> !obj.type.equals("3"));
                    break;
                case 5:
                    placesResArrayList.removeIf(obj -> !obj.type.equals("1"));
                    break;
            }
            compilePlaces(placesResArrayList.toArray(new PlacesRes[0]));
        }
    }

    @Override
    public void compilePlaces(PlacesRes[] placesRes) {
        clusterManager.clearItems();
        for (PlacesRes placesRe : placesRes) {
            if(placesRe.lat == null || placesRe.lat.isEmpty() ||
                    placesRe.lon == null || placesRe.lon.isEmpty())
                continue;

            LatLng placeLocation = new LatLng(Double.parseDouble(placesRe.lat), Double.parseDouble(placesRe.lon));
            BitmapDescriptor icon = null;
            switch (placesRe.type) {
                case "1":
                    icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_small_shop_new);
                    break;
                case "2":
                case "3":
                    icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_small_cafe_rest_new);
                    break;
                case "4":
                case "5":
                    icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_small_church_new);
                    break;
            }


            MapClusterItem clusterItem = new MapClusterItem(placeLocation, "", "", icon, requireContext(), map, clusterManager, placesRe);
            clusterItem.setMinClusterSize(1000);

            clusterManager.addItem(clusterItem);
            clusterManager.setRenderer(clusterItem);
            clusterManager.setOnClusterItemClickListener(item -> {
                item.setChange(true);
                clickCluster(item.getPlacesRes(), item);
                return true;
            });
        }
    }

    private void dialogSearchShow() {
        dialogSearch.setContentView(R.layout.dialog_map_search);

        RecyclerView searchRv = dialogSearch.findViewById(R.id.searchRv);
        EditText searchEdit = dialogSearch.findViewById(R.id.search_edit);
        TextView cancel = dialogSearch.findViewById(R.id.cancel);
        ImageButton backButton = dialogSearch.findViewById(R.id.backButton);

        cancel.setOnClickListener(v -> searchEdit.setText(""));
        backButton.setOnClickListener(v -> dialogSearch.dismiss());

        adapter = new MapSearchAdapter(placesRes, this, searchEdit);
        LinearLayoutManager llmOn = new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false);
        searchRv.setLayoutManager(llmOn);
        searchRv.setAdapter(adapter);

        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0)
                    cancel.setVisibility(View.VISIBLE);
                else
                    cancel.setVisibility(View.GONE);

                adapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        dialogSearch.show();
    }

    private void changeIcon(PlacesRes placesRes, MapClusterItem item) {
        switch (placesRes.type) {
            case "1":
                item.getMarker().setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_shop_new));
                break;
            case "2":
            case "3":
                item.getMarker().setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_cafe_rest_new));
                break;
            case "4":
            case "5":
                item.getMarker().setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_church_new));
                break;
        }
    }


    @Override
    public void clickCluster(PlacesRes placesRes, MapClusterItem item) {
        currentZoom = 18;
        LatLng itemLocation = new LatLng(Double.parseDouble(placesRes.lat), Double.parseDouble(placesRes.lon));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(itemLocation, currentZoom));

        MapDialog mapDialog = new MapDialog(placesRes, itemLocation);
        mapDialog.show(requireActivity().getSupportFragmentManager(), "MapDialog");

        if (item.getMarker() != null)
            changeIcon(placesRes, item);
    }

    @Override
    public void choosePlace(PlacesRes placesRes) {
        dialogSearch.dismiss();
        LatLng placeLocation = new LatLng(Double.parseDouble(placesRes.lat), Double.parseDouble(placesRes.lon));
        BitmapDescriptor icon = null;
        switch (placesRes.type) {
            case "1":
                icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_shop_new);
                break;
            case "2":
            case "3":
                icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_cafe_rest_new);
                break;
            case "4":
            case "5":
                icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_church_new);
                break;
        }
        MapClusterItem mapClusterItem = new MapClusterItem(placeLocation, "", "", icon, requireContext(), map, clusterManager, placesRes);
        clickCluster(placesRes, mapClusterItem);
    }

    private void getLocationPermission() {
        if (!(ContextCompat.checkSelfPermission(requireContext().getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED))
            ActivityCompat.requestPermissions(requireActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;

        clusterManager = new ClusterManager<>(requireContext(), map);

        map.setOnCameraIdleListener(clusterManager);
        map.setOnMarkerClickListener(clusterManager);


        getLocationPermission();
        getPlaces();
        if (ContextCompat.checkSelfPermission(requireContext().getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
            map.setOnMyLocationChangeListener(this);
            map.setOnMyLocationButtonClickListener(this);
            map.setOnCameraMoveListener(this);

        } else {
            Toast.makeText(requireContext(), requireContext().getString(R.string.locus_location_resolution_message), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMyLocationChange(@NonNull Location location) {
        userLocation = location;
        if (firstOpen)
            onMyLocationButtonClick();
        firstOpen = false;
        userLocationMarker.setPosition(new LatLng(userLocation.getLatitude(), userLocation.getLongitude()));
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            map.setMyLocationEnabled(false);
    }

    @Override
    public boolean onMyLocationButtonClick() {
        if (firstOpen && userLocation!=null){
            userLocationMarker = map.addMarker(new MarkerOptions()
                    .position(new LatLng(userLocation.getLatitude(), userLocation.getLongitude()))
                    .draggable(true)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location_new)));

            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(userLocation.getLatitude(),userLocation.getLongitude()),currentZoom));
        }else if(userLocation!=null)
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(userLocation.getLatitude(),userLocation.getLongitude()),15));
        return false;
    }

    @Override
    public void onCameraMove() {
        cameraPosition=map.getCameraPosition();
    }

    @Override
    public void showError(String error) {
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
    }

    void getPlaces(){
        if(userRepository!=null && userRepository.getPlacesRes()!=null && userRepository.getPlacesRes().length>0){
            placesRes = userRepository.getPlacesRes();

            changeFilter(requireArguments().getInt("filterMap"));
            adapterFilter.currentPosition=requireArguments().getInt("filterMap");
            adapterFilter.notifyDataSetChanged();
        }else
            userRepository.getPlaces().subscribe(new SingleObserver<>() {
                @Override
                public void onSubscribe(@NonNull Disposable d) {
                }

                @Override
                public void onSuccess(@NonNull PlacesRes[] placesResp) {
                    placesRes = placesResp;

                    changeFilter(getArguments().getInt("filterMap"));
                    adapterFilter.currentPosition = getArguments().getInt("filterMap");
                    adapterFilter.notifyDataSetChanged();
                }

                @Override
                public void onError(@NonNull Throwable e) {
                    Toast.makeText(requireActivity(), requireContext().getString(R.string.markerError), Toast.LENGTH_SHORT).show();
                }
            });
    }



}
