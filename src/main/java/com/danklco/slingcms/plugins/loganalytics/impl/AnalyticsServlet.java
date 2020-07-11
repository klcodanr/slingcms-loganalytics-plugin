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
import java.util.Date;
import java.util.List;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import com.danklco.slingcms.plugins.loganalytics.LogLine;
import com.danklco.slingcms.plugins.loganalytics.LogManager;
import org.jetbrains.annotations.NotNull;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = Servlet.class, property = { "sling.servlet.resourceTypes=com-danklco/loganalytics/components/report",
        "sling.servlet.resourceTypes=com-danklco/loganalytics/components/summary",
        "sling.servlet.resourceTypes=com-danklco/loganalytics/components/report", "sling.servlet.methods=GET",
        "sling.servlet.extensions=json", "sling.servlet.selectors=analytics" })
public class AnalyticsServlet extends SlingSafeMethodsServlet {

    private static final Logger log = LoggerFactory.getLogger(AnalyticsServlet.class);

    private static final long serialVersionUID = 1L;

    @Reference
    private transient LogManager logManager;

    @Override
    protected void doGet(@NotNull SlingHttpServletRequest request, @NotNull SlingHttpServletResponse response)
            throws ServletException, IOException {
        long start = Long.parseLong(request.getParameter("start"), 10);
        long end = Long.parseLong(request.getParameter("end"), 10);
        log.debug("Retrieving logs between {} and {}", start, end);
        List<LogLine> lines = logManager.getLogs(new Date(start), new Date(end));
        log.debug("Retrieved {} lines", lines.size());

        response.setContentType("application/json");
        new ObjectMapper().writeValue(response.getOutputStream(), lines);
        log.debug("Successfully wrote response");
    }
}