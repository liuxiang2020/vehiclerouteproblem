package com.liuxiang.vrp.algorithm.evaluate;

import com.liuxiang.vrp.element.Route;
import com.liuxiang.vrp.service.RouteService;

public class DistanceUpdate {

    public static double[][] updateSwapDistance(RouteService routeService, Route route, double[][] gapDistance){
        for (int i = 0; i < route.length(); i++)
            for (int j = i; j < route.length(); j++)
                gapDistance[i][j] = gapDistance[j][i] = routeService.calculateSwapGapDistance(route, i, j);

       return gapDistance;
    }

    public static double[][] updateInsertDistance(RouteService routeService, Route route, double[][] gapDistance){
        for (int i = 0; i < route.length(); i++)
            for (int j = 0; j < route.length(); j++)
                gapDistance[i][j] = routeService.calculateInsertGapDistance(route, i, j);

        return gapDistance;
    }

    public static double[][] updateReversionDistance(RouteService routeService, Route route, double[][] gapDistance){
        for (int i = 0; i < route.length(); i++)
            for (int j = i; j < route.length(); j++)
                gapDistance[i][j] = gapDistance[j][i] = routeService.calculateReversionGapDistance(route, i, j);

        return gapDistance;
    }

}
