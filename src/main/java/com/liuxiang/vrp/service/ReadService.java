package com.liuxiang.vrp.service;

import com.liuxiang.vrp.TspModel;
import com.liuxiang.vrp.element.City;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

@Slf4j
public class ReadService {
    public static TspModel readFile(String filename, int citySize) {
        ArrayList<City> cities = new ArrayList<>();
        try {
            BufferedReader in = new BufferedReader(new FileReader(filename));
            String line = null;
            while((line = in.readLine()) != null && citySize>=0) {
                String[] blocks = line.trim().split("\\s+");
                if (blocks.length == 3) {
                    City c = new City();
                    c.index = Integer.parseInt(blocks[0]);
                    c.x = Double.parseDouble(blocks[1]);
                    c.y = Double.parseDouble(blocks[2]);
                    log.info("City{} {} {}", c.index, c.x, c.y);
                    cities.add(c);
                    citySize -- ;
                }
            }
        } catch (IOException ioe) {
            log.info(ioe.getMessage());
        }
        double[][] distance = new double[cities.size()][cities.size()];
        for (int i = 0; i < cities.size(); i++) {
            City ci = cities.get(i);
            for (int j = i; j < cities.size(); j++) {
                City cj = cities.get(j);
                distance[i][j] = distance[j][i] = Math.sqrt(Math.pow((ci.x - cj.x),2) + Math.pow((ci.y - cj.y),2));
            }
        }
        return TspModel.builder()
                .citySize(cities.size())
                .distanceMatrix(distance)
                .cityList(cities)
                .build();
    }

    public static void main(String[] args) {
        ReadService.readFile("src/main/java/com/liuxiang/vrp/data/C110_1.TXT", 5);
    }
}
