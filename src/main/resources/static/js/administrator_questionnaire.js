document.body.addEventListener("click",function(event){
    if(event.target.id.indexOf("questionnaire_review") !== -1){
        var type = "view";
        var id = event.target.dataset.question_id;
        var target = event.target;
        var admin_view = new adminQuestionnaire();
        admin_view.ajax(type,id,target);
    }
})

class adminQuestionnaire{
    constructor(){
        this.req = new XMLHttpRequest();
        this.context_path = location.pathname.substring(0,1+location.pathname.substring(1).indexOf("/"));
        this.token = document.getElementsByName("csrf-token")[0].content;
    }
    
    ajax(type,id,target){
        this.req.open('GET',this.context_path + "/questionnaire_edit?id="+id+"&type="+type,true)
        this.req.setRequestHeader('X-CSRF-Token', this.token)
        this.req.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded;charset=UTF-8');
        this.req.send(null);
    	this.req.onreadystatechange = function(){
            if(this.readyState == 4){
                if(this.status == 200){
                    console.log("通信完了");
                    document.getElementById(target.id).style.color = JSON.parse(this.responseText) == "true"?"#0080ff":"#00aa00";
                    document.getElementById(target.id).style.borderColor = JSON.parse(this.responseText) == "true"?"#0080ff":"#00aa00";
                    document.getElementById(target.id).innerText = JSON.parse(this.responseText) == "true"?"表示":"非表示";
                }else{
                    console.log("サーバーエラーが発生しました");
                }
            }else{
                console.log("通信中");
            }
        }
    }
}