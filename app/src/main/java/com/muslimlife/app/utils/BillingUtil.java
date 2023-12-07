package com.muslimlife.app.utils;

import static com.android.billingclient.api.BillingClient.SkuType.SUBS;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;

import java.util.ArrayList;
import java.util.List;

import com.muslimlife.app.view.MainActivity;

//!!Не забыть <uses-permission android:name="com.android.vending.BILLING" />!!

public class BillingUtil implements PurchasesUpdatedListener, BillingClientStateListener, SkuDetailsResponseListener, ConsumeResponseListener{

    private final BillingClient billingClient;
    private final Activity activity;

    //Activity передавайте сразу для oneActivityApp или в момент старта покупки, если есть разные активити, с которых идут покупки
    public BillingUtil(Context context, Activity activity){
        billingClient = BillingClient.newBuilder(context)
                .setListener(this)
                .enablePendingPurchases()
                .build();

        this.activity=activity;

        billingClient.startConnection(this);
    }

    // Проверка успешности подключения
    @Override
    public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
        if (billingResult.getResponseCode() ==  BillingClient.BillingResponseCode.OK){
            Log.d("BiillingSetup","Ok");
            //todo test hide sub logic
            checkSubNew();
        }
        else
            Log.e("BiillingSetup","ERROR");
    }

    //При необходимости можете ограничить кол-во попыток подключения
    @Override
    public void onBillingServiceDisconnected() {
        Log.e("BiillingConnect","Disconnected");
        billingClient.startConnection(this);
    }

    //Если есть валидация видов подписок, передавайте айдишник и выбирайте из констант, или передавайте константу айдишника
    public void startPurchase(){
        List<String> skuList = new ArrayList<>();
        skuList.add(Constants.SKU_NAME);

        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(skuList).setType(SUBS);

        billingClient.querySkuDetailsAsync(params.build(),this);
    }

    @Override
    public void onSkuDetailsResponse(@NonNull BillingResult billingResult, @Nullable List<SkuDetails> sku) {
        if(billingResult.getResponseCode()==BillingClient.BillingResponseCode.OK && sku!=null && sku.size()>0){
            //Если вы покупаете не 1 объект сразу, реализация может отличаться
            BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                                                                   .setSkuDetails(sku.get(0))
                                                                   .build();

            billingClient.launchBillingFlow(activity, billingFlowParams);
        }else if(billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK)
            //При нуловом list
            Log.e("SKU_NULL", "SKU error");
        else
            //При провальном запросе
            Log.e("onSkuDetailsResponse", "Result error");
    }

    @Override
    public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> purchases) {
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && purchases != null) {
            // Пихаем его в обработчик. Снова вариант при покупке 1 подписки. Если всё сразу покупаем - переделать
            ConsumeParams consumeParams = ConsumeParams.newBuilder()
                                            .setPurchaseToken(purchases.get(0).getPurchaseToken())
                                            .build();

            billingClient.consumeAsync(consumeParams, this);
        } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED)
            //При желании можете по другому обработать пользователя по лицу за отмену процесса
            Log.e("PurchaseUpdate","UserCancel");
        else
            //При провальном запросе
            Log.e("onPurchasesUpdated", "Result error");
    }

    @Override
    public void onConsumeResponse(@NonNull BillingResult billingResult, @NonNull String s) {
        // обрабатываем Успешный успех
        ((MainActivity) activity).setSubscribe(true);
    }

    public void checkSub(){
/*        List<Purchase> list = billingClient.queryPurchases(SUBS).getPurchasesList();
        if(list!=null)
            for(Purchase purchase:list){
                if(purchase.getSku().equals(SKU_NAME)){
                    ((MainActivity) activity).setSubscribe(purchase.getPurchaseState()==1);
                    break;
                }
            }
        else
            ((MainActivity) activity).setSubscribe(false);*/
    }

    public void checkSubNew(){
        if(!isGooglePlayBillingAvailable())
            return;
        billingClient.queryPurchasesAsync(SUBS, (billingResult, list) -> {
            for (Purchase purchase : list) {
                if (purchase.getSkus().get(0).equalsIgnoreCase(Constants.SKU_NAME)) {
                    ((MainActivity) activity).setSubscribe(purchase.getPurchaseState() == 1);
                    break;
                }
            }
            ((MainActivity) activity).setSubscribe(false);
        });
    }

    public boolean isGooglePlayBillingAvailable() {
        BillingResult responseCode = billingClient.isFeatureSupported(BillingClient.FeatureType.SUBSCRIPTIONS);

        if(responseCode.getResponseCode() == BillingClient.BillingResponseCode.SERVICE_UNAVAILABLE ||
                responseCode.getResponseCode() == BillingClient.BillingResponseCode.BILLING_UNAVAILABLE)
            return false;
        else
            return true;
    }
}