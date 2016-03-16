package com.example.huzzah.messagelocator;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Huzzah on 11.3.2016.
 */
public class BackgroundTask extends AsyncTask<String,Void,String> {
    Context ctx;
    BackgroundTask(Context ctx)
    {
        this.ctx = ctx;
    }



    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    //Palauttaa Toastilla "Message success..." -viestin jos onnistuu.
    @Override
    protected void onPostExecute(String result) {
        Toast.makeText(ctx, result, Toast.LENGTH_LONG).show();
    }

    @Override
    protected String doInBackground(String... params) {
        //PHP-sivun joka vastaanottaa tietoja ja syöttää ne MySQL-tietokantaan
        String login_url = "http://192.168.1.103/messageboard.php";
        String text = params[0];
        String username = params[1];
        String latitude = params[2];
        String longitude = params[3];
        //Placeholdereita
        //String latitude = "62.296";
        //String longitude = "22.7685";
        try {
            URL url = new URL(login_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            OutputStream OS = httpURLConnection.getOutputStream();

            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
            //URL-enkoodatut tiedot, jotka POSTataan PHP-sivun kautta tietokantaan.
            String data = URLEncoder.encode("username","UTF-8") +"=" +URLEncoder.encode(username, "UTF-8") + "&"+
                    URLEncoder.encode("text","UTF-8") + "=" +URLEncoder.encode(text, "UTF-8") + "&"+
                    URLEncoder.encode("latitude","UTF-8") + "=" +URLEncoder.encode(latitude, "UTF-8") + "&"+
                    URLEncoder.encode("longitude","UTF-8") + "=" +URLEncoder.encode(longitude, "UTF-8");
            bufferedWriter.write(data);
            bufferedWriter.flush();
            bufferedWriter.close();
            OS.close();
            //Haetaan ja suljetaan virta inputstreamiin
            InputStream IS = httpURLConnection.getInputStream();
            IS.close();
            return "Message success...";
            }
        catch (IOException e) {

            e.printStackTrace();
        }
        return null;
    }
}
