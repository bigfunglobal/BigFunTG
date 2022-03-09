package com.bigfun.sdk.NetWork;


import com.bigfun.sdk.model.FBISPlacement;

public interface BFRewardedVideoListener {

    void onRewardedVideoAdClosed();

    void onRewardedVideoAdRewarded(FBISPlacement placement);

}
