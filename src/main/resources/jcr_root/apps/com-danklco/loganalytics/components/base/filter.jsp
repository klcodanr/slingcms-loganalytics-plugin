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
<span class="is-pulled-right">
    <span class="field is-grouped is-horizontal">
        <span class="field-body">
            <span class="control">
               <input type="text" class="input is-small filter" value="${sling:encode(properties.defaultFilter,'HTML_ATTR')}" />
            </span>
            &nbsp;
            <span class="control">
                <a class="button is-outlined is-small do-filter" href="#"><em class="jam jam-arrow-right"></em></a>
            </span>
        </span>
    </span>
</span>