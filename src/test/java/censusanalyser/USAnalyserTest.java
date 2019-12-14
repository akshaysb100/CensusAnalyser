package censusanalyser;

import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Test;

public class USAnalyserTest {

    private static final String UC_CENSUS_CSV_FILE_PATH = "./src/test/resources/USCensusData.csv";
    private static final String WRONG_CSV_FILE_PATH = "./src/main/resources/IndiaStateCensusData.csv";
    private static final String WRONG_DELIMITER_CSV_FILE_PATH = "./src/test/resources/delimiterWrong.csv";
    private static final String WRONG_HEADER_CSV_FILE_PATH = "./src/test/resources/headerWrong.csv";
    CensusAnalyser censusAnalyser = new CensusAnalyser(CensusAnalyser.Country.USA);

    @Test
    public void givenUSCensusCSVFile_ReturnsCorrectRecords() {
        try {
            int numOfRecords = censusAnalyser.loadCSVCensusData(UC_CENSUS_CSV_FILE_PATH);
            Assert.assertEquals(51, numOfRecords);
        } catch (CensusAnalyserException e) {
        }
    }

    @Test
    public void givenUSCensusData_WithWrongFile_ShouldThrowException() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser(CensusAnalyser.Country.INDIA);
            int numOfRecords = censusAnalyser.loadCSVCensusData(WRONG_CSV_FILE_PATH);
            Assert.assertEquals(51, numOfRecords);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM, e.type);
        }
    }

    @Test
    public void givenUSCSVFile_WhenHeaderNotAvailable_ThrowsException() {
        try {
            int numOfRecords = censusAnalyser.loadCSVCensusData(WRONG_HEADER_CSV_FILE_PATH);
            Assert.assertEquals(51, numOfRecords);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM, e.type);
        }
    }

    @Test
    public void givenUSCSVFile_WhenPassEmptyFile_ThrowsException() {
        try {
            int numOfRecords = censusAnalyser.loadCSVCensusData("");
            Assert.assertEquals(51, numOfRecords);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM, e.type);
        }
    }

    @Test
    public void givenUSCensusData_WithWrongDelimiter_ShouldThrowException() {
        try {
            int numOfRecords = censusAnalyser.loadCSVCensusData(WRONG_DELIMITER_CSV_FILE_PATH);
            Assert.assertEquals(51, numOfRecords);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM, e.type);
        }
    }
}
