import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.extern.slf4j.Slf4j;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class RecordReadListener extends AnalysisEventListener<Record> {

    private List<Record> recordList = new ArrayList<>();

    // 每读一样，会调用该invoke方法一次
    @Override
    public void invoke(Record data, AnalysisContext context) {
        recordList.add(data);
        log.info("解析到一条数据：" + data);
    }

    // 全部读完之后，会调用该方法
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        System.out.printf("全部解析完成, 共解析%d条数据", recordList.size());
    }
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        log.info("表头：" + headMap);
    }

    /**
     * 返回读取到的记录集合
     * @return
     */
    public List<Record> getRecordList() {
        return recordList;
    }

}
