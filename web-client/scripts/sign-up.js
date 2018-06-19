$(document).ready(function(){
	$("#login").click(function(e){
		e.preventDefault();
		var name = $('#name').val();
		var email = $('#email').val();
		var password = $('#password').val();
		var ddd = $('#ddd').val();
		var phone = $('#phone').val();
		var formData = {"email":email,"password":password,"name":name,"phones":[{"ddd":ddd,"phone":phone}]};
		var jsonConvertedData = JSON.stringify(formData);
		$.ajax({ 
			url:"http://173.249.47.91:8080/api/sign-up",
			type:"POST", 
			contentType: "application/json; charset=utf-8",
			data: jsonConvertedData, 
			async: true,
		}).done(function(data, statusText, xhr){
			var status = xhr.status;
			var head = xhr.getAllResponseHeaders();
			if(xhr.responseText.indexOf('Usuario criado com sucesso') != -1){
				alert('Usuario criado com sucesso');
				window.location.replace('index.html');
			}
		});
	});
});