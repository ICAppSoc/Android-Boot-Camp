package uk.co.icappsoc.projectsunshine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class DetailActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new DetailFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A fragment displaying weather details.
     */
    public static class DetailFragment extends Fragment {

        private String forecastStr;
        public DetailFragment() {
            setHasOptionsMenu(true);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
            TextView weatherDetail = (TextView) rootView.findViewById(R.id.detail_text);

            Intent intent = getActivity().getIntent();
            if(null != intent && intent.hasExtra(Intent.EXTRA_TEXT)){
                forecastStr = intent.getStringExtra(Intent.EXTRA_TEXT);
                weatherDetail.setText(forecastStr);
            }

            return rootView;
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
            // Inflate the menu; this adds items to the app bar if it is present
            inflater.inflate(R.menu.detail_fragment, menu);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item){
            if(item.getItemId() == R.id.action_share){
                // Share our forecast to brag to the world about your local weather!
                startActivity(createShareForecastIntent());
                return true;
            }
            return super.onOptionsItemSelected(item);
        }

        private Intent createShareForecastIntent(){
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, forecastStr + " #SunshineApp #ICAppSoc");
            return shareIntent;
        }
    }
}
