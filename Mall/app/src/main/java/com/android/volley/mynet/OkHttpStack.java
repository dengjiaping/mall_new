package com.android.volley.mynet;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.HttpStack;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;


/**
 * Created by 508632 on 2017/1/2.
 */

public class OkHttpStack implements HttpStack {
	okhttp3.OkHttpClient.Builder mClientBuilder;

	public OkHttpStack() {
		mClientBuilder = new okhttp3.OkHttpClient.Builder();
	}

	@Override
	public HttpResponse performRequest(Request<?> request, Map<String, String> additionalHeaders) throws IOException, AuthFailureError {
		OkHttpClient mOkHttpClient = mClientBuilder
				.connectTimeout(30, TimeUnit.SECONDS)
				.writeTimeout(30, TimeUnit.SECONDS)
				.readTimeout(30, TimeUnit.SECONDS)
				.build();

		okhttp3.Request.Builder mRequestBuilder = new okhttp3.Request.Builder();
		mRequestBuilder.url(request.getUrl());

		Map<String, String> headers = request.getHeaders();
		for (String name : headers.keySet()) {
			mRequestBuilder.addHeader(name, headers.get(name));
		}
		for (String name : additionalHeaders.keySet()) {
			mRequestBuilder.addHeader(name, additionalHeaders.get(name));
		}

		okhttp3.Request okRequest = getOkhttpRequestForVolleyRequest(mRequestBuilder, request);
		Response okResponse = mOkHttpClient.newCall(okRequest).execute();

		BasicStatusLine responseStatus = new BasicStatusLine( parseProtocol(okResponse.protocol()), okResponse.code(), okResponse.message() );
		BasicHttpResponse response = new BasicHttpResponse(responseStatus);
		response.setEntity(entityFromOkHttpResponse(okResponse));
		Headers responseHeaders = okResponse.headers();

		int size = responseHeaders.size();
		String name = null;
		String value = null;
		for (int i = 0; i < size; i++) {
			name = responseHeaders.name(i);
			if (name != null) {
				response.addHeader(new BasicHeader(name, value));
			}
		}

		return response;
	}

	private static HttpEntity entityFromOkHttpResponse(Response r) throws IOException {
		BasicHttpEntity entity = new BasicHttpEntity();
		ResponseBody body = r.body();

		entity.setContent(body.byteStream());
		entity.setContentLength(body.contentLength());
		entity.setContentEncoding(r.header("Content-Encoding"));

		if (body.contentType() != null) {
			entity.setContentType(body.contentType().type());
		}
		return entity;
	}

	static okhttp3.Request getOkhttpRequestForVolleyRequest(okhttp3.Request.Builder builder, Request<?> volleyRequest) throws IOException, AuthFailureError {
		switch (volleyRequest.getMethod()) {
			case Request.Method.DEPRECATED_GET_OR_POST:
				byte[] postBody = volleyRequest.getPostBody();
				if (postBody != null) {
					builder.post(RequestBody.create(MediaType.parse(volleyRequest.getPostBodyContentType()), postBody));
				}
				break;
			case Request.Method.GET:
				Map<String, Object> pa = volleyRequest.getParams();
				StringBuilder encodedParams = new StringBuilder();
				if (pa != null && pa.size() > 0){
					try {
						for (Map.Entry<String, Object> entry : pa.entrySet()) {
							encodedParams.append(URLEncoder.encode(entry.getKey(), volleyRequest.DEFAULT_PARAMS_ENCODING));
							encodedParams.append('=');

							Object valueObject = entry.getValue();
							if ( valueObject instanceof String ){
								String valueStr = (String) valueObject;
								encodedParams.append(URLEncoder.encode(valueStr, volleyRequest.DEFAULT_PARAMS_ENCODING));
								encodedParams.append('&');
							}else{
								throw new IllegalArgumentException("get请求，参数值不允许是object类型的");
							}
						}
					} catch (UnsupportedEncodingException uee) {
						throw new RuntimeException("Encoding not supported: " + volleyRequest.DEFAULT_PARAMS_ENCODING, uee);
					}
				}
				builder.url(volleyRequest.getUrl() + "?"+ encodedParams);
				builder.get();
				break;
			case Request.Method.DELETE:
				builder.delete();
				break;
			case Request.Method.POST:
				builder.post(createRequestBody(volleyRequest));
				break;
			case Request.Method.PUT:
				builder.put(createRequestBody(volleyRequest));
				break;
			case Request.Method.HEAD:
				builder.head();
				break;
			case Request.Method.OPTIONS:
				builder.method("OPTIONS", null);
				break;
			case Request.Method.TRACE:
				builder.method("TRACE", null);
				break;
			case Request.Method.PATCH:
				builder.patch(createRequestBody(volleyRequest));
				break;
			default:
				throw new IllegalStateException("Unknown method TYPE.");
		}
		return builder.build();
	}

	private static ProtocolVersion parseProtocol(final Protocol p) {
		switch (p) {
			case HTTP_1_0:
				return new ProtocolVersion("HTTP", 1, 0);
			case HTTP_1_1:
				return new ProtocolVersion("HTTP", 1, 1);
			case SPDY_3:
				return new ProtocolVersion("SPDY", 3, 1);
			case HTTP_2:
				return new ProtocolVersion("HTTP", 2, 0);
		}

		throw new IllegalAccessError("Unkwown protocol");
	}

	private static RequestBody createRequestBody(Request r) throws AuthFailureError {
		final byte[] body = r.getBody();
		if (body == null) return null;

		return RequestBody.create(MediaType.parse(r.getBodyContentType()), body);
	}
}