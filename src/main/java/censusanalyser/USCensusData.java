package censusanalyser;

import com.opencsv.bean.CsvBindByName;

public class USCensusData {

    @CsvBindByName(column = "State", required = true)
    public String state;

    @CsvBindByName(column = "StateId", required = true)
    public String stateId;

    @CsvBindByName(column = "Population", required = true)
    public int population;

    @CsvBindByName(column = "TotalArea", required = true)
    public Double totalArea;

    @CsvBindByName(column = "PopulationDensity", required = true)
    public Double populationDensity;

    public USCensusData() {
    }

    public USCensusData(String state, String stateId, int population, Double totalArea, Double populationDensity) {
        this.state = state;
        this.stateId = stateId;
        this.population = population;
        this.totalArea = totalArea;
        this.populationDensity = populationDensity;
    }
}
