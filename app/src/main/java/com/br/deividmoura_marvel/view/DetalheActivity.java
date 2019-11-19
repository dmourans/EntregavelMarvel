package com.br.deividmoura_marvel.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import com.br.deividmoura_marvel.R;
import com.br.deividmoura_marvel.model.Date;
import com.br.deividmoura_marvel.model.Url;
import com.br.deividmoura_marvel.viewmodel.DetalheActivityViewModel;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Locale;

import static com.br.deividmoura_marvel.view.HomeActivity.RESULT_KEY;

public class DetalheActivity extends AppCompatActivity {
    private ImageView livroBackDrop;
    private ImageView livroPoster;
    private TextView livroTitulo;
    private TextView livroData;
    private TextView livroDescricao;
    private TextView livroPreco;
    private TextView livroPaginas;
    private Button livroSiteButton;
    private ProgressBar progressBar;
    private RelativeLayout contentContainer;
    private DetalheActivityViewModel viewModel;
    private String livroUrl;
    private String thumbUrl;
    private String thumbUrlLandscape;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe);

        initViews();

        Toolbar toolbar = findViewById(R.id.toolbar_detalhes);

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        getData();
    }

    private void initViews() {
        livroBackDrop = findViewById(R.id.livro_backdrop);
        livroPoster = findViewById(R.id.livro_poster);
        livroTitulo = findViewById(R.id.livro_titulo);
        livroData = findViewById(R.id.livro_data);
        livroDescricao = findViewById(R.id.livro_descricao);
        livroPreco = findViewById(R.id.livro_preco);
        livroPaginas = findViewById(R.id.livro_paginas);
        livroSiteButton = findViewById(R.id.livro_url_botao);
        progressBar = findViewById(R.id.progress_detalhes);
        contentContainer = findViewById(R.id.container_detalhes);
        viewModel = ViewModelProviders.of(this).get(DetalheActivityViewModel.class);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void getData() {
        Long id = getIntent().getLongExtra(RESULT_KEY, 0);

        viewModel.getComicById(id);

        viewModel.getComic().observe(this, result -> {
            livroTitulo.setText(result.getTitle());

            if (result.getDescription() != null) {
                livroDescricao.setText(result.getDescription().toString());
            } else {
                livroDescricao.setVisibility(View.GONE);
            }

            for (Date date : result.getDates()) {
                if (date.getType().equals("onsaleDate")) {
                    SimpleDateFormat output = new SimpleDateFormat("dd MMM yyyy", Locale.US);
                    livroData.setText(output.format(date.getDate()));
                }
            }

            if (result.getPrices() != null) {
                livroPreco.setText(String.format(Locale.US, "$ %.2f", result.getPrices().get(0).getPrice()));
            }

            livroPaginas.setText(String.format(Locale.US, "%d", result.getPageCount()));
            thumbUrl = result.getThumbnail().getPath() + "/detail." + result.getThumbnail().getExtension();
            thumbUrlLandscape = result.getThumbnail().getPath() + "/landscape_incredible." + result.getThumbnail().getExtension();
            Picasso.get().load(thumbUrl).into(livroPoster);

            for (Url url : result.getUrls()) {
                if (url.getType().equals("detail")) {
                    livroUrl = url.getUrl();
                }
            }

            if (livroUrl != null && !livroUrl.isEmpty()) {
                livroSiteButton.setVisibility(View.VISIBLE);
                livroSiteButton.setOnClickListener(view -> {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(livroUrl));
                    startActivity(intent);
                });
            } else {
                livroSiteButton.setVisibility(View.GONE);
            }

            livroPoster.setOnClickListener(view -> {
                FullImageDialog.showImage(this, thumbUrl);
            });

        });

        viewModel.getVariant().observe(this, result1 -> {
            Picasso.get().load(result1.getThumbnail().getPath() + "/landscape_incredible." + result1.getThumbnail().getExtension()).into(livroBackDrop);
        });

        viewModel.getError().observe(this, s -> {
            Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
        });

        viewModel.getLoading().observe(this, aBoolean -> {
            if (aBoolean) {
                progressBar.setVisibility(View.VISIBLE);
                contentContainer.setVisibility(View.GONE);
            } else {
                progressBar.setVisibility(View.GONE);
                contentContainer.setVisibility(View.VISIBLE);
            }
        });

    }
}
