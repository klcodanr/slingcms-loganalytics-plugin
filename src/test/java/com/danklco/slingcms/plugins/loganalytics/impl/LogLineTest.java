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
import static org.junit.Assert.fail;

import com.danklco.slingcms.plugins.loganalytics.LogLine;
import org.junit.Test;

public class LogLineTest {

    @Test
    public void validTest() throws InstantiationException {
        String line = "18.05.2020 16:28:14.968 *INFO* [qtp1531478398-44] log.fullrequest ␝18/May/2020:16:28:14 +0000␝127.0.0.1␝2600:2b00:822d:6900:7408:a015:d647:7b85, 162.158.187.118␝Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36␝en-US,en;q=0.9␝admin␝-␝https://cms.danklco.com/system/console/slinglog␝HTTP/1.1␝GET␝localhost:8080␝/system/sling/form/login␝?resource=%2Fsystem%2Fconsole%2Fslinglog%2Ftailer.txt␝2␝3068␝/system/sling/form/login␝text/html␝20";
        LogLine parsed = LogLineImpl.parse(line,null);
        assertNotNull(parsed);
        assertEquals(1589819294000L, parsed.getTime().getTime());
        assertEquals("text/html", parsed.getContentType());
        assertEquals("2600:2b00:822d:6900:7408:a015:d647:7b85, 162.158.187.118", parsed.getForwardedFor());
        assertEquals("localhost:8080", parsed.getHost());
        assertEquals("GET", parsed.getMethod());
        assertEquals("/system/sling/form/login", parsed.getPath());
        assertEquals("HTTP/1.1", parsed.getProtocol());
        assertEquals("?resource=%2Fsystem%2Fconsole%2Fslinglog%2Ftailer.txt", parsed.getQueryString());
        assertEquals("https://cms.danklco.com/system/console/slinglog", parsed.getReferer());
        assertEquals("cms.danklco.com", parsed.getRefererHost());
        assertEquals("/system/console/slinglog", parsed.getRefererPath());
        assertEquals("127.0.0.1", parsed.getRemoteHost());
        assertEquals("/system/sling/form/login", parsed.getResolvedPath());
        assertEquals(3068L, parsed.getResponseSize());
        assertEquals(2, parsed.getResponseTime());
        assertEquals("-", parsed.getSessionId());
        assertEquals("admin", parsed.getUser());
        assertEquals(
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36",
                parsed.getUserAgent());
        assertEquals("Chrome", parsed.getBrowser());
        assertEquals("81", parsed.getBrowserMajorVersion());
        assertEquals("Browser", parsed.getBrowserType());
        assertEquals("Desktop", parsed.getDeviceType());
        assertEquals("macOS", parsed.getPlatform());
        assertEquals("10.14", parsed.getPlatformVersion());
    }

    @Test
    public void validBotTest() throws InstantiationException {
        String line = "18.05.2020 16:26:27.728 *INFO* [qtp1531478398-657] log.fullrequest ␝18/May/2020:16:26:27 +0000␝127.0.0.1␝114.119.167.91, 162.158.167.15␝Mozilla/5.0 (Linux; Android 7.0;) AppleWebKit/537.36 (KHTML, like Gecko) Mobile Safari/537.36 (compatible; AspiegelBot)␝en-US,en;q=0.9␝anonymous␝-␝-␝HTTP/1.1␝GET␝www.danklco.com␝/content/personal-sites/danklco-com/tags.html/etc/taxonomy/architecture/taxonomy␝?page=1␝135␝16619␝/content/personal-sites/danklco-com/tags␝text/html␝200";
        LogLine parsed = LogLineImpl.parse(line,null);
        assertNotNull(parsed);
        assertEquals(1589819187000L, parsed.getTime().getTime());
        assertEquals("text/html", parsed.getContentType());
        assertEquals("114.119.167.91, 162.158.167.15", parsed.getForwardedFor());
        assertEquals("www.danklco.com", parsed.getHost());
        assertEquals("GET", parsed.getMethod());
        assertEquals("/content/personal-sites/danklco-com/tags.html/etc/taxonomy/architecture/taxonomy",
                parsed.getPath());
        assertEquals("HTTP/1.1", parsed.getProtocol());
        assertEquals("?page=1", parsed.getQueryString());
        assertEquals("-", parsed.getReferer());
        assertEquals("127.0.0.1", parsed.getRemoteHost());
        assertEquals("/content/personal-sites/danklco-com/tags", parsed.getResolvedPath());
        assertEquals(16619L, parsed.getResponseSize());
        assertEquals(135L, parsed.getResponseTime());
        assertEquals("-", parsed.getSessionId());
        assertEquals("anonymous", parsed.getUser());
        assertEquals(
                "Mozilla/5.0 (Linux; Android 7.0;) AppleWebKit/537.36 (KHTML, like Gecko) Mobile Safari/537.36 (compatible; AspiegelBot)",
                parsed.getUserAgent());
        assertEquals("Default Browser", parsed.getBrowser());
        assertEquals("Unknown", parsed.getBrowserMajorVersion());
        assertEquals("Default Browser", parsed.getBrowserType());
        assertEquals("Unknown", parsed.getDeviceType());
        assertEquals("Unknown", parsed.getPlatform());
        assertEquals("Unknown", parsed.getPlatformVersion());
    }


    @Test
    public void invalidReferer() throws InstantiationException {
        String line = "18.05.2020 16:28:14.968 *INFO* [qtp1531478398-44] log.fullrequest ␝18/May/2020:16:28:14 +0000␝127.0.0.1␝2600:2b00:822d:6900:7408:a015:d647:7b85, 162.158.187.118␝Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36␝en-US,en;q=0.9␝admin␝-␝banana:/hoist|invalid__url␝HTTP/1.1␝GET␝localhost:8080␝/system/sling/form/login␝?resource=%2Fsystem%2Fconsole%2Fslinglog%2Ftailer.txt␝2␝3068␝/system/sling/form/login␝text/html␝20";
        try {
            LogLineImpl.parse(line,null);
            fail("Expected exception not caught");
        } catch (InstantiationException ie) {
            assertEquals("Failed to parse referer: banana:/hoist|invalid__url", ie.getMessage());
        }
    }

    @Test
    public void invalidFields() throws InstantiationException {
        String line = "18.05.2020 16:28:14.968 *INFO* [qtp1531478398-44] log.fullrequest ␝18/May/2020:16:28:14 +0000␝␝␝127.0.0.1␝2600:2b00:822d:6900:7408:a015:d647:7b85, 162.158.187.118␝Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36␝en-US,en;q=0.9␝admin␝-␝https://cms.danklco.com/system/console/slinglog␝HTTP/1.1␝GET␝localhost:8080␝/system/sling/form/login␝?resource=%2Fsystem%2Fconsole%2Fslinglog%2Ftailer.txt␝2␝3068␝/system/sling/form/login␝text/html␝200";
        try {
            LogLineImpl.parse(line,null);
            fail("Expected exception not caught");
        } catch (InstantiationException ie) {
            assertEquals("Parsed an incorrect number of fields: 21", ie.getMessage());
        }
    }

    @Test
    public void testPeek() throws InstantiationException {
        String line = "18.05.2020 16:28:14.968 *INFO* [qtp1531478398-44] log.fullrequest ␝18/May/2020:16:28:14 +0000␝␝␝127.0.0.1␝2600:2b00:822d:6900:7408:a015:d647:7b85, 162.158.187.118␝Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36␝en-US,en;q=0.9␝admin␝-␝https://cms.danklco.com/system/console/slinglog␝HTTP/1.1␝GET␝localhost:8080␝/system/sling/form/login␝?resource=%2Fsystem%2Fconsole%2Fslinglog%2Ftailer.txt␝2␝3068␝/system/sling/form/login␝text/html␝200";
        try {
            assertEquals(1589819294000L, LogLineImpl.parse(line,null).getTime().getTime());
        } catch (InstantiationException ie) {
            assertEquals("Parsed an incorrect number of fields: 21", ie.getMessage());
        }
    }

    @Test
    public void invalidTime() throws InstantiationException {
        String line = "18.05.2020 16:28:14.968 *INFO* [qtp1531478398-44] log.fullrequest ␝18/Juney/2020:16:28:14 +0000␝127.0.0.1␝2600:2b00:822d:6900:7408:a015:d647:7b85, 162.158.187.118␝Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36␝en-US,en;q=0.9␝admin␝-␝https://cms.danklco.com/system/console/slinglog␝HTTP/1.1␝GET␝localhost:8080␝/system/sling/form/login␝?resource=%2Fsystem%2Fconsole%2Fslinglog%2Ftailer.txt␝2␝3068␝/system/sling/form/login␝text/html␝200";
        try {
            LogLineImpl.parse(line,null);
            fail("Expected exception not caught");
        } catch (InstantiationException ie) {
            assertEquals("Failed to parse time: 18/Juney/2020:16:28:14 +0000", ie.getMessage());
        }
    }

    @Test
    public void invalidResponseTime() throws InstantiationException {
        String line = "18.05.2020 16:28:14.968 *INFO* [qtp1531478398-44] log.fullrequest ␝18/May/2020:16:28:14 +0000␝127.0.0.1␝2600:2b00:822d:6900:7408:a015:d647:7b85, 162.158.187.118␝Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36␝en-US,en;q=0.9␝admin␝-␝https://cms.danklco.com/system/console/slinglog␝HTTP/1.1␝GET␝localhost:8080␝/system/sling/form/login␝?resource=%2Fsystem%2Fconsole%2Fslinglog%2Ftailer.txt␝2d␝3068␝/system/sling/form/login␝text/html␝200";
        try {
            LogLineImpl.parse(line,null);
            fail("Expected exception not caught");
        } catch (InstantiationException ie) {
            assertEquals("Failed to parse response time: 2d", ie.getMessage());
        }
    }

    @Test
    public void invalidResponseSize() throws InstantiationException {
        String line = "18.05.2020 16:28:14.968 *INFO* [qtp1531478398-44] log.fullrequest ␝18/May/2020:16:28:14 +0000␝127.0.0.1␝2600:2b00:822d:6900:7408:a015:d647:7b85, 162.158.187.118␝Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36␝en-US,en;q=0.9␝admin␝-␝https://cms.danklco.com/system/console/slinglog␝HTTP/1.1␝GET␝localhost:8080␝/system/sling/form/login␝?resource=%2Fsystem%2Fconsole%2Fslinglog%2Ftailer.txt␝2␝30d68␝/system/sling/form/login␝text/html␝200";
        try {
            LogLineImpl.parse(line,null);
            fail("Expected exception not caught");
        } catch (InstantiationException ie) {
            assertEquals("Failed to parse response size: 30d68", ie.getMessage());
        }
    }
}