/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
const gulp = require("gulp");
const concat = require("gulp-concat");

gulp.task("css", function () {
  return gulp
    .src([
      "./node_modules/chartist/dist/chartist.min.css",
      "./src/main/frontend/css/analytics.css",
    ])
    .pipe(concat("analytics.css"))
    .pipe(
      gulp.dest(
        "./target/dist/jcr_root/static/clientlibs/com-danklco/loganalytics/css"
      )
    );
});

gulp.task("js", function () {
  return gulp
    .src([
      "./node_modules/moment/min/moment-with-locales.min.js",
      "./node_modules/chartist/dist/chartist.min.js",
      "./src/main/frontend/js/analytics.js",
    ])
    .pipe(concat("analytics.js"))
    .pipe(
      gulp.dest(
        "./target/dist/jcr_root/static/clientlibs/com-danklco/loganalytics/js"
      )
    );
});

gulp.task("default", gulp.series(["css", "js"]));
