package esa.mydemo.setting;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Locale;

import esa.mydemo.MyApplication;
import esa.mydemo.R;
import esa.mylibrary.language.LanguageHelper;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            //服务器
            ListPreference lp = findPreference("server");
            lp.setSummary(lp.getEntry());
            lp.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    lp.setSummary(lp.getEntries()[lp.findIndexOfValue((String) newValue)]);
                    return true;
                }
            });

            //当前设备语言
            EditTextPreference editTextPreference = findPreference("devlanguage");

            //当前应用程序语言
            ListPreference listPreferenceLanguage = findPreference("language");
            listPreferenceLanguage.setSummary(listPreferenceLanguage.getEntry());
            listPreferenceLanguage.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    listPreferenceLanguage.setSummary(listPreferenceLanguage.getEntries()[listPreferenceLanguage.findIndexOfValue((String) newValue)]);

                    Resources resources = getActivity().getApplication().getResources();
                    Configuration config = resources.getConfiguration();
                    Locale locale = null;
                    switch ((String) newValue) {
                        case "cn":
                            locale = Locale.CHINA;
                            break;
                        case "en":
                            locale = Locale.ENGLISH;
                            break;
                    }
                    config.setLocale(locale);

                    LanguageHelper.INSTANCE.forceLocale(getActivity().getApplication(), getActivity(), locale);

                    //重启所有activity
                    ((MyApplication) getActivity().getApplication()).recreateAllActivity();
                    getActivity().recreate();
                    return true;
                }
            });

            //主题
            ListPreference listPreferenceTheme = findPreference("theme");
            listPreferenceTheme.setSummary(listPreferenceTheme.getEntry());
            listPreferenceTheme.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    listPreferenceTheme.setSummary(listPreferenceTheme.getEntries()[listPreferenceTheme.findIndexOfValue((String) newValue)]);

                    switch ((String) newValue) {
                        case "normal":
                            getActivity().setTheme(R.style.Theme_Mydemo);
                            break;
                        case "dark":
                            getActivity().setTheme(R.style.Theme_Mydemo_Dark);
                            break;
                    }
                    //重启所有activity
                    ((MyApplication) getActivity().getApplication()).recreateAllActivity();
                    getActivity().recreate();
                    return true;
                }
            });


        }


    }
}