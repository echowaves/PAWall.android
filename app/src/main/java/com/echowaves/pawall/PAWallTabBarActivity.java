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

    private TextView titleTextView;

    @SuppressWarnings("unused")
    private SearchPostsTabFragment searchPostsFragment;
    @SuppressWarnings("unused")
    private BookmarksTabFragment bookmarksFragment;
    @SuppressWarnings("unused")
    private AlertsTabFragment alertsTabFragment;

    private TabHost tabHost;

    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pawall_tab_bar);

        titleTextView = (TextView) findViewById(R.id.tabBar_title);
        titleTextView.setText("Posts Near You");

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
            Log.d("~~~~~~~~~~ Loading tab", String.valueOf(index));
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


        searchPostsFragment = (SearchPostsTabFragment) getFragmentManager()
                .findFragmentById(R.id.fragment_searchPosts);
        bookmarksFragment = (BookmarksTabFragment) getFragmentManager()
                .findFragmentById(R.id.fragment_bookmarks);
        alertsTabFragment = (AlertsTabFragment) getFragmentManager()
                .findFragmentById(R.id.fragment_alerts);


    }

    @Override
    public void onTabChanged(String tabId) {
        Log.d("PAWallTabBarActivity", "onTabChanged(): tabId=" + tabId.substring(3));
        currentTab = Integer.parseInt(tabId.substring(3));
        switch (currentTab) {
            case 0:
                titleTextView.setText("Posts Near You");
                break;
            case 1:
                titleTextView.setText("My Bookmarks");
                break;
            case 2:
                titleTextView.setText("My Alerts");
                break;
            default:
                titleTextView.setText("...");
        }
    }

}
