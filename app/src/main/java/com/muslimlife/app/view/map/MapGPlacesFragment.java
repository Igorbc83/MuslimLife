package com.muslimlife.app.view.map;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Bundle;
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
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.muslimlife.app.data.model.MapClusterItem;
import com.muslimlife.app.data.model.PlacesRes;
import com.muslimlife.app.data.model.gplaces.GPlaceDetail;
import com.muslimlife.app.data.model.gplaces.GPlaceDetailPhoto;
import com.muslimlife.app.data.model.gplaces.GPlaceDetailResult;
import com.muslimlife.app.data.model.gplaces.GPlaces;
import com.muslimlife.app.data.model.gplaces.GPlacesResult;
import com.muslimlife.app.data.repository.GPlacesRepository;
import com.muslimlife.app.data.repository.UserRepository;
import com.muslimlife.app.utils.Constants;
import com.muslimlife.app.view.MainActivity;
import com.muslimlife.app.view.map.models.MapFilterModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import com.muslimlife.app.R;
import com.muslimlife.app.databinding.FragmentMapBinding;

import com.muslimlife.app.view.base.BaseFragment;

public class MapGPlacesFragment extends BaseFragment implements MapView, OnMapReadyCallback, GoogleMap.OnMyLocationChangeListener,
        GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnCameraMoveListener, GoogleMap.OnMarkerClickListener {

    @Inject
    UserRepository userRepository;

    @Inject
    GPlacesRepository gPlacesRepository;

    private FragmentMapBinding binding;

    GoogleMap map;
    Location userLocation;
    CameraPosition cameraPosition;
    float currentZoom = 13;
    boolean firstOpen = true;

    List<MapFilterModel> filter;

    BottomSheetDialog dialogFull;

    BottomSheetDialog dialogSmall;

    Dialog dialogSearch;

    MapSearchAdapter adapter;

    List<PlacesRes> placesRes;

    MapFilterAdapter adapterFilter;

    Marker userLocationMarker;

    SupportMapFragment mapFragment;

    private MapFilterModel currentFilter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMapBinding.inflate(inflater, container, false);
        ((MainActivity) requireActivity()).setMenu(Constants.MENU_MAP);

        mapFragment = (SupportMapFragment) getChildFragmentManager()
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

        filter = new ArrayList<>();
        //filter.add(new MapFilterModel(getString(R.string.all), null, ""));
        //filter.add(new MapFilterModel(getString(R.string.eatPoint), PlaceTypes.TYPE_EAT, ""));
        filter.add(new MapFilterModel(getString(R.string.pointChurch), PlaceTypes.TYPE_MOSQUES, "mosque"));
        filter.add(new MapFilterModel(getString(R.string.pointCafe), PlaceTypes.TYPE_CAFE, "cafe"));
        filter.add(new MapFilterModel(getString(R.string.pointRestaurants), PlaceTypes.TYPE_RESTAURANT, "restaurant"));
        filter.add(new MapFilterModel(getString(R.string.halalProduct), PlaceTypes.TYPE_HALAL, "supermarket|store|shopping_mall|convenience_store"));

        currentFilter = filter.get(requireArguments().getInt("filterMap"));

        adapterFilter = new MapFilterAdapter(filter, this, requireArguments().getInt("filterMap"));

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.mapRv.setLayoutManager(layoutManager);
        binding.mapRv.setAdapter(adapterFilter);

        dialogFull = new BottomSheetDialog(requireContext());
        dialogSmall = new BottomSheetDialog(requireContext());
        dialogSearch = new Dialog(requireContext(), R.style.FullScreenDialog);

        binding.loadPlacesBtn.setOnClickListener(view -> {
            getPlaces(getCenterMap(), getCurrentMapRadius());
            binding.loadPlacesBtn.setVisibility(View.GONE);
        });

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
    public void changeFilter(MapFilterModel filter) {
        currentFilter = filter;
        //compilePlaces(placesResArrayList.toArray(new PlacesRes[0]));//todo remove for test
        getPlaces(getCenterMap(), getCurrentMapRadius());
    }

    @Override
    public void clickCluster(PlacesRes placesRes, MapClusterItem item) {}

    private void showPlaces() {
        try {
            requireActivity().runOnUiThread(() -> mapFragment.getMapAsync(googleMap -> {
                googleMap.clear();
                for (PlacesRes place : placesRes) {
                    int height = 90;
                    int width = 90;
                    BitmapDrawable bitmapdraw = (BitmapDrawable)getResources().getDrawable(getIconByType());
                    Bitmap b = bitmapdraw.getBitmap();
                    Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

                    googleMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(Double.parseDouble(place.lat), Double.parseDouble(place.lon)))
                                    .draggable(true)
                                    .icon(BitmapDescriptorFactory.fromBitmap(smallMarker)))
                            .setTag(place);
                }

                showMyMarker();
            /*googleMap.addMarker(new MarkerOptions().position(map.getCameraPosition().target));

            googleMap.addCircle(new CircleOptions()
                    .center(googleMap.getCameraPosition().target)
                    .radius(getCurrentMapRadius()));*/
            }));
        }catch (Exception ignored){}
    }

    private int getIconByType(){
        switch (currentFilter.getType()){
            case TYPE_MOSQUES:
                return R.drawable.ic_small_church_new;
            case TYPE_CAFE:
            case TYPE_EAT:
            case TYPE_RESTAURANT:
                return R.drawable.ic_small_cafe_rest_new;
            default:
                return R.drawable.ic_small_shop_new;
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

        /*adapter = new MapSearchAdapter(placesRes, this, searchEdit);
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
        });*/

        dialogSearch.show();
    }

   /* @Override
    public void clickCluster(PlacesRes placesRes, MapClusterItem item) {
        currentZoom = 18;
        LatLng itemLocation = new LatLng(Double.parseDouble(placesRes.lat), Double.parseDouble(placesRes.lon));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(itemLocation, currentZoom));

        MapDialog mapDialog = new MapDialog(placesRes, itemLocation);
        mapDialog.show(requireActivity().getSupportFragmentManager(), "MapDialog");

        if (item.getMarker() != null)
            changeIcon(placesRes, item);
    }*/

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
    }

    private void getLocationPermission() {
        if (!(ContextCompat.checkSelfPermission(requireContext().getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED))
            ActivityCompat.requestPermissions(requireActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;

        map.setOnMarkerClickListener(this);


        getLocationPermission();
        //getPlaces();
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
        if (firstOpen) {
            onMyLocationButtonClick();
            getPlaces(location.getLatitude() + "," + location.getLongitude(), 3000);
        }
        firstOpen = false;
        userLocationMarker.setPosition(new LatLng(userLocation.getLatitude(), userLocation.getLongitude()));
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            map.setMyLocationEnabled(false);
    }

    @Override
    public boolean onMyLocationButtonClick() {
        if (firstOpen && userLocation!=null){
            showMyMarker();

            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(userLocation.getLatitude(),userLocation.getLongitude()),currentZoom));
        }else if(userLocation!=null)
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(userLocation.getLatitude(),userLocation.getLongitude()),15));
        return false;
    }

    private void showMyMarker(){
        try {
            userLocationMarker = map.addMarker(new MarkerOptions()
                    .position(new LatLng(userLocation.getLatitude(), userLocation.getLongitude()))
                    .draggable(true)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location_new)));
        }catch (Exception ignored){}
    }

    @Override
    public void onCameraMove() {
        cameraPosition=map.getCameraPosition();
        binding.loadPlacesBtn.setVisibility(View.VISIBLE);
    }

    @Override
    public void showError(String error) {
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
    }

    void getPlaces(String location, int radius){
        if(userRepository!=null && userRepository.getMapPlaces()!=null &&
                userRepository.getMapPlaces().size() > 0 &&
                userRepository.getMapPlaces().get(currentFilter.getType()) != null){
            placesRes = userRepository.getMapPlaces().get(currentFilter.getType());
            showPlaces();
        }else
            gPlacesRepository.getPlaces(
                    currentFilter.getName(),
                    location,
                    radius,
                    currentFilter.getTypeStr()
            ).subscribe(new SingleObserver<>() {
                @Override
                public void onSubscribe(@NonNull Disposable d) {
                }

                @Override
                public void onSuccess(@NonNull GPlacesResult gPlacesResult) {

                    placesRes = new ArrayList<>();
                    for(GPlaces place : gPlacesResult.getResults()){
                        PlacesRes p = new PlacesRes();

                        p.id = place.getId();
                        p.lat = String.valueOf(place.getGeometry().getLocation().getLat());
                        p.lon = String.valueOf(place.getGeometry().getLocation().getLng());
                        p.name = place.getName();
                        p.address = place.getVicinity();
                        p.type = "5";

                        placesRes.add(p);
                    }

                    //userRepository.getMapPlaces().put(currentFilter.getType(), placesRes);

                    showPlaces();
                }

                @Override
                public void onError(@NonNull Throwable e) {
                    //Toast.makeText(requireActivity(), requireContext().getString(R.string.markerError), Toast.LENGTH_SHORT).show();
                }
            });
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        PlacesRes place = (PlacesRes) marker.getTag();

        gPlacesRepository.getPlaceDetail(
                place.id
        ).subscribe(new SingleObserver<>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
            }

            @Override
            public void onSuccess(@NonNull GPlaceDetailResult gPlaceDetailResult) {
                GPlaceDetail gPlace = gPlaceDetailResult.getResult();

                if(gPlace.getFormattedPhone() != null && !gPlace.getFormattedPhone().isEmpty())
                    place.phone = gPlace.getFormattedPhone();
                else if(gPlace.getInternationalPhone() != null && !gPlace.getInternationalPhone().isEmpty())
                    place.phone = gPlace.getInternationalPhone();

                if(gPlace.getOpeningHours() != null)
                    place.schedule = gPlace.getOpeningHours().getWeekdayText();

                place.images = getPlaceImages(gPlace.getPhotos());

                MapGPlaceDialog mapDialog = new MapGPlaceDialog(place,
                        new LatLng(Double.parseDouble(place.lat), Double.parseDouble(place.lon)));
                mapDialog.show(requireActivity().getSupportFragmentManager(), "MapDialog");
            }

            @Override
            public void onError(@NonNull Throwable e) {
                 Toast.makeText(requireActivity(), requireContext().getString(R.string.markerError), Toast.LENGTH_SHORT).show();
            }
        });
        return false;
    }

    private String[] getPlaceImages(List<GPlaceDetailPhoto> photos){
        String url = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&key=AIzaSyCY86gWeztSiacWZr9tc2D2zU3ui49BWqM&photo_reference=";
        if(photos != null && !photos.isEmpty()){
            String[] images = new String[Math.min(photos.size(), 3)];

            for(int i = 0; i < images.length; i++){
                images[i] = url + photos.get(i).getPhotoReference();
            }

            return images;
        }

        return null;
    }

    private int getCurrentMapRadius(){
        try {
            LatLngBounds bounds = map.getProjection().getVisibleRegion().latLngBounds;
            double llNeLat = bounds.northeast.latitude;
            double llSwLat = bounds.southwest.latitude;
            double llNeLng = bounds.northeast.longitude;
            double llSwLng = bounds.southwest.longitude;
            float results[] = new float[5];
            Location.distanceBetween(llNeLat, llNeLng, llSwLat, llSwLng, results);
            return (int) (results[0] / 4);
        }catch (Exception ignored){
            return 15000;
        }
    }

    private String getCenterMap(){
        try {
            LatLng pos = map.getCameraPosition().target;
            return pos.latitude + "," + pos.longitude;
        }catch (Exception ignored){
            return "55.742038,37.623680";
        }
    }
}
