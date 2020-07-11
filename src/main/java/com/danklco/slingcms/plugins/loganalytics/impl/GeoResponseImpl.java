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

import com.danklco.slingcms.plugins.loganalytics.GeoResponse;
import org.jetbrains.annotations.NotNull;

/**
 * Implementation of the GeoResponse model
 */
public class GeoResponseImpl implements GeoResponse {

    private final String countryCode;
    private final String postalCode;
    private final String regionCode;

    public GeoResponseImpl(String countryCode, String postalCode, String regionCode) {
        this.countryCode = countryCode;
        this.postalCode = postalCode;
        this.regionCode = regionCode;
    }

    @Override
    public @NotNull String getCountryCode() {
        return countryCode;
    }

    @Override
    public @NotNull String getPostalCode() {
        return postalCode;
    }

    @Override
    public @NotNull String getRegionCode() {
        return regionCode;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "GeoResponseImpl [countryCode=" + countryCode + ", postalCode=" + postalCode + ", regionCode="
                + regionCode + "]";
    }

}