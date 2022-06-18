package com.example.ibook_social_network;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

class SendingPost extends AsyncTask<String, Void, Void> {

    String responseStr;

    interface Callback {
        void callingBack(String s);
    }

    private final Callback callback;

    public SendingPost(Callback callback) {
        this.callback = callback;
    }

    @Override
    protected Void doInBackground(String... strings) {
        MediaType mediaType = MediaType.parse("application/json");
        OkHttpClient httpClient = new OkHttpClient();

        Config NetworkConfiguration = new Config(strings);
        String jsonStr;
        String url;
        Request request = null;

        if(strings[0].equals("authorization")){
            jsonStr = NetworkConfiguration.jsonAuthorization;
            url = NetworkConfiguration.authUrl;
            Log.e("Server url", url);
            try {
                request = new Request.Builder()
                        .url(url)
                        .post(RequestBody.create(mediaType, jsonStr))
                        .build();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        else{
            jsonStr = NetworkConfiguration.jsonStr;
            request = new Request.Builder()
                    .url(NetworkConfiguration.url)
                    .post(RequestBody.create(mediaType, jsonStr))
                    .build();
        }
        try {
            if(request!=null) {
                Response response = httpClient.newCall(request).execute();
                responseStr = Objects.requireNonNull(response.body()).string();
                Log.e("IbookServer1", responseStr);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void Void) {
        try {
                if (responseStr != null) {
                    JSONObject responseObj = new JSONObject(responseStr);
                    callback.callingBack(String.valueOf(responseObj.get("text")));
                }
               else callback.callingBack("404");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}