package com.echowaves.pawall;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.echowaves.pawall.core.PAWFragmentActivity;


public class PAWallTabBarActivity extends PAWFragmentActivity implements TabHost.OnTabChangeListener {

    static int currentTab;
    private SearchPostsTabFragment searchPostsFragment;
    private BookmarsTabFragment bookmarksFragment;
    private AlertsTabFragment alertsTabFragment;

    private TabHost tabHost;

    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pawall_tab_bar);

        backButton = (ImageButton) findViewById(R.id.tabBar_backButton);
        //Listening to button event
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                finish();
            }
        });


        tabHost = (TabHost) findViewById(R.id.nav_tabhost);
        tabHost.setup();

        final TabWidget tabWidget = tabHost.getTabWidget();
        final FrameLayout tabContent = tabHost.getTabContentView();

        // Get the original tab textviews and remove them from the viewgroup.
        TextView[] originalTextViews = new TextView[tabWidget.getTabCount()];
        for (int index = 0; index < tabWidget.getTabCount(); index++) {
            originalTextViews[index] = (TextView) tabWidget.getChildTabViewAt(index);
        }
        tabWidget.removeAllViews();

        // Ensure that all tab content childs are not visible at startup.
        for (int index = 0; index < tabContent.getChildCount(); index++) {
            tabContent.getChildAt(index).setVisibility(View.GONE);
        }

        // Create the tabspec based on the textview childs in the xml file.
        // Or create simple tabspec instances in any other way...
        for (int index = 0; index < originalTextViews.length; index++) {
            Log.d("~~~~~~~~~~~~~~~~~~~~~ Loading tab", String.valueOf(index));
            final TextView tabWidgetTextView = originalTextViews[index];
            final View tabContentView = tabContent.getChildAt(index);
            TabHost.TabSpec tabSpec = tabHost.newTabSpec((String) tabWidgetTextView.getTag());
            tabSpec.setContent(new TabHost.TabContentFactory() {
                @Override
                public View createTabContent(String tag) {
                    return tabContentView;
                }
            });
//            if (tabWidgetTextView.getBackground() == null) {
//                tabSpec.setIndicator(tabWidgetTextView.getText());
//            } else {
            tabSpec.setIndicator(tabWidgetTextView.getText(), tabWidgetTextView.getBackground());
//            }
            tabHost.addTab(tabSpec);
        }

        tabHost.setOnTabChangedListener(this);

//        // tune out button
//        ImageView backButton = (ImageView) findViewById(R.id.nav_tuneOut);
//        //Listening to button event
//        tuneOutButton.setOnClickListener(new View.OnClickListener() {
//
//            public void onClick(final View v) {
//
//                EWWave.tuneOut(new EWJsonHttpResponseHandler(v.getContext()) {
//
//                    @Override
//                    public void onSuccess(int statusCode, Header[] headers, JSONObject jsonResponse) {
//                        Log.d(">>>>>>>>>>>>>>>>>>>> ", jsonResponse.toString());
//
//                        Intent home = new Intent(getApplicationContext(), HomeActivity.class);
//                        startActivity(home);
//                    }
//
//                });
//
//            }
//        });


        searchPostsFragment = (SearchPostsTabFragment) getFragmentManager()
                .findFragmentById(R.id.fragment_searchPosts);
        bookmarksFragment = (BookmarsTabFragment) getFragmentManager()
                .findFragmentById(R.id.fragment_bookmarks);
        alertsTabFragment = (AlertsTabFragment) getFragmentManager()
                .findFragmentById(R.id.fragment_alerts);


    }

    @Override
    public void onTabChanged(String tabId) {
        Log.d("PAWallTabBarActivity ==========================================: ", "onTabChanged(): tabId=" + tabId.substring(3));
        currentTab = Integer.parseInt(tabId.substring(3));
    }

}
