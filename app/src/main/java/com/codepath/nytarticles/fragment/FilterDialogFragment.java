package com.codepath.nytarticles.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.codepath.nytarticles.R;
import com.codepath.nytarticles.activity.SearchActivity;
import com.codepath.nytarticles.model.Filter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.R.attr.filter;

public class FilterDialogFragment extends DialogFragment{

    @BindView(R.id.spSortOrder) Spinner spSortOrder;
    @BindView(R.id.btnDone)     Button doneButton;
    @BindView(R.id.btnCancel)   Button cancelButton;
    @BindView(R.id.etBeginDate) EditText beginDateEditText;
    @BindView(R.id.cbArts)      CheckBox artsCheckbox;
    @BindView(R.id.cbFashion_and_Stlye) CheckBox fashionAndStyleCheckbox;
    @BindView(R.id.cbSports) CheckBox sportsCheckbox;

    private String beginDate;

    // Empty Constructor
    public FilterDialogFragment() {
    }

    public static FilterDialogFragment newInstance(String title) {

        FilterDialogFragment frag = new FilterDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);

        return frag;
    }

    // Defines the listener interface
    public interface FilterEditedListener {
        void onFinishEditFilter(Filter filter);
    }


    // Call this method to send the data back to the parent fragment
    public void sendBackResult() {

        FilterEditedListener listener = (FilterEditedListener) getActivity();

        Filter curFilter = populatingFilterFileds();
        listener.onFinishEditFilter(curFilter);

        dismiss();
    }

    // insert all filter field from the Dialog View
    @NonNull
    private Filter populatingFilterFileds() {

        // Building filter to send back to Search Activity
        Filter curFilter = new Filter();

        if ( beginDate != null)
            curFilter.setBeginDate(beginDate);

        curFilter.setSortOrder(spSortOrder.getSelectedItem().toString());

        if (artsCheckbox.isChecked()) {
            curFilter.getNewsDesks().add("arts");
        }
        if (fashionAndStyleCheckbox.isChecked()) {
            curFilter.getNewsDesks().add("Fashion & Style");
        }
        if (sportsCheckbox.isChecked()) {
            curFilter.getNewsDesks().add("Sports");
        }

        return curFilter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sort_dialog, container, false);
        ButterKnife.bind(this,view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title", "Enter Name");
        getDialog().setTitle(title);

        // Show soft keyboard automatically and request focus to field
        beginDateEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);


        /* onClick handlers*/

        doneButton.setOnClickListener(v -> sendBackResult());

        beginDateEditText.setOnClickListener(v -> {
            DialogFragment newFragment = new DatePickerFragment();
            newFragment.setTargetFragment(FilterDialogFragment.this, 300);
            newFragment.show(getFragmentManager(), "datePicker");
        });

        cancelButton.setOnClickListener(v -> dismiss());
    }

    public void setBeginDate(String beginDate) {

        String date = beginDate.substring(4,6) + "/" + beginDate.substring(6,8) + "/" + beginDate.substring(0,4);
        beginDateEditText.setText(date);

        this.beginDate = beginDate;
    }
}
