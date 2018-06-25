package com.wallet.crypto.di;

import android.content.Context;

import com.google.gson.Gson;
import com.safframework.http.interceptor.LoggingInterceptor;
import com.wallet.crypto.App;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

@Module
class ToolsModule {
	@Provides
	Context provideContext(App application) {
		return application.getApplicationContext();
	}

	@Singleton
	@Provides
	Gson provideGson() {
		return new Gson();
	}

	@Singleton
	@Provides
	OkHttpClient okHttpClient() {
		return new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();
	}

    private LoggingInterceptor loggingInterceptor = new LoggingInterceptor.Builder()
            .loggable(true) // TODO: 发布到生产环境需要改成false
            .request()
            .requestTag("Request")
            .response()
            .responseTag("Response")
            .build();
}
