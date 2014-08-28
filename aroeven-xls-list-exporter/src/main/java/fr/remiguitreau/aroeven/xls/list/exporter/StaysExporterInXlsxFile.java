package fr.remiguitreau.aroeven.xls.list.exporter;

import org.apache.commons.io.FileUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import lombok.extern.slf4j.Slf4j;
import fr.remiguitreau.aroeven.lucine.api.AroevenStay;
import fr.remiguitreau.aroeven.lucine.impl.StayExtractionException;

@Slf4j
public class StaysExporterInXlsxFile {

    public File exportStays(final List<AroevenStay> stays, final StaysExporterListener listener) {
        try {
            log.info("Try to export {} stays in excel format !", stays.size());
            final File destFile = new File("exports", "lucine_export" + System.currentTimeMillis() + ".xlsx");
            if (!destFile.getParentFile().exists()) {
                destFile.getParentFile().mkdir();
            }
            FileUtils.copyURLToFile(StaysExporterInXlsxFile.class.getResource("export.xlsx"), destFile);
            final XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(destFile));
            final XSSFSheet sheet = workbook.getSheet("extraction");
            final Map<String, Integer> headers = extractHeaders(sheet.getRow(0));
            fillSheetWithStays(stays, sheet, headers, listener);
            workbook.write(new FileOutputStream(destFile));
            return destFile;
        } catch (final Exception ex) {
            throw new StayExtractionException(ex);
        }
    }

    private void fillSheetWithStays(final List<AroevenStay> stays, final XSSFSheet sheet,
            final Map<String, Integer> headers, final StaysExporterListener listener) {
        for (int i = 1; i < stays.size(); i++) {
            fillRowWithStay(sheet.getRow(i), stays.get(i), headers);
            listener.newStayExported();
        }
    }

    // -------------------------------------------------------------------------

    private void fillRowWithStay(final XSSFRow row, final AroevenStay aroevenStay,
            final Map<String, Integer> headers) {
        log.debug("-------------------- Insert new Stay");
        for (final Entry<String, String> entry : aroevenStay.getProperties().entrySet()) {
            if (headers.containsKey(entry.getKey())) {
                log.debug("     - {} = {} exported", entry.getKey(), entry.getValue());
                final Integer columnIndex = headers.get(entry.getKey());
                XSSFCell cell = row.getCell(columnIndex);
                if (cell == null) {
                    cell = row.createCell(columnIndex);
                }
                cell.setCellValue(entry.getValue());
            }
        }

    }

    private Map<String, Integer> extractHeaders(final XSSFRow row) {
        final Map<String, Integer> headers = new HashMap<String, Integer>();
        log.info("Headers: ");
        final short cellNum = row.getLastCellNum();
        for (int i = 0; i < cellNum; i++) {
            final String key = row.getCell(i).getStringCellValue().trim();
            log.info("    - {} -> column {}", key, i);
            if (!headers.containsKey(key)) {
                headers.put(key, Integer.valueOf(i));
            }
        }
        return headers;
    }

}
