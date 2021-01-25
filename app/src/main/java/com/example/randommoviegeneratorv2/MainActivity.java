package com.example.randommoviegeneratorv2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;


public class MainActivity extends AppCompatActivity {

    TextView title;
    Button button;
    String imgURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        title = findViewById(R.id.title2);
        new genFilm().execute();
        button =findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new genFilm().execute();
            }
        });
    }

    private class genFilm extends AsyncTask<Void, Void, Void>{
        String result;
        @Override
        protected Void doInBackground(Void... voids) {
            int movieID = (int) (Math.random()*617001);
            String url = "https://api.themoviedb.org/3/movie/";
            String key = "cc6cada9d5dfe3c0f069fc0472fc604f";
            URL requestURL;
            try {
                requestURL = new URL(url + movieID + "?api_key=" + key);
                InputStreamReader in = new InputStreamReader(requestURL.openStream());
                BufferedReader bufferedReader = new BufferedReader(in);
                String stringBuffer;
                String string = "";
                while ((stringBuffer = bufferedReader.readLine()) != null){
                    string = String.format("%s%s", string, stringBuffer);
                }
                bufferedReader.close();
                result = string;


            } catch (IOException e){
                e.printStackTrace();
                result = e.toString();
                new genFilm().execute();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            try {
                JSONObject obj = new JSONObject(result);
                if(obj.getBoolean("adult")==true){
                    new genFilm().execute();
                }
                title.setText(obj.getString("title"));
                imgURL = obj.get("poster_path").toString();
                ImageView poster = findViewById(R.id.poster);
                Picasso.with(MainActivity.this).load("https://image.tmdb.org/t/p/w500"+imgURL).fit().placeholder(R.drawable.n1).into(poster);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onPostExecute(aVoid);
        }
    }

}


