import com.alibaba.excel.EasyExcel;

import java.util.List;

public class OperatorComparator {


    public static void main(String[] args) {
        List<Record> javaRecordList = EasyExcelService.readRecord( "src/main/java/com/liuxiang/vrp/data/java_record.xlsx");
        javaRecordList.remove(0);
        List<Record> matlabRecordList = EasyExcelService.readRecord( "C:/Users/刘祥/Downloads/matlab_record.xlsx");
        for (int i = 0; i < matlabRecordList.size(); i++) {
            Record javaRecord = javaRecordList.get(i);
            Record matlabRecord = matlabRecordList.get(i);
            if(javaRecord.getNeighborIndex()==matlabRecord.getNeighborIndex() &&
                    javaRecord.getRow()==matlabRecord.getRow() &&
                    javaRecord.getCol()==matlabRecord.getCol()
            ){
                double gap = Math.abs(javaRecord.getDistance()-matlabRecord.getDistance());
                if(gap>0.5){
                    System.out.printf("i=%d, neighborIndex=%d, row=%d, col=%d, ,gap=%.2f, 请检查\n",
                            i,
                            javaRecord.getNeighborIndex(),
                            javaRecord.getRow(),
                            javaRecord.getCol(),
                            gap
                            );
                    throw new IllegalArgumentException("错误");
                }
            }
        }
    }
}
