package com.sztouyun.advertisingsystem.common.map;

import java.util.Collection;

public interface IMapService {
    int searchNearByCount(double longitude, double latitude, int distance, Collection<String> poiTypes);
}
