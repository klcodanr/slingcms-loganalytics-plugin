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
<c:if test="${slingRequest.requestPathInfo.suffixResource != null}">
    <div class="scroll-container">
        <div class="analytics analytics__report" data-path="${sling:encode(slingRequest.requestPathInfo.suffix,'HTML_ATTR')}/jcr:content.analytics.json">
            <sling:include path="${slingRequest.requestPathInfo.suffix}/jcr:content/filter" resourceType="com-danklco/loganalytics/components/filter" />
            <sling:include path="${slingRequest.requestPathInfo.suffix}/jcr:content/report" resourceType="sling-cms/components/general/reloadcontainer" />
        </div>
    </div>
</c:if>