package censusanalyser;

import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Test;

public class IndianStateTest {

    private static final String INDIA_CENSUS_CSV_FILE_PATH = "./src/test/resources/IndiaStateCensusData.csv";
    private static final String WRONG_CSV_FILE_PATH = "./src/main/resources/IndiaStateCensusData.csv";
    private static final String INDIA_CODE_CSV_FILE_PATH = "./src/test/resources/IndiaStateCode.csv";
    private static final String WRONG_DELIMITER_CSV_FILE_PATH = "./src/test/resources/delimiterWrong.csv";
    private static final String WRONG_HEADER_CSV_FILE_PATH = "./src/test/resources/headerWrong.csv";

    CensusAnalyser censusAnalyser = new CensusAnalyser();

    @Test
    public void givenIndiaCSVData_ShouldGetMapOfCorrectSize() {
        try {
            int numOfRecords = censusAnalyser.loadCSVCensusData(CensusAnalyser.Country.USA, INDIA_CENSUS_CSV_FILE_PATH);
            Assert.assertEquals(29, numOfRecords);
        } catch (CensusAnalyserException e) {
        }
    }

    @Test
    public void givenStateCSVFile_WhenHeaderNotAvailable_ThrowsException() {
        try {
            int numOfRecords = censusAnalyser.loadCSVCensusData(CensusAnalyser.Country.USA, WRONG_HEADER_CSV_FILE_PATH);
            Assert.assertEquals(29, numOfRecords);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.SOME_OTHER_FILE_ERROR, e.type);
        }
    }

    @Test
    public void givenStateCSVFile_WhenPassEmptyFile_ThrowsException() {
        try {
            int numOfRecords = censusAnalyser.loadCSVCensusData(CensusAnalyser.Country.USA, "");
            Assert.assertEquals(29, numOfRecords);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.SOME_OTHER_FILE_ERROR, e.type);
        }
    }

    @Test
    public void givenIndiaCensusData_WithWrongDelimiter_ShouldThrowException() {
        try {
            int numOfRecords = censusAnalyser.loadCSVCensusData(CensusAnalyser.Country.INDIA, WRONG_DELIMITER_CSV_FILE_PATH);
            Assert.assertEquals(29, numOfRecords);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.SOME_OTHER_FILE_ERROR, e.type);
        }
    }

    @Test
    public void givenIndianStateData_SortedOnState_ShouldReturnSortedResult() {
        String sortedData = null;
        try {
            censusAnalyser.loadCSVCensusData(CensusAnalyser.Country.INDIA, INDIA_CENSUS_CSV_FILE_PATH, INDIA_CODE_CSV_FILE_PATH);
            sortedData = censusAnalyser.getSortedDataBasedOnState(SortDataBaseOnField.state);
            IndiaCensusCSV[] censusCSV = new Gson().fromJson(sortedData, IndiaCensusCSV[].class);
            Assert.assertEquals("Andhra Pradesh", censusCSV[0].state);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.SOME_OTHER_FILE_ERROR, e.type);
        }
    }

    @Test
    public void givenIndianCensusData_WithWrongFile_ShouldThrowException() {
        try {
            int numOfRecords = censusAnalyser.loadCSVCensusData(CensusAnalyser.Country.USA, WRONG_CSV_FILE_PATH);
            Assert.assertEquals(29, numOfRecords);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM, e.type);
        }
    }
}