package com.integrate.unified.utiuqjyrti;

import com.integrate.unified.tyiyfvohom.AdBFPlatForm;

public class Distribution_es {

    public static AdBFPlatForm RandomMooncake(int fb,int tm){
        int first = (int)(0+Math.random()*100);
        AdBFPlatForm type;
//        if((first>=0)&&(first<=fb)){
            type =AdBFPlatForm.Facebook;
//        }else {
//            type =AdBFPlatForm.TigerMedia;
//        }
        return type;
    }

}
