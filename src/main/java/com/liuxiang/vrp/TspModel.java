package com.liuxiang.vrp;

import com.liuxiang.vrp.element.City;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class TspModel {

    private int citySize;

    private double[][] distanceMatrix;

    private List<City> cityList;

}
