package com.integrate.unified.gwdlhmkkm;

import com.android.billingclient.api.BillingResult;

/**
 * 一次性商品消耗回调
 */
public interface GoogleConsumePurchaseListener {
    void onConsumePurchase(BillingResult billingResult, String purchaseToken);
}
