package com.integrate.unified;





import android.content.Context;

import com.tendcloud.tenddata.TalkingDataSDK;

import java.util.Map;

public class TalkingDataEvent {
    /**
     *
     * @param context
     * @param eventId
     */
    public static void WKeeNM(Context context, String eventId,Map map) {

        TalkingDataSDK.onEvent(context,eventId,0.0, map);
    }
}
