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
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

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
import java.util.List;

import static android.R.attr.resource;

public class BusStopSchedule extends AppCompatActivity {
    String RouteId;
    int StopID;
    TextView StopName;
    String Name;
    ListView listView;
    TextView real;
    ArrayList<String> TimeList = new ArrayList<String>();
    ArrayList<String> RouteID = new ArrayList<>();
    ArrayList<String>Towards = new ArrayList<>();
    ArrayList<String>Directoin = new ArrayList<>();
    public final static String Server_Url = "https://api.wmata.com/Bus.svc/json/jStopSchedule?StopID=";
    public final static String Server_Url2="&api_key=43abc63928e542f986a2ed9f2882e46f";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_stop_schedule);
        Intent intent = getIntent();
        RouteId = intent.getExtras().getString("RouteID");
        StopID = intent.getExtras().getInt("StopID");
        Name = intent.getExtras().getString("StopName");
        StopName = (TextView) findViewById(R.id.textView7);
        //Log.d("Stop",Name);
        StopName.setText(Name);
        listView = (ListView) findViewById(R.id.ScheduleList);
        //Log.d("Actu", StopID + RouteID);
        real = (TextView) findViewById(R.id.textView4);
        real.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BusStopSchedule.this,RealTimeSchedule.class);
                intent.putExtra("StopID",StopID);
                intent.putExtra("StopName",Name);
                startActivity(intent);
            }
        });
        GetSchedule getSchedule = new GetSchedule();
        getSchedule.execute();

    }

    class GetSchedule extends AsyncTask<Void , Void , Void>{

        @Override
        protected Void doInBackground(Void... params) {
            String link = Server_Url;
            link = link + StopID;
            link = link + Server_Url2;
            Log.d("BusStop StopID", String.valueOf(StopID));
            URL url = null;

            try {
               url = new URL(link);
                BufferedReader reader  = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream(), "UTF-8"));
                String jsonString = reader.readLine();
                Log.d("JSON1", jsonString);

                JSONObject theResult = new JSONObject(jsonString);
                //Log.d("JSON ", theResult.toString());
                JSONArray theSchedule = theResult.getJSONArray("ScheduleArrivals");
                //Log.d("Schedule", theSchedule.toString());
                JSONObject First = null;
                String Time = null;
                //String TimeList;

                int TimeOP = 0;
                //JSONArray TimeList = null;
                for(int i =0; i<theSchedule.length();i++){
                    First = theSchedule.getJSONObject(i);
                    Time = First.getString("ScheduleTime");
                    TimeOP = Time.indexOf("T");
                    TimeList.add( Time.substring(TimeOP + 1));
                    RouteID.add(First.getString("RouteID"));
                    Towards.add(First.getString("TripHeadsign"));
                    Directoin.add(First.getString("TripDirectionText"));
                    //Log.d("Time", Directoin.toString());


                }


            } catch (MalformedURLException e) {

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
            ListAdapter1 adapter = new ListAdapter1(BusStopSchedule.this, RouteID,Directoin,Towards,TimeList);
            listView.setAdapter(adapter);

        }
    }
}

class ListAdapter1 extends ArrayAdapter {
    Context context;
    ArrayList<String> TimeList = new ArrayList<>();
    ArrayList<String> RouteID = new ArrayList<>();
    ArrayList<String>Towards = new ArrayList<>();
    ArrayList<String>Directoin = new ArrayList<>();

    public ListAdapter1(Context context, ArrayList<String> RouteID, ArrayList<String> Direction, ArrayList<String> Towards, ArrayList<String> TimeList) {
        super(context, R.layout.schedule_row,R.id.routeID,RouteID);
        this.RouteID = RouteID;
        this.Directoin = Direction;
        this.Towards = Towards;
        this.TimeList = TimeList;
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
        ListAdapter1.MyViewHolder holder= null;
        Log.d("getView","Hello");
        if(row==null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.schedule_row, parent, false);
            holder = new ListAdapter1.MyViewHolder(row);
            row.setTag(holder);
        }
        else{
            holder= (ListAdapter1.MyViewHolder) row.getTag();
        }

        holder.textView.setText(RouteID.get(position));
        holder.textView1.setText(Towards.get(position)+ "  (" + Directoin.get(position) +")");
        holder.textView2.setText("                 "+TimeList.get(position));

        return row;


    }
}