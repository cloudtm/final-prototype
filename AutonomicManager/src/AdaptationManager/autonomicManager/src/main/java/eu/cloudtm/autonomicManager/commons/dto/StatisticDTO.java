package eu.cloudtm.autonomicManager.commons.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by: Fabio Perfetti
 * E-mail: perfabio87@gmail.com
 * Date: 6/10/13
 */
public class StatisticDTO {

    private String param;
    private List<List<Double>> data = new ArrayList<List<Double>>();

    public StatisticDTO(String _param){
        this.param = _param;
    }

    public void addPoint(long x, Double y){
        List<Double> point = new ArrayList();
        point.add( new Double(x) );
        point.add(y);
        data.add(point);
    }

    public List<List<Double>> getData(){
        return data;
    }
}
