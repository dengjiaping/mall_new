package com.android.volley.toolbox;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;

public abstract class MoccaPostRequest<T> extends JsonRequest<T> {

	public MoccaPostRequest(String url, Listener<T> listener, ErrorListener errorListener) {
		super(Method.POST, url, listener, errorListener);
	}

}
