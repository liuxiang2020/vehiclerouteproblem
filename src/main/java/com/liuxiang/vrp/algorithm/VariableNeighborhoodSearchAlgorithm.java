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
import org.apache.commons.lang3.SerializationUtils;

@Slf4j
public class VariableNeighborhoodSearchAlgorithm {

    private TspModel model;

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
        //参数初始化
    }

    public void test(Route initRoute){
        RouteService.swap(initRoute, 0, 8);
//        RouteService.swap(initRoute, 0, 7);
//        RouteService.reversion(initRoute, 0, 7);
//        RouteService.reversion(initRoute, 0, 6);
//        RouteService.reversion(initRoute, 0, 7);
//        RouteService.swap(initRoute, 2, 4);
//        RouteService.insert(initRoute, 4,0);
//        RouteService.insert(initRoute, 1,4);
        RouteService.evaluate(initRoute);
    }

    public Solution vnsForTSP(){
        // 生成初始解
        iterDistanceRecord = new double[maxIteration+1][2];
//        Route route = new InitSolutionGenerator().generateRouteRandom(model);
        Route route = new InitSolutionGenerator().generateRouteGreedy(model);
//        Route route = new InitSolutionGenerator().generateRouteWrite(model);
        double bestRouteLength = route.getDistance();
        int[] bestPath = RouteService.gertPath(route);
        // 迭代
        int iter = 0;
        Route neighborRoute = null;
        while(iter < maxIteration){
            iterDistanceRecord[iter][0] = iter;
            iterDistanceRecord[iter][1] = bestRouteLength;
            NeighborHoodType neighborHoodType = NeighborHoodType.SWAP;
            while(neighborHoodType!=null){
                neighborRoute = null;
                switch (neighborHoodType){
                    case SWAP:
                        neighborRoute = OperationService.swapNeighbor(route, maxSwapNum);
                        break;
                    case REVERSION:
                        neighborRoute = OperationService.reversionNeighbor(route, maxSwapNum);
                        break;
                    case INSERT:
                        neighborRoute = OperationService.insertNeighbor(route, maxSwapNum);
                        break;
                }
                if(neighborRoute.getDistance() < bestRouteLength){
                    bestRouteLength = neighborRoute.getDistance();
                    bestPath = RouteService.gertPath(neighborRoute);
                    route = neighborRoute;
                    neighborHoodType = null;
                }
                neighborHoodType = NeighborHoodType.getNextNeighborhoodType(neighborHoodType);
            }
            log.info("第{}次迭代，当前最好解的总行驶距离为{}", iter, Math.round(bestRouteLength));
            iter+=1;
        }
        iterDistanceRecord[iter][0] = iter;
        iterDistanceRecord[iter][1] = bestRouteLength;
        // 输出结果
        bestRoute = new Route(bestPath, route.getMatrix());
        log.info("迭代{}代数后，最优解为，{}", iter-1, bestRoute.toString());
        // 绘图
        return Solution.builder().bestRoute(bestRoute).iterDistanceRecord(iterDistanceRecord).build();
    }

    public static void main(String[] args) {
//        TspModel model = TspExample.generateSimpleExample();
        TspModel model = TspExample.generateExampleFromFile("src/main/java/com/liuxiang/vrp/data/C110_1.TXT", 200);
//        TspModel model = TspExample.generateExampleFromFile("src/main/java/com/liuxiang/vrp/data/book_input.txt", 51);
        VariableNeighborhoodSearchAlgorithm algorithm = new VariableNeighborhoodSearchAlgorithm(model, 50);
        Solution solution = algorithm.vnsForTSP();
        DrawService.drawDoubleFigure(solution.getIterDistanceRecord(), solution.getBestRoute(), model);
    }
}
