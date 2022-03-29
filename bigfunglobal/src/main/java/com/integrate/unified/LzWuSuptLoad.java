package com.integrate.unified;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.widget.FrameLayout;

import androidx.annotation.Keep;
import androidx.annotation.RequiresApi;

import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustAttribution;
import com.adjust.sdk.AdjustConfig;
import com.adjust.sdk.OnAttributionChangedListener;


//import com.android.billingclient.api.Purchase;
//import com.android.billingclient.api.SkuDetails;

import com.goldsource.sdk.GoldListener;
import com.goldsource.sdk.GoldSource;
import com.integrate.unified.adjoelakqw.BFRewardedVideoListener;

//import com.integrate.unified.adjoelakqw.TMNetWork;
//import com.integrate.unified.gwdlhmkkm.GoogleCommodityListener;
//import com.integrate.unified.gwdlhmkkm.GoogleConsumePurchaseListener;
//import com.integrate.unified.gwdlhmkkm.GoogleQueryPayListener;
//import com.integrate.unified.gwdlhmkkm.GoogleQueryPurchaseListener;
//import com.integrate.unified.gwdlhmkkm.MyBillingImpl;
import com.integrate.unified.lveterksl.BFAdjustListener;
//import com.integrate.unified.lveterksl.LoginModel;
import com.integrate.unified.mphqzrzwd.BigFunViewModel;
import com.integrate.unified.mphqzrzwd.SdkConfigurationInfoBean;
import com.integrate.unified.tyiyfvohom.AdBFPlatForm;
import com.integrate.unified.tyiyfvohom.AdBFSize;

//
//import com.google.android.gms.auth.api.identity.GetSignInIntentRequest;
//import com.google.android.gms.auth.api.identity.SignInClient;

import com.google.gson.Gson;
import com.integrate.unified.utiuqjyrti.EmulatorDetector;
import com.integrate.unified.utiuqjyrti.LocationUtils;
import com.integrate.unified.utiuqjyrti.SystemUtil;
import com.tendcloud.tenddata.TDGAProfile;
import com.tendcloud.tenddata.TalkingDataGA;


import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Keep
public class LzWuSuptLoad {

    public String mPhone = "";
    public static Context mContext;
    public static String mChannel, mChannelCode;
    private static LzWuSuptLoad instance;
    public static final String TAG = "LzWuSuptLoad";
    private static Application mApplication;
    public static boolean isIn = false;
    private static long rgqwtime = 0;
    public final static int SIGN_LOGIN = 1001;
    private static String TDid="";

    //    private MyBillingImpl myBilling;
//    private static GetSignInIntentRequest mGetSignInIntentRequest;
    private static JSONObject fbgv = new JSONObject();

    //获取时间
    public static long xaPhax() {
        Date date = new Date(System.currentTimeMillis());
        return date.getTime();
    }

    /**
     * 是否是Debug模式
     */

    static boolean isDebug = false;

    private static long mTime;
    public static Activity mActivity;
    private static String data;
    private static final String EVENT_URL = "http://gmgateway.xiaoxiangwan.com:5702/TestAPI/TestAPIDataHandler.ashx?action=sdktestinfo";

    private LzWuSuptLoad() {

    }

    /**
     * @param application 上下文 “必填”
     * @param //channel   短信渠道 “必填” 短信渠道由平台提供
     * @param channelCode 渠道编码 "必填" 由平台提供
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Keep
    public static void init(Application application, String channelCode) {
        mTime = System.currentTimeMillis();
        mApplication = application;
        mContext = application.getApplicationContext();
//        mChannel = channel;
        mChannelCode = channelCode;
//        SourceNetWork.initListener();
//        TMNetWork.init();
//        LoginModel.getInstance();
//        MyBillingImpl.getInstance().initialize(mContext);
        ExceptionHandler.install(new ExceptionHandler.CustomExceptionHandler() {
            @Override
            public void handlerException(Thread thread, Throwable throwable) {
                Log.e("SDK", throwable.getMessage());
            }
        });

        mApplication.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                mActivity = activity;
            }

            @Override
            public void onActivityStarted(Activity activity) {
                mActivity = activity;
            }

            @Override
            public void onActivityResumed(final Activity activity) {
                mActivity = activity;
                if (!isIn)
                    HttpUtils.getInstance().upload(activity);
                if (BigFunViewModel.adjust)
                    Adjust.onResume();

//                IronSource.onResume(activity);

            }

            @Override
            public void onActivityPaused(Activity activity) {
                mActivity = activity;
                if (BigFunViewModel.adjust)
                    Adjust.onPause();

//                IronSource.onPause(activity);
            }

            @Override
            public void onActivityStopped(Activity activity) {
                mActivity = activity;
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                mActivity = activity;
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                mActivity = activity;
            }
        });

//        myBilling = new MyBillingImpl();
        HttpUtils.getInstance().jptnptpl(NetConstant.BINFUN_SDK, mChannelCode, new ResponseListener() {
            @Override
            public void onSuccess() {

                data = (String) SPUtils.getInstance().get(LzWuSuptLoad.mContext, Constant.KEY_DATA, "");
                LogUtils.log(data);
                SdkConfigurationInfoBean bean =
                        new Gson().fromJson(data, SdkConfigurationInfoBean.class);
                TDid=bean.getTalkingDataAppId();
                BigFunViewModel.getInstance().BigFunViewModelGosn(bean);
//                if (BigFunViewModel.FBnet) {
//                    audienceNetwork();
//                }
                if (BigFunViewModel.adjust) {
                    adjust(bean.getAdjustAppToken());
                }
                if (BigFunViewModel.tkdata) {
                    talkingDataSDK(bean.getTalkingDataAppId(), bean.getChannelName());
                }
                if (BigFunViewModel.fblonig || BigFunViewModel.shar) {
                    facebookSdk();
                }
                if (BigFunViewModel.google) {
//                    Googleinit(bean.getGoogleClientId());
                }
                if(!TextUtils.isEmpty(bean.getIronSourceAppKey())) {
                    GoldSource.initialize(mApplication, "2a935f695894e3d17e982c6bd0778b8f", bean.getIronSourceAppKey(), new GoldListener() {
                        @Override
                        public void onInitializationCompleted() {
                            Log.d(TAG, "tm init succeeded");
                        }
                    });
                }
                Log.e("BigFun", "tm init succeeded");
            }

            @Override
            public void onFail(String msg) {
                Log.e("BigFunSDK", msg);
            }

        });

//        myBilling.initialize(mContext);

//        initLogin();
        //是否已经归因
//        LogUtils.log("sdk init success");

    }

    /**
     * @param application 上下文 “必填”
     * @param //channel   短信渠道 “必填” 短信渠道由平台提供
     * @param channelCode 渠道编码 "必填" 由平台提供
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Keep
    public static void init(Application application, String channelCode, BFAdjustListener listener) {
        mTime = System.currentTimeMillis();
        mApplication = application;
        mContext = application.getApplicationContext();
//        mChannel = channel;
        mChannelCode = channelCode;
//        SourceNetWork.initListener();
//        TMNetWork.init();
//        LoginModel.getInstance();
//        MyBillingImpl.getInstance().initialize(mContext);
        ExceptionHandler.install(new ExceptionHandler.CustomExceptionHandler() {
            @Override
            public void handlerException(Thread thread, Throwable throwable) {
                Log.e("SDK", throwable.getMessage());
            }
        });

        mApplication.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                mActivity = activity;
            }

            @Override
            public void onActivityStarted(Activity activity) {
                mActivity = activity;
            }

            @Override
            public void onActivityResumed(final Activity activity) {
                mActivity = activity;
                if (!isIn)
                    HttpUtils.getInstance().upload(activity);
                if (BigFunViewModel.adjust)
                    Adjust.onResume();

//                IronSource.onResume(activity);

            }

            @Override
            public void onActivityPaused(Activity activity) {
                mActivity = activity;
                if (BigFunViewModel.adjust)
                    Adjust.onPause();

//                IronSource.onPause(activity);
            }

            @Override
            public void onActivityStopped(Activity activity) {
                mActivity = activity;
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                mActivity = activity;
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                mActivity = activity;
            }
        });

//        myBilling = new MyBillingImpl();
        HttpUtils.getInstance().jptnptpl(NetConstant.BINFUN_SDK, mChannelCode, new ResponseListener() {
            @Override
            public void onSuccess() {

                data = (String) SPUtils.getInstance().get(LzWuSuptLoad.mContext, Constant.KEY_DATA, "");
                LogUtils.log(data);
                SdkConfigurationInfoBean bean =
                        new Gson().fromJson(data, SdkConfigurationInfoBean.class);
                TDid=bean.getTalkingDataAppId();
                BigFunViewModel.getInstance().BigFunViewModelGosn(bean);
//                if (BigFunViewModel.FBnet) {
//                    audienceNetwork();
//                }
                if (BigFunViewModel.adjust) {
                    adjust(bean.getAdjustAppToken(), listener);
                }
                if (BigFunViewModel.tkdata) {
                    talkingDataSDK(bean.getTalkingDataAppId(), bean.getChannelName());
                }
                if (BigFunViewModel.fblonig || BigFunViewModel.shar) {
                    facebookSdk();
                }
                if (BigFunViewModel.google) {
//                    Googleinit(bean.getGoogleClientId());
                }

                Log.e("BigFun", "tm init succeeded");
            }

            @Override
            public void onFail(String msg) {
                Log.e("BigFunSDK", msg);
            }

        });

//        myBilling.initialize(mContext);

//        initLogin();
        //是否已经归因
//        LogUtils.log("sdk init success");

    }

    private static void facebookSdk() {
//        if (fblonig || shar)
//            return;
//        FacebookSdk.setAutoInitEnabled(true);
//        FacebookSdk.setIsDebugEnabled(true);
//        FacebookSdk.fullyInitialize();
    }


    //    private static void audienceNetwork() {
//
//        AudienceNetworkAds.initialize(mContext);
//        AudienceNetworkInitializeHelper.initialize(mContext);
////        AdSettings.setIntegrationErrorMode(INTEGRATION_ERROR_CRASH_DEBUG_MODE);
//    }

    @Keep
    public static String getDeviceId() {
        return TalkingDataGA.getDeviceId(mContext);
    }

    @Keep
    public static String getOAID() {
        return TalkingDataGA.getOAID(mContext);
    }

    /**
     * 是否真机
     * @return
     */
    @Keep
    public static boolean fictitious(){
        return EmulatorDetector.with(mContext).detects();
    }

    /**
     * 手机设备信息
     * @return
     */
    @Keep
    public static String SuspiciousEquipment(){
        Map<String,String> map=new HashMap<>();
        map.put("androidId", Settings.System.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID));
        map.put("model", SystemUtil.getInstance(mContext).getModel());
        map.put("versionName", SystemUtil.getInstance(mContext).getVersion());
        map.put("ip", IpUtils.getOutNetIP(mContext, 0));
        map.put("packageName", SystemUtil.getInstance(mContext).getPackageName());
        map.put("resolution", SystemUtil.getInstance(mContext).getResolution());
        map.put("networkType", SystemUtil.getInstance(mContext).getNetWorkType());
        map.put("gps", LocationUtils.getInstance(mContext).initLocation());
        return map.toString();
    }

    private static void talkingDataSDK(String talkingDataId, String TalkingDataChannelCode) {
        TalkingDataGA.init(mContext, talkingDataId, TalkingDataChannelCode);
        TDGAProfile.setProfile(TalkingDataGA.getDeviceId(mContext));
    }

    private static void adjust(String adjustAppToken, BFAdjustListener listener) {
        AdjustConfig acaaigxc = new AdjustConfig(mApplication, adjustAppToken, AdjustConfig.ENVIRONMENT_PRODUCTION);
        //获取时间
        rgqwtime = xaPhax();
        //获取Adjust的配置数据
        acaaigxc.setOnAttributionChangedListener(new OnAttributionChangedListener() {
            @Override
            public void onAttributionChanged(AdjustAttribution atibunt) {
                listener.onAttributionChanged(atibunt);
            }
        });
        Adjust.onCreate(acaaigxc);

    }

    private static void adjust(String adjustAppToken) {
        AdjustConfig acaaigxc = new AdjustConfig(mApplication, adjustAppToken, AdjustConfig.ENVIRONMENT_PRODUCTION);
        //获取时间
        rgqwtime = xaPhax();
        //获取Adjust的配置数据
        Adjust.onCreate(acaaigxc);
    }

    /**
     * @param context 上下文
     * @param eventId 自定义事件名称。
     * @param map     自定义事件的参数及参数取值
     */
    @Keep
    public static void onEvent(Context context, String eventId, Map map) {
        if (checkSdkNotInit()) {
            return;
        }

        if (BigFunViewModel.tkdata)
            TalkingDataEvent.WKeeNM(context, eventId, map);
        if (BigFunViewModel.adjust)
            AdjustonEvent.TrackEvent(eventId, map);
//        if (BigFunViewModel.firebase)
//            FirebaseEvent.TrackEvent(context, eventId, map);
    }
    @Keep
    public static void onEvent(String eventId, Map map) {
        if (checkSdkNotInit()) {
            return;
        }
        if (BigFunViewModel.tkdata)
            TalkingDataEvent.WKeeNM( eventId, map);
        if (BigFunViewModel.adjust)
            AdjustonEvent.TrackEvent(eventId, map);
    }
    @Keep
    public static void onEvent(String eventId) {
        if (checkSdkNotInit()) {
            return;
        }
        if (BigFunViewModel.tkdata)
            TalkingDataEvent.WKeeNM(eventId);
    }
    @Keep
    public static void trackonEvent(String eventId) {
        if (checkSdkNotInit()) {
            return;
        }
        if (BigFunViewModel.adjust)
            AdjustonEvent.TrackEvent(eventId);
    }
    @Keep
    public static void trackonEvent(String eventId,Map map) {
        if (checkSdkNotInit()) {
            return;
        }
        if (BigFunViewModel.adjust)
            AdjustonEvent.TrackEvent(eventId,map);
    }


//    @Keep
//    public void init(Context context, String channel, IAttributionListener listener) {
//        mTime = System.currentTimeMillis();
//        mContext = context;
//        mChannel = channel;
////        mListener = listener;
//        checkSdkNotInit();
//        initLogin();
//        //是否已经归因
////        LogUtils.log("sdk init success");
//    }

    /**
     * @param activity
     * @param premium_upgrade
     * @param googlePayUpdatedListener
     */

//    @Keep
//    public void GooglePay(Activity activity, String premium_upgrade, GooglePayUpdatedListener googlePayUpdatedListener) {
//        if (checkSdkNotInit()) {
//            return;
//        }
//        myBilling.googlepay(activity, premium_upgrade, googlePayUpdatedListener);
//    }



    /**
     * 设置是否是Debug模式
     *
     * @param debug
     */

    public static void setDebug(boolean debug) {
        isDebug = debug;
    }

//    private static void Googleinit(String clientId) {
//        mGetSignInIntentRequest =
//                GetSignInIntentRequest.builder()
//                        .setServerClientId(clientId)
//                        .build();
//    }

    /**
//     * @param activity Activity上下文
     */

//
//    @Keep
//    public static void BigFunLogin(Activity activity) {
//
//        if (checkSdkNotInit()) {
//            return;
//        }
//        if (!BigFunViewModel.google || mGetSignInIntentRequest == null) {
//            Log.e("BigFunSDK", "Background not set");
//            return;
//        }
//        Map<String, Object> map = new HashMap<>();
//        map.put("BFLogin_Google", "Google");
//        onEvent(mContext, "BFLogin_Google", map);
//        LoginModel.Login(activity, mGetSignInIntentRequest);
//    }


//    @Keep
//    public static SignInClient BigFunIdentity() {
//        return LoginModel.BigFunIdentity();
//    }


//    /**
//     * facebook登录
//     *
//     * @param context  activity或者fragment的context “必填”
//     * @param listener 登录回调
//     */
//    @Keep
//    public static void BigFunLogin(Context context, LoginListener listener) {
//        if (checkSdkNotInit()) {
//            return;
//        }
//        if (!BigFunViewModel.fblonig) {
//            Log.e("BigFunSDK", "后台未配置 Facebook登录");
//            return;
//        }
//        List<String> permissionList = new ArrayList<>();
////        permissionList.add("public_profile");
//        permissionList.add("email");
//        Map<String, Object> map = new HashMap<>();
//        map.put("BFLogin_FB", permissionList.toString());
//        onEvent(mContext, "BFLogin_FB", map);
//        LoginModel.facebookLogin(context, permissionList, listener);
//    }
//    @Keep
//    public static void BigFunLogout() {
//        LoginModel.BigFunLogout();
//    }

    /**
     *内购商品的展示
     * @param googleCommodityListener
     */
//    @Keep
//    public static void googleQueryPay(GoogleCommodityListener googleCommodityListener){
//        MyBillingImpl.getInstance().googleQueryPay(googleCommodityListener);
//    }

    /**
     * 已购买的未消费的商品
     * @param queryPurchaseListener
     */
//    @Keep
//    public static void googleQueryPurchase(GoogleQueryPurchaseListener queryPurchaseListener){
//        MyBillingImpl.getInstance().googleQueryPurchase(queryPurchaseListener);
//    }

    /**
     * 内购商品的购买，回调确认购买
     * @param activity
     * @param skuDetails
     * @param googleQueryPayListener
     */
//    @Keep
//    public static void initiatePurchaseFlow(Activity activity, final SkuDetails skuDetails, GoogleQueryPayListener googleQueryPayListener){
//        MyBillingImpl.getInstance().initiatePurchaseFlow(activity,skuDetails,googleQueryPayListener);
//    }

    /**
     * 消费购买的商品
     * @param purchase
     * @param purchaseListener
     */
//    @Keep
//    public static void consumePurchase(Purchase purchase, GoogleConsumePurchaseListener purchaseListener){
//        MyBillingImpl.getInstance().consumePurchase(purchase,purchaseListener);
//    }

//    /**
//     * facebook分享
//     *
//     * @param context     activity或者fragment的context “必填”
//     * @param linkContent 分享类型 “必填”
//     * @param listener    分享回调
//     */
//    @Keep
//    public static void BigFunShare(Context context, ShareContent linkContent, ShareListener listener) {
//        if (checkSdkNotInit()) {
//            return;
//        }
//        if (!BigFunViewModel.shar) {
//            Log.e("BigFunSDK", "后台未配置 Facebook分享");
//            return;
//        }
//        Map<String, Object> map = new HashMap<>();
//        map.put("linkContent", linkContent.getPageId());
//        onEvent(mContext, "BFShare_FB", map);
//        LoginModel.facebookShare(context, linkContent, listener);
//    }

    /**
     * @param activity    必填
     * @param textContent 设置文本内容
     */
    @Keep
    public static void BigFunShare(Context activity, String textContent) {
        Map<String, Object> map = new HashMap<>();
        map.put("textContent", textContent);
        onEvent(mContext, "BFShare_SYS", map);
        new Share.Builder(activity)
                .setTextContent(textContent)
                .build()
                .shareBySystem();
    }

    @Keep
    public static void BigFunShare(Context activity, Uri shareFileUri) {
        Map<String, Object> map = new HashMap<>();
        map.put("shareFileUri", shareFileUri);
        onEvent(mContext, "BFShare_SYS", map);
        new Share.Builder(activity)
                .setShareFileUri(shareFileUri)
                .build()
                .shareBySystem();
    }

    private AdBFPlatForm FBPlatForm;

    /**
     * 插屏
     */

    @Keep
    public static void ShowInterstitialAdLoadAd() {
        if (checkSdkNotInit()) {
            return;
        }
//        FBPlatForm=Distribution_es.RandomMooncake(BigFunViewModel.insetAdFB,BigFunViewModel.insetAdTM);
//        if(AdBFPlatForm.Facebook.equals(FBPlatForm)) {
//            if(!BigFunViewModel.FBnet){
//                Log.e("BigFunSDK","后台未配置 Facebook 广告");
//                return;
//            }
//            if(!TextUtils.isEmpty(BigFunViewModel.interstitialId)) {
//            Log.e("BigFunSDK", "后台未配置 插屏广告 id");
//            return;
//        }
//            AdNetwork.getInstance().interstitialAdLoadAd(mActivity, BigFunViewModel.interstitialId);
//        }else if(AdBFPlatForm.TigerMedia.equals(FBPlatForm)){
////            Map<String, Object> map = new HashMap<>();
////            map.put("placementId", BigFunViewModel.interstitialId);
////            map.put("adBFPlatForm", "TigerMedia");
////            onEvent(mContext, "BFAd_TM_Interstitial", map);
        if (!BigFunViewModel.ISoure) {
            Log.e("BigFunSDK", "后台未配置 广告");
            return;
        }
//        SourceNetWork.showInterstitial();
//        }
//        TMNetWork.showInterstitial();
    }

    /**
     * 奖励视屏
     */

    @Keep
    public static void ShowRewardedVideo(BFRewardedVideoListener listener) {

        if (checkSdkNotInit()) {
            return;
        }
//        FBPlatForm=Distribution_es.RandomMooncake(BigFunViewModel.incentiveVideoFB,BigFunViewModel.incentiveVideoTM);
//        if(!BigFunViewModel.FBnet){
//            Log.e("BigFunSDK","后台未配置 Facebook 广告");
//            return;
//        }
//        if(!TextUtils.isEmpty(BigFunViewModel.rewardedVideoId)) {
//            Log.e("BigFunSDK", "后台未配置 奖励式视频广告 id");
//            return;
//        }
//
//        if(AdBFPlatForm.Facebook.equals(FBPlatForm)) {
//            AdNetwork.getInstance().rewardedVideoLoadAd(mActivity, BigFunViewModel.rewardedVideoId, listener);
//        }else if(AdBFPlatForm.TigerMedia.equals(FBPlatForm)){
//            Map<String, Object> map = new HashMap<>();
//            map.put("placementId", BigFunViewModel.rewardedVideoId);
//            map.put("adBFPlatForm", "TigerMedia");
//            onEvent(mContext, "BFAd_TM_RewardsVedio", map);
//            GoldSource.showRewardedVideo();
        if (!BigFunViewModel.ISoure) {
            Log.e("BigFunSDK", "后台未配置 广告");
            return;
        }
//        TMNetWork.showRewardedVideo(listener);
//        SourceNetWork.showRewardedVideo(listener);
//        }
    }
    @Keep
    public static void ShowRewardedVideo( ) {

        if (checkSdkNotInit()) {
            return;
        }
//        FBPlatForm=Distribution_es.RandomMooncake(BigFunViewModel.incentiveVideoFB,BigFunViewModel.incentiveVideoTM);
//        if(!BigFunViewModel.FBnet){
//            Log.e("BigFunSDK","后台未配置 Facebook 广告");
//            return;
//        }
//        if(!TextUtils.isEmpty(BigFunViewModel.rewardedVideoId)) {
//            Log.e("BigFunSDK", "后台未配置 奖励式视频广告 id");
//            return;
//        }
//
//        if(AdBFPlatForm.Facebook.equals(FBPlatForm)) {
//            AdNetwork.getInstance().rewardedVideoLoadAd(mActivity, BigFunViewModel.rewardedVideoId, listener);
//        }else if(AdBFPlatForm.TigerMedia.equals(FBPlatForm)){
//            Map<String, Object> map = new HashMap<>();
//            map.put("placementId", BigFunViewModel.rewardedVideoId);
//            map.put("adBFPlatForm", "TigerMedia");
//            onEvent(mContext, "BFAd_TM_RewardsVedio", map);
//            GoldSource.showRewardedVideo();
        if (!BigFunViewModel.ISoure) {
            Log.e("BigFunSDK", "后台未配置 广告");
            return;
        }
//        TMNetWork.showRewardedVideo();
//        SourceNetWork.showRewardedVideo(listener);
//        }
    }

    /**
     * 横屏
     *
     * @param mBannerParentLayout
     * @param size
     */
    @Keep
    public static void ShowBanner(FrameLayout mBannerParentLayout, AdBFSize size) {
        if (checkSdkNotInit()) {
            return;
        }
//        FBPlatForm=Distribution_es.RandomMooncake(BigFunViewModel.streamerAdFB,BigFunViewModel.streamerAdTM);
//        if(AdBFPlatForm.Facebook.equals(FBPlatForm)) {
//            if(!BigFunViewModel.FBnet){
//                Log.e("BigFunSDK","后台未配置 Facebook 广告");
//                return;
//            }
//
//            if(!TextUtils.isEmpty(BigFunViewModel.bannerAdId)) {
//                Log.e("BigFunSDK", "后台未配置 横幅广告 id");
//                return;
//            }
//            AdNetwork.getInstance().adViewLoadAd(mActivity, BigFunViewModel.bannerAdId, mBannerParentLayout, size);
//        }else if(AdBFPlatForm.TigerMedia.equals(FBPlatForm)) {
        if (!BigFunViewModel.ISoure) {
            Log.e("BigFunSDK", "后台未配置 IronSource 广告");
            return;
        }
//        SourceNetWork.createAndloadBanner(mBannerParentLayout, size);
//        }
    }

    /**
     * 资源释放
     */

    @Keep
    public static void onDestroy() {
        if (checkSdkNotInit()) {
            return;
        }
//        AdNetwork.getInstance().dstroy();
//        SourceNetWork.onDestroy();
    }

    /**
     * 添加到callbackManager
     *
     * @param requestCode “必填”
     * @param resultCode  “必填”
     * @param data        “必填”
     */

//    @Keep
//    public static void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        LoginModel.onActivityResult(requestCode, resultCode, data);
//    }

    /**
     * 检查是否初始化
     */
    private static boolean checkSdkNotInit() {
        if (TextUtils.isEmpty(mChannelCode) || mContext == null || !BigFunViewModel.sdk) {
            Log.e("BigFunSDK", "sdk not init");
            return true;
        }
        return false;
    }



    /**
     * 发送短信
     *
     * @param params   号码
     * @param listener 回调
     */
//    @Keep
//    public void sendSms(Map<String, Object> params, ResponseListener listener) {
//        if (checkSdkNotInit()) {
//            return;
//        }
//        if (!sms) {
//            return;
//        }
//        if (!params.containsKey("mobile")) {
//            throw new IllegalArgumentException(PAY_TAG + "mobile is required");
//        }
//        mPhone = params.get("mobile").toString();
//        if (mPhone.length() != 12 && mPhone.length() != 10) {
//            Toast.makeText(
//                    mContext,
//                    "Please fill in the correct phone number",
//                    Toast.LENGTH_SHORT
//            ).show();
//            return;
//        }
//        String phone;
//        if (mPhone.startsWith("91")) {
//            phone = mPhone;
//        } else {
//            phone = "91" + mPhone;
//        }
//        Map<String, Object> map = new HashMap<>();
//        map.putAll(params);
//        map.put("codeType", 2);
//        map.put("channelCode", mChannel);
//        HttpUtils.getInstance().sendSms(NetConstant.REPORT_URL, map, listener);
//    }


    /**
     * 手机号+验证码登录
     *
     * @param params
     * @param listener
     */
//    @Keep
//    public void loginWithCode(Map<String, Object> params, ResponseListener listener) {
//        if (checkSdkNotInit()) {
//            return;
//        }
//        if (!params.containsKey("mobile") || !params.containsKey("code") ||
//                TextUtils.isEmpty(params.get("mobile").toString()) ||
//                TextUtils.isEmpty(params.get("code").toString())) {
//            listener.onFail("缺少参数");
//            return;
//        }
//        if (TextUtils.isEmpty(HttpUtils.mCode)) {
//            listener.onFail("请先获取验证码");
//            return;
//        }
//        if (!HttpUtils.mCode.equals(params.get("code")) || !HttpUtils.mPhone.equals(params.get("mobile"))) {
//            listener.onFail("验证码错误或者验证码与上一次获取的手机号不一致");
//            return;
//        }
//        listener.onSuccess();
//    }

}
