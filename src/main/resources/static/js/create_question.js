var original_questions = document.getElementById("questions[0]").cloneNode(true);
var original_question = document.getElementsByName("questionnaireCategoryFormList[0].childNameList[0].parentName")[0].cloneNode(true);
var original_delete_question = document.getElementById("delete_question[0]").cloneNode(true);
var questions = document.getElementById("questions");
var select_value = {
	target_num: ""
};
var prev_num = 0;

document.getElementById("questionnaire_form");

document.body.addEventListener('click', function(event){
	if(event.target.id === "add_questions"){
		var questions_num = questions.getElementsByTagName("div").length;
		add_qes = original_questions.cloneNode(true);
		add_qes.id = "questions["+questions_num+"]";
		add_qes.getElementsByTagName("input")[0].name="questionnaireCategoryFormList["+questions_num+"].parentName";
		add_qes.getElementsByTagName("input")[1].name="questionnaireCategoryFormList["+questions_num+"].childNameList[0].parentName";
		add_qes.dataset.questions_num = questions_num;
		questions.appendChild(add_qes);
		var elementHtml = document.documentElement;
		var bottom = elementHtml.scrollHeight - elementHtml.clientHeight;
		window.scroll(0, bottom);
	}else if(event.target.attributes[0].value === "delete_questions"){
		var confirm_count = 0;
		var delete_confirm = true;
		[].forEach.call(event.target.parentNode.getElementsByTagName("input"),function(el){
			if(el.value !== "" && el.value !== null && confirm_count === 0){
				delete_confirm = window.confirm("設問を削除します。書き込みの途中ですが本当によろしいですか？")
				confirm_count = 1;
			}
		})
		if(delete_confirm === true){
			var questions_num = event.target.parentNode.dataset.questions_num;
			event.target.parentNode.remove();
			questions.getElementsByTagName("div").length;
			if(questions.getElementsByTagName("div").length !== 0){
				for(var i = Number(questions_num) + 1;i<= questions.getElementsByTagName("div").length;i++){
					modification_num = i - 1;
					document.getElementById("questions["+ i +"]").dataset.questions_num = modification_num;
					document.getElementById("questions["+ i +"]").getElementsByTagName("input")[0].name="questionnaireCategoryFormList["+modification_num+"].parentName";
					document.getElementById("questions["+ i +"]").getElementsByTagName("input")[1].name="questionnaireCategoryFormList["+modification_num+"].childNameList[0].parentName";
					document.getElementById("questions["+ i +"]").id="questions["+ modification_num +"]";
				}
			}
		};
	}else if(event.target.attributes[0].value === "delete_question"){
		var questions_num = event.target.parentNode.dataset.questions_num;
		var question_num = Number(event.target.dataset.delete_num);
		if(event.target.parentNode.getElementsByTagName("input").length === 2){
			var confirm_count = 0;
			var delete_confirm = true;
			[].forEach.call(event.target.parentNode.getElementsByTagName("input"),function(el){
				if(el.value !== "" && el.value !== null && confirm_count === 0){
					delete_confirm = window.confirm("設問を削除します。書き込みの途中ですが本当によろしいですか？")
					confirm_count = 1;
				}
			})
			if(delete_confirm === true){
				var questions_num = event.target.parentNode.dataset.questions_num;
				event.target.parentNode.remove();
				questions.getElementsByTagName("div").length;
				if(questions.getElementsByTagName("div").length !== 0){
					for(var i = Number(questions_num) + 1;i<= questions.getElementsByTagName("div").length;i++){
						modification_num = i - 1;
						document.getElementById("questions["+ i +"]").dataset.questions_num = modification_num;
						document.getElementById("questions["+ i +"]").getElementsByTagName("input")[0].name="questionnaireCategoryFormList["+modification_num+"].parentName";
						document.getElementById("questions["+ i +"]").getElementsByTagName("input")[1].name="questionnaireCategoryFormList["+modification_num+"].childNameList[0].parentName";
						document.getElementById("questions["+ i +"]").id="questions["+ modification_num +"]";
					}
				}
			};
		}else{
			event.target.previousElementSibling.remove();
			if(event.target.parentNode.getElementsByTagName("input").length - 1 !== question_num){
				var return_num = event.target.parentNode.getElementsByTagName("input").length - 2;
				for(var i = question_num;i<=return_num;i++ ){
					modification_num = i + 1;
					document.getElementsByName("questionnaireCategoryFormList["+questions_num+"].childNameList[" + modification_num +"].parentName")[0].nextElementSibling.id = "delete_question["+ i +"]";
					document.getElementsByName("questionnaireCategoryFormList["+questions_num+"].childNameList[" + modification_num +"].parentName")[0].nextElementSibling.dataset.delete_num = i;
					document.getElementsByName("questionnaireCategoryFormList["+questions_num+"].childNameList[" + modification_num +"].parentName")[0].name = "questionnaireCategoryFormList["+questions_num+"].childNameList[" + i +"].parentName";
				}
			}
			event.target.nextElementSibling.remove();
			event.target.remove(); 
		}
	}else if(event.target.attributes[0].value === "add"){
		var qes_num = Number(event.target.parentNode.dataset.questions_num)
		var next_qes_num = document.getElementById("questions["+ qes_num +"]").getElementsByTagName("input").length - 1;
		var next_qes = original_question.cloneNode(true);
		next_qes.name = "questionnaireCategoryFormList[" + qes_num + "].childNameList["+ next_qes_num + "].parentName";
		var next_delete_qes = original_delete_question.cloneNode(true);
		next_delete_qes.id = "delete_question[" + next_qes_num + "]";
		next_delete_qes.dataset.delete_num = next_qes_num;
		event.target.insertAdjacentHTML("beforebegin",next_qes.outerHTML + next_delete_qes.outerHTML +"<br>")
	}else if(event.target.id === "up"){
		scrollTo(0, 0);
	}else if(event.target.id === "down"){
		var elementHtml = document.documentElement;
		var bottom = elementHtml.scrollHeight - elementHtml.clientHeight;
		window.scroll(0, bottom);
	}else if(event.target.id === "outline"){
	}else if(event.target.id === "all_check"){
		[].forEach.call(document.getElementById("target_list").getElementsByTagName('input'), function(el) {
			el.checked=true;
		});
		[].forEach.call(document.getElementById("target_list").getElementsByTagName('label'), function(el) {
			el.style.backgroundColor = "#E0E0E0";
		});
	}else if(event.target.id === "all_cancel"){
		[].forEach.call(document.getElementById("target_list").getElementsByTagName('input'), function(el) {
			el.checked=false;
		});
		[].forEach.call(document.getElementById("target_list").getElementsByTagName('label'), function(el) {
			el.style.backgroundColor = "#FFF";
		});
	}else if(event.target.id === "target_title"){
		if(document.getElementById("target_all").style.display === "" &&document.getElementById("target_list").style.display === ""){
			document.getElementById("target_all").style.display = "table";
			document.getElementById("target_list").style.display = "table";
		}else{
			document.getElementById("target_all").style.display = "";
			document.getElementById("target_list").style.display = "";
		}
	}
	if(event.target.parentNode !== null){
		event.target.parentNode.style.backgroundColor = event.target.checked === true? "#E0E0E0":"#FFF";
	}
})

/*Tabキーを押すとカラムを追加*/
document.getElementById("questions").addEventListener('keydown', function(event){
	var lastChild = document.activeElement.parentNode.getElementsByTagName("input").length - 1;
	if(navigator.platform.indexOf("Win") != -1 && (event.shiftKey === true || event.ctrlKey === true || event.altKey === true)){
		return false;
	}else if(navigator.platform.indexOf("Mac") != -1 && (event.shiftKey === true || event.ctrlKey === true || event.altKey === true)){
		return false;
	}else if(navigator.platform.indexOf("Win") != -1 && event.keyCode === 9 && document.activeElement.tagName === "INPUT" && document.activeElement.parentNode.getElementsByTagName("input")[lastChild] === document.activeElement){
		add_key();
	}else if(navigator.platform.indexOf("Mac") != -1 && event.keyCode === 48 && document.activeElement.tagName === "INPUT" && document.activeElement.parentNode.getElementsByTagName("input")[lastChild] === document.activeElement){
		add_key();
	}
})

/*バリデーションチェック*/
document.body.addEventListener('submit',function(event){
	var checker = 0;
	var title = document.getElementById("title");
	var description = document.getElementById("description");
	var matcher=/^\s|^　/;
	var length = document.getElementsByClassName("error_messages_list").length-1;
	for(var i = 0; i <= length; i++){
		document.getElementsByClassName("error_messages_list")[0].remove();
	}
	[].forEach.call(document.getElementsByName("questionnaireTargetList"),function(el){
		if(el.checked === false){
			++checker;
		}
	});
	if(document.getElementsByName("questionnaireTargetList").length === checker){
		document.getElementById("target_title").insertAdjacentHTML('beforebegin', "<label class='error-messages error_messages_list'>アンケート対象が選択されていません。</label><br class='error_messages_list'>");
	}
	checker = 0;
	title.value = title.value.trim();
	description.value = description.value.trim();
	if(title.value.match(matcher) || title.value===''){
		title.insertAdjacentHTML('beforebegin', "<label class='error-messages error_messages_list'>アンケートタイトルが未入力です。</label><br class='error_messages_list'>");
	}
	if(description.value.match(matcher) || description.value===''){
		description.insertAdjacentHTML('beforebegin', "<label class='error-messages error_messages_list'>アンケートの説明が未入力です。</label><br class='error_messages_list'>");
	}
	
	[].forEach.call(document.getElementById("questions").getElementsByTagName("input"),function(el){
		el.value = el.value.trim();
		if(el.value.match(matcher) || el.value===''){
			el.insertAdjacentHTML('beforebegin', "<label class='error-messages error_messages_list'>値が未入力です。</label><br class='error_messages_list'>");
		}
	})
	if(document.getElementsByClassName("error_messages_list").length !== 0){
		event.preventDefault();
	}
})

//タブキーでカラムを増やす実装
function add_key(){
	var qes_num = Number(document.activeElement.parentNode.dataset.questions_num);
	var next_qes_num = document.getElementById("questions["+ qes_num +"]").getElementsByTagName("input").length - 1;
	var next_qes = original_question.cloneNode(true);
	next_qes.name = "questionnaireCategoryFormList[" + qes_num + "].childNameList["+ next_qes_num + "].parentName";
	var next_delete_qes = original_delete_question.cloneNode(true);
	next_delete_qes.id = "delete_question[" + next_qes_num + "]";
	next_delete_qes.dataset.delete_num = next_qes_num;
	var length = document.getElementById("questions").querySelectorAll("br").length-1;
	document.getElementById("questions").querySelectorAll("br")[length].insertAdjacentHTML("afterend",next_qes.outerHTML + next_delete_qes.outerHTML +"<br>")
}
