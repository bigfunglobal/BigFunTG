package com.integrate.unified.lveterksl;

import androidx.annotation.Keep;

import com.adjust.sdk.AdjustAttribution;
@Keep
public interface BFAdjustListener {
    void onAttributionChanged(AdjustAttribution attribution);
}
