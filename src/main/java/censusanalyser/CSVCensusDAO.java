package censusanalyser;

public class CSVCensusDAO {
    public String state;
    public String StateCode;
    public int areaInSqKm;
    public int population;
    public int densityPerSqKm;
    public String stateId;
    public Double populationDensity;
    public Double totalArea;

    public CSVCensusDAO(IndiaCensusCSV indianCensusCSV) {
        state = indianCensusCSV.state;
        areaInSqKm = indianCensusCSV.areaInSqKm;
        population = indianCensusCSV.population;
        densityPerSqKm = indianCensusCSV.densityPerSqKm;
    }

    public CSVCensusDAO(USCensusData csvCensus) {
        state = csvCensus.state;
        stateId = csvCensus.stateId;
        population = csvCensus.population;
        populationDensity = csvCensus.populationDensity;
        totalArea = csvCensus.totalArea;
    }
}
