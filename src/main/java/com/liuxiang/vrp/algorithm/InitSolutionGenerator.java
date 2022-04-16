package com.liuxiang.vrp.algorithm;

import com.liuxiang.vrp.TspModel;
import com.liuxiang.vrp.element.Coordinate;
import com.liuxiang.vrp.element.Route;
import com.liuxiang.vrp.service.ArrayUtils;
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
        for (int i = 1; i < path.size(); i++){
            route.add(path.get(i));
        }
        log.info("初始解为{}", route.toString());
        return route;
    }

    public Route generateRouteGreedy(TspModel model){
        List<Integer> path = new ArrayList<>();
        boolean[] visited = new boolean[model.getCitySize()];
        double[][] distanceMatrix = model.getDistanceMatrix();
        Coordinate coordinate = ArrayUtils.findCoordinateOfMinimumValue(distanceMatrix);
        int curIndex = coordinate.getRow();
        visited[curIndex] = true;
        path.add(curIndex);
        while (path.size()<model.getCitySize()){
            double distance = Double.MAX_VALUE;
            int nextIndex = -1;
            for (int i = 0; i < model.getCitySize(); i++) {
                if((!visited[i]) && (distanceMatrix[curIndex][i] < distance)){
                    distance = distanceMatrix[curIndex][i];
                    nextIndex = i;
                }
            }
            visited[nextIndex] = true;
            path.add(nextIndex);
            curIndex = nextIndex;
        }
        Route route = new Route(path.get(0), model.getDistanceMatrix());
        for (int i = 1; i < path.size(); i++){
            route.add(path.get(i));
        }

        log.info("初始解为{}", route.toString());
        return route;
    }

    public Route generateRouteWrite(TspModel model){
        int[] path = new int[]{36, 35, 34, 39, 40, 38, 37, 48, 24,  5, 15,  6,  4, 25, 46, 44, 16, 50, 20, 23, 31, 18, 22,  1, 49, 32, 45, 19, 41,
                8, 10,  9, 43, 33, 51, 12, 28, 27, 26, 47, 13, 14, 52, 11, 29, 30, 21, 17,  3, 42,  7,  2};
        for (int i = 0; i < path.length; i++){
            path[i]-=1;
        }
        Route route = new Route(path[0], model.getDistanceMatrix());
        for (int i = 1; i < path.length; i++){
            route.add(path[i]);
        }
        return route;
    }

    public Route generateRouteSeq(TspModel model){
        int[] path = new int[model.getCitySize()];
        for (int i = 0; i < path.length; i++){
            path[i] = i;
        }
        Route route = new Route(path[0], model.getDistanceMatrix());
        for (int i = 1; i < path.length; i++){
            route.add(path[i]);
        }
        return route;
    }

}
