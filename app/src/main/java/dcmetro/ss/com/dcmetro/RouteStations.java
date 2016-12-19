package dcmetro.ss.com.dcmetro;

import android.support.v7.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Path;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;


//import static com.example.bhavanisrinivas.dcpublictransit.RouteStations.Server_Url;
//import static com.example.bhavanisrinivas.dcpublictransit.RouteStations.Server_Url2;

public class RouteStations extends AppCompatActivity {
    public static final String Server_Url = "https://api.wmata.com/Bus.svc/json/jRouteDetails?RouteID=";
    public static final String Server_Url2 = "&api_key=43abc63928e542f986a2ed9f2882e46f";
    String RouteID = null;
    TextView tv;
    TextView tv2;
    String Direction0;
    String Direction1;
    String Directions0;
    String Directions1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_stations2);

        Intent intent = getIntent();
        RouteID = intent.getExtras().getString("RouteID").toString();
        tv = (TextView) findViewById(R.id.textView2);
        tv2 = (TextView) findViewById(R.id.textView3);

        GetRouteDirections getRouteDirections = new GetRouteDirections();
        getRouteDirections.execute();


    }



    class GetRouteDirections extends AsyncTask<Void,Void,Integer> {



        @Override
        protected Integer doInBackground(Void... Void) {
            String link = Server_Url;
            link = link + RouteID;
            link = link + Server_Url2;

            Log.d("Link", link);
            try {
                URL url = new URL(link);
                BufferedReader reader = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream(), "UTF-8"));
                String jsonString = reader.readLine();
                Log.d("JSON", jsonString);

                JSONObject theResult = new JSONObject(jsonString);

                if(!theResult.isNull("Direction0")) {
                    JSONObject theDirections0 = theResult.getJSONObject("Direction0");
                    // JSONStringer Direction0 =  theDirections0.getJSONObject("TripHeadsign");
                    Direction0 = theDirections0.getString("TripHeadsign");
                    Directions0 = theDirections0.getString("DirectionText");
                    Log.d("Direction ", Direction0);


                }

                if(!theResult.isNull("Direction1")) {
                    JSONObject theDirections1 = theResult.getJSONObject("Direction1");
                    //JSONObject Direction1 =  theDirections1.getJSONObject("TripHeadsign");
                    Direction1  = theDirections1.getString("TripHeadsign");
                    Directions1 = theDirections1.getString("DirectionText");

                    Log.d("Direction ", Direction1);

                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();

            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d("FINISHED","done with Do in Background");
            return 1;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            Log.d("Hello","hello");
            if(Direction0!=null) {
                tv.setText(Direction0 + "    (" + Directions0 +  ")");
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(RouteStations.this, RouteSchedule.class);
                        intent.putExtra("Direction", Direction0);
                        intent.putExtra("RouteID", RouteID);
                        intent.putExtra("DirectionNum",0);
                        startActivity(intent);

                    }
                });
            }
            if(Direction1!=null) {
                tv2.setText(Direction1 + "    (" +Directions1+")");
                tv2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(RouteStations.this, RouteSchedule.class);
                        intent.putExtra("Direction", Direction1);
                        intent.putExtra("RouteID", RouteID);
                        intent.putExtra("DirectionNum",1);
                        startActivity(intent);

                    }
                });
            }
        }
    }
}

