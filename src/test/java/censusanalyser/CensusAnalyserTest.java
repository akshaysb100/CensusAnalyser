package censusanalyser;

import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.lang.reflect.InvocationTargetException;

public class CensusAnalyserTest {

    private static final String INDIA_CENSUS_CSV_FILE_PATH = "./src/test/resources/IndiaStateCensusData.csv";
    private static final String WRONG_CSV_FILE_PATH = "./src/main/resources/IndiaStateCensusData.csv";
    private static final String INDIA_CODE_CSV_FILE_PATH = "./src/test/resources/IndiaStateCode.csv";
    private static final String WRONG_DELIMITER_CSV_FILE_PATH = "./src/test/resources/delimiterWrong.csv";
    private static final String WRONG_HEADER_CSV_FILE_PATH = "./src/test/resources/headerWrong.csv";

    @Test
    public void givenIndianCensusCSVFileReturnsCorrectRecords() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            int numOfRecords = censusAnalyser.loadIndiaCensusData(INDIA_CENSUS_CSV_FILE_PATH);
            Assert.assertEquals(29,numOfRecords);
        } catch (CensusAnalyserException e) { }
    }

    @Test
    public void givenIndiaCensusData_WithWrongFile_ShouldThrowException() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            ExpectedException exceptionRule = ExpectedException.none();
            exceptionRule.expect(CensusAnalyserException.class);
            censusAnalyser.loadIndiaCensusData(WRONG_CSV_FILE_PATH);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM,e.type);
        }
    }

    @Test
    public void givenIndianStateCodeSCV_ShouldReturnExtraCode() {

        CensusAnalyser censusAnalyser = new CensusAnalyser();
        try {
            censusAnalyser.loadIndiaCensusData(INDIA_CENSUS_CSV_FILE_PATH);
            int numOfRecords = censusAnalyser.loadIndiaSateCode(INDIA_CODE_CSV_FILE_PATH);
            Assert.assertEquals(29,numOfRecords);
        } catch (CensusAnalyserException e) {
        }
    }

    @Test
    public void givenIndianStateData_SortedOnState_ShouldReturnSortedResult() {

        CensusAnalyser censusAnalyser = new CensusAnalyser();
        String sortedData = null;
        try {
            censusAnalyser.loadIndiaCensusData(INDIA_CENSUS_CSV_FILE_PATH);
            sortedData = censusAnalyser.getSortedDataBasedOnState();
            IndiaCensusCSV[] censusCSV = new Gson().fromJson(sortedData,IndiaCensusCSV[].class);
            Assert.assertEquals("Andhra Pradesh",censusCSV[0].state);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.SOME_OTHER_FILE_ERROR,e.type);
        }
    }

    @Test
    public void givenIndianStateData_SortedOnState_IfPassWrongFile_ShouldThrowException() {

        CensusAnalyser censusAnalyser = new CensusAnalyser();
        String sortedData = null;
        try {
            censusAnalyser.loadIndiaCensusData(INDIA_CODE_CSV_FILE_PATH);
            sortedData = censusAnalyser.getSortedDataBasedOnState();
            IndiaCensusCSV[] censusCSV = new Gson().fromJson(sortedData,IndiaCensusCSV[].class);
            Assert.assertEquals("Andhra Pradesh",censusCSV[0].state);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.SOME_OTHER_FILE_ERROR,e.type);
        }
    }

    @Test
    public void givenIndianStateData_SortedOnPopulation_ShouldReturnSortedResult() {

        CensusAnalyser censusAnalyser = new CensusAnalyser();
        String sortedData = null;
        try {
            censusAnalyser.loadIndiaCensusData(INDIA_CENSUS_CSV_FILE_PATH);
            sortedData = censusAnalyser.getSortedDataBasedOnState();
            IndiaCensusCSV[] censusCSV = new Gson().fromJson(sortedData,IndiaCensusCSV[].class);
            Assert.assertEquals(49386799,censusCSV[0].population);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.SOME_OTHER_FILE_ERROR,e.type);
        }
    }

    @Test
    public void givenIndianStateData_SortedOnArea_In_Sq_Km_ShouldReturnSortedResult() {

        CensusAnalyser censusAnalyser = new CensusAnalyser();
        String sortedData = null;
        try {
            censusAnalyser.loadIndiaCensusData(INDIA_CENSUS_CSV_FILE_PATH);
            sortedData = censusAnalyser.getSortedDataBasedOnState();
            IndiaCensusCSV[] censusCSV = new Gson().fromJson(sortedData,IndiaCensusCSV[].class);
            Assert.assertEquals(162968,censusCSV[0].areaInSqKm);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.SOME_OTHER_FILE_ERROR,e.type);
        }
    }

    @Test
    public void givenIndianStateData_SortedOnArea_IfPassNullFile_ShouldThrowException() {

        CensusAnalyser censusAnalyser = new CensusAnalyser();
        String sortedData = null;
        try {
            censusAnalyser.loadIndiaCensusData("");
            sortedData = censusAnalyser.getSortedDataBasedOnState();
            IndiaCensusCSV[] censusCSV = new Gson().fromJson(sortedData,IndiaCensusCSV[].class);
            Assert.assertEquals(162968,censusCSV[0].areaInSqKm);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.SOME_OTHER_FILE_ERROR,e.type);
        }
    }

    @Test
    public void givenIndianStateData_SortedOnDensityPerSqKm_ShouldReturnSortedResult() {

        CensusAnalyser censusAnalyser = new CensusAnalyser();
        String sortedData = null;
        try {
            censusAnalyser.loadIndiaCensusData(INDIA_CENSUS_CSV_FILE_PATH);
            sortedData = censusAnalyser.getSortedDataBasedOnState();
            IndiaCensusCSV[] censusCSV = new Gson().fromJson(sortedData,IndiaCensusCSV[].class);
            Assert.assertEquals(303,censusCSV[0].densityPerSqKm);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.SOME_OTHER_FILE_ERROR,e.type);
        }
    }

    @Test
    public void givenIndiaCensusData_WithWrongDelimiter_ShouldThrowException() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            ExpectedException exceptionRule = ExpectedException.none();
            exceptionRule.expect(CensusAnalyserException.class);
            censusAnalyser.loadIndiaCensusData(WRONG_DELIMITER_CSV_FILE_PATH);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.SOME_OTHER_FILE_ERROR,e.type);
        }
    }

    @Test
    public void givenIndiaCensusData_WithWrongHeader_ShouldThrowException() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            ExpectedException exceptionRule = ExpectedException.none();
            exceptionRule.expect(CensusAnalyserException.class);
            censusAnalyser.loadIndiaCensusData(WRONG_HEADER_CSV_FILE_PATH);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.SOME_OTHER_FILE_ERROR,e.type);
        }
    }
}
