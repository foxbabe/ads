;(function (window,undefined){
    var onloadFn,ajax,getUrlParam,onload;
    //封装获取url参数
    getUrlParam = function(inUrl){
        var url = inUrl||location.search; //获取url中"?"符后的字串  
        var theRequest = new Object();  
        if (url.indexOf("?") != -1) {
            var str;
            if(inUrl){
            str = url.replace(/\S*\?/,"");       
            }else{
            str = url.substr(1);
            }  
           strs = str.split("&");  
           for(var i = 0; i < strs.length; i ++) {  
              theRequest[strs[i].split("=")[0]]=unescape(strs[i].split("=")[1]);  
           }  
        }  
        return theRequest;  
    }
    //封装ajax函数
    ajax = function (opt) {
        opt = opt || {};
        opt.type = opt.type.toUpperCase() || 'POST';
        opt.url = opt.url || '';
        opt.async = opt.async || true;
        opt.data = opt.data || null;
        opt.success = opt.success || function () {};
        opt.contentType = opt.contentType || 'application/x-www-form-urlencoded;charset=utf-8'
        var xmlHttp = null;
        if (XMLHttpRequest) {
            xmlHttp = new XMLHttpRequest();
        }
        else {
            xmlHttp = new ActiveXObject('Microsoft.XMLHTTP');
        }var params = [];
        for (var key in opt.data){
            params.push(key + '=' + opt.data[key]);
        }
        if (opt.type.toUpperCase() === 'POST') {
            xmlHttp.open(opt.type, opt.url, opt.async);
            xmlHttp.setRequestHeader('Content-Type',opt.contentType);
            xmlHttp.send(opt.data);
        }
        else if (opt.type.toUpperCase() === 'GET') {
            var postData = params.join('&');
            var postParams = postData ? ('?' + postData) : "";
            xmlHttp.open(opt.type, opt.url + postParams, opt.async);
            xmlHttp.send(null);
        } 
        xmlHttp.onreadystatechange = function () {
            if (xmlHttp.readyState == 4 && xmlHttp.status == 200) {
                opt.success(xmlHttp.responseText);
            }
        };
    }
    //封装window.onload
    onload = function (func){ 
        var oldonload=window.onload; 
        if(typeof window.onload!='function'){ 
            window.onload=func; 
        }else{ 
            window.onload=function(){ 
                oldonload(); 
                func(); 
            } 
        } 
    } 
    //初始执行函数
    onloadFn=function(){
        var getCallUrl = getUrlParam();
        //使用image标签  避免跨域问题
        var img=new Image();
        img.src= getCallUrl.callUrl
        // ajax({
        //     type:"GET",
        //     url:getCallUrl.callUrl
        // })
    }
    onload(onloadFn)   
 window.$tyad = {
     ajax:ajax,
     getUrlParam:getUrlParam,
     onload:onload
 }    
})(window);

// version 1.0.0

