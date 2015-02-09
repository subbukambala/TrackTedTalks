package kambala.data.orm;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import kambala.tedtalks.WatchedTalks;
import kambala.web.http.ServiceHandler;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
/**
 * Async task class to get json by making HTTP call
 * */
public class GetTalks extends AsyncTask<String, Void, Void> {

	private List<TedTalk> talks = null;

    JSONArray talksJson = null;
    
	/*
	 * Format JSON response
	 *
	 * {  
			   "results":[  
			      {  
			         "talk":{  
			            "id":2180,
			            "event_id":352,
			            "name":"Brian Dettmer: Old books reborn as art",
			            "description":"What do you do with an outdated encyclopedia in the information age? With X-Acto knives and an eye for a good remix, artist Brian Dettmer makes beautiful, unexpected sculptures that breathe new life into old books.",
			            "slug":"brian_dettmer_old_books_reborn_as_intricate_art",
			            "native_language_code":"en",
			            "published_at":"2015-02-06 16:00:42",
			            "recorded_at":"2014-11-04 00:00:00",
			            "updated_at":"2015-02-08 07:47:38",
			            "released_at":"2015-02-07 13:50:02"
			         }
			      }
			   ]
			}
	 */
    
	 // JSON Node names
    private static final String TAG_CONTACTS = "results";
    private static final String TAG_TALKS = "talk";
    
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_DESCRIPTION = "description";
    private static final String TAG_EVENT_ID = "event_id";
    private static final String TAG_SLUG = "slug";
    private static final String TAG_PUBLISHED_AT = "published_at";
    private static final String TAG_RECORDED_AT = "recorded_at";
    private static final String TAG_UPDATED_AT = "updated_at";
    private static final String TAG_RELEASED_AT = "released_at";
    private static final String TAG_LANG_CODE = "native_language_code";
    
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
 
        @Override
        protected Void doInBackground(String... urls) {
 
        	talks = new ArrayList<TedTalk>();
        	
        	ServiceHandler sh = new ServiceHandler();
        	 
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(urls[0], ServiceHandler.GET);
  
            Log.d("Response: ", "> " + jsonStr);
 
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                     
                    // Getting JSON Array node
                    talksJson = jsonObj.getJSONArray(TAG_CONTACTS);
 
                    Log.i("no of talks: ", "> " + talksJson.length());
                    
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    // looping through All Contacts
                    for (int i = 0; i < talksJson.length(); i++) {
                        JSONObject innerObject = talksJson.getJSONObject(i);
                        
                        JSONObject c = innerObject.getJSONObject("talk");
                        
                        int id = Integer.parseInt(c.getString(TAG_ID));
                        String name = c.getString(TAG_NAME);
                        String desc = c.getString(TAG_DESCRIPTION);
                        int eventId = Integer.parseInt(c.getString(TAG_EVENT_ID));
                        String slug = c.getString(TAG_SLUG);

                        String publishedStr = c.getString(TAG_PUBLISHED_AT);
                        Date publishedAt = sdf.parse(publishedStr);
						
                        String recordedStr = c.getString(TAG_RECORDED_AT);
                        Date recordedAt = sdf.parse(recordedStr);
 
                        TedTalk talk = new TedTalk();
                        talk.setId(id);
                        talk.setName(name);
                        talk.setDescription(desc);
                        talk.setSlug(slug);
                        talk.setEventId(eventId);
                        talk.setPublishedAt(publishedAt);
                        talk.setRecordedAt(recordedAt);
 
                        Log.i("Response: ", "> " + talk.getId() + "--- " + talk.getName());
                        
                        // adding contact to contact list
                        talks.add(talk);
                    }
                    
                } catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
 
            return null;
        }
 
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
        
        /**
         * Making service call
         * @url - url to make request
         * @method - http request method
         * @params - http request params
         * */
        public String makeServiceCall(String url, int method,
                List<NameValuePair> params) {
        	String response = "";
            try {
                // http client
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpEntity httpEntity = null;
                HttpResponse httpResponse = null;
                 
                // Checking http request method type
                if (method == ServiceHandler.POST) {
                    HttpPost httpPost = new HttpPost(url);
                    // adding post params
                    if (params != null) {
                        httpPost.setEntity(new UrlEncodedFormEntity(params));
                    }
     
                    httpResponse = httpClient.execute(httpPost);
     
                } else if (method == ServiceHandler.GET) {
                    // appending params to url
                    if (params != null) {
                        String paramString = URLEncodedUtils
                                .format(params, "utf-8");
                        url += "?" + paramString;
                    }
                    HttpGet httpGet = new HttpGet(url);
     
                    httpResponse = httpClient.execute(httpGet);
     
                }
                httpEntity = httpResponse.getEntity();
                response = EntityUtils.toString(httpEntity);
     
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
             
            return response;
     
        }
        
        public List<TedTalk> GetSearchTalks() 
        {
        	return talks;
        }
 
    }

