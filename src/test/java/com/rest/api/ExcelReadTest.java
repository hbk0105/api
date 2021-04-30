package com.rest.api;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ExcelReadTest {

    @Test
    public void excelTest()  throws Exception{
        try {
            // 경로에 있는 파일을 읽
            FileInputStream file = new FileInputStream("C:\\upload\\testExcel.xlsx");
            XSSFWorkbook workbook = new XSSFWorkbook(file);

            int rowNo = 0;
            int cellIndex = 0;

            XSSFSheet sheet = workbook.getSheetAt(0); // 0 번째 시트를 가져온다
            // 만약 시트가 여러개 인 경우 for 문을 이용하여 각각의 시트를 가져온다
            int rows = sheet.getPhysicalNumberOfRows(); // 사용자가 입력한 엑셀 Row수를 가져온다
            for(rowNo = 0; rowNo < rows; rowNo++){
                XSSFRow row = sheet.getRow(rowNo);
                if(row != null){
                    int cells = row.getPhysicalNumberOfCells(); // 해당 Row에 사용자가 입력한 셀의 수를 가져온다
                    for(cellIndex = 0; cellIndex <= cells; cellIndex++){
                        XSSFCell cell = row.getCell(cellIndex); // 셀의 값을 가져온다
                        String value = "";
                        if(cell == null){ // 빈 셀 체크
                            continue;
                        }else{
                            // 타입 별로 내용을 읽는다
                            switch (cell.getCellType()){
                                case XSSFCell.CELL_TYPE_FORMULA:
                                    value = cell.getCellFormula();
                                    break;
                                case XSSFCell.CELL_TYPE_NUMERIC:
                                    value = cell.getNumericCellValue() + "";
                                    break;
                                case XSSFCell.CELL_TYPE_STRING:
                                    value = cell.getStringCellValue() + "";
                                    break;
                                case XSSFCell.CELL_TYPE_BLANK:
                                    value = cell.getBooleanCellValue() + "";
                                    break;
                                case XSSFCell.CELL_TYPE_ERROR:
                                    value = cell.getErrorCellValue() + "";
                                    break;
                            }
                        }
                        System.out.println( rowNo + "번 행 : " + cellIndex + "번 열 값은: " + value);
                    }
                }
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
}