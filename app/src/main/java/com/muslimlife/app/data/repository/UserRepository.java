package com.muslimlife.app.data.repository;

import android.util.ArrayMap;

import androidx.lifecycle.MutableLiveData;

import com.muslimlife.app.data.GetFondsReq;
import com.muslimlife.app.data.model.AddAnswerReq;
import com.muslimlife.app.data.model.AddAnswerRes;
import com.muslimlife.app.data.model.DiaspoarResponce;
import com.muslimlife.app.data.model.GetAnswerReq;
import com.muslimlife.app.data.model.GetAnswerRes;
import com.muslimlife.app.data.model.GetFondsRes;
import com.muslimlife.app.data.model.GetQuastionsReq;
import com.muslimlife.app.data.model.GetQuastionsRes;
import com.muslimlife.app.data.model.ImamReq;
import com.muslimlife.app.data.model.ImamResMain;
import com.muslimlife.app.data.model.ImamsReq;
import com.muslimlife.app.data.model.KoranTestRes;
import com.muslimlife.app.data.model.NotificationReceiveReq;
import com.muslimlife.app.data.model.NotificationReq;
import com.muslimlife.app.data.model.NotificationRes;
import com.muslimlife.app.data.model.NotificationSoundReq;
import com.muslimlife.app.data.model.PlacesRes;
import com.muslimlife.app.data.model.PushTimeReq;
import com.muslimlife.app.data.model.QuastionTypeRes;
import com.muslimlife.app.data.model.RadioRes;
import com.muslimlife.app.data.model.ReadNotificationReq;
import com.muslimlife.app.data.model.ReadNotificationRes;
import com.muslimlife.app.data.model.ReadersReq;
import com.muslimlife.app.data.model.ReadersRes;
import com.muslimlife.app.data.model.Save64Req;
import com.muslimlife.app.data.model.Save64Res;
import com.muslimlife.app.data.model.SendQuastionRes;
import com.muslimlife.app.data.model.SendSupportRes;
import com.muslimlife.app.data.model.SendSupprotReq;
import com.muslimlife.app.data.model.SubscribeReq;
import com.muslimlife.app.data.model.TokenUpdateReq;
import com.muslimlife.app.data.model.TvRes;
import com.muslimlife.app.data.model.UpdateAnswerReq;
import com.muslimlife.app.data.model.UpdateAnswerRes;
import com.muslimlife.app.data.model.UserLocation;
import com.muslimlife.app.data.model.UserProfile;
import com.muslimlife.app.data.model.WallpaperRes;
import com.muslimlife.app.data.model.surah.AyahRes;
import com.muslimlife.app.data.model.surah.SurahRes;
import com.muslimlife.app.utils.Constants;
import com.muslimlife.app.view.map.PlaceTypes;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import com.muslimlife.app.data.model.parameters.CheckUserParameters;
import com.muslimlife.app.data.model.parameters.CheckUserResponse;
import com.muslimlife.app.data.model.parameters.LoadUserDataParameters;
import com.muslimlife.app.data.model.parameters.RegistrationParameters;
import com.muslimlife.app.data.model.parameters.SendQuastionReq;
import com.muslimlife.app.data.model.parameters.SermonsResponse;
import com.muslimlife.app.data.model.parameters.UpdateProfileResponse;
import com.muslimlife.app.data.network.MuslimApi;

public class UserRepository {

    private String language;
    private final MuslimApi api;
    private QuastionTypeRes[] quastionTypeRes;
    private CheckUserResponse checkUserResponse;
    private GetAnswerRes answerRes;
    private WallpaperRes wallpaperRes;
    private DiaspoarResponce diaspoarResponce;
    private DiaspoarResponce[] diaspoarResponces;
    private NotificationRes notificationRes;
    private ImamResMain[] imamResMains;
    private ReadersRes[] koranReaders;
    private PlacesRes[] placesRes;
    private Map<PlaceTypes, List<PlacesRes>> mapPlaces = new ArrayMap<>();
    private List<UserLocation> countriesList;
    private List<UserLocation> citiesList;

    private final MutableLiveData<UserProfile> userProfileMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> userIdMutableLiveData = new MutableLiveData<>();
    private List<KoranTestRes> koranPagesList;
    ArrayList<SurahRes> surahList;
    private ArrayList<String> types;
    private boolean notifReaded, messageReaded;

    private final MutableLiveData<ArrayList<SurahRes>> surahListMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<KoranTestRes>> koranPagesMutableLiveData = new MutableLiveData<>();

    private boolean subscribe;

    public UserRepository(MuslimApi api) {
        this.api = api;
    }

    public Single<UpdateProfileResponse> updateProfile(UserProfile userProfile) {
        return api.updateProfile(language,userProfile).map(result -> {
            userProfileMutableLiveData.postValue(userProfile);
            return result;
        });
    }

    public Single<UserProfile> getUserProfile(LoadUserDataParameters parameters) {
        return api.getProfile(language,parameters).flatMap((Function<UserProfile, Single<UserProfile>>) userProfile -> {
            userProfileMutableLiveData.postValue(userProfile);
            return Single.just(userProfile);
        });
    }

    public Single<CheckUserResponse> checkUserInApi(CheckUserParameters parameters) {
        return api.checkUser(parameters).map(checkUserResponse -> {
            userIdMutableLiveData.postValue(checkUserResponse.getId());
            this.checkUserResponse = checkUserResponse;
            return checkUserResponse;
        });
    }

    public Single<ArrayList<KoranTestRes>> getKoranPages() {
        return api.getKoranPages()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public Single<List<SermonsResponse>> getSermons(){
        return api.getSermons(language).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<PlacesRes[]> getPlaces(){
        return api.getPlaces(language).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<UserProfile> registrationUserInApi(RegistrationParameters parameters) {
        return api.registerUser(language,parameters).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<ArrayList<SurahRes>> getSurahs() {
        return api.getSurahs(language)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<List<AyahRes>> getAyahs() {
        return api.getAyahs()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<ReadersRes[]> getReaders(){
        return api.getReaders(language, new ReadersReq()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Single<List<UserLocation>> loadCountries() {
        return api.getCountries(language);
    }

    public Single<List<UserLocation>> loadCities(String countryId) {
        return api.getCities(language);
    }

    public Single<RadioRes[]> getRadio(){
        return api.getRadio(language).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public MutableLiveData<UserProfile> getUserProfileMutableLiveData() {
        return userProfileMutableLiveData;
    }

    public Single<Save64Res> Save64(String file, String id){
        return api.setAvatar(new Save64Req(id, file)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Save64Res> updateImage(String id, File image){
        RequestBody imageBody =
                RequestBody.create(MediaType.parse("image/png"), image);

        MultipartBody.Part imagePart =
                MultipartBody.Part.createFormData("file", image.getName(), imageBody);

        RequestBody idPart =
                RequestBody.create(MediaType.parse("form-data"), id);

        return api.setAvatarImage(imagePart, idPart).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Single<TvRes[]> getTv(){
        return api.getTv(language).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Single<ImamResMain[]> getImams(){
        return api.getImams(language).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Single<ImamResMain[]> getImams(String id){
        return api.getImams(language,new ImamsReq(id)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Single<ImamResMain[]> getImam(String id){
        return api.getImam(language, new ImamReq(id)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Single<QuastionTypeRes[]> getQuastionType(){
        return api.getQuastionType(language).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public QuastionTypeRes[] getQuastionTypeRes() {
        return quastionTypeRes;
    }

    public void setQuastionTypeRes(QuastionTypeRes[] quastionTypeRes) {
        this.quastionTypeRes = quastionTypeRes;
    }

    public Single<SendQuastionRes> sendQuastion(SendQuastionReq sendQuastionReq){
        return api.sendQuastion(sendQuastionReq).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Single<GetAnswerRes[]> getAnswer(GetAnswerReq getAnswerReq){
        return api.getAnswers(language,getAnswerReq).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Single<GetQuastionsRes[]> getQuastions(GetQuastionsReq quastionsReq){
        return api.getQuastions(quastionsReq).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Single<AddAnswerRes> setAnswer(AddAnswerReq addAnswerReq){
        return api.setAnswer(addAnswerReq).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public CheckUserResponse getCheckUserResponse() {
        return checkUserResponse;
    }

    public GetAnswerRes getAnswerRes() {
        return answerRes;
    }

    public void setAnswerRes(GetAnswerRes answerRes) {
        this.answerRes = answerRes;
    }

    public Single<UpdateAnswerRes> updateAnswer(UpdateAnswerReq updateAnswerReq){
        return api.updateAnswer(updateAnswerReq).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Single<WallpaperRes[]> getWallpaper(){
        return api.getWallpapers().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public WallpaperRes getWallpaperRes() {
        return wallpaperRes;
    }

    public void setWallpaperRes(WallpaperRes wallpaperRes) {
        this.wallpaperRes = wallpaperRes;
    }

    public Single<DiaspoarResponce[]> getDiaspoars(){
        return api.getDiaspoars(language).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public DiaspoarResponce getDiaspoarResponce() {
        return diaspoarResponce;
    }

    public void setDiaspoarResponce(DiaspoarResponce diaspoarResponce) {
        this.diaspoarResponce = diaspoarResponce;
    }

    public Single<SendSupportRes[]> sendSupport(SendSupprotReq sendSupprotReq){
        return api.sendSupportReq(sendSupprotReq).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Single<NotificationRes[]> getNotifications(NotificationReq notificationReq){
        return api.getNotifications(language, notificationReq).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public NotificationRes getNotificationRes() {
        return notificationRes;
    }

    public void setNotificationRes(NotificationRes notificationRes) {
        this.notificationRes = notificationRes;
    }

    public ImamResMain[] getImamResMains() {
        return imamResMains;
    }

    public void setImamResMains(ImamResMain[] imamResMains) {
        this.imamResMains = imamResMains;
    }

    public DiaspoarResponce[] getDiaspoarResponces() {
        return diaspoarResponces;
    }

    public void setDiaspoarResponces(DiaspoarResponce[] diaspoarResponces) {
        this.diaspoarResponces = diaspoarResponces;
    }

    public Single<ReadNotificationRes[]> readNotification(ReadNotificationReq readNotificationReq){
        return api.setReadNotification(readNotificationReq).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Single<GetFondsRes[]> getFonds(GetFondsReq getFondsReq){
        return api.getFonds(language, getFondsReq).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Boolean> pushTimeUpdate(PushTimeReq pushTimeReq){
        return api.pushTimeUpdate(language, pushTimeReq).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Boolean> setSubscribe(SubscribeReq subscribeReq){
        return api.setSubscribe(subscribeReq).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Boolean> updateToken(TokenUpdateReq tokenUpdateReq){
        return api.updateToken(tokenUpdateReq).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public void saveTypes(ArrayList<String> types){
        this.types = types;
    }

    public ArrayList<String> getTypes(){
        return types;
    }

    public List<SurahRes> getSurahList() {
        return surahList;
    }

    public void setSurahList(ArrayList<SurahRes> surahList) {
        this.surahList = surahList;
    }

    public List<KoranTestRes> getKoranPagesList() {
        return koranPagesList;
    }

    public void setKoranPagesList(List<KoranTestRes> koranPagesList) {
        this.koranPagesList = koranPagesList;
    }

    public ReadersRes[] getKoranReaders() {
        return koranReaders;
    }

    public void setKoranReaders(ReadersRes[] koranReaders) {
        this.koranReaders = koranReaders;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLanguage() {
        return (language != null && !language.isEmpty()) ? language : Constants.DEFAULT_LANG;
    }

    public PlacesRes[] getPlacesRes() {
        return placesRes;
    }

    public void setPlacesRes(PlacesRes[] placesRes) {
        this.placesRes = placesRes;
    }


    public List<UserLocation> getCountriesList() {
        return countriesList;
    }

    public void setCountriesList(List<UserLocation> countriesList) {
        this.countriesList = countriesList;
    }

    public List<UserLocation> getCitiesList() {
        return citiesList;
    }

    public void setCitiesList(List<UserLocation> citiesList) {
        this.citiesList = citiesList;
    }


    public boolean isNotifReaded() {
        return notifReaded;
    }

    public void setNotifReaded(boolean notifReaded) {
        this.notifReaded = notifReaded;
    }

    public boolean isMessageReaded() {
        return messageReaded;
    }

    public void setMessageReaded(boolean messageReaded) {
        this.messageReaded = messageReaded;
    }

    public Observable<Boolean> updateNotificationsSetting(NotificationSoundReq sound, NotificationReceiveReq receive){
        return Observable.merge(
                api.updateNotificationsSetting(sound),
                api.updateNotificationReceive(receive))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public MutableLiveData<ArrayList<SurahRes>> getSurahListMutableLiveData() {
        return surahListMutableLiveData;
    }

    public MutableLiveData<ArrayList<KoranTestRes>> getKoranPagesMutableLiveData() {
        return koranPagesMutableLiveData;
    }

    public Map<PlaceTypes, List<PlacesRes>> getMapPlaces() {
        return mapPlaces;
    }

    public void setMapPlaces(Map<PlaceTypes, List<PlacesRes>> mapPlaces) {
        this.mapPlaces = mapPlaces;
    }

    public boolean isSubscribe() {
        return subscribe;
    }

    public void setSubscribe(boolean subscribe) {
        this.subscribe = subscribe;
    }
}
