package all.muslimlife.view.migrants;

import static com.muslimlife.app.utils.Constants.MENU_OFF;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import javax.inject.Inject;

import all.muslimlife.view.migrants.adapters.MigrantsAdapter;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import all.muslimlife.data.model.migrants.MigrantsRes;
import com.muslimlife.app.data.repository.AzkarsRepository;
import com.muslimlife.app.databinding.FragmentMigrantListBinding;
import com.muslimlife.app.view.MainActivity;
import com.muslimlife.app.view.adapters.OnItemSelectListener;
import com.muslimlife.app.view.base.BaseFragment;

public class MigrantListFragment extends BaseFragment implements OnItemSelectListener {

    private FragmentMigrantListBinding binding;

    @Inject
    AzkarsRepository azkarsRepository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMigrantListBinding.inflate(inflater, container, false);
        ((MainActivity) requireActivity()).setMenu(MENU_OFF);

        initView();

        return binding.getRoot();
    }

    private void initView(){
        binding.backButton.setOnClickListener(v-> Navigation.findNavController(requireView()).popBackStack());

        if(azkarsRepository.getMigrantsRes() == null){
            azkarsRepository.getMigrants().subscribe(new SingleObserver<>() {
                @Override
                public void onSubscribe(@NonNull Disposable d) {

                }

                @Override
                public void onSuccess(@NonNull MigrantsRes migrantsRes) {
                    azkarsRepository.setMigrantsRes(migrantsRes);
                    initRecycler();
                }

                @Override
                public void onError(@NonNull Throwable e) {
                }
            });
        }else{
            initRecycler();
        }
    }

    private void initRecycler(){
        if(azkarsRepository.getMigrantsRes().getOffices() != null) {
            MigrantsAdapter adapter = new MigrantsAdapter(azkarsRepository.getMigrantsRes().getOffices(), this);
            binding.migrantsRecycler.setAdapter(adapter);
        }
    }

    @Override
    public void showError(String error) {

    }

    @Override
    public void onItemSelect(int position) {

    }

    @Override
    public void callPhone(String phone) {
        Intent dialIntent = new Intent(Intent.ACTION_DIAL);
        dialIntent.setData(Uri.parse("tel:" + phone));

        if (dialIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivity(dialIntent);
        }
    }

    @Override
    public void sendEmail(String email) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        emailIntent.setType("message/rfc822");

        startActivity(Intent.createChooser(emailIntent, "Choose an Email client :"));
    }

    @Override
    public void openWebsite(String website) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(website));
        startActivity(browserIntent);
    }
}
