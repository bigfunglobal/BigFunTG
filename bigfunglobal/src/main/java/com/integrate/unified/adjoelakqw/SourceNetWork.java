package com.integrate.unified.adjoelakqw;

import static com.integrate.unified.LzWuSuptLoad.mActivity;
import static com.integrate.unified.LzWuSuptLoad.mContext;
import static com.integrate.unified.LzWuSuptLoad.onEvent;

import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.integrate.unified.LzWuSuptLoad;
import com.integrate.unified.LogUtils;
import com.integrate.unified.mphqzrzwd.BigFunViewModel;
import com.integrate.unified.mphqzrzwd.FBISPlacement;
import com.integrate.unified.tyiyfvohom.AdBFSize;
import com.integrate.unified.utiuqjyrti.FunctionUtils;
import com.ironsource.mediationsdk.ISBannerSize;
import com.ironsource.mediationsdk.IronSource;
import com.ironsource.mediationsdk.IronSourceBannerLayout;
import com.ironsource.mediationsdk.impressionData.ImpressionData;
import com.ironsource.mediationsdk.impressionData.ImpressionDataListener;
import com.ironsource.mediationsdk.integration.IntegrationHelper;
import com.ironsource.mediationsdk.logger.IronSourceError;
import com.ironsource.mediationsdk.model.Placement;
import com.ironsource.mediationsdk.sdk.BannerListener;
import com.ironsource.mediationsdk.sdk.InterstitialListener;
import com.ironsource.mediationsdk.sdk.RewardedVideoListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class SourceNetWork{
    private static FrameLayout mBannerParentLayout;
    private static IronSourceBannerLayout mIronSourceBannerLayout;
    private static BFRewardedVideoListener listener;

    public SourceNetWork() {

    }

    private static SourceNetWork instance;

    public static SourceNetWork getInstance() {

        if (instance == null) {
            synchronized (SourceNetWork.class) {
                if (instance == null) {
                    instance = new SourceNetWork();
                }
            }
        }
        return instance;
    }

    static Timer timer;
    public void TimerIronSource() {
       LogUtils.log("instance:  "+instance+"");
            timer = new Timer();
            if(FunctionUtils.getCurrentTask(mContext)) {
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        LogUtils.log("timer: " + "1000");
                        LogUtils.log("mActivity: " + mActivity + "Ket");
                        if (mActivity != null && !TextUtils.isEmpty(BigFunViewModel.SourceAppKey)) {
                            LogUtils.log("mActivity123123: " + mActivity + "Ket" + BigFunViewModel.SourceAppKey);
                            timer.cancel();
                            timer = null;
                            initIronSource();
                        }
                    }
                }, 0, 1000);
            }
    }

    public static void initListener(){
        //?????????????????????????????????????????????????????????
        //?????????????????????
        IronSource.setRewardedVideoListener(rewardedVideoListener);
        //?????????????????????
        IronSource.setInterstitialListener(interstitialListener);
        //??????Impression???????????????
        IronSource.addImpressionDataListener(impressionDataListener);
        IronSource.isRewardedVideoAvailable();
    }

    private void initIronSource() {

        LogUtils.log("initIronSource: "+ mActivity+"Ket");
            IntegrationHelper.validateIntegration(mActivity);

            //??????IronSource??????id
            IronSource.setUserId(IronSource.getAdvertiserId(mContext));
             //?????????IronSource SDK
            IronSource.getAdvertiserId(mContext);
            IronSource.init(mActivity, BigFunViewModel.SourceAppKey);
//        ??????????????????
            IronSource.shouldTrackNetworkState(mContext, true);
        IronSource.isRewardedVideoAvailable();
        IronSource.loadInterstitial();
    }


    public static void showRewardedVideo(BFRewardedVideoListener videolistener) {
        Map<String, Object> map = new HashMap<>();
        map.put("adBFPlatForm", "IronSource");
        onEvent(mContext, "BFAd_IS_RewardedVideo", map);
        listener = videolistener;
        if (IronSource.isRewardedVideoAvailable())
            //show rewarded video
            IronSource.showRewardedVideo();
    }

    public static void showInterstitial() {
        Map<String, Object> map = new HashMap<>();
        map.put("adBFPlatForm", "IronSource");
        onEvent(mContext, "BFAd_IS_Interstitial", map);
        if (IronSource.isInterstitialReady()) {
//                    //show the interstitial
            IronSource.showInterstitial();
        }
    }

    public static void onDestroy() {
        if(timer!=null){
            timer.cancel();
        }
        if (mIronSourceBannerLayout != null) {
            IronSource.destroyBanner(mIronSourceBannerLayout);
            if (mBannerParentLayout != null) {
                mBannerParentLayout.removeView(mIronSourceBannerLayout);
            }
        }
    }

    /**
     * ???????????????IronSource??????
     */
    private static ISBannerSize isBannerSize = ISBannerSize.BANNER;

    public static void createAndloadBanner(FrameLayout mbannerParentLayout, AdBFSize size) {

        if (size.equals(AdBFSize.BANNER_HEIGHT_50))
            isBannerSize = ISBannerSize.BANNER;
        if (size.equals(AdBFSize.BANNER_HEIGHT_90))
            isBannerSize = ISBannerSize.LARGE;
        if (size.equals(AdBFSize.RECTANGLE_HEIGHT_250))
            isBannerSize = ISBannerSize.RECTANGLE;

        Map<String, Object> map = new HashMap<>();
        map.put("adBFPlatForm", "IronSource");
        map.put("adSize", isBannerSize);
        onEvent(mContext, "BFAd_IS_Banner", map);

        mBannerParentLayout = mbannerParentLayout;

        //??????IronSource?????????IronSourceBanner?????????createBanner API
        mIronSourceBannerLayout = IronSource.createBanner(mActivity, isBannerSize);

        //???IronSourceBanner??????????????????
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
        mBannerParentLayout.addView(mIronSourceBannerLayout, 0, layoutParams);

        if (mIronSourceBannerLayout != null) {
            //?????????????????????
            mIronSourceBannerLayout.setBannerListener(new BannerListener() {
                @Override
                public void onBannerAdLoaded() {
                     LogUtils.log( "onBannerAdLoaded");
                    //??????????????????????????????????????????????????????????????????????????????????????????????????????
                    mBannerParentLayout.setVisibility(View.VISIBLE);
                }

                @Override
                public void onBannerAdLoadFailed(IronSourceError error) {
                     LogUtils.log( "onBannerAdLoadFailed" + "IronSourceError??? " + error);
                }

                @Override
                public void onBannerAdClicked() {
                     LogUtils.log( "onBannerAdClicked");
                }

                @Override
                public void onBannerAdScreenPresented() {
                     LogUtils.log( "onBannerAdScreenPresented");
                }

                @Override
                public void onBannerAdScreenDismissed() {
                     LogUtils.log( "onBannerAdScreenDismissed");
                }

                @Override
                public void onBannerAdLeftApplication() {
                     LogUtils.log( "onBannerAdLeftApplication");
                }
            });
            //????????????????????????????????????
            IronSource.loadBanner(mIronSourceBannerLayout);
        } else {
            Toast.makeText(mActivity, "IronSource.createBanner returned null", Toast.LENGTH_LONG).show();
        }
    }

    // --------- IronSource??????????????? ---------
    static RewardedVideoListener rewardedVideoListener=new RewardedVideoListener() {
        @Override
        public void onRewardedVideoAdOpened() {
            //?????????????????????
            LogUtils.log( "onRewardedVideoAdOpened");
        }

        @Override
        public void onRewardedVideoAdClosed() {
            //?????????????????????
            LogUtils.log( "onRewardedVideoAdClosed");
            //?????????????????????????????????????????????????????????????????????
            listener.onRewardedVideoAdClosed();
        }

        @Override
        public void onRewardedVideoAvailabilityChanged(boolean b) {
            //??????????????????????????????
            LogUtils.log( "onRewardedVideoAvailabilityChanged" + " " + b);

        }

        @Override
        public void onRewardedVideoAdStarted() {
            //?????????????????????
            LogUtils.log( "onRewardedVideoAdStarted");
        }

        @Override
        public void onRewardedVideoAdEnded() {
            //?????????????????????
            LogUtils.log( "onRewardedVideoAdEnded");
        }

        @Override
        public void onRewardedVideoAdRewarded(Placement placement) {
            //?????????????????????????????????????????????????????????
            LogUtils.log( "onRewardedVideoAdRewarded" + " " + placement);
            listener.onRewardedVideoAdRewarded(new FBISPlacement(placement));
        }

        @Override
        public void onRewardedVideoAdShowFailed(IronSourceError ironSourceError) {
            //???????????????????????????
            //?????????????????????IronSourceError???????????????????????????
            // IronSourceError.getErrorCode();
            // IronSourceError.getErrorMessage();
            LogUtils.log( "onRewardedVideoAdShowFailed" + "ironSourceError??? " + ironSourceError);
        }

        @Override
        public void onRewardedVideoAdClicked(Placement placement) {

        }
    };


    // --------- IronSource ???????????? Listener ---------

    static InterstitialListener interstitialListener=new InterstitialListener() {
        @Override
        public void onInterstitialAdClicked() {
            //?????????????????????
            LogUtils.log( "onInterstitialAdClicked");
        }

        @Override
        public void onInterstitialAdReady() {
            //???????????????????????????

            LogUtils.log( "onInterstitialAdReady");
        }

        @Override
        public void onInterstitialAdLoadFailed(IronSourceError ironSourceError) {
            //??????????????????????????????
//?????????????????????IronSourceError???????????????????????????
//         IronSourceError.getErrorCode();
//         IronSourceError.getErrorMessage();
            LogUtils.log( "onInterstitialAdLoadFailed" + "ironSourceError??? " + ironSourceError);

        }

        @Override
        public void onInterstitialAdOpened() {
            //?????????????????????
            LogUtils.log( "onInterstitialAdOpened");
        }

        @Override
        public void onInterstitialAdClosed() {
            //????????????????????????
            LogUtils.log( "onInterstitialAdClosed");

        }

        @Override
        public void onInterstitialAdShowSucceeded() {
            //???????????????????????????
            LogUtils.log( "onInterstitialAdShowSucceeded");
        }

        @Override
        public void onInterstitialAdShowFailed(IronSourceError ironSourceError) {
            //???????????????????????????

//?????????????????????IronSourceError???????????????????????????
            // IronSourceError.getErrorCode();
            // IronSourceError.getErrorMessage();
            LogUtils.log( "onInterstitialAdShowFailed" + "ironSourceError??? " + ironSourceError);

        }
    };

    //------------------------
    static ImpressionDataListener impressionDataListener=new ImpressionDataListener() {
        @Override
        public void onImpressionSuccess(ImpressionData impressionData) {
            if (impressionData != null) {
                LogUtils.log( "onImpressionSuccess " + impressionData);
            }
        }
    };


}
