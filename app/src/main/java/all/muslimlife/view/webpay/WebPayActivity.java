package all.muslimlife.view.webpay;

import static com.muslimlife.app.utils.Constants.BUNDLE_WEB_PAY_TYPE;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;


import com.muslimlife.app.databinding.ActivityWebPayBinding;

import dagger.android.support.DaggerAppCompatActivity;

public class WebPayActivity extends DaggerAppCompatActivity {

    ActivityWebPayBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityWebPayBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if(getIntent().getStringExtra(BUNDLE_WEB_PAY_TYPE) != null){
            String url = getIntent().getStringExtra(BUNDLE_WEB_PAY_TYPE);
            setWebWork(url);
        }else{
            orderFinish(false);
        }
    }
    private void setWebWork(String url){
        binding.paymentView.setVisibility(View.VISIBLE);
        binding.paymentView.getSettings().setLoadsImagesAutomatically(true);
        binding.paymentView.getSettings().setJavaScriptEnabled(true);
        binding.paymentView.getSettings().setLoadWithOverviewMode(true);
        binding.paymentView.getSettings().setUseWideViewPort(true);
        binding.paymentView.getSettings().setAllowFileAccess(true);
        binding.paymentView.getSettings().setAllowContentAccess(true);
        binding.paymentView.getSettings().setDomStorageEnabled(true);

        binding.paymentView.loadUrl(url);
        binding.paymentView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                if(url.toLowerCase().indexOf("success=true") > 0){
                    Toast.makeText(view.getContext(),"Оплата прошла успешно!",Toast.LENGTH_LONG).show();
                    finish();
                }
            }

           /*@Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

                if(url.toLowerCase().indexOf("success=true") > 0){
                    Toast.makeText(view.getContext(),"Оплата прошла успешно!",Toast.LENGTH_LONG).show();
                    finish();
                }
            }*/
        });
    }

    private void orderFinish(boolean finish){
        if(finish) {
            Bundle bundle = new Bundle();
            bundle.putBoolean(BUNDLE_WEB_PAY_TYPE, finish);
            //Navigation.findNavController(requireView()).navigate(R.id.action_basketPayFragment_to_mainFragment, bundle);
        }else{
            //Navigation.findNavController(requireView()).popBackStack();
            //Navigation.findNavController(requireView()).navigate(R.id.orderErrorDialog);
        }
    }
}
