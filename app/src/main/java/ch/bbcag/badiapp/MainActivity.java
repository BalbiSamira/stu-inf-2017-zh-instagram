package ch.bbcag.badiapp;

import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import ch.bbcag.badiapp.dal.BadiDao;
import ch.bbcag.badiapp.helper.DialogHelper;
import ch.bbcag.badiapp.model.Badi;

/**
 * In MainActivity werden alle Favoritenbadis aufgelistet
 * Nach Klick auf eine Badi, wird die BadiDetailsActivity gestartet, wo die Beckennamen und Temperaturen der geklickten Badi angezeigt werden.
 * Wenn in der AppBar auf das Icon geklickt wird, öffnet sich eine Liste, wo die Favoritenbadis ausgewählt werden können.
 */
public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_BADI_ID = "badiId";
    public static final String EXTRA_BADI_NAME = "badiName";
    private static final String TAG = "MainActivity";
    private ArrayAdapter<Badi> badiAdapter;
    private ProgressBar progressBar;
    private BadiDao badiDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.loading_main_progress);
        setTitle(getString(R.string.main_title));
        badiAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1);
        try {
            badiDao = BadiDao.getInstance(getApplicationContext());
        } catch (SQLiteException e) {
            generateAlertDialog();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_favorite:
                Intent intent = new Intent(getApplicationContext(), BadiFavoritesActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    private void addBadisToList() {
        ListView badis =  findViewById(R.id.badiliste);
        badiAdapter.clear();
        badiAdapter.addAll(badiDao.getFavorites());
        progressBar.setVisibility(View.GONE);
        badis.setAdapter(badiAdapter);
        AdapterView.OnItemClickListener mListClickedHandler = new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView parent, View v, int position, long id)
            {
                Intent intent = new Intent(getApplicationContext(), BadiDetailsActivity.class);
                Badi selected = (Badi)parent.getItemAtPosition(position);

                intent.putExtra(EXTRA_BADI_ID, selected.getId());
                intent.putExtra(EXTRA_BADI_NAME, selected.getName());
                startActivity(intent);
            }
        };

        badis.setOnItemClickListener(mListClickedHandler);
    }

    @Override
    protected void onStart() {
        super.onStart();
        TextView text = findViewById(R.id.nobadisfound_text);
        if (badiDao.getFavoritesCount() == 0) {
            text.setVisibility(View.VISIBLE);
        } else {
            text.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            addBadisToList();
        }
    }

    private void generateAlertDialog() {
        DialogHelper.generateAlertDialog(this, getString(R.string.all_error), getString(R.string.main_alertDialogLoadingFavorites));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            BadiDao.getInstance(getApplicationContext()).close();
        } catch (SQLiteException e) {
            Log.e(TAG, e.getMessage(), e);
        }

    }
}
