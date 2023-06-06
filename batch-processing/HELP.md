# 批量导入数据（Excel）

### 实现方式: Apache poi
* 测试数据库: [database.sql](./database.sql) 其中就包含两个表, 人员表people和权限表permission

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