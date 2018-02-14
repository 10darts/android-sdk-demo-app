package com.auroralabs.tendarts.app.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.auroralabs.tendarts.R;

/**
 * Created by luisma on 13/2/18.
 */

public class SetUserDialogFragment extends DialogFragment {

    private static final String LOG_TAG = SetUserDialogFragment.class.getSimpleName();

    public static final String USER_EXTRA = "user_extra";

    public interface SetUserDialogListener {
        void onUserNameSaved(String userName);
    }

    private EditText userNameEditText;

    private String userString;
    private SetUserDialogListener listener;

    public static SetUserDialogFragment newInstance(String userName) {

        Bundle args = new Bundle();
        args.putString(USER_EXTRA, userName);

        SetUserDialogFragment fragment = new SetUserDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_dialog_set_user, null);

        userString = getArguments().getString(USER_EXTRA, "");

        userNameEditText = (EditText) view.findViewById(R.id.user_name_edit_text);

        userNameEditText.setText(userString);

        userNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                userString = userNameEditText.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        userNameEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    hideSoftKeyBoard(userNameEditText);

                    userString = userNameEditText.getText().toString();

                    if (listener != null) {
                        listener.onUserNameSaved(userString);
                    }
                    dismiss();

                    return true;
                }
                return false;
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (listener != null) {
                            listener.onUserNameSaved(userString);
                        }
                        dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismiss();
                    }
                });
        return builder.create();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    private void hideSoftKeyBoard(View view) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm.isAcceptingText() && view != null) {// verify if the soft keyboard is open
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void setListener(SetUserDialogListener listener) {
        this.listener = listener;
    }

}
