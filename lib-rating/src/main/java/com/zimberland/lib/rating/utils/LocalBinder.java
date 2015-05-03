package com.zimberland.lib.rating.utils;

import android.app.Service;
import android.os.Binder;

import java.lang.ref.WeakReference;

public class LocalBinder<S extends Service> extends Binder {
    private WeakReference<S> service;

    public LocalBinder(S service) {
        this.service = new WeakReference<S>(service);
    }

    public S getService() {
        return service.get();
    }

}
