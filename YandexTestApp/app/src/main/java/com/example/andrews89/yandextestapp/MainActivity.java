package com.example.andrews89.yandextestapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    public static String LOG_TAG = "log_message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new ParseTask().execute();
    }

    //Получаем и парсим исходный JSON
    private class ParseTask extends AsyncTask<Void, Void, String> {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";
        String destinationUrl = getString(R.string.url).toString();

        @Override
        protected String doInBackground(Void... params) {
            // получаем данные с внешнего ресурса
            try {
                URL url = new URL(destinationUrl);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                resultJson = buffer.toString();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return resultJson;
        }

        @Override
        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);
            // выводим целиком полученную json-строку
            Log.d(LOG_TAG, strJson);

            //Основной Json-массив artists, массив содержащий жанры genres
            JSONArray artists = null, genresArr = null;

            //Json-объект cover
            JSONObject cover = null;

            //Имя исполнителя, Жанры, Ссылка, Описание
            String artistName = "", genres = "", link = "", description = "";

            //id, Количество треков, количество альбомов, количество исполнителей
            int artistID = 0, tracksCount = 0, albumsCount = 0, artistsCount = 0;

            try {
                artists = new JSONArray(strJson);
                artistsCount = artists.length();

                //Получаем данные
                for (int i = 0; i < artistsCount; i++){
                    JSONObject artistObj = artists.getJSONObject(i);

                    artistID = artistObj.getInt("id");
                    artistName = artistObj.getString("name");
                    genres = artistObj.getString("genres");
                    tracksCount = artistObj.getInt("tracks");
                    albumsCount = artistObj.getInt("albums");
                    link = artistObj.getString("link");
                    description = artistObj.getString("description");
                    cover = artistObj.getJSONObject("cover");

                  /*  String[] coverTypes = {"small", "big"};

                    for (int j = 0; j < cover.length(); j++){
                        Log.d(LOG_TAG, cover.getString(coverTypes[j]));
                    }*/

                    Log.d(LOG_TAG, "id: " + artistID);
                    Log.d(LOG_TAG, "Имя: " + artistName);
                    Log.d(LOG_TAG, "Жанр: " + genres);
                    Log.d(LOG_TAG, "Песни: " + tracksCount);
                    Log.d(LOG_TAG, "Альбомы: " + albumsCount);
                    Log.d(LOG_TAG, "Ссылка: " + link);
                    Log.d(LOG_TAG, "Описание: " + description);
                    Log.d(LOG_TAG, "Обложка: " + cover.length());
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
