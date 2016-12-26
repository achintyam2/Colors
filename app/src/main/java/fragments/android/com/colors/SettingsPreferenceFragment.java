package fragments.android.com.colors;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.util.Log;
import android.view.MenuItem;
import static fragments.android.com.colors.SettingsActivity.bindPreferenceSummaryToValue;

/**
 * This fragment shows notification preferences only. It is used when the
 * activity is showing a two-pane settings UI.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class SettingsPreferenceFragment extends PreferenceFragment {

    public SwitchPreference wifi, bluetooth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.pref_settings);
        setHasOptionsMenu(true);

        wifi = (SwitchPreference) findPreference("example_wifi");
        bluetooth = (SwitchPreference) findPreference("example_bluetooth");

        if (wifi != null) {
            wifi.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    boolean wifi_test = Boolean.parseBoolean(newValue.toString());
                    Log.d("tag", "Is Wifi on: " + wifi_test);
                    if (wifi_test)
                        wifi.setSummary("Enabled");
                    else
                        wifi.setSummary("Disabled");

                    return true;
                }
            });
        }
        if (bluetooth != null) {
            bluetooth.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    boolean bluetooth_test = Boolean.parseBoolean(newValue.toString());
                    Log.d("tag", "Is Bluetooth on: " + bluetooth_test);
                    if (bluetooth_test)
                        bluetooth.setSummary("Enabled");
                    else
                        bluetooth.setSummary("Disabled");

                    return true;
                }
            });
        }
        // Bind the summaries of EditText/List/Dialog/Ringtone preferences
        // to their values. When their values change, their summaries are
        // updated to reflect the new value, per the Android Design
        // guidelines.
        bindPreferenceSummaryToValue(findPreference("example_settings_text"));
        bindPreferenceSummaryToValue(findPreference("example_settings_list"));
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            startActivity(new Intent(getActivity(), SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}


