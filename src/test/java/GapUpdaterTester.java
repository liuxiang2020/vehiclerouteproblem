import com.liuxiang.vrp.TspExample;
import com.liuxiang.vrp.TspModel;
import com.liuxiang.vrp.algorithm.InitSolutionGenerator;
import com.liuxiang.vrp.algorithm.evaluate.DistanceUpdate;
import com.liuxiang.vrp.element.Route;
import com.liuxiang.vrp.service.RouteService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SerializationUtils;

@Slf4j
public class GapUpdaterTester {

    public static void compareGap(double[][] operateGapDistance, double[][] standardGapDistance, String operatorName){
        for (int i = 0; i < operateGapDistance.length; i++) {
            for (int j = 0; j < operateGapDistance.length; j++) {
                if(i==j){
                    continue;
                }
                if(Math.abs(operateGapDistance[i][j]-standardGapDistance[i][j]) > 0.1){
                    System.out.printf("i=%d, j=%d, operateGapDistance=%.2f, standardGapDistance=%.2f\n",
                            i, j, operateGapDistance[i][j],standardGapDistance[i][j]);
                }
            }
        }
    }

    public void testSwapGapUpdater(Route route){
        System.out.println("\n\ncheck updateSwapDistance operate");
        double[][] swapGapDistance = new double[route.length()][route.length()];
        swapGapDistance = DistanceUpdate.updateSwapDistance(route, swapGapDistance);
        double[][] standardGapDistance = new double[route.length()][route.length()];
        for (int i = 0; i < route.length(); i++) {
            for (int j = 0; j < route.length(); j++) {
                Route tempRoute = RouteService.copyRoute(route);
                RouteService.swap(tempRoute, i, j);
                standardGapDistance[i][j] = tempRoute.getDistance()-route.getDistance();
                if(standardGapDistance[i][j]<0) {
//                    System.out.printf("%s,standardGapDistance[i][j]=%.2f\n", tempRoute.toString(), standardGapDistance[i][j]);
                }
            }
        }
        compareGap(swapGapDistance, standardGapDistance, "SWAP");
    }

    public void testReverseGapUpdater(Route route){
        System.out.println("\n\ncheck updateReversionDistance operate");
        double[][] reverseGapDistance = new double[route.length()][route.length()];
        DistanceUpdate.updateReversionDistance(route, reverseGapDistance);
        double[][] standardGapDistance = new double[route.length()][route.length()];
        for (int i = 0; i < route.length(); i++) {
            for (int j = 0; j < route.length(); j++) {
                if((i==0 && j == route.length()-1) || (i == route.length()-1 && j==0)){
                    standardGapDistance[i][j] = Double.MAX_VALUE;
                    continue;
                }
                Route tempRoute = RouteService.copyRoute(route);
                RouteService.reverse(tempRoute, i, j);
                standardGapDistance[i][j] = tempRoute.getDistance()-route.getDistance();
            }
        }
        compareGap(reverseGapDistance, standardGapDistance, "REVERSE");
    }

    public void testInsertGapUpdater(Route route){
        System.out.println("\n\ncheck updateInsertDistance operate");
        double[][] insertGapDistance = new double[route.length()][route.length()];
        DistanceUpdate.updateInsertDistance(route, insertGapDistance);
        double[][] standardGapDistance = new double[route.length()][route.length()];
        for (int i = 0; i < route.length(); i++) {
            for (int j = 0; j < route.length(); j++) {
                if( i-j == 1 || (i==0 && j==route.length()-1)){
                    standardGapDistance[i][j] = Double.MAX_VALUE;
                    continue;
                }
                Route tempRoute = RouteService.copyRoute(route);
                RouteService.insert(tempRoute, i, j);
                standardGapDistance[i][j] = tempRoute.getDistance()-route.getDistance();
            }
        }
        compareGap(insertGapDistance, standardGapDistance, "INSERT");
    }

    public void swapCheck(Route route, int indexA, int indexB){
        double operateGap= RouteService.calculateSwapGapDistance(route, indexA, indexB);
        Route tempRoute = RouteService.copyRoute(route);
        RouteService.swap(tempRoute, indexA, indexB);
        double standardGapDistance = tempRoute.getDistance() - route.getDistance();
        double gap = Math.abs(operateGap - standardGapDistance);
        if(gap>0.5)
            System.out.printf("indexA=%d, indexB=%d, operateGapDistance=%.2f, standardGapDistance=%.2f\n",
                indexA, indexB, operateGap,standardGapDistance);
        else
            System.out.println("无错误");
    }

    public void reverseCheck(Route route, int indexA, int indexB){
        double operateGap= RouteService.calculateReversionGapDistance(route, indexA, indexB);
        Route tempRoute = RouteService.copyRoute(route);
        RouteService.reverse(tempRoute, indexA, indexB);
        double standardGapDistance = tempRoute.getDistance() - route.getDistance();
        double gap = Math.abs(operateGap - standardGapDistance);
        if(gap>0.5)
            System.out.printf("indexA=%d, indexB=%d, operateGapDistance=%.2f, standardGapDistance=%.2f\n",
                    indexA, indexB, operateGap,standardGapDistance);
        else
            System.out.println("无错误");
    }

    public void munu(double[][] matrix){
        int[] path1 = {0, 1, 2, 3, 4, 5, 6, 7, 0};
        int[] path2 = {3, 2, 1, 0, 4, 5, 6, 7, 3};
        double len1 = 0.0, len2 = 0.0;
        for (int i = 0; i < path1.length-1; i++) {
            len1 += matrix[path1[i]][path1[i+1]];
        }
        for (int i = 0; i < path2.length-1; i++) {
            len2 += matrix[path2[i]][path2[i+1]];
        }
        double gap = len2-len1;
        System.out.printf("len1=%.2f, len2=%.2f, gap=%.2f", len1, len2, gap);
    }

    public static void main(String[] args) {
        TspModel model = TspExample.generateExampleFromFile("src/main/java/com/liuxiang/vrp/data/book_input.txt", 7);
        Route route = new InitSolutionGenerator().generateRouteSeq(model);
        GapUpdaterTester tester = new GapUpdaterTester();
//        tester.munu(model.getDistanceMatrix());
//        tester.swapCheck(route, 7, 0);
//        tester.reverseCheck(route, 3, 7);
        tester.testSwapGapUpdater(route);
        tester.testReverseGapUpdater(route);
        tester.testInsertGapUpdater(route);
    }
}
