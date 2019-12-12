package censusanalyser;

import com.csvbuilder.CSVBuilderFactory;
import com.csvbuilder.CsvBuilderException;
import com.csvbuilder.ICSVBuilder;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class CensusAnalyser<E> {

    Map<String, IndiaCensusDAO> indiaCensusDAOMap = null;
    Map<SortDataBaseOnField, Comparator<IndiaCensusDAO>> fields = null;

    public CensusAnalyser() {
        this.indiaCensusDAOMap = new HashMap<String, IndiaCensusDAO>();
        this.fields = new HashMap<>();
        this.fields.put(SortDataBaseOnField.state, Comparator.comparing(census -> census.state));
        this.fields.put(SortDataBaseOnField.POPULATION, Comparator.comparing(census -> census.population, Comparator.reverseOrder()));
        this.fields.put(SortDataBaseOnField.AREA_IN_SQ_KM, Comparator.comparing(census -> census.areaInSqKm, Comparator.reverseOrder()));
        this.fields.put(SortDataBaseOnField.DENSITY_PER_SQ_KMS, Comparator.comparing(census -> census.densityPerSqKm, Comparator.reverseOrder()));
    }

    public int loadIndiaCensusData(String csvFilePath) throws CensusAnalyserException {
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));) {
            ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
            Iterator<IndiaCensusCSV> censusList = csvBuilder.getCSVFileIterator(reader, IndiaCensusCSV.class);
            Iterable<IndiaCensusCSV> indiaCensusCSVS = () -> censusList;
            StreamSupport.stream(indiaCensusCSVS.spliterator(), false).
                    forEach(csvCensus -> this.indiaCensusDAOMap.put(csvCensus.state, new IndiaCensusDAO(csvCensus)));
            return this.indiaCensusDAOMap.size();
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
    }

    public int loadIndiaSateCode(String csvFilePath) throws CensusAnalyserException {
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));) {
            ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
            Iterator<IndianStateCodeCSV> censusCSVIterator = csvBuilder.getCSVFileIterator(reader,
                    IndianStateCodeCSV.class);
            while (censusCSVIterator.hasNext()) {
                IndianStateCodeCSV censusCSV = censusCSVIterator.next();
                IndiaCensusDAO indiaCensusDAO = indiaCensusDAOMap.get(censusCSV.stateCode);
                if (indiaCensusDAO == null) continue;
                indiaCensusDAO.StateCode = censusCSV.stateCode;
            }
            return indiaCensusDAOMap.size();

        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        } catch (CsvBuilderException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        }
    }

    public String getSortedDataBasedOnState(SortDataBaseOnField fieldName) throws CensusAnalyserException {

        if (indiaCensusDAOMap == null || indiaCensusDAOMap.size() == 0) {
            throw new CensusAnalyserException("No Census Data", CensusAnalyserException
                    .ExceptionType.CENSUS_FILE_PROBLEM);
        }
        List<IndiaCensusDAO> daos = indiaCensusDAOMap.values().stream().collect(Collectors.toList());
        this.sort(daos, this.fields.get(fieldName));
        String sortedStateCensus = new Gson().toJson(daos);
        return sortedStateCensus;
    }

    private void sort(List<IndiaCensusDAO> censusDAOS, Comparator<IndiaCensusDAO> censusCSVComparator) {
        for (int i = 0; i < censusDAOS.size(); i++) {
            for (int j = 0; j < censusDAOS.size() - i - 1; j++) {
                IndiaCensusDAO censusCSV = censusDAOS.get(j);
                IndiaCensusDAO censusCSV1 = censusDAOS.get(j + 1);
                if (censusCSVComparator.compare(censusCSV, censusCSV1) > 0) {
                    censusDAOS.set(j, censusCSV1);
                    censusDAOS.set(j + 1, censusCSV);
                }
            }
        }
    }
}
