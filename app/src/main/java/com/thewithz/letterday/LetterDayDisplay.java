package com.thewithz.letterday;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

public class LetterDayDisplay extends AppCompatActivity {
    public static final String TAG = "LetterDay";
    private String letterday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_letter_day_display);
        Intent i = new Intent(this, RegistrationService.class);
        startService(i);
    }

    @Override
    protected void onResume() {
        super.onResume();
        TextView txtViewLetterDay = (TextView) findViewById(R.id.txtViewLetterDay);
        UpdateTask task = new UpdateTask();
        task.execute();
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (txtViewLetterDay != null) {
            switch (letterday) {
                case "A":
                    txtViewLetterDay.setText(R.string.letter_day_a);
                    break;
                case "B":
                    txtViewLetterDay.setText(R.string.letter_day_b);
                    break;
                case "C":
                    txtViewLetterDay.setText(R.string.letter_day_c);
                    break;
                case "D":
                    txtViewLetterDay.setText(R.string.letter_day_d);
                    break;
                case "E":
                    txtViewLetterDay.setText(R.string.letter_day_e);
                    break;
                case "F":
                    txtViewLetterDay.setText(R.string.letter_day_f);
                    break;
                case "0":
                    txtViewLetterDay.setText(R.string.letter_day_null);
                    break;
                default:
                    txtViewLetterDay.setText(letterday);
                    break;
            }
        }
    }

    public static String getLetterDay() {
        try {
            JSONObject json = readJsonFromUrl("http://www.letterday.info/today/json");
            if (json.getString("school").equals("true"))
                return json.getString("letter");
            else if (json.getString("school").equals("false"))
                return "0";
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "uh oh";
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    private static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }

    private class UpdateTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            letterday = getLetterDay();
            Log.i(TAG, letterday);
            return letterday;
        }

    }
}
