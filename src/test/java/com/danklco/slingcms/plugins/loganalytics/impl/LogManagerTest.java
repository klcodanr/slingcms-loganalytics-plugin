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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.danklco.slingcms.plugins.loganalytics.LogLine;
import com.danklco.slingcms.plugins.loganalytics.LogManager;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.osgi.framework.BundleContext;

public class LogManagerTest {

    private LogManager logManager;

    @Before
    public void init() {
        logManager = new LogManagerImpl();
        BundleContext context = Mockito.mock(BundleContext.class);
        Mockito.when(context.getProperty(Mockito.anyString())).thenAnswer(in -> {
            String key = (String) in.getArgument(0);
            return "sling.home".equals(key)
                    ? LogManagerTest.class.getClassLoader().getResource("").getPath()
                    : null;
        });

        ((LogManagerImpl) logManager).activate(context);
    }

    @Test
    public void simpleRecent() throws InstantiationException {
        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);

        List<LogLine> logs = logManager.getLogs(cal.getTime(), now);
        assertNotNull(logs);
    }

    @Test
    public void testDateRange() throws InstantiationException, ParseException {
        Date start = new SimpleDateFormat(LogLineImpl.LOG_DATE_FORMAT).parse("02/Jun/2020:13:12:01 -0400");
        Date end = new SimpleDateFormat(LogLineImpl.LOG_DATE_FORMAT).parse("02/Jun/2020:13:12:10 -0400");
        List<LogLine> logs = logManager.getLogs(start, end);
        assertNotNull(logs);
        assertEquals(13, logs.size());
    }
}