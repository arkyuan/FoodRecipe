package app.com.ark.android.foodrecipe;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import app.com.ark.android.foodrecipe.data.MySuggestionProvider;

/**
 * Created by ark on 3/19/2016.
 */
public class ConfirmDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.dialog_clean_history)
                .setPositiveButton(R.string.clean, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Clean
                        SearchRecentSuggestions suggestions = new SearchRecentSuggestions(getActivity(),
                                MySuggestionProvider.AUTHORITY, MySuggestionProvider.MODE);
                        suggestions.clearHistory();
                        Toast.makeText(getActivity(), getString(R.string.search_his_deleted), Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
