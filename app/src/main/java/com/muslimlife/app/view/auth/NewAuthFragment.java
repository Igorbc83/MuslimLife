package com.muslimlife.app.view.auth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.muslimlife.app.di.viewModel.DaggerViewModelFactory;
import com.muslimlife.app.utils.Constants;
import com.muslimlife.app.view.base.BaseFragment;
import com.muslimlife.app.viewmodel.auth.AuthViewModel;

import javax.inject.Inject;

import com.muslimlife.app.R;
import com.muslimlife.app.databinding.FragmentNewAuthBinding;


public class NewAuthFragment extends BaseFragment {

    @Inject
    DaggerViewModelFactory viewModelFactory;
    private FragmentNewAuthBinding binding;
    private AuthViewModel viewModel;
    private GoogleSignInClient mGoogleSignInClient;

    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                        try {
                            GoogleSignInAccount account = task.getResult(ApiException.class);
                            viewModel.firebaseAuthWithGoogle(account.getIdToken());
                            viewModel.checkUserInApi(account.getEmail(),requireActivity());
                        } catch (ApiException e) {
                            Toast.makeText(requireContext(), "Google sign in failed" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.buttonGoogleAccount.setOnClickListener(onClickListener);
        binding.buttonSkip.setOnClickListener(onClickListener);
        binding.buttonAppleId.setOnClickListener(onClickListener);
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(requireActivity(), viewModelFactory).get(AuthViewModel.class);
    }

    private void createRequest() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(Constants.DEFAULT_WEB_CLIENT_ID)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), gso);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNewAuthBinding.inflate(inflater, container, false);
        getUserEmail();
        initViewModel();
        initObservables();
        createRequest();
        if (getUserEmail()!=null) viewModel.checkUserInApi(getUserEmail(), requireActivity());

        binding.policyCheck.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.POLICY_CHECK));
            startActivity(browserIntent);
        });

        return binding.getRoot();
    }

    private void initObservables() {
        // Моментальная авторизация, подтверждение с помощью кода не требуется, сразу уходим на основной экран
        viewModel.getUserProfileLiveData().observe(getViewLifecycleOwner(), userProfile -> {
            if (FirebaseAuth.getInstance().getCurrentUser() != null)
                if (userProfile != null && userProfile.getId() != null) {
                    saveUserId(userProfile.getId());
                    Navigation.findNavController(requireView()).navigate(R.id.mainFragment);
                }else{
                    Navigation.findNavController(requireView()).navigate(R.id.regProfileFragment);
                }
        });

        viewModel.getUserLiveData().observe(getViewLifecycleOwner(), firebaseUser -> {
            if (FirebaseAuth.getInstance().getCurrentUser() != null)
                if (!firebaseUser.isAnonymous() && getUserEmail()!=null)
                    Navigation.findNavController(requireView()).navigate(R.id.mainFragment);
        });

        viewModel.getErrorLiveData().observe(getViewLifecycleOwner(), this::showError);
    }

    private void setLoading(boolean isLoading) {
        if (isLoading)
            binding.progressBar.setVisibility(View.VISIBLE);
        else
            binding.progressBar.setVisibility(View.GONE);
    }

    private String getUserEmail() {
        SharedPreferences sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getString(Constants.SP_KEY_USER_EMAIL, null);
    }

    private void saveUserId(String userId) {
        SharedPreferences sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Constants.SP_KEY_USER_ID, userId);
        editor.apply();
    }

    View.OnClickListener onClickListener = v -> {
        switch (v.getId()) {
            case R.id.buttonGoogleAccount:
                if (!binding.checkedPolitics.isChecked()) {
                    showError(requireContext().getString(R.string.licenseError));
                    return;
                }
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                mStartForResult.launch(signInIntent);
             //   initObservables();
                break;
            case R.id.buttonSkip:
                viewModel.skipAuth();
                Navigation.findNavController(requireView()).navigate(R.id.mainFragment);
               // launchMainFragment();
                break;
        }
    };

    @Override
    public void showError(String error) {
        setLoading(false);
        Toast.makeText(getContext(),
                error,
                Toast.LENGTH_SHORT).show();
    }
}