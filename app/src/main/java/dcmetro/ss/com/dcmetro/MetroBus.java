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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

public class MetroBus extends MainActivity {
    public static final String Server_Url= "https://api.wmata.com/Bus.svc/json/jRoutes?api_key=43abc63928e542f986a2ed9f2882e46f";
    ListView listView;
    Button button;
    TextView tv;
    ArrayList<String> RouteID = new ArrayList<String>();
    ArrayList<String> LineDescription = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metro_bus);
        GetAllRoutes getAllRoutesData = new GetAllRoutes();
        getAllRoutesData.execute();
        listView= (ListView) findViewById(R.id.list);


        //  RouteID = new ArrayList<String>();
        //LineDescription = new ArrayList<String>();




    }


    class GetAllRoutes extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {
            BufferedReader reader = null;
            try {
                URL theUrl = new URL(Server_Url);
                reader= new BufferedReader(new InputStreamReader(theUrl.openConnection().getInputStream(), "UTF-8"));
                String jsonString = reader.readLine();
                Log.d("JSON", jsonString);

                JSONObject theResult = new JSONObject(jsonString);
                JSONArray resultArray = theResult.getJSONArray("Routes");



                for(int i =0; i<resultArray.length();i++){
                    JSONObject RouteId= resultArray.getJSONObject(i);

                    RouteID.add(RouteId.getString("RouteID"));
                    LineDescription.add(RouteId.getString("LineDescription"));

                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            finally {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //Set Adapter here
            Log.d("Line" , LineDescription.toString());
            Log.d("ID's", RouteID.toString());

            ListAdapter adapter = new ListAdapter(MetroBus.this, RouteID,LineDescription);

            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(MetroBus.this, RouteStations.class);
                    intent.putExtra("RouteID",RouteID.get(i));
                    startActivity(intent);
                }
            });
        }
    }
}

class ListAdapter extends ArrayAdapter<String> {

    ArrayList<String> RouteID = null;
    ArrayList<String> LineDesc = null;
    Context context;


    public ListAdapter(Context context, ArrayList<String> routeID, ArrayList<String> lineDesc) {
        super(context, R.layout.single_row_layout,R.id.textView,routeID);
        this.RouteID= routeID;
        this.LineDesc= lineDesc;
        this.context=context;

    }
    class MyViewHolder{

        TextView textView ;
        TextView textView1;
        MyViewHolder(View v){

            textView = (TextView) v.findViewById(R.id.textView);
            textView1= (TextView) v.findViewById(R.id.textView1);
        }
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        MyViewHolder holder= null;
        if(row==null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.single_row_layout, parent, false);
            holder = new MyViewHolder(row);
            row.setTag(holder);
        }
        else{
            holder= (MyViewHolder) row.getTag();
        }

        holder.textView.setText(RouteID.get(position));
        holder.textView1.setText(LineDesc.get(position));

        return row;


    }

}
