package com.wallet.crypto.repository;

import com.wallet.crypto.entity.NetworkInfo;

public interface OnNetworkChangeListener {
	void onNetworkChanged(NetworkInfo networkInfo);
}
