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
function groupByTime(roundTo = "minute", logs = []) {
  const count = {};
  logs.forEach((log) => {
    const time = moment(log.time);
    const tStr = time.startOf(roundTo).toString();
    if (!count[tStr]) {
      count[tStr] = 0;
    }
    count[tStr]++;
  });
  const data = [];
  Object.keys(count).forEach((k) => {
    data.push({
      x: new Date(moment(k).valueOf()),
      y: count[k],
    });
  });
  data.sort((a, b) => a.x.getTime() - b.x.getTime());
  return data;
}

function getLogValue(log = {}, key = "") {
  const values = [];
  key.split("+").forEach((k) => {
    values.push(log[k]);
  });
  return values.join(" ");
}

function groupByField(logs = [], field = "path", dir = "desc") {
  const count = {};
  logs.forEach((log) => {
    const val = getLogValue(log, field);
    if (!count[val]) {
      count[val] = 0;
    }
    count[val]++;
  });
  const sorted = [];
  Object.keys(count).forEach((k) => {
    sorted.push({
      field: k,
      value: count[k],
    });
  });
  sorted.sort((a, b) => (dir === "desc" ? -1 : 1) * (a.value - b.value));
  return sorted;
}

function filterChart(chart, logs) {
  const filterVal = chart.closest(".panel").querySelector(".filter")?.value;
  if (filterVal) {
    const filter = compileExpression(filterVal);
    return logs.filter(filter);
  } else {
    return logs;
  }
}

rava.bind(".analytics__element.piechart", {
  events: {
    dataLoad(evt) {
      const chart = this;
      const field = chart.dataset.field;
      const otherCutoff = parseInt(chart.dataset.otherCutoff, 10);
      const logs = evt.detail;

      const paintChart = function () {
        const sorted = groupByField(filterChart(chart, logs), field);
        const reducer = (accumulator, currentValue) =>
          accumulator + currentValue.value;
        const sum = sorted.reduce(reducer, 0);

        let other = 0;
        const data = { labels: [], series: [], percent: [] };

        sorted.forEach((el) => {
          const percent = Math.round((el.value / sum) * 100);
          if (percent < otherCutoff) {
            other += el.value;
          } else {
            data.labels.push(el.field);
            data.series.push(el.value);
            data.percent.push(percent);
          }
        });
        if (other > 0) {
          data.labels.push("Other");
          data.series.push(other);
          data.percent.push(Math.round((other / sum) * 100));
        }

        var options = {
          labelInterpolationFnc: function (value) {
            return value + " " + data.percent[data.labels.indexOf(value)] + "%";
          },
        };
        var responsiveOptions = [
          [
            "screen and (min-width: 640px)",
            {
              chartPadding: 30,
              labelOffset: 100,
              labelDirection: "explode",
              labelInterpolationFnc: function (value) {
                return (
                  value + " " + data.percent[data.labels.indexOf(value)] + "%"
                );
              },
            },
          ],
          [
            "screen and (min-width: 1024px)",
            {
              labelOffset: 80,
              chartPadding: 20,
            },
          ],
        ];

        new Chartist.Pie(chart, data, options, responsiveOptions);
      };
      paintChart();
      chart
        .closest(".panel")
        .querySelector(".do-filter")
        .addEventListener("click", paintChart);
    },
  },
});

rava.bind(".analytics__element.timeseries", {
  events: {
    dataLoad(evt) {
      const chart = this;
      const interpolationFormat = chart.dataset.interpolationFormat || "mm/yy";
      const roundTo = chart.dataset.roundTo;
      const name = chart.dataset.name;
      const divisor = parseInt(chart.dataset.divisor, 10) || 5;
      const logs = evt.detail;

      const paintChart = function () {
        new Chartist.Line(
          chart,
          {
            series: [
              {
                name: name,
                data: groupByTime(roundTo, filterChart(chart, logs)),
              },
            ],
          },
          {
            axisX: {
              type: Chartist.FixedScaleAxis,
              divisor: divisor,
              labelInterpolationFnc: function (value) {
                return moment(value).format(interpolationFormat);
              },
            },
          }
        );
      };
      paintChart();
      chart
        .closest(".panel")
        .querySelector(".do-filter")
        .addEventListener("click", paintChart);
    },
  },
});

rava.bind(".analytics__element.grouped-table", {
  events: {
    dataLoad(evt) {
      const table = this;
      const dir = table.dataset.dir;
      const limit = parseInt(table.dataset.limit, 10);
      const field = table.dataset.field;
      const tbody = table.querySelector("tbody");
      const logs = evt.detail;

      const paintTable = function () {
        tbody.innerHTML = "";

        const sorted = groupByField(filterChart(table, logs), field, dir);
        sorted.slice(0, limit).forEach((el) => {
          const tr = document.createElement("tr");
          const fieldTd = document.createElement("td");
          fieldTd.innerText = el.field;
          tr.appendChild(fieldTd);
          const valueTd = document.createElement("td");
          valueTd.innerText = el.value;
          tr.appendChild(valueTd);
          tbody.appendChild(tr);
        });
      };
      paintTable();
      table
        .closest(".panel")
        .querySelector(".do-filter")
        .addEventListener("click", paintTable);
    },
  },
});

rava.bind(".analytics__element.grouped-bar", {
  events: {
    dataLoad(evt) {
      const dir = this.dataset.dir;
      const limit = parseInt(this.dataset.limit, 10) || 10;
      const field = this.dataset.field;
      const chart = this;
      const logs = evt.detail;
      const paintChart = function () {
        const sorted = groupByField(filterChart(chart, logs), field, dir);
        const data = { labels: [], series: [[]] };
        sorted.slice(0, limit).forEach((el) => {
          data.labels.push(el.field);
          data.series[0].push(el.value);
        });
        new Chartist.Bar(chart, data, {
          seriesBarDistance: 10,
        });
      };
      paintChart();
      chart
        .closest(".panel")
        .querySelector(".do-filter")
        .addEventListener("click", paintChart);
    },
  },
});

document.querySelectorAll(".analytics__report").forEach((r) => {
  const startEl = r.querySelector("input[name=start]");
  const endEl = r.querySelector("input[name=end]");
  const filterEl = r.querySelector("input[name=filter]");

  let dirty = true;
  let logs = [];
  startEl.addEventListener("change", () => (dirty = true));
  endEl.addEventListener("change", () => (dirty = true));

  async function loadReport() {
    const modal = Sling.CMS.ui.loaderModal("Loading...");
    const params = new URLSearchParams();
    params.set(
      "start",
      moment(startEl.value, moment.HTML5_FMT.DATETIME_LOCAL).valueOf()
    );
    params.set(
      "end",
      moment(endEl.value, moment.HTML5_FMT.DATETIME_LOCAL).valueOf()
    );

    let filter = null;
    if (filterEl.value) {
      filter = compileExpression(filterEl.value);
    }
    try {
      if (dirty) {
        const resp = await fetch(`${r.dataset.path}?${params.toString()}`);
        logs = await resp.json();
        dirty = false;
      }
      let filteredLogs = logs;
      if (filter) {
        filteredLogs = filteredLogs.filter(filter);
      }
      r.querySelectorAll(".analytics__element").forEach((chart) => {
        const event = new CustomEvent("dataLoad", { detail: filteredLogs });
        chart.dispatchEvent(event);
        console.log("Dispatching data load event");
      });
    } catch (e) {}
    modal.remove();
  }
  endEl.value = moment().format(moment.HTML5_FMT.DATETIME_LOCAL);
  startEl.value = moment()
    .subtract(1, "days")
    .format(moment.HTML5_FMT.DATETIME_LOCAL);
  loadReport();

  document
    .querySelector(".analytics__report form")
    .addEventListener("submit", (evt) => {
      evt.preventDefault();
      evt.stopPropagation();
      loadReport();
    });
});

document.querySelectorAll(".analytics__summary").forEach((s) => {
  async function loadReport(summary) {
    const params = new URLSearchParams();
    params.set("start", new Date().getTime() - 3600000);
    params.set("end", new Date().getTime());

    const resp = await fetch(`${summary.dataset.path}?${params.toString()}`);
    const logs = await resp.json();

    s.querySelectorAll(".analytics__element").forEach((chart) => {
      const event = new CustomEvent("dataLoad", { detail: logs });
      chart.dispatchEvent(event);
      console.log("Dispatching data load event");
    });
  }
  loadReport(s);
});
