package com.integrate.unified;

import android.app.Activity;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import com.integrate.unified.enxlkjtiy.EncryptUtil;
import com.integrate.unified.utiuqjyrti.LocationUtils;
import com.integrate.unified.utiuqjyrti.SystemUtil;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ReportTask implements Runnable {

    private Activity activity;
    private Gson gson=new Gson();
    private MediaType mediaType = MediaType.parse("application/json; charset=utf-8");

    public ReportTask(Activity activity) {
        this.activity=activity;
    }

    @Override
    public void run() {
        try {
                Map<String,String> map=new HashMap<>();
                    map.put("channelCode", LzWuSuptLoad.mChannelCode);
                    map.put("androidId", Settings.System.getString(LzWuSuptLoad.mContext.getContentResolver(), Settings.Secure.ANDROID_ID));
                    map.put("model", SystemUtil.getInstance(LzWuSuptLoad.mContext).getModel());
                    map.put("versionName", SystemUtil.getInstance(LzWuSuptLoad.mContext).getVersion());
                    map.put("ip", IpUtils.getOutNetIP(LzWuSuptLoad.mContext, 0));
                    map.put("packageName", SystemUtil.getInstance(LzWuSuptLoad.mContext).getPackageName());
                    map.put("resolution", SystemUtil.getInstance(LzWuSuptLoad.mContext).getResolution());
                    map.put("networkType", SystemUtil.getInstance(LzWuSuptLoad.mContext).getNetWorkType());
                    map.put("gps", LocationUtils.getInstance(activity).initLocation());
//                    try {
//                        jsonObject.put("aaid", com.bigfun.sdk.AdvertisingIdClient.getAdId(BigFunSDK.mContext));
//                        jsonObject.put("gaid", com.bigfun.sdk.AdvertisingIdClient.getAdId(BigFunSDK.mContext));
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        jsonObject.put("aaid", "11111111111");
//                        jsonObject.put("gaid", "11111111111");
//                    }
                String s=map.toString();
                String json = EncryptUtil.encryptsdkreData(gson.toJson(map));
                Request request = new Request.Builder()
                        .url(NetConstant.REPORT_URL)
                        .post(RequestBody.create(mediaType, json))
                        .build();
                Response response = HttpUtils.getInstance().okHttpClient.newCall(request).execute();
                if (response.isSuccessful()) {
                    ResponseBody body = response.body();
                    if (body != null) {
                        String string = body.string();
                        if (!TextUtils.isEmpty(string)) {
                            JSONObject jsonObject = new JSONObject(string);
                            String code = jsonObject.optString("code");
                            if(code.equals("0")){
                                Log.e("code",code);
                                LzWuSuptLoad.isIn=true;
                            }
                        }
                    }
                } else {
                    LogUtils.log(response.message());
                }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.log(e.getMessage());
        }
    }
}
