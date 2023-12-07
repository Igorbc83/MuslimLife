package com.muslimlife.app.di;

import javax.inject.Singleton;

import all.muslimlife.data.network.ChatAIApi;
import all.muslimlife.data.network.WebPayApi;
import all.muslimlife.data.repository.ChatAIRepository;
import all.muslimlife.data.repository.WebPayRepository;
import dagger.Module;
import dagger.Provides;

import com.muslimlife.app.data.network.AzkarsApi;
import com.muslimlife.app.data.network.GPlacesApi;
import com.muslimlife.app.data.network.MuslimApi;
import com.muslimlife.app.data.repository.AuthRepository;
import com.muslimlife.app.data.repository.AzkarsRepository;
import com.muslimlife.app.data.repository.GPlacesRepository;
import com.muslimlife.app.data.repository.UserRepository;

@Module
public class RepositoryModule {

    @Singleton
    @Provides
    public AuthRepository provideAuthRepository(MuslimApi api) {
        return new AuthRepository(api);
    }

    @Singleton
    @Provides
    public UserRepository provideUserRepository(MuslimApi api) {
        return new UserRepository(api);
    }

    @Singleton
    @Provides
    public GPlacesRepository provideGPlacesRepository(GPlacesApi api) {
        return new GPlacesRepository(api);
    }

    @Singleton
    @Provides
    public AzkarsRepository provideAzkarsRepository(AzkarsApi api) {
        return new AzkarsRepository(api);
    }

    @Singleton
    @Provides
    public ChatAIRepository provideChatAIRepository(ChatAIApi api) {
        return new ChatAIRepository(api);
    }

    @Singleton
    @Provides
    public WebPayRepository provideWebPayRepository(WebPayApi api) {
        return new WebPayRepository(api);
    }
}
