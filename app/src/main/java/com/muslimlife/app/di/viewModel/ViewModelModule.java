package com.muslimlife.app.di.viewModel;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.muslimlife.app.di.scopes.ViewModelKey;
import com.muslimlife.app.viewmodel.auth.AuthViewModel;
import com.muslimlife.app.viewmodel.main.MainViewModel;
import com.muslimlife.app.viewmodel.profile.ProfileViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(DaggerViewModelFactory factory);
    
    @Binds
    @IntoMap
    @ViewModelKey(AuthViewModel.class)
    abstract ViewModel provideAuthViewModel(AuthViewModel authViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel.class)
    abstract ViewModel provideProfileViewModel(ProfileViewModel authViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel.class)
    abstract ViewModel provideMainViewModel(MainViewModel mainViewModel);
}
