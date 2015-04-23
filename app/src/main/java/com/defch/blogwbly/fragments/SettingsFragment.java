package com.defch.blogwbly.fragments;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.view.View;

import com.defch.blogwbly.MyApplication;
import com.defch.blogwbly.R;
import com.defch.blogwbly.activities.SettingsActivity;
import com.defch.blogwbly.ui.WeeblyThemes;
import com.defch.blogwbly.util.LogUtil;


/**
 * Created by DiegoFranco on 4/16/15.
 */
public class SettingsFragment extends PreferenceFragment  implements Preference.OnPreferenceChangeListener{

    private MyApplication app;
    private boolean mFirstLaunch = true;

    public static SettingsFragment createInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = MyApplication.getInstance(getActivity().getApplicationContext());
        addPreferencesFromResource(R.xml.settings);
        bindPreference(findPreference(SettingsActivity.THEME_KEY));
        findPreference(SettingsActivity.KEY_DARK_THEME).setOnPreferenceChangeListener(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFirstLaunch = false;
    }

    private void bindPreference(Preference preference) {
        preference.setOnPreferenceChangeListener(this);
        onPreferenceChange(preference, app.getPreferences().getString(preference.getKey(), ""));
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object object) {
        if (preference instanceof ListPreference) {
            if (preference.getKey().equals(SettingsActivity.THEME_KEY)) {
                ListPreference listPreference = (ListPreference) preference;
                int prefIndex = listPreference.findIndexOfValue(object.toString());
                if (prefIndex >= 0) {
                    preference.setSummary(listPreference.getEntries()[prefIndex]);
                }
                boolean isDarkTheme = app.getWTheme().isDarkTheme;
                WeeblyThemes theme = WeeblyThemes.getThemeFromString(listPreference.getEntries()[prefIndex].toString());
                theme.isDarkTheme = isDarkTheme;
                app.setTheme(theme);
                if (!mFirstLaunch) {
                    getActivity().recreate();
                }
            }
            return true;
        } else if (preference instanceof CheckBoxPreference) {
            if (preference.getKey().equals(SettingsActivity.KEY_DARK_THEME)) {
                app.getWTheme().isDarkTheme = (Boolean) object;
                getActivity().recreate();
            }
            return true;
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            String version = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName;
            findPreference("version").setSummary(version);
        } catch (PackageManager.NameNotFoundException e) {
            LogUtil.e("Settings Activity", "Unable to get version summary", e);
        }
    }
}
