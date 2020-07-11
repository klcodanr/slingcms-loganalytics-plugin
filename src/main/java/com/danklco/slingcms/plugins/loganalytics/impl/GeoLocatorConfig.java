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

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "%cms.geolocator.name", description = "%cms.geolocator.description", localization = "OSGI-INF/l10n/bundle")
public @interface GeoLocatorConfig {

    @AttributeDefinition(name = "%licensekey.name", description = "%licensekey.description")
    String licenseKey();

    @AttributeDefinition(name = "%scheduler.expression.name", description = "%scheduler.expression.description", defaultValue = "0 0 0 ? * WED")
    String scheduler_expression() default "0 0 0 ? * WED";

}
