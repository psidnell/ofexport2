package org.psidnell.omnifocus.format;

import java.io.IOException;
import java.io.Writer;

import org.psidnell.omnifocus.model.Node;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONFormatter implements Formatter {

    private static ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public void format(Node node, Writer out) throws IOException {
        MAPPER.writerWithDefaultPrettyPrinter().writeValue(out, node);
    }
}
