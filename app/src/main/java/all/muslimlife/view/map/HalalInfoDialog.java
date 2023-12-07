package all.muslimlife.view.map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.muslimlife.app.databinding.DialogHalalInfoBinding;

public class HalalInfoDialog  extends DialogFragment {
    private DialogHalalInfoBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogHalalInfoBinding.inflate(inflater, container, false);

        binding.dialogClose.setOnClickListener(v -> dismiss());

        return binding.getRoot();
    }
}
