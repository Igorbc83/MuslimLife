package com.muslimlife.app.view.about;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import com.muslimlife.app.utils.Constants;
import com.muslimlife.app.view.MainActivity;
import com.muslimlife.app.view.base.BaseFragment;

import com.muslimlife.app.R;
import com.muslimlife.app.databinding.FragmentAboutBinding;
import com.muslimlife.app.databinding.FragmentProfileBinding;

public class AboutFragment extends BaseFragment {
    private FragmentAboutBinding binding;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAboutBinding.inflate(inflater, container, false);
        ((MainActivity)requireActivity()).setMenu(Constants.MENU_OFF);
        binding.backButton.setOnClickListener(view->{
            Navigation.findNavController(requireView()).popBackStack();
        });

        try {
            PackageInfo pInfo = requireContext().getPackageManager().getPackageInfo(requireContext().getPackageName(), 0);
            binding.aboutText.setText("\n" + requireContext().getString(R.string.about) + pInfo.versionName+"\n\n" + "Приложение Muslim Life разработано для мусульман и содержит разделы: Намаз и Кибла, Коран (достоверный Кираат), Ислам (образование, вопросы, мечети), Халяль (еда, кафе, доставка).\n" +
                    "\n" +
                    "Muslim Life благоверным мусульманином, почитающим Ислам:\n" +
                    "\n" +
                    "1. Намаз. Приложение Muslim Life напомнит Вам точное время намаза. Чтoбы paccчитaть время мoлитв для ĸoнĸpeтнoгo мecтa, приложение определяет шиpoтy и дoлгoтy мecтoпoлoжeния пользователя, а также чacoвoй пoяc. Приложение заранее уведомит Вас о времени намаза.\n" +
                    "\n" +
                    "2. Кибла. Встроенный компас покажет направление на Киблу и подаст вибрационный сигнал, как только Вы примете правильное положение.\n" +
                    "\n" +
                    "3. Коран. Главным достоинством приложения Muslim Life является раздел Коран, суры в котором читают профессиональные чтецы. Приложение Muslim Life содержит перевод коранического арабского текста на русский язык.\n" +
                    "\n" +
                    "4. Тасбих. Приложение Muslim Life содержит опцию \"Тасбих\" - электронные четки для повторения определенных фраз после намаза, которое дошло до нас из Сунны Пророка Мухаммада.\n" +
                    "\n" +
                    "5. Ислам. В приложении мусульмане обоих полов и всех возрастов могут задать вопрос имаму. В настоящее время представлены эксперты по следующим вопросам: Намаз. Принятие Ислама. Акида. Грехи и прощение. Женские вопросы. Мужские вопросы. Обряды и традиции Ислама.\n" +
                    "\n" +
                    "6. Ислам - поклонение и образование. ТВ и радиоканалы мира Ислама. 24 часа в сутки идут прямые трансляции из Мекки и из мечети Аль-Масджид ан-Набави. Работает также специальный ТВ-канал Muslim Life TV. Он рассказывает о жизни мусульман в разных регионах России. В скором будущем планируется открытие Школы Ислама для детей.\n" +
                    "\n" +
                    "7. Халяль. Приложение Muslim Life содержит карту, показывающую ближайшие к Вам халяльные кафе, рестораны и магазины. В будущем планируется доставка халяльной еды и продукции халяль.\n" +
                    "\n" +
                    "Приложение доступно на русском, узбекском, таджикском, киргизском, турецком, английском и французском языках.\n" +
                    "Muslim Life предоставляет каждому пользователю возможность индивидуальной настройки - Вы можете изменять функционал - подключать сервисы, которые нужны лично Вам и отключать ненужные.\n" +
                    "Намаз, Коран, Кибла, Ислам образование и ТВ — всё, что нужно правоверному мусульманину содержится в нашем приложении.");



        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }



        return binding.getRoot();
    }

    @Override
    public void showError(String error) {
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
    }
}
