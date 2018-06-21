package com.wallet.crypto.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.wallet.crypto.R;

public class PasswordView extends FrameLayout {
    private EditText password;

    public PasswordView(@NonNull Context context) {
        super(context);

        init();
    }

    private void init() {
        LayoutInflater.from(getContext())
                .inflate(R.layout.layout_dialog_password, this, true);
        password = findViewById(R.id.password);
    }

    public String getPassword() {
        return password.getText().toString();
    }

    public void showKeyBoard() {
        password.requestFocus();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        showKeyBoard();
    }
}
