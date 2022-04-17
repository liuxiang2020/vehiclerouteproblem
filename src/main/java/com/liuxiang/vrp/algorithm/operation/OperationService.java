package com.liuxiang.vrp.algorithm.operation;

import com.liuxiang.vrp.algorithm.evaluate.DistanceUpdate;
import com.liuxiang.vrp.element.Coordinate;
import com.liuxiang.vrp.element.Route;
import com.liuxiang.vrp.enums.NeighborHoodType;
import com.liuxiang.vrp.service.ArrayUtils;
import com.liuxiang.vrp.service.RouteService;
import com.liuxiang.vrp.utils.MyNumber;
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
     * @param route
     * @param neighborHoodType
     * @return
     */
    public static Route shaking(Route route, NeighborHoodType neighborHoodType){

        int[] indexs = ArrayUtils.randomTwo(0, route.length());
        switch (neighborHoodType){
            case SWAP:
                RouteService.swap(route, indexs[0], indexs[1]);
                break;
            case INSERT:
                RouteService.insert(route, indexs[0], indexs[1]);
                break;
            case REVERSION:
                RouteService.reverse(route, indexs[0], indexs[1]);
        }
        return route;
    }

    public static Route swapNeighbor(Route curRoute, int maxSwapNum){
        shaking(curRoute, NeighborHoodType.SWAP);
        Route route = RouteService.copyRoute(curRoute);
        double[][] gapDistance = new double[route.length()][route.length()];
        for (int i = 0; i < route.length(); i++)
            for (int j = i; j < route.length(); j++)
                gapDistance[i][j] = gapDistance[j][i] = RouteService.calculateSwapGapDistance(route, i, j);

        int localIter = 0;
        while(localIter < maxSwapNum){
            Coordinate coordinate = ArrayUtils.findCoordinateOfMinimumValue(gapDistance);
            if(gapDistance[coordinate.getRow()][coordinate.getCol()] < - MyNumber.MIN_VALUE){
                  RouteService.swap(route, coordinate.getRow(), coordinate.getCol());
                DistanceUpdate.updateSwapDistance(route, gapDistance);
            }else {
                break;
            }
            localIter++;
        }
        return route;
    }

    public static Route insertNeighbor(Route curRoute, int maxInsertNum){
        shaking(curRoute, NeighborHoodType.INSERT);
        Route route = RouteService.copyRoute(curRoute);

        double[][] gapDistance = new double[route.length()][route.length()];
        for (int i = 0; i < route.length(); i++)
            for (int j = 0; j < route.length(); j++)
                gapDistance[i][j] = RouteService.calculateInsertGapDistance(route, i, j);
        int localIter = 0;
        while(localIter<maxInsertNum){
            Coordinate coordinate = ArrayUtils.findCoordinateOfMinimumValueAsymmetry(gapDistance);
            if(gapDistance[coordinate.getRow()][coordinate.getCol()] < - MyNumber.MIN_VALUE) {
                RouteService.insert(route, coordinate.getRow(), coordinate.getCol());
                DistanceUpdate.updateInsertDistance(route, gapDistance);
            }else{
                break;
            }
            localIter++;
        }
        return route;
    }

    public static Route reversionNeighbor(Route curRoute, int maxReversionNum){
        shaking(curRoute, NeighborHoodType.REVERSION);
        Route route = RouteService.copyRoute(curRoute);

        double[][] gapDistance = new double[route.length()][route.length()];
        for (int i = 0; i < route.length(); i++)
            for (int j = i; j < route.length(); j++)
                gapDistance[i][j] = gapDistance[j][i] = RouteService.calculateReversionGapDistance(route, i, j);
        int localIter = 0;
        while(localIter<maxReversionNum){
            Coordinate coordinate = ArrayUtils.findCoordinateOfMinimumValue(gapDistance);
            if(gapDistance[coordinate.getRow()][coordinate.getCol()] < - MyNumber.MIN_VALUE) {
                RouteService.reverse(route, coordinate.getRow(), coordinate.getCol());
                DistanceUpdate.updateReversionDistance(route, gapDistance);
            }else{
                break;
            }
            localIter++;
        }
        return route;
    }

}
