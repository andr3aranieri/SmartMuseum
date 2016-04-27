package it.sapienza.pervasivesystems.smartmuseum.neo4j;

/**
 * Object to represent a row of data from Neo4j graph, either column or object based.
 */
public class CypherRow<T> {

    T row;

    public CypherRow(T dataset) {
        this.row = dataset;
    }

    public T getRow() {
        return row;
    }

    @Override
    public String toString() {
        return "CypherRow{" +
                "row=" + row +
                '}';
    }
}
