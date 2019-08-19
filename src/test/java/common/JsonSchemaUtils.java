package common;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jackson.JsonNodeReader;
import com.github.fge.jsonschema.core.report.ProcessingMessage;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import org.testng.Assert;
import org.testng.Reporter;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class JsonSchemaUtils {

    private static JsonNode readJSONfile(String filePath) throws IOException {
        URL fileUrl = JsonSchemaUtils.class.getResource(filePath);
        InputStream stream = JsonSchemaUtils.class.getResourceAsStream(filePath);
        return new JsonNodeReader().fromInputStream(stream);
    }

    private static JsonNode readJSONStr(String str) throws IOException {
        return new ObjectMapper().readTree(str);
    }
    private static void assertJsonSchema(JsonNode schema, JsonNode data) {
        ProcessingReport report = JsonSchemaFactory.byDefault().getValidator().validateUnchecked(schema, data);
        if (!report.isSuccess()) {
            for (ProcessingMessage aReport : report) {
                Reporter.log(aReport.getMessage(), true);
            }
        }
        Assert.assertTrue(report.isSuccess());
    }

    public static void assertResponseJsonSchema(String schemaPath, String response) throws IOException {
        JsonNode jsonSchema = readJSONfile(schemaPath);
        JsonNode responseJN = readJSONStr(response);
        assertJsonSchema(jsonSchema, responseJN);
    }
}

