package com.muslimlife.app.view.sub_ru_store

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.muslimlife.app.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import ru.rustore.sdk.billingclient.RuStoreBillingClient
import ru.rustore.sdk.billingclient.model.product.Product
import ru.rustore.sdk.billingclient.model.product.ProductType
import ru.rustore.sdk.billingclient.model.product.ProductsResponse
import ru.rustore.sdk.billingclient.model.purchase.PaymentFinishCode
import ru.rustore.sdk.billingclient.model.purchase.PaymentResult
import ru.rustore.sdk.billingclient.model.purchase.PurchaseState
import ru.rustore.sdk.billingclient.model.purchase.response.PurchasesResponse
import ru.rustore.sdk.core.exception.*
import ru.rustore.sdk.core.feature.model.FeatureAvailabilityResult
import ru.rustore.sdk.core.tasks.OnCompleteListener

class RuStoreModule(ruStoreBillingClient: RuStoreBillingClient, listener: RuStoreListener) {

    var listener: RuStoreListener
    var ruStoreBillingClient: RuStoreBillingClient

    init {
        this.listener = listener
        this.ruStoreBillingClient = ruStoreBillingClient
    }

    fun purchaseProduct() {
        ruStoreBillingClient.purchases.purchaseProduct(Constants.RUSTORE_SUB_KEY)
            .addOnSuccessListener { paymentResult ->
                handlePaymentResult(paymentResult)
            }
            .addOnFailureListener {
                setErrorStateOnFailure(it)
            }
    }

    fun getProducts(){
        ruStoreBillingClient.products.getProducts(productIds = listOf("monthly_feb_2023"))
            .addOnCompleteListener(object : OnCompleteListener<ProductsResponse> {
                override fun onSuccess(result: ProductsResponse) {
                    listener.getRuStoreProducts(result.products)
                }

                override fun onFailure(throwable: Throwable) {
                    // Process error
                    if(throwable.message != null && throwable.message!!.isNotEmpty())
                        listener.showError(throwable.message)
                    else if(throwable.cause != null && throwable.cause!!.message != null && throwable.cause!!.message!!.isNotEmpty())
                        listener.showError(throwable.cause!!.message)
                }
            })
    }

    fun getPurchase(){
        ruStoreBillingClient.purchases.getPurchases()
            .addOnCompleteListener(object : OnCompleteListener<PurchasesResponse> {
                override fun onFailure(throwable: Throwable) {
                    // Process error
                    val s = ""
                }

                override fun onSuccess(result: PurchasesResponse) {
                    // Process success
                    result.purchases?.forEach { purchase ->
                        val purchaseId = purchase.purchaseId
                        if (purchaseId != null && purchase.productId == Constants.RUSTORE_SUB_KEY) {
                            when (purchase.purchaseState) {
                                PurchaseState.CREATED, PurchaseState.INVOICE_CREATED -> {
                                    deletePurchase(purchaseId, false)
                                }
                                PurchaseState.PAID -> {
                                    confirmPurchase(purchaseId)
                                }
                                PurchaseState.CONFIRMED -> {
                                    listener.confirmedPurchases()
                                    return
                                }
                                PurchaseState.CLOSED -> {
                                    listener.cancelPurchases()
                                    return
                                }
                                else -> Unit
                            }
                        }
                    }

                    listener.cancelPurchases()
                }
            })
    }

    private fun purchaseProduct(product: Product){
        ruStoreBillingClient.purchases.purchaseProduct(product.productId)
            .addOnSuccessListener { paymentResult ->
                handlePaymentResult(paymentResult)
            }
            .addOnFailureListener {
                setErrorStateOnFailure(it)
            }
    }
    private fun handlePaymentResult(paymentResult: PaymentResult) {
        when (paymentResult) {
            is PaymentResult.InvalidPurchase -> {
                paymentResult.purchaseId?.let { deletePurchase(it, false) }
            }
            is PaymentResult.PurchaseResult -> {
                when (paymentResult.finishCode) {
                    PaymentFinishCode.SUCCESSFUL_PAYMENT -> {
                        confirmPurchase(paymentResult.purchaseId)
                    }
                    PaymentFinishCode.CLOSED_BY_USER,
                    PaymentFinishCode.UNHANDLED_FORM_ERROR,
                    PaymentFinishCode.PAYMENT_TIMEOUT,
                    PaymentFinishCode.DECLINED_BY_SERVER,
                    PaymentFinishCode.RESULT_UNKNOWN,
                    -> {
                        deletePurchase(paymentResult.purchaseId, false)
                    }
                }
            }
            else -> Unit
        }
    }
    private fun confirmPurchase(purchaseId: String){
        listener.showProgress(true)
        ruStoreBillingClient.purchases.confirmPurchase(purchaseId)
            .addOnSuccessListener { response ->
                listener.confirmedPurchases()
                listener.showProgress(false)
            }
            .addOnFailureListener {
                //setErrorStateOnFailure(it)
                listener.confirmedPurchases()
                listener.showProgress(false)
            }
    }

    private fun setErrorStateOnFailure(error: Throwable){
        listener.showError(error.localizedMessage)
    }

    fun cancelPurchase(){
        listener.showProgress(true)
        ruStoreBillingClient.purchases.getPurchases()
            .addOnCompleteListener(object : OnCompleteListener<PurchasesResponse> {
                override fun onFailure(throwable: Throwable) {
                    // Process error
                    listener.showProgress(false)
                }

                override fun onSuccess(result: PurchasesResponse) {
                    // Process success
                    try {
                        result.purchases?.forEach { purchase ->
                            val purchaseId = purchase.purchaseId
                            if (purchaseId != null) {
                                when (purchase.purchaseState) {
                                    PurchaseState.CONFIRMED -> {
                                        deletePurchase(purchaseId, true)
                                    }
                                    else -> Unit
                                }
                            }
                        }

                        listener.showProgress(false)
                    }catch (error: Exception){
                        val s = ""
                    }
                }
            })
    }

    private fun deletePurchase(purchaseId: String, cancel: Boolean){
        if(cancel)
            listener.showProgress(true)
        ruStoreBillingClient.purchases.deletePurchase(purchaseId)
            .addOnSuccessListener { response ->
                if(cancel){
                    listener.cancelPurchases()
                    listener.showProgress(false)
                }

            }
            .addOnFailureListener {
                setErrorStateOnFailure(it)
            }
    }

    fun checkPurchases(){
        ruStoreBillingClient.purchases.checkPurchasesAvailability()
            .addOnCompleteListener(object : OnCompleteListener<FeatureAvailabilityResult> {
                override fun onSuccess(result: FeatureAvailabilityResult) {
                    when (result) {
                        is FeatureAvailabilityResult.Available -> {
                            // Process purchases available
                            listener.checkPurchases(true)
                        }
                        is FeatureAvailabilityResult.Unavailable -> {
                            // Process purchases unavailable
                            when (result.cause) {
                                is RuStoreOutdatedException -> {
                                    listener.checkPurchases(false)
                                    listener.showError("RuStore, установленный на вашем устройстве, не поддерживает платежи. Пожалуйста обновите RuStore или напишите им в поддержку")
                                }
                                is RuStoreNotInstalledException -> {
                                    listener.checkPurchases(false)
                                    listener.showError("На вашем устройстве не установлен RuStore. Установите пожалуйста RuStore")
                                }
                                is RuStoreUserUnauthorizedException -> {
                                    listener.checkPurchases(false)
                                    listener.showError("Вы не авторизованы в RuStore")
                                }
                                is RuStoreApplicationBannedException -> {
                                    listener.showError("Приложение заблокировано в RuStore")
                                }
                                is RuStoreUserBannedException -> {
                                    listener.showError("Вы заблокированы в RuStore")
                                }
                            }
                        }
                    }
                }

                override fun onFailure(throwable: Throwable) {
                    // Process error
                    if(throwable.message != null && throwable.message!!.isNotEmpty())
                        listener.showError(throwable.message)
                    else if(throwable.cause != null && throwable.cause!!.message != null && throwable.cause!!.message!!.isNotEmpty())
                        listener.showError(throwable.cause!!.message)
                }
            })

    }
}