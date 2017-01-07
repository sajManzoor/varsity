package my.firstApp.sajid.versity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

/**
 * Created by Sajid on 4/29/2016.
 */
public class UsContactDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        builder.setTitle("Contact");
        builder.setMessage(UsDetailedData.contact);


        // accepting the terms and conditions will to the app
        builder.setPositiveButton("OK",null);
        Dialog dialog=builder.create();

        return dialog;
    }
}
