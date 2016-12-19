package dcmetro.ss.com.dcmetro;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Near extends AppCompatActivity{
   public static final double Latitude= 39.0046;
    public static final double Longitude=76.8755;
    public static final String Server_URL = "https://api.wmata.com/Bus.svc/json/jStops?Lat="+Latitude+"&Lon=-"+Longitude+"+&Radius=500&api_key=43abc63928e542f986a2ed9f2882e46f";
    ArrayList<String> StopName = new ArrayList<>();
    ArrayList<Integer> StopID = new ArrayList<>();
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near);
        listView = (ListView) findViewById(R.id.list_near);
        GetNearByStations getNearByStations = new GetNearByStations();
        getNearByStations.execute();

    }

    class GetNearByStations extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            Log.d("Link",Server_URL);
            try {
                URL url = new URL(Server_URL);
                BufferedReader reader= new BufferedReader(new InputStreamReader(url.openConnection().getInputStream(), "UTF-8"));
                String jsonString = reader.readLine();
               // Log.d("JSON1", jsonString);

                JSONObject theResult = new JSONObject(jsonString);

                JSONArray resultArray = theResult.getJSONArray("Stops");
               // Log.d("Results",resultArray.toString());


                for(int i =0; i<resultArray.length();i++){
                    JSONObject Stop= resultArray.getJSONObject(i);
                    //Log.d("StopID",Stop.getString("StopID"));
                   // Log.d("Name",Stop.getString("Name"));
                    StopID.add(Stop.getInt("StopID"));
                    StopName.add(Stop.getString("Name"));


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
        ArrayAdapter adapter = new ArrayAdapter(Near.this,R.layout.support_simple_spinner_dropdown_item,StopName);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(Near.this,BusStopSchedule.class);
                    intent.putExtra("StopID",StopID.get(i));
                    Log.d("Near STopId", String.valueOf(StopID.get(i)));
                    intent.putExtra("StopName",StopName.get(i));

                    startActivity(intent);
                }
            });

        }
    }

}
