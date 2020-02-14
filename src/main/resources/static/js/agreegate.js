//XMLHttpRequestオブジェクトの生成
var ctx = document.getElementById("myChart").getContext('2d');

var agreegate;
//アンケートのグラフを表示する.
var myChart;
//グラフの結果を表形式で表示し、且つグラフに映らないでデータ全てを表示する.
var grid = canvasDatagrid();

var employee_grid =  canvasDatagrid();

//個々のアンケート結果を入社日別に表示する際に日付を格納する.
var date = "all";
//グラフを削除するかどうかを判定する.
var del = "true";
//個々のアンケート結果を再検索するかを判定する.
var research = "true"
//ページを初めて読み込んだ時にグラフを表示する.
window.onload　=　agreegateAjax("point");

var thisMonth = new Date().getMonth();

//個人用データの表を動的に変更するためのセレクトボックスを生成する関数
window.onload=function(){
	if(document.getElementById("employee_join_date") != null){
		var defaultYear = new Date().getFullYear();
		for(var year = new Date().getFullYear(); year > 2016; year--){
			var month = 10;
			if(year === defaultYear){
				month = Math.floor(new Date().getMonth()/3) + 1;
			}
			for(var option_month = month;option_month >= 1; option_month--){
				if(option_month%3 == 1){
					var option = document.createElement("option");
					option.text = year + "年" + option_month + "月生";
					option.value = year + "-" + option_month;
					document.getElementById("employee_join_date").appendChild(option);
				}
			}
		}
	}
}

document.body.addEventListener("change",function(event){
	if(event.target.id === "sort"){
		del = "true";
		research = "false";
		date = "all";
		agreegateAjax(event.target.value);		
	}else if(event.target.id === "change_questionnaire"){
		document.getElementById("questionnaireId").value = event.target.value;
		del = "true";
		research = "true";
		date="all";
		if(document.getElementById("employee_join_date")!=null){
			document.getElementById("employee_join_date").options[0].selected = true;
		}
		agreegateAjax("point");	
	}else if(event.target.id === "employee_join_date"){
		date = document.getElementById("employee_join_date").value;
		del = "false";
		research = "true";
		agreegateAjax(document.getElementById("sort").value);
	}
})

//CSVエクスポート発火
document.addEventListener("click",function(event){
	var grid_data;
	var csv_dataset = [];
	if(event.target.id === "exportPersonGraph" || event.target.id==="exportCSV"){
		grid_data = event.target.parentNode.lastChild.data;
		grid_data.forEach(function(el){
			var csv_data = {};
			var keys = Object.keys(el);
			keys.forEach(function(key){
				csv_data[key] = el[key];
			})
			csv_dataset.push(csv_data);
		})
		var file_name = window.prompt("保存したいファイル名を入力してください。","");
		if(file_name !== null){
			(new CSV(csv_dataset)).save(file_name);
		}
	}
})

//グラフを非表示
document.querySelector("#graph_display").addEventListener("click",function(event){
	var graphDisplay = document.getElementById("graph_display");
	var display = document.getElementById("graph_display").dataset.display;
	document.getElementById("sort").style.display = display === "none"?"none":"inline";
	graphDisplay.dataset.display = display === "none"?"block":"none";
	graphDisplay.innerText = display === "none"?"グラフを表示":"グラフを非表示";
	document.getElementById("myChart").style.display = display === "none"?"none":"block";
})

//グラフ生成用Ajax
function agreegateAjax(type) {
	var req = new XMLHttpRequest();
	var id = Number(document.getElementById("questionnaireId").value);
	var token = document.getElementsByName("csrf-token")[0].content;
	req.onreadystatechange = function() {
		if (req.readyState == 4) {
			if (req.status == 200) {
				console.log('通信完了');
				answer = JSON.parse(req.responseText);
				console.log(answer);
				document.getElementById("sort").style.display = answer["noAnswer"] == null?"inline":"none";
				document.getElementById("myChart").style.display = answer["noAnswer"] == null?"block":"none";
				document.getElementById("canvas_data_grid").style.display = answer["noAnswer"] == null?"block":"none";
				document.getElementById("graph_display").style.display = answer["noAnswer"] == null?"inline-block":"none";
				document.getElementById("exportCSV").style.display = answer["noAnswer"] == null?"inline-block":"none";
				document.getElementById("agreegate_error").style.display = answer["noAnswer"] == null?"none":"block";
				if(document.getElementById("canvas_personal_data_grid") != null){
					document.getElementById("canvas_personal_data_grid").style.display = answer["noAnswer"] == null?"block":"none";
					document.getElementById("exportPersonGraph").style.display = answer["noAnswer"] == null?"inline-block":"none";
				}
	        	if(answer["noAnswer"] != null){
	        		grid.data = [];
	        		employee_grid.data = [];
	        		if(myChart){
	        			myChart.destroy();
	        		}
	        		return false;
	        	}
				agreegate(answer,type);
			}else{
				console.log('サーバーエラーが発生しました')
			}
		} else {
			console.log('通信中');
		}
	}
	req.open('GET', '/rakus-questionnaire/agreegate/all_answer?id='+id+'&type='+type+'&date='+date, true);
	req.setRequestHeader('X-CSRF-Token', token)
	req.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded;charset=UTF-8');
	req.send(null);
}


//グラフを生成
function agreegate(answer,type){
	var context_path = location.pathname.indexOf("/");
	if (myChart && del == "true") {
		myChart.destroy();
	}
	var dataset=[]
	var labels = [];
	console.log(answer["noAnswer"])
	if(answer["noAnswer"] !== undefined && answer["noAnswer"] == null&&type==="point" && del == "true"){
		var keys = Object.keys(answer['numOfPeople']);
		keys.forEach(function(el){
			labels.push(answer['numOfPeople'][el]);
		});
		myChart = new Chart(ctx, {
		type: 'horizontalBar',
	    data: {
	        labels: answer['categoryName'],
	        datasets: [{
	            label: '1',
	            data: labels[0],//人数
	            borderWidth: 1,
	            backgroundColor: "#454545",
	            borderColor: "#454545"
	        },
	        {
		        label: '2',
		        data: labels[1],
	            borderWidth: 1,
	            backgroundColor: "#01579b",
	            borderColor: "#01579b"
	        },
	        {
	            label: '3',
		        data: labels[2],
	            borderWidth: 1,
	            backgroundColor: "#0288D1",
	            borderColor: "#2906F6"
	        },
	        {
	            label: '4',
		        data: labels[3],
	            borderWidth: 1,
	            backgroundColor: "#81D4FA",
	            borderColor: "#0288D1"
	        },
	        {
	            label: '5',
		        data: labels[4],
	            borderWidth: 1,
	            backgroundColor: "#E1F5FE",
	            borderColor: "#81D4FA"
	        }]
	    },
	    options: {
	    	title:{
	    		display: true
	    		, text: answer["title"] + ":アンケート結果"
	    		,padding: 3
	    		,fontSize: 20
	    	},
	        scales: {
	            yAxes: [
	            	{
	            		stacked: true
	            		, xbarThickness: 16
	            		, scaleLabal: {
	            			display: true
	            			, labelString: '名前'
	            			, fontSize: 16
	            		}
	                }
	            ],
	            xAxes: [
	            	{
	            		stacked: true
	            		, scaleLabal: {
	            			display: false
	            			, labelString: '総得点'
							, fontSize: 16
						}
					}
	            ]
	        },
			legend: {
	        	labels: {
	                boxWidth:30
	                ,padding:20//凡例の各要素間の距離
	                ,fontSize:15
	            }
	            , display: true
	        }
	        , tooltips: {
	            mode: "label"
	        }
		}});
		for(var i = 0; i <= answer['categoryName'].length - 1; i++){
			var data = [];
			data["category_name"] = answer['categoryName'][i];
			for(var j = 1; j <= labels.length; j++){
				var num;
				//半角を全角に変換することで数字を連想配列のキーとして設定できるようにする.
				String(j).replace(/[A-Za-z0-9]/g, function(s) {
				    num = String.fromCharCode(s.charCodeAt(0) + 65248);
				});
				data[num] = labels[j-1][i];
			}
			dataset.push(data)
		}
		grid.data = dataset;
		document.getElementById("canvas_data_grid").appendChild(grid);
	}else if(answer["noAnswer"] == null&&type==="joinYear" && del == "true"){
		var thisYear = new Date().getFullYear();
		var datasets = [];
		for(var i = 0; i <= Object.keys(answer["joinYear"]).length -1 ; i++){
			labels.push(Object.keys(answer["joinYear"])[i].slice(0,7));
			datasets.push(answer["joinYear"][Object.keys(answer["joinYear"])[i]])
		};
		myChart = new Chart(ctx, {
		    type: 'horizontalBar',
		    data: {
		        labels: answer['categoryName'],
		        datasets: [{
		            label: labels[0],
		            data: datasets[0],
		            borderWidth: 1,
		            backgroundColor: "#ff0000",
		            borderColor: "#000"
		        },
		        {
			        label: labels[1],
			        data: datasets[1],
		            borderWidth: 1,
		            backgroundColor: "#ff0",
		            borderColor: "#000"
		        },
		        {
		            label: labels[2],
		            data:datasets[2],
		            borderWidth: 1,
		            backgroundColor: "#00f",
		            borderColor: "#000"
		        },
		        {
		            label: labels[3],
		            data: datasets[3],
		            borderWidth: 1,
		            backgroundColor: "#0f0",
		            borderColor: "#000"
		        }]
		    },
		    options: {
		    	title:{
		    		display: true
		    		,text: answer["title"] + ":アンケート結果"
		    		,padding: 3
		    		,fontSize: 20
		    	},
		        scales: {
		            yAxes: [
		            	{
		            		stacked: false
		            		, xbarThickness: 16
		            		, scaleLabal: {
		            			display: true
		            			, labelString: '名前'
		            			, fontSize: 16
		            		},
		            		ticks:{
		            			fontSize: 18
		            		}
		            	}
		            ],
		            xAxes: [
		            	{
		            		stacked: false
		            		, scaleLabal: {
		            			display: false
		            			, labelString: '総得点'
								, fontSize: 16
							},
		            		ticks:{                      //最大値最小値設定
		                        min: 0,                   //最小値
		                        fontSize: 18,             //フォントサイズ
		                        stepSize: 0.25               //軸間隔
		                    }
						}
		            ]
		    	},
				legend: {
		        	labels: {
		                boxWidth:30
		                ,padding:20 //凡例の各要素間の距離
		                ,fontSize:15
		        	}
		            , display: true
		        }
		        , tooltips: {
		            mode: "label"
		        }
			}
		});
		for(var i = 0; i <= answer['categoryName'].length - 1; i++){
			var data = [];
			data["category_name"] = answer['categoryName'][i];
			for(var j = 0; j <= labels.length - 1; j++){
				data[labels[j]] = datasets[j][i];
			}
			dataset.push(data)
		}
		grid.data = dataset;
		document.getElementById("canvas_data_grid").appendChild(grid);
	}
	
	
	if(answer["employeeAnswers"] != null && research == "true"){
		document.getElementById("employee_error").style.display = "none";
		document.getElementById("canvas_personal_data_grid").style.display = "block"
		document.getElementById("exportPersonGraph").style.display = "inline-block";
		var keys = Object.keys(answer["employeeAnswers"]);
		var dataset = [];
		if(keys.length === 0){
			document.getElementById("employee_error").style.display = "block";
			employee_grid.data = "";
			document.getElementById("exportPersonGraph").style.display = "none";
			return;
		}
		var	length = answer["employeeAnswers"][keys[0]].length;
		for(var i = 0; i<=length-1;i++){
			var data = [];
			keys.forEach(function(el){
				data[el] = answer["employeeAnswers"][el][i];
			})
			dataset.push(data);
		}
		employee_grid.data = dataset;
		document.getElementById("canvas_personal_data_grid").appendChild(employee_grid);
	}
	
}

//CSVファイルを出力
class CSV {
	  constructor(data, keys = false) {
	    this.ARRAY  = Symbol('ARRAY')
	    this.OBJECT = Symbol('OBJECT')

	    this.data = data

	    if (CSV.isArray(data)) {
	      if (0 == data.length) {
	        this.dataType = this.ARRAY
	      } else if (CSV.isObject(data[0])) {
	        this.dataType = this.OBJECT
	      } else if (CSV.isArray(data[0])) {
	        this.dataType = this.ARRAY
	      } else {
	        throw Error('Error: 未対応のデータ型です')
	      }
	    } else {
	      throw Error('Error: 未対応のデータ型です')
	    }

	    this.keys = keys
	  }

	  toString() {
	    if (this.dataType === this.ARRAY) {
	      return this.data.map((record) => (
	        record.map((field) => (
	          CSV.prepare(field)
	        )).join(',')
	      )).join('\n')
	    } else if (this.dataType === this.OBJECT) {
	      const keys = this.keys || Array.from(this.extractKeys(this.data))

	      const arrayData = this.data.map((record) => (
	        keys.map((key) => record[key])
	      ))

	      return [].concat([keys], arrayData).map((record) => (
	        record.map((field) => (
	          CSV.prepare(field)
	        )).join(',')
	      )).join('\n')
	    }
	  }

	  save(filename = 'data.csv') {
	    if (!filename.match(/\.csv$/i)) { filename = filename + '.csv' }

	    console.info('filename:', filename)
	    console.table(this.data)

	    const csvStr = this.toString()

	    const bom     = new Uint8Array([0xEF, 0xBB, 0xBF]);
	    const blob    = new Blob([bom, csvStr], {'type': 'text/csv'});
	    const url     = window.URL || window.webkitURL;
	    const blobURL = url.createObjectURL(blob);

	    let a      = document.createElement('a');
	    a.download = decodeURI(filename);
	    a.href     = blobURL;
	    a.type     = 'text/csv';

	    a.click();
	  }

	  extractKeys(data) {
	    return new Set([].concat(...this.data.map((record) => Object.keys(record))))
	  }

	  static prepare(field) {
	    return '"' + (''+field).replace(/"/g, '""') + '"'
	  }

	  static isObject(obj) {
	    return '[object Object]' === Object.prototype.toString.call(obj)
	  }

	  static isArray(obj) {
	    return '[object Array]' === Object.prototype.toString.call(obj)
	  }
	}