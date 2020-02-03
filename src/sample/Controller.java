package sample;

import sample.Model.DataRetrieval;

public class Controller {
    void initialize(){
        //DataRetrieval.GenerateLink();
    }

    public Controller() {
        String asd = "asda";
        String ds = "34sd" + asd;
        //DataRetrieval.GenerateLink();
        var links = DataRetrieval.GenerateLink();
        //DataRetrieval.ObtainHtml("https://avalanche.pc.gc.ca/station-eng.aspx?d=2011-09-10");
    }
}
