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
    public Map<String, CSVCensusDAO> loadCensus(String... filePath) throws CensusAnalyserException {
        CensusDAOMap = super.loadCensusCSVFileData(IndiaCensusCSV.class, filePath[0]);
        this.loadIndiaSateCode(CensusDAOMap, filePath[1]);
        return loadCensusCSVFileData(IndiaCensusCSV.class, filePath);
    }

    public int loadIndiaSateCode(Map<String, CSVCensusDAO> CensusDAOMap, String csvFilePath) throws CensusAnalyserException {
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));) {
            ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
            Iterator<IndianStateCodeCSV> censusCSVIterator = csvBuilder.getCSVFileIterator(reader, IndianStateCodeCSV.class);
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
        } catch (RuntimeException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        }
    }
}
