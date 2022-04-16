package com.liuxiang.vrp.algorithm;

import com.liuxiang.vrp.TspExample;
import com.liuxiang.vrp.TspModel;
import com.liuxiang.vrp.algorithm.operation.OperationService;
import com.liuxiang.vrp.element.Route;
import com.liuxiang.vrp.element.Solution;
import com.liuxiang.vrp.enums.NeighborHoodType;
import com.liuxiang.vrp.service.DrawService;
import com.liuxiang.vrp.service.RouteService;
import lombok.extern.slf4j.Slf4j;

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

    private double[][] iterDistanceRecord;

    public VariableNeighborhoodSearchAlgorithm(TspModel model, int maxIteration) {
        this.model = model;
        this.maxIteration = maxIteration;
        this.routeService = new RouteService(model.getDistanceMatrix());
        //参数初始化
    }


    public void test(Route initRoute){
        routeService.swap(initRoute, 0, 8);
//        routeService.swap(initRoute, 0, 7);
//        routeService.reversion(initRoute, 0, 7);
//        routeService.reversion(initRoute, 0, 6);
//        routeService.reversion(initRoute, 0, 7);
//        routeService.swap(initRoute, 2, 4);
//        routeService.insert(initRoute, 4,0);
//        routeService.insert(initRoute, 1,4);
        routeService.evaluate(initRoute);
    }

    public Solution vnsForTSP(){
        // 生成初始解
        iterDistanceRecord = new double[maxIteration+1][2];
//        Route route = new InitSolutionGenerator().generateRouteRandom(model);
//        Route route = new InitSolutionGenerator().generateRouteGreedy(model);
        Route route = new InitSolutionGenerator().generateRouteWrite(model);
        bestRoute = route;
        // 迭代
        int iter = 0;
        while(iter < maxIteration){
            iterDistanceRecord[iter][0] = iter;
            iterDistanceRecord[iter][1] = bestRoute.getDistance();
            NeighborHoodType neighborHoodType = NeighborHoodType.SWAP;
            while(neighborHoodType!=null){
                Route neighborRoute = null;
                switch (neighborHoodType){
                    case SWAP:
                        neighborRoute = OperationService.swapNeighbor(routeService, route, maxSwapNum);
                        break;
                    case REVERSION:
                        neighborRoute = OperationService.reversionNeighbor(routeService, route, maxSwapNum);
                        break;
                    case INSERT:
                        neighborRoute = OperationService.insertNeighbor(routeService, route, maxSwapNum);
                        break;
                }
                if(neighborRoute.getDistance() < bestRoute.getDistance()){
                    bestRoute = route = neighborRoute;
                    neighborHoodType = null;
                }
                neighborHoodType = NeighborHoodType.getNextNeighborhoodType(neighborHoodType);
            }
            iter+=1;
        }
        iterDistanceRecord[iter][0] = iter;
        iterDistanceRecord[iter][1] = bestRoute.getDistance();
        // 输出结果
        log.info("迭代{}代数后，最优解为，{}", iter, route.toString());
        // 绘图
        return Solution.builder().bestRoute(bestRoute).iterDistanceRecord(iterDistanceRecord).build();
    }

    public static void main(String[] args) {
//        TspModel model = TspExample.generateSimpleExample();
//        model = TspExample.generateExampleFromFile("src/main/java/com/liuxiang/vrp/data/C110_1.TXT", 50);
        TspModel model = TspExample.generateExampleFromFile("src/main/java/com/liuxiang/vrp/data/book_input.txt", 51);
        VariableNeighborhoodSearchAlgorithm algorithm = new VariableNeighborhoodSearchAlgorithm(model, 50);
        Solution solution = algorithm.vnsForTSP();
        DrawService.drawDoubleFigure(solution.getIterDistanceRecord(), solution.getBestRoute(), model);
    }
}
