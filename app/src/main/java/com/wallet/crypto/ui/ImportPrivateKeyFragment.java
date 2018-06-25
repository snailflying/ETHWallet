package com.wallet.crypto.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.wallet.crypto.R;
import com.wallet.crypto.ui.widget.OnImportPrivateKeyListener;

public class ImportPrivateKeyFragment extends Fragment implements View.OnClickListener {

    private static final OnImportPrivateKeyListener dummyOnImportPrivateKeyListener = (key, pwd) -> {
    };

    private EditText privateKey;
    private EditText pwd;
    private EditText pwdConfirm;
    private OnImportPrivateKeyListener onImportPrivateKeyListener;

    public static ImportPrivateKeyFragment create() {
        return new ImportPrivateKeyFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return LayoutInflater.from(getContext())
                .inflate(R.layout.fragment_import_private_key, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        privateKey = view.findViewById(R.id.private_key);
        pwd = view.findViewById(R.id.pwd);
        pwdConfirm = view.findViewById(R.id.pwd_confirm);
        view.findViewById(R.id.import_action).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        privateKey.setError(null);
        String privateKeyValue = privateKey.getText().toString();
        String pwdValue = pwd.getText().toString();
        String pwdConfirmValue = pwdConfirm.getText().toString();
        if (TextUtils.isEmpty(privateKeyValue) || privateKeyValue.length() != 64) {
            privateKey.setError(getString(R.string.error_field_required));
        } else if (TextUtils.isEmpty(pwdValue)) {
            pwd.setError(getString(R.string.error_invalid_password));
        } else if (TextUtils.isEmpty(pwdConfirmValue)) {
            pwdConfirm.setError(getString(R.string.error_invalid_password));
        } else if (!pwdValue.equals(pwdConfirmValue)) {
            pwdConfirm.setError(getString(R.string.error_incorrect_password_confirm));
        } else {
            onImportPrivateKeyListener.onPrivateKey(privateKeyValue, pwdConfirmValue);
        }

    }

    public void setOnImportPrivateKeyListener(OnImportPrivateKeyListener onImportPrivateKeyListener) {
        this.onImportPrivateKeyListener = onImportPrivateKeyListener == null
                ? dummyOnImportPrivateKeyListener
                : onImportPrivateKeyListener;
    }
}
