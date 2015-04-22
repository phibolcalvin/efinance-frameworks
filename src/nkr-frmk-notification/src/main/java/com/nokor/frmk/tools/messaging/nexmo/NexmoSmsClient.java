package com.nokor.frmk.tools.messaging.nexmo;

import java.io.StringReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.nokor.frmk.tools.HttpClientUtils;


public class NexmoSmsClient {

    private static final Log log = LogFactory.getLog(NexmoSmsClient.class);

    /**
     * http://rest.nexmo.com<br>
     * Service url used unless over-ridden on the constructor
     */
    public static final String DEFAULT_BASE_URL = "http://rest.nexmo.com";

    /**
     * The endpoint path for submitting sms messages
     */
    public static final String SUBMISSION_PATH_SMS = "/sms/xml";

    /**
     * The endpoint path for submitting ussd 'display' messages
     */
    public static final String SUBMISSION_PATH_USSD_DISPLAY = "/ussd/xml";

    /**
     * The endpoint path for submitting ussd 'prompt' messages that require a response
     */
    public static final String SUBMISSION_PATH_USSD_PROMPT = "/ussd-prompt/xml";

    /**
     * Default connection timeout of 5000ms used by this client unless specifically overridden onb the constructor
     */
    public static final int DEFAULT_CONNECTION_TIMEOUT = 5000;

    /**
     * Default read timeout of 30000ms used by this client unless specifically overridden onb the constructor
     */
    public static final int DEFAULT_SO_TIMEOUT = 30000;

    private DocumentBuilderFactory documentBuilderFactory;
    private DocumentBuilder documentBuilder;

    private final String baseUrlHttp;
    private final String baseUrlHttps;
    private final String apiKey;
    private final String apiSecret;

    private final int connectionTimeout;
    private final int soTimeout;

    private final boolean useSSL;

    private HttpClient httpClient = null;

    /**
     * @param apiKey
     * @param apiSecret
     * @throws Exception
     */
    public NexmoSmsClient(final String apiKey,
                          final String apiSecret) throws Exception {
        this(DEFAULT_BASE_URL,
             apiKey,
             apiSecret,
             DEFAULT_CONNECTION_TIMEOUT,
             DEFAULT_SO_TIMEOUT,
             false); // useSSL
    }

    /**
     * @param apiKey
     * @param apiSecret
     * @param connectionTimeout
     * @param soTimeout
     * @throws Exception
     */
    public NexmoSmsClient(final String apiKey,
                          final String apiSecret,
                          final int connectionTimeout,
                          final int soTimeout) throws Exception {
        this(DEFAULT_BASE_URL,
             apiKey,
             apiSecret,
             connectionTimeout,
             soTimeout,
             false); // useSSL
    }

    /**
     * @param baseUrl
     * @param apiKey
     * @param apiSecret
     * @param connectionTimeout
     * @param soTimeout
     * @param useSSL
     * @throws Exception
     */
    public NexmoSmsClient(String baseUrl,
                          final String apiKey,
                          final String apiSecret,
                          final int connectionTimeout,
                          final int soTimeout,
                          final boolean useSSL) throws Exception {

        // Derive a http and a https version of the supplied base url
        baseUrl = baseUrl.trim();
        String lc = baseUrl.toLowerCase();
        if (!lc.startsWith("http://") && !lc.startsWith("https://"))
            throw new Exception("base url does not start with http:// or https://");
        if (lc.startsWith("http://")) {
            this.baseUrlHttp = baseUrl;
            this.baseUrlHttps = "https://" + baseUrl.substring(7);
        } else {
            this.baseUrlHttps = baseUrl;
            this.baseUrlHttp = "http://" + baseUrl.substring(8);
        }

        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        this.connectionTimeout = connectionTimeout;
        this.soTimeout = soTimeout;
        try {
            this.documentBuilderFactory = DocumentBuilderFactory.newInstance();
            this.documentBuilder = this.documentBuilderFactory.newDocumentBuilder();
        } catch (javax.xml.parsers.ParserConfigurationException e) {
            throw new Exception("ERROR initializing XML Document builder!", e);
        }

        this.useSSL = useSSL;
    }

    /**
     * @param message
     * @return
     * @throws Exception
     */
    public SmsSubmissionResult[] submitMessage(Message message) throws Exception {
        boolean performReachabilityCheck = false;
        ValidityPeriod validityPeriod = null;
        String networkCode = null;
        return submitMessage(message, validityPeriod, networkCode, performReachabilityCheck);
    }

    
    /**
     * @param message
     * @param validityPeriod
     * @return
     * @throws Exception
     */
    public SmsSubmissionResult[] submitMessage(Message message, ValidityPeriod validityPeriod) throws Exception {
        boolean performReachabilityCheck = false;
        String networkCode = null;
        return submitMessage(message, validityPeriod, networkCode, performReachabilityCheck);
    }

    
    public SmsSubmissionResult[] submitMessage(Message message,
                                               ValidityPeriod validityPeriod,
                                               String networkCode,
                                               boolean performReachabilityCheck) throws Exception {

        log.debug("HTTP-Message-Submission Client .. from [ " + message.getFrom() + " ] to [ " + message.getTo() + " ] msg [ " + message.getMessageBody() + " ] ");

        // From the Message object supplied, construct an appropriate request to be submitted to the Nexmo REST Service.

        // Determine what 'product' type we are submitting, and select the appropriate endpoint path
        String submitPath = SUBMISSION_PATH_SMS;
        if (message.getType() == Message.MESSAGE_TYPE_USSD_DISPLAY) {
            submitPath = SUBMISSION_PATH_USSD_DISPLAY;
        }
        if (message.getType() == Message.MESSAGE_TYPE_USSD_PROMPT) {
            submitPath = SUBMISSION_PATH_USSD_PROMPT;
        }

        // Determine the type parameter based on the type of Message object.
        boolean unicode = message.isUnicode();

        String mode = "text";

        if (unicode) {
            mode = "unicode";
        }

        // Construct a query string as a list of NameValuePairs

        List<NameValuePair> params = new ArrayList<NameValuePair>();

        boolean doPost = false;

        params.add(new BasicNameValuePair("api_key", this.apiKey));
        params.add(new BasicNameValuePair("api_secret", this.apiSecret));
        params.add(new BasicNameValuePair("from", message.getFrom()));
        params.add(new BasicNameValuePair("to", message.getTo()));
        params.add(new BasicNameValuePair("type", mode));
        // Text Message
        params.add(new BasicNameValuePair("text", message.getMessageBody()));
        if (message.getMessageBody() != null && message.getMessageBody().length() > 255) {
            doPost = true;
        }
    

        if (message.getClientReference() != null) {
            params.add(new BasicNameValuePair("client-ref", message.getClientReference()));
        }

        params.add(new BasicNameValuePair("status-report-req", "" + message.getStatusReportRequired()));

        if (message.getMessageClass() != null) {
            params.add(new BasicNameValuePair("message-class", "" + message.getMessageClass().getMessageClass()));
        }

        if (message.getProtocolId() != null) {
            params.add(new BasicNameValuePair("protocol-id", "" + message.getProtocolId()));
        }

        if (validityPeriod != null) {
            if (validityPeriod.getTimeToLive() != null) {
                params.add(new BasicNameValuePair("ttl", "" + validityPeriod.getTimeToLive().intValue()));
            }
            if (validityPeriod.getValidityPeriodHours() != null) {
                params.add(new BasicNameValuePair("ttl-hours", "" + validityPeriod.getValidityPeriodHours().intValue()));
            }
            if (validityPeriod.getValidityPeriodMinutes() != null) {
                params.add(new BasicNameValuePair("ttl-minutes", "" + validityPeriod.getValidityPeriodMinutes().intValue()));
            }
            if (validityPeriod.getValidityPeriodSeconds() != null) {
                params.add(new BasicNameValuePair("ttl-seconds", "" + validityPeriod.getValidityPeriodSeconds().intValue()));
            }
        }

        if (networkCode != null) {
            params.add(new BasicNameValuePair("network-code", networkCode));
        }

        if (performReachabilityCheck) {
            params.add(new BasicNameValuePair("test-reachable", "true"));
        }


        String baseUrl = this.useSSL ? this.baseUrlHttps : this.baseUrlHttp;
        baseUrl = baseUrl + submitPath;

        // Now that we have generated a query string, we can instanciate a HttpClient,
        // construct a POST or GET method and execute to submit the request
        String response = null;
        for (int pass=1;pass<=2;pass++) {
            HttpUriRequest method = null;
            doPost = true;
            String url = null;
            if (doPost) {
                HttpPost httpPost = new HttpPost(baseUrl);
                httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
                method = httpPost;
                url = baseUrl + "?" + URLEncodedUtils.format(params, "utf-8");
            } else {
                String query = URLEncodedUtils.format(params, "utf-8");
                method = new HttpGet(baseUrl + "?" + query);
                url = method.getRequestLine().getUri();
            }

            try {
                if (this.httpClient == null) {
                    this.httpClient = HttpClientUtils.getInstance(this.connectionTimeout, this.soTimeout).getNewHttpClient();
                }
                HttpResponse httpResponse = this.httpClient.execute(method);
                int status = httpResponse.getStatusLine().getStatusCode();
                if (status != 200) {
                    throw new Exception("got a non-200 response [ " + status + " ] from Nexmo-HTTP for url [ " + url + " ] ");
                }
                response = new BasicResponseHandler().handleResponse(httpResponse);
                log.info(".. SUBMITTED NEXMO-HTTP URL [ " + url + " ] -- response [ " + response + " ] ");
                break;
            } catch (Exception e) {
                method.abort();
                log.info("communication failure: " + e);
                String exceptionMsg = e.getMessage();
                if (exceptionMsg.indexOf("Read timed out") >= 0) {
                    log.info("we're still connected, but the target did not respond in a timely manner ..  drop ...");
                } else {
                    if (pass == 1) {
                        log.info("... re-establish http client ...");
                        this.httpClient = null;
                        continue;
                    }
                }

                // return a COMMS failure ...
                SmsSubmissionResult[] results = new SmsSubmissionResult[1];
                results[0] = new SmsSubmissionResult(SmsSubmissionResult.STATUS_COMMS_FAILURE,
                                                     null,
                                                     null,
                                                     "Failed to communicate with NEXMO-HTTP url [ " + url + " ] ..." + e,
                                                     message.getClientReference(),
                                                     null,
                                                     null,
                                                     true,
                                                     null,
                                                     null);
                return results;
            }
        }


        List<SmsSubmissionResult> results = new ArrayList<SmsSubmissionResult>();

        Document doc = null;
        synchronized(this.documentBuilder) {
            try {
                doc = this.documentBuilder.parse(new InputSource(new StringReader(response)));
            } catch (Exception e) {
                throw new Exception("Failed to build a DOM doc for the xml document [ " + response + " ] ", e);
            }
        }

        NodeList replies = doc.getElementsByTagName("mt-submission-response");
        for (int i=0;i<replies.getLength();i++) {
            Node reply = replies.item(i);
            NodeList messageLists = reply.getChildNodes();
            for (int i2=0;i2<messageLists.getLength();i2++) {
                Node messagesNode = messageLists.item(i2);
                if (messagesNode.getNodeType() != Node.ELEMENT_NODE) {
                    continue;
                }
                if (messagesNode.getNodeName().equals("messages")) {
                    NodeList messages = messagesNode.getChildNodes();
                    for (int i3=0;i3<messages.getLength();i3++) {
                        Node messageNode = messages.item(i3);
                        if (messageNode.getNodeType() != Node.ELEMENT_NODE) {
                            continue;
                        }

                        int status = -1;
                        String messageId = null;
                        String destination = null;
                        String errorText = null;
                        String clientReference = null;
                        BigDecimal remainingBalance = null;
                        BigDecimal messagePrice = null;
                        SmsSubmissionReachabilityStatus smsSubmissionReachabilityStatus = null;
                        String network = null;

                        NodeList nodes = messageNode.getChildNodes();
                        for (int i4=0;i4<nodes.getLength();i4++) {
                            Node node = nodes.item(i4);
                            if (node.getNodeType() != Node.ELEMENT_NODE)
                                continue;
                            if (node.getNodeName().equals("messageId")) {
                                messageId = node.getFirstChild() == null ? null : node.getFirstChild().getNodeValue();
                            } else if (node.getNodeName().equals("to")) {
                                destination = node.getFirstChild() == null ? null : node.getFirstChild().getNodeValue();
                            } else if (node.getNodeName().equals("status")) {
                                String str = node.getFirstChild() == null ? null : node.getFirstChild().getNodeValue();
                                try {
                                    status = Integer.parseInt(str);
                                } catch (NumberFormatException e) {
                                    log.error("xml parser .. invalid value in <status> node [ " + str + " ] ");
                                    status = SmsSubmissionResult.STATUS_INTERNAL_ERROR;
                                }
                            } else if (node.getNodeName().equals("errorText")) {
                                errorText = node.getFirstChild() == null ? null : node.getFirstChild().getNodeValue();
                            } else if (node.getNodeName().equals("clientRef")) {
                                clientReference = node.getFirstChild() == null ? null : node.getFirstChild().getNodeValue();
                            } else if (node.getNodeName().equals("remainingBalance")) {
                                String str = node.getFirstChild() == null ? null : node.getFirstChild().getNodeValue();
                                try {
                                    if (str != null)
                                        remainingBalance = new BigDecimal(str);
                                } catch (NumberFormatException e) {
                                    log.error("xml parser .. invalid value in <remainingBalance> node [ " + str + " ] ");
                                }
                            } else if (node.getNodeName().equals("messagePrice")) {
                                String str = node.getFirstChild() == null ? null : node.getFirstChild().getNodeValue();
                                try {
                                    if (str != null)
                                        messagePrice = new BigDecimal(str);
                                } catch (NumberFormatException e) {
                                    log.error("xml parser .. invalid value in <messagePrice> node [ " + str + " ] ");
                                }
                            } else if (node.getNodeName().equals("reachability")) {
                                NamedNodeMap attributes = node.getAttributes();
                                Node attr = attributes.getNamedItem("status");
                                String str = attr == null ? "" + SmsSubmissionReachabilityStatus.REACHABILITY_STATUS_UNKNOWN : attr.getNodeValue();
                                int reachabilityStatus = SmsSubmissionReachabilityStatus.REACHABILITY_STATUS_UNKNOWN;
                                try {
                                    reachabilityStatus = Integer.parseInt(str);
                                } catch (NumberFormatException e) {
                                    log.error("xml parser .. invalid value in 'status' attribute in <reachability> node [ " + str + " ] ");
                                    reachabilityStatus = SmsSubmissionReachabilityStatus.REACHABILITY_STATUS_UNKNOWN;
                                }

                                attr = attributes.getNamedItem("description");
                                String description = attr == null ? "-UNKNOWN-" : attr.getNodeValue();

                                smsSubmissionReachabilityStatus = new SmsSubmissionReachabilityStatus(reachabilityStatus, description);
                            } else if (node.getNodeName().equals("network")) {
                                network = node.getFirstChild() == null ? null : node.getFirstChild().getNodeValue();
                            } else
                                log.error("xml parser .. unknown node found in status-return, expected [ messageId, to, status, errorText, clientRef, messagePrice, remainingBalance, reachability, network ] -- found [ " + node.getNodeName() + " ] ");
                        }

                        if (status == -1) {
                            throw new Exception("Xml Parser - did not find a <status> node");
                        }

                        // Is this a temporary error ?
                        boolean temporaryError = (status == SmsSubmissionResult.STATUS_THROTTLED ||
                                                  status == SmsSubmissionResult.STATUS_INTERNAL_ERROR ||
                                                  status == SmsSubmissionResult.STATUS_TOO_MANY_BINDS);

                        results.add(new SmsSubmissionResult(status,
                                                            destination,
                                                            messageId,
                                                            errorText,
                                                            clientReference,
                                                            remainingBalance,
                                                            messagePrice,
                                                            temporaryError,
                                                            smsSubmissionReachabilityStatus,
                                                            network));
                    }
                }
            }
        }

        return results.toArray(new SmsSubmissionResult[0]);
    }

}
