package com.br.deividmoura_marvel.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.br.deividmoura_marvel.model.Result;
import com.br.deividmoura_marvel.model.repository.LivrosRepository;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static com.br.deividmoura_marvel.util.Md5Creator.PRIVATE_KEY;
import static com.br.deividmoura_marvel.util.Md5Creator.PUBLIC_KEY;
import static com.br.deividmoura_marvel.util.Md5Creator.md5;

public class DetalheActivityViewModel extends AndroidViewModel {



    private MutableLiveData<Result> comic = new MutableLiveData<>();
    private MutableLiveData<String> error = new MutableLiveData<>();
    private MutableLiveData<Boolean> loading = new MutableLiveData<>();
    private CompositeDisposable disposable = new CompositeDisposable();
    private LivrosRepository repository = new LivrosRepository();
    private MutableLiveData<Result> variant = new MutableLiveData<>();

    public String timestamp = Long.toString(System.currentTimeMillis() / 1000);
    public String hash = md5(timestamp + PRIVATE_KEY + PUBLIC_KEY);

    public DetalheActivityViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Result> getComic() {
        return this.comic;
    }

    public LiveData<Result> getVariant() {
        return this.variant;
    }

    public LiveData<String> getError() {
        return this.error;
    }

    public LiveData<Boolean> getLoading() {
        return this.loading;
    }

    public void getComicById(Long comicId) {
        disposable.add(
                repository.getSingle(comicId, timestamp, hash, PUBLIC_KEY)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(disposable1 -> {
                            loading.setValue(true);
                        })
                        .doAfterTerminate(() -> {
                            loading.setValue(false);
                        })
                        .doOnError(throwable -> {
                            error.setValue(throwable.getMessage());
                        })
                        .doOnNext(comicsResponse -> {
                            comic.setValue(comicsResponse.getData().getResults().get(0));
                        })
                        .observeOn(Schedulers.newThread())
                        .flatMap(comicsResponse -> {
                                    if (!comicsResponse.getData().getResults().get(0).getVariants().isEmpty()) {
                                        return repository.getSingle(getIdFromVariantUrl(comicsResponse.getData().getResults().get(0).getVariants().get(0).getResourceURI()), timestamp, hash, PUBLIC_KEY);
                                    } else {
                                        return Observable.just(comicsResponse);
                                    }
                                }
                        )
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnError(throwable -> {
                            error.setValue(throwable.getMessage());
                        })
                        .doOnNext(comicsResponse -> {
                            variant.setValue(comicsResponse.getData().getResults().get(0));
                        })
                        .subscribe()

        );
    }

    private Long getIdFromVariantUrl(String url) {
        return Long.parseLong(url.replace("http://gateway.marvel.com/v1/public/comics/", ""));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}
