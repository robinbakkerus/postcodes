package org.jrb.postcode;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.base.Charsets;
import com.google.common.io.CharSink;
import com.google.common.io.Files;

public class CsvParser {

	private int total = 0;
	private int count = 0;
	PreparedStatement pstmt = null;
	List<String> writeLines = new ArrayList<String>();
	Set<String> postcodes = new HashSet<>();
	
	private final static String SQL = "INSERT INTO Postcode_NL (postcode, street, city, province_code, lat, lon) VALUES (?,?,?,?,?,?)";
    
	public void readCsv(String filename) throws IOException, SQLException {
		Connection conn = null;
		try {
//			conn = getSqliteConnection();
//            pstmt = conn.prepareStatement(SQL);
			
            File file = new File(filename);
			List<String> result = Files.readLines(file, Charsets.UTF_8);
			total = result.size();
			this.writeLines.add("Postcode,straat,plaats,provincie_code,latitude,longitude");
			System.out.println("Processing " + total + " lines");
			result.forEach(s -> this.writeUpdatedCsv(s));
			
			File outFile = new File("d:/tmp/dbimport.csv");
		    CharSink sink = Files.asCharSink(outFile, Charsets.UTF_8);
		    sink.writeLines(this.writeLines, "\n");
			
		} finally {
//			this.closeDbConnection(conn);
		}

	}

	private Connection getSqliteConnection() {
		Connection conn = null;
		try {
			String url = "jdbc:sqlite:d:/my-dev/dbs/postcodes/postcode.db";
			conn = DriverManager.getConnection(url);
			System.out.println("Connection to SQLite has been established.");
			return conn;
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	private void closeDbConnection(Connection conn) {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}
	}



	private void processLine(String line) {
		if (line.startsWith("Postcode")) return; 
		
		insertSqliteRecord(line);
        
	}

	private void insertSqliteRecord(String line) {
		String cells[] = line.split(",");
		String postcode = cells[2];
		String straat = cells[5];
		String city = cells[8];
		String provincie = cells[10];
		String latStr = cells[11];
		String lngStr = cells[12];

		System.out.println(String.format("Processing %d : %s", this.count++, line));
		
		try {
			pstmt.setString(1, postcode);
			pstmt.setString(2, straat);
			pstmt.setString(3, city);
			pstmt.setString(4, this.getProvinceCode(provincie));
			pstmt.setFloat(5, Float.parseFloat(latStr));
			pstmt.setFloat(6, Float.parseFloat(lngStr));
			pstmt.executeUpdate();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void writeUpdatedCsv(String line) {
		String cells[] = line.split(",");
		String postcode = cells[2];
		String straat = cells[5];
		String city = cells[8];
		String provincie = cells[10];
		String latStr = cells[11];
		String lngStr = cells[12];

		System.out.println(String.format("Processing %d : %s", this.count++, line));
		if (!this.postcodes.contains(postcode)) {
			this.postcodes.add(postcode);
			writeLines.add(String.format("%s,%s,%s,%s,%s,%s",
					postcode, this.formatName(straat), this.formatName(city), this.getProvinceCode(provincie), latStr, lngStr));
		}
	}

	private String formatName(String name) {
		return name.replaceAll(" ","_");
	}
	
	private String getProvinceCode(String provincie) {
		if ("Noord-Holland".equals(provincie)) {
			return "NH";
		} else if ("Zuid-Holland".equals(provincie)) {
			return "ZH";
		} else if ("Noord-Brabant".equals(provincie)) {
			return "NB";
		} else if ("Zeeland".equals(provincie)) {
			return "ZE";
		} else if ("Groningen".equals(provincie)) {
			return "GR";
		} else if ("Friesland".equals(provincie)) {
			return "FR";
		} else if ("Drenthe".equals(provincie)) {
			return "DR";
		} else if ("Overijssel".equals(provincie)) {
			return "OV";
		} else if ("Gelderland".equals(provincie)) {
			return "GE";
		} else if ("Limburg".equals(provincie)) {
			return "LI";
		} else if ("Utrecht".equals(provincie)) {
			return "UT";
		} else if ("Flevoland".equals(provincie)) {
			return "FL";
		} else {
			return "??";
		}
	}
	
	//----------
	
	/**
	 * Deze een memory fout.
	 * 
	 * @param filename
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
//	@SuppressWarnings("deprecation")
//	public void parseFile(String filename) throws FileNotFoundException, IOException {
//		FileInputStream file = new FileInputStream(new File(filename));
//		Workbook workbook = new XSSFWorkbook(file);
//		Sheet sheet = workbook.getSheetAt(0);
//
//		Map<Integer, List<String>> data = new HashMap<>();
//		int i = 0;
//		for (Row row : sheet) {
//			data.put(i, new ArrayList<String>());
//			for (Cell cell : row) {
//				switch (cell.getCellTypeEnum()) {
//				case STRING:
//					System.out.println("String " + cell);
//					break;
//				case NUMERIC:
//					System.out.println("String " + cell);
//					break;
//				default:
//					data.get(new Integer(i)).add(" ");
//				}
//			}
//			i++;
//		}
//		workbook.close();
//	}

}
