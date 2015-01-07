package com.example.challengesolver;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.OkHttpClient;

import android.util.Log;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.mime.TypedInput;

/**
 * Runnable to fetch commands from server.
 */
public class FetchInputRunnable implements Runnable {

    private static final String TAG = FetchInputRunnable.class.getSimpleName();

    public FetchInputRunnable() {
    }

    @Override
    public void run() {
        Log.d(TAG, "GetCommandRunnable run...");

        try {
        	String endpoint = "http://play.bnotions.com:9090";
    		IGetCommandService gcs = newService(endpoint);
    		gcs.getJsonResponse(new Callback<String>() {
    	        @Override
    	        public void success(String str, Response ignored) {
    	            // Prints the correct String representation of body.
    	            System.out.println(str);
    	            parseResponse(str);
    	        }

    	        @Override
    	        public void failure(RetrofitError retrofitError) {
    	            System.out.println("Failure, retrofitError" + retrofitError);
    	        }
    	    });
    		
        } catch (Exception e) {
            Log.w(TAG, "Exception getting command from MDM server", e);
        }
    }

	public interface IGetCommandService {
		@GET("/test")
		public void getJsonResponse(Callback<String> callback);
	}

	private IGetCommandService newService(String endpoint) {
		RestAdapter restAdapter = new RestAdapter.Builder()
		.setClient(new OkClient(new OkHttpClient()))
		.setEndpoint(endpoint)
		.setConverter(new StringConverter())
		.setLogLevel(RestAdapter.LogLevel.FULL)
		.build();

		return restAdapter.create(IGetCommandService.class);
	}
	
	private ArrayList<ConferenceTalk> parseResponse(String responseBody) {
		ArrayList<ConferenceTalk> talks = new ArrayList<ConferenceTalk>();
		
		final Class<?> arraylistClass = new ArrayList<ConferenceTalk>().getClass();
		Gson gson = new Gson();
		try{
			talks = (ArrayList<ConferenceTalk>) gson.fromJson(responseBody, new TypeToken<List<ConferenceTalk>>(){}.getType());
		} catch(Exception e) {
			Log.e("test", "Error casting to arraylist");
		}
		
		for(ConferenceTalk talk : talks) {
			Log.d(TAG, "name= "+talk.getName() + ", votes = "+ talk.getVotes()+ ", minutes = "+talk.getMinutes());
		}
			
		return talks;
	}
}
