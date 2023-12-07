package com.muslimlife.app.view;

import static com.muslimlife.app.utils.Constants.IS_USER;
import static com.muslimlife.app.utils.Constants.LANGUAGE;
import static com.muslimlife.app.utils.Constants.MAIN_BOTTOM_DIALOG_OTHER;
import static com.muslimlife.app.utils.Constants.MAIN_BOTTOM_DIALOG_WORSHIP;
import static com.muslimlife.app.utils.Constants.MENU_ANSWER;
import static com.muslimlife.app.utils.Constants.MENU_MAIN;
import static com.muslimlife.app.utils.Constants.MENU_MAP;
import static com.muslimlife.app.utils.Constants.MENU_OFF;
import static com.muslimlife.app.utils.Constants.MENU_OTHER;
import static com.muslimlife.app.utils.Constants.MENU_WORSHIP;
import static com.muslimlife.app.utils.Constants.SOUNDS;
import static com.muslimlife.app.utils.Constants.SOUND_1;
import static com.muslimlife.app.utils.Constants.SOUND_2;
import static com.muslimlife.app.utils.Constants.SOUND_3;
import static com.muslimlife.app.utils.Constants.SOUND_4;
import static com.muslimlife.app.utils.Constants.SOUND_5;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import all.muslimlife.data.repository.WebPayRepository;
import all.muslimlife.services.InternetConnectionService;
import dagger.android.support.DaggerAppCompatActivity;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import ru.rustore.sdk.billingclient.RuStoreBillingClient;

import com.muslimlife.app.R;
import com.muslimlife.app.data.model.GetQuastionsReq;
import com.muslimlife.app.data.model.GetQuastionsRes;
import com.muslimlife.app.data.model.KoranTestRes;
import com.muslimlife.app.data.model.SubscribeReq;
import com.muslimlife.app.data.model.TokenUpdateReq;
import com.muslimlife.app.data.model.UserProfile;
import com.muslimlife.app.data.model.surah.SurahRes;
import com.muslimlife.app.data.repository.AzkarsRepository;
import com.muslimlife.app.data.repository.UserRepository;
import com.muslimlife.app.databinding.ActivityMainBinding;
import com.muslimlife.app.di.viewModel.DaggerViewModelFactory;
import com.muslimlife.app.notification.AlarmBrodcast;
import com.muslimlife.app.utils.BillingUtil;
import com.muslimlife.app.utils.CacheHelper;
import com.muslimlife.app.utils.Constants;
import com.muslimlife.app.utils.MyContextWrapper;
import com.muslimlife.app.view.mlcoin.MLCoinBillingListener;
import com.muslimlife.app.view.mlcoin.MLCoinBillingModule;
import com.muslimlife.app.view.sub_ru_store.RuStoreListener;
import com.muslimlife.app.view.sub_ru_store.RuStoreModule;
import com.muslimlife.app.viewmodel.main.MainViewModel;

public class MainActivity extends DaggerAppCompatActivity implements MLCoinBillingListener {

    private ActivityMainBinding binding;
    public BottomNavigationView bnm;
    public NavController navController;

    @Inject
    UserRepository userRepository;

    @Inject
    public AzkarsRepository azkarsRepository;

    @Inject
    public WebPayRepository webPayRepository;

    @Inject
    public RuStoreBillingClient ruStoreBillingClient;

    public BillingUtil billingUtil;
    public MLCoinBillingModule billingCoin;

    public SharedPreferences sharedPref;

    @Inject
    DaggerViewModelFactory viewModelFactory;
    private MainViewModel viewModel;

    private UserProfile userProfile;

    InternetConnectionService internetConnectionService;


    private MutableLiveData<Boolean> isConnectedData = new MutableLiveData<>();
    boolean isConnected = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        bnm = binding.bottomNavigationView;

        Context context = MyContextWrapper.wrap(this, getSharedPreferences("muslimLife.lang", MODE_PRIVATE).getString(Constants.LANGUAGE,Constants.DEFAULT_LANG));
        getResources().updateConfiguration(context.getResources().getConfiguration(), context.getResources().getDisplayMetrics());

        initViewModel();
        initUserProfileObserve();

        NavHostFragment navHostFragment = (NavHostFragment)getSupportFragmentManager()
                .findFragmentById(R.id.nav_host);
        NavigationUI.setupWithNavController(bnm,
                navHostFragment.getNavController());
        createChannel();
        changeLanguage(false);

        //billingUtil = new BillingUtil(this, this);

        navController = navHostFragment.getNavController();

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            try{
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
                if(destination.getId() == R.id.ImageFragment){
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
                }
            }catch (Exception ignored){}

        });

        loadSurahs();

        //initInternetCheck();
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            InternetConnectionService.LocalBinder binder = (InternetConnectionService.LocalBinder) service;
            internetConnectionService = binder.getService();

            internetConnectionService.setInternetConnectionObserver(isConnected -> {
                showInternetState(isConnected);
            });

            internetConnectionService.checkInternetConnectionAsync();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            internetConnectionService = null;

            // Логика при разрыве соединения с сервисом
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        bindService(new Intent(this, InternetConnectionService.class), serviceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(serviceConnection);
    }

    @Override
    protected void onResume() {
        super.onResume();

        checkSub();
    }

    private void checkSub(){
        new RuStoreModule(ruStoreBillingClient, new RuStoreListener() {
            @Override
            public void cancelPurchases() {
                setSubscribe(false);
            }

            @Override
            public void confirmedPurchases() {
                setSubscribe(true);
            }
        }).getPurchase();
    }

    private void showInternetState(boolean isConnected){
        isConnectedData.postValue(isConnected);
        this.isConnected = isConnected;
        runOnUiThread(() -> {
            if (isConnected) {
                binding.internetState.setVisibility(View.GONE);
            } else {
                binding.internetState.setVisibility(View.VISIBLE);
            }
        });
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this, viewModelFactory).get(MainViewModel.class);
    }

    private void initUserProfileObserve() {
        viewModel.getUserProfileLiveData().observe(this, this::init);
    }

    void init(UserProfile userProfile) {
        showProgress(false);
        this.userProfile = userProfile;

        if(billingCoin == null) {
            billingCoin = new MLCoinBillingModule(ruStoreBillingClient, this);
            billingCoin.getPurchase();
        }

        webPayRepository.setUserData(userProfile.getId(), userProfile.getEmail());
        webPayRepository.getMLCoinCount();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        SharedPreferences sharedPref = newBase.getSharedPreferences("muslimLife.lang", Context.MODE_PRIVATE);
        String lang = sharedPref.getString(Constants.LANGUAGE, Constants.DEFAULT_LANG);
        super.attachBaseContext(MyContextWrapper.wrap(newBase, lang));
    }

    void loadSurahs(){
        ArrayList<SurahRes> surahList = CacheHelper.loadSurah(this);

        if(surahList != null && !surahList.isEmpty()){
            Log.d("LOAD_QURAN", "loadSurahs local");
            setSurahs(surahList);
            loadPages();
        }else{
            Log.d("LOAD_QURAN", "loadSurahs rest start");
            userRepository.getSurahs()
                    .subscribe(surahItems -> {
                        Log.d("LOAD_QURAN", "loadSurahs rest end");
                        CacheHelper.saveSurahs(this, surahItems);
                        setSurahs(surahItems);
                        loadPages();
                    }, throwable -> Log.d("LOAD_QURAN", "getSurahs error: " + throwable.getMessage()));
        }
    }

    void setSurahs(ArrayList<SurahRes> surahs){
        userRepository.setSurahList(surahs);
        userRepository.getSurahListMutableLiveData().postValue(surahs);
    }

    void loadPages(){
        ArrayList<KoranTestRes> quranList = CacheHelper.loadQuranPages(this);

        if(quranList != null && !quranList.isEmpty()){
            setPages(quranList);
            Log.d("LOAD_QURAN", "loadPages local");
        }else{
            Log.d("LOAD_QURAN", "loadPages rest start");
            userRepository.getKoranPages()
                    .subscribe(koranPages -> {
                        Log.d("LOAD_QURAN", "loadPages rest end");
                        parsePages(koranPages);
                    }, throwable -> Log.d("LOAD_QURAN", "getKoranPages error: " + throwable.getMessage()));
        }
    }

    private void parsePages(ArrayList<KoranTestRes> koranPages){
        try {
            Map<Integer, KoranTestRes> resMap = new LinkedHashMap<>();
            for (KoranTestRes koranPage : koranPages) {
                resMap.put(Integer.parseInt(koranPage.getNumber()), koranPage);
            }
            koranPages = new ArrayList<>(resMap.values());

            ArrayList<KoranTestRes> temp = new ArrayList<>();

            int pagePosition = 0;
            for (int i = 0; i < 604; i++) {
                if (pagePosition < koranPages.size() && Integer.parseInt(koranPages.get(pagePosition).getNumber()) == i + 1) {
                    temp.add(koranPages.get(pagePosition));
                    pagePosition++;
                } else {
                    KoranTestRes t = new KoranTestRes();
                    t.setNumber(String.valueOf(i + 1));
                    t.setPage("!онедйан_ен_ыцинартс" + "\n" +
                            "!онедйан_ен_ыцинартс" + "\n" +
                            "!онедйан_ен_ыцинартс" + "\n" +
                            "!онедйан_ен_ыцинартс" + "\n" +
                            "!онедйан_ен_ыцинартс" + "\n" +
                            "!онедйан_ен_ыцинартс" + "\n" +
                            "!онедйан_ен_ыцинартс" + "\n" +
                            "!онедйан_ен_ыцинартс" + "\n" +
                            "!онедйан_ен_ыцинартс" + "\n" +
                            "!онедйан_ен_ыцинартс" + "\n" +
                            "!онедйан_ен_ыцинартс" + "\n" +
                            "!онедйан_ен_ыцинартс" + "\n" +
                            "!онедйан_ен_ыцинартс" + "\n" +
                            "!онедйан_ен_ыцинартс" + "\n" +
                            "!онедйан_ен_ыцинартс");
                    t.setStartAyah(0);
                    t.setSurahNumber("0");
                    t.setSymbols("");
                    temp.add(t);
                }
            }

            temp.add(0, new KoranTestRes());
            temp.add(new KoranTestRes());

            CacheHelper.saveQuranPages(this, temp);
            setPages(temp);
            //this.koranPages.addAll(koranPages);
        } catch (Exception e) {
            Log.e("KoranFragment", e.getMessage());
        }
    }

    void setPages(ArrayList<KoranTestRes> koranPages){
        userRepository.setKoranPagesList(koranPages);
        userRepository.getKoranPagesMutableLiveData().postValue(koranPages);
    }

    void createChannel(){
        for(String sound : Constants.SOUNDS){
            int raw = -1;
            if(sound.equals(Constants.SOUND_1)){
                raw =  R.raw.first;
            }
            if(sound.equals(Constants.SOUND_2)){
                raw = R.raw.second;
            }
            if(sound.equals(Constants.SOUND_3)){
                raw = R.raw.thirt_push_sound;
            }
            if(sound.equals(Constants.SOUND_4)){
                raw = R.raw.forth_push_sound;
            }
            if(sound.equals(Constants.SOUND_5)){
                raw = R.raw.fith_push_sound;
            }
            if(raw != -1) {
                Uri soundUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + raw);
                NotificationChannel mChannel;
                mChannel = new NotificationChannel(sound, sound, NotificationManager.IMPORTANCE_HIGH);
                mChannel.setLightColor(Color.GRAY);
                mChannel.enableLights(true);
                mChannel.setDescription(sound);
                AudioAttributes audioAttributes = new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .build();
                mChannel.setSound(soundUri, audioAttributes);

                NotificationManager notificationManager =
                        (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.createNotificationChannel(mChannel);
            } else {
                Log.e("ERROR", "Not found raw");
            }
        }
    }

    public void changeLanguage(boolean restart){
        Locale locale = new Locale(getSharedPreferences("muslimLife.lang", MODE_PRIVATE).getString(Constants.LANGUAGE,Constants.DEFAULT_LANG));
        //Locale.setDefault(locale);
        //Resources resources = getResources();
        //Configuration config = resources.getConfiguration();
        //config.setLocale(locale);
        //resources.updateConfiguration(config, resources.getDisplayMetrics());
        //SharedPreferences sharedPref = getSharedPreferences("muslimLife.lang", Context.MODE_PRIVATE);
        userRepository.setLanguage(locale.getLanguage());
        //sharedPref.edit().putString(LANGUAGE, locale.getLanguage()).apply();

        if(restart) {
            Intent intent = getIntent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            finish();
            startActivity(intent);
        }
    }

    /*public void setAlarmDay(PrayerTimeUtil prayerTimeUtil) {

        @SuppressLint("SimpleDateFormat") DateFormat formatter = new SimpleDateFormat("d-M-yyyy hh:mm");

        sharedPref = getPreferences(Context.MODE_PRIVATE);
        String sharedDate = sharedPref.getString("TEST_NOFITI_DATE", "");
        StringBuilder currentDate = new StringBuilder();

        List<Calendar> nextDateList = getNextDate();

        long curr = Calendar.getInstance().getTimeInMillis();

        for (Calendar next : nextDateList) {
            String date = DateUtil.getDateForNotification(next.getTime());
            currentDate.append(date).append("/");
            if (!sharedDate.contains(date)) {
                List<String> times = prayerTimeUtil.getTimesOnDate(next);
                for (int i = 0; i < times.size(); i++)
                    try {
                        Date tempDate = formatter.parse(date + " " + times.get(i));
                        if (tempDate.getTime() > curr) {
                            Log.i("setAlarmDay", prayerTimeUtil.names[i] + " " + date + " " + times.get(i) + " " + new Random().nextInt(100000));
                            setAlarmTest(prayerTimeUtil.names[i], date, times.get(i), new Random().nextInt(100000));
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
            }
        }

        Log.i("setAlarmDay", currentDate.toString());
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("TEST_NOFITI_DATE", currentDate.toString());
        editor.apply();
    }*/

    private List<Calendar> getNextDate() {
        List<Calendar> nextDate = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, i);
            calendar.set(Calendar.HOUR, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 1);
            nextDate.add(calendar);
        }

        return nextDate;
    }

    private void setAlarmTest(int text, String date, String time, int id) {
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(getApplicationContext(), AlarmBrodcast.class);
        intent.putExtra("event", getString(text));
        intent.putExtra("time", time);
        intent.putExtra("date", date);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), id, intent, PendingIntent.FLAG_ONE_SHOT);
        String dateandtime = date + " " + time;
        DateFormat formatter = new SimpleDateFormat("d-M-yyyy hh:mm");
        try {
            Date date1 = formatter.parse(dateandtime);
            am.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, date1.getTime(), pendingIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setMenu(int type) {
        bnm.setOnItemSelectedListener(null);
        bnm.setVisibility(View.VISIBLE);
        if(type == Constants.MENU_OFF)
            bnm.setVisibility(View.GONE);
        else
            bnm.setSelectedItemId(type);

        bnm.getOrCreateBadge(Constants.MENU_ANSWER).setBackgroundColor(this.getColor(R.color.yelow_message));
        bnm.setOnItemSelectedListener(menuItem -> {
            Bundle bundle = new Bundle();
            switch (menuItem.getItemId()){
                case Constants.MENU_MAIN:
                    navController.navigate(R.id.mainFragment);
                    break;
                case Constants.MENU_MAP:
                    if(isConnected) {
                        bundle.putInt("filterMap", 0);
                        navController.navigate(R.id.mapGPlacesFragment, bundle);
                    }else {
                        Toast.makeText(this, getString(R.string.off_internet_desc), Toast.LENGTH_SHORT).show();
                    }
                    break;
                case Constants.MENU_WORSHIP:
                    bundle.putString(Constants.MAIN_BOTTOM_DIALOG_TYPE, Constants.MAIN_BOTTOM_DIALOG_WORSHIP );
                    navController.navigate(R.id.bottomWorshipDialog, bundle);
                    break;
                case Constants.MENU_OTHER:
                    if(isConnected) {
                        bundle.putString(Constants.MAIN_BOTTOM_DIALOG_TYPE, Constants.MAIN_BOTTOM_DIALOG_OTHER);
                        navController.navigate(R.id.bottomWorshipDialog, bundle);
                    }else{
                        Toast.makeText(this, getString(R.string.off_internet_desc), Toast.LENGTH_SHORT).show();
                    }
                    break;
                case Constants.MENU_ANSWER:
                    checkQuastions();
                    navController.navigate(R.id.AnswersFragment);
                    break;
            }
            return false;
        });
    }

    public void checkQuastions(){
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        GetQuastionsReq getQuastionsReq = new GetQuastionsReq(sharedPref.getString(Constants.SP_KEY_USER_ID, "0"));
        userRepository.getQuastions(getQuastionsReq).subscribe(new SingleObserver<>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onSuccess(@NonNull GetQuastionsRes[] getQuastionsRes) {
                for (GetQuastionsRes quastionsRes : getQuastionsRes)
                    if (quastionsRes.getAnswered().equals("0")) {
                        bnm.getOrCreateBadge(Constants.MENU_ANSWER).setVisible(true);
                        return;
                    } else
                        bnm.getOrCreateBadge(Constants.MENU_ANSWER).setVisible(false);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                bnm.getOrCreateBadge(Constants.MENU_ANSWER).setVisible(false);
            }
        });
    }

    public void checkUser(){
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        boolean checkIfImam = sharedPref.getBoolean(Constants.IS_USER, false);
        if(!checkIfImam)
            bnm.getMenu().removeItem(Constants.MENU_ANSWER);

        try{
            int id = Integer.parseInt(sharedPref.getString(Constants.SP_KEY_USER_ID, "0"));
            if(id!=0)
                updateToken(id);
        }catch (Exception e){
            Log.e("getIdError",e.getMessage());
        }
    }

    public void setSubscribe(boolean sub){
        userRepository.setSubscribe(sub);
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        String id = sharedPref.getString(Constants.SP_KEY_USER_ID, "0");
        userRepository.setSubscribe(new SubscribeReq(id,sub)).subscribe(new SingleObserver<>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(Boolean aBoolean) {
                viewModel.loadUserProfile(getUserId());
            }

            @Override
            public void onError(Throwable e) {
                Log.e("SubscribeUpdate", e.getMessage());
            }
        });
    }

    private String getUserId() {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getString(Constants.SP_KEY_USER_ID, "0");
    }

    public void updateToken(int id){
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(
                task -> runOnUiThread(()->{
                    if (task.isSuccessful()) {
                        String token = task.getResult();
                        userRepository.updateToken(new TokenUpdateReq(id, token)).subscribe(new SingleObserver<>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                            }

                            @Override
                            public void onSuccess(Boolean aBoolean) {
                                Log.d("tokenSend", aBoolean.toString());
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e("TokenFirebaseSend", e.getMessage());
                            }
                        });
                    }
                })
        );
    }

    //@Override
    public void showProgress(boolean show) {
        binding.progress.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    public void startBuyCoin(String productId){
        binding.progress.setVisibility(View.VISIBLE);
        billingCoin.purchaseProduct(productId);
    }

    @Override
    public void buyCoinSuccess(String productId) {
        webPayRepository.updateMLCoin(MLCoinBillingModule.Companion.getCoinCountByProductId(productId));
    }

    public boolean isConnected() {
        return isConnected;
    }

    public MutableLiveData<Boolean> getIsConnectedData() {
        return isConnectedData;
    }
}

