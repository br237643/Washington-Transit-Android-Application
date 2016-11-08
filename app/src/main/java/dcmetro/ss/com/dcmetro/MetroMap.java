package dcmetro.ss.com.dcmetro;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MetroMap extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metro_map);
    }
}





//        Override overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
//        TouchImageView map = (TouchImageView) findViewById(R.id.map_img);
//        map.setImageDrawable(getResources().getDrawable(R.drawable.img_metro));
//        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_stations);
//        toolbar.setTitle("Metro Map");
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });
//    }
//
//    @Override
//    public void finish() {
//        super.finish();
//        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
//    }
//}