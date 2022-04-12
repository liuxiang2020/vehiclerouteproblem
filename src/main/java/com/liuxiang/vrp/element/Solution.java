package com.liuxiang.vrp.element;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Solution {
    private double[][] iterDistanceRecord;
    private Route bestRoute;
}
