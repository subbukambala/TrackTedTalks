package kambala.tedtalks;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.List;

import kambala.data.orm.GetTalks;
import kambala.data.orm.TedTalk;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SearchableActivity extends ListActivity {
	

	private List<TedTalk> talks = null;
	private final String LOG_TAG = getClass().getSimpleName();

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(LOG_TAG, "Entered into search activity");
        
        setContentView(R.layout.search_list_view);
        
        
        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
          String query = intent.getStringExtra(SearchManager.QUERY);
          GetTalks searchObject = new GetTalks(); 
          try {
        	  searchObject.execute(getTedUrl(query)).get();
          }
          catch(Exception e) {
        	  Log.i(LOG_TAG, "exception raised in getting data" + e.getMessage());
          }
          
          talks  = searchObject.GetSearchTalks(); 
          
       // initiate the listadapter
          ArrayAdapter<TedTalk> myAdapter = new ArrayAdapter <TedTalk>(this,
                  R.layout.search_listitem, R.id.search_name, talks);
           // assign the list adapter
           setListAdapter(myAdapter);
        }
    }

	protected void onListItemClick(ListView l, View v, int position, long id) {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

	    builder.setTitle("Add to Watched Ted Talk list?");
	    builder.setMessage("Title: " + talks.get(position).toString());
	    final TedTalk talk = talks.get(position);

	    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

	        public void onClick(DialogInterface dialog, int which) {
	            // Do nothing but close the dialog

	        	try {
					WatchedTalks.dbHelper.getDao().create(talk);
					
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
	
	private String getTedUrl(String query) {
		try {
			return Constants.TedSearchUrl + "?q=" +  URLEncoder.encode(query, "UTF-8") + "&categories=talks&api-key=" + Constants.TedApiKey;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
}
