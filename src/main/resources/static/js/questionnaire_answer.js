var oldAnswer = [];

document.body.addEventListener('click', function(event){
	if(event.target.attributes[0].value === "questionnaire_title" && event.target.nextElementSibling.style.display === ""){
		event.target.nextElementSibling.style.display = "table";
	}else if(event.target.attributes[0].value === "questionnaire_title" && event.target.nextElementSibling.style.display === "table"){
		event.target.nextElementSibling.style.display = "";
	}
})

//アンケートのバリデーションチェック
document.body.addEventListener("submit",function(event){
	var parent_category_length = document.getElementsByTagName("fieldset").length;
	for(var i=0; i<=parent_category_length-1; i++){
		if(document.getElementById("error" + i +"") != null){
			document.getElementById("error" + i +"").remove();
		}
		var error_count = 0;
		var columns_count = document.getElementsByTagName("fieldset")[i].getElementsByClassName("columns").length;
		for(var j=0; j<=columns_count-1; j++){
			var check_count=0;
			var input_length = document.getElementsByTagName("fieldset")[i].getElementsByClassName("columns")[j].getElementsByTagName("input").length
			for(var k = 0; k<=input_length-1;k++){
				if(document.getElementsByTagName("fieldset")[i].getElementsByClassName("columns")[j].getElementsByTagName("input")[k].checked === false){
					check_count += 1;
					if(check_count === 6 && error_count === 0) {
						document.getElementsByTagName("fieldset")[i].insertAdjacentHTML("beforebegin","<label style='color:red' id='error" + i +"'>未回答の項目があります</label>");
						error_count += 1;
						event.preventDefault();
					}
				}else if(document.getElementsByTagName("fieldset")[i].getElementsByClassName("columns")[j].getElementsByTagName("input")[k].checked === true){
					break;
				}
			}
		}
	}
	if(document.getElementById("update").value === "update"){
		var newAnswer = [];
		[].forEach.call(document.forms[0],function(el){
			if(el.checked === true){
				newAnswer.push(el.id);
			}
		});
		
		
		var filtered = newAnswer.filter(function(el,index){
			return (oldAnswer[index] === el);
		})
		if(oldAnswer.length === filtered.length){
			var update_check_old = window.confirm("前回の解答から変更がありません。更新しますか？")
			if(update_check_old == false){
				event.preventDefault();
			}
			return;
		};
		
		var update_check = window.confirm("回答を更新しますか？OKを押した場合更新を開始します。")
		if(update_check == false){
			event.preventDefault();
		}
	}
})

window.onbeforeunload = function(event){
	console.log(event.srcElement.activeElement)
	if(event.srcElement.activeElement.id != "sendAnswer"){
		event = event || window.event; 
		event.returnValue = '入力中のページから移動しますか？';
	}
}

window.onload = function(){
	if(document.getElementById("update").value === "update"){
		var update_check = window.confirm("前回の回答があります。回答を更新しますか？いいえを押した場合は前の画面に戻ります。")
		if(update_check == false){
			history.back();
		}
		[].forEach.call(document.forms[0],function(el){
			if(el.checked == true){
				oldAnswer.push(el.id);
			};
		});
	}
}
	
