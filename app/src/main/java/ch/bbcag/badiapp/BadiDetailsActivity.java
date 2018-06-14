package ch.bbcag.badiapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;

import ch.bbcag.badiapp.helper.DialogHelper;
import ch.bbcag.badiapp.helper.WieWarmJsonParser;
import ch.bbcag.badiapp.model.Badi;
import ch.bbcag.badiapp.model.Becken;

/**
 * Erhält Badi-Id und Badiname per Intent.
 * Badiname wird im Titel angezeigt.
 * Anhand der Badi-Id wird über die wiewarm.ch REST-API ein JSON-Objekt, welche alle Badiinformationen inkl. Beckeninformationen enthält, angefordert.
 * Aus dem erhaltenen JSON-Objekt werden Beckennamen und Temperaturen ausgelesen und in einer Liste angezeigt.
 */
public class BadiDetailsActivity extends AppCompatActivity {
    private static String TAG = "BadiInfo";
    private int badiId;
    private ProgressBar progressBar;
    public static final String WIE_WARM_API_URL = "http://www.wiewarm.ch/api/v1/bad.json/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_badi_details);
        progressBar = findViewById(R.id.loading_badi_details_progress);
        Intent intent = getIntent();
        badiId = intent.getIntExtra(MainActivity.EXTRA_BADI_ID, 0);
        String name = intent.getStringExtra(MainActivity.EXTRA_BADI_NAME);
        setTitle(name);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_favorite:
                Intent intent = new Intent(getApplicationContext(), BadiFavoritesActivity.class);
                startActivity(intent);
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        progressBar.setVisibility(View.VISIBLE);
        getBadiTemp(WIE_WARM_API_URL + badiId);
    }

    private void getBadiTemp(String url)
    {
        final ArrayAdapter<Becken> beckenInfosAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1);
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        final Activity mainActivity = this;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);
                        try {
                            Badi badi = WieWarmJsonParser.createBadiFromJsonString(response);
                            beckenInfosAdapter.addAll(badi.getBecken());
                            ListView badiInfoList = findViewById(R.id.becken_infos);
                            badiInfoList.setAdapter(beckenInfosAdapter);

                        } catch (JSONException e) {
                            Log.e(TAG, e.getMessage(), e);
                            DialogHelper.generateAlertDialog(mainActivity, getString(R.string.all_error), getString(R.string.badiDetails_alertDialogLoadingBadis));
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                DialogHelper.generateAlertDialog(mainActivity, getString(R.string.all_error), getString(R.string.badiDetails_alertDialogLoadingBadis));
            }
        });

        queue.add(stringRequest);
    }


}
