package project;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

public class ExcelReader {
	
	public static Track[] ReadTracksFromExcelSheets(){
		//
		// reads the CSV files and return a track list
		// that is created by using needed values in the CSV files
		//
		String valDataSheetFileName = "term_project_value_data"; // file name of value data CSV file
		String seqDataSheetFileName = "term_project_sequential_data"; // file name of sequential data CSV file
		
		Track[] trackList = null;
		CSVReader valDataReader = null;  
		CSVReader seqDataReader = null;  
		
		try {
			valDataReader = new CSVReader(new FileReader(
					System.getProperty("user.dir") + "\\" + valDataSheetFileName + ".csv"));
			seqDataReader = new CSVReader(new FileReader(
					System.getProperty("user.dir") + "\\" + seqDataSheetFileName + ".csv"));  	

			List<String[]> valData = valDataReader.readAll(); // value data in string array list format
			List<String[]> seqData = seqDataReader.readAll(); // sequential data in string array list format
			
			int trackIndex = 0;
			int trackCount = valData.size(); // total track count
			trackList = new Track[trackCount - 1]; // -1 is for eliminate the first row that consists of column titles
			
			for (int rowIndex = 1; rowIndex < trackCount; rowIndex++) {
				// rowIndex starts from 1 because, first row has only column titles
				double[] seqValue = ConvertStringListToDoubleArray(Arrays.asList(
						seqData.get(rowIndex))
						.stream()
						.skip(1) // this part is for skipping the first(track id) column
						.collect(Collectors.toList()));
				
				Track newTrack = new Track(
						Integer.parseInt(valData.get(rowIndex)[0]), // id
						Integer.parseInt(valData.get(rowIndex)[5]), // duration
						Integer.parseInt(valData.get(rowIndex)[4]), // individual value
						seqValue); // sequential value
				
				// add track to track list
				trackList[trackIndex++] = newTrack;
			}
			
		} catch (FileNotFoundException e) {
			System.out.println("Your CSV files could not read!\nPlease put the CSV files into the main project folder ("
					+ System.getProperty("user.dir") + ")\n"
					+ "and make sure that the file names are correct.");
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (CsvException e) {
			e.printStackTrace();
		}
		
		return trackList;
	}
	
	private static double[] ConvertStringListToDoubleArray(List<String> stringList) {
		//
		// converts given string list to double array
		//
		double[] doubleArray = new double[stringList.size()];
		for (int index = 0; index < stringList.size(); index++) {
			try {
				if (!stringList.get(index).isEmpty())
					doubleArray[index] = Double.parseDouble(stringList.get(index));
			}catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		return doubleArray;
	}
	
}
