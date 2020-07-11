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
package com.danklco.slingcms.plugins.loganalytics.impl;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import com.blueconic.browscap.Capabilities;
import com.blueconic.browscap.UserAgentParser;
import com.blueconic.browscap.UserAgentService;

import org.apache.commons.lang3.StringUtils;
import com.danklco.slingcms.plugins.loganalytics.GeoLocator;
import com.danklco.slingcms.plugins.loganalytics.GeoResponse;
import com.danklco.slingcms.plugins.loganalytics.LogLine;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogLineImpl implements LogLine {

    private static final Logger log = LoggerFactory.getLogger(LogLineImpl.class);

    public static final String SEPARATOR = "␝";

    private static final UserAgentParser uaParser;

    static {
        UserAgentParser temp = null;
        try {
            temp = new UserAgentService().loadParser();
        } catch (IOException | com.blueconic.browscap.ParseException e) {
            log.error("Failed to initialize user agent parser: {}", e.getMessage(), e);
        }
        uaParser = temp;
    }

    private final String[] segments;

    private final Date time;

    private final long responseTime;

    private final long responseSize;

    private final int status;

    private final Capabilities capabilities;

    private final String refererPath;

    private final String refererHost;

    private final String language;

    private final InetAddress ipAddress;
    private final GeoResponse geoResponse;

    public static final String LOG_DATE_FORMAT = "dd/MMM/yyyy:HH:mm:ss Z";

    public static final LogLine parse(String line, GeoLocator geoLocator) throws InstantiationException {
        return new LogLineImpl(line, geoLocator);
    }

    public static final Date peekDate(String line) throws InstantiationException {
        try {
            String[] seg = line.split("␝");
            if (seg.length != 19) {
                throw new InstantiationException("Parsed an incorrect number of fields: " + seg.length);
            }
            return new SimpleDateFormat(LOG_DATE_FORMAT).parse(seg[1]);
        } catch (ParseException e) {
            throw new InstantiationException("Failed to parse time: " + line.split("␝")[1]);
        }
    }

    private LogLineImpl(String line, GeoLocator geoLocator) throws InstantiationException {

        segments = line.split("␝");

        if (segments.length != 19) {
            throw new InstantiationException("Parsed an incorrect number of fields: " + segments.length);
        }
        try {
            time = new SimpleDateFormat(LOG_DATE_FORMAT).parse(segments[1]);
        } catch (ParseException e) {
            throw new InstantiationException("Failed to parse time: " + segments[1]);
        }

        if (StringUtils.isNotBlank(segments[5])) {
            language = segments[5].split(",")[0];
        } else {
            language = segments[5];
        }

        try {
            responseTime = Long.parseLong(segments[14], 10);
        } catch (NumberFormatException e) {
            throw new InstantiationException("Failed to parse response time: " + segments[14]);
        }
        try {
            responseSize = Long.parseLong(segments[15], 10);
        } catch (NumberFormatException e) {
            throw new InstantiationException("Failed to parse response size: " + segments[15]);
        }
        try {
            status = Integer.parseInt(segments[18], 10);
        } catch (NumberFormatException e) {
            throw new InstantiationException("Failed to parse status: " + segments[18]);
        }

        if (uaParser == null) {
            throw new InstantiationException("Failed to parse user agent: " + getUserAgent());
        } else {
            capabilities = uaParser.parse(this.getUserAgent());
        }
        try {
            if (StringUtils.isNotBlank(this.getReferer()) && !"-".equals(this.getReferer())) {
                URL url = new URL(this.getReferer());
                refererHost = url.getHost();
                refererPath = url.getPath();
            } else {
                refererHost = "";
                refererPath = "";
            }
        } catch (MalformedURLException e) {
            throw new InstantiationException("Failed to parse referer: " + this.getReferer());
        }
        this.ipAddress = getIp();

        this.geoResponse = getGeoResponse(geoLocator);

    }

    private InetAddress getIp() {
        try {
            if (StringUtils.isNotBlank(this.getForwardedFor()) && !"-".equals(this.getForwardedFor())) {
                return InetAddress.getByName(this.getForwardedFor().split(",")[0]);
            } else {
                return InetAddress.getByName(this.getRemoteHost());
            }
        } catch (UnknownHostException e) {
            log.warn("Failed to resolve address from IP", e);
            return null;
        }
    }

    private GeoResponse getGeoResponse(GeoLocator geoLocator) {
        if (this.ipAddress == null) {
            log.debug("Skipping as IP Address not set");
            return null;
        }
        if (geoLocator == null) {
            log.debug("Skipping GeoLocator is not set");
            return null;
        }
        GeoResponse response = null;
        try {
            response = geoLocator.getGeoResponse(this.ipAddress);
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.debug("Failed to load geolocation {}", e.getMessage());
            }
        }

        log.debug("Loaded GeoResponse: {}", response);
        return response;
    }

    @Override
    public @NotNull Date getTime() {
        return time;
    }

    @Override
    public @NotNull String getRemoteHost() {
        return segments[2];
    }

    @Override
    public @NotNull String getForwardedFor() {
        return segments[3];
    }

    @Override
    public @NotNull String getUserAgent() {
        return segments[4];
    }

    @Override
    public @NotNull String getLanguage() {
        return language;
    }

    @Override
    public @NotNull String getUser() {
        return segments[6];
    }

    @Override
    public @NotNull String getSessionId() {
        return segments[7];
    }

    @Override
    public @NotNull String getReferer() {
        return segments[8];
    }

    @Override
    public @NotNull String getProtocol() {
        return segments[9];
    }

    @Override
    public @NotNull String getMethod() {
        return segments[10];
    }

    @Override
    public @NotNull String getHost() {
        return segments[11];
    }

    @Override
    public @NotNull String getPath() {
        return segments[12];
    }

    @Override
    public @NotNull String getQueryString() {
        return segments[13];
    }

    @Override
    public @NotNull long getResponseTime() {
        return responseTime;
    }

    @Override
    public @NotNull long getResponseSize() {
        return responseSize;
    }

    @Override
    public @NotNull String getResolvedPath() {
        return segments[16];
    }

    @Override
    public @NotNull String getContentType() {
        return segments[17];
    }

    @Override
    public @NotNull int getStatus() {
        return status;
    }

    @Override
    public @NotNull String getBrowser() {
        return capabilities.getBrowser();
    }

    @Override
    public @NotNull String getBrowserType() {
        return capabilities.getBrowserType();
    }

    @Override
    public @NotNull String getBrowserMajorVersion() {
        return capabilities.getBrowserMajorVersion();
    }

    @Override
    public @NotNull String getDeviceType() {
        return capabilities.getDeviceType();
    }

    @Override
    public @NotNull String getPlatform() {
        return capabilities.getPlatform();
    }

    @Override
    public @NotNull String getPlatformVersion() {
        return capabilities.getPlatformVersion();
    }

    @Override
    public @NotNull String getRefererHost() {
        return refererHost;
    }

    @Override
    public @NotNull String getRefererPath() {
        return refererPath;
    }

    @Override
    public String getResolvedIp() {
        return Optional.ofNullable(ipAddress).map(InetAddress::getHostAddress).orElse(null);
    }

    @Override
    public String getCountryCode() {
        return Optional.ofNullable(this.geoResponse).map(GeoResponse::getCountryCode).orElse("");
    }

    @Override
    public String getRegionCode() {
        return Optional.ofNullable(this.geoResponse).map(GeoResponse::getRegionCode).orElse("");
    }

    @Override
    public String getPostalCode() {
        return Optional.ofNullable(this.geoResponse).map(GeoResponse::getPostalCode).orElse("");
    }

}