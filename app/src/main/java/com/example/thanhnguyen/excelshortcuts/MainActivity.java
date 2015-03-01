package com.example.thanhnguyen.excelshortcuts;

import android.app.ActionBar;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class MainActivity extends Activity implements ViewTreeObserver.OnScrollChangedListener{

    ArrayList<Shortcut> shortcutList;
    CustomAdapter ca;
    EditText searchBox;
    private float mActionBarHeight;
    private ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        setContentView(R.layout.activity_main);

        final TypedArray styledAttributes = getTheme().obtainStyledAttributes(
            new int[] {android.R.attr.actionBarSize});
        mActionBarHeight = styledAttributes.getDimension(0,0);
        styledAttributes.recycle();
        mActionBar = getActionBar();

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
        shortcutListView.getViewTreeObserver().addOnScrollChangedListener(this);

        ca = new CustomAdapter(this,0,shortcutList);
        shortcutListView.setAdapter(ca);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        if (Build.VERSION.SDK_INT >= 11) {
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setIconifiedByDefault(false);
            searchView.setQueryHint("Search shortcuts");
            int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
            searchBox = (EditText) searchView.findViewById(id);
        }

        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count < before ) {
                    ca.resetData();
                }
                ca.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

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

    @Override
    public void onScrollChanged() {
        float y = ((ListView)findViewById(R.id.shortcutListView)).getScrollY();
        if ( y >= mActionBarHeight && mActionBar.isShowing()) {
            mActionBar.hide();
        } else if (y == 0 && !mActionBar.isShowing()) {
            mActionBar.show();
        }
    }
}
