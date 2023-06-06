package com.zhuzhe.batchprocessing.util;

import com.zhuzhe.batchprocessing.annotation.ExcelColumn;
import com.zhuzhe.batchprocessing.annotation.ExcelTable;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.CollectionUtils;

public class PoiUtils {

  /*表格初始行索引*/
  public static final int POSITION_ROW = 0;
  /*表格初始列索引*/
  public static final int POSITION_COL = 0;
  /*一个表中最大的支持的行数， 这里-1是为了保留表头行*/
  /*xls格式支持的最大数据行-1*/
  public static final int XLS_MAX_ROW = 65535;
  /*xlsx格式支持的最大数据行-1*/
  public static final int XLSX_MAX_ROW = 1048575;
  /*一个表中最大支持的列数，这里不做处理，因为垂直拆表的实际场景不多*/
  /*xls格式支持的最大数据列*/
  public static final int XLS_MAX_COL = 256;
  /*xlsx格式支持的最大数据列*/
  public static final int XLSX_MAX_COL = 1048576;

  private static void assertException(boolean condition, String message) {
    if (!condition) {
      throw new RuntimeException(message);
    }
  }

  /**
   * 将数据转为Excel表格
   *
   * @param list 数据列表
   * @param <T>
   * @return
   */
  public static <T> Workbook writeDataToExcel(List<T> list) {

    // 判断数据有效性以及提取类对象
    assertException(!CollectionUtils.isEmpty(list), "需要读取为表格的数据不能为空！");

    // 这里还有其他更精确的方式，因为我这边是默认一个列表存储的元素都是一样的类型，所以简单提取
    Class<?> clazz = list.get(0).getClass();

    // 使用反射方式获取注解内容
    var excelTable = clazz.getAnnotation(ExcelTable.class);
    assertException(excelTable != null, "数据对象没有添加注解@ExcelTable");
    var tableName = excelTable.name();
    var tableFormat = excelTable.format();

    var headers = new ArrayList<String>();
    var widths = new ArrayList<Integer>();
    var fields = clazz.getDeclaredFields();
    for (Field field : fields) {
      var excelColumn = field.getAnnotation(ExcelColumn.class);
      if (excelColumn != null) {
        headers.add(excelColumn.header());
        widths.add(excelColumn.width() * 256);
      }
    }
    assertException(!headers.isEmpty() && !widths.isEmpty(),
        "对象成员没有添加注解@ExcelColumn");

    // 根据需求,创建xls或者创建xlsx的表格（目前只支持这两种）
    Workbook workbook = new SXSSFWorkbook();
    int sheetMaxRow = XLSX_MAX_ROW;
    if (StringUtils.equals("xls", tableFormat)) {
      workbook = new HSSFWorkbook();
      sheetMaxRow = XLS_MAX_ROW;
    }

    // 根据需求,对不同格式文件做数据水平切分到各个表中
    var size = list.size();
    int sheetSize = size % sheetMaxRow == 0 ? size / sheetMaxRow : size / sheetMaxRow + 1;

    for (int i = 0; i < sheetSize; i++) {
      int rows = POSITION_ROW;
      int cols = POSITION_COL;

      // 设置表信息
      var sheet = workbook.createSheet();
      workbook.setSheetName(i, tableName + (i + 1));

      // 设置表头
      var head = sheet.createRow(rows++);
      var headStyle = getHeadCellStyle(workbook);
      for (int j = 0; j < headers.size(); j++) {
        sheet.setColumnWidth(cols, widths.get(j));
        addCellWithStyle(head, cols++, headStyle).setCellValue(headers.get(j));
      }

      // 设置表内容
      var bodyStyle = getBodyCellStyle(workbook);
      for (int j = i * sheetMaxRow; j < Integer.min(size, (i + 1) * sheetMaxRow); j++) {
        var data = list.get(j);
        cols = POSITION_COL;
        var row = sheet.createRow(rows++);

        for (int k = 0; k < fields.length; k++) {
          var field = fields[k];
          field.setAccessible(true);
          try {
            var excelColumn = field.getAnnotation(ExcelColumn.class);
            if (excelColumn != null) {
              var value = field.get(data);
              var cell = addCellWithStyle(row, cols++, bodyStyle);
              setCellValue(cell, value);
            }
          } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
          }
        }
      }
    }
    return workbook;
  }

  /*设置单元格的样式*/
  private static Cell addCellWithStyle(Row row, int colPosition, CellStyle cellStyle) {
    var cell = row.createCell(colPosition);
    cell.setCellStyle(cellStyle);
    return cell;
  }

  /*设置表头的样式*/
  private static CellStyle getHeadCellStyle(Workbook workbook) {
    var style = getBaseCellStyle(workbook);
    // 设置填充颜色
    style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    return style;
  }

  /*设置表体样式*/
  private static CellStyle getBodyCellStyle(Workbook workbook) {
    return getBaseCellStyle(workbook);
  }

  /*基础表格样式*/
  private static CellStyle getBaseCellStyle(Workbook workbook) {
    var style = workbook.createCellStyle();

    // 字体设置
    var font = workbook.createFont();
    font.setBold(true);
    style.setFont(font);

    // 对其方式
    style.setAlignment(HorizontalAlignment.CENTER);
    style.setVerticalAlignment(VerticalAlignment.TOP);

    // 边框设置
    style.setBorderBottom(BorderStyle.THIN);
    style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
    style.setBorderLeft(BorderStyle.THIN);
    style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
    style.setBorderRight(BorderStyle.THIN);
    style.setRightBorderColor(IndexedColors.BLACK.getIndex());
    style.setBorderTop(BorderStyle.THIN);
    style.setTopBorderColor(IndexedColors.BLACK.getIndex());
    return style;
  }

  /*排队并将属性值设置到单元格中*/
  private static void setCellValue(Cell cell, Object value) {
    if (Objects.nonNull(value)) {
      if (value instanceof String) {
        cell.setCellValue((String) value);
      } else if (value instanceof Integer) {
        cell.setCellValue((Integer) value);
      } else if (value instanceof Long) {
        cell.setCellValue((Long) value);
      } else if (value instanceof Float) {
        cell.setCellValue((Float) value);
      } else if (value instanceof Double) {
        cell.setCellValue((Double) value);
      } else if (value instanceof Boolean) {
        cell.setCellValue((Boolean) value);
      } else if (value instanceof Date) {
        var sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        cell.setCellValue(sdf.format((Date) value));
      } else {
        cell.setCellValue(value.toString());
      }
    }
  }

  public static <T> List<T> readDataFormExcel(InputStream inputStream, Class<T> clazz) {

    List<T> list = null;
    try {
      var excelTable = clazz.getAnnotation(ExcelTable.class);
      assertException(excelTable != null, "数据对象没有添加注解@ExcelTable");
      var tableFormat = excelTable.format();

      // 根据实体定义来使用对应的Workbook
      Workbook workbook = new XSSFWorkbook(inputStream);
      if (StringUtils.equals("xls", tableFormat)) {
        workbook = new HSSFWorkbook(inputStream);
      }

      // 构建注解和field映射map
      var fields = clazz.getDeclaredFields();
      Map<String, Field> excelColumnMap = Arrays.stream(fields)
          .filter(field -> field.isAnnotationPresent(ExcelColumn.class))
          .collect(Collectors.toMap(field -> field.getAnnotation(ExcelColumn.class).header(),
              Function.identity(), (o1, o2) -> o1));
      System.out.println("excelColumnMap = " + excelColumnMap);

      // 校验Excel中是否至少一个Sheet
      assertException(workbook.getSheetAt(0) != null, "无效Excel文件，文件中至少包含一个表格");

      // 遍历Excel中的表插入到MySQL中(一个Excel中有多个sheet)
      list = new ArrayList<>();
      var sheetIterator = workbook.sheetIterator();
      while (sheetIterator.hasNext()) {
        var sheet = sheetIterator.next();
        var headerRow = sheet.getRow(POSITION_ROW);
        // 遍历表体每一行
        for (int i = POSITION_ROW + 1; i <= sheet.getLastRowNum(); i++) {
          var bodyRow = sheet.getRow(i);
          assertException(headerRow.getLastCellNum() >= bodyRow.getLastCellNum(),
              "无效数据，请检查表格中数据格式，表头和表体长度一致");
          T instance = clazz.getDeclaredConstructor().newInstance();
          //遍历列，匹配实体注解标题名并插入对象
          for (int j = 0; j <= headerRow.getLastCellNum(); j++) {
            var header = headerRow.getCell(j);
            var body = bodyRow.getCell(j);
            if (header == null || body == null) {
              continue;
            }
            String headerStr = String.valueOf(header);
            if (excelColumnMap.containsKey(headerStr)) {
              setInstanceField(instance, excelColumnMap.get(headerStr), body);
            }
          }
          list.add(instance);
        }
      }
      workbook.close();
    } catch (IOException | InstantiationException | IllegalAccessException |
             InvocationTargetException | NoSuchMethodException e) {
      System.out.println("读取表格失败：" + e.getMessage());
      throw new RuntimeException(e);
    }
    return list;
  }

  static <T> void setInstanceField(T instance, Field field, Cell cell) {

    if (cell == null) {
      return;
    }

    Object value = null;
    field.setAccessible(true);
    Class<?> fieldType = field.getType();

    if (fieldType.equals(String.class)) {
      value = cell.getStringCellValue();
    } else if (fieldType.equals(Integer.class) || fieldType == int.class) {
      value = (int) cell.getNumericCellValue();
    } else if (fieldType.equals(Long.class) || fieldType == long.class) {
      value = (long) cell.getNumericCellValue();
    } else if (fieldType.equals(Float.class) || fieldType == float.class) {
      value = (float) cell.getNumericCellValue();
    } else if (fieldType.equals(Short.class) || fieldType == short.class) {
      value = (short) cell.getNumericCellValue();
    } else if (fieldType.equals(Double.class) || fieldType == double.class) {
      value = cell.getNumericCellValue();
    } else if (fieldType.equals(Boolean.class) || fieldType == boolean.class) {
      value = cell.getBooleanCellValue();
    } else if (fieldType.equals(Date.class)) {
      var sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
      try {
        value = sdf.parse(cell.getStringCellValue());
      } catch (ParseException e) {
        throw new RuntimeException(e);
      }
    }
    try {
      field.set(instance, value);
    } catch (IllegalAccessException e) {
      System.out.println("字段无法访问：" + e.getMessage());
      throw new RuntimeException(e);
    }
  }
}
