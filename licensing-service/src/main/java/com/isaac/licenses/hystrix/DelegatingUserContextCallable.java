package com.isaac.licenses.hystrix;

import com.isaac.licenses.util.UserContext;
import com.isaac.licenses.util.UserContextHolder;

import java.util.concurrent.Callable;

public class DelegatingUserContextCallable<V> implements Callable<V> {
    private final  Callable<V> delegate;
    private UserContext originalUserContext;

    public DelegatingUserContextCallable(
            Callable<V> delegate, UserContext originalUserContext) {
        this.delegate = delegate;
        this.originalUserContext = originalUserContext;
    }

    @Override
    public V call() throws Exception {
        UserContextHolder.setContext(originalUserContext);
        try {
            return delegate.call();
        } finally {
            this.originalUserContext = null;
        }
    }

    public static <V> Callable<V> create(Callable<V> delegate, UserContext userContext){
        return new DelegatingUserContextCallable<>(delegate, userContext);
    }
}
