package com.example.thanhnguyen.excelshortcuts;

import android.app.SearchManager;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SearchViewCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class MainActivity extends ActionBarActivity {

    ArrayList<Shortcut> shortcutList;
    CustomAdapter ca;
    EditText searchBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyDBHandler db = new MyDBHandler(this);

        try {
            db.createDatabase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }

        try {
            db.openDatabase();
        } catch (SQLException sqle) {

        }

        shortcutList = db.getAllShortcuts();

        ListView shortcutListView = (ListView) findViewById(R.id.shortcutListView);
        ca = new CustomAdapter(this,0,shortcutList);
        shortcutListView.setAdapter(ca);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.search);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setIconifiedByDefault(false);

        int id = android.support.v7.appcompat.R.id.search_src_text;
        searchBox = (EditText) searchView.findViewById(id);

        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count < before ) {
                    ca.resetData();
                }
                ca.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        if (Build.VERSION.SDK_INT >= 11) {
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

            searchView.setQueryHint("Search shortcuts");
        } else {
            SearchViewCompat.setSearchableInfo(searchView, getComponentName());
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
