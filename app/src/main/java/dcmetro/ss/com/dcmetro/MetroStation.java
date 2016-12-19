package dcmetro.ss.com.dcmetro;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MetroStation extends AppCompatActivity {
    String LineName;
    String LineCode;
    String StationStart;
    String StationEnd;
    ArrayList<String> StationCode = new ArrayList<>();
    ArrayList<String> StationName = new ArrayList<>();
    ListView listView;
    public static final String Server_URl ="https://api.wmata.com/Rail.svc/json/jStations?LineCode=";
    public static final String Server_URl1 ="&ToStationCode=";
    public static final String Server_URl2 ="&api_key=43abc63928e542f986a2ed9f2882e46f";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metro_station);
        listView = (ListView) findViewById(R.id.metro_station_list);
        Intent intent = getIntent();
        LineName = intent.getExtras().getString("LineName");
        LineCode = intent.getExtras().getString("LineCode");
        StationEnd = intent.getExtras().getString("StationEnd");
        StationStart = intent.getExtras().getString("StationStart");
        GetTrainPath getTrainPath = new GetTrainPath();
        getTrainPath.execute();
        //Log.d("StationName",StationName);
    }

    class GetTrainPath extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            String link = Server_URl;
            link = link + LineCode;
            link = link + Server_URl2;
            Log.d("Link ", link);
            try {
                URL url = new URL(link);
                BufferedReader reader = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream(), "UTF-8"));
                String jsonString = reader.readLine();
                // Log.d("JSON", jsonString);
                String Name;
                String Value = "";
                String Value1="";
                String Value2="";
                String Lines= "";
                JSONObject theResult = new JSONObject(jsonString);
                JSONArray thePath = theResult.getJSONArray("Stations");
                for(int i=0;i<thePath.length();i++){

                    JSONObject theStation = thePath.getJSONObject(i);
                   // Log.d("StationCode", theStation.getString("Code"));
                    //Log.d("StationName", theStation.getString("Name"));
                    //Log.d("Line1",theStation.getString("LineCode1"));
                    //Log.d("Line2",theStation.getString("LineCode2"));
                    //Log.d("Line3",theStation.getString("LineCode3"));
                    Name = theStation.getString("Name");
                    Value = theStation.getString("LineCode1");
                    Value1 = theStation.getString("LineCode2");
                    Value2 = theStation.getString("LineCode3");
                    if(Value !=null){

                        switch (Value){
                            case "BL": Value = "Black";
                                break;
                            case "GR": Value = "Green";
                                break;
                            case "OR": Value = "Orange";
                                break;
                            case "RD": Value = "Red";
                                break;
                            case "SV":Value = "Silver";
                                break;
                            case "YL":Value = "Yellow";
                                break;
                            case "null":
                                break;
                        }
                        Lines = Value;

                    }

                    if(Value1!=null){

                        switch (Value1){
                            case "BL": Value1 = "Black";
                                break;
                            case "GR": Value1 = "Green";
                                break;
                            case "OR": Value1 = "Orange";
                                break;
                            case "RD": Value1 = "Red";
                                break;
                            case "SV":Value1 = "Silver";
                                break;
                            case "YL":Value1 = "Yellow";
                                break;
                            case "null":
                                break;
                        }if(Value1!="null") {
                            Lines = Lines + ", " + Value1;
                        }
                    }

                    if(Value2 !=null){

                        switch (Value2){
                            case "BL": Value2 = "Black";
                                break;
                            case "GR": Value2 = "Green";
                                break;
                            case "OR": Value2 = "Orange";
                                break;
                            case "RD": Value2 = "Red";
                                break;
                            case "SV":Value2 = "Silver";
                                break;
                            case "YL":Value2 = "Yellow";
                                break;
                            case "null":
                                break;
                        }
                        if(Value2!="null") {
                            Lines = Lines + ", " + Value2;
                        }
                    }

                    Log.d("name",Name +"(" +Lines+")");
                   StationCode.add(theStation.getString("Code"));
                    StationName.add( Name + "\n ( " +Lines+")");


                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {

                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            ArrayAdapter adapter = new ArrayAdapter(MetroStation.this, R.layout.support_simple_spinner_dropdown_item,StationName);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(MetroStation.this, MetroSchedule.class);
                    Log.d("Station Name ", StationName.get(i));
                    intent.putExtra("MetroStationName",StationName.get(i));
                    intent.putExtra("MetroStationCode",StationCode.get(i));
                    startActivity(intent);
                }
            });

        }
    }
}
