package com.example.challengesolver;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;
import retrofit.http.GET;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.OkHttpClient;

import android.support.v7.app.ActionBarActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

	private Button computeSolutionBtn;
	private TextView inputTv;
	private TextView solutionTv;
	private Handler mHandler1;
	private Handler mHandler2;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        computeSolutionBtn = (Button) findViewById(R.id.computeSolnBtn);
        inputTv = (TextView) findViewById(R.id.inputTv);
        solutionTv = (TextView) findViewById(R.id.solutionTv);
        
        mHandler1 = new Handler() { 
        	@Override public void handleMessage(Message msg) { 
        		String input =(String) msg.obj;
        		inputTv.setText("Input = "+ input);
        		Solver.setAllTalks(parseResponse(input));
        	}
        };
        
        mHandler2 = new Handler() { 
        	@Override public void handleMessage(Message msg) { 
        		String solution =(String) msg.obj;
        		solutionTv.setText(solution);
        	}
        };
        
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    public void onFetchInputClicked(View v) {
    	// get input from api endpoint
        ExecutorService executor = Executors.newSingleThreadExecutor(); 
        executor.submit(new FetchInputRunnable());
        computeSolutionBtn.setVisibility(View.VISIBLE);
    }
    
    public void onComputeSolutionClicked(View v) {
    	if(Solver.getAllTalks().size() > 0) {
    		// run in background
    		AsyncTask<Void, Void, String> asyncTask = new AsyncTask<Void, Void, String>() {
				@Override
				protected String doInBackground(Void... params) {
					String res = Solver.getOptimalSchedule();
					return res;
				}
				
				@Override
    			protected void onPostExecute(String solution) {
					Message msg=new Message();
    	            msg.obj= solution;
    	            mHandler2.sendMessage(msg);
    			}
    		};

            asyncTask.execute();
    	}
    }
    
    private ArrayList<ConferenceTalk> parseResponse(String responseBody) {
		ArrayList<ConferenceTalk> talks = new ArrayList<ConferenceTalk>();
		
		Gson gson = new Gson();
		try{
			talks = (ArrayList<ConferenceTalk>) gson.fromJson(responseBody, new TypeToken<List<ConferenceTalk>>(){}.getType());
		} catch(Exception e) {
			Log.e("test", "Error casting to arraylist");
		}
		
//		for(ConferenceTalk talk : talks) {
//			Log.d(TAG, "name= "+talk.getName() + ", votes = "+ talk.getVotes()+ ", minutes = "+talk.getMinutes());
//		}
			
		return talks;
	}
    
    /**
     * Runnable to fetch commands from server.
     */
    private class FetchInputRunnable implements Runnable {

        private final String TAG = FetchInputRunnable.class.getSimpleName();

        public FetchInputRunnable() {
        }

        @Override
        public void run() {
            Log.d(TAG, "FetchInputRunnable run...");

            try {
            	String endpoint = "http://play.bnotions.com:9090";
        		IGetCommandService gcs = newService(endpoint);
        		gcs.getJsonResponse(new Callback<String>() {
        	        @Override
        	        public void success(String str, Response ignored) {
        	            // Prints the correct String representation of body.
        	            Log.d(TAG, str);
        	            
        	            Message msg=new Message();
        	            msg.obj= str;
        	            mHandler1.sendMessage(msg);
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

    	private IGetCommandService newService(String endpoint) {
    		RestAdapter restAdapter = new RestAdapter.Builder()
    		.setClient(new OkClient(new OkHttpClient()))
    		.setEndpoint(endpoint)
    		.setConverter(new StringConverter())
    		.setLogLevel(RestAdapter.LogLevel.FULL)
    		.build();

    		return restAdapter.create(IGetCommandService.class);
    	}
    }
    
    private interface IGetCommandService {
		@GET("/test")
		public void getJsonResponse(Callback<String> callback);
	}

}
