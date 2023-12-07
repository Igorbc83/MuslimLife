package com.muslimlife.app.view.sub_ru_store;

import java.util.List;

import ru.rustore.sdk.billingclient.model.product.Product;

public interface RuStoreListener {

    default void getRuStoreProducts(List<Product> products){}
    default void showError(String error){}
    default void checkPurchases(boolean state){}
    default void showProgress(boolean show){}
    void cancelPurchases();
    void confirmedPurchases();
}
