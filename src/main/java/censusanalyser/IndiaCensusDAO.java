package censusanalyser;

public class IndiaCensusDAO {
    public String state;
    public String StateCode;
    public int areaInSqKm;
    public int population;
    public int densityPerSqKm;

    public IndiaCensusDAO(IndiaCensusCSV indianCensusCSV) {
        state = indianCensusCSV.state;
        areaInSqKm = indianCensusCSV.areaInSqKm;
        population = indianCensusCSV.population;
        densityPerSqKm = indianCensusCSV.densityPerSqKm;
    }
}
