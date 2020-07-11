/*
 * Copyright (C) 2020 Dan Klco
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.danklco.slingcms.plugins.loganalytics;

import java.util.Date;

import org.jetbrains.annotations.NotNull;

/**
 * A single, parsed line from the Analytics fullrequest.log.
 */
public interface LogLine {

    /**
     * The time the request occurred.
     * 
     * @return the time as a date
     */
    @NotNull
    Date getTime();

    /**
     * The remote host for the request, when proxied this is usually localhost.
     * 
     * @return the remote host
     */
    @NotNull
    String getRemoteHost();

    /**
     * The content of the X-Forwarded-For header, when proxied this will be the
     * originating IP of the request
     * 
     * @return the IP / host originating the request
     */
    @NotNull
    String getForwardedFor();

    /**
     * The content of the User-Agent header.
     * 
     * @return the content of the User-Agent Header
     */
    @NotNull
    String getUserAgent();

    /**
     * The reported user id
     * 
     * @return the user id
     */
    @NotNull
    String getUser();

    /**
     * The contents of the JSESSIONID cookie
     * 
     * @return the user's session ID
     */
    @NotNull
    String getSessionId();

    /**
     * The contents of the Referer Header
     * 
     * @return the referrer
     */
    @NotNull
    String getReferer();

    /**
     * The host parsed from the Referer Header
     * 
     * @return the referrer host
     */
    @NotNull
    String getRefererHost();

    /**
     * The path parsed from the Referer Header
     * 
     * @return the referrer path
     */
    @NotNull
    String getRefererPath();

    /**
     * The request protocol
     * 
     * @return
     */
    @NotNull
    String getProtocol();

    /**
     * The request method, e.g. GET, POST, etc
     * 
     * @return
     */
    @NotNull
    String getMethod();

    /**
     * The contents of the Host header
     * 
     * @return the host
     */
    @NotNull
    String getHost();

    /**
     * The requested path
     * 
     * @return
     */
    @NotNull
    String getPath();

    /**
     * The Query string (if any) from the request.
     * 
     * @return the query string from the request
     */
    @NotNull
    String getQueryString();

    /**
     * The time in milliseconds to serve the request
     * 
     * @return the response time
     */
    @NotNull
    long getResponseTime();

    /**
     * The response size in bytes
     * 
     * @return the respnse size
     */
    @NotNull
    long getResponseSize();

    /**
     * The path resolved by Apache Sling
     * 
     * @return the resolved path
     */
    @NotNull
    String getResolvedPath();

    /**
     * The contents of the response Content-Type header
     * 
     * @return the content type
     */
    @NotNull
    String getContentType();

    /**
     * The HTTP status code for the response
     * 
     * @return the status code
     */
    @NotNull
    int getStatus();

    /**
     * Get the browser as parsed from the User-Agent header
     * 
     * @return the browser
     */
    @NotNull
    String getBrowser();

    /**
     * Get the browser type as parsed from the User-Agent header
     * 
     * @return the browser type
     */
    @NotNull
    String getBrowserType();

    /**
     * Get the browser major version as parsed from the User-Agent header
     * 
     * @return the browser major version
     */
    @NotNull
    String getBrowserMajorVersion();

    /**
     * Get the device type as parsed from the User-Agent header
     * 
     * @return the device type
     */
    @NotNull
    String getDeviceType();

    /**
     * Get the platform as parsed from the User-Agent header
     * 
     * @return the platform
     */
    @NotNull
    String getPlatform();

    /**
     * Get the platform version as parsed from the User-Agent header
     * 
     * @return the platform version
     */
    @NotNull
    String getPlatformVersion();

    /**
     * Get the primary language from the Accept-Language header
     * 
     * @return the primary language from the Accept-Language header
     */
    @NotNull
    String getLanguage();

    /**
     * Get the resolved IP from the X-Forward-For header or Remote Host
     * 
     * @return the resolved IP from the X-Forward-For header or Remote Host
     */
    String getResolvedIp();

    /**
     * Get the country code based on the resolved IP
     * 
     * @return the country code based on the resolved IP
     */
    String getCountryCode();

    /**
     * Get the region code based on the resolved IP
     * 
     * @return the region code based on the resolved IP
     */
    String getRegionCode();

    /**
     * Get the postal code based on the resolved IP
     * 
     * @return the postal code based on the resolved IP
     */
    String getPostalCode();

}