package com.example.jason.taskit;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.DateFormat;
import java.util.Calendar;


public class TaskActivity extends ActionBarActivity {
    public static final String EXTRA = "TaskExtra";
    public static final String TAG = "TaskActivity";
    private Calendar mCal;
    private Task mTask;
    private Button mDateButton;
    private EditText mTaskNameInput;
    private CheckBox mDoneBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        mTask = (Task) getIntent().getSerializableExtra(EXTRA);
        if (mTask == null) {
            mTask = new Task();
        }
        mCal = Calendar.getInstance();
        mCal.setTime(mTask.getDueDate());

        Log.d(TAG, "Task Activity");

        mTaskNameInput = (EditText) findViewById(R.id.task_name);
        Button saveButton = (Button) findViewById(R.id.save_button);
        mDoneBox = (CheckBox) findViewById(R.id.task_done);
        mDateButton = (Button) findViewById(R.id.task_date);
        mTaskNameInput.setText(mTask.getName());
        mDoneBox.setChecked(mTask.isDone());

        if (mTask.getDueDate() == null) {
            mDateButton.setText(getResources().getString(R.string.no_date));
        } else {
            updateButton();
        }


        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpd = new DatePickerDialog(TaskActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Log.d(TAG, "Year is " + year);
                        Log.d(TAG, "Month is " + monthOfYear);//note zero index (0-11)
                        Log.d(TAG, "Day is " + dayOfMonth);
                        mCal.set(Calendar.YEAR, year);
                        mCal.set(Calendar.MONTH, monthOfYear);
                        mCal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateButton();
                    }
                }, mCal.get(Calendar.YEAR), mCal.get(Calendar.MONTH), mCal.get(Calendar.DAY_OF_MONTH));

                dpd.show();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTask.setName(mTaskNameInput.getText().toString());
                mTask.setDone(mDoneBox.isChecked());
                mTask.setDueDate(mCal.getTime());

                Intent i = new Intent();
                i.putExtra(EXTRA, mTask);
                setResult(RESULT_OK, i); // basically returns info the the page that called this one
                // we are done with this activity. go back
                finish();
            }
        });

    }

    private void updateButton() {
        DateFormat df = DateFormat.getDateInstance();
        if (mCal.getTime() != null) {
            mDateButton.setText(df.format(mCal.getTime()));
        } else {
            mDateButton.setText(getResources().getString(R.string.no_date));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_task, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
