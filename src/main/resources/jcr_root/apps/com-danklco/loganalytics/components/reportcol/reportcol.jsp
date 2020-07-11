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
<div class="columns">
    <c:set var="oldAvailableTypes" value="${availableTypes}" />
    <c:set var="availableTypes" value="LogAnalytics" scope="request" />
    <div class="column ${properties.columns == '2' ? 'is-6' : ''}">
        <sling:include path="col" resourceType="sling-cms/components/general/reloadcontainer" />
    </div>
    <c:if test="${properties.columns == '2'}">
        <div class="column is-6">
            <sling:include path="col-1" resourceType="sling-cms/components/general/reloadcontainer" />
        </div>
    </c:if>
    <c:set var="availableTypes" value="${oldAvailableTypes}" scope="request" />
</div>