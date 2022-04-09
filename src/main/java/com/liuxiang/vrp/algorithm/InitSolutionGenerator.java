package com.liuxiang.vrp.algorithm;

import com.liuxiang.vrp.TspModel;
import com.liuxiang.vrp.element.Route;
import lombok.extern.slf4j.Slf4j;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
public class InitSolutionGenerator {

    public Route generateRouteRandom(TspModel model){
        List<Integer> path = new ArrayList<>();
        for (int i = 0; i < model.getCitySize(); i++)
            path.add(i);
        Collections.shuffle(path);
        Route route = new Route(path.get(0), model.getDistanceMatrix());
        System.out.println("添加元素的顺序为"+path.toString());
        for (int i = 1; i < path.size(); i++){
            route.add(path.get(i));
        }
        log.info("初始解为{}", route.toString());
        return route;
    }

}
