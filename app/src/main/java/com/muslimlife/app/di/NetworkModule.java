package com.muslimlife.app.di;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.muslimlife.app.BuildConfig;

import java.util.concurrent.TimeUnit;

import all.muslimlife.data.network.ChatAIApi;
import all.muslimlife.data.network.WebPayApi;
import dagger.Module;
import dagger.Provides;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import com.muslimlife.app.data.network.AzkarsApi;
import com.muslimlife.app.data.network.GPlacesApi;
import com.muslimlife.app.data.network.MuslimApi;

@Module
public class NetworkModule {

    private final String BASE_URL = "base";
    private final String BASE_GPLACES_URL = "base";

    private final String BASE_AZKARS_URL = "base";

    private final String BASE_CHAT_AI_URL = "base";

    private final String BASE_WEB_PAY_URL = "base";

    @Provides
    public HttpLoggingInterceptor provideLoggingInterceptor() {
        return new HttpLoggingInterceptor().setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
    }

    @Provides
    public OkHttpClient provideHttpClient(HttpLoggingInterceptor loggingInterceptor) {
        long TIMEOUT = 10;

        return new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .followRedirects(true)
                .followSslRedirects(true)
                .retryOnConnectionFailure(true)
                .connectTimeout(TIMEOUT, TimeUnit.MINUTES)
                .build();
    }

    @Provides
    public Gson provideGson() {
        return new GsonBuilder().setLenient().create();
    }

    @Provides
    public GsonConverterFactory provideGsonConverterFactory(Gson gson) {
        return GsonConverterFactory.create(gson);
    }

    private Retrofit provideRetrofit(OkHttpClient client, GsonConverterFactory gsonConverterFactory, String url) {
        return new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .client(client)
                .build();
    }

    @Provides
    public MuslimApi provideMuslimApi(OkHttpClient client, GsonConverterFactory gsonConverterFactory) {
        return provideRetrofit(client, gsonConverterFactory, BASE_URL).create(MuslimApi.class);
    }

    @Provides
    public GPlacesApi provideGPlacesApi(OkHttpClient client, GsonConverterFactory gsonConverterFactory) {
        return provideRetrofit(client, gsonConverterFactory, BASE_GPLACES_URL).create(GPlacesApi.class);
    }

    @Provides
    public AzkarsApi provideAzkarsApi (OkHttpClient client, GsonConverterFactory gsonConverterFactory) {
        return provideRetrofit(client, gsonConverterFactory, BASE_AZKARS_URL).create(AzkarsApi.class);
    }

    @Provides
    public ChatAIApi provideChatAIApi (OkHttpClient client, GsonConverterFactory gsonConverterFactory) {
        return provideRetrofit(client, gsonConverterFactory, BASE_CHAT_AI_URL).create(ChatAIApi.class);
    }

    @Provides
    public WebPayApi provideWebPayApi (OkHttpClient client, GsonConverterFactory gsonConverterFactory) {
        return provideRetrofit(client, gsonConverterFactory, BASE_WEB_PAY_URL).create(WebPayApi.class);
    }
}
