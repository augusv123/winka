package com.google.firebase.codelab.friendlychat.MiCuenta;




import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.codelab.friendlychat.MiCuenta.Card;
import com.google.firebase.codelab.friendlychat.R;

import java.util.List;

public class CardsAdapter extends RecyclerView.Adapter<CardsAdapter.ViewHolder> {
    private List<Card> tarjetas;
    private int layout;
    private OnItemClickListener itemClickListener;

    public CardsAdapter(List<Card> listado, int layout, OnItemClickListener listener){

        this.tarjetas = listado;
        this.layout = layout;
        this.itemClickListener = listener;
    }
    @NonNull
    @Override

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(layout,parent,false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(tarjetas.get(position),itemClickListener);
    }




    @Override
    public int getItemCount() {
        return tarjetas.size();
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {
        public Switch cardSwitch;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.cardSwitch = itemView.findViewById(R.id.switchCard);


        }
        public void bind(final Card card, final OnItemClickListener listener){
            cardSwitch.setText(card.getValue());
          /*  cardSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    // do something, the isChecked will be
                    // true if the switch is in the On position
                    if(isChecked)
                }
            });*/

            itemView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   listener.onItemClick(card,getAdapterPosition(),cardSwitch.isChecked());
               }
           });


        }



    }
    public interface OnItemClickListener {
        void onItemClick(Card card ,int position,boolean checked);
    }
}
