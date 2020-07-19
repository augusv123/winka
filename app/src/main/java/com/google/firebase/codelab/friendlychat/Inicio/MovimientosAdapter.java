package com.google.firebase.codelab.friendlychat.Inicio;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.codelab.friendlychat.R;

import java.util.List;

public class MovimientosAdapter extends RecyclerView.Adapter<MovimientosAdapter.ViewHolder> {
    private List<Movement> movimientos;
    private int layout;
    public MovimientosAdapter(List<Movement> listado, int layout){

        this.movimientos = listado;
        this.layout = layout;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(layout,null,false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(movimientos.get(position));
    }

    @Override
    public int getItemCount() {
        return movimientos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView movimientosText;
        public TextView keymovimiento;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.movimientosText = itemView.findViewById(R.id.movimientosText);
            this.keymovimiento = itemView.findViewById(R.id.keymovimiento);

        }
        public void bind(final Movement movimiento){
            movimientosText.setText(movimiento.getValue());
            keymovimiento.setText(movimiento.getKey());
        }
    }
}
