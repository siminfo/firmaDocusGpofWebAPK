package com.gtappdevelopers.firmadocus;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class DocusAdapter extends RecyclerView.Adapter<DocusAdapter.ViewHolder> {

    private static final String TAG = "DEBUG LineasInventAdap";
    private final Activity padre;

    private List<DocuementosClass> documentos;

    public DocusAdapter(Activity padre, List<DocuementosClass> documentos) {
        this.padre = padre;
        this.documentos = documentos;
    }

    @NonNull
    @Override
    public DocusAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.recycler_docus, parent, false);
        return new DocusAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DocusAdapter.ViewHolder holder, int position) {
        final DocuementosClass docActual = documentos.get(position);
        if(docActual != null ) {




            holder.txtFecha.setText(docActual.getFecha());
            holder.txtPaciente.setText(docActual.getPaciente());
            holder.txtNif.setText(docActual.getNif());
            holder.txtDoc.setText(docActual.getArchivo());





            holder.layoutPadre.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    Intent i = new Intent(padre, PdfActivity.class);
                    Log.d("tag", docActual.getIDDP()+"");

                    i.putExtra("iddp", docActual.getIDDP()+"");
                    i.putExtra("archivo",docActual.getRuta());

                    padre.startActivity(i);


                }
            });



       }
    }

    @Override
    public int getItemCount() {
        return documentos.size();
    }




    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtFecha;
        TextView txtPaciente;
        TextView txtNif;
        TextView txtDoc;
        LinearLayout layoutPadre;

        ViewHolder(View itemView) {
            super(itemView);
            layoutPadre = itemView.findViewById(R.id.padre);
            txtFecha = itemView.findViewById(R.id.txtFecha);
            txtPaciente = itemView.findViewById(R.id.txtPaciente);
            txtNif = itemView.findViewById(R.id.txtNif);
            txtDoc = itemView.findViewById(R.id.txtDoc);


        }
    }
}
