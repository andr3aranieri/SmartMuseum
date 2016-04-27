package it.sapienza.pervasivesystems.smartmuseum.neo4j;

import java.util.List;

/**
 * Cypher response object.
 */
public class CypherNodeSet<T> {

    List<String> columns;
    List<CypherRow<T>> data;

    public CypherNodeSet(List<String> columns, List<CypherRow<T>> data) {
        this.columns = columns;
        this.data = data;
    }

    @Override
    public String toString() {
        return "CypherNodeSet{" +
                "columns=" + columns +
                ", data=" + data +
                '}';
    }

    public List<CypherRow<T>> getData() {
        return data;
    }

    public void setData(List<CypherRow<T>> data) {
        this.data = data;
    }
}
