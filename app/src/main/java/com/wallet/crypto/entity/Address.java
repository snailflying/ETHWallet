package com.wallet.crypto.entity;

import android.text.TextUtils;

import org.web3j.crypto.WalletUtils;

import java.util.regex.Pattern;

public class Address {

    private static final Pattern ignoreCaseAddrPattern = Pattern.compile("(?i)^(0x)?[0-9a-f]{40}$");
    private static final Pattern lowerCaseAddrPattern = Pattern.compile("^(0x)?[0-9a-f]{40}$");
    private static final Pattern upperCaseAddrPattern = Pattern.compile("^(0x)?[0-9A-F]{40}$");

    public final String value;

    public Address(String value) {
        this.value = value;
    }

    public static boolean isAddress(String address) {
        return WalletUtils.isValidAddress(address);
    }
}
