package kambala.tedtalks;

import java.sql.SQLException;
import java.util.List;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;

import kambala.data.orm.DatabaseHelper;
import kambala.data.orm.GetTalks;
import kambala.data.orm.TedTalk;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


/**
 * @author Bala subrahmanyam Kambala
 * 
 * Displays watched ted talks
 * 
 */
public class WatchedTalks extends OrmLiteBaseActivity<DatabaseHelper> {

	public static DatabaseHelper dbHelper; 
	TextView mSearchText;
	TedTalksAdapter talksListAdapter;
	
	private final String LOG_TAG = getClass().getSimpleName();
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(LOG_TAG, "Entered into watched talks activity");
               
        setContentView(R.layout.activity_watched_talks);
        
        dbHelper = getHelper();
        
        talksListAdapter = new TedTalksAdapter(this);
        ListView talksListView = (ListView)findViewById(R.id.watchedTalksList);
        talksListView.setAdapter(talksListAdapter);
        talksListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				
				TedTalk talk = talksListAdapter.getTedTalksPosition(position);
			
				// Fix: error in showing this data
				
				/*StringBuilder sb = new StringBuilder();
				sb.append("id = ").append(talk.getId());
				sb.append("name = ").append(talk.getName());
				sb.append("published At = ").append(talk.getPublishedAt());
				sb.append("Native Language = ").append(talk.getNativeLanguage());*/

				Log.i(LOG_TAG, "position: " + position);
				
				AlertDialog.Builder builder = new AlertDialog.Builder(WatchedTalks.this);

				List<TedTalk> talks = TedTalksAdapter.getWatchedTedTalks();
			    builder.setTitle("Remove from Watched Ted Talk list?");
			    builder.setMessage("Title: " + talks.get(position).toString());
			    final TedTalk selectedTalk = talks.get(position);
			    
			    Log.i(LOG_TAG, "position: " + selectedTalk.getId());
			    Log.i(LOG_TAG, "position: " + selectedTalk.getName());

			    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

			        public void onClick(DialogInterface dialog, int which) {
			            // Do nothing but close the dialog

			        	try {
							WatchedTalks.dbHelper.getDao().delete(selectedTalk);
							
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			            dialog.dismiss();
			        }

			    });

			    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

			        @Override
			        public void onClick(DialogInterface dialog, int which) {
			            // Do nothing
			            dialog.dismiss();
			        }
			    });

			    AlertDialog alert = builder.create();
			    alert.show();
				
			}
		});
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.watched_talks, menu);
        
        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        
        SearchableInfo si = searchManager.getSearchableInfo(new ComponentName(getApplicationContext(), SearchableActivity.class));
        searchView.setSearchableInfo(si);
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
        
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }
 
    
    protected boolean isAlwaysExpanded() {
        return false;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_refresh:
            	refreshTalks();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
        
    private void refreshTalks()
    {
    	Log.i(LOG_TAG, "Clicked on refresh");
    	
    	this.runOnUiThread(new Runnable() {

            public void run() {
            	TedTalksAdapter.updateData();
            	talksListAdapter.notifyDataSetChanged();
            }
        });
    }
}
