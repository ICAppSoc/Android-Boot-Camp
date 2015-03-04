package uk.co.icappsoc.projectsunshine;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import uk.co.icappsoc.projectsunshine.data.WeatherContract;

/**
 * A view containing our list of weather forecasts.
 */
public class ForecastFragment extends Fragment {

    private ForecastAdapter forecastAdapter;

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
        String locationSetting = Utility.getPreferredLocation(getActivity());

        // Sort order:  Ascending, by date.
        String sortOrder = WeatherContract.WeatherEntry.COLUMN_DATE + " ASC";
        Uri weatherForLocationUri = WeatherContract.WeatherEntry.buildWeatherLocationWithStartDate(
                locationSetting, System.currentTimeMillis());

        Cursor cur = getActivity().getContentResolver().query(weatherForLocationUri,
                null, null, null, sortOrder);

        // The CursorAdapter will take data from our cursor and populate the ListView
        // However, we cannot use FLAG_AUTO_REQUERY since it is deprecated, so we will end
        // up with an empty list the first time we run.
        forecastAdapter = new ForecastAdapter(getActivity(), cur, 0);

        View rootView = inflater.inflate(
                R.layout.fragment_main, container, false);

        // Get a reference to the ListView, and attach this adapter to it.
        ListView listView =
                (ListView) rootView.findViewById(R.id.listview_forecast);
        listView.setAdapter(forecastAdapter);

        return rootView;
    }

    /** Starts a background worker to fetch the latest weather asynchronously. */
    private void updateWeather(){
        FetchWeatherTask weatherTask = new FetchWeatherTask(getActivity());
        String location = Utility.getPreferredLocation(getActivity());
        weatherTask.execute(location); // Note we pass in the location as a parameter!
    }

    @Override
    public void onStart(){
        super.onStart();
        updateWeather();
    }
}
