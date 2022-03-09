package com.bigfun.sdk.login;

import com.adjust.sdk.AdjustAttribution;

public interface BFAdjustListener {
    void onAttributionChanged(AdjustAttribution attribution);
}
