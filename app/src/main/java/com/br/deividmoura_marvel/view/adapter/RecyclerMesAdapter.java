package com.br.deividmoura_marvel.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.br.deividmoura_marvel.R;
import com.br.deividmoura_marvel.model.Result;
import com.br.deividmoura_marvel.view.interfaces.LivroOnClick;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerMesAdapter extends RecyclerView.Adapter<RecyclerMesAdapter.ViewHolder> {
    private List<Result> listaResultado;
    private LivroOnClick listener;

    public RecyclerMesAdapter(List<Result> listaResultado, LivroOnClick listener) {
        this.listaResultado = listaResultado;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comic, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Result result = listaResultado.get(position);
        holder.onBind(result);

        holder.itemView.setOnClickListener(view -> {
            listener.onClick(result);
        });
    }

    @Override
    public int getItemCount() {
        return listaResultado == null ? 0 : listaResultado.size();
    }

    public void updateList(List<Result> newList) {
        if (this.listaResultado.isEmpty()) {
            this.listaResultado = newList;
        } else {
            this.listaResultado.addAll(newList);
        }
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView livroCover;
        private TextView livroTitulo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            livroCover = itemView.findViewById(R.id.imagem_item_cover);
            livroTitulo = itemView.findViewById(R.id.texto_item_titulo);
        }

        public void onBind(Result result) {
            livroTitulo.setText(result.getTitle());
            Picasso picasso = Picasso.get();
            picasso.load(result.getThumbnail().getPath() + "/detail." + result.getThumbnail().getExtension()).into(livroCover);
        }
    }
}
