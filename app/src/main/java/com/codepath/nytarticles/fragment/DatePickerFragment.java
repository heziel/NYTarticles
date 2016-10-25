package com.codepath.nytarticles.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.DatePicker;

import com.codepath.nytarticles.fragment.FilterDialogFragment;

import java.util.Calendar;

/**
 * Created by @author Shrikant Pandhare on 2/11/16.
 */
public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();

        int year  = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day   = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        FilterDialogFragment filterDialogFragment = (FilterDialogFragment) getTargetFragment();

        String beginDate = returnBeginDateFormat(year, month, day);

        filterDialogFragment.setBeginDate(beginDate);
    }

    private String returnBeginDateFormat(int year, int month, int day) {

        month++;
        String sMonth = String.valueOf(month);
        String sDay = String.valueOf(day);

        if ( month < 9 )
            sMonth= "0" + sMonth;

        if ( day < 9 )
            sDay = "0" + sDay;

        String date = String.valueOf(year) + sMonth + sDay;
        return date;
    }
}
