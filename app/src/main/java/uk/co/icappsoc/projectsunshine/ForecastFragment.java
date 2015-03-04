package uk.co.icappsoc.projectsunshine;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * A view containing our list of weather forecasts.
 */
public class ForecastFragment extends Fragment {

    private ArrayAdapter<String> forecastAdapter;

    public ForecastFragment() {
        // Must call to make sure onCreateOptionsMenu and onOptionsItemSelected
        // are called by the system.
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        // Ctrl/Cmd + Click on R.menu.forecast_fragment to see the menu we're loading here.
        inflater.inflate(R.menu.forecast_fragment, menu);
    }

    // Called by the system when a menu item is pressed by the user.
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == R.id.action_refresh){
            // If it's the refresh menu button, fetch and display our weather data!
            updateWeather();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Called by the system when our Fragment is first created,
    // here we set up the UI for the first time.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Now that we have some dummy forecast data, create an ArrayAdapter.
        // The ArrayAdapter will take data from a source (like our dummy forecast) and
        // use it to populate the ListView it's attached to.
        forecastAdapter =
                new ArrayAdapter<>(
                        getActivity(), // The current context (this activity)
                        R.layout.list_item_forecast, // The name of the layout ID.
                        R.id.list_item_forecast_textview, // The ID of the textview to populate.
                        new ArrayList<String>());

        View rootView = inflater.inflate(
                R.layout.fragment_main, container, false);

        // Get a reference to the ListView, and attach this adapter to it.
        ListView listView =
                (ListView) rootView.findViewById(R.id.listview_forecast);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String forecast = forecastAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra(Intent.EXTRA_TEXT, forecast);
                startActivity(intent);
            }
        });
        listView.setAdapter(forecastAdapter);

        return rootView;
    }

    /** Starts a background worker to fetch the latest weather asynchronously. */
    private void updateWeather(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        // Retrieve user's location preference, or use "London" by default
        String location = prefs.getString(getString(R.string.pref_location_key), "London");

        FetchWeatherTask weatherTask = new FetchWeatherTask(getActivity(), forecastAdapter);
        weatherTask.execute(location); // Note we pass in the location as a parameter!
    }

    @Override
    public void onStart(){
        super.onStart();
        updateWeather();
    }
}
