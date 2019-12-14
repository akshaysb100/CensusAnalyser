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

public abstract class CensusAdapter {

    public abstract Map<String, CSVCensusDAO> loadCensus(String... filePath) throws CensusAnalyserException;

    Map<String, CSVCensusDAO> CensusDAOMap = new HashMap<>();

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
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        } catch (CsvBuilderException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        } catch (RuntimeException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        }
        return CensusDAOMap;
    }
}
