package esa.myweather;

/**
 * @ProjectName: mydemo
 * @Package: esa.myweather
 * @ClassName: TableDirectory
 * @Description: java类作用描述
 * @Author: tvrddmss
 * @CreateDate: 2023/6/2 15:40
 * @UpdateUser: tvrddmss
 * @UpdateDate: 2023/6/2 15:40
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class TableDirectory {
    public String name; //table name

    public int checkSum; //Check sum

    public int offset; //Offset from beginning of file

    public int length; //length of the table in bytes
}
