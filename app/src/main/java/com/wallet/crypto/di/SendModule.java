package com.wallet.crypto.di;

import com.wallet.crypto.router.ConfirmationRouter;
import com.wallet.crypto.viewmodel.SendViewModelFactory;

import dagger.Module;
import dagger.Provides;

@Module
class SendModule {
    @Provides
    SendViewModelFactory provideSendViewModelFactory(ConfirmationRouter confirmationRouter) {
        return new SendViewModelFactory(confirmationRouter);
    }

    @Provides
    ConfirmationRouter provideConfirmationRouter() {
        return new ConfirmationRouter();
    }
}
