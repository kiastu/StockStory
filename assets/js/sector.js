$(document).ready(function() {
	init();
});

//create the sector <div>s
function init() {
	var sectors = ["Energy", "Materials", "Industrials", "Consumer", "Health Care", "Financials",
		"Information Technology", "Telecommunication Services", "Utilities"];
	var indexes = ["7.8", "9.2", "9.2", "9.2", "9.2", "-2.8", "9.2", "9.2", "9.2"];
	var sector_index = "8.2"
	var container = document.getElementById("container");

	for(var i=0; i<9; i++) {
		var stock_container = document.createElement("div");
		stock_container.setAttribute("class", "stock_container");
		if(indexes[i] > sector_index && indexes[i] > 0) {
			$(stock_container).css({"background-color": "rgba(0, 255, 0, 1)"});
		} else if(indexes[i] < sector_index && indexes[i] > 0) {
			$(stock_container).css({"background-color": "rgba(0, 178, 0, 0.6)"});
		} else if(indexes[i] > sector_index && indexes[i] < 0) {
			$(stock_container).css({"background-color": "rgba(255, 0, 0, 1)"});
		} else if(indexes[i] < sector_index && indexes[i] < 0){
			$(stock_container).css({"background-color": "rgba(178, 0, 0, 0.6)"});
		}
		stock_container.setAttribute("id", sectors[i]);


		var stock_text = document.createElement("p");
		stock_text.innerHTML = sectors[i];
		stock_text.setAttribute("class", "stock_text");
		stock_container.appendChild(stock_text);
		var stock_index = document.createElement("p");
		stock_index.innerHTML = indexes[i];
		stock_container.appendChild(stock_index);
		var stock_link = document.createElement("a");
		stock_link.setAttribute("href", sectors[i] + ".html");
		stock_container.appendChild(stock_link);

		container.appendChild(stock_container);

		$(stock_container).click(function() {
			window.location=$(this).find("a").attr("href");
		});
	}
}
