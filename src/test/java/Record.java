import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Record {
    @ExcelProperty(value="neighbor", index = 0)
    private int neighborIndex;
    @ExcelProperty(value="i", index = 1)
    private int row;
    @ExcelProperty(value="j", index = 2)
    private int col;
    @ExcelProperty(value="distance", index = 3)
    private double distance;

}
