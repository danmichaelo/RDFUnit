package org.aksw.rdfunit.io.writer;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import org.aksw.jena_sparql_api.core.QueryExecutionFactory;
import org.aksw.rdfunit.utils.PrefixNSService;

import java.io.OutputStream;

/**
 * <p>RDFHTMLResultsStatusWriter class.</p>
 *
 * @author Dimitris Kontokostas
 *         Description
 * @since 4/23/14 8:55 AM
 * @version $Id: $Id
 */
public class RDFHTMLResultsStatusWriter extends RDFHTMLResultsWriter {

    /**
     * <p>Constructor for RDFHTMLResultsStatusWriter.</p>
     *
     * @param filename a {@link java.lang.String} object.
     */
    public RDFHTMLResultsStatusWriter(String filename) {
        super(filename);
    }

    /**
     * <p>Constructor for RDFHTMLResultsStatusWriter.</p>
     *
     * @param outputStream a {@link java.io.OutputStream} object.
     */
    public RDFHTMLResultsStatusWriter(OutputStream outputStream) {
        super(outputStream);
    }

    /** {@inheritDoc} */
    @Override
    protected StringBuffer getResultsHeader() {
        return new StringBuffer("<tr><th>Status</th><th>Level</th><th>Test Case</th><th>Description</th></tr>");
    }

    /** {@inheritDoc} */
    @Override
    protected StringBuffer getResultsList(QueryExecutionFactory qef, String testExecutionURI) throws RDFWriterException {
        StringBuffer results = new StringBuffer();
        String template = "<tr class=\"%s\"><td>%s</td><td>%s</td><td>%s</td><td>%s</td></tr>";

        String sparql = PrefixNSService.getSparqlPrefixDecl() +
                " SELECT DISTINCT ?resultStatus ?testcase ?level ?description WHERE {" +
                " ?s a rut:StatusTestCaseResult ; " +
                "    rut:resultStatus ?resultStatus ; " +
                "    rut:testCaseLogLevel ?level ; " +
                "    rut:testCase ?testcase ;" +
                "    dcterms:description ?description ;" +
                //"    rut:resultCount ?resultCount ; " +
                //"    rut:resultPrevalence ?resultPrevalence ; " +
                //"    prov:wasGeneratedBy <" + testExecutionURI + "> " +
                "} ";

        QueryExecution qe = null;

        try {
            qe = qef.createQueryExecution(sparql);
            ResultSet rs = qe.execSelect();

            while (rs.hasNext()) {
                QuerySolution qs = rs.next();
                String resultStatus = qs.get("resultStatus").toString();
                String testcase = qs.get("testcase").toString();
                String description = qs.get("description").toString();
                String level = qs.get("level").toString();

                String statusShort = resultStatus.replace(PrefixNSService.getNSFromPrefix("rut") + "ResultStatus", "");
                String levelShort = PrefixNSService.getLocalName(level, "rlog");
                String rowClass = getStatusClass(statusShort);

                String row = String.format(template,
                        rowClass,
                        "<a href=\"" + resultStatus + "\">" + statusShort + "</a>",
                        "<a href=\"" + level + "\">" + levelShort + "</a>",
                        testcase.replace(PrefixNSService.getNSFromPrefix("rutt"), "rutt:"),
                        description
                        //resultCount,
                        //resultPrevalence
                );
                results.append(row);
            }

        } catch (Exception e) {
            throw new RDFWriterException(e);
        } finally {
            if (qe != null) {
                qe.close();
            }
        }

        return results;
    }

    /**
     * <p>getStatusClass.</p>
     *
     * @param status a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    protected String getStatusClass(String status) {

        switch (status) {
            case "Success":
                return "success";
            case "Fail":
                return "danger";
            case "Timeout":
                return "warning";
            case "Error":
                return "warning";
            default:
                return "";
        }
    }
}
