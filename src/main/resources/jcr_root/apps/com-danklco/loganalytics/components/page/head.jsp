<%-- /*
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
 */ --%>
 <%@include file="/libs/sling-cms/global.jsp"%>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>${properties['jcr:title']} :: ${branding.appName}</title>
    <link href="${branding.css}" rel="stylesheet" />
    <link href="/static/clientlibs/com-danklco/loganalytics/css/analytics.css" rel="stylesheet" />
    <link rel="apple-touch-icon" sizes="180x180" href="${branding.appleTouchIcon}" />
    <link rel="icon" type="image/png" sizes="32x32" href="${branding.favicon32}" />
    <link rel="icon" type="image/png" sizes="16x16" href="${branding.favicon16}" />
    <link rel="shortcut icon" href="${branding.favicon}" />
    <meta name="apple-mobile-web-app-title" content="${branding.appName}" />
    <meta name="application-name" content="${branding.appName}" />
    <link rel="mask-icon" href="${branding.appleMaskIcon}" color="${branding.tileColor}" />
    <meta name="msapplication-TileColor" content="${branding.tileColor}" />
    <meta name="theme-color" content="${branding.tileColor}" />
    <meta name="msapplication-config" content="${branding.browserConfig}">
    <link rel="manifest" href="${branding.webManifest}">
</head>