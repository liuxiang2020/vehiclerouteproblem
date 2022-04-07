package com.liuxiang.vrp;

import com.liuxiang.vrp.service.ReadService;

public class TspExample {

    public static TspModel generateSimpleExample(){
        int n = 4;
//        double[][] distance = {
//                  {0.0, 5.39, 11.18, 7.07}
//                , {5.39, 0.0, 5.83, 9.85}
//                , {11.18, 5.83, 0.0, 15.0}
//                , {7.07, 9.85, 15.0, 0.0}
//        };
        double[][] distance = {
                {0.0, 5.0, 11.0, 7.0}
                , {5.0, 0.0, 5.0, 9.0}
                , {11.0, 5.0, 0.0, 15.0}
                , {7.0, 9.0, 15.0, 0.0}
        };

        return TspModel.builder().citySize(n).distanceMatrix(distance).build();
    }

    public static TspModel generateExampleFromFile(String filename, int citySize){
        return ReadService.readFile(filename, citySize);
    }

    public static void main(String[] args) {
        TspModel model = TspExample.generateSimpleExample();

    }

}
