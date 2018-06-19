$(document).ready(function(){
	$("#login").click(function(e){
		e.preventDefault();
		var email = $('#email').val();
		var password = $('#password').val();
		var credentials = {"email":email,"password":password};
		var jsonConvertedData = JSON.stringify(credentials);
		$.ajax({ 
			url:"http://173.249.47.91:8080/api/login",
			type:"POST", 
			contentType: "application/json; charset=utf-8",
			data: jsonConvertedData, 
			async: true,
		}).done(function(data, statusText, xhr){
			var status = xhr.status;
			var head = xhr.getAllResponseHeaders();
			if(xhr.responseText.indexOf('Login bem sucedido') != -1){
				alert('Login bem sucedido');
				window.location.replace('dashboard.html');
			}
			if(xhr.responseText.indexOf('Usuário e/ou senha inválidos') != -1){
				alert('Usuário e/ou senha inválidos');
			}
			if(xhr.responseText.indexOf('Email não cadastrado') != -1){
				alert('Email não cadastrado');
			}
		});
	});
});