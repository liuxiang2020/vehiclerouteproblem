package com.liuxiang.vrp.enums;
import java.util.*;

public enum NeighborHoodEnum {
    SWAP("Swap", 1, ""),
    INSERT("Insert", 2, ""),
    REVERSION("Reversion", 3, "");

    private String name;
    private int index;
    private String explan;

    NeighborHoodEnum(String name, int index, String explan) {
        this.name = name;
        this.index = index;
        this.explan = explan;
    }

    private static Map<Integer, NeighborHoodEnum> neighborHoodEnumMap;
    private static List<NeighborHoodEnum> neighborHoodEnumList = Arrays.asList(SWAP, INSERT, REVERSION);

    private static final void generateNeighborHoodEnumMap(){
        neighborHoodEnumMap = new HashMap<>();
        neighborHoodEnumMap.put(1, SWAP);
        neighborHoodEnumMap.put(2, INSERT);
        neighborHoodEnumMap.put(3, REVERSION);
    }

    public static NeighborHoodEnum getByIndex(int index){
        return neighborHoodEnumMap.get(index);
    }

    public static NeighborHoodEnum getByName(String name){
        for(NeighborHoodEnum neighborHoodEnum: neighborHoodEnumList){
            if(neighborHoodEnum.name.equals(name))
                return neighborHoodEnum;
        }
        throw new IllegalArgumentException("不存在该操作");
    }
}
