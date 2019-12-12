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

public class CensusLoader {

    public <E> Map<String, IndiaCensusDAO> loadCensusCSVFileData(String csvFilePath, Class<E> censusCSVClass) throws CensusAnalyserException {
        Map<String, IndiaCensusDAO> CensusDAOMap = new HashMap<>();
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));) {
            ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
            Iterator<E> censusList = csvBuilder.getCSVFileIterator(reader, censusCSVClass);
            Iterable<E> indiaCensusCSVS = () -> censusList;
            if (censusCSVClass.getName().equals("censusanalyser.IndiaCensusCSV")) {
                StreamSupport.stream(indiaCensusCSVS.spliterator(), false)
                        .map(IndiaCensusCSV.class::cast)
                        .forEach(csvCensus -> CensusDAOMap.put(csvCensus.state, new IndiaCensusDAO(csvCensus)));
            } else if (censusCSVClass.getName().equals("censusanalyser.USCensusData")) {
                StreamSupport.stream(indiaCensusCSVS.spliterator(), false)
                        .map(USCensusData.class::cast)
                        .forEach(csvCensus -> CensusDAOMap.put(csvCensus.state, new IndiaCensusDAO(csvCensus)));
            }

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

}
