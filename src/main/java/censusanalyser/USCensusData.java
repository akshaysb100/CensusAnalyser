package censusanalyser;

import com.opencsv.bean.CsvBindByName;

public class USCensusData {

    @CsvBindByName(column = "StateId", required = true)
    public String state;

    @CsvBindByName(column = "State", required = true)
    public String stateId;

    @CsvBindByName(column = "Population", required = true)
    public  int population;

    @CsvBindByName(column = "Total area", required = true)
    public Double totalArea;

    @CsvBindByName(column = "Population Density", required = true)
    public Double populationDensity;

    @CsvBindByName(column = "Housing Density", required = true)
    public Double housingDensity;

    @CsvBindByName(column = "Land area", required = true)
    public Double landArea;

    @CsvBindByName(column = "waterArea", required = true)
    public Double waterArea;


}
