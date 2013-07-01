package fr.remiguitreau.aroeveren.lucine.impl;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import fr.remiguitreau.aroeveren.lucine.LucineUtils;
import fr.remiguitreau.aroeveren.lucine.api.AroevenStaysDescriptor;
import fr.remiguitreau.aroeveren.lucine.api.LucineAPIConnector;
import fr.remiguitreau.aroeveren.lucine.api.LucineAPIRequestFactory;
import fr.remiguitreau.aroeveren.lucine.api.LucineAccessDescriptor;

public class LucineAPIConnectorByHttpClient implements LucineAPIConnector {

    private static final String LOGIN_SUFFIX = "/Login_Admin.php";

    private final LucineAccessDescriptor lucineAccessDescriptor;

    private final LucineAPIRequestFactory lucineAPIRequestFactory;

    private HttpClient httpClient;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public LucineAPIConnectorByHttpClient(final HttpClient httpClient,
            final LucineAccessDescriptor lucineAccessDescriptor,
            final LucineAPIRequestFactory lucineAPIRequestFactory) {
        Validate.notNull(lucineAPIRequestFactory);
        this.lucineAPIRequestFactory = lucineAPIRequestFactory;
        Validate.notNull(lucineAccessDescriptor);
        this.lucineAccessDescriptor = lucineAccessDescriptor;
        prepareAuthenticateClient();
    }

    @Override
    public byte[] retrieveStaysFileAsXLSFormat(final AroevenStaysDescriptor aroevenStaysDescriptor) {
        HttpMethod method = null;
        try {
            method = lucineAPIRequestFactory.buildRetrieveStaysMethod(lucineAccessDescriptor,
                    aroevenStaysDescriptor);
            sendLucineRequest(method, false);
            logger.info("Received {} bytes as '{}'", method.getResponseBody().length,
                    method.getResponseHeader("Content-Type"));
            return method.getResponseBody();
        } catch (final LucineAuthenticationException ex) {
            try {
                if (method != null) {
                    logger.warn("We have to authenticate on lucine server first...");
                    authenticateOnServer();
                    logger.warn("...authentication OK ! Resend method.");
                    sendLucineRequest(method, false);
                    logger.info("Received {} bytes as '{}'", method.getResponseBody().length,
                            method.getResponseHeader("Content-Type"));
                    return method.getResponseBody();
                }
                return null;
            } catch (final LucineRedirectRequest ex2) {
                return method == null ? null : processRedirection(method.getPath(), ex2.urlToRedirect);
            } catch (final Exception ex2) {
                throw new LucineAccessException(
                        "Error while retrieving stays from lucine, even after authentication done...", ex2);
            }
        } catch (final LucineRedirectRequest ex) {
            return method == null ? null : processRedirection(method.getPath(), ex.urlToRedirect);
        } catch (final Exception ex) {
            throw new LucineAccessException("Error while retrieving stays from lucine...", ex);
        }
    }

    // -------------------------------------------------------------------------

    private byte[] processRedirection(final String basePath, final String redirectUrl) {
        logger.info("Must redirect from '{}' to {}", basePath, redirectUrl);
        final GetMethod redirectMethod = new GetMethod(LucineUtils.buildRedirectUrl(lucineAccessDescriptor,
                basePath, redirectUrl));
        try {
            sendLucineRequest(redirectMethod, true);
            logger.info("Received {} bytes as '{}'", redirectMethod.getResponseBody().length,
                    redirectMethod.getResponseHeader("Content-Type"));
            return redirectMethod.getResponseBody();
        } catch (final Exception ex) {
            logger.error("Failed to retrieve redirect url '" + redirectUrl + "' content...", ex);
            return null;
        }
    }

    private void sendLucineRequest(final HttpMethod method, final boolean ignoreRedirect) throws IOException,
            HttpException, URIException {
        final int responseCode = httpClient.executeMethod(method);
        checkResponse(responseCode, method, ignoreRedirect);
    }

    private void prepareAuthenticateClient() {
        httpClient = new HttpClient();
        // final Credentials defaultcreds = new UsernamePasswordCredentials(
        // lucineAccessDescriptor.user, lucineAccessDescriptor.password);
        // httpClient.getParams().setAuthenticationPreemptive(true);
        // httpClient.getState().setCredentials(AuthScope.ANY, defaultcreds);
    }

    private void checkResponse(final int responseCode, final HttpMethod httpMethod,
            final boolean ignoreRedirect) throws URIException, IOException {
        switch (responseCode) {
            case 401:
                throw new LucineAccessException("User '" + lucineAccessDescriptor.user
                        + "' has no right to execute this request with URL " + httpMethod.getURI() + ": "
                        + httpMethod.getResponseBodyAsString());
            case 403:
                throw new LucineAccessException("Forbidden access to repository with URL "
                        + httpMethod.getURI() + ": " + httpMethod.getResponseBodyAsString());
            case 404:
                throw new LucineAccessException("Repository with URL " + httpMethod.getURI() + " not found: "
                        + httpMethod.getResponseBodyAsString());
            case 200:
                logger.info("OK");
                break;

            case 301:
            case 302:
            case 303:
            case 307:
                logger.info("Redirect ({})", responseCode);
                if (!ignoreRedirect) {
                    final Header locationHeader = httpMethod.getResponseHeader("location");
                    if (locationHeader != null) {
                        if (locationHeader.getValue().contains(LOGIN_SUFFIX)) {
                            throw new LucineAuthenticationException(
                                    "We have to authenticate on lucine server first on url '"
                                            + locationHeader.getValue() + "'");
                        }
                        throw new LucineRedirectRequest(locationHeader.getValue());
                    }
                    throw new LucineAccessException("URI " + httpMethod.getURI()
                            + " should be redirected... but it's not... (code=" + responseCode + ")");
                }
                logger.info("... but we ignore it !");
                break;
            default:
                throw new LucineAccessException("Error accessing repository with URL " + httpMethod.getURI()
                        + ": " + httpMethod.getResponseBodyAsString() + " (code=" + responseCode + ")");
        }
    }

    private void authenticateOnServer() throws URIException, HttpException, IOException,
            IllegalArgumentException, NoSuchAlgorithmException {
        final PostMethod method = new PostMethod(lucineAccessDescriptor.url + LOGIN_SUFFIX);
        method.addParameter("user", lucineAccessDescriptor.user);
        method.addParameter("mdp", md5Encrypting(lucineAccessDescriptor.password));
        method.addParameter("action", "Login_Verifier");
        method.addParameter("lang", "fr");
        method.addParameter("taille_ecran", "600");
        sendLucineRequest(method, true);
    }

    private String md5Encrypting(final String password) throws NoSuchAlgorithmException {
        return DigestUtils.md5Hex(password.getBytes());
    }
}
