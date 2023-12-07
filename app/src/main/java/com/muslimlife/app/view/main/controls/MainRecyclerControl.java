package com.muslimlife.app.view.main.controls;

import com.muslimlife.app.data.model.DiaspoarResponce;
import com.muslimlife.app.data.model.ImamResMain;
import com.muslimlife.app.data.model.OtherEntity;
import com.muslimlife.app.data.model.azkars.AzkarsRes;
import com.muslimlife.app.data.repository.AzkarsRepository;
import com.muslimlife.app.data.repository.UserRepository;
import com.muslimlife.app.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.muslimlife.app.R;
import com.muslimlife.app.adapters.RecyclerModel;
import com.muslimlife.app.view.main.listeners.MainRecyclerListener;

public class MainRecyclerControl {

    MainRecyclerListener listener;
    UserRepository userRepository;
    AzkarsRepository azkarsRepository;
    Map<String, Boolean> servicesActive;

    public MainRecyclerControl(MainRecyclerListener listener, UserRepository userRepository, AzkarsRepository azkarsRepository, Map<String, Boolean> servicesActive) {
        this.listener = listener;
        this.userRepository = userRepository;
        this.azkarsRepository = azkarsRepository;
        this.servicesActive = servicesActive;
    }

    //todo remove test data
    public void getMainModels(){
        List<RecyclerModel> items = new ArrayList<>();

        items.add(new RecyclerModel(Constants.RECYCLER_MAIN_BARAKAT_ITEM, new ArrayList<>()));
        items.add(new RecyclerModel(Constants.RECYCLER_SPACE_12_ITEM));

        List<RecyclerModel> worships = getWorshipModels();
        if(worships.size() > 2) {
            items.add(new RecyclerModel(Constants.RECYCLER_MAIN_WORSHIP_ITEM, worships));
            items.add(new RecyclerModel(Constants.RECYCLER_SPACE_12_ITEM));
        }

        //todo test hide azkars
        List<RecyclerModel> azkars = getAzkarModels();
        if(azkars.size() > 2) {
            items.add(new RecyclerModel(Constants.RECYCLER_MAIN_AZKARS_ITEM, azkars));
            items.add(new RecyclerModel(Constants.RECYCLER_SPACE_12_ITEM));
        }

        if(servicesActive.getOrDefault(Constants.SERVICE_QUESTION, true)) {
            items.add(new RecyclerModel(Constants.RECYCLER_MAIN_CHAT_AI_ITEM, new ArrayList<>()));
            items.add(new RecyclerModel(Constants.RECYCLER_SPACE_12_ITEM));
        }

        if(servicesActive.getOrDefault(Constants.SERVICE_MIRGANTS, true)) {
            items.add(new RecyclerModel(Constants.RECYCLER_MAIN_MIGRANTS_ITEM, new ArrayList<>()));
            items.add(new RecyclerModel(Constants.RECYCLER_SPACE_12_ITEM));
        }

        if(servicesActive.getOrDefault(Constants.SERVICE_MUFTIY, true)) {
            items.add(new RecyclerModel(Constants.RECYCLER_MAIN_QUESTION_ITEM, getImamModels()));
            items.add(new RecyclerModel(Constants.RECYCLER_SPACE_12_ITEM));
        }

        //todo test hide diaspoars
        /*if(servicesActive.getOrDefault(Constants.SERVICE_DIASPOARS, true)) {
            items.add(new RecyclerModel(Constants.RECYCLER_MAIN_DIASPOARS_ITEM, getDiaspoarModels()));
            items.add(new RecyclerModel(Constants.RECYCLER_SPACE_12_ITEM));
        }*/

        List<RecyclerModel> others = getOtherModels();
        if(others.size() > 2) {
            items.add(new RecyclerModel(Constants.RECYCLER_MAIN_OTHER_ITEM, others));
            items.add(new RecyclerModel(Constants.RECYCLER_SPACE_12_ITEM));
        }

        items.add(new RecyclerModel(Constants.RECYCLER_MAIN_PLACES_ITEM, getPlacesModels()));
        items.add(new RecyclerModel(Constants.RECYCLER_SPACE_12_ITEM));

        listener.initMainRecycler(items);
    }

    public void getMainModelsWithNoInternet(){
        List<RecyclerModel> items = new ArrayList<>();

        List<RecyclerModel> worships = getWorshipModels();
        if(worships.size() > 2) {
            items.add(new RecyclerModel(Constants.RECYCLER_MAIN_WORSHIP_ITEM, worships));
            items.add(new RecyclerModel(Constants.RECYCLER_SPACE_12_ITEM));
        }

        //todo test hide azkars
        List<RecyclerModel> azkars = getAzkarModels();
        if(azkars.size() > 2) {
            items.add(new RecyclerModel(Constants.RECYCLER_MAIN_AZKARS_ITEM, azkars));
            items.add(new RecyclerModel(Constants.RECYCLER_SPACE_12_ITEM));
        }

        //todo test hide diaspoars
        /*if(servicesActive.getOrDefault(Constants.SERVICE_DIASPOARS, true)) {
            items.add(new RecyclerModel(Constants.RECYCLER_MAIN_DIASPOARS_ITEM, getDiaspoarModels()));
            items.add(new RecyclerModel(Constants.RECYCLER_SPACE_12_ITEM));
        }*/

        listener.initMainRecycler(items);
    }
    public List<RecyclerModel> getWorshipModels(){
        List<RecyclerModel> models = new ArrayList<>();

        models.add(new RecyclerModel(Constants.RECYCLER_SPACE_12_ITEM));

        if(servicesActive.getOrDefault(Constants.SERVICE_COMPASS, true))
            models.add(new RecyclerModel(Constants.RECYCLER_WORSHIP_ITEM, new OtherEntity(R.id.QibleFragment, R.string.compass, R.drawable.ic_compass_new, R.color.light_white)));

        if(servicesActive.getOrDefault(Constants.SERVICE_NAMAZ, true))
            models.add(new RecyclerModel(Constants.RECYCLER_WORSHIP_ITEM, new OtherEntity(R.id.PrayerFragment, R.string.namaz, R.drawable.ic_namaz_new_2, R.color.light_white)));

        if(servicesActive.getOrDefault(Constants.SERVICE_KORAN, true))
            models.add(new RecyclerModel(Constants.RECYCLER_WORSHIP_ITEM, new OtherEntity(R.id.KoranFragment, R.string.koran, R.drawable.ic_koran_new_2, R.color.light_white)));

        if(servicesActive.getOrDefault(Constants.SERVICE_TASBIH, true))
            models.add(new RecyclerModel(Constants.RECYCLER_WORSHIP_ITEM, new OtherEntity(R.id.tasbihFragment, R.string.tasbih, R.drawable.ic_tasbih_new, R.color.light_white)));

        if(servicesActive.getOrDefault(Constants.SERVICE_ZAKYAT, true))
            models.add(new RecyclerModel(Constants.RECYCLER_WORSHIP_ITEM, new OtherEntity(R.id.ZakyatFragment, R.string.zakyat, R.drawable.ic_zakyat_new, R.color.light_white)));

        models.add(new RecyclerModel(Constants.RECYCLER_SPACE_12_ITEM));

        return models;
    }

    public List<RecyclerModel> getOtherModels(){
        List<RecyclerModel> models = new ArrayList<>();

        models.add(new RecyclerModel(Constants.RECYCLER_SPACE_12_ITEM));

        if(servicesActive.getOrDefault(Constants.SERVICE_TV, true))
            models.add(new RecyclerModel(Constants.RECYCLER_WORSHIP_ITEM, new OtherEntity(R.id.TvFragment, R.string.tv, R.drawable.ic_tv_new, R.color.light_white)));

        if(servicesActive.getOrDefault(Constants.SERVICE_RADIO, true))
            models.add(new RecyclerModel(Constants.RECYCLER_WORSHIP_ITEM, new OtherEntity(R.id.RadioFragment, R.string.radio, R.drawable.ic_radio_new, R.color.light_white)));
        //models.add(new RecyclerModel(Constants.RECYCLER_WORSHIP_ITEM, new OtherEntity(R.id.QuastionFragment, R.string.send_quastion, R.drawable.ic_imam_quastion, R.color.icon_imam_quastion)));

        if(servicesActive.getOrDefault(Constants.SERVICE_SERMONS, true))
            models.add(new RecyclerModel(Constants.RECYCLER_WORSHIP_ITEM, new OtherEntity(R.id.SermonsFragment,R.string.sermons, R.drawable.ic_sermon, R.color.light_white)));

        if(servicesActive.getOrDefault(Constants.SERVICE_WALLPAPER, true))
            models.add(new RecyclerModel(Constants.RECYCLER_WORSHIP_ITEM, new OtherEntity(R.id.WallpaperFragment, R.string.wallpaper, R.drawable.ic_wallpapers, R.color.light_white)));

        models.add(new RecyclerModel(Constants.RECYCLER_SPACE_12_ITEM));

        return models;
    }

    public List<RecyclerModel> getPlacesModels(){
        List<RecyclerModel> models = new ArrayList<>();

        models.add(new RecyclerModel(Constants.RECYCLER_SPACE_12_ITEM));
        models.add(new RecyclerModel(Constants.RECYCLER_PLACES_ITEM, new OtherEntity(R.id.mapGPlacesFragment, R.string.pointChurch, R.drawable.ic_mosque, R.color.light_white)));
        models.add(new RecyclerModel(Constants.RECYCLER_PLACES_ITEM, new OtherEntity(R.id.mapGPlacesFragment, R.string.halalShort, R.drawable.ic_halal_new, R.color.light_white)));
        models.add(new RecyclerModel(Constants.RECYCLER_PLACES_ITEM, new OtherEntity(R.id.mapGPlacesFragment,R.string.pointCafe, R.drawable.ic_food_icon_new,R.color.light_white)));
        models.add(new RecyclerModel(Constants.RECYCLER_PLACES_ITEM, new OtherEntity(R.id.mapGPlacesFragment,R.string.pointRestaurants, R.drawable.ic_food_icon_new,R.color.light_white)));
        models.add(new RecyclerModel(Constants.RECYCLER_SPACE_12_ITEM));

        return models;
    }

    public List<RecyclerModel> getImamModels(){
        List<RecyclerModel> models = new ArrayList<>();
        models.add(new RecyclerModel(Constants.RECYCLER_SPACE_12_ITEM));
        if (userRepository.getImamResMains()!=null)
            for (ImamResMain imamRe : userRepository.getImamResMains())
                models.add(new RecyclerModel(Constants.RECYCLER_IMAM_ITEM, imamRe));
        models.add(new RecyclerModel(Constants.RECYCLER_SPACE_12_ITEM));
        return models;
    }

    public List<RecyclerModel> getDiaspoarModels(){
        List<RecyclerModel> models = new ArrayList<>();

        models.add(new RecyclerModel(Constants.RECYCLER_SPACE_12_ITEM));
        if (userRepository.getDiaspoarResponces()!=null)
            for (DiaspoarResponce diaspoarRe : userRepository.getDiaspoarResponces())
                models.add(new RecyclerModel(Constants.RECYCLER_DIASPOARS_ITEM, diaspoarRe));
        models.add(new RecyclerModel(Constants.RECYCLER_SPACE_12_ITEM));

        return models;
    }

    public List<RecyclerModel> getAzkarModels() {
        List<RecyclerModel> models = new ArrayList<>();
        models.add(new RecyclerModel(Constants.RECYCLER_SPACE_12_ITEM));

        AzkarsRes azkars = azkarsRepository.getAzkarsRes();
        if (azkars != null) {
            if(azkars.getMorning() != null && !azkars.getMorning().isEmpty())
                models.add(new RecyclerModel(Constants.RECYCLER_AZKARS_ITEM,
                        new OtherEntity(Constants.AZKAR_MORNING, R.string.azkar_morning, R.drawable.ic_azkars_morning, R.color.light_white)));

            if(azkars.getEvening() != null && !azkars.getEvening().isEmpty())
                models.add(new RecyclerModel(Constants.RECYCLER_AZKARS_ITEM,
                        new OtherEntity(Constants.AZKAR_EVENING, R.string.azkar_evening, R.drawable.ic_azkars_evening, R.color.light_white)));

            if(azkars.getImportant() != null && !azkars.getImportant().isEmpty())
                models.add(new RecyclerModel(Constants.RECYCLER_AZKARS_ITEM,
                        new OtherEntity(Constants.AZKAR_IMPORTANT, R.string.azkar_important, R.drawable.ic_azkars_important, R.color.light_white)));

            if(azkars.getAfterNamaz() != null && !azkars.getAfterNamaz().isEmpty())
                models.add(new RecyclerModel(Constants.RECYCLER_AZKARS_ITEM,
                        new OtherEntity(Constants.AZKAR_AFTER_NAMAZ, R.string.azkar_after_namaz, R.drawable.ic_azkars_after_pray, R.color.light_white)));
        }
        models.add(new RecyclerModel(Constants.RECYCLER_SPACE_12_ITEM));
        return models;
    }
}
