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
<div class="ct-chart ct-perfect-fourth analytics__element timeseries" data-name="${sling:encode(properties.title,'HTML_ATTR')}" data-divisor="${properties.divisor}" data-round-to="${properties.roundTo}" data-interpolation-format="${properties.displayFormat}">
</div>