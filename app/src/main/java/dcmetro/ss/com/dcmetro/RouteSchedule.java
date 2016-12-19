package dcmetro.ss.com.dcmetro;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class RouteSchedule extends AppCompatActivity {
    String RouteID;
    String Direction;
    String StopName;
    int DirectionNum;
    public static final String Server_Url= "https://api.wmata.com/Bus.svc/json/jRouteSchedule?RouteID=";
    public static final String Server_Url2= "&IncludingVariations=false&api_key=43abc63928e542f986a2ed9f2882e46f";
    ListView listView;
    JSONObject BusStop =  null;
    JSONArray Stops= new JSONArray();
    ArrayList<String > BusStops= new ArrayList<>();
    int StopID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_schedule);
        Intent intent = getIntent();
        RouteID = intent.getExtras().getString("RouteID").toString();
        Direction = intent.getExtras().getString("Direction").toString();
        DirectionNum = intent.getExtras().getInt("DirectionNum");
        listView = (ListView) findViewById(R.id.stop_list);

        GetStops getStops = new GetStops();
        getStops.execute();


    }
    class GetStops extends AsyncTask<Void,Void, Void>{

        @Override
        protected Void doInBackground(Void... params) {
            String link= Server_Url;
            link = link + RouteID;
            link = link + Server_Url2;

            try {
                URL url = new URL(link);
                BufferedReader reader = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream(), "UTF-8"));
                String jsonString = reader.readLine();
                Log.d("JSON", jsonString);

                JSONObject theResult = new JSONObject(jsonString);
                if(DirectionNum==0) {

                    JSONArray theDirection = theResult.getJSONArray("Direction0");
                    Log.d("Stops",theDirection.toString());
                    JSONObject theStops = null;
                    for(int i = 0;i< theDirection.length();i++) {
                        theStops = theDirection.getJSONObject(i);
                    }
                    Log.d("Stops",theStops.toString());
                    Stops = theStops.getJSONArray("StopTimes");
                    // JSONArray BusStops = Stops.getJSONArray()

                    JSONObject BusStop =  null;
                    for(int i=0;i<Stops.length();i++){
                        BusStop = Stops.getJSONObject(i);
                        BusStops.add(BusStop.getString("StopName"));

                    }
                    Log.d("Stops",BusStops.toString());

                }
                if(DirectionNum==1) {

                    JSONArray theDirection = theResult.getJSONArray("Direction1");
                    JSONObject theStops = null;
                    for(int i = 0;i< theDirection.length();i++) {
                        theStops = theDirection.getJSONObject(i);
                    }
                    Stops = theStops.getJSONArray("StopTimes");
                    // JSONArray BusStops = Stops.getJSONArray()


                    for(int i=0;i<Stops.length();i++){
                        BusStop = Stops.getJSONObject(i);
                        BusStops.add(BusStop.getString("StopName"));

                    }
                    Log.d("Stops",BusStops.toString());

                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(RouteSchedule.this,R.layout.support_simple_spinner_dropdown_item,BusStops);
            listView.setAdapter(arrayAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    int posi = position+1;
                    //Log.d("posi",String.valueOf(posi));
                    // Log.d("post",Stops.toString());
                    try {
                        for(int i=0;i<Stops.length();i++) {
                            BusStop = Stops.getJSONObject(i);
                            int seq = BusStop.getInt("StopSeq");
                            if(seq == posi){
                                StopID= BusStop.getInt("StopID");
                                StopName = BusStop.getString("StopName");
                                Intent intent = new Intent(RouteSchedule.this,BusStopSchedule.class);
                                intent.putExtra("StopID",StopID);
                                intent.putExtra("StopName",StopName);
                                intent.putExtra("RouteID",RouteID);
                                startActivity(intent);
                                //Toast.makeText(RouteSchedule.this,StopID + BusStop.getString("StopName"),Toast.LENGTH_LONG).show();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }



                }
            });
        }
    }
}