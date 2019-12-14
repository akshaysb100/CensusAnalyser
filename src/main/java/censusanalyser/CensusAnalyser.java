package censusanalyser;

import com.google.gson.Gson;

import java.util.*;

import static java.util.stream.Collectors.toCollection;

public class CensusAnalyser<E> {

    public enum Country {
        INDIA, USA
    }

    private Country country;
    Map<String, CSVCensusDAO> censusDAOMap = new HashMap<String, CSVCensusDAO>();
    Map<SortDataBaseOnField, Comparator<CSVCensusDAO>> fields = null;

    public CensusAnalyser(Country country) {
        this.country = country;
        this.fields = new HashMap<>();
        this.fields.put(SortDataBaseOnField.STATE, Comparator.comparing(census -> census.state));
        this.fields.put(SortDataBaseOnField.POPULATION, Comparator.comparing(census -> census.population, Comparator.reverseOrder()));
        this.fields.put(SortDataBaseOnField.AREA_IN_SQ_KM, Comparator.comparing(census -> census.areaInSqKm, Comparator.reverseOrder()));
        this.fields.put(SortDataBaseOnField.DENSITY_PER_SQ_KMS, Comparator.comparing(census -> census.densityPerSqKm, Comparator.reverseOrder()));
        this.fields.put(SortDataBaseOnField.POPULATION_DENSITY, Comparator.comparing(census -> census.populationDensity, Comparator.reverseOrder()));
        Comparator<CSVCensusDAO> comp = Comparator.comparing(censusDAO -> censusDAO.population, Comparator.reverseOrder());
        this.fields.put(SortDataBaseOnField.POPULATION_AND_DENSITY, comp.thenComparing(censusDAO -> censusDAO.populationDensity, Comparator.reverseOrder()));
    }

    public int loadCSVCensusData(String... csvFilePath) throws CensusAnalyserException {
        CensusAdapter censusAdapter = CensusAdapterFactory.getCensusDataObject(country);
        censusDAOMap = censusAdapter.loadCensus(csvFilePath);
        return censusDAOMap.size();
    }

    public String getSortedDataBasedOnState(SortDataBaseOnField fieldName) throws CensusAnalyserException {
        if (censusDAOMap == null || censusDAOMap.size() == 0) {
            throw new CensusAnalyserException("No Census Data", CensusAnalyserException
                    .ExceptionType.CENSUS_FILE_PROBLEM);
        }
        ArrayList arrayList = censusDAOMap.values().stream()
                .sorted(this.fields.get(fieldName))
                .map(censusDAO -> censusDAO.getCensusDTO(country))
                .collect(toCollection(ArrayList::new));
        String sortedStateCensus = new Gson().toJson(arrayList);
        return sortedStateCensus;
    }
}
