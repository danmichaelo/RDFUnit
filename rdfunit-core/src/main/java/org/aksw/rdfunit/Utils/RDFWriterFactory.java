package org.aksw.rdfunit.Utils;

import org.aksw.rdfunit.enums.TestCaseExecutionType;
import org.aksw.rdfunit.io.format.FormatService;
import org.aksw.rdfunit.io.format.SerializationFormat;
import org.aksw.rdfunit.io.writer.*;

import java.io.OutputStream;

/**
 * <p>RDFWriterFactory class.</p>
 *
 * @author Dimitris Kontokostas
 *         Description
 * @since 6/19/14 9:20 AM
 * @version $Id: $Id
 */
public final class RDFWriterFactory {
    private RDFWriterFactory() {
    }

    /**
     * <p>createWriterFromFormat.</p>
     *
     * @param filenameWithoutExtension a {@link java.lang.String} object.
     * @param serializationFormat a {@link org.aksw.rdfunit.io.format.SerializationFormat} object.
     * @param executionType a {@link org.aksw.rdfunit.enums.TestCaseExecutionType} object.
     * @return a {@link org.aksw.rdfunit.io.writer.RDFWriter} object.
     */
    public static RDFWriter createWriterFromFormat(String filenameWithoutExtension, SerializationFormat serializationFormat, TestCaseExecutionType executionType) {
        if (serializationFormat.equals(FormatService.getOutputFormat("html"))) {
            return createHTMLWriter(executionType, filenameWithoutExtension + "." + serializationFormat.getExtension());
        } else {
            return new RDFFileWriter(filenameWithoutExtension + "." + serializationFormat.getExtension(), serializationFormat.getName());
        }
    }

    /**
     * <p>createWriterFromFormat.</p>
     *
     * @param outputStream a {@link java.io.OutputStream} object.
     * @param serializationFormat a {@link org.aksw.rdfunit.io.format.SerializationFormat} object.
     * @param executionType a {@link org.aksw.rdfunit.enums.TestCaseExecutionType} object.
     * @return a {@link org.aksw.rdfunit.io.writer.RDFWriter} object.
     */
    public static RDFWriter createWriterFromFormat(OutputStream outputStream, SerializationFormat serializationFormat, TestCaseExecutionType executionType) {
        if (serializationFormat.equals(FormatService.getOutputFormat("html"))) {
            return createHTMLWriter(executionType, outputStream);
        } else {
            return new RDFStreamWriter(outputStream, serializationFormat.getName());
        }
    }


    /**
     * <p>createHTMLWriter.</p>
     *
     * @param type a {@link org.aksw.rdfunit.enums.TestCaseExecutionType} object.
     * @param filename a {@link java.lang.String} object.
     * @return a {@link org.aksw.rdfunit.io.writer.RDFHTMLResultsWriter} object.
     */
    public static RDFHTMLResultsWriter createHTMLWriter(TestCaseExecutionType type, String filename) {
        switch (type) {
            case statusTestCaseResult:
                return new RDFHTMLResultsStatusWriter(filename);
            case aggregatedTestCaseResult:
                return new RDFHTMLResultsAggregateWriter(filename);
            case rlogTestCaseResult:
                return new RDFHTMLResultsRlogWriter(filename);
            case extendedTestCaseResult:
                // TODO extended not supported ATM, use RLOG instead
                return new RDFHTMLResultsRlogWriter(filename);
            default:
                return null;
        }
    }

    /**
     * <p>createHTMLWriter.</p>
     *
     * @param type a {@link org.aksw.rdfunit.enums.TestCaseExecutionType} object.
     * @param outputStream a {@link java.io.OutputStream} object.
     * @return a {@link org.aksw.rdfunit.io.writer.RDFHTMLResultsWriter} object.
     */
    public static RDFHTMLResultsWriter createHTMLWriter(TestCaseExecutionType type, OutputStream outputStream) {
        switch (type) {
            case statusTestCaseResult:
                return new RDFHTMLResultsStatusWriter(outputStream);
            case aggregatedTestCaseResult:
                return new RDFHTMLResultsAggregateWriter(outputStream);
            case rlogTestCaseResult:
                return new RDFHTMLResultsRlogWriter(outputStream);
            case extendedTestCaseResult:
                // TODO extended not supported ATM, use RLOG instead
                return new RDFHTMLResultsRlogWriter(outputStream);
            default:
                return null;
        }
    }
}
