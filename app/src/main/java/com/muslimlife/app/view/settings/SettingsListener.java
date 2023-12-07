package com.muslimlife.app.view.settings;


import static com.muslimlife.app.utils.Constants.NAVIGATION;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.navigation.Navigation;
import com.muslimlife.app.BuildConfig;
import com.muslimlife.app.R;

interface SettingsListener {

    static void rv_click(int position, View view){
        switch (position){
            case 0:
                Bundle bundle = new Bundle();
                bundle.putInt(NAVIGATION, R.id.SettingsFragment);
                Navigation.findNavController(view).navigate(R.id.MessageFragment, bundle);
                break;
            case 1:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        "MuslimLife: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
                sendIntent.setType("text/plain");
                view.getContext().startActivity(sendIntent);
                break;
            case 2:
                //Toast.makeText(view.getContext(), view.getContext().getString(R.string.section_under_development), Toast.LENGTH_SHORT).show();
                Navigation.findNavController(view).navigate(R.id.DonationFragment);
                break;
            case 3:
                Navigation.findNavController(view).navigate(R.id.LanguageFragment);
                break;
            case 4:
                Navigation.findNavController(view).navigate(R.id.ServicesFragment);
                break;
            case 5:
                Navigation.findNavController(view).navigate(R.id.SupportFragment);
                break;
            case 6:
                Navigation.findNavController(view).navigate(R.id.AboutFragment);
                break;
        }
    }
}