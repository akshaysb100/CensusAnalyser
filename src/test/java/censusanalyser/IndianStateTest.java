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
    private static final String WRONG_DELIMITER_CODE_CSV_FILE_PATH = "./src/test/resources/wrongDelimiterCsvSateCodeFile.csv";
    CensusAnalyser censusAnalyser = new CensusAnalyser(CensusAnalyser.Country.INDIA);

    @Test
    public void givenIndiaCSVData_ShouldGetMapOfCorrectSize() {
        try {
            int numOfRecords = censusAnalyser.loadCSVCensusData(INDIA_CENSUS_CSV_FILE_PATH, INDIA_CODE_CSV_FILE_PATH);
            Assert.assertEquals(29, numOfRecords);
        } catch (CensusAnalyserException e) {
        }
    }

    @Test
    public void givenStateCSVFile_WhenHeaderNotAvailable_ThrowsException() {
        try {
            int numOfRecords = censusAnalyser.loadCSVCensusData(WRONG_HEADER_CSV_FILE_PATH, INDIA_CODE_CSV_FILE_PATH);
            Assert.assertEquals(29, numOfRecords);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM, e.type);
        }
    }

    @Test
    public void givenStateCSVFile_WhenPassEmptyFile_ThrowsException() {
        try {
            int numOfRecords = censusAnalyser.loadCSVCensusData("", INDIA_CODE_CSV_FILE_PATH);
            Assert.assertEquals(29, numOfRecords);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM, e.type);
        }
    }

    @Test
    public void givenIndiaCensusData_WithWrongDelimiter_ShouldThrowException() {
        try {
            int numOfRecords = censusAnalyser.loadCSVCensusData(WRONG_DELIMITER_CSV_FILE_PATH, INDIA_CODE_CSV_FILE_PATH);
            Assert.assertEquals(29, numOfRecords);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM, e.type);
        }
    }

    @Test
    public void givenIndianCensusData_WithWrongFile_ShouldThrowException() {
        try {
            int numOfRecords = censusAnalyser.loadCSVCensusData(WRONG_DELIMITER_CSV_FILE_PATH, WRONG_CSV_FILE_PATH);
            Assert.assertEquals(29, numOfRecords);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM, e.type);
        }
    }

    @Test
    public void givenIndianCensusData_WithWrongOneFile_ShouldThrowException() {
        try {
            int numOfRecords = censusAnalyser.loadCSVCensusData(INDIA_CENSUS_CSV_FILE_PATH, WRONG_CSV_FILE_PATH);
            Assert.assertEquals(29, numOfRecords);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM, e.type);
        }
    }

    @Test
    public void givenIndiaStateCodeCensusData_WithWrongDelimiter_ShouldThrowException() {
        try {
            int numOfRecords = censusAnalyser.loadCSVCensusData(INDIA_CENSUS_CSV_FILE_PATH, WRONG_DELIMITER_CODE_CSV_FILE_PATH);
            Assert.assertEquals(29, numOfRecords);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM, e.type);
        }
    }
}