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
import java.io.InputStream;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;

import com.maxmind.db.CHMCache;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import com.danklco.slingcms.plugins.loganalytics.GeoLocator;
import com.danklco.slingcms.plugins.loganalytics.GeoResponse;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of the GeoLocator service based on MaxMind GeoIP2
 */
@Component(service = { GeoLocator.class, Runnable.class })
@Designate(ocd = GeoLocatorConfig.class)
public class GeoLocatorImpl implements GeoLocator, Runnable {

    private static final Logger log = LoggerFactory.getLogger(GeoLocatorImpl.class);

    private DatabaseReader reader;

    private GeoLocatorConfig config;

    @Activate
    public void activate(GeoLocatorConfig config) throws IOException {
        this.config = config;
        loadDatabase();
    }

    private void loadDatabase() throws IOException {
        log.trace("loadDatabase");
        URL url = new URL(String.format(
                "https://download.maxmind.com/app/geoip_download?edition_id=GeoLite2-City&license_key=%s&suffix=tar.gz",
                config.licenseKey()));
        log.debug("Downloading file from {}", url);
        URLConnection conn = url.openConnection();
        boolean loaded = false;
        try (InputStream is = conn.getInputStream()) {
            log.debug("Uncompressing stream...");
            ArchiveInputStream arch = new TarArchiveInputStream(new GzipCompressorInputStream(is));
            ArchiveEntry entry = null;
            while ((entry = arch.getNextEntry()) != null) {
                log.debug("Handling entry: {}", entry.getName());
                if (entry.getName().endsWith("GeoLite2-City.mmdb")) {
                    log.debug("Updating database...");
                    loaded = true;
                    reader = new DatabaseReader.Builder(arch).withCache(new CHMCache()).build();
                    log.debug("Database uncompressed successfully");
                }
            }
        }
        if (!loaded) {
            throw new IOException("Failed to load maxmind DB");
        } else {
            log.info("GeoIP database loaded");
        }
    }

    @Override
    public GeoResponse getGeoResponse(InetAddress ipAddress) {
        GeoResponse response = null;
        try {
            CityResponse mmResponse = reader.city(ipAddress);
            response = new GeoResponseImpl(mmResponse.getCountry().getIsoCode(), mmResponse.getPostal().getCode(),
                    mmResponse.getMostSpecificSubdivision().getIsoCode());
        } catch (IOException | GeoIp2Exception e) {
            log.debug("Failed to resolve IP address {}", ipAddress, e);
        }
        return response;
    }

    @Override
    public void run() {
        try {
            loadDatabase();
        } catch (IOException e) {
            log.error("Failed to load database", e);
        }
    }

}