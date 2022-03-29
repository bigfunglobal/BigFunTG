package com.integrate.unified;

import android.app.Activity;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.integrate.unified.enxlkjtiy.EncryptUtil;
import com.integrate.unified.mphqzrzwd.LoginBean;
import com.integrate.unified.mphqzrzwd.SendSmsBean;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpUtils{


    private HttpUtils() {

        ExceptionHandler.install(new ExceptionHandler.CustomExceptionHandler() {
            @Override
            public void handlerException(Thread thread, Throwable throwable) {
                Log.e("SDK",throwable.getMessage());
            }
        });

        mediaType = MediaType.parse("application/json; charset=utf-8");
        gson = new Gson();
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(TIME_OUT, TimeUnit.SECONDS)
                .build();
    }


    private static class InstanceHolder {
        private static HttpUtils instance = new HttpUtils();
    }

    public static HttpUtils getInstance() {
        return InstanceHolder.instance;
    }


    private String token = (String) SPUtils.getInstance().get(LzWuSuptLoad.mContext, "accessToken", "");
    private static final long TIME_OUT = 30L;
    public static String mCode = "";
    public static String mPhone = "";
    private Gson gson;
    private MediaType mediaType;
    public OkHttpClient okHttpClient;
    private ExecutorService mExecutors = Executors.newFixedThreadPool(2);
    public static final String ORDER_FAIL = "ORDER_FAIL";
    public static final String ORDER_EXCEPTION = "ORDER_EXCEPTION";
    public static final String ORDER_SDK = "ORDER_SDK";
    public static final String PAY_FAIL = "PAY_FAIL";
    public static Handler mHandler = new Handler(Looper.getMainLooper());
    public static final int REQUEST_CODE = 100;

    /**
     * post请求
     *
     * @param url      请求地址
     * @param params   请求参数
     * @param callback 请求回调
     */
    public <T> void post(String url, Map<String, Object> params, com.integrate.unified.lveterksl.Callback<T> callback) {
        if (TextUtils.isEmpty(url)) throw new IllegalArgumentException("url.length() == 0");
        if (params.isEmpty()) throw new IllegalArgumentException("params.size == 0");
        String json = null;
        try {
            json = EncryptUtil.encryptData(gson.toJson(params));
            Request request = new Request.Builder()
                    .url(url)
                    .addHeader(
                            "accessToken",
                            (String) SPUtils.getInstance().get(LzWuSuptLoad.mContext, Constant.KEY_TOKEN, "")
                    )
                    .post(RequestBody.create(mediaType, json))
                    .build();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    callback.onFail(e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        if (response.isSuccessful()) {
                            if (response.code() == 200) {
                                if (response.body() != null) {
                                    callback.onResult((T) response.body().string());
                                } else {
                                    callback.onFail(response.code() + "--" + response.message());
                                }
                            } else {
                                callback.onFail(response.code() + "--" + response.message());
                            }
                        } else {
                            callback.onFail(response.code() + "--" + response.message());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onFail(e.getMessage());
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 登录
     *
     * @param url
     * @param params
     * @param listener
     */
    public void login(String url, Map<String, Object> params, ResponseListener listener) {
        if (TextUtils.isEmpty(url)) throw new IllegalArgumentException("url.length() == 0");
        if (params.isEmpty()) throw new IllegalArgumentException("params.size == 0");
        String json = null;
        try {
            json = EncryptUtil.encryptData(gson.toJson(params));
            Request request = new Request.Builder()
                    .url(url)
                    .addHeader(
                            "accessToken",
                            (String) SPUtils.getInstance().get(LzWuSuptLoad.mContext, Constant.KEY_TOKEN, "")
                    )
                    .post(RequestBody.create(mediaType, json))
                    .build();

            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    listener.onFail(e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        if (response.isSuccessful()) {
                            if (response.code() == 200) {
                                if (response.body() != null) {
                                    Log.e("response",response.body().string()+"1111111");
                                    LoginBean loginBean =
                                            gson.fromJson(response.body().string(), LoginBean.class);
                                    if (Integer.parseInt(loginBean.getCode()) == 0) {
                                        int loginType = (int) params.get("loginType");
                                        if (loginType == 2) {
                                            String mobile = (String) params.get("mobile");
                                            if (!TextUtils.isEmpty(mobile)) {
                                                SPUtils.getInstance().put(LzWuSuptLoad.mContext, Constant.KEY_LOGIN_PHONE, mobile);
                                            }
                                        }
                                        SPUtils.getInstance().put(
                                                LzWuSuptLoad.mContext,
                                                Constant.KEY_TOKEN,
                                                loginBean.getData().getAccessToken()
                                        );
                                        listener.onSuccess();
                                    } else {
                                        SPUtils.getInstance().remove(LzWuSuptLoad.mContext, "channel");
                                        listener.onFail(loginBean.getMsg());
                                    }
                                } else {
                                    listener.onFail(response.message());
                                }
                            } else {
                                SPUtils.getInstance().remove(LzWuSuptLoad.mContext, "channel");
                                listener.onFail(response.message());
                            }
                        } else {
                            listener.onFail(response.code() + "--" + response.message());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * SDK配置
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void jptnptpl(String url, String params, ResponseListener listener) {
        if (TextUtils.isEmpty(url)) throw new IllegalArgumentException("url.length() == 0");
        if (params.isEmpty()) throw new IllegalArgumentException("params.size == 0");
        String json = null;
        try {
            json = EncryptUtil.encryptsdkData(params);
            Request request = new Request.Builder()
                    .url(url)
                    .addHeader(
                            "accessToken",
                            (String) SPUtils.getInstance().get(LzWuSuptLoad.mContext, Constant.KEY_TOKEN, "")
                    )
                    .post(RequestBody.create(mediaType, json))
                    .build();

            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    listener.onFail(e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        if (response.isSuccessful()) {
                            if (response.code() == 200) {
                                if (response.body() != null) {
                                    SendSmsBean bean =
                                            gson.fromJson(
                                                    response.body().string(),
                                                    SendSmsBean.class
                                            );
                                    if (Integer.parseInt(bean.getCode()) == 0) {
                                        mCode = bean.getData();
                                        SPUtils.getInstance().put(
                                                LzWuSuptLoad.mContext,
                                                Constant.KEY_DATA,
                                                EncryptUtil.decryptDataSDK(bean.getData()));

                                        listener.onSuccess();
                                    } else {
                                        listener.onFail(bean.getMsg());
                                    }
                                } else {
                                    listener.onFail(response.message());
                                }
                            } else {
                                listener.onFail(response.message());
                            }
                        } else {
                            listener.onFail(response.code() + "--" + response.message());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        listener.onFail(e.getMessage());
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            listener.onFail(e.getMessage());
        }
    }
    /**
     * SDK配置
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void bigfunsdkReport(String url, String params, ResponseListener listener) {
        if (TextUtils.isEmpty(url)) throw new IllegalArgumentException("url.length() == 0");
        if (params.isEmpty()) throw new IllegalArgumentException("params.size == 0");
        String json = null;
        try {
            json = EncryptUtil.encryptsdkData(params);
            Request request = new Request.Builder()
                    .url(url)
                    .addHeader(
                            "accessToken",
                            (String) SPUtils.getInstance().get(LzWuSuptLoad.mContext, Constant.KEY_TOKEN, "")
                    )
                    .post(RequestBody.create(mediaType, json))
                    .build();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    listener.onFail(e.getMessage());
                }



                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        if (response.isSuccessful()) {
                            if (response.code() == 200) {
                                if (response.body() != null) {
                                    SendSmsBean bean =
                                            gson.fromJson(
                                                    response.body().string(),
                                                    SendSmsBean.class
                                            );
                                    if (Integer.parseInt(bean.getCode()) == 0) {
                                        mCode = bean.getData();
                                        SPUtils.getInstance().put(
                                                LzWuSuptLoad.mContext,
                                                Constant.KEY_DATA,
                                                EncryptUtil.decryptDataSDK(bean.getData()));
                                        Log.e("EncryptUtil",EncryptUtil.decryptDataSDK(bean.getData()));
                                        listener.onSuccess();
                                    } else {
                                        listener.onFail(bean.getMsg());
                                    }
                                } else {
                                    listener.onFail(response.message());
                                }
                            } else {
                                listener.onFail(response.message());
                            }
                        } else {
                            listener.onFail(response.code() + "--" + response.message());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        listener.onFail(e.getMessage());
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            listener.onFail(e.getMessage());
        }
    }

    /**
     * 发送验证码
     */
    public void sendSms(String url, Map<String, Object> params, ResponseListener listener) {
        if (TextUtils.isEmpty(url)) throw new IllegalArgumentException("url.length() == 0");
        if (params.isEmpty()) throw new IllegalArgumentException("params.size == 0");
//        mPhone = params.get("mobile").toString();
        String json = null;
        try {
            json = EncryptUtil.encryptsdkreData(gson.toJson(params));
//            json = gson.toJson(params);
            Request request = new Request.Builder()
                    .url(url)
                    .addHeader(
                            "accessToken",
                            (String) SPUtils.getInstance().get(LzWuSuptLoad.mContext, Constant.KEY_TOKEN, "")
                    )
                    .post(RequestBody.create(mediaType, json))
                    .build();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    listener.onFail(e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        if (response.isSuccessful()) {
                            if (response.code() == 200) {
                                if (response.body() != null) {
                                    SendSmsBean bean =
                                            gson.fromJson(
                                                    response.body().string(),
                                                    SendSmsBean.class
                                            );
                                    if (Integer.parseInt(bean.getCode()) == 0) {
                                        mCode = bean.getData();
                                        listener.onSuccess();
                                    } else {
                                        listener.onFail(bean.getMsg());
                                    }
                                } else {
                                    listener.onFail(response.message());
                                }
                            } else {
                                listener.onFail(response.message());
                            }
                        } else {
                            listener.onFail(response.code() + "--" + response.message());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        listener.onFail(e.getMessage());
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            listener.onFail(e.getMessage());
        }
    }

    /**
     * get请求
     *
     * @param url      请求地址
     * @param params   请求参数
     * @param listener 请求回调
     */
    public <T> void get(String url, Map<String, Object> params, ResponseListener listener) {
        try {
            if (TextUtils.isEmpty(url)) throw new IllegalArgumentException("url.length() == 0");
            StringBuffer requestUrl = new StringBuffer(url);
            boolean isFirst = true;
            for (String key : params.keySet()) {
//                if (isFirst) {
//                    isFirst = false;
//                    requestUrl.append("?");
//                } else {
//                    requestUrl.append("&");
//                }
                requestUrl.append("&");
                requestUrl.append(key).append("=").append(params.get(key));
            }
            LogUtils.log(requestUrl.toString());
            Request request = new Request.Builder()
                    .url(requestUrl.toString())
                    .addHeader("accessToken", token)
                    .get()
                    .build();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    LogUtils.log(e.getMessage());
                    if (listener != null) {
                        listener.onFail(e.getMessage());
                    }
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    LogUtils.log(response.body().string());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取渠道配置
     */
    public <T> void getChannelConfig(String url, Map<String, Object> params, com.integrate.unified.lveterksl.Callback<T> callback) {
        if (TextUtils.isEmpty(url)) throw new IllegalArgumentException("url.length() == 0");
        if (params.isEmpty()) throw new IllegalArgumentException("params.size == 0");
        String json = null;
        try {
            json = EncryptUtil.encryptData(gson.toJson(params));
            Request request = new Request.Builder()
                    .url(url)
                    .addHeader(
                            "accessToken",
                            (String) SPUtils.getInstance().get(LzWuSuptLoad.mContext, Constant.KEY_TOKEN, "")
                    )
                    .post(RequestBody.create(mediaType, json))
                    .build();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    callback.onFail(e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        if (response.code() == 200) {
                            if (response.body() != null) {
                                callback.onResult((T) response.body().string());
                            } else {
                                callback.onFail(response.message());
                            }
                        } else {
                            callback.onFail(response.message());
                        }
                    } else {
                        callback.onFail(response.message());
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            callback.onFail(e.getMessage());
        }

    }

    public void initLogin(String url, Map<String, Object> params, com.integrate.unified.lveterksl.Callback<LoginBean> listener) {
        if (TextUtils.isEmpty(url)) throw new IllegalArgumentException("url.length() == 0");
        if (params.isEmpty()) throw new IllegalArgumentException("params.size == 0");
        String json = null;
        try {
            json = EncryptUtil.encryptData(gson.toJson(params));
            Request request = new Request.Builder()
                    .url(url)
                    .addHeader(
                            "accessToken",
                            (String) SPUtils.getInstance().get(LzWuSuptLoad.mContext, Constant.KEY_TOKEN, "")
                    )
                    .post(RequestBody.create(mediaType, json))
                    .build();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    listener.onFail(e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        if (response.isSuccessful()) {
                            if (response.code() == 200) {
                                if (response.body() != null) {
                                    LoginBean loginBean =
                                            gson.fromJson(response.body().string(), LoginBean.class);
                                    if (Integer.parseInt(loginBean.getCode()) == 0) {
                                        listener.onResult(loginBean);
                                    } else {
                                        SPUtils.getInstance().remove(LzWuSuptLoad.mContext, "channel");
                                        listener.onFail(loginBean.getMsg());
                                    }
                                } else {
                                    listener.onFail(response.message());
                                }
                            } else {
                                SPUtils.getInstance().remove(LzWuSuptLoad.mContext, "channel");
                                listener.onFail(response.message());
                            }
                        } else {
                            listener.onFail(response.code() + "--" + response.message());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public synchronized void upload(Activity activity) {
        mExecutors.execute(new ReportTask(activity));
    }
    public synchronized void adawad(Activity activity) {
        mExecutors.execute(new ReportTask(activity));
    }


}
