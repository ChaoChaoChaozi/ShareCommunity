$(document).ready(function(){
	//禁止鼠标右键菜单和F12打开控制台看源码
	function click(e) {
	if (document.all) {
	if (event.button==2||event.button==3) { 
	alert("欢迎光临寒舍，有什么需要帮忙的话，请与站长联系！谢谢您的合作！！！");
	oncontextmenu='return false';
	}
	}
	if (document.layers) {
	if (e.which == 3) {
	oncontextmenu='return false';
	}
	}
	}
	if (document.layers) {
	document.captureEvents(Event.MOUSEDOWN);
	}
	document.onmousedown=click;
	document.oncontextmenu = new Function("return false;")
	document.onkeydown =document.onkeyup = document.onkeypress=function(){ 
	if(window.event.keyCode == 123) { 
	window.event.returnValue=false;
	return(false); 
	} 
	}
	// <--123——112是F1-F12的代码数-->
	
	$('#loginbtn').live('click', function() {
		var that = this;
		$(this).addClass('loading');
		var email = $.trim($('#email').val());
		var password = $('#password').val();
		var error_area=$('#error');
		var err_msg="";

		$(error_area).find('ul:first').text('');
		$(error_area).addClass('hidden');
		$('#emailTip').text('');
		$('#paswordTip').text('');
		$('#email').parent('div.field:first').removeClass('error');
		$('#password').parent('div.field:first').removeClass('error');

		$.ajax({
			url: basePath+'/account/login',
			type: 'POST',
			dataType: 'json',
			data: {email: email,
				   password: password},
		})
		.success(function(data) {
			$(that).removeClass('loading');
			if(data.status==SUCCESS_ACCOUNT_LOGIN){
				//self.location = basePath + '/';
				location.reload();
			} else{

				if(data.status == ERROR_EMAIL_NOT_REG){
					err_msg="账户不存在";
					$('#emailTip').text('账户不存在');
					$('#email').parent('div.field:first').addClass('error');
				} else if(data.status == ERROR_EMAIL_EMPTY){
					err_msg="请输入邮箱";
					$('#emailTip').text('请输入邮箱');
					$('#email').parent('div.field:first').addClass('error');
				} else if(data.status == ERROR_EMAIL_FORMAT){
					err_msg="请输入正确的邮箱地址";
					$('#emailTip').text('请输入正确的邮箱地址');
					$('#email').parent('div.field:first').addClass('error');
				} else if(data.status == ERROR_PWD_EMPTY){
					err_msg="请输入密码";
					$('#paswordTip').text('请输入密码');
					$('#password').parent('div.field:first').addClass('error');
				} else if(data.status == ERROR_PWD_DIFF){
					err_msg = "密码错误"
					$('#paswordTip').text('密码错误');
					$('#password').parent('div.field:first').addClass('error');
				} else{
					err_msg="邮箱或密码错误";
					$('#paswordTip').text('邮箱或密码错误');
					$('#password').parent('div.field:first').addClass('error');
				}	
				var msg=$('<li>'+err_msg+'</li>')
				$(error_area).find('ul:first').prepend($(msg));
				$(error_area).text(err_msg);
				$(error_area).removeClass('hidden');
			}

			
		})
		.fail(function() {
			console.log("error");
		})
		.always(function() {
			console.log("complete");
		});
		
	});

	
	$('#registerbtn').live('click', function(){
		self.location = basePath + '/account/register';
	});



})