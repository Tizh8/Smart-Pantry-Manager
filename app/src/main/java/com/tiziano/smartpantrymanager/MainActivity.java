package com.tiziano.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ListView pantryList;
    private DatabaseHelper databaseHelper;
    private ArrayList<PantryItem> pantryItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pantryList = findViewById(R.id.pantryList);
        Button btnAddIngredient = findViewById(R.id.btnAddIngredient);

        databaseHelper = new DatabaseHelper(this);

        btnAddIngredient.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddIngredientActivity.class);
            startActivity(intent);
        });

        pantryList.setOnItemClickListener((parent, view, position, id) -> {

            PantryItem selectedItem = pantryItems.get(position);

            Intent editIntent = new Intent(
                    MainActivity.this,
                    EditIngredientActivity.class
            );

            editIntent.putExtra("ingredientId", selectedItem.getId());
            editIntent.putExtra("ingredientName", selectedItem.getName());
            editIntent.putExtra("ingredientQuantity", selectedItem.getQuantity());
            editIntent.putExtra("ingredientUnit", selectedItem.getUnit());
            editIntent.putExtra("ingredientExpiry", selectedItem.getExpiryDate());

            startActivity(editIntent);

        });
        loadPantryItems();

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (databaseHelper != null) {
            loadPantryItems();
        }
    }

    // This allows for it to load the saved ingredients when screen opens
    private void loadPantryItems() {
        pantryItems = databaseHelper.getAllPantryItems();
        ArrayList<String> itemNames = new ArrayList<>();

        for (PantryItem item : pantryItems) {
            String displayText = item.getName() + " - "
                    + item.getQuantity() + " "
                    + item.getUnit();
            itemNames.add(displayText);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                itemNames

        );

        pantryList.setAdapter(adapter);
    }
}