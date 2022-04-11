package com.liuxiang.vrp.algorithm;

import com.liuxiang.vrp.TspExample;
import com.liuxiang.vrp.TspModel;
import com.liuxiang.vrp.algorithm.operation.OperationService;
import com.liuxiang.vrp.element.Route;
import com.liuxiang.vrp.enums.NeighborHoodType;
import com.liuxiang.vrp.service.RouteService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SerializationUtils;

@Slf4j
public class VariableNeighborhoodSearchAlgorithm {

    private TspModel model;

    private RouteService routeService;

    private int maxIteration = 50;

    private int maxSwapNum = 50;

    private int maxInsertNum = 50;

    private int maxReversionNum = 50;

    private int neighborhoodSize = 3;

    private Route bestRoute;

    private double minDistance;

    public VariableNeighborhoodSearchAlgorithm(TspModel model, int maxIteration) {
        this.model = model;
        this.maxIteration = maxIteration;
        this.routeService = new RouteService(model.getDistanceMatrix());
        //参数初始化

    }


    public void test(){
        // 生成初始解
        Route initRoute = new InitSolutionGenerator().generateRouteRandom(model);
        // 迭代
        routeService.swap(initRoute, 0, 8);
//        routeService.swap(initRoute, 2, 2);
//        routeService.reversion(initRoute, 0, 7);
//        routeService.reversion(initRoute, 0, 6);
//        routeService.reversion(initRoute, 0, 7);
//        routeService.reversion(initRoute, 3, 7);
//        routeService.swap(initRoute, 2, 3);
//        routeService.swap(initRoute, 2, 4);
//        routeService.swap(initRoute, 2, 4);
//        routeService.insert(initRoute, 4,0);
//        routeService.insert(initRoute, 1,4);
        routeService.evaluate(initRoute);
    }

    public void vnsForTSP(){

        // 生成初始解
        double[] iterDistanceRecord = new double[maxIteration+1];
        Route route = new InitSolutionGenerator().generateRouteRandom(model);
        bestRoute = route;
        // 迭代
        int iter = 0;
        while(iter < maxIteration){
            iterDistanceRecord[iter] = bestRoute.getDistance();
            NeighborHoodType neighborHoodType = NeighborHoodType.SWAP;
            while(neighborHoodType!=null){
                switch (neighborHoodType){
                    case SWAP:
                        Route swapRoute = OperationService.swapNeighbor(routeService, route, maxSwapNum);
                        if(swapRoute.getDistance()<bestRoute.getDistance()){
                            bestRoute= route = swapRoute;
                            neighborHoodType = null;
                        }
                        break;
                    case INSERT:
                        Route insertRoute = OperationService.insertNeighbor(routeService, route, maxSwapNum);
                        if(insertRoute.getDistance()<bestRoute.getDistance()){
                            bestRoute = route = insertRoute;
                            neighborHoodType = null;
                        }
                        break;
                    case REVERSION:
                        Route reversionRoute = OperationService.reversionNeighbor(routeService, route, maxSwapNum);
                        if(reversionRoute.getDistance()<bestRoute.getDistance()){
                            bestRoute = route = reversionRoute;
                            neighborHoodType = null;
                        }
                        break;
                }
                neighborHoodType = NeighborHoodType.getNextNeighborhoodType(neighborHoodType);
            }
            iter+=1;
        }
        iterDistanceRecord[iter] = bestRoute.getDistance();
        // 输出结果
        log.info("迭代{}代数后，最优解为，{}", iter, route.toString());
        // 绘图
    }

    public static void main(String[] args) {
        TspModel model = TspExample.generateSimpleExample();
        model = TspExample.generateExampleFromFile("src/main/java/com/liuxiang/vrp/data/C110_1.TXT", 7);
        VariableNeighborhoodSearchAlgorithm algorithm = new VariableNeighborhoodSearchAlgorithm(model, 20);
//        algorithm.test();
        algorithm.vnsForTSP();
    }

}
