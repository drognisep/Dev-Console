package com.saylorsolutions.devutils.data;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;
import org.apache.commons.csv.*;

public class CsvTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 2090700213874160950L;
	private List<CSVRecord> records;
	private Map<String, Integer> headerMap;
	
	public CsvTableModel(File f, Charset c) {
		this(f, c, CSVFormat.DEFAULT);
	}

	public CsvTableModel(File f, Charset c, CSVFormat format) {
		try {
			CSVParser parser = CSVParser.parse(f, c, format);
			initialize(parser);
		} catch (IOException iox) {
			// TODO Auto-generated catch block
			iox.printStackTrace();
		}
	}
	
	public CsvTableModel(String s) {
		this(s, CSVFormat.DEFAULT);
	}

	public CsvTableModel(String s, CSVFormat format) {
		try(CSVParser parser = CSVParser.parse(s, format);) {
			initialize(parser);
		} catch (IOException iox) {
			// TODO Auto-generated catch block
			iox.printStackTrace();
		}
	}
	
	protected void initialize(CSVParser parser) throws IOException {
		headerMap = parser.getHeaderMap();
		records = parser.getRecords();
	}

	@Override
	public int getRowCount() {
		return records.size();
	}

	@Override
	public int getColumnCount() {
		return records.get(0).size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return records.get(rowIndex).get(columnIndex);
	}

	// TODO: Make a mutable extension of CSVRecord
//	@Override
//	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
//		CSVRecord rec = records.get(rowIndex);
//		String[] values = new String[rec.size()];
//		for(int ii = 0; ii < values.length; ii++) {
//			values[ii] = rec.get(ii);
//		}
//		values[columnIndex] = aValue.toString();
//		
//	}

	public void exportCsv(File f) throws IOException {
		exportCsv(f, CSVFormat.DEFAULT);
	}

	public void exportCsv(File f, CSVFormat format) throws IOException {
		StringBuilder sb = new StringBuilder();
		getCsvText(sb, format);
		try(FileWriter fw = new FileWriter(f);) {
			fw.write(sb.toString());
		} catch (IOException iox) {
			throw new IOException("Failed to write to file", iox);
		}
	}

	protected void getCsvText(StringBuilder sb, CSVFormat format) throws IOException {
		try(CSVPrinter printer = new CSVPrinter(sb, format);) {
			printer.printRecords(records);
			printer.flush();
			printer.close();
		} catch (IOException iox) {
			throw new IOException("Unable to write records to StringBuilder", iox);
		}
	}
}
