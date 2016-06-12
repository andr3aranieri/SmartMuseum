package it.sapienza.pervasivesystems.smartmuseum.neo4j;

import android.util.Base64;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Cypher service
 */
public class CypherService {

    protected static final String SERVER_ROOT_URI = "http://ec2-52-50-0-114.eu-west-1.compute.amazonaws.com:4126";
    protected static final String USERNAME = "neo4j";
    protected static final String PASSWORD = "andreaguamaral4123";

    private static final String[] DATE_FORMATS = new String[]{
            "yyyy-MM-dd'T'HH:mm:ssZ",
            "yyyy-MM-dd"
    };

    private static class DateDeserializer implements JsonDeserializer<Date> {
        @Override
        public Date deserialize(JsonElement jsonElement, Type typeOF,
                                JsonDeserializationContext context) throws JsonParseException {
            for (String format : DATE_FORMATS) {
                try {
                    return new SimpleDateFormat(format, Locale.US).parse(jsonElement.getAsString());
                } catch (ParseException e) {
                }
            }
            throw new JsonParseException("Unparseable date: \"" + jsonElement.getAsString()
                    + "\". Supported formats: " + Arrays.toString(DATE_FORMATS));
        }
    }

    public static class CypherRowDeserializer<T> implements JsonDeserializer<CypherRow<T>> {

        JsonDeserializer<T> delegate;

        public CypherRowDeserializer(JsonDeserializer<T> delegate) {
            this.delegate = delegate;
        }

        @Override
        public CypherRow<T> deserialize
                (JsonElement jElement, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            JsonObject jObject = jElement.getAsJsonObject();
            JsonArray row = jObject.getAsJsonArray("row");
            if (row.get(0).isJsonPrimitive()) {
                System.out.println("COLUMN ResetSet");
            }
            if (row.get(0).isJsonObject()) {
                System.out.println("OBJECT ResetSet");
            }
            CypherRow<T> result = new CypherRow<>(delegate.deserialize(jElement, typeOfT, context));
            return result;
        }
    }

    protected final String credentials;
    protected final RestAdapter.Builder restAdaptorBuilder;
    protected final GsonBuilder gsonBuilder = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
            .registerTypeAdapter(Date.class, new DateDeserializer());

    protected final Gson gson;
    protected final String serverRootUri;
    protected final Neo4jRestService neo4j;

    public CypherService() {
        this(SERVER_ROOT_URI, USERNAME, PASSWORD);
    }

    public CypherService(String serverRootUri, String username, String password) {
        this(new GsonBuilder()
                        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                        .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                        .registerTypeAdapter(Date.class, new DateDeserializer())
                        .create(),
                serverRootUri,
                username,
                password
        );
    }

    public CypherService(Gson gson, String serverRootUri, String username, String password) {
        this.serverRootUri = serverRootUri;
        this.gson = gson;

        byte[] credentialsByteArray = (username + ":" + password).getBytes();
        this.credentials = Base64.encodeToString(credentialsByteArray, Base64.DEFAULT);

        RequestInterceptor userAgentInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("User-Agent", "CypherService");
            }
        };

        RequestInterceptor authInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("Authorization", "Basic " + credentials);
            }
        };

        this.restAdaptorBuilder = new RestAdapter.Builder()
                .setEndpoint(this.serverRootUri)
                .setRequestInterceptor(userAgentInterceptor)
                .setRequestInterceptor(authInterceptor)
                .setLogLevel(RestAdapter.LogLevel.FULL);

        RestAdapter restAdapter = this.restAdaptorBuilder
                .setConverter(new GsonConverter(this.gson))
                .build();

        this.neo4j = restAdapter.create(Neo4jRestService.class);
    }

    public int getServerStatus() {
        return neo4j.getStatus().getStatus();
    }

    public Gson getGson() {
        return gson;
    }

    public Neo4jRestService getNeo4jRestService() {
        return neo4j;
    }

    public <T> Neo4jRestService getNeo4jColumnResponse(JsonDeserializer<T> factory) {

        CypherRowDeserializer<T> deserializer = new CypherRowDeserializer<>(factory);

        Gson gsonWithCustomPojoFactory = this.gsonBuilder
                .registerTypeAdapter(CypherRow.class, deserializer)
                .create();

        RestAdapter restAdapter = this.restAdaptorBuilder
                .setConverter(new GsonConverter(gsonWithCustomPojoFactory))
                .build();

        return restAdapter.create(Neo4jRestService.class);
    }


    // ------------------------------------------------------------
    // CYPHER QUERIES
    // ------------------------------------------------------------

    public <T> CypherResultSet<T> runCypher(String query) {
        return runCypher(CypherStatement.Builder().withCypher(query).build());
    }

    public <T> CypherResultSet<T> runCypher(CypherStatement statement) {
        return runCypher(CypherRequest.Builder().withStatement(statement).build());
    }

    public <T> CypherResultSet<T> runCypher(CypherRequest request) {
        final CypherResultSet<T> response;
        try {
            response = neo4j.getCypherResultSet(request);
        } catch (Exception e) {
            throw e;
        }
        return response;
    }

//    public String loadQuery(String filename) {
//        String query = "";
//        try {
//            Path path = Paths.get(getClass().getClassLoader().getResource(filename).toURI());
//            query = String.join(" ", Files.readAllLines(path));
//        } catch (Exception err) {
//            Log.e("ANDREA", "Failed to load cypher query file " + filename, err);
//        }
//        return query;
//    }
//
//    public int runCypherFile(String filename) {
//        Arrays.asList(loadQuery(filename).split(";"))
//                .stream()
//                .filter(query -> !query.trim().isEmpty())
//                .forEach(query -> runCypher(query));
//        return HttpStatus.OK_200;
//    }
}
