package com.liuxiang.vrp.enums;
import java.util.*;

public enum NeighborHoodType {
    SWAP("Swap", 1, "swap operation"),
    REVERSION("Reversion", 2, "reversion operation"),
    INSERT("Insert", 3, "insert operation");

    private String name;
    private int index;
    private String desc;

    NeighborHoodType(String name, int index, String desc) {
        this.name = name;
        this.index = index;
        this.desc = desc;
    }

    private static final Map<Integer, NeighborHoodType> neighborHoodIndexMap = new HashMap<>();
    private static final Map<String, NeighborHoodType> neighborHoodNameMap = new HashMap<>();
    private static final Map<NeighborHoodType, NeighborHoodType> nextNeighbor = new HashMap<>();

    static {
        for(NeighborHoodType type : values())
            neighborHoodIndexMap.put(type.index, type);
        for(NeighborHoodType type : values())
            neighborHoodNameMap.put(type.name, type);

        NeighborHoodType lastNeighbor = null;
        for(NeighborHoodType type : values()){
            nextNeighbor.put(lastNeighbor, type);
            lastNeighbor = type;
        }
        nextNeighbor.put(lastNeighbor, null);
    }


    public static NeighborHoodType getNeighborhoodType(int index){
        return neighborHoodIndexMap.get(index);
    }

    public static NeighborHoodType getNeighborhoodType(String name){
        return neighborHoodNameMap.get(name);
    }

    public static NeighborHoodType getNextNeighborhoodType(NeighborHoodType neighborHoodType){
        return nextNeighbor.get(neighborHoodType);
    }
}
