package com.sztouyun.advertisingsystem.utils;

import com.sztouyun.advertisingsystem.viewmodel.store.StoreDataMapInfo;

public class DistanceUtil {
    private static double EARTH_RADIUS = 6371;

    public static StoreDataMapInfo returnSquarePoint(double longitude, double latitude, double distance) {
        // 计算经度弧度,从弧度转换为角度
        double dLongitude = 2 * (Math.asin(Math.sin(distance / (2 * EARTH_RADIUS)) / Math.cos(Math.toRadians(latitude))));
        dLongitude = Math.toDegrees(dLongitude);
        // 计算纬度角度
        double dLatitude = distance / EARTH_RADIUS;
        dLatitude = Math.toDegrees(dLatitude);
        // 正方形
        double[] leftTopPoint = { latitude + dLatitude, longitude - dLongitude };
        double[] rightTopPoint = { latitude + dLatitude, longitude + dLongitude };
        double[] leftBottomPoint = { latitude - dLatitude, longitude - dLongitude };
        double[] rightBottomPoint = { latitude - dLatitude, longitude + dLongitude };
        return new StoreDataMapInfo(leftTopPoint[1], leftTopPoint[0], rightTopPoint[1], rightTopPoint[0], leftBottomPoint[1], leftBottomPoint[0], rightBottomPoint[1], rightBottomPoint[0]);
    }
}
