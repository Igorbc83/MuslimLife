package com.muslimlife.app.di;

import com.muslimlife.app.view.azkar.AzkarFragment;
import com.muslimlife.app.view.azkar.AzkarListFragment;
import com.muslimlife.app.view.bookmark.BookmarkFragment;
import com.muslimlife.app.view.choosedWallpaper.ChoosedWallpaperFragment;
import com.muslimlife.app.view.diaspoarQuastion.DiaspoarQuastionFragment;
import com.muslimlife.app.view.diaspoars.DiaspoarsFragment;
import com.muslimlife.app.view.donation.DonationFragment;
import com.muslimlife.app.view.imamQuastion.ImamQuastionFragment;
import com.muslimlife.app.view.koranSettings.KoranSettingsFragment;
import com.muslimlife.app.view.language.LanguageFragment;
import com.muslimlife.app.view.language.LanguageStartFragment;
import com.muslimlife.app.view.mlcoin.MLCoinBillingNewFragment;
import com.muslimlife.app.view.notification.NotificationFragment;
import com.muslimlife.app.view.pushSettings.PushSettingsFragment;
import com.muslimlife.app.view.quastion.QuastionFragment;
import com.muslimlife.app.view.quastionAnswer.QuastionsAnswerFragment;
import com.muslimlife.app.view.quastions.QuastionsFragment;
import com.muslimlife.app.view.services.ServicesFragment;
import com.muslimlife.app.view.settings.SettingsFragment;
import com.muslimlife.app.view.sub_ru_store.SubRuStoreFragment;
import com.muslimlife.app.view.translate.TranslateFragment;
import com.muslimlife.app.view.tv.TvFragment;
import com.muslimlife.app.view.wallpaper.ImageFragment;
import com.muslimlife.app.view.wallpaper.WallpaperFragment;
import com.muslimlife.app.view.zakyatSettings.ZakyatSettingsCurrencyFragment;
import com.muslimlife.app.view.zakyatSettings.ZakyatSettingsFondsFragment;
import com.muslimlife.app.view.zakyatSettings.ZakyatSettingsFragment;

import all.muslimlife.view.chat.ChatAIFragment;
import all.muslimlife.view.map.MapAllFragment;
import all.muslimlife.view.migrants.MigrantListFragment;
import all.muslimlife.view.qrcode.QrCodeFragment;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import com.muslimlife.app.view.SplashFragment;
import com.muslimlife.app.view.about.AboutFragment;
import com.muslimlife.app.view.answer.ImamAnswerFragment;
import com.muslimlife.app.view.auth.AuthFragment;
import com.muslimlife.app.view.auth.ConfirmCodeFragment;
import com.muslimlife.app.view.auth.NewAuthFragment;
import com.muslimlife.app.view.auth.RegProfileFragment;
import com.muslimlife.app.view.compass.CompassFragment;
import com.muslimlife.app.view.dialogs.ChooseKindDialog;
import com.muslimlife.app.view.diaspor.DiasporFragment;
import com.muslimlife.app.view.imam.ImamFragment;
import com.muslimlife.app.view.koran.KoranFragment;
import com.muslimlife.app.view.main.MainFragment;
import com.muslimlife.app.view.map.MapFragment;
import com.muslimlife.app.view.map.MapGPlacesFragment;
import com.muslimlife.app.view.message.MessageFragment;
import com.muslimlife.app.view.message.PageFragment;
import com.muslimlife.app.view.prayer.PrayerFragment;
import com.muslimlife.app.view.profile.ProfileFragment;
import com.muslimlife.app.view.radio.RadioFragment;
import com.muslimlife.app.view.reader.ReaderFragment;
import com.muslimlife.app.view.sermons.SermonsFragment;
import com.muslimlife.app.view.sub.SubFragment;
import com.muslimlife.app.view.support.SupportFragment;
import com.muslimlife.app.view.suri.SuriFragment;
import com.muslimlife.app.view.tasbih.TasbihFragment;
import com.muslimlife.app.view.zakyat.ZakyatFragment;
import com.muslimlife.app.view.zakyat.ZakyatInfoFragment;

@Module
interface MainFragmentInjector {

    @ContributesAndroidInjector
    SplashFragment provideSplashFragment();

    @ContributesAndroidInjector
    AuthFragment provideAuthFragment();

    @ContributesAndroidInjector
    ConfirmCodeFragment provideConfirmCodeFragment();

    @ContributesAndroidInjector
    MainFragment provideMainFragment();

    @ContributesAndroidInjector
    RegProfileFragment provideRegProfileFragment();

    @ContributesAndroidInjector
    SettingsFragment provideSettingsFragment();

    @ContributesAndroidInjector
    ProfileFragment provideProfileFragment();

    @ContributesAndroidInjector
    SubFragment provideSubFragment();

    @ContributesAndroidInjector
    DonationFragment provideDonationFragment();

    @ContributesAndroidInjector
    SupportFragment provideSupportFragment();

    @ContributesAndroidInjector
    ServicesFragment provideServicesFragment();

    @ContributesAndroidInjector
    LanguageFragment provideLanguageFragment();

    @ContributesAndroidInjector
    AboutFragment provideAboutFragment();

    @ContributesAndroidInjector
    MessageFragment provideMessageFragment();

    @ContributesAndroidInjector
    ImamAnswerFragment provideImamAnswerFragment();

    @ContributesAndroidInjector
    ZakyatFragment provideZakyatFragment();

    @ContributesAndroidInjector
    QuastionFragment provideQuastionFragment();

    @ContributesAndroidInjector
    ImamFragment provideImamFragment();

    @ContributesAndroidInjector
    ImamQuastionFragment provideImamQuastionFragment();

    @ContributesAndroidInjector
    CompassFragment provideCompassFragment();

    @ContributesAndroidInjector
    PageFragment provideViewPagerAdapterFragment();

    @ContributesAndroidInjector
    ChooseKindDialog provideChooseKindDialog();

    @ContributesAndroidInjector
    PrayerFragment providePrayerFragment();

    @ContributesAndroidInjector
    DiasporFragment provideDiasporFragment();

    @ContributesAndroidInjector
    DiaspoarQuastionFragment provideDiaspoarQuastionFragment();

    @ContributesAndroidInjector
    TasbihFragment provideViewTasbihFragment();

    @ContributesAndroidInjector
    KoranFragment provideKoranFragment();

    @ContributesAndroidInjector
    KoranSettingsFragment provideKoranSettingsFragment();

    @ContributesAndroidInjector
    SuriFragment provideSuriFragment();

    @ContributesAndroidInjector
    TranslateFragment provideTranslateFragment();

    @ContributesAndroidInjector
    ReaderFragment provideReaderFragment();

    @ContributesAndroidInjector
    BookmarkFragment provideBookmarkFragment();

    @ContributesAndroidInjector
    NotificationFragment provideNotificationFragment();

    @ContributesAndroidInjector
    RadioFragment provideRadioFragment();

    @ContributesAndroidInjector
    TvFragment provideTvFragment();

    @ContributesAndroidInjector
    DiaspoarsFragment provideDiaspoarsFragment();

    @ContributesAndroidInjector
    WallpaperFragment provideWallpaperFragment();

    @ContributesAndroidInjector
    ChoosedWallpaperFragment provideChoosedWallpaperFragment();

    @ContributesAndroidInjector
    SermonsFragment provideSermonsFragment();

    @ContributesAndroidInjector
    MapFragment provideMapFragment();

    @ContributesAndroidInjector
    QuastionsFragment provideAnswersFragment();

    @ContributesAndroidInjector
    QuastionsAnswerFragment provideQuastionAnswerFragment();

    @ContributesAndroidInjector
    ImageFragment provideImageFragment();


    @ContributesAndroidInjector
    ZakyatSettingsFragment provideZakyatSettingsFragment();

    @ContributesAndroidInjector
    ZakyatSettingsFondsFragment provideZZakyatSettingsFondsFragment();

    @ContributesAndroidInjector
    ZakyatSettingsCurrencyFragment provideZZakyatSettingsCurrencyFragment();

    @ContributesAndroidInjector
    PushSettingsFragment providePushFragment();

    @ContributesAndroidInjector
    NewAuthFragment provideNewAuthFragment();

    @ContributesAndroidInjector
    ZakyatInfoFragment provideZakyatInfoFragment();

    @ContributesAndroidInjector
    MapGPlacesFragment provideMapGPlacesFragment();

    @ContributesAndroidInjector
    SubRuStoreFragment provideSubRuStoreFragment();

    @ContributesAndroidInjector
    AzkarListFragment provideAzkarListFragment();

    @ContributesAndroidInjector
    AzkarFragment provideAzkarFragment();

    @ContributesAndroidInjector
    LanguageStartFragment provideLanguageStartFragment();

    @ContributesAndroidInjector
    MLCoinBillingNewFragment provideMLCoinBillingNewFragment();

    @ContributesAndroidInjector
    MigrantListFragment provideMigrantListFragment();

    @ContributesAndroidInjector
    QrCodeFragment provideQrCodeFragment();

    @ContributesAndroidInjector
    MapAllFragment provideMapAllFragment();

    @ContributesAndroidInjector
    ChatAIFragment provideChatAIFragment();
}
