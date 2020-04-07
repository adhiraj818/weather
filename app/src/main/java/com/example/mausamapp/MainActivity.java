package com.example.mausamapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    TextView txt;
    TextView city;
    TextView temperature;
    String mausam="";

    public void findweather(View view){

        DownloadTask task=new DownloadTask();
        task.execute("http://api.openweathermap.org/data/2.5/weather?q="+city.getText()+"&appid=874a164cc56fd1b3d6b38e0f0029932c");


    }

    public class DownloadTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... urls) {

            String result="";
            URL url;
            HttpURLConnection urlConnection=null;
            try{
                url=new URL(urls[0]);
                urlConnection=(HttpURLConnection)url.openConnection();
                InputStream in=urlConnection.getInputStream();
                InputStreamReader reader=new InputStreamReader(in);

                int data =reader.read();

                while(data!=-1){
                    char current= (char) data;
                    result=result+current;
                    data=reader.read();
                }

                return result;

            }
            catch(Exception e){
                e.printStackTrace();
                return "there is some error occured please contact developer";
            }

        }

       @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.i("mausams is :",s);

           try {
               JSONObject jso=new JSONObject(s);
                String info =jso.getString("weather");
                Log.i("weather is::",info);
                JSONArray arr= new JSONArray(info);
               JSONObject jspart=new JSONObject();

               for(int i=0; i<arr.length();i++){

                   jspart=arr.getJSONObject(i);
               }
               txt.setText("weather ->"+jspart.get("description"));

               String t="[";
                       t+=jso.getString("main");
                       t+="]";
               Log.i("temperature is ",t);
             JSONArray temp_arr;
               temp_arr = new JSONArray(t);
               JSONObject temp =new JSONObject();

             for(int i=0;i<temp_arr.length();i++){

                 temp=temp_arr.getJSONObject(i);

             }
             Log.i("tempersture ",temp.getString("temp"));


               float cel =Float.parseFloat(temp.getString("temp"));
               cel= (float) (cel-273.15);



               temperature.setText("temp "+Float.toString(cel)+"C         pressure "+temp.getString("pressure")+"hpa        humidity "+temp.getString("humidity")+"%");

           } catch (JSONException e) {
               e.printStackTrace();
           }



       }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txt=(TextView)findViewById(R.id.details);
        city=(TextView)findViewById(R.id.cityname);
        temperature=(TextView)findViewById(R.id.temperature);

    }
}
