package app.com.ark.android.foodrecipe;

import android.annotation.TargetApi;
import android.app.DialogFragment;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import com.google.android.gms.appinvite.AppInviteInvitation;

/**
 * Created by ark on 3/18/2016.
 */
public class SettingsActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener {
    private static final int REQUEST_INVITE = 0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add 'general' preferences, defined in the XML file
        // TODO: Add preferences from XML
        addPreferencesFromResource(R.xml.pref_general);
        // For all preferences, attach an OnPreferenceChangeListener so the UI summary can be
        // updated when the preference changes.
        // TODO: Add preferences
        //bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_sort_key)));
        Preference button = (Preference)findPreference(getString(R.string.clean_search_history));
        button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                //code for what you want it to do
                DialogFragment newFragment = new ConfirmDialogFragment();
                newFragment.show(getFragmentManager(), "Clean History");
                return true;
            }
        });

        Preference app_invite = (Preference)findPreference(getString(R.string.app_invite));
        app_invite.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                try {
                    Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invitation_message))
                            .setMessage(getString(R.string.invitation_message))
                            .setCallToActionText(getString(R.string.invitation_cta))
                            .build();
                    startActivityForResult(intent, REQUEST_INVITE);
                } catch (ActivityNotFoundException ac) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.setType("text/plain");
                    startActivity(sendIntent);
                }

                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_INVITE) {
            if (resultCode == RESULT_OK) {
                String[] ids = AppInviteInvitation.getInvitationIds(resultCode, data);
                //Log.d(TAG, getString(R.string.sent_invitations_fmt, ids.length));
            } else {

               // Log.d(TAG, "invite send failed or cancelled:" + requestCode + ",resultCode:" + resultCode );
            }
        }
    }

    /**
     * Attaches a listener so the summary is always updated with the preference value.
     * Also fires the listener once, to initialize the summary (so it shows up before the value
     * is changed.)
     */
    private void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(this);

        // Trigger the listener immediately with the preference's
        // current value.
        onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {
        String stringValue = value.toString();

        if (preference instanceof ListPreference) {
            // For list preferences, look up the correct display value in
            // the preference's 'entries' list (since they have separate labels/values).
            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(stringValue);
            if (prefIndex >= 0) {
                preference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        } else {
            // For other preferences, set the summary to the value's simple string representation.
            preference.setSummary(stringValue);
        }
        return true;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public Intent getParentActivityIntent() {
        return super.getParentActivityIntent().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    }
}
