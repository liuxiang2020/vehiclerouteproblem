package com.liuxiang.vrp.algorithm.operation;

import com.liuxiang.vrp.element.Route;
import com.liuxiang.vrp.enums.NeighborHoodType;
import com.liuxiang.vrp.service.ArrayRandom;
import com.liuxiang.vrp.service.RouteService;

/**
 * 扰动操作
 */
public class OperationService  {

    RouteService routeService;

    public OperationService(RouteService routeService) {
        this.routeService = routeService;
    }

    /**
     * shaking
     * @param routeService
     * @param route
     * @param neighborHoodType
     * @return
     */
    public static Route shaking(RouteService routeService, Route route, NeighborHoodType neighborHoodType){

        // 深拷贝

        int[] indexs = ArrayRandom.randomTwo(0, route.length());
        switch (neighborHoodType){
            case SWAP:
                routeService.swap(route, indexs[0], indexs[1]);
                break;
            case INSERT:
                routeService.insert(route, indexs[0], indexs[1]);
                break;
            case REVERSION:
                routeService.reversion(route, indexs[0], indexs[1]);
        }
        return route;
    }

    public static void swapNeighbor(RouteService routeService, Route route, int maxSwapNum){
        double[][] gapDistance = new double[route.length()][route.length()];
        for (int i = 0; i < route.length(); i++)
            for (int j = i+1; j < route.length(); j++)
                gapDistance[i][j] = gapDistance[j][i] = routeService.calculateSwapGapDistance(route, i, j);
        int localIter = 0;
        while(localIter<maxSwapNum){

            localIter++;
        }

    };

    public static void reversionNeighbor(Route route, int maxReversionNum){

    }

    public static void insertNeighbor(Route route, int maxInsertNum){

    }
}
