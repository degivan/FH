package spbau.mit.divan.foodhunter.net;

import java.io.Serializable;

public class Query implements Serializable {
    private String query;
    private Boolean isPlace;

    public Query(String query, Boolean isPlace) {
        this.query = query;
        this.isPlace = isPlace;
    }

    public String getQuery() {
        return query;
    }


    public Boolean isPlace() {
        return isPlace;
    }
}
