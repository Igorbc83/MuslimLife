package all.muslimlife.data.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import all.muslimlife.data.model.QrCodeRes;
import all.muslimlife.data.model.webpay.mlcoin.WebPayMLCoinRes;
import all.muslimlife.data.model.webpay.sub.WebPaySubRes;
import all.muslimlife.data.network.WebPayApi;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class WebPayRepository {

    private WebPayApi api;

    private String userId;
    private String email;

    private final MutableLiveData<Integer> mlCoinsLiveData = new MutableLiveData<>();
    private boolean mlCoinsInit = false;

    public WebPayRepository(WebPayApi api) {
        this.api = api;
    }

    public void getMLCoinCount(){
        if(mlCoinsInit)
            return;

        api.getMLCoinCount(getPart("get_balance"), getPart(userId)).subscribe(new SingleObserver<>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
            }

            @Override
            public void onSuccess(@NonNull WebPayMLCoinRes mlCoins) {
                Log.d("getMLCoinCount", "onSuccess");
                mlCoinsInit = true;

                mlCoinsLiveData.postValue(Integer.parseInt(mlCoins.getBalance()));
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d("getMLCoinCount", "onError");
            }
        });
    }

    public void updateMLCoin(int count) {
        api.addMLCoin(
                        getPart("update_balance"),
                        getPart(userId),
                        getPart(String.valueOf(count)))
                .subscribe(new SingleObserver<>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onSuccess(@NonNull WebPayMLCoinRes mlCoins) {
                        Log.d("getMLCoinCount", "onSuccess");
                        mlCoinsLiveData.postValue(Integer.parseInt(mlCoins.getBalance()));
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d("getMLCoinCount", "onError");
                    }
                });
    }

    public Single<WebPaySubRes> getSubStatus(){
        return api.getSubStatus(getPart("check_subscription"), getPart(userId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private RequestBody getPart(String str){
        return RequestBody.create(MediaType.parse("form-data"), str);
    }

    public void setUserData(String userId, String email) {
        this.userId = userId;
        this.email = email;
    }

    public MutableLiveData<Integer> getMlCoinsLiveData() {
        return mlCoinsLiveData;
    }

    public String getSubWebUrl(){
        return "https://muslimlife.site/muslimlife-subscription.html?email=" + email;
    }

    public String getMlCoinWebUrl(){
        return "https://muslimlife.site/mlcoin.html?email=" + email;
    }

    public boolean checkCanSpendCoin(int cost){
        if(mlCoinsLiveData.getValue() != null){
            return mlCoinsLiveData.getValue() - cost >= 0;
        }else{
            return false;
        }
    }

    public Single<QrCodeRes> getQrCodeInfo(String userId) {
        return api.getQrCodeInfo(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
