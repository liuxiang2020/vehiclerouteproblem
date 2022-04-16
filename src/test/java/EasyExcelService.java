import com.alibaba.excel.EasyExcel;

import java.io.FileNotFoundException;
import java.util.List;

public class EasyExcelService {
    public static void writeRecord(List<Record> recordList) throws FileNotFoundException {
// 写法1
        String fileName = "src/main/java/com/liuxiang/vrp/data/java_record" + ".xlsx";
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        // 如果这里想使用03 则 传入excelType参数即可
        EasyExcel.write(fileName, Record.class).sheet("record").doWrite(recordList);

        System.out.println("写入完成！");
    }

    public static List<Record> readRecord(String filePath){
        RecordReadListener readListener = new RecordReadListener();
        EasyExcel.read(filePath, Record.class, readListener).sheet().doRead();
        System.out.println("===读取完成===");
        return readListener.getRecordList();
    }

    public static void main(String[] args) {
        String fileName = "src/main/java/com/liuxiang/vrp/data/java_record" + ".xlsx";
        List<Record> recordList = EasyExcelService.readRecord(fileName);
        System.out.printf("读取记录数为%d\n", recordList.size());
    }
}
