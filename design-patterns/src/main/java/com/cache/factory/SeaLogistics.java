package com.cache.factory;

public class SeaLogistics extends Logistics{

    public Transport createTransport()
    {
        return new Ship();
    }
}
