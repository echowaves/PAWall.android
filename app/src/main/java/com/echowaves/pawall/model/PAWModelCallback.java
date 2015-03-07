package com.echowaves.pawall.model;

/**
 * Created by dmitry on 3/2/15.
 *
 */
abstract public class PAWModelCallback {
    public abstract void succeeded(Object results);
    public abstract void failed(com.parse.ParseException e);
}
