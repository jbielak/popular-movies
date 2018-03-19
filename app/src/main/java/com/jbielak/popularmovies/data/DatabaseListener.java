package com.jbielak.popularmovies.data;

/**
 * Created by Justyna on 2018-03-18.
 */

public interface DatabaseListener {
    void onInsertSuccess();
    void onInsertError();
    void onRemoveSuccess();
    void onRemoveError();
}
