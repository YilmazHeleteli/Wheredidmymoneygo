package com.test.minicostcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class NewItem extends AppCompatActivity {

    EditText itemName;
    EditText itemPrice;
    Button btnAdd;
    EditText itemFreq;

    double price;
    String category;
    String necessary;
    double perWeek;
    double costPerDay, costPerWeek, costPerMonth, costPerYear;


    RadioGroup radioGroupFreq;
    RadioButton radioButtonFreq;
    RadioGroup radioGroupCat;
    RadioButton radioButtonCat;
    RadioGroup radioGroupNecessary;
    RadioButton radioButtonNecessary;

    //objects in the View Item layout

    ImageView itemImage;
    TextView itemViewName;
    TextView itemViewFreq;
    TextView itemViewDay;
    TextView itemViewWeek;
    TextView itemViewMonth;
    TextView itemViewYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);

        itemName = findViewById(R.id.NewItemName);
        btnAdd = findViewById(R.id.AddItem);
        itemPrice   = findViewById(R.id.Price);

        //initialise radio group button values
        itemFreq = findViewById(R.id.itemFreq);
        radioGroupFreq = findViewById(R.id.radioFrequency);
        radioGroupCat = findViewById(R.id.radioCat);
        radioGroupNecessary = findViewById(R.id.radioNec);

    }

//capture data from fiels when Add Item is tapped

public void btnAdd(View v) {

        //first checks a name has been entered for the item
if(itemName.getText().toString().isEmpty())
{
    Toast.makeText(NewItem.this, "Item name is empty", Toast.LENGTH_SHORT).show();
}
else{

        try {

            //calculate the frequency of purchases
        float getFreq;

        int selectedFreq = radioGroupFreq.getCheckedRadioButtonId();
        radioButtonFreq = findViewById(selectedFreq);

        int selectedCat = radioGroupCat.getCheckedRadioButtonId();
        radioButtonCat = findViewById(selectedCat);


        int selectedNec = radioGroupNecessary.getCheckedRadioButtonId();
        radioButtonNecessary = findViewById(selectedNec);

        //removes any white space from fields the user has entered values into
        itemPrice.getText().toString().trim();
        itemName.getText().toString().trim();
        itemFreq.getText().toString().trim();

        //retrieves the numerical value of the below strings
        price = Double.parseDouble(itemPrice.getText().toString());
        getFreq = Float.parseFloat(itemFreq.getText().toString());

        //checks to see if the user has selected whether the item is necessary

        switch (radioButtonNecessary.getText().toString())
        {
            case "Yes":
                necessary = "Necessary";
                break;
            case "Not Really...":
                necessary = "Unnecessary";
                break;

        }

        //calculates the weekly frequency of purchases based on whether the purchase is daily, monthly, weekly or annually

            double weekYear = 52.1429;
            switch(radioButtonFreq.getText().toString())
        {
            case "Day":
                perWeek = getFreq * 7;
                break;
            case "Week":
                perWeek = getFreq;
                break;
            case "Month":
                perWeek = (getFreq * 12) / weekYear;
                break;
            case "Year":
                perWeek = getFreq/ weekYear;
                break;
        }

        //assigns a category to the item based on what the user has selected

        switch(radioButtonCat.getText().toString())
        {
            case "Food/Drink":
                category = "Food/Drink";
                break;
            case "Bills":
                category = "Bills";
                break;
            case "Transport":
                category = "Transport";
                break;
            case "Clothing":
                category = "Clothing";
                break;
            case "Recreational":
                category =  "Recreational";
                break;
        }


        //calculate costs per day, week, month and year
        costPerWeek = price * perWeek;
        costPerDay = costPerWeek / 7;
        costPerMonth = (costPerWeek * weekYear) / 12;
        costPerYear = costPerWeek * weekYear;

        //initialise object values
        Item item = new Item();
        item.Name = itemName.getText().toString();
        item.price = price;
        item.category = category;

        item.costPerWeek = costPerWeek;
        item.costPerDay = costPerDay;
        item.costPerMonth = costPerMonth;
        item.costPerYear = costPerYear;

        String perX;

        if(getFreq == 1)
        {
            perX = "once a ";
        }
        else
        {
            perX = itemFreq.getText().toString()+" times a ";
        }


        Toast.makeText(NewItem.this, "Item Added!" , Toast.LENGTH_SHORT).show();

        //Display and initialise objects in View Item Layout

        setContentView(R.layout.viewitem);
        itemImage = findViewById(R.id.itemImage);
        itemViewName = findViewById(R.id.itemViewName);
        itemViewFreq = findViewById(R.id.itemViewFreq);
        itemViewDay = findViewById(R.id.itemViewDay);
        itemViewWeek = findViewById(R.id.itemViewWeek);
        itemViewMonth = findViewById(R.id.itemViewMonth);
        itemViewYear = findViewById(R.id.itemViewYear);

        //displays an image for the item based on the category selected

        switch (item.category)
        {
            case "Food/Drink":
                itemImage.setImageResource(R.drawable.food);
                break;
            case "Bills":
                itemImage.setImageResource(R.drawable.bill);
                break;
            case "Transport":
                itemImage.setImageResource(R.drawable.trans);
                break;
            case "Clothing":
                itemImage.setImageResource(R.drawable.cloth);
                break;
            case "Recreational":
                itemImage.setImageResource(R.drawable.rec);
                break;
        }

        //creates a database, opens it and creates an entry using the values of the object created above

        ItemsDB db = new ItemsDB(this);
        db.open();
        db.createItem(item.Name+" ("+necessary+")", ", £"+String.valueOf(item.price), " "+perX+radioButtonFreq.getText().toString()+", £"+String.format("%.2f", costPerDay) +" per day, "
                +"£"+String.format("%.2f", costPerMonth)+" per month "+", £"+String.format("%.2f", costPerYear)+" per year.");
        db.close();


        //sets the text values of the view item page based on the object's values

        itemViewName.setText(item.Name+" - £"+String.format("%.2f", item.price)+"("+item.category+").");
        itemViewFreq.setText("You buy this "+perX+radioButtonFreq.getText().toString()+". This is costing you:");
        itemViewDay.setText("£"+String.format("%.2f", costPerDay)+" a day");
        itemViewWeek.setText("£"+String.format("%.2f", costPerWeek)+" a week");
        itemViewMonth.setText("£"+String.format("%.2f", costPerMonth)+" a month");
        itemViewYear.setText("£"+String.format("%.2f", costPerYear)+" a year");

    }
        catch (NumberFormatException i){
            Toast.makeText(NewItem.this, "Incorrect value in price &/or how often item is purchased", Toast.LENGTH_SHORT).show();
        }
}
}



//navigates the user back to the home screen

    public void backHome(View view) {
        Intent intent = new Intent(this, home.class);
        startActivity(intent);

    }


}