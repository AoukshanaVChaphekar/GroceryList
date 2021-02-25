package com.example.babyneeds;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;

import com.example.babyneeds.database.databaseHandler;
import com.example.babyneeds.model.Details;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import com.example.babyneeds.MainActivity;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;


public class listactivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<Details> detailsList;
    private databaseHandler databaseHandler;
    private FloatingActionButton floatingActionButton;
    private AlertDialog dialog;
    private AlertDialog.Builder builder;

        private EditText babyitem;
        private EditText itemqty;
        private EditText itemcolor;
        private EditText itemsize;
        private Button saveButton;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listactivity);

        Window window =this.getWindow();

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.notification));

        floatingActionButton=findViewById(R.id.actionBar);
        floatingActionButton.setOnClickListener(    new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    pop();
            }
        });
        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        databaseHandler=new databaseHandler(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        detailsList=new ArrayList<>();
        detailsList=databaseHandler.getAlldetails();

        adapter= new recyclerViewAdapter(this,detailsList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();



    }
    private void pop()
    {
        builder=new AlertDialog.Builder(this);
        View view=getLayoutInflater().inflate(R.layout.popup,null);
        babyitem=view.findViewById(R.id.babyItem);
        itemcolor=view.findViewById(R.id.itemColor);
        itemqty=view.findViewById(R.id.itemQuantity);
        itemsize=view.findViewById(R.id.itemSize);
        saveButton=view.findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!babyitem.getText().toString().isEmpty() && !itemsize.getText().toString().isEmpty() && !itemqty.getText().toString().isEmpty()
                        && !itemcolor.getText().toString().isEmpty()) {
                    saveItem(view);


                    babyitem.setText("");
                    itemcolor.setText("");
                    itemqty.setText("");
                    itemsize.setText("");
                    List<Details> list = databaseHandler.getAlldetails();
                    for (Details details : list) {
                        Log.d("Database", "Id: " + details.getId() + " Item: " + details.getItem() + " Size: " + details.getSize() + " Color: "
                                + details.getColor() + " Quantity:  " + details.getQty() + " Date: " + details.getDate());
                    }
                }
                else
                {
                    Snackbar.make(view,"Empty Fields not allowed!", BaseTransientBottomBar.LENGTH_SHORT).show();
                }
            }
        });
        builder.setView(view);
        dialog=builder.create();
        dialog.show();
    }
    private void saveItem(View v) {
        Details details=new Details(String.valueOf(babyitem.getText().toString().trim()),Integer.parseInt(String.valueOf(itemqty.getText()).trim()),
                String.valueOf(itemcolor.getText()).trim(),
                Integer.parseInt(String.valueOf(itemsize.getText()).trim()));
        databaseHandler.addDetail(details);

        Snackbar.make(v,"Item Saved", BaseTransientBottomBar.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                startActivity(new Intent(listactivity.this,listactivity.class));
                finish();
            }
        },1200);



    }
}