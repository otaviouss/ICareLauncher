package com.example.otavio.tcc.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.otavio.tcc.Model.Alarme;
import com.example.otavio.tcc.R;
import com.example.otavio.tcc.SQLite.TabelaAlarmes;
import com.example.otavio.tcc.Telas.EditarAlarmes;

import java.util.List;

public class Alarmes_Adapter extends RecyclerView.Adapter<Alarmes_Adapter.MyViewHolder> {

    private Context context;
    private List<Alarme> alarmeList;
    private TabelaAlarmes tabelaAlarmes;

    public Alarmes_Adapter(Context context, List<Alarme> alarmeList) {
        this.context = context;
        this.alarmeList = alarmeList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(context).inflate(R.layout.item_alarme, parent, false);
        tabelaAlarmes = new TabelaAlarmes(context);
        final MyViewHolder viewHolder = new MyViewHolder(v);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditarAlarmes.class);
                context.startActivity(intent);
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        //final int position = holder.getAdapterPosition();
        holder.nome.setText(alarmeList.get(position).getNome());
        holder.descricao.setText(alarmeList.get(position).getDescricao());
        holder.switchOnOff.setScaleX((float) 1.3);
        holder.switchOnOff.setScaleY((float) 1.3);

        String onOff = tabelaAlarmes.carregaDados().get(position).getLigado();

        if (onOff.equals("1")) {
            holder.switchOnOff.setChecked(true);
        } else {
            holder.switchOnOff.setChecked(false);
        }

        holder.switchOnOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String ligado;
                if (isChecked) {
                    ligado = "1";
                } else {
                    ligado = "0";
                }
                tabelaAlarmes.alteraSituacao(alarmeList.get(position).getID(), ligado);
            }
        });

        holder.btnDeletaAlarme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("Excluir")
                        .setMessage("Tem certeza que quer exluir o alarme?")
                        .setPositiveButton("Excluir", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                tabelaAlarmes.deletaRegistro(alarmeList.get(position));
                                Toast.makeText(context, "Alarme deletado com sucesso!", Toast.LENGTH_SHORT).show();

                                try {
                                    alarmeList.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position, getItemCount());
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }

                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //não exclui, apenas fecha a mensagem
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return alarmeList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nome;
        TextView descricao;
        Switch switchOnOff;
        ImageButton btnDeletaAlarme;
        RecyclerView recyclerView;


        public MyViewHolder(View itemView) {
            super(itemView);

            nome = itemView.findViewById(R.id.nome);
            descricao = itemView.findViewById(R.id.descricao);
            switchOnOff = itemView.findViewById(R.id.switchOnOff);
            btnDeletaAlarme = itemView.findViewById(R.id.buttonDelete);
            recyclerView = itemView.findViewById(R.id.recyclerViewAlarmes);
        }


    }

}