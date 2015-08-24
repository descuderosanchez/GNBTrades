package lanaccess.gnbtrades;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by David on 23/8/15.
 * Busqueda por SKU en el ActionBar.
 */
public class SearchResultsActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        //Si la busqueda coincide abre una activity pasandole el SKU como extra.
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Intent intent2 = new Intent(SearchResultsActivity.this, ListDetailActivity.class);
            intent2.putExtra("sku", query.toUpperCase());
            startActivity(intent2);
            finish();
        }
    }
}

