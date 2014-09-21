$(document).ready(function() {
	init();
});

//create the sector <div>s
function init() {
	var sectors = ["Energy", "Materials", "Industrials", "Consumer", "Health Care", "Financials",
		"Information Technology", "Telecommunication Services", "Utilities"];
	var indexes = ["7.8", "6.9", "-1.7", "-1.2", "9.2", "9.2", "9.2", "9.2", "9.2"];
	var stock_index = "-1.5";
	var container = document.getElementById("container");

	for(var i=0; i<9; i++) {
		var sector_container = document.createElement("div");
		sector_container.setAttribute("class", "sector_container");
		if(indexes[i] > stock_index && indexes[i] > 0) {
			$(sector_container).css({"background-color": "rgba(0, 220, 0, 1)"});
		} else if(indexes[i] < stock_index && indexes[i] > 0) {
			$(sector_container).css({"background-color": "rgba(0, 178, 0, 0.6)"});
		} else if(indexes[i] > stock_index && indexes[i] < 0) {
			$(sector_container).css({"background-color": "rgba(178, 0, 0, 0.6)"});
		} else if(indexes[i] < stock_index && indexes[i] < 0){
			$(sector_container).css({"background-color": "rgba(220, 0, 0, 0.8)"});
		}
		sector_container.setAttribute("id", sectors[i]);

		var sector_text = document.createElement("p");
		sector_text.innerHTML = sectors[i];
		sector_text.setAttribute("class", "sector_text");
		sector_container.appendChild(sector_text);
		var sector_index = document.createElement("p");
		sector_index.innerHTML = indexes[i];
		sector_container.appendChild(sector_index);
		var sector_link = document.createElement("a");
		sector_link.setAttribute("href", sectors[i] + ".html");
		sector_container.appendChild(sector_link);

		container.appendChild(sector_container);

		$(sector_container).click(function() {
			window.location=$(this).find("a").attr("href");
		});
	}
}
