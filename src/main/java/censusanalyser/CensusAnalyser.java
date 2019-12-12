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

    Map<String, IndiaCensusDAO> CensusDAOMap = null;
    Map<SortDataBaseOnField, Comparator<IndiaCensusDAO>> fields = null;

    public CensusAnalyser() {
        this.CensusDAOMap = new HashMap<String, IndiaCensusDAO>();
        this.fields = new HashMap<>();
        this.fields.put(SortDataBaseOnField.state, Comparator.comparing(census -> census.state));
        this.fields.put(SortDataBaseOnField.POPULATION, Comparator.comparing(census -> census.population, Comparator.reverseOrder()));
        this.fields.put(SortDataBaseOnField.AREA_IN_SQ_KM, Comparator.comparing(census -> census.areaInSqKm, Comparator.reverseOrder()));
        this.fields.put(SortDataBaseOnField.DENSITY_PER_SQ_KMS, Comparator.comparing(census -> census.densityPerSqKm, Comparator.reverseOrder()));
    }

    public int loadIndiaCensusData(String csvFilePath) throws CensusAnalyserException {
        return this.loadCensusCSVFileData(csvFilePath, IndiaCensusCSV.class);
    }

    public <E> int loadCensusCSVFileData(String csvFilePath, Class<E> censusCSVClass) throws CensusAnalyserException {
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));) {
            ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
            Iterator<E> censusList = csvBuilder.getCSVFileIterator(reader, censusCSVClass);
            Iterable<E> indiaCensusCSVS = () -> censusList;
            if (censusCSVClass.getName().equals("censusanalyser.IndiaCensusCSV")) {
                StreamSupport.stream(indiaCensusCSVS.spliterator(), false)
                        .map(IndiaCensusCSV.class::cast)
                        .forEach(csvCensus -> this.CensusDAOMap.put(csvCensus.state, new IndiaCensusDAO(csvCensus)));
            } else if (censusCSVClass.getName().equals("censusanalyser.USCensusData")) {
                StreamSupport.stream(indiaCensusCSVS.spliterator(), false)
                        .map(USCensusData.class::cast)
                        .forEach(csvCensus -> this.CensusDAOMap.put(csvCensus.state, new IndiaCensusDAO(csvCensus)));
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
        return this.CensusDAOMap.size();
    }

    public int loadIndiaSateCode(String csvFilePath) throws CensusAnalyserException {
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));) {
            ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
            Iterator<IndianStateCodeCSV> censusCSVIterator = csvBuilder.getCSVFileIterator(reader,
                    IndianStateCodeCSV.class);
            Iterable<IndianStateCodeCSV> indianStateCodeCSVS = () -> censusCSVIterator;
            StreamSupport.stream((indianStateCodeCSVS.spliterator()), false).filter(csvState -> CensusDAOMap.get(csvState.stateName) != null)
                    .forEach(scState -> CensusDAOMap.get(scState.stateName).StateCode = scState.stateCode);
            return this.CensusDAOMap.size();
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        } catch (CsvBuilderException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        }
    }

    public String getSortedDataBasedOnState(SortDataBaseOnField fieldName) throws CensusAnalyserException {
        if (CensusDAOMap == null || CensusDAOMap.size() == 0) {
            throw new CensusAnalyserException("No Census Data", CensusAnalyserException
                    .ExceptionType.CENSUS_FILE_PROBLEM);
        }
        List<IndiaCensusDAO> indiaCensusDAOS = CensusDAOMap.values().stream().collect(Collectors.toList());
        this.sort(indiaCensusDAOS, this.fields.get(fieldName));
        String sortedStateCensus = new Gson().toJson(indiaCensusDAOS);
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

    public int loadUCCensusData(String ucCensusCsvFilePath) throws CensusAnalyserException {
        return this.loadCensusCSVFileData(ucCensusCsvFilePath, USCensusData.class);
    }
}
