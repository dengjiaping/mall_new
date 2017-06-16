package com.android.volley.toolbox;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;

public abstract class MoccaGetRequest<T> extends JsonRequest<T> {

	public MoccaGetRequest(String url, Listener<T> listener, ErrorListener errorListener) {
		super(Method.GET, url, listener, errorListener);
	}

}
