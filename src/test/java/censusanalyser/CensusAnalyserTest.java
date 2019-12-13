package censusanalyser;

import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class CensusAnalyserTest {

    private static final String INDIA_CENSUS_CSV_FILE_PATH = "./src/test/resources/IndiaStateCensusData.csv";
    private static final String WRONG_CSV_FILE_PATH = "./src/main/resources/IndiaStateCensusData.csv";
    private static final String INDIA_CODE_CSV_FILE_PATH = "./src/test/resources/IndiaStateCode.csv";
    private static final String WRONG_DELIMITER_CSV_FILE_PATH = "./src/test/resources/delimiterWrong.csv";
    private static final String WRONG_HEADER_CSV_FILE_PATH = "./src/test/resources/headerWrong.csv";
    private static final String US_CENSUS_CSV_FILE_PATH = "./src/test/resources/USCensusData.csv";

    @Test
    public void givenIndianStateData_SortedOnState_ShouldReturnSortedResult() {
        CensusAnalyser censusAnalyser = new CensusAnalyser(CensusAnalyser.Country.INDIA);
        String sortedData = null;
        try {
            censusAnalyser.loadCSVCensusData(INDIA_CENSUS_CSV_FILE_PATH, INDIA_CODE_CSV_FILE_PATH);
            sortedData = censusAnalyser.getSortedDataBasedOnState(SortDataBaseOnField.STATE);
            IndiaCensusCSV[] censusCSV = new Gson().fromJson(sortedData, IndiaCensusCSV[].class);
            Assert.assertEquals("Andhra Pradesh", censusCSV[0].state);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.SOME_OTHER_FILE_ERROR, e.type);
        }
    }

    @Test
    public void givenIndianStateData_NotLoadFile_SortedOnState_ShouldThrowException() {
        CensusAnalyser censusAnalyser = new CensusAnalyser(CensusAnalyser.Country.INDIA);
        String sortedData = null;
        try {
            sortedData = censusAnalyser.getSortedDataBasedOnState(SortDataBaseOnField.STATE);
            IndiaCensusCSV[] censusCSV = new Gson().fromJson(sortedData, IndiaCensusCSV[].class);
            Assert.assertEquals("Andhra Pradesh", censusCSV[0].state);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM, e.type);
        }
    }

    @Test
    public void givenIndianStateData_SortedOnState_IfPassWrongFile_ShouldThrowException() {
        CensusAnalyser censusAnalyser = new CensusAnalyser(CensusAnalyser.Country.INDIA);
        String sortedData = null;
        try {
            censusAnalyser.loadCSVCensusData(INDIA_CODE_CSV_FILE_PATH, WRONG_CSV_FILE_PATH);
            sortedData = censusAnalyser.getSortedDataBasedOnState(SortDataBaseOnField.STATE);
            IndiaCensusCSV[] censusCSV = new Gson().fromJson(sortedData, IndiaCensusCSV[].class);
            Assert.assertEquals("Andhra Pradesh", censusCSV[0].state);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM, e.type);
        }
    }

    @Test
    public void givenIndianStateData_SortedOnPopulation_ShouldReturnSortedResult() {
        CensusAnalyser censusAnalyser = new CensusAnalyser(CensusAnalyser.Country.INDIA);
        String sortedData = null;
        try {
            censusAnalyser.loadCSVCensusData(INDIA_CENSUS_CSV_FILE_PATH, INDIA_CODE_CSV_FILE_PATH);
            sortedData = censusAnalyser.getSortedDataBasedOnState(SortDataBaseOnField.POPULATION);
            IndiaCensusCSV[] censusCSV = new Gson().fromJson(sortedData, IndiaCensusCSV[].class);
            Assert.assertEquals(199812341, censusCSV[0].population);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.SOME_OTHER_FILE_ERROR, e.type);
        }
    }

    @Test
    public void givenIndianStateData_SortedOnArea_In_Sq_Km_ShouldReturnSortedResult() {
        CensusAnalyser censusAnalyser = new CensusAnalyser(CensusAnalyser.Country.INDIA);
        String sortedData = null;
        try {
            censusAnalyser.loadCSVCensusData(INDIA_CENSUS_CSV_FILE_PATH, INDIA_CODE_CSV_FILE_PATH);
            sortedData = censusAnalyser.getSortedDataBasedOnState(SortDataBaseOnField.AREA_IN_SQ_KM);
            IndiaCensusCSV[] censusCSV = new Gson().fromJson(sortedData, IndiaCensusCSV[].class);
            Assert.assertEquals(342239, censusCSV[0].areaInSqKm);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.SOME_OTHER_FILE_ERROR, e.type);
        }
    }

    @Test
    public void givenIndianStateData_SortedOnArea_IfPassNullFile_ShouldThrowException() {
        CensusAnalyser censusAnalyser = new CensusAnalyser(CensusAnalyser.Country.INDIA);
        String sortedData = null;
        try {
            censusAnalyser.loadCSVCensusData("");
            sortedData = censusAnalyser.getSortedDataBasedOnState(SortDataBaseOnField.AREA_IN_SQ_KM);
            IndiaCensusCSV[] censusCSV = new Gson().fromJson(sortedData, IndiaCensusCSV[].class);
            Assert.assertEquals(162968, censusCSV[0].areaInSqKm);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM, e.type);
        }
    }

    @Test
    public void givenIndiaCensusData_WithWrongHeader_ShouldThrowException() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser(CensusAnalyser.Country.INDIA);
            ExpectedException exceptionRule = ExpectedException.none();
            exceptionRule.expect(CensusAnalyserException.class);
            censusAnalyser.loadCSVCensusData(WRONG_HEADER_CSV_FILE_PATH);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM, e.type);
        }
    }

    @Test
    public void givenIndianStateData_SortedOnDensityPerSqKm_ShouldReturnSortedResult() {
        CensusAnalyser censusAnalyser = new CensusAnalyser(CensusAnalyser.Country.INDIA);
        String sortedData = null;
        try {
            censusAnalyser.loadCSVCensusData(INDIA_CENSUS_CSV_FILE_PATH, INDIA_CODE_CSV_FILE_PATH);
            sortedData = censusAnalyser.getSortedDataBasedOnState(SortDataBaseOnField.STATE);
            IndiaCensusCSV[] censusCSV = new Gson().fromJson(sortedData, IndiaCensusCSV[].class);
            Assert.assertEquals(303, censusCSV[0].densityPerSqKm);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.SOME_OTHER_FILE_ERROR, e.type);
        }
    }

    @Test
    public void givenIndiaCensusData_WithWrongDelimiter_ShouldThrowException() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser(CensusAnalyser.Country.INDIA);
            ExpectedException exceptionRule = ExpectedException.none();
            exceptionRule.expect(CensusAnalyserException.class);
            censusAnalyser.loadCSVCensusData(WRONG_DELIMITER_CSV_FILE_PATH, WRONG_DELIMITER_CSV_FILE_PATH);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM, e.type);
        }
    }

    @Test
    public void givenUSStateData_IfPassNullFile_ShouldThrowException() {
        CensusAnalyser censusAnalyser = new CensusAnalyser(CensusAnalyser.Country.INDIA);
        String sortedData = null;
        try {
            censusAnalyser.loadCSVCensusData("");
            sortedData = censusAnalyser.getSortedDataBasedOnState(SortDataBaseOnField.AREA_IN_SQ_KM);
            IndiaCensusCSV[] censusCSV = new Gson().fromJson(sortedData, IndiaCensusCSV[].class);
            Assert.assertEquals(162968, censusCSV[0].areaInSqKm);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM, e.type);
        }
    }

    @Test
    public void givenUSStateData_SortedOnState_ShouldReturnSortedResult() {
        CensusAnalyser censusAnalyser = new CensusAnalyser(CensusAnalyser.Country.USA);
        String sortedData = null;
        try {
            censusAnalyser.loadCSVCensusData(US_CENSUS_CSV_FILE_PATH);
            sortedData = censusAnalyser.getSortedDataBasedOnState(SortDataBaseOnField.STATE);
            USCensusData[] censusCSV = new Gson().fromJson(sortedData, USCensusData[].class);
            Assert.assertEquals("Alabama", censusCSV[0].state);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.SOME_OTHER_FILE_ERROR, e.type);
        }
    }

    @Test
    public void givenUSStateData_NotLoadFile_SortedOnState_ShouldThrowException() {
        CensusAnalyser censusAnalyser = new CensusAnalyser(CensusAnalyser.Country.INDIA);
        String sortedData = null;
        try {
            sortedData = censusAnalyser.getSortedDataBasedOnState(SortDataBaseOnField.STATE);
            USCensusData[] censusCSV = new Gson().fromJson(sortedData, USCensusData[].class);
            Assert.assertEquals("Alabama", censusCSV[0].state);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM, e.type);
        }
    }

    @Test
    public void givenUSStateData_SortedOnPopulation_ShouldReturnSortedResult() {
        CensusAnalyser censusAnalyser = new CensusAnalyser(CensusAnalyser.Country.USA);
        String sortedData = null;
        try {
            censusAnalyser.loadCSVCensusData(US_CENSUS_CSV_FILE_PATH);
            sortedData = censusAnalyser.getSortedDataBasedOnState(SortDataBaseOnField.POPULATION);
            USCensusData[] censusCSV = new Gson().fromJson(sortedData, USCensusData[].class);
            Assert.assertEquals(37253956, censusCSV[0].population);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.SOME_OTHER_FILE_ERROR, e.type);
        }
    }

    @Test
    public void givenUSStateData_SortedOnPopulationDensity_ShouldReturnSortedResult() {
        CensusAnalyser censusAnalyser = new CensusAnalyser(CensusAnalyser.Country.USA);
        String sortedData = null;
        try {
            censusAnalyser.loadCSVCensusData(US_CENSUS_CSV_FILE_PATH);
            sortedData = censusAnalyser.getSortedDataBasedOnState(SortDataBaseOnField.POPULATION_DENSITY);
            USCensusData[] censusCSV = new Gson().fromJson(sortedData, USCensusData[].class);
            Assert.assertEquals("District of Columbia", censusCSV[0].state);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.SOME_OTHER_FILE_ERROR, e.type);
        }
    }

    @Test
    public void givenUSStateData_SortedFirstPopulationBasedAnd_ThenPopulationDensity_ShouldReturnSortedResult() {
        CensusAnalyser censusAnalyser = new CensusAnalyser(CensusAnalyser.Country.USA);
        String sortedData = null;
        try {
            censusAnalyser.loadCSVCensusData(US_CENSUS_CSV_FILE_PATH);
            sortedData = censusAnalyser.getSortedDataBasedOnState(SortDataBaseOnField.POPULATION_AND_DENSITY);
            USCensusData[] censusCSV = new Gson().fromJson(sortedData, USCensusData[].class);
            Assert.assertEquals("California", censusCSV[0].state);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.SOME_OTHER_FILE_ERROR, e.type);
        }
    }
}
