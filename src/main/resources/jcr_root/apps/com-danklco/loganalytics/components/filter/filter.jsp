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
<form>
    <div class="field is-grouped is-horizontal">
        <div class="field-label is-normal">
            <label class="label">Date Range</label>
        </div>
        <div class="field-body">
            <p class="control">
                <input type="datetime-local" class="input is-small" name="start" />
            </p>
            &nbsp;&mdash;&nbsp;
            <p class="control">
               <input type="datetime-local" class="input is-small" name="end" />
            </p>
        </div>
    </div>
    <div class="field is-grouped is-horizontal">
        <div class="field-label is-normal">
            <label class="label">Filter</label>
        </div>
        <div class="field-body">
            <p class="control">
               <input type="text" class="input is-small" name="filter" value="${sling:encode(properties.defaultFilter,'HTML_ATTR')}" />
            </p>
            &nbsp;
            <p class="control">
                <button class="button is-light is-small">Go</button>
            </p>
        </div>
    </div>
</form>
