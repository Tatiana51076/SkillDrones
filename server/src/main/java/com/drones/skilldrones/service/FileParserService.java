package com.drones.skilldrones.service;

import com.drones.skilldrones.model.RawTelegram;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileParserService {
    
    public List<RawTelegram> parseExcelFile(MultipartFile file) {
        List<RawTelegram> telegrams = new ArrayList<>();
        
        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {
            
            Sheet sheet = workbook.getSheetAt(0);
            
            // Пропускаем заголовок
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                
                RawTelegram telegram = parseRow(row);
                if (telegram != null) {
                    telegram.setFileName(file.getOriginalFilename());
                    telegrams.add(telegram);
                }
            }
            
        } catch (Exception e) {
            throw new RuntimeException("Ошибка парсинга Excel файла: " + e.getMessage(), e);
        }
        
        return telegrams;
    }
    
    private RawTelegram parseRow(Row row) {
        if (row.getPhysicalNumberOfCells() < 4) return null;
        
        RawTelegram telegram = new RawTelegram();
        
        // Центр ЕС ОрВД
        telegram.setCenter(getCellStringValue(row.getCell(0)));
        
        // SHR raw text
        telegram.setShrRawText(getCellStringValue(row.getCell(1)));
        
        // DEP raw text  
        telegram.setDepRawText(getCellStringValue(row.getCell(2)));
        
        // ARR raw text
        telegram.setArrRawText(getCellStringValue(row.getCell(3)));
        
        telegram.setProcessingStatus("PENDING");
        
        return telegram;
    }
    
    private String getCellStringValue(Cell cell) {
        if (cell == null) return "";
        
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getLocalDateTimeCellValue().toString();
                } else {
                    return String.valueOf((long) cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }
}
