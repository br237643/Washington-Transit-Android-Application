package dcmetro.ss.com.dcmetro;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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

public class MetroSchedule extends AppCompatActivity {
    String StationName;
    String StationCode;
    ListView listView;
    TextView textView;
    ArrayList<String> DestinationName = new ArrayList<>();
    ArrayList<String> Line = new ArrayList<>();
    ArrayList<String> Min = new ArrayList<>();
    String Location;
    public static String ServerURL  = "https://api.wmata.com/StationPrediction.svc/json/GetPrediction/";
    public static String ServerURL2 = "?api_key=43abc63928e542f986a2ed9f2882e46f";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metro_schedule);
        Intent intent = getIntent();
        StationName = intent.getExtras().getString("MetroStationName");
        StationCode = intent.getExtras().getString("MetroStationCode");
        listView = (ListView) findViewById(R.id.real_time_scehdule);
        textView = (TextView) findViewById(R.id.Location);
        GetMetroRealTimeSchedule getMetroRealTimeSchedule = new GetMetroRealTimeSchedule();
        getMetroRealTimeSchedule.execute();

    }
    class GetMetroRealTimeSchedule extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            String link = ServerURL;
            link = link + StationCode;
            link = link + ServerURL2;

            try {
                URL url = new URL(link );
                BufferedReader reader = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream(), "UTF-8"));
                String jsonString = reader.readLine();
                // Log.d("JSON", jsonString);
                String Value;
                JSONObject theResult = new JSONObject(jsonString);
                JSONArray theTrains = theResult.getJSONArray("Trains");
                for(int i=0;i<theTrains.length();i++){
                    JSONObject theTrainDetails = theTrains.getJSONObject(i);
                    DestinationName.add(theTrainDetails.getString("DestinationName"));
                    Min.add(theTrainDetails.getString("Min"));
                    Value = theTrainDetails.getString("Line");
                    Location = theTrainDetails.getString("LocationName");
                    if(Value !="--"){
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
                        Line.add(Value);
                    }else {
                        Line.add("Information Not Available");
                    }


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
            ListAdapter2 adapter = new ListAdapter2(MetroSchedule.this, Line,DestinationName,Min);
            listView.setAdapter(adapter);
            textView.setText("AT LOCATION : "+Location);

        }
    }
}

class ListAdapter2 extends ArrayAdapter {
    Context context;
    ArrayList<String> Line = new ArrayList<>();
    ArrayList<String> DestinationName = new ArrayList<>();
    ArrayList<String>Min = new ArrayList<>();




    public ListAdapter2(MetroSchedule context, ArrayList<String> line, ArrayList<String> destinationName, ArrayList<String> min) {
        super(context,R.layout.schedule_row,R.id.routeID,line);
        this.context = context;
        this.Line = line;
        this.DestinationName= destinationName;
        this.Min=min;
    }

    class MyViewHolder{

        TextView textView ;
        TextView textView1;
        TextView textView2;
        MyViewHolder(View v){

            textView = (TextView) v.findViewById(R.id.routeID);
            textView1= (TextView) v.findViewById(R.id.towards);
            textView2= (TextView) v.findViewById(R.id.time);
        }
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ListAdapter2.MyViewHolder holder= null;
        Log.d("getView","Hello");
        if(row==null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.schedule_row, parent, false);
            holder = new ListAdapter2.MyViewHolder(row);
            row.setTag(holder);
        }
        else{
            holder= (ListAdapter2.MyViewHolder) row.getTag();
        }

        holder.textView.setText(Line.get(position));
        holder.textView1.setText(DestinationName.get(position));
        holder.textView2.setText("                 "+Min.get(position) +"mins");

        return row;


    }
}
