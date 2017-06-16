/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.volley.toolbox;

import java.util.TreeMap;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.mynet.MyRequest;

/**
 * A request for retrieving a T TYPE response body at a given URL that also
 * optionally sends along a JSON body in the request specified.
 *
 * @param <T> JSON TYPE of response expected
 */
public abstract class JsonRequest<T> extends MyRequest<T> {
	/** Charset for request. */
//	private static final String PROTOCOL_CHARSET = "utf-8";

	/** Content TYPE for request. */
//	private static final String PROTOCOL_CONTENT_TYPE = String.format("application/json; charset=%s",
//			PROTOCOL_CHARSET);

	private final Listener<T> mListener;

//	private final String mRequestBody;

	/**
	 * Deprecated constructor for a JsonRequest which defaults to GET unless {@link #getPostBody()}
	 * or {@link #getPostParams()} is overridden (which defaults to POST).
	 *
	 * @deprecated Use {@link #JsonRequest(int, String, String, Listener, ErrorListener)}.
	 */
	@Deprecated
	public JsonRequest(String url, String requestBody, Listener<T> listener, ErrorListener errorListener) {
		this(Method.DEPRECATED_GET_OR_POST, url, requestBody, listener, errorListener);
	}

	public JsonRequest(int method, String url, String requestBody, Listener<T> listener,
			ErrorListener errorListener) {
		super(method, url, errorListener);
		mListener = listener;
//		mRequestBody = requestBody;
	}

	public JsonRequest(int method, String url, Listener<T> listener, ErrorListener errorListener) {
		super(method, url, errorListener);
		mListener = listener;
//		mRequestBody = null;
	}

	@Override
	protected void deliverResponse(T response) {
		mListener.onResponse(response);
	}

	@Override
	abstract protected Response<T> parseNetworkResponse(NetworkResponse response);



}
