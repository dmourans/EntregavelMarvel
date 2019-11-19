package com.br.deividmoura_marvel.model.repository;

import com.br.deividmoura_marvel.model.ComicsResponse;

import io.reactivex.Observable;

import static com.br.deividmoura_marvel.model.data.remote.RetrofitService.getApiService;

public class LivrosRepository {

    public Observable<ComicsResponse> getComics(String data, String format, String formatType, String order, String timestamp, String hash, String apiKey, Boolean noVariants, int offset) {
        return getApiService().getAllLivros(data, format, formatType, order, timestamp, hash, apiKey, noVariants, offset);
    }

    public Observable<ComicsResponse> getSingle(Long id, String timestamp, String hash, String apiKey) {
        return getApiService().getSingleLivro(id, timestamp, hash, apiKey);
    }

}
