package org.psidnell.omnifocus.cli;

import java.io.IOException;

import javax.script.ScriptException;

import org.apache.commons.cli.ParseException;

public interface ActiveOptionProcess<P> {
    void process (P program, ActiveOption<P> o) throws IOException, ScriptException, ParseException;
}