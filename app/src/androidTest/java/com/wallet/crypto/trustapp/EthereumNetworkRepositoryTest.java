package com.wallet.crypto;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.wallet.crypto.repository.EthereumNetworkRepository;
import com.wallet.crypto.repository.SharedPreferenceRepository;
import com.wallet.crypto.repository.SharedPreferenceRepository;

import org.junit.Before;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class EthereumNetworkRepositoryTest {

    private EthereumNetworkRepository networkRepository;

    @Before
    public void setUp() {
        Context context = InstrumentationRegistry.getTargetContext();
        SharedPreferenceRepository preferenceRepositoryType = new SharedPreferenceRepository(context);
        networkRepository = new EthereumNetworkRepository(preferenceRepositoryType);
    }

}
