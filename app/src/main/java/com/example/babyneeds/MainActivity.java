package com.example.babyneeds;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.example.babyneeds.database.databaseHandler;
import com.example.babyneeds.model.Details;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import java.text.DateFormat;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private EditText babyitem;
    private EditText itemqty;
    private EditText itemcolor;
    private EditText itemsize;

    private Button saveButton;

    databaseHandler databaseHandler=new databaseHandler(this);
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        Window window =this.getWindow();

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.notification));

        setSupportActionBar(toolbar);

        byPassActivity();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup();
            }
        });
    }

    private void byPassActivity() {
       if(databaseHandler.count()>0)
       {
           startActivity(new Intent(MainActivity.this,listactivity.class));
           finish();
       }
    }

    @Override
    public String[] databaseList() {
        return super.databaseList();
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
                startActivity(new Intent(MainActivity.this,listactivity.class));
            }
        },1200);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public  void popup()
    {
        builder=new AlertDialog.Builder(this);
        View view= getLayoutInflater().inflate(R.layout.popup,null);
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

                }
                else
                {
                    Snackbar.make(view,"Empty Fields not allowed!",BaseTransientBottomBar.LENGTH_SHORT).show();
                }
            }
        });
        builder.setView(view);
        dialog=builder.create();

        dialog.show();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }
}