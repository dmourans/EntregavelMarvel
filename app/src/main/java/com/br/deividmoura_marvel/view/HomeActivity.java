package com.br.deividmoura_marvel.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.br.deividmoura_marvel.R;
import com.br.deividmoura_marvel.model.Result;
import com.br.deividmoura_marvel.view.adapter.RecyclerMesAdapter;
import com.br.deividmoura_marvel.view.interfaces.LivroOnClick;
import com.br.deividmoura_marvel.viewmodel.HomeActivityViewModel;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements LivroOnClick {
    private RecyclerView recyclerMonthComics;
    private ProgressBar progressMonthComics;
    private HomeActivityViewModel viewModel;
    private RecyclerMesAdapter adapter;
    private List<Result> monthComics = new ArrayList<>();

    public static final String RESULT_KEY = "result";

    public int offset = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().setTitle("This Month");

        initViews();
        setScrollView();

        viewModel.getThisMonthComics(offset);

        viewModel.getComics().observe(this, results -> {
            if (results != null && !results.isEmpty()) {
                adapter.updateList(results);
            } else {
                adapter.updateList(this.monthComics);
            }
        });

        viewModel.getLoading().observe(this, aBoolean -> {
            if (aBoolean) {
                progressMonthComics.setVisibility(View.VISIBLE);
            } else {
                progressMonthComics.setVisibility(View.GONE);
            }
        });

        viewModel.getError().observe(this, s -> {
            Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
        });
    }

    public void initViews() {
        recyclerMonthComics = findViewById(R.id.recycler_home_comics);
        progressMonthComics = findViewById(R.id.progress_home_comics);
        viewModel = ViewModelProviders.of(this).get(HomeActivityViewModel.class);
        adapter = new RecyclerMesAdapter(monthComics, this);
        recyclerMonthComics.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerMonthComics.setAdapter(adapter);
    }

    @Override
    public void onClick(Result result) {
        Intent intent = new Intent(HomeActivity.this, DetalheActivity.class);
        intent.putExtra(RESULT_KEY, result.getId());
        startActivity(intent);
    }

    private void setScrollView() {
        recyclerMonthComics.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();

                int totalItemCount = layoutManager.getItemCount();
                int lastVisible = layoutManager.findLastVisibleItemPosition();
                boolean ultimoItem = lastVisible + 6 >= totalItemCount;

                if (totalItemCount > 0 && ultimoItem) {
                    offset += 20;
                    viewModel.getThisMonthComics(offset);
                }
            }
        });
    }
}
