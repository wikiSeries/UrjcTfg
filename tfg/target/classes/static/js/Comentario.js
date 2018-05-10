$(document).on('click', '#addacomment', function(){
    $('#addcomment').toggle();
});

function enviarComentario(){
	var comentario = document.getElementById("textoComentario").value;
	var nombre = document.getElementById("usuarioActual").getAttribute("data-usuario");
	var idSerie = document.getElementById("serieActual").getAttribute("data-idSerie");
	
	var dialog = bootbox.dialog({
	    message: '<p class="text-center" id = "modalComentario"><i class="fa fa-spin fa-spinner"></i> Enviando comentario...</p>',
	    closeButton: true
	});
	
	$.ajax({
		type: "POST",
		url: "/EnviarComentario",
		data: JSON.stringify({
			comentario: comentario,
			nombreUsuario: nombre,
			idSerie: idSerie
		}),
		contentType: "application/json",
		dataType: "text"
	}).done(function(data){
		if(data == "ok"){
			$("#modalComentario").text("El comentario ha sido enviado y se publicara una vez validado su contenido, ¡muchas gracias!");
		}
		else{
			$("#modalComentario").text("Error al enviar el comentario. Intentelo mas tarde. Si el error continua, pongase en contacto con el administrador.");

		}
	});
}
