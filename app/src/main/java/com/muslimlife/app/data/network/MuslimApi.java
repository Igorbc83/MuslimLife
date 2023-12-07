package com.muslimlife.app.data.network;

import androidx.annotation.Nullable;

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

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

import com.muslimlife.app.data.model.parameters.CheckUserParameters;
import com.muslimlife.app.data.model.parameters.CheckUserResponse;
import com.muslimlife.app.data.model.parameters.LoadUserDataParameters;
import com.muslimlife.app.data.model.parameters.RegistrationParameters;
import com.muslimlife.app.data.model.parameters.SendQuastionReq;
import com.muslimlife.app.data.model.parameters.SermonsResponse;
import com.muslimlife.app.data.model.parameters.UpdateProfileResponse;

public interface MuslimApi {

    @GET("countries")
    Single<List<UserLocation>> getCountries(@Header("language") String language);

    @GET("cities")
    Single<List<UserLocation>> getCities(@Header("language") String language);

    @POST("profile/update")
    Single<UpdateProfileResponse> updateProfile(@Header("language") String language,@Body UserProfile userProfile);

    @POST("profile")
    Single<UserProfile> getProfile(@Header("language") String language,@Body LoadUserDataParameters parameters);

    @POST("reg")
    Single<UserProfile> registerUser(@Header("language") String language,@Body RegistrationParameters parameters);

    @POST("check")
    Single<CheckUserResponse> checkUser(@Body CheckUserParameters parameters);

    @POST("predicts")
    Single<List<SermonsResponse>> getSermons(@Header("language") String language);

    @POST("koran/read")
    Single<ArrayList<KoranTestRes>> getKoranPages();

    @POST("koran/suras")
    Single<ArrayList<SurahRes>> getSurahs(@Header("language") String language);

    @POST("koran/ayahs")
    Single<List<AyahRes>> getAyahs();

    @POST("places")
    Single<PlacesRes[]> getPlaces(@Header("language") String language);

    @POST("koran/readers")
    Single<ReadersRes[]> getReaders(@Header("language") String language, @Body ReadersReq readersReq);

    @POST("radio")
    Single<RadioRes[]> getRadio(@Header("language") String language);

    @POST("user/saveimage64")
    Single<Save64Res> setAvatar(@Body Save64Req save64Req);

    @Multipart
    @POST("user/saveimage")
    Single<Save64Res> setAvatarImage(@Part MultipartBody.Part file,
                                     @Part("user") RequestBody user);

    @POST("tv")
    Single<TvRes[]> getTv(@Header("language") String language);

    @POST("imams")
    Single<ImamResMain[]> getImam(@Header("language") String language,
                                  @Nullable @Body ImamReq imamReq);

    @POST("imams")
    Single<ImamResMain[]> getImams(@Header("language") String language);

    @POST("imams")
    Single<ImamResMain[]> getImams(@Header("language") String language,
                                   @Nullable @Body ImamsReq imamsReq);

    @POST("questiontypes")
    Single<QuastionTypeRes[]> getQuastionType(@Header("language") String language);

    @POST("questions/add")
    Single<SendQuastionRes> sendQuastion(@Body SendQuastionReq sendQuastionReq);

    @POST("answers")
    Single<GetAnswerRes[]> getAnswers(@Header("language") String language,
                                      @Body GetAnswerReq getAnswerReq);

    @POST("questions")
    Single<GetQuastionsRes[]> getQuastions(@Body GetQuastionsReq getQuasionsReq);

    @POST("answers/add")
    Single<AddAnswerRes> setAnswer(@Body AddAnswerReq addAnswerReq);

    @POST("answers/update")
    Single<UpdateAnswerRes> updateAnswer(@Body UpdateAnswerReq updateAnswerReq);

    @POST("background")
    Single<WallpaperRes[]> getWallpapers();

    @POST("diasporas")
    Single<DiaspoarResponce[]> getDiaspoars(@Header("language") String language);

    @POST("support/add")
    Single<SendSupportRes[]> sendSupportReq(@Body SendSupprotReq sendSupprotReq);

    @POST("notifications")
    Single<NotificationRes[]> getNotifications(@Header("language") String language,
                                               @Body NotificationReq notificationReq);

    @POST("notifications/read")
    Single<ReadNotificationRes[]> setReadNotification(@Body ReadNotificationReq readNotificationReq);

    @POST("fonds")
    Single<GetFondsRes[]> getFonds(@Header("language") String language,
                                   @Body GetFondsReq getFondsReq);

    @POST("subscribed/update")
    Single<Boolean> setSubscribe(@Body SubscribeReq subscribeReq);

    @POST("token/update")
    Single<Boolean> updateToken(@Body TokenUpdateReq tokenUpdateReq);

    @POST("pushtime/update")
    Single<Boolean> pushTimeUpdate(@Header("language") String language,
                                   @Body PushTimeReq pushTimeReq);
    @POST("sound/update")
    Observable<Boolean> updateNotificationsSetting(@Body NotificationSoundReq notifySettingReq);

    @POST("pushreceive/update")
    Observable<Boolean> updateNotificationReceive(@Body NotificationReceiveReq notifyReceiveReq);
}
