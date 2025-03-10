package com.example.unit_converter_app;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.text.TextWatcher;
import android.text.Editable;

import java.util.Locale;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.util.Log;

public class MainActivity extends AppCompatActivity {
    public enum UnitType {
        TEMPERATURE, WEIGHT, LENGTH
    }

    private UnitType selectedUnitType = UnitType.TEMPERATURE;
    private String selectedSourceUnit = "";
    private String selectedDestinationUnit = "";
    private Double value = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        handleUnitTypeChange();
        updateSpinners();
        handleValueOnChange();
        handleOnClick();
    }

    private void handleUnitTypeChange() {
        RadioGroup unitRadioGroup = findViewById(R.id.unitTypeRadioGroup);
        RadioButton radioTemperature = findViewById(R.id.radioTemperature);
        radioTemperature.setChecked(true);
        unitRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioTemperature) {
                selectedUnitType = UnitType.TEMPERATURE;
            } else if (checkedId == R.id.radioWeight) {
                selectedUnitType = UnitType.WEIGHT;
            } else if (checkedId == R.id.radioLength) {
                selectedUnitType = UnitType.LENGTH;
            }
            updateSpinners();
            Log.d("SelectedUnitType", "Selected unit Type: " + selectedUnitType);
        });
    }

    private void setupSourceUnitSpinner(final int componentId) {
        Spinner unitSpinner = findViewById(componentId);

        String[] lengthUnits = {"inch", "foot", "yard", "mile", "cm", "km"};
        String[] tempUnits = {"F", "C", "K"};
        String[] weightUnits = {"pound", "ounce", "ton", "g", "kg"};
        String[] units = {};

        switch (selectedUnitType) {
            case TEMPERATURE:
                units = tempUnits;
                break;
            case WEIGHT:
                units = weightUnits;
                break;
            case LENGTH:
                units = lengthUnits;
                break;
            default:
                break;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, units);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitSpinner.setAdapter(adapter);
        if (componentId == R.id.sourceUnitSpinner) {
            selectedSourceUnit = units[0];
            unitSpinner.setSelection(0, false);
        } else if (componentId == R.id.destinationUnitSpinner) {
            selectedDestinationUnit = units[1];
            unitSpinner.setSelection(1, false);
        }

        unitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                if (componentId == R.id.sourceUnitSpinner) {
                    selectedSourceUnit = selectedItem;
                } else if (componentId == R.id.destinationUnitSpinner) {
                    selectedDestinationUnit = selectedItem;
                }
                Log.d("selectedSourceUnit", "selectedSourceUnit: " + selectedSourceUnit);
                Log.d("selectDestinationUnit", "selectDestinationUnit: " + selectedDestinationUnit);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    private void updateSpinners() {
        setupSourceUnitSpinner(R.id.sourceUnitSpinner);
        setupSourceUnitSpinner(R.id.destinationUnitSpinner);
    }

    private void handleValueOnChange() {
        EditText valueTextField = findViewById(R.id.editTextNumberDecimal);
        String valueInString = String.format(value.toString());
        valueTextField.setText(valueInString);

        valueTextField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String valueString = editable.toString().trim();

                if (!valueString.isEmpty() && !valueString.equals("-")) {
                    try {
                        value = Double.parseDouble(valueString);
                    } catch (NumberFormatException e) {
                        Log.e("Conversion", "Invalid input: " + valueString);
                        value = 0.0;
                    }
                } else {
                    value = 0.0;
                }
            }
        });
    }

    private void handleOnClick() {
        Button convertUnitButton = findViewById(R.id.convertUnitButton);
        TextView resultView = findViewById(R.id.resultView);

        convertUnitButton.setOnClickListener(v -> {
            Log.d("Value", "Entered value: " + value);
            Log.d("selectedSourceUnit", "Entered selectedSourceUnit: " + selectedSourceUnit);


            if (!Double.isNaN(value)) {
                // For weight and length, check if value is non-negative
                if ((selectedUnitType == UnitType.WEIGHT || selectedUnitType == UnitType.LENGTH) && value < 0) {
                    resultView.setText("Please enter a value ≥ 0 for weight or length units.");
                    return;
                }else if(selectedSourceUnit.equals(selectedDestinationUnit)){
                    resultView.setText("Please choose a different source and destination unit.");
                    return;
                }

                double result = 0.0;

                switch (selectedSourceUnit) {
                    // Temperature units (negative values allowed)
                    case "F":
                        result = convertFahrenheit(selectedDestinationUnit);
                        break;
                    case "C":
                        result = convertCelsius(selectedDestinationUnit);
                        break;
                    case "K":
                        result = convertKelvin(selectedDestinationUnit);
                        break;

                    // Weight units
                    case "pound":
                        result = convertPound(selectedDestinationUnit);
                        break;
                    case "ounce":
                        result = convertOunce(selectedDestinationUnit);
                        break;
                    case "ton":
                        result = convertTon(selectedDestinationUnit);
                        break;
                    case "g":
                        result = convertGram(selectedDestinationUnit);
                        break;
                    case "kg":
                        result = convertKiloGram(selectedDestinationUnit);
                        break;

                    // Length units
                    case "inch":
                        result = convertInch(selectedDestinationUnit);
                        break;
                    case "foot":
                        result = convertFoot(selectedDestinationUnit);
                        break;
                    case "yard":
                        result = convertYard(selectedDestinationUnit);
                        break;
                    case "mile":
                        result = convertMile(selectedDestinationUnit);
                        break;
                    case "cm":
                        result = convertCm(selectedDestinationUnit);
                        break;
                    case "km":
                        result = convertKm(selectedDestinationUnit);
                        break;

                    default:
                        Log.d("Conversion", "Invalid source unit");
                        resultView.setText("Invalid source unit");
                        return;
                }

                resultView.setText(String.format(Locale.getDefault(), "%.2f %s equals %.2f %s", value, selectedSourceUnit, result, selectedDestinationUnit));

            } else {
                Log.d("Value", "Entered value: " + "Null input");
                resultView.setText("Invalid input");
            }
        });
    }



    private double convertFahrenheit(String destinationUnit) {
        switch (destinationUnit) {
            case "C":
                return (value - 32) * 1.8 / 3.0;
            case "K":
                return (value - 32) * 1.8 / 3.0 + 273.15;
            default:
                Log.d("Conversion", "Invalid destination unit");
                return Double.NaN;
        }
    }


    private double convertCelsius(String destinationUnit) {
        switch (destinationUnit) {
            case "F":
                return (value * 1.8) + 32;
            case "K":
                return value + 273.15;
            default:
                Log.d("Conversion", "Invalid destination unit");
                return Double.NaN;
        }
    }


    private double convertKelvin(String destinationUnit) {
        switch (destinationUnit) {
            case "F":
                return (value - 273.15) * 1.8 + 32;
            case "C":
                return value - 273.15;
            default:
                Log.d("Conversion", "Invalid destination unit");
                return Double.NaN;
        }
    }

    private double convertPound(String destinationUnit) {
        switch (destinationUnit) {
            case "kg":
                return value * 0.453592;
            case "g":
                return value * 453.592;
            case "ounce":
                return value * 16;
            case "ton":
                return value * 0.000453592;
            default:
                return Double.NaN;
        }
    }

    private double convertOunce(String destinationUnit) {
        switch (destinationUnit) {
            case "kg":
                return value * 0.0283495;
            case "g":
                return value * 28.3495;
            case "pound":
                return value * 0.0625;
            case "ton":
                return value * 0.00003125;
            default:
                return Double.NaN;
        }
    }

    private double convertTon(String destinationUnit) {
        switch (destinationUnit) {
            case "kg":
                return value * 907.185;
            case "g":
                return value * 907185;
            case "pound":
                return value * 2000;
            case "ounce":
                return value * 32000;
            default:
                return Double.NaN;
        }
    }

    private double convertGram(String destinationUnit) {
        switch (destinationUnit) {
            case "kg":
                return value * 0.001;
            case "pound":
                return value * 0.00220462;
            case "ounce":
                return value * 0.035274;
            case "ton":
                return value * 1.1023e-6;
            default:
                return Double.NaN;
        }
    }

    private double convertKiloGram(String destinationUnit) {
        switch (destinationUnit) {
            case "g":
                return value * 1000;
            case "pound":
                return value * 2.20462;
            case "ounce":
                return value * 35.274;
            case "ton":
                return value * 0.00110231;
            default:
                return Double.NaN;
        }
    }

    private double convertInch(String destinationUnit) {
        switch (destinationUnit) {
            case "cm":
                return value * 2.54;
            case "foot":
                return value / 12;
            case "yard":
                return value / 36;
            case "mile":
                return value / 63360;
            case "km":
                return value * 0.0000254;
            default:
                return Double.NaN;
        }
    }

    private double convertFoot(String destinationUnit) {
        switch (destinationUnit) {
            case "cm":
                return value * 30.48;
            case "inch":
                return value * 12;
            case "yard":
                return value / 3;
            case "mile":
                return value / 5280;
            case "km":
                return value * 0.0003048;
            default:
                return Double.NaN;
        }
    }

    private double convertYard(String destinationUnit) {
        switch (destinationUnit) {
            case "cm":
                return value * 91.44;
            case "inch":
                return value * 36;
            case "foot":
                return value * 3;
            case "mile":
                return value / 1760;
            case "km":
                return value * 0.0009144;
            default:
                return Double.NaN;
        }
    }

    private double convertMile(String destinationUnit) {
        switch (destinationUnit) {
            case "km":
                return value * 1.60934;
            case "cm":
                return value * 160934;
            case "inch":
                return value * 63360;
            case "foot":
                return value * 5280;
            case "yard":
                return value * 1760;
            default:
                return Double.NaN;
        }
    }

    private double convertCm(String destinationUnit) {
        switch (destinationUnit) {
            case "inch":
                return value / 2.54;
            case "foot":
                return value / 30.48;
            case "yard":
                return value / 91.44;
            case "mile":
                return value / 160934;
            case "km":
                return value * 0.00001;
            default:
                return Double.NaN;
        }
    }

    private double convertKm(String destinationUnit) {
        switch (destinationUnit) {
            case "mile":
                return value / 1.60934;
            case "cm":
                return value * 100000;
            case "inch":
                return value * 39370.1;
            case "foot":
                return value * 3280.84;
            case "yard":
                return value * 1093.61;
            default:
                return Double.NaN;
        }
    }
}