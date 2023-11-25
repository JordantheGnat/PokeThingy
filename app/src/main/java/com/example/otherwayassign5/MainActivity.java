package com.example.otherwayassign5;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
ArrayList<String> itemList;
ListView listView;
Button submitButton;
ArrayAdapter<String> adapter;
EditText inputText;
TextView numberTV,heightTV2,weightTV,baseXP,nameTV,moveTV,abilityTV;
ImageView pokeIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidNetworking.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        submitButton = findViewById(R.id.requestButton);
        inputText = findViewById(R.id.imputET);
        numberTV = findViewById(R.id.numberTV);
        heightTV2 = findViewById(R.id.heightTV2);
        weightTV = findViewById(R.id.weightTV);
        baseXP = findViewById(R.id.baseXP);
        nameTV  = findViewById(R.id.nameTV);
        pokeIV = findViewById(R.id.pokeIV);
        listView = findViewById(R.id.listView);
        moveTV = findViewById(R.id.move);
        abilityTV = findViewById(R.id.abilityTV);
        itemList =  new ArrayList<>();
        adapter =  new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,itemList);
        listView.setAdapter(adapter);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeRequest(inputText.getText().toString());
            }
        });
    }
    //https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/35.png
    private void makeRequest(String pokemon){
        pokemon = pokemon.toLowerCase();
        boolean containsOnlyNumbers = pokemon.matches("[0-9]+");
        boolean cointainsOnlyLetter = pokemon.matches("[a-z]+");

        if(containsOnlyNumbers==true){

            //Log.i("String",imgURL);
            String request = "https://pokeapi.co/api/v2/pokemon/"+pokemon+"/";
            makeRequestHelper(request);
        }else if (cointainsOnlyLetter==true){
            String request = "https://pokeapi.co/api/v2/pokemon/"+pokemon+"/";
            makeRequestHelper(request);
        }else{
            Toast.makeText(getApplicationContext(),"",Toast.LENGTH_SHORT);
        }

    }
    public void makeRequestHelper(String request){
        ANRequest req =
                AndroidNetworking.get(request)
                        .setPriority(Priority.LOW)
                        .build();
        req.getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("Response:","Successful");
                try {
                    String numberStr =response.getString("id");
                    String imgURL = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/"+numberStr+".png";
                    Log.i("ImgUrl",imgURL);
                    numberTV.setText(numberStr);
                    nameTV.setText(response.getString("name"));
                    heightTV2.setText(response.getString("height"));
                    weightTV.setText(response.getString("weight"));
                    baseXP.setText(response.getString("base_experience"));
                    /*JSONObject firstLayer = response.getJSONObject("moves");
                    JSONObject secondLayer = firstLayer.getJSONObject("0");
                    JSONObject thirdLayer = secondLayer.getJSONObject("move"); this should work, but I cant get it to :/
                    moveTV.setText(thirdLayer.getString("name"));*/
                    Picasso.get().load(imgURL).into(pokeIV);
                    itemList.add(response.getString("name")+" "+numberStr);
                    adapter.notifyDataSetChanged();
                    
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onError(ANError anError) {
                Log.i("Issue","Holding");//for some reason its holding here nvm fixed it url was bad
            }
        });
    }

}