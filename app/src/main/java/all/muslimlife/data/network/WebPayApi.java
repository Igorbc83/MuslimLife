package all.muslimlife.data.network;


import all.muslimlife.data.model.QrCodeRes;
import all.muslimlife.data.model.webpay.mlcoin.WebPayMLCoinRes;
import all.muslimlife.data.model.webpay.sub.WebPaySubRes;
import io.reactivex.Single;
import okhttp3.RequestBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface WebPayApi {

    @Multipart
    @POST("mlcoin/index.php")
    Single<WebPayMLCoinRes> getMLCoinCount(
            @Part("action") RequestBody action,
            @Part("user_id") RequestBody userId
    );

    @Multipart
    @POST("mlcoin/index.php")
    Single<WebPayMLCoinRes> addMLCoin(
            @Part("action") RequestBody action,
            @Part("user_id") RequestBody userId,
            @Part("amount") RequestBody count
    );

    @Multipart
    @POST("subscription/index.php")
    Single<WebPaySubRes> getSubStatus(
            @Part("action") RequestBody action,
            @Part("user_id") RequestBody userId
    );

    @GET("qrs")
    Single<QrCodeRes> getQrCodeInfo(
            @Query("user_id") String userId
    );
}
