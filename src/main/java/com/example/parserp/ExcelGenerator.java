package com.example.parserp;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.List;

public class ExcelGenerator {

    public static void main(String[] args) {
        try {
            // Получаем URL XML-файла

            URL url = new URL("https://www.international.gc.ca/world-monde/assets/office_docs/international_relations-relations_internationales/sanctions/sema-lmes.xml");
            // Создаем объект Unmarshaller для преобразования XML в Java-объекты
            JAXBContext jaxbContext = JAXBContext.newInstance(DataSet.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            // Преобразуем XML в Java-объекты
            DataSet sanctionsList = (DataSet) jaxbUnmarshaller.unmarshal(url);

            // Создаем новую книгу Excel
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Sanctions");

            // Заголовки столбцов
            String[] headers = {"№", "Фамилия", "Имя", "Дата рождения", "Гражданство"};
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            // Добавляем данные из Java-объектов в Excel
            List<Record> records = sanctionsList.getRecords();
            int rowNum = 1;
            for (Record record : records) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(record.getItem());
                row.createCell(1).setCellValue(record.getGivenName());
                row.createCell(2).setCellValue(record.getLastName());
                row.createCell(3).setCellValue(record.getDateOfBirth());
                row.createCell(4).setCellValue(record.getCountry());
            }

            // Сохраняем книгу Excel в файл
            FileOutputStream fileOut = new FileOutputStream("sanctions.xlsx");
            workbook.write(fileOut);
            fileOut.close();

            // Закрываем книгу Excel
            workbook.close();

            System.out.println("Excel файл успешно создан!");

        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
