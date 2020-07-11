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

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.input.ReversedLinesFileReader;
import com.danklco.slingcms.plugins.loganalytics.GeoLocator;
import com.danklco.slingcms.plugins.loganalytics.LogLine;
import com.danklco.slingcms.plugins.loganalytics.LogManager;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicyOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default implementation of the LogManager service
 */
@Component(service = { LogManager.class })
public class LogManagerImpl implements LogManager {

    private static final Logger log = LoggerFactory.getLogger(LogManagerImpl.class);

    private File logRoot;

    @Reference(cardinality = ReferenceCardinality.OPTIONAL, policyOption = ReferencePolicyOption.GREEDY)
    private GeoLocator geoLocator;

    @Activate
    public void activate(final BundleContext context) {
        String rootDir = context.getProperty("sling.log.root");
        if (rootDir == null) {
            log.info("Falling back to sling.home");
            rootDir = context.getProperty("sling.home");
            if (rootDir == null) {
                log.info("Falling back using current path");
                rootDir = new File(".").getAbsolutePath();
            }
        }
        log.info("Loaded root directory: {}", rootDir);
        logRoot = new File(rootDir + "/logs");
    }

    private File[] getLogFiles(final Date start, final Date end) {
        return logRoot.listFiles((file, name) -> filter(name, start, end));
    }

    private boolean filter(final String name, final Date start, final Date end) {
        return name.startsWith("fullrequest.log") && fileDateWithinRange(name, start, end);
    }

    private Date getStartOfDay() {
        final Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DATE);
        calendar.set(year, month, day, 0, 0, 0);
        return calendar.getTime();
    }

    private Date getEndOfDay(final Date date) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DATE);
        calendar.set(year, month, day, 23, 59, 59);
        calendar.add(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    private boolean fileDateWithinRange(final String name, final Date start, final Date end) {
        Date fileStart = getStartOfDay();
        Date fileEnd = getEndOfDay(new Date());
        if (!"fullrequest.log".equals(name)) {
            try {
                fileStart = new SimpleDateFormat("yyyy-MM-dd").parse(name.substring(16));
                fileEnd = getEndOfDay(new SimpleDateFormat("yyyy-MM-dd").parse(name.substring(16)));
            } catch (final ParseException e) {
                log.warn("Failed to parse date from: {}", name.substring(16), e);
            }
        }
        return !((fileStart.equals(end) || fileStart.before(end)) && (fileEnd.equals(start) || fileEnd.before(start)));
    }

    @Override
    public List<LogLine> getLogs(final Date start, final Date end) {
        log.debug("Finding logs between {} and {}", start, end);
        final File[] logFiles = getLogFiles(start, end);
        if (log.isDebugEnabled()) {
            log.debug("Retrieved log files {}",
                    Arrays.stream(logFiles).map(File::getName).collect(Collectors.joining(",")));
        }
        return Arrays.stream(logFiles).flatMap(f -> {
            try {
                return getLogLines(f, start, end);
            } catch (final IOException e) {
                log.warn("Failed to load logs from {}", f, e);
                return new ArrayList<LogLine>().stream();
            }
        }).collect(Collectors.toList());
    }

    private Stream<LogLine> getLogLines(final File f, final Date start, final Date end) throws IOException {
        final List<LogLine> lines = new ArrayList<>();
        try (ReversedLinesFileReader reader = new ReversedLinesFileReader(f, StandardCharsets.UTF_8)) {
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                try {
                    if (!line.startsWith("#")) {
                        final Date logTime = LogLineImpl.peekDate(line);
                        if (!logTime.before(start) && !logTime.after(end)) {
                            lines.add(LogLineImpl.parse(line, geoLocator));
                        }
                    }
                } catch (final InstantiationException e) {
                    log.warn("Failed to read line: {}", line, e);
                }

            }
        }
        return lines.stream();
    }

}