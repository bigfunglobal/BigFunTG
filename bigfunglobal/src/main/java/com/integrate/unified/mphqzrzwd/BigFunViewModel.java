package com.integrate.unified.mphqzrzwd;
import android.text.TextUtils;


import com.integrate.unified.LogUtils;
import com.integrate.unified.LzWuSuptLoad;



public class BigFunViewModel {
    public static boolean google=false,ISoure=false,sms = false, sdk = false, shar = false, fblonig = false, adjust = false, tkdata = false,firebase=false,IRnet=false,FBnet=false,TMnet=false;
    public static String bannerAdId = "",interstitialId="",rewardedVideoId="",SourceAppKey="";
    public static int insetAdTM,insetAdFB,incentiveVideoTM,incentiveVideoFB,streamerAdFB,streamerAdTM;
    private static BigFunViewModel instance;
    public BigFunViewModel(){

    }
    public static BigFunViewModel getInstance() {
        if (instance == null) {
            synchronized (BigFunViewModel.class) {
                if (instance == null) {
                    instance = new BigFunViewModel();
                }
            }
        }
        return instance;
    }
    public void BigFunViewModelGosn(SdkConfigurationInfoBean bean){
        sdk = true;
        if (!TextUtils.isEmpty(bean.getAdjustAppToken()) && !TextUtils.isEmpty(bean.getBuriedPointType()) && bean.getBuriedPointType().contains("3")) {
            adjust = true;

        }
//        if(!TextUtils.isEmpty(bean.getIronSourceAppKey())){
//
//        }
//        if (!TextUtils.isEmpty(bean.getAdjustAppToken()) && !TextUtils.isEmpty(bean.getBuriedPointType()) && bean.getBuriedPointType().contains("1")) {
//            firebase = true;
//        }
        if (!TextUtils.isEmpty(bean.getTalkingDataAppId()) && !TextUtils.isEmpty(bean.getBuriedPointType()) && bean.getBuriedPointType().contains("2")) {
            tkdata = true;
        }
        if (!TextUtils.isEmpty(bean.getAdsType())&&bean.getAdsType().contains("1")&&!TextUtils.isEmpty(bean.getIronSourceAppKey())) {
            ISoure=true;
            SourceAppKey=bean.getIronSourceAppKey();

//            SourceNetWork.getInstance().TimerIronSource();
//            FBnet=true;
//            if (!TextUtils.isEmpty(bean.getBannerAdId())) {
//                bannerAdId = bean.getPlacementIdProduction();
//            }
//            if (!TextUtils.isEmpty(bean.getInterstitialId())) {
//                interstitialId = bean.getPlacementIdTest();
//            }
//            if (!TextUtils.isEmpty(bean.getRewardedVideoId())) {
//                rewardedVideoId = bean.getPlacementIdTest();
//            }
        }
        if (!TextUtils.isEmpty(bean.getAdsType())&&bean.getAdsType().contains("2")) {
            TMnet=true;

        }

        if (!TextUtils.isEmpty(bean.getLoginType()) && bean.getLoginType().contains("1")) {
            fblonig = true;

        }
//        if (!TextUtils.isEmpty(bean.getLoginType()) && bean.getLoginType().contains("2")) {
//            sms = true;
//        }
        if (!TextUtils.isEmpty(bean.getLoginType()) && bean.getLoginType().contains("3")) {
            google = true;

        }
        if (!TextUtils.isEmpty(bean.getShareType())) {
            shar = true;
        }
//        if(!TextUtils.isEmpty(bean.getInsetAdTM())){
//            insetAdTM= Integer.parseInt(bean.getInsetAdTM());
//        }
//        if(!TextUtils.isEmpty(bean.getInsetAdFB())){
//            insetAdFB= Integer.parseInt(bean.getInsetAdFB());
//        }
//        if(!TextUtils.isEmpty(bean.getIncentiveVideoTM())){
//            incentiveVideoTM= Integer.parseInt(bean.getIncentiveVideoTM());
//        }
//        if(!TextUtils.isEmpty(bean.getIncentiveVideoFB())){
//            incentiveVideoFB= Integer.parseInt(bean.getIncentiveVideoFB());
//        }
//        if(!TextUtils.isEmpty(bean.getStreamerAdFB())){
//            streamerAdFB= Integer.parseInt(bean.getStreamerAdFB());
//        }
//        if(!TextUtils.isEmpty(bean.getStreamerAdTM())){
//            streamerAdTM= Integer.parseInt(bean.getStreamerAdTM());
//        }

    }
}
