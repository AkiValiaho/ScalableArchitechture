package com.akivaliaho;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.TimeUnit;

/**
 * Created by vagrant on 4/5/17.
 */
@Component
public class AsyncWrapper {
    public <T> DeferredResult<T> wrapAsDeferredResult(ListenableFuture<T> integerFuture) {
        DeferredResult<T> tDeferredResult = new DeferredResult<>(TimeUnit.SECONDS.convert(5, TimeUnit.MILLISECONDS));
        integerFuture.addCallback(new ListenableFutureCallback<T>() {
            @Override
            public void onFailure(Throwable throwable) {
                tDeferredResult.setErrorResult(throwable);
            }

            @Override
            public void onSuccess(T t) {
                tDeferredResult.setResult((T) ResponseEntity.ok(t));
            }
        });
        return tDeferredResult;
    }
}
