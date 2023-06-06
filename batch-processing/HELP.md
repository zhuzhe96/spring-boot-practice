# 批量导入数据（Excel）

### 实现方式: Apache poi
* 测试数据库: [database.sql](./database.sql) 其中就包含两个表, 人员表people和权限表permission
* 测试将数据库数据转为Excel表格: 
  * http://localhost:5014/people/download
  * http://localhost:5014/permission/download
* 测试将Excel表格转为数据库数据: 
  * http://localhost:5014/people/upload
  * http://localhost:5014/permission/upload

### 代码风格
* 通过注解实现在实体类中就可以配置表格的基础信息, 同时调用工具类进行转换时根据注解来读取处理表格.
* 只需要一个标准的Mybatis-Plus中使用的数据实体,增加上表信息注解@ExcelTable和列注解@ExcelColumn就可以用于Excel表格的导入和导出
* 目前实现了对xls和xlsx两种表格格式的导出和导入,使用方式在实体的注解@ExcelTable中的一个属性format指定
* 导入和导出的数据都会去根据上面提到的两种表格所支持的最大存储行数而做分表操作.
* 目前是仅支持标准的表格格式的读写, 复杂格式暂不支持
---
### xls和xlsx的区别

| XLS                                   | XLSX                           |
| ------------------------------------- | ------------------------------ |
| 只能打开xls格式，无法直接打开xlsx格式 | 可以直接打开xls、xlsx格式      |
| 只有65536行、256列                    | 可以有1048576行、16384列       |
| 占用空间大                            | 占用空间小，运算速度也会快一点 |

### POI中的对象关系

| Excel            | POI XLS              | POI XLSX(Excel 2007+) |
| ---------------- | -------------------- | --------------------- |
| Excel 文件       | HSSFWorkbook （xls） | XSSFWorkbook（xlsx）  |
| Excel 工作表     | HSSFSheet            | XSSFSheet             |
| Excel 行         | HSSFRow              | XSSFRow               |
| Excel 单元格     | HSSFCell             | XSSFCell              |
| Excel 单元格样式 | HSSFCellStyle        | HSSFCellStyle         |
| Excel 颜色       | HSSFColor            | XSSFColor             |
| Excel 字体       | HSSFFont             | XSSFFont              |