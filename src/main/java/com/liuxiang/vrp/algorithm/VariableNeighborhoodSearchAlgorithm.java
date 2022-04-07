package com.liuxiang.vrp.algorithm;

import com.liuxiang.vrp.TspExample;
import com.liuxiang.vrp.TspModel;
import com.liuxiang.vrp.element.Route;
import com.liuxiang.vrp.service.RouteService;

public class VariableNeighborhoodSearchAlgorithm {

    private TspModel model;

    private RouteService routeService;

    private int maxIteration;

    public VariableNeighborhoodSearchAlgorithm(TspModel model, int maxIteration) {
        this.model = model;
        this.maxIteration = maxIteration;
        routeService = new RouteService(model.getDistanceMatrix());

    }



    public void run(){
        Route initRoute = new InitSolutionGenerator().generateRouteRandom(model);
//        routeService.exchange(initRoute, 0, 3);
//        routeService.exchange(initRoute, 2, 2);
//        routeService.exchange(initRoute, 2, 5);
//        routeService.exchange(initRoute, 2, 3);
//        routeService.exchange(initRoute, 2, 4);
//        routeService.swap(initRoute, 2, 4);
//        routeService.insert(initRoute, 1,2);
        routeService.insert(initRoute, 1,4);
        routeService.evaluate(initRoute);

    }

    public static void main(String[] args) {
        TspModel model = TspExample.generateSimpleExample();
        model = TspExample.generateExampleFromFile("src/main/java/com/liuxiang/vrp/data/C110_1.TXT", 7);
        VariableNeighborhoodSearchAlgorithm algorithm = new VariableNeighborhoodSearchAlgorithm(model, 100);
        algorithm.run();
    }

}
