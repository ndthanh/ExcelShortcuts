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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class MainActivity extends Activity{

    ArrayList<Shortcut> shortcutList;
    CustomAdapter ca;
    EditText searchBox;
    private float mActionBarHeight;
    private ActionBar mActionBar;
    private static final String TAG = "MainActivity";
    ListView shortcutListView;

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

        shortcutListView = (ListView) findViewById(R.id.shortcutListView);
        shortcutListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            private int mLastFirstVisibleItem;
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(mLastFirstVisibleItem < firstVisibleItem) {
                    Log.i(TAG,"Scrolling down");
                    if (mActionBar.isShowing())
                        mActionBar.hide();
                }
                if(mLastFirstVisibleItem>firstVisibleItem) {
                    Log.i(TAG,"Scrolling up");
                    if (!mActionBar.isShowing())
                        mActionBar.show();
                }
                mLastFirstVisibleItem = firstVisibleItem;
            }
        });
        shortcutListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String shortcutItem = ((TextView) view.findViewById(R.id.descriptionText)).getText().toString();
//                Log.i(TAG,"item cliked " + shortcutItem);
            }
        });

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
        switch (id) {
            case R.id.action_settings:
                return true;
            case R.id.action_sort_by_description:
                ca.sortByDescription(shortcutList);
                return true;
            case R.id.action_sort_by_category:
                ca.sortByCategory(shortcutList);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
