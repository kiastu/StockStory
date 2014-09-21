
window.onload = function() {
  InitChart();
}

function InitChart() {

 //var lineData=[];
 var lineData = [{
    'x': 1,
    'y': 5
  }, {
    'x': 20,
    'y': 20
  }, {
    'x': 40,
    'y': 10
  }, {
    'x': 60,
    'y': 40
  }, {
    'x': 80,
    'y': 5
  }, {
    'x': 100,
    'y': 60
  }];
var text = '{"micro":[' +
'{"PrevClose":"50","Open":"48" }]}';

obj = JSON.parse(text);
  var i;
  var k=1;

  for(i=0;i<15;i++){
    lineData.push({'x':i,'y':k})
    k+=2;
  }

  var vis = d3.select("#visualisation"),
    WIDTH = 960,
    HEIGHT = 400,
    MARGINS = {
      top: 20,
      right: 20,
      bottom: 20,
      left: 50
    },
    xRange = d3.scale.linear().range([MARGINS.left, WIDTH - MARGINS.right]).domain([d3.min(lineData, function (d) {
        return d.x;
      }),
      d3.max(lineData, function (d) {
        return d.x;
      })
    ]),

    yRange = d3.scale.linear().range([HEIGHT - MARGINS.top, MARGINS.bottom]).domain([d3.min(lineData, function (d) {
        return d.y;
      }),
      d3.max(lineData, function (d) {
        return d.y;
      })
    ]),

  xAxis = d3.svg.axis()
    .scale(xRange)
    .tickSize(5)
    .tickSubdivide(true),

  yAxis = d3.svg.axis()
    .scale(yRange)
    .tickSize(5)
    .orient("left")
    .tickSubdivide(true);


  vis.append("svg:g")
    .attr("class", "x axis")
    .attr("transform", "translate(0," + (HEIGHT - MARGINS.bottom) + ")")
    .call(xAxis);

  vis.append("svg:g")
    .attr("class", "y axis")
    .attr("transform", "translate(" + (MARGINS.left) + ",0)")
    .call(yAxis);

  var lineFunc = d3.svg.line()
  .x(function (d) {
    return xRange(d.x);
  })
  .y(function (d) {
    return yRange(d.y);
  })
  .interpolate('linear');

vis.append("svg:path")
  .attr("d", lineFunc(lineData))
  .attr("stroke", "blue")
  .attr("stroke-width", 2)
  .attr("fill", "none");

document.getElementById("Prev_Close").innerHTML = "Prev Close: " + obj.micro[0].PrevClose;
document.getElementById("Open").innerHTML = "Open: " + obj.micro[0].Open;
document.getElementById("Bid").innerHTML = "Bid: " + " ";
document.getElementById("Ask").innerHTML = "Ask: " + 47.59;
document.getElementById("1y_Target_Est").innerHTML = "1y Target Est: " + 47.93;
document.getElementById("Beta").innerHTML = "Beta: " + 0.73;
document.getElementById("Earnings_Date").innerHTML = "Earnings Date: " + "Oct 22 - Oct 27 (Est.)";
document.getElementById("Day's_Range").innerHTML = "Day's Range: " + 46.60 + " - " + 47.57;
document.getElementById("53wk").innerHTML = "52wk Range: " + 32.15 + " - " + 47.57;
document.getElementById("Volume").innerHTML = "Volume: " + "202,526,536";
document.getElementById("AvgVol").innerHTML = "Avg Vol (3m): " + "33,289,100";
document.getElementById("MarketCap").innerHTML = "Market Cap: " + 391.56;
document.getElementById("PE").innerHTML = "P/E (ttm): " + 17.80;
document.getElementById("EPS").innerHTML = "EPS (ttm): " + 2.67;
document.getElementById("DivYield").innerHTML = "Div & Yield: " + 1.12;

}
