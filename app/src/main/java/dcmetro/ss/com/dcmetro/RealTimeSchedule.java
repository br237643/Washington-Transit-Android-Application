package dcmetro.ss.com.dcmetro;

import android.content.Context;
import android.content.Intent;

import android.gesture.Prediction;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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

public class RealTimeSchedule extends AppCompatActivity {
    public final static String Server_Url = "https://api.wmata.com/NextBusService.svc/json/jPredictions?StopID=";
    public final static String Server_Url2="&api_key=43abc63928e542f986a2ed9f2882e46f";
    int StopID;
    TextView textView;
    ListView listView;
    ArrayList<String> Destination = new ArrayList<>();
    ArrayList<String> Minutes = new ArrayList<>();
    ArrayList<String>RouteID = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_time_schedule);
        Intent intent = getIntent();
        StopID = intent.getExtras().getInt("StopID");
        textView = (TextView) findViewById(R.id.stop_name);
        textView.setText(intent.getExtras().getString("StopName"));
        listView = (ListView) findViewById(R.id.real_time_bus);
        RealTimeBusSchedule realTimeBusSchedule = new RealTimeBusSchedule();
        realTimeBusSchedule.execute();

    }
    class RealTimeBusSchedule extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            String link = Server_Url;
            link = link + StopID;
            link = link + Server_Url2;
            Log.d("Link ", link);
            try {
                URL url = new URL(link);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream(), "UTF-8"));
                    String jsonString = reader.readLine();
                   // Log.d("JSON1", jsonString);

                    JSONObject theResult = new JSONObject(jsonString);
                //Log.d("The JSON", theResult.toString());
                JSONArray thePredictions = theResult.getJSONArray("Predictions");
                for(int i = 0;i<thePredictions.length();i++){
                    JSONObject Predictions = thePredictions.getJSONObject(i);
                    Destination.add(Predictions.getString("DirectionText"));
                    Minutes.add(Predictions.getString("Minutes"));
                    RouteID.add(Predictions.getString("RouteID"));

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
            ListAdapter3 adapter = new ListAdapter3(RealTimeSchedule.this, RouteID,Destination,Minutes);
            listView.setAdapter(adapter);

        }
    }
}
class ListAdapter3 extends ArrayAdapter {
    Context context;
    ArrayList<String> Destination = new ArrayList<>();
    ArrayList<String> Minutes = new ArrayList<>();
    ArrayList<String>RouteID = new ArrayList<>();

    public ListAdapter3(RealTimeSchedule context, ArrayList<String> RouteID, ArrayList<String> Destination, ArrayList<String> Minutes) {
        super(context, R.layout.schedule_row,R.id.routeID,RouteID);
        this.RouteID = RouteID;
        this.Destination = Destination;
        this.Minutes = Minutes;
        this.context = context;

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
        ListAdapter3.MyViewHolder holder= null;
        Log.d("getView","Hello");
        if(row==null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.schedule_row, parent, false);
            holder = new ListAdapter3.MyViewHolder(row);
            row.setTag(holder);
        }
        else{
            holder= (ListAdapter3.MyViewHolder) row.getTag();
        }

        holder.textView.setText(RouteID.get(position));
        holder.textView1.setText(Destination.get(position));
        holder.textView2.setText("                 "+Minutes.get(position) +"Mins");

        return row;


    }
}