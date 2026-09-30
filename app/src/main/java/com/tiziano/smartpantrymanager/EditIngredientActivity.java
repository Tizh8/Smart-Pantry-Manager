package com.tiziano.smartpantrymanager;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditIngredientActivity extends AppCompatActivity {

    private EditText editName;
    private EditText editQuantity;
    private EditText editUnit;
    private EditText editExpiry;

    private DatabaseHelper databaseHelper;
    private int ingredientId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_ingredient);

        editName = findViewById(R.id.editName);
        editQuantity = findViewById(R.id.editQuantity);
        editUnit = findViewById(R.id.editUnit);
        editExpiry = findViewById(R.id.editExpiry);

        Button btnUpdateIngredient = findViewById(R.id.btnUpdateIngredient);
        Button btnDeleteIngredient = findViewById(R.id.btnDeleteIngredient);

        databaseHelper = new DatabaseHelper(this);

        // Get the ingredient information sent from the pantry list
        ingredientId = getIntent().getIntExtra("ingredientId", -1);

        String name = getIntent().getStringExtra("ingredientName");
        double quantity = getIntent().getDoubleExtra("ingredientQuantity", 0);
        String unit = getIntent().getStringExtra("ingredientUnit");
        String expiry = getIntent().getStringExtra("ingredientExpiry");

        editName.setText(name);
        editQuantity.setText(String.valueOf(quantity));
        editUnit.setText(unit);
        editExpiry.setText(expiry);

        btnUpdateIngredient.setOnClickListener(v -> updateIngredient());

        btnDeleteIngredient.setOnClickListener(v -> deleteIngredient());
    }

    private void updateIngredient() {

        String name = editName.getText().toString().trim();
        String quantityText = editQuantity.getText().toString().trim();
        String unit = editUnit.getText().toString().trim();
        String expiry = editExpiry.getText().toString().trim();

        if (name.isEmpty() || quantityText.isEmpty() || unit.isEmpty()) {
            Toast.makeText(this,
                    "Please fill in the required fields",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        double quantity;

        try {
            quantity = Double.parseDouble(quantityText);
        } catch (NumberFormatException e) {
            Toast.makeText(this,
                    "Please enter a valid quantity",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        PantryItem item = new PantryItem(
                ingredientId,
                name,
                quantity,
                unit,
                expiry
        );

        boolean updated = databaseHelper.updatePantryItem(item);

        if (updated) {
            Toast.makeText(this,
                    "Ingredient updated successfully",
                    Toast.LENGTH_SHORT).show();

            finish();
        } else {
            Toast.makeText(this,
                    "Could not update ingredient",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteIngredient() {

        if (ingredientId == -1) {
            Toast.makeText(this,
                    "Ingredient could not be found",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        boolean deleted = databaseHelper.deletePantryItem(ingredientId);

        if (deleted) {
            Toast.makeText(this,
                    "Ingredient deleted",
                    Toast.LENGTH_SHORT).show();

            finish();
        } else {
            Toast.makeText(this,
                    "Could not delete ingredient",
                    Toast.LENGTH_SHORT).show();
        }
    }
}