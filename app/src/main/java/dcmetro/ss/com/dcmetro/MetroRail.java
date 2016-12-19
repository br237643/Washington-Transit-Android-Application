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

public class MetroRail extends AppCompatActivity {
    public static final String Server_URl = "https://api.wmata.com/Rail.svc/json/jLines?api_key=43abc63928e542f986a2ed9f2882e46f";
    ListView listView;
    ArrayList<String> MetroList = new ArrayList<>();
    ArrayList<String>MetroListCode = new ArrayList<>();
    ArrayList<String> StationStart = new ArrayList<>();
    ArrayList<String>StationEnd = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metro_rail);

        listView = (ListView) findViewById(R.id.metro_list);
        GetMetroList getMetroList = new GetMetroList();
        getMetroList.execute();
    }

    class GetMetroList extends AsyncTask<Void,Void,Void>{


        @Override
        protected Void doInBackground(Void... voids) {
            try {
                URL url = new URL(Server_URl);
                BufferedReader reader = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream(), "UTF-8"));
                String jsonString = reader.readLine();
               // Log.d("JSON", jsonString);

                JSONObject theResult = new JSONObject(jsonString);
                JSONArray theArray = theResult.getJSONArray("Lines");
                for(int i =0;i<theArray.length();i++){
                    JSONObject theList = theArray.getJSONObject(i);
                    Log.d("List",theList.getString("DisplayName"));
                    MetroList.add(theList.getString("DisplayName")+" Line");
                    MetroListCode.add(theList.getString("LineCode"));
                    StationStart.add(theList.getString("StartStationCode"));
                    StationEnd.add(theList.getString("EndStationCode"));
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
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MetroRail.this,R.layout.support_simple_spinner_dropdown_item,MetroList);
            listView.setAdapter(arrayAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                    Intent intent = new Intent(MetroRail.this,MetroStation.class);
                                                    intent.putExtra("LineName", MetroList.get(i));
                                                    intent.putExtra("LineCode",MetroListCode.get(i));
                                                    intent.putExtra("StationStart",StationStart.get(i));
                                                    intent.putExtra("StationEnd",StationEnd.get(i));
                                                    startActivity(intent);
                                                }
                                            }
            );
        }
    }

}
