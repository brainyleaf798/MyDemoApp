package com.ranjeet.mydemoapp;

import androidx.appcompat.app.AppCompatActivity;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TimePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.Calendar;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Access JSON data from the raw resource
        String jsonData = readJsonFile(R.raw.sample);

        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            LinearLayout linearLayout = findViewById(R.id.linearLayout); // Reference your LinearLayout

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);

                String widgetType = item.getString("type");
                String text = item.optString("text", "");
                String hint = item.optString("hint", "");

                // Based on widgetType, create and configure widgets
                if ("EditText".equals(widgetType)) {
                    EditText editText = new EditText(new ContextThemeWrapper(this, R.style.EditTextStyle));
                    editText.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    ));
                    editText.setHint(hint);

                    // Add padding to the EditText
                    editText.setPadding(16, 0, 16, 16);

                    // Add margin to the EditText
                    ((LinearLayout.LayoutParams) editText.getLayoutParams()).setMargins(20, 20, 20, 16);

                    linearLayout.addView(editText);
                } else if ("Spinner".equals(widgetType)) {
                    Spinner spinner = new Spinner(new ContextThemeWrapper(this, R.style.SpinnerStyle));
                    spinner.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    ));

                    // Create an ArrayAdapter for the Spinner
                    ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                            this, android.R.layout.simple_spinner_item);
                    spinnerAdapter.setDropDownViewResource(
                            android.R.layout.simple_spinner_dropdown_item);

                    // Add items to the Spinner adapter
                    JSONArray spinnerItems = item.getJSONArray("items");
                    for (int j = 0; j < spinnerItems.length(); j++) {
                        spinnerAdapter.add(spinnerItems.getString(j));
                    }

                    spinner.setAdapter(spinnerAdapter);

                    // Add margin to the Spinner
                    ((LinearLayout.LayoutParams) spinner.getLayoutParams()).setMargins(20, 20, 20, 16);

                    linearLayout.addView(spinner);
                } else if ("Button".equals(widgetType)) {
                    Button button = new Button(new ContextThemeWrapper(this, R.style.ButtonStyle));
                    button.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    ));
                    button.setText(text);

                    // Add padding to the Button
                    button.setPadding(16, 16, 16, 16);

                    // Add margin to the Button
                    ((LinearLayout.LayoutParams) button.getLayoutParams()).setMargins(20, 0, 20, 16);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Validate EditText widgets
                            validateEditTexts(linearLayout);
                        }
                    });
                    linearLayout.addView(button);
                }

                else if ("DatePicker".equals(widgetType)) {
                    final EditText dateEditText = new EditText(this);
                    dateEditText.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    ));
                    dateEditText.setHint(hint);
                    dateEditText.setFocusable(false);
                    ((LinearLayout.LayoutParams) dateEditText.getLayoutParams()).setMargins(20, 0, 20, 16);
                    dateEditText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showDatePicker(dateEditText);
                        }
                    });
                    linearLayout.addView(dateEditText);
                } else if ("TimePicker".equals(widgetType)) {
                    final EditText timeEditText = new EditText(this);
                    timeEditText.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    ));
                    timeEditText.setHint(hint);
                    timeEditText.setFocusable(false);
                    ((LinearLayout.LayoutParams) timeEditText.getLayoutParams()).setMargins(20, 0, 20, 16);
                    timeEditText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showTimePicker(timeEditText);
                        }
                    });
                    linearLayout.addView(timeEditText);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String readJsonFile(int resourceId) {
        Resources resources = getResources();
        InputStream inputStream = resources.openRawResource(resourceId);
        Scanner scanner = new Scanner(inputStream);
        StringBuilder builder = new StringBuilder();

        while (scanner.hasNextLine()) {
            builder.append(scanner.nextLine());
        }

        return builder.toString();
    }


    private void validateEditTexts(LinearLayout linearLayout) {
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            View child = linearLayout.getChildAt(i);
            if (child instanceof EditText) {
                EditText editText = (EditText) child;
                String input = editText.getText().toString().trim();
                if (input.isEmpty()) {
                    editText.setError("This field is required.");
                    // You can add more complex validation logic here
                }
                // Additional validation checks can be added as needed
            }
        }
    }
    private void showDatePicker(final EditText dateEditText) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String date = (month + 1) + "/" + dayOfMonth + "/" + year;
                        dateEditText.setText(date);
                    }
                }, year, month, day);

        datePickerDialog.show();
    }

    private void showTimePicker(final EditText timeEditText) {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String time = hourOfDay + ":" + minute;
                        timeEditText.setText(time);
                    }
                }, hour, minute, false);

        timePickerDialog.show();
    }
}