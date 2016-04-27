package it.sapienza.pervasivesystems.smartmuseum.neo4j;

import android.text.TextUtils;

/**
 * Collection of static helper methods related to Cypher.
 */
public class CypherUtils {

    public static String toGraph(String startNode, String relationship, String endNode) {
        return String.format("(%s)-[%s]-(%s)", startNode, relationship, endNode);
    }

    private static String toQuote(final String value) {
        return "\"" + value + "\"";
    }

    private static String toNameValue(final String name, final String value) {
        return toQuote(name) + " : " + toQuote(value);
    }

    private static String toBlock(String value) {
        return "{ " + value + " }";
    }

    public static String toComma(String... args) {
        return TextUtils.join(", ", args);
    }

    private static String toNameValueBlock(String name, String value) {
        return "{ " + toNameValue(name, value) + " }";
    }

}
