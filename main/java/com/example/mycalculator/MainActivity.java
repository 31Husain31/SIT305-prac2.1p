package com.example.mycalculator;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // UI elements for user input and interaction
    private Spinner categoryMenu;
    private Spinner sourceMenu;
    private Spinner destinationMenu;
    private EditText inputBox;
    private TextView outputBox;
    private Button convertButton;

    // Unit type categories
    private final String[] categoryOptions = {"Length", "Weight", "Temperature"};

    // Units for each type
    private final String[] lengthUnits = {"Inch", "Foot", "Yard", "Mile", "Centimeter", "Kilometer"};
    private final String[] weightUnits = {"Pound", "Ounce", "Ton", "Gram", "Kilogram"};
    private final String[] tempUnits = {"Celsius", "Fahrenheit", "Kelvin"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Load layout file

        // Link XML views to Java variables
        inputBox = findViewById(R.id.TextToConvert);
        outputBox = findViewById(R.id.ResultTextView);
        convertButton = findViewById(R.id.ConvertButton);
        categoryMenu = findViewById(R.id.UnitCategorySpinner);
        sourceMenu = findViewById(R.id.SourceUnitSpinner);
        destinationMenu = findViewById(R.id.DestinationUnitSpinner);

        // Populate category spinner with unit types
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categoryOptions);
        categoryMenu.setAdapter(categoryAdapter);

        // Whenever the user selects a different unit category
        categoryMenu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String selectedCategory = categoryOptions[pos];
                updateUnitMenus(selectedCategory); // Change source/destination options
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No action needed
            }
        });

        // When the user presses the Convert button
        convertButton.setOnClickListener(view -> {
            String inputText = inputBox.getText().toString().trim();

            if (inputText.isEmpty()) {
                outputBox.setText("Please enter a value first.");
                return;
            }

            try {
                double inputValue = Double.parseDouble(inputText);
                String selectedType = categoryMenu.getSelectedItem().toString();
                String source = sourceMenu.getSelectedItem().toString();
                String target = destinationMenu.getSelectedItem().toString();

                // Run the conversion logic
                double finalResult = convertValue(selectedType, source, target, inputValue);
                outputBox.setText(String.format("%.4f", finalResult));

            } catch (NumberFormatException e) {
                outputBox.setText("Input is not a valid number.");
            } catch (Exception e) {
                outputBox.setText("Something went wrong during conversion.");
            }
        });
    }

    // Updates the unit spinners depending on which type (length, weight, temperature) is selected
    private void updateUnitMenus(String category) {
        String[] options;

        switch (category) {
            case "Length":
                options = lengthUnits;
                break;
            case "Weight":
                options = weightUnits;
                break;
            case "Temperature":
                options = tempUnits;
                break;
            default:
                options = new String[]{};
        }

        ArrayAdapter<String> unitAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, options);
        sourceMenu.setAdapter(unitAdapter);
        destinationMenu.setAdapter(unitAdapter);
    }

    // Core method that performs the actual conversion based on type and selected units
    private double convertValue(String type, String from, String to, double input) {
        double result = 0.0;

        if (type.equals("Length")) {
            double cmValue = 0.0;

            // Convert all input to centimeters first
            switch (from) {
                case "Inch": cmValue = input * 2.54; break;
                case "Foot": cmValue = input * 30.48; break;
                case "Yard": cmValue = input * 91.44; break;
                case "Mile": cmValue = input * 160934; break;
                case "Centimeter": cmValue = input; break;
                case "Kilometer": cmValue = input * 100000; break;
            }

            // Then convert from centimeters to target unit
            switch (to) {
                case "Inch": result = cmValue / 2.54; break;
                case "Foot": result = cmValue / 30.48; break;
                case "Yard": result = cmValue / 91.44; break;
                case "Mile": result = cmValue / 160934; break;
                case "Centimeter": result = cmValue; break;
                case "Kilometer": result = cmValue / 100000; break;
            }

        } else if (type.equals("Weight")) {
            double gramValue = 0.0;

            // Convert to grams
            switch (from) {
                case "Pound": gramValue = input * 453.592; break;
                case "Ounce": gramValue = input * 28.3495; break;
                case "Ton": gramValue = input * 907185; break;
                case "Gram": gramValue = input; break;
                case "Kilogram": gramValue = input * 1000; break;
            }

            // Convert grams to target
            switch (to) {
                case "Pound": result = gramValue / 453.592; break;
                case "Ounce": result = gramValue / 28.3495; break;
                case "Ton": result = gramValue / 907185; break;
                case "Gram": result = gramValue; break;
                case "Kilogram": result = gramValue / 1000; break;
            }

        } else if (type.equals("Temperature")) {
            double celsius = 0.0;

            // First convert to Celsius
            switch (from) {
                case "Celsius": celsius = input; break;
                case "Fahrenheit": celsius = (input - 32) / 1.8; break;
                case "Kelvin": celsius = input - 273.15; break;
            }

            // Then convert Celsius to desired unit
            switch (to) {
                case "Celsius": result = celsius; break;
                case "Fahrenheit": result = (celsius * 1.8) + 32; break;
                case "Kelvin": result = celsius + 273.15; break;
            }
        }

        return result;
    }
}
