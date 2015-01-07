package com.example.challengesolver;

import java.util.ArrayList;

import com.google.gson.Gson;

import android.util.Log;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.mime.TypedInput;

public class Solver {

	private Response getResponse() {
		String endpoint = "http://play.bnotions.com:9090";
		IGetCommandService gcs = newService(endpoint);
		Response response = gcs.handleGetCommandRequest();
		return response;
	}
	

	public interface IGetCommandService {
		@GET("/test")
		Response handleGetCommandRequest();
	}

	private IGetCommandService newService(String endpoint) {
		RestAdapter restAdapter = new RestAdapter.Builder()
		.setClient(new OkClient())
		.setEndpoint(endpoint)
		.setLogLevel(RestAdapter.LogLevel.FULL)
		.build();

		return restAdapter.create(IGetCommandService.class);
	}
	
	private ArrayList<ConferenceTalk> parseResponse(Response response) {
		ArrayList<ConferenceTalk> talks = new ArrayList<ConferenceTalk>();
		
		final Class<?> arraylistClass = new ArrayList<ConferenceTalk>().getClass();
		if(response.getStatus() == 200) {
			TypedInput body = response.getBody();
			Gson gson = new Gson();
			try{
				talks = (ArrayList<ConferenceTalk>) gson.fromJson(body.toString(),talks.getClass());
			} catch(Exception e) {
				Log.e("test", "Error casting to arraylist");
			}
			
		}
		
		return talks;
	}
}
