package com.example.babyneeds;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.babyneeds.database.databaseHandler;
import com.example.babyneeds.model.Details;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static androidx.core.content.ContextCompat.startActivity;

public class recyclerViewAdapter extends RecyclerView.Adapter<recyclerViewAdapter.ViewHolder> {
    public Context context;
    public List<Details> detailsList;
    public AlertDialog alertDialog;
    public AlertDialog.Builder builder;
    private LayoutInflater inflater;
    private databaseHandler databaseHandler;
    public recyclerViewAdapter(Context context, List<Details> detailsList)
    {
        this.context=context;
        this.detailsList=detailsList;
    }
    public recyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_row,viewGroup,false);
        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull recyclerViewAdapter.ViewHolder holder, int position) {

        Details details=detailsList.get(position);

        holder.itemName.setText("Item: "+details.getItem());
        holder.itemColor.setText("Color: "+details.getColor());
        holder.itemQty.setText("Quantity: "+String.valueOf(details.getQty()));
        holder.itemSize.setText("Size: "+String.valueOf(details.getSize()));
        holder.itemDate.setText("Date Modified: "+details.getDate());
        

    }

    @Override
    public int getItemCount() {

        return detailsList.size();
    }

   public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView itemName;
        public TextView itemQty;
        public int itemId;
        public TextView itemDate;
        public TextView itemColor;
        public TextView itemSize;
        public Button editButton;
        public Button deleteButton;



        public ViewHolder(@NonNull View itemView,Context ctx) {
            super(itemView);
            context=ctx;

            itemName=itemView.findViewById(R.id.item);
            itemColor=itemView.findViewById(R.id.item_color);
            itemQty=itemView.findViewById(R.id.item_qty);
            itemSize=itemView.findViewById(R.id.item_size);
            itemDate=itemView.findViewById(R.id.item_date);
            editButton=(Button)itemView.findViewById(R.id.editButton);
            deleteButton=(Button)itemView.findViewById(R.id.deleteButton);

            editButton.setOnClickListener(this);
            deleteButton.setOnClickListener(this);



        }

        @Override
        public void onClick(View view) {
            int position=getAdapterPosition();
            switch (view.getId())
            {
                case R.id.editButton:
                    position=getAdapterPosition();
                    Details details=detailsList.get(position);
                    edit(details);
                    break;
                case R.id.deleteButton:

                    position=getAdapterPosition();
                    Details detail=detailsList.get(position);
                    deleteItem(detail.getId());
                    break;
            }
        }

       private void edit(final Details details) {

            builder=new AlertDialog.Builder(context);
            inflater=LayoutInflater.from(context);
            View view=inflater.inflate(R.layout.popup,null);
            databaseHandler=new databaseHandler(context);
            builder.setView(view);
            alertDialog=builder.create();
            alertDialog.show();
            final Button saveButton=view.findViewById(R.id.saveButton);
            final TextView itemV=view.findViewById(R.id.babyItem);
            final TextView sizeV=view.findViewById(R.id.itemSize);
            final TextView qtyV=view.findViewById(R.id.itemQuantity);
            final TextView colorV=view.findViewById(R.id.itemColor);

            itemV.setText(details.getItem());
            sizeV.setText(String.valueOf(details.getSize()));
            qtyV.setText(String.valueOf(details.getQty()));
            colorV.setText(details.getColor());


            saveButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    if (!itemV.getText().toString().isEmpty() && !sizeV.getText().toString().isEmpty() && !qtyV.getText().toString().isEmpty()
                            && !colorV.getText().toString().isEmpty()) {


                        Details details=new Details(String.valueOf(itemV.getText().toString().trim()),Integer.parseInt(String.valueOf(qtyV.getText()).trim()),
                                String.valueOf(colorV.getText()).trim(),
                                Integer.parseInt(String.valueOf(sizeV.getText()).trim()));
                        databaseHandler.updateDetail(details);
                        itemName.setText("Item: "+details.getItem());
                        itemColor.setText("Color:"+details.getColor());
                        itemQty.setText("Quantity: "+String.valueOf(details.getQty()));
                        itemSize.setText("Size: "+String.valueOf(details.getSize()));


                        Date d=new Date();
                        d.setTime(java.lang.System.currentTimeMillis());
                        String formattedDate=new SimpleDateFormat("d MMM ,YYYY").format(d);

                        itemDate.setText("Date Modified: "+formattedDate);
                        Snackbar.make(view,"Item Updated", BaseTransientBottomBar.LENGTH_SHORT).show();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                alertDialog.dismiss();
                            }
                        },1200);

                        itemV.setText("");
                        sizeV.setText("");
                        qtyV.setText("");
                        colorV.setText("");

                    }
                    else
                    {
                        Snackbar.make(view,"Empty Fields not allowed!", BaseTransientBottomBar.LENGTH_SHORT).show();
                    }



                }
            });
       }

       public void deleteItem(final int id)
        {
            builder=new AlertDialog.Builder(context);
            inflater=LayoutInflater.from(context);
            View view=inflater.inflate(R.layout.confirmation,null);
            Button yes=view.findViewById(R.id.conf);
            Button no=view.findViewById(R.id.noDelete);
            builder.setView(view);
            alertDialog=builder.create();
            alertDialog.show();

            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    databaseHandler databaseHandler=new databaseHandler(context);
                    databaseHandler.delete(id);
                    detailsList.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                    alertDialog.dismiss();
                }
            });
            no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });


        }

    }


}
