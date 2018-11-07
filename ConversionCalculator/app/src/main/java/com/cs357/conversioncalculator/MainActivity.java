package com.cs357.conversioncalculator;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
{
    public static final int SETTINGS_SELECTION = 1;
    public int UNIT_SELECTION = 1; // 1 for length, 2 for volume
    public TextView toUnit;
    public TextView fromUnit;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Conversion Calculator"); // for set actionbar title

        final TextView title = (TextView) findViewById(R.id.title);

        toUnit = (TextView) findViewById(R.id.toUnit);
        fromUnit = (TextView) findViewById(R.id.fromUnit);
        final EditText toValue = (EditText) findViewById(R.id.toValue);
        final EditText fromValue = (EditText) findViewById(R.id.fromValue);

        final Button calculate = findViewById(R.id.calculate);
        calculate.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                if (fromValue.getText().length() > 0)
                {
                    if (title.getText().equals("Length Converter"))
                    {
                        toValue.setText(String.valueOf(UnitsConverter.convert(
                                Double.parseDouble(fromValue.getText().toString()),
                                UnitsConverter.LengthUnits.valueOf(fromUnit.getText().toString()),
                                UnitsConverter.LengthUnits.valueOf(toUnit.getText().toString()))));
                    }
                    else if (title.getText().equals("Volume Converter"))
                    {
                        toValue.setText(String.valueOf(UnitsConverter.convert(
                                Double.parseDouble(fromValue.getText().toString()),
                                UnitsConverter.VolumeUnits.valueOf(fromUnit.getText().toString()),
                                UnitsConverter.VolumeUnits.valueOf(toUnit.getText().toString()))));
                    }
                }
                hideSoftKeyboard(v);
            }
        });

        final Button clear = findViewById(R.id.clear);
        clear.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                toValue.setText("");
                fromValue.setText("");


                hideSoftKeyboard(v);
            }
        });

        final Button mode = findViewById(R.id.mode);
        mode.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                if (title.getText().equals("Length Converter"))
                {
                    title.setText(getString(R.string.volume_converter));

                    UNIT_SELECTION = 2;
                    toUnit.setText(getString(R.string.gallons));
                    fromUnit.setText(getString(R.string.liters));
                }
                else if (title.getText().equals("Volume Converter"))
                {
                    title.setText(getString(R.string.length_converter));

                    UNIT_SELECTION = 1;

                    toUnit.setText(getString(R.string.meters));
                    fromUnit.setText(getString(R.string.yards));
                }

                hideSoftKeyboard(v);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Intent intentSettings = new Intent(this, SettingsActivity.class);
                Bundle extras = new Bundle();
                extras.putInt("mode", UNIT_SELECTION);
                extras.putString("curFromUnit", fromUnit.getText().toString());
                extras.putString("curToUnit", toUnit.getText().toString());

                intentSettings.putExtras(extras);
                startActivityForResult(intentSettings, SETTINGS_SELECTION);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SETTINGS_SELECTION)
        {
            if (resultCode == RESULT_OK) {
                Bundle b = data.getExtras();
                toUnit.setText(b.getString("toSelection"));
                fromUnit.setText(b.getString("fromSelection"));
            }
        }


    }

    public void hideSoftKeyboard(View view) {
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManager.isAcceptingText()) {
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }

    }


}

