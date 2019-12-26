package org.jrb.postcode;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import org.junit.Test;

public class SqliteLoader {
	
	@Test
	public void loadSqlite() {
		 CsvParser excelParser = new CsvParser();
	        try {
//				excelParser.parseFile("D:\\my-dev\\dbs\\postcodes\\postcodetabel.xlsx");
				excelParser.readCsv("D:\\my-dev\\dbs\\postcodes\\postcodetabel.csv");
				System.out.println("Done!");
			} catch (FileNotFoundException|SQLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

}
