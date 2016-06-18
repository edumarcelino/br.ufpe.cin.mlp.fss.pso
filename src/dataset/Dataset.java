package dataset;

import java.util.ArrayList;

public class Dataset {

    ArrayList<String[]> datasetTreino = new ArrayList<String[]>();
    ArrayList<String[]> datasetTeste = new ArrayList<String[]>();

//    List<>

    public Dataset() {

    }

    public ArrayList<String[]> getDatasetTreino() {
        return datasetTreino;
    }

    public void setDatasetTreino(ArrayList<String[]> datasetTreino) {
        this.datasetTreino = datasetTreino;
    }

    public ArrayList<String[]> getDatasetTeste() {
        return datasetTeste;
    }

    public void setDatasetTeste(ArrayList<String[]> datasetTeste) {
        this.datasetTeste = datasetTeste;
    }

}
