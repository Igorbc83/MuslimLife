package com.muslimlife.app.view.mlcoin;

import java.util.List;

import ru.rustore.sdk.billingclient.model.product.Product;

public interface MLCoinBillingListener {

    void showProgress(boolean show);

    void buyCoinSuccess(String productId);

    default void getRuStoreProducts(List<Product> products){}
    default void showError(String error){}
    default void checkPurchases(boolean state){}
    default void cancelPurchases(){}
}
