window.onload = default_select;
var now = new Date();       
var year = now.getFullYear();
var month = now.getMonth() + 1;

function default_select(){
	console.log(month)
	for(var i = year; i >= 2017; i--){
		var option = document.createElement("option");
		option.text = i + "年";
		option.value = i;
		document.getElementById("joinYear").appendChild(option);
	}
	for(var i = 1; i <= month; i++){
		if(i%3 == 1){
			var option = document.createElement("option");
			option.text = i + "月";
			option.value = ( '00' + i ).slice( -2 );
			document.getElementById("joinMonth").appendChild(option);
		}
	}
}

document.getElementById("joinYear").addEventListener("change",function(event){
	var length = document.getElementById("joinMonth").length;
	if(Number(event.target.value) !== year){
		for(var j = 0; j <= length - 1; j++){
			document.getElementById("joinMonth").children[0].remove();
		}
		for(var i = 1; i <= 12; i++){
			if(i%3 == 1){
				var option = document.createElement("option");
				option.text = i + "月";
				option.value = ( '00' + i ).slice( -2 );
				document.getElementById("joinMonth").appendChild(option);
			}
		}
	}else if(Number(event.target.value) === year){
		for(var j = 0; j <= length - 1; j++){
			document.getElementById("joinMonth").children[0].remove();
		}
		for(var i = 1; i <= month; i++){
			if(i%3 == 1){
				var option = document.createElement("option");
				option.text = i + "月";
				option.value = ( '00' + i ).slice( -2 );
				document.getElementById("joinMonth").appendChild(option);
			}
		}
	}
})

