package censusanalyser;

import com.csvbuilder.CSVBuilderFactory;
import com.csvbuilder.CsvBuilderException;
import com.csvbuilder.ICSVBuilder;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.StreamSupport;

public class IndiaCensusAdapter extends CensusAdapter {
    Map<String, CSVCensusDAO> CensusDAOMap = new HashMap<>();

    @Override
    public Map<String, CSVCensusDAO> loadCensus(CensusAnalyser.Country country, String... filePath) throws CensusAnalyserException {
             CensusDAOMap = super.loadCensusCSVFileData(IndiaCensusCSV.class, filePath[0]);
             if(filePath.length>1)
                 this.loadIndiaSateCode(CensusDAOMap,filePath[1]);
             return loadCensusCSVFileData(IndiaCensusCSV.class,filePath);
    }

    public <E> Map<String, CSVCensusDAO> loadCensusCSVFileData(Class<E> censusCSVClass, String... csvFilePath) throws CensusAnalyserException {

        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath[0]))) {
            ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
            Iterator<E> censusList = csvBuilder.getCSVFileIterator(reader, censusCSVClass);
            Iterable<E> indiaCensusCSVS = () -> censusList;
            if (censusCSVClass.getName().equals("censusanalyser.IndiaCensusCSV")) {
                StreamSupport.stream(indiaCensusCSVS.spliterator(), false)
                        .map(IndiaCensusCSV.class::cast)
                        .forEach(csvCensus -> CensusDAOMap.put(csvCensus.state, new CSVCensusDAO(csvCensus)));
            } else if (censusCSVClass.getName().equals("censusanalyser.USCensusData")) {
                StreamSupport.stream(indiaCensusCSVS.spliterator(), false)
                        .map(USCensusData.class::cast)
                        .forEach(csvCensus -> CensusDAOMap.put(csvCensus.state, new CSVCensusDAO(csvCensus)));
            }
            if(csvFilePath.length==1)
                return CensusDAOMap;
            loadIndiaSateCode(CensusDAOMap,csvFilePath[0]);
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        } catch (CsvBuilderException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        } catch (RuntimeException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.SOME_OTHER_FILE_ERROR);
        }
        return CensusDAOMap;
    }

    public int loadIndiaSateCode(Map<String, CSVCensusDAO> CensusDAOMap, String csvFilePath) throws CensusAnalyserException {
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));) {
            ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
            Iterator<IndianStateCodeCSV> censusCSVIterator = csvBuilder.getCSVFileIterator(reader,
                    IndianStateCodeCSV.class);
            Iterable<IndianStateCodeCSV> indianStateCodeCSVS = () -> censusCSVIterator;
            StreamSupport.stream((indianStateCodeCSVS.spliterator()), false).filter(csvState -> CensusDAOMap.get(csvState.stateName) != null)
                    .forEach(scState -> CensusDAOMap.get(scState.stateName).StateCode = scState.stateCode);
            return CensusDAOMap.size();
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        } catch (CsvBuilderException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        }
    }

    public Map<String, CSVCensusDAO> loaderCensusData(CensusAnalyser.Country country, String[] csvFilePath) throws CensusAnalyserException {
        if (country.equals(CensusAnalyser.Country.INDIA))
            return this.loadCensusCSVFileData(IndiaCensusCSV.class, csvFilePath);
        else if (country.equals(CensusAnalyser.Country.USA))
            return this.loadCensusCSVFileData(USCensusData.class, csvFilePath);
        else throw new CensusAnalyserException("Incorrect Country", CensusAnalyserException.ExceptionType.INVALID_COUNTRY);
    }
}
