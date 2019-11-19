package com.br.deividmoura_marvel.model.repository;

import com.br.deividmoura_marvel.model.ComicsResponse;

import io.reactivex.Observable;

import static com.br.deividmoura_marvel.model.data.remote.RetrofitService.getApiService;

public class LivrosRepository {

    public Observable<ComicsResponse> getLivros(String data, String formato, String tipoFormato, String ordem, String timestamp, String hash, String apiKey, Boolean noVariants, int offset) {
        return getApiService().getAllLivros(data, formato, tipoFormato, ordem, timestamp, hash, apiKey, noVariants, offset);
    }

    public Observable<ComicsResponse> getUmLivro(Long id, String timestamp, String hash, String apiKey) {
        return getApiService().getUmLivro(id, timestamp, hash, apiKey);
    }

}
