package com.liuxiang.vrp.algorithm.operation;

import com.liuxiang.vrp.algorithm.evaluate.DistanceUpdate;
import com.liuxiang.vrp.element.Coordinate;
import com.liuxiang.vrp.element.Route;
import com.liuxiang.vrp.enums.NeighborHoodType;
import com.liuxiang.vrp.service.ArrayUtils;
import com.liuxiang.vrp.service.RouteService;
import org.apache.commons.lang3.SerializationUtils;

/**
 * 扰动操作
 */
public class OperationService  {

    RouteService routeService;

    public OperationService(RouteService routeService) {
        this.routeService = routeService;
    }

    /**
     * shaking 扰动算子
     * @param routeService
     * @param route
     * @param neighborHoodType
     * @return
     */
    public static Route shaking(RouteService routeService, Route route, NeighborHoodType neighborHoodType){

        int[] indexs = ArrayUtils.randomTwo(0, route.length());
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

    public static Route swapNeighbor(RouteService routeService, Route curRoute, int maxSwapNum){
        Route route = (Route) SerializationUtils.clone(curRoute);
        shaking(routeService, route, NeighborHoodType.SWAP);
        double[][] gapDistance = new double[route.length()][route.length()];
        for (int i = 0; i < route.length(); i++)
            for (int j = i; j < route.length(); j++)
                gapDistance[i][j] = gapDistance[j][i] = routeService.calculateSwapGapDistance(route, i, j);

        int localIter = 0;
        while(localIter<maxSwapNum){
            Coordinate coordinate = ArrayUtils.findCoordinateOfMinimumValue(gapDistance);
            if(routeService.getDistance(coordinate.getRow(),coordinate.getCol()) < 0){
                routeService.swap(route, coordinate.getRow(), coordinate.getCol());
                DistanceUpdate.updateSwapDistance(routeService, route, gapDistance);
            }else {
                break;
            }
            localIter++;
        }
        return route;
    }

    public static Route insertNeighbor(RouteService routeService, Route curRoute, int maxInsertNum){
        Route route = (Route) SerializationUtils.clone(curRoute);
        shaking(routeService, route, NeighborHoodType.INSERT);

        double[][] gapDistance = new double[route.length()][route.length()];
        for (int i = 0; i < route.length(); i++)
            for (int j = 0; j < route.length(); j++)
                gapDistance[i][j] = routeService.calculateInsertGapDistance(route, i, j);
        int localIter = 0;
        while(localIter<maxInsertNum){
            Coordinate coordinate = ArrayUtils.findCoordinateOfMinimumValueAsymmetry(gapDistance);
            if(routeService.getDistance(coordinate.getRow(),coordinate.getCol()) < 0) {
                routeService.insert(route, coordinate.getRow(), coordinate.getCol());
                DistanceUpdate.updateInsertDistance(routeService, route, gapDistance);
            }else{
                break;
            }
            localIter++;
        }
        return route;
    }

    public static Route reversionNeighbor(RouteService routeService, Route curRoute, int maxReversionNum){

        Route route = (Route) SerializationUtils.clone(curRoute);
        shaking(routeService, route, NeighborHoodType.REVERSION);

        double[][] gapDistance = new double[route.length()][route.length()];
        for (int i = 0; i < route.length(); i++)
            for (int j = i; j < route.length(); j++)
                gapDistance[i][j] = gapDistance[j][i] = routeService.calculateReversionGapDistance(route, i, j);
        int localIter = 0;
        while(localIter<maxReversionNum){
            Coordinate coordinate = ArrayUtils.findCoordinateOfMinimumValue(gapDistance);
            if(routeService.getDistance(coordinate.getRow(),coordinate.getCol()) < 0) {
                routeService.reversion(route, coordinate.getRow(), coordinate.getCol());
                DistanceUpdate.updateReversionDistance(routeService, route, gapDistance);
            }else{
                break;
            }
            localIter++;
        }
        return route;
    }



}
