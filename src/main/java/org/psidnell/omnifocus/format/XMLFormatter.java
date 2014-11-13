package org.psidnell.omnifocus.format;

import java.io.IOException;
import java.io.Writer;

import org.psidnell.omnifocus.model.Node;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class XMLFormatter implements Formatter{

    private static XmlMapper MAPPER = new XmlMapper ();

    @Override
    public void format(Node node, Writer out)
            throws IOException {
        MAPPER.writeValue (out, node);
    }
}
