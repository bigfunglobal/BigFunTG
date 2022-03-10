package com.integrate.unified;

import android.content.Context;


import java.util.Map;

/**
 * FirebaseAnalytics数据埋点
 */

public class FirebaseEvent {

    public static void TrackEvent(Context context,String eventId, Map map) {
//        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
//        Bundle bundle = new Bundle();
//        bundle.putLong("time",System.currentTimeMillis());
//        Set set = map.keySet();
//        Iterator iter = set.iterator();
//        while (iter.hasNext()) {
//            String key = (String) iter.next();
//            bundle.putString(key,map.get(key)+"");
//        }
//        mFirebaseAnalytics.logEvent(eventId, bundle);
    }
}
