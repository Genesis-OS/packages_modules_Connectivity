/*
 * Copyright (C) 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.server.nearby.provider;

import android.accounts.Account;
import android.annotation.Nullable;
import android.content.Context;
import android.nearby.FastPairDataProviderBase;
import android.util.Log;

import androidx.annotation.WorkerThread;

import com.android.server.nearby.fastpair.footprint.FastPairUploadInfo;

import service.proto.Rpcs;

/** FastPairDataProvider is a singleton that implements APIs to get FastPair data. */
public class FastPairDataProvider {

    private static final String TAG = "FastPairDataProvider";

    private static FastPairDataProvider sInstance;

    private ProxyFastPairDataProvider mProxyFastPairDataProvider;

    /** Initializes FastPairDataProvider singleton. */
    public static synchronized FastPairDataProvider init(Context context) {

        if (sInstance == null) {
            sInstance = new FastPairDataProvider(context);
        }
        if (sInstance.mProxyFastPairDataProvider == null) {
            Log.wtf(TAG, "no proxy fast pair data provider found");
        } else {
            sInstance.mProxyFastPairDataProvider.register();
        }
        return sInstance;
    }

    @Nullable
    public static synchronized FastPairDataProvider getInstance() {
        return sInstance;
    }

    private FastPairDataProvider(Context context) {
        mProxyFastPairDataProvider = ProxyFastPairDataProvider.create(
                context, FastPairDataProviderBase.ACTION_FAST_PAIR_DATA_PROVIDER);
        if (mProxyFastPairDataProvider == null) {
            Log.d("FastPairService", "fail to initiate the fast pair proxy provider");
        } else {
            Log.d("FastPairService", "the fast pair proxy provider initiated");
        }
    }

    /** loadFastPairDeviceMetadata. */
    @WorkerThread
    @Nullable
    public Rpcs.GetObservedDeviceResponse loadFastPairDeviceMetadata(byte[] modelId) {
        if (mProxyFastPairDataProvider != null) {
            return mProxyFastPairDataProvider.loadFastPairDeviceMetadata(modelId);
        }
        throw new IllegalStateException("No ProxyFastPairDataProvider yet constructed");
    }

    /**
     * opt in default account to fast pair
     */
    public void optIn(Account account) {

    }

    /**
     * Upload the device to the footprint
     */
    public void upload(Account account, FastPairUploadInfo uploadInfo) {

    }

}
