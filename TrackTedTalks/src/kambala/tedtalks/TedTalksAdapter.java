package kambala.tedtalks;

import java.util.ArrayList;
import java.util.List;

import com.j256.ormlite.dao.Dao;

import kambala.data.orm.TedTalk;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TedTalksAdapter extends BaseAdapter {

	private static List<TedTalk> talksList;

	private WatchedTalks watchedTalksActivity;
	private final String LOG_TAG = getClass().getSimpleName();
	
	public TedTalksAdapter(WatchedTalks activity) {
		this.watchedTalksActivity = activity;
		talksList = getWatchedTedTalks();
	}
	
	@Override
	public int getCount() {
		return talksList.size();
	}

	@Override
	public TedTalk getItem(int arg0) {
		return talksList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		
		if(arg1==null)
		{
			LayoutInflater inflater = (LayoutInflater) this.watchedTalksActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			arg1 = inflater.inflate(R.layout.listitem, arg2,false);
		}
		
		TextView talkName = (TextView)arg1.findViewById(R.id.textView1);
		TextView talkDescription = (TextView)arg1.findViewById(R.id.textView2);
		
		TedTalk chapter = talksList.get(arg0);
		
		talkName.setText(chapter.getName());
		talkDescription.setText(chapter.getDescription());
		
		return arg1;
	}
	
	public TedTalk getTedTalksPosition(int position)
	{
		return talksList.get(position);
	}
	
    public static List<TedTalk> getWatchedTedTalks()
    {
    	List<TedTalk> list = new ArrayList<TedTalk>();
		
		try {
			// get our dao
			Dao<TedTalk, Integer> simpleDao = WatchedTalks.dbHelper.getDao();
			// query for all of the data objects in the database
			list = simpleDao.queryForAll();
			
			Log.i("TalksAdapater", "No of watched talks: " + list.size());
		} catch (Exception e) {
			Log.i("TalksAdapater", e.toString());
		}
		
		return list;
    }
    
    public static void updateData() {
    	talksList = getWatchedTedTalks();
    }
}