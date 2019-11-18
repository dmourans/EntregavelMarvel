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
    private ImageView comicBackdrop;
    private ImageView comicPoster;
    private TextView comicTitle;
    private TextView comicDate;
    private TextView comicDescription;
    private TextView comicPrice;
    private TextView comicPages;
    private Button comicWebsiteButton;
    private ProgressBar progressBar;
    private RelativeLayout contentContainer;
    private DetalheActivityViewModel viewModel;
    private String comicUrl;
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
        comicBackdrop = findViewById(R.id.comic_backdrop);
        comicPoster = findViewById(R.id.comic_poster);
        comicTitle = findViewById(R.id.comic_title);
        comicDate = findViewById(R.id.comic_date);
        comicDescription = findViewById(R.id.comic_description);
        comicPrice = findViewById(R.id.comic_price);
        comicPages = findViewById(R.id.comic_pages);
        comicWebsiteButton = findViewById(R.id.comic_url_button);
        progressBar = findViewById(R.id.progress_details);
        contentContainer = findViewById(R.id.container_details);
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
            comicTitle.setText(result.getTitle());

            if (result.getDescription() != null) {
                comicDescription.setText(result.getDescription().toString());
            } else {
                comicDescription.setVisibility(View.GONE);
            }

            for (Date date : result.getDates()) {
                if (date.getType().equals("onsaleDate")) {
                    SimpleDateFormat output = new SimpleDateFormat("dd MMM yyyy", Locale.US);
                    comicDate.setText(output.format(date.getDate()));
                }
            }

            if (result.getPrices() != null) {
                comicPrice.setText(String.format(Locale.US, "$ %.2f", result.getPrices().get(0).getPrice()));
            }

            comicPages.setText(String.format(Locale.US, "%d", result.getPageCount()));
            thumbUrl = result.getThumbnail().getPath() + "/detail." + result.getThumbnail().getExtension();
            thumbUrlLandscape = result.getThumbnail().getPath() + "/landscape_incredible." + result.getThumbnail().getExtension();
            Picasso.get().load(thumbUrl).into(comicPoster);

            for (Url url : result.getUrls()) {
                if (url.getType().equals("detail")) {
                    comicUrl = url.getUrl();
                }
            }

            if (comicUrl != null && !comicUrl.isEmpty()) {
                comicWebsiteButton.setVisibility(View.VISIBLE);
                comicWebsiteButton.setOnClickListener(view -> {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(comicUrl));
                    startActivity(intent);
                });
            } else {
                comicWebsiteButton.setVisibility(View.GONE);
            }

            comicPoster.setOnClickListener(view -> {
                FullImageDialog.showImage(this, thumbUrl);
            });

        });

        viewModel.getVariant().observe(this, result1 -> {
            Picasso.get().load(result1.getThumbnail().getPath() + "/landscape_incredible." + result1.getThumbnail().getExtension()).into(comicBackdrop);
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
