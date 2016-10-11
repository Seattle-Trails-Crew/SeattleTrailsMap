package gov.seattle.trails;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

public class SearchResultsActivity extends AppCompatActivity {

    ListView searchResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        handleIntent(getIntent());
    }

    /**
     * onNewIntent() only used for android:launchMode="singleTop" which is not currently being used
     * but we might as well include it for later
     */
    @Override
    protected void onNewIntent(Intent intent) {

        handleIntent(intent);
    }

    /**

     */
    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
        }
    }
}
