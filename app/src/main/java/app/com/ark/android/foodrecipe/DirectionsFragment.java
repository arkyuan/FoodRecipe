package app.com.ark.android.foodrecipe;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

/**
 * Created by hp1 on 21-01-2015.
 */
public class DirectionsFragment extends Fragment {

    static final String DIRECTIONURL = "DIRECTION";
    String mDirection_url;
    WebView mDirection_webView;
    TextView mEmptyView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.directions,container,false);
        mDirection_webView = (WebView) rootView.findViewById(R.id.direction_webview);
        mEmptyView = (TextView) rootView.findViewById(R.id.direction_empty);
        Bundle arguments = getArguments();
        if(arguments!=null){
            mDirection_url = arguments.getString(DirectionsFragment.DIRECTIONURL);
        }


        return rootView;
    }

    public void onTab(String mRecipeUrlData) {
        mDirection_url=mRecipeUrlData;
        if(mDirection_url!=null) {
            final ProgressDialog pd = ProgressDialog.show(getContext(), "", "Loading...",true);
            mDirection_webView.getSettings().setJavaScriptEnabled(true);
            mDirection_webView.getSettings().setSupportZoom(true);
            mDirection_webView.getSettings().setBuiltInZoomControls(true);
            mDirection_webView.setWebViewClient(new WebViewClient(){

                @Override
                public void onPageCommitVisible(WebView view, String url) {
                    if(pd!=null && pd.isShowing())
                    {
                        pd.dismiss();
                    }
                }
            });
            mDirection_webView.loadUrl(mDirection_url);
        }else {
            mEmptyView.setVisibility(View.VISIBLE);
        }
    }
}
