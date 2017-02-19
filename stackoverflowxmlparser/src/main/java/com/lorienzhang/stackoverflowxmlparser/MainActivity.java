package com.lorienzhang.stackoverflowxmlparser;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //URL：下载一个XML Feed，用于解析
    private static final String URL =
            "http://stackoverflow.com/feeds/tag?tagnames=android&sort=newest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_parse:
                new DownloadAndParseXmlTask().execute(URL);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class DownloadAndParseXmlTask extends AsyncTask<String, Void, List<StackOverflowXmlParser.Entry>> {

        @Override
        protected List doInBackground(String... params) {
            try {
                return loadXmlFromNetwork(params[0]);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<StackOverflowXmlParser.Entry> entries) {
            if(entries != null) {
                for(StackOverflowXmlParser.Entry entry : entries) {
                    Log.d("TAG", entry.title + " ### " + entry.link);
                }
            }
        }

        //从StackOverflow上下载XML Feed，并将Entry子标签解析出来返回。
        private List loadXmlFromNetwork(String urlString) throws IOException, XmlPullParserException {
            StackOverflowXmlParser parser = new StackOverflowXmlParser();
            InputStream is = getInputStreamFromUrl(urlString);
            List entries = parser.parse(is);
            return entries;
        }

        private InputStream getInputStreamFromUrl(String urlString) throws IOException {
            URL url = new URL(urlString);
            HttpURLConnection httpURLConnection =
                    (HttpURLConnection) url.openConnection();
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setConnectTimeout(10000);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();

            return httpURLConnection.getInputStream();
        }
    }
}
