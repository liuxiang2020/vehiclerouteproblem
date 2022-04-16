import com.alibaba.excel.EasyExcel;
import com.liuxiang.vrp.TspExample;
import com.liuxiang.vrp.TspModel;
import com.liuxiang.vrp.algorithm.InitSolutionGenerator;
import com.liuxiang.vrp.element.Route;
import com.liuxiang.vrp.service.RouteService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SerializationUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class RouteServiceTester {
    List<Record> recordList = new ArrayList<>();

    public void testSwap(Route route, RouteService routeService){
        for (int i = 0; i < route.length(); i++) {
            for (int j = 0; j < route.length(); j++) {
                Route tempRoute = (Route) SerializationUtils.clone(route);
                routeService.swap(tempRoute, i, j);
                System.out.printf("交换第%d个节点和第%d个节点后，路径起点为%d，%s\n", i, j, route.getHead().getIndex(), tempRoute.toString());
                recordList.add(Record.builder()
                        .neighborIndex(1)
                        .row(i)
                        .col(j)
                        .distance(tempRoute.getDistance())
                        .build());
            }
        }
    }

    public void testRevere(Route route, RouteService routeService){
        for (int i = 0; i < route.length(); i++) {
            for (int j = 0; j < route.length(); j++) {
                Route tempRoute = (Route) SerializationUtils.clone(route);
                routeService.reverse(tempRoute, i, j);
                System.out.printf("翻转第%d个节点和第%d个节点后，路径起点为%d，%s\n", i, j, route.getHead().getIndex(), tempRoute.toString());
                recordList.add(Record.builder()
                        .neighborIndex(2)
                        .row(i)
                        .col(j)
                        .distance(tempRoute.getDistance())
                        .build());
            }
        }
    }

    public void testInsert(Route route, RouteService routeService){
        for (int i = 0; i < route.length(); i++) {
            for (int j = 0; j < route.length(); j++) {
                Route tempRoute = (Route) SerializationUtils.clone(route);
                routeService.insert(tempRoute, i, j);
                System.out.printf("将第%d个节点插入到第%d个节点后，路径起点为%d，%s\n", i, j, route.getHead().getIndex(), tempRoute.toString());
                recordList.add(Record.builder()
                        .neighborIndex(3)
                        .row(i)
                        .col(j)
                        .distance(tempRoute.getDistance())
                        .build());
            }
        }
    }


    public static void main(String[] args) {
        TspModel model = TspExample.generateExampleFromFile("src/main/java/com/liuxiang/vrp/data/book_input.txt", 51);
        Route route = new InitSolutionGenerator().generateRouteSeq(model);
        RouteService routeService = new RouteService(model.getDistanceMatrix());

        RouteServiceTester tester = new RouteServiceTester();
        tester.testSwap(route, routeService);
        tester.testRevere(route, routeService);
        tester.testInsert(route, routeService);

        String fileName = "src/main/java/com/liuxiang/vrp/data/java_record" + ".xlsx";
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        // 如果这里想使用03 则 传入excelType参数即可
        EasyExcel.write(fileName, Record.class).sheet("record").doWrite(tester.recordList);
        System.out.println("写入完成！");
    }
}
