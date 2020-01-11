package io.osman.football.league.repository.api;

import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.Response;

public class ResponseObserver<T> implements Observer<Response<T>> {

    private ChangeListener<T> changeListener;

    public static final String TAG = "ResponseObserver";

    public ResponseObserver(ChangeListener<T> changeListener) {
        this.changeListener = changeListener;
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(Response<T> tResponse) {
        Gson gson = new Gson();
        if (tResponse != null) {
            Log.w(TAG, "onNext: body: " + gson.toJson(tResponse.body()));
            Log.w(TAG, "onNext: errorBody: " + gson.toJson(tResponse.errorBody()));
            if (tResponse.isSuccessful() && tResponse.body() != null) {
                changeListener.onSuccess(tResponse.body());
                Log.w(TAG, "onNext: " + gson.toJson(tResponse.body()));
            } else {
                try {
                    if (tResponse.errorBody() != null) {
                        changeListener.onException(tResponse.errorBody().string());
                        Log.w(TAG, "error onNext: " + gson.toJson(tResponse.errorBody().string()));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.w(TAG, "error onNext: ", e);
                }
            }
            return;
        }
        changeListener.onException("Error");
    }

    @Override
    public void onError(Throwable e) {
        changeListener.onException(e.getMessage());
        Log.w(TAG, "onError: ", e);
    }

    @Override
    public void onComplete() {

    }

    public interface ChangeListener<T> {

        void onSuccess(T responseBody);

        void onException(String errorMessage);
    }
}
