package com.shopgun.android.sdk;

import android.content.Context;

public class CoreKit implements Kit {

    public CoreKit(Context ctx) {

    }

    @Override
    public String getVersion() {
        return null;
    }

    @Override
    public void inject(ShopGun sgn) {

    }

    public static class Builder {

        Context mContext;

        public Builder(Context context) {
            mContext = context;
        }

        public CoreKit build() {
            return new CoreKit(mContext);
        }

    }

}