package com.muslimlife.app.view.map;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import org.imaginativeworld.whynotimagecarousel.ImageCarousel;
import org.imaginativeworld.whynotimagecarousel.listener.CarouselListener;
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;
import java.util.ArrayList;
import com.muslimlife.app.R;
import com.muslimlife.app.data.model.PlacesRes;
import com.muslimlife.app.databinding.DialogMapBinding;
import com.muslimlife.app.utils.WorkTimeUtils;

public class MapDialog extends BottomSheetDialogFragment {
    private DialogMapBinding binding;
    private PlacesRes placesRes;
    private LatLng placeLocation;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogMapBinding.inflate(inflater, container, false);



        if (placesRes.description == null || placesRes.description.isEmpty()) {
            binding.descriptionTitle.setVisibility(View.GONE);
            binding.descriptionText.setVisibility(View.GONE);
            binding.descriptionLine.setVisibility(View.GONE);
        } else
            binding.descriptionText.setText(placesRes.description);

        if (placesRes.address == null || placesRes.address.isEmpty()) {
            binding.addressTitle.setVisibility(View.GONE);
            binding.addressText.setVisibility(View.GONE);
            binding.addressLine.setVisibility(View.GONE);
        } else
            binding.addressText.setText(placesRes.address);

        if (placesRes.schedule == null || placesRes.schedule.length < 1) {
            binding.workTimeTitle.setVisibility(View.GONE);
            binding.workTimesText.setVisibility(View.GONE);
            binding.workTimeLine.setVisibility(View.GONE);
        } else
            binding.workTimesText.setText(WorkTimeUtils.getWorkTime(placesRes.schedule,requireContext()));

        if (placesRes.phone == null || placesRes.phone.isEmpty()) {
            binding.phoneTitle.setVisibility(View.GONE);
            binding.phoneText.setVisibility(View.GONE);
            binding.phoneButton.setVisibility(View.GONE);
            binding.phoneLine.setVisibility(View.GONE);
        } else
            binding.phoneText.setText(placesRes.phone);

        if (placesRes.images.length < 1) {
            binding.carousel.setVisibility(View.GONE);
            binding.customIndicator.setVisibility(View.GONE);
        } else {
            ArrayList<CarouselItem> carouselItems = new ArrayList<>();

            for (int i = 0; i < placesRes.images.length; i++)
                carouselItems.add(new CarouselItem(placesRes.images[i]));

            binding.carousel.setShowIndicator(true);
            binding.carousel.setIndicator(binding.customIndicator);
            binding.carousel.setData(carouselItems);
        }

        if (placesRes.instagram == null || placesRes.instagram.isEmpty())
            binding.instagramButton.setVisibility(View.GONE);

        if (placesRes.vk == null || placesRes.vk.isEmpty())
            binding.vkButton.setVisibility(View.GONE);

        if (placesRes.telegram == null || placesRes.telegram.isEmpty())
            binding.telegramButton.setVisibility(View.GONE);

        if (placesRes.whatsapp == null || placesRes.whatsapp.isEmpty())
            binding.whatsAppButton.setVisibility(View.GONE);

        if (binding.instagramButton.getVisibility() == View.GONE && binding.vkButton.getVisibility() == View.GONE &&
                binding.telegramButton.getVisibility() == View.GONE && binding.whatsAppButton.getVisibility() == View.GONE) {
            binding.socialLine.setVisibility(View.GONE);
            binding.socialTitle.setVisibility(View.GONE);
        }

        binding.dialogTitle.setText(placesRes.name);

        binding.dialogClose.setOnClickListener(v -> dismiss());

        binding.phoneButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + placesRes.phone));
            startActivity(intent);
        });

        binding.instagramButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(placesRes.instagram));
            startActivity(intent);
        });

        binding.vkButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(placesRes.vk));
            startActivity(intent);
        });

        binding.telegramButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tg://resolve?domain=" + placesRes.telegram));
            startActivity(intent);
        });

        binding.whatsAppButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/" + placesRes.whatsapp));
            startActivity(intent);
        });

        binding.getDirectionsButton.setOnClickListener(v -> initNavigation(placeLocation));

        binding.carousel.setCarouselListener(new CarouselListener() {
            @Nullable
            @Override
            public ViewBinding onCreateViewHolder(@NonNull LayoutInflater layoutInflater, @NonNull ViewGroup viewGroup) {
                return null;
            }

            @Override
            public void onBindViewHolder(@NonNull ViewBinding viewBinding, @NonNull CarouselItem carouselItem, int i) {

            }

            @Override
            public void onClick(int i, @NonNull CarouselItem carouselItem) {
                dialogImageShow();
            }

            @Override
            public void onLongClick(int i, @NonNull CarouselItem carouselItem) {

            }
        });

        return binding.getRoot();
    }

    public MapDialog(PlacesRes placesRes, LatLng placeLocation){
        this.placesRes=placesRes;
        this.placeLocation=placeLocation;
    }

    private void dialogImageShow() {
        Dialog dialogImage =  new Dialog(requireContext(), R.style.FullScreenDialog);
        dialogImage.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialogImage.setContentView(R.layout.dialog_image_view);

        TextView title = dialogImage.findViewById(R.id.title_rest);
        ImageCarousel carousel = dialogImage.findViewById(R.id.carouselDial);
        ImageButton imageDialogBackButton = dialogImage.findViewById(R.id.backButton);

        ArrayList<CarouselItem> carouselItems = new ArrayList<>();

        for (int i = 0; i < placesRes.images.length; i++)
            carouselItems.add(new CarouselItem(placesRes.images[i]));

        carousel.setData(carouselItems);
        carousel.setShowIndicator(false);
        title.setText(placesRes.name);

        imageDialogBackButton.setOnClickListener(v -> dialogImage.dismiss());

        dialogImage.show();

    }

    private void initNavigation(LatLng placeLocation) {
        Uri google = Uri.parse("google.navigation:q="
                + placeLocation.latitude
                + ","
                + placeLocation.longitude
                + "&mode=g");

        Intent intentGoogle = new Intent(Intent.ACTION_VIEW, google);


        Uri yandex = Uri.parse("yandexnavi://build_route_on_map?lat_to="
                + placeLocation.latitude
                + "&lon_to="
                + placeLocation.longitude);
        Intent intentYandex = new Intent(Intent.ACTION_VIEW, yandex);

        Intent chooser = Intent.createChooser(intentGoogle, ""); // default action
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentYandex);

        startActivity(chooser);
    }


}
