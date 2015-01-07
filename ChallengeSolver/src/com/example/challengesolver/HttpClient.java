package com.example.challengesolver;

import android.content.Context;
import com.squareup.okhttp.OkHttpClient;

public class HttpClient {
	private static final String TAG = HttpClient.class.getSimpleName();

	public static OkHttpClient newOkHttpClient(Context context) {
		OkHttpClient client = new OkHttpClient();

//		SSLSocketFactory sslfactory = getPinnedCertSslSocketFactory(context);
//		client.setSslSocketFactory(sslfactory);
//		client.setHostnameVerifier(newAllHostnameVerifier());
		return client;
	}

}
