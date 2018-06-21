function cambiarColor(){

	var colorClick = $("#like").attr("data-color");
	var operacion;
	
	if(colorClick == "vacio"){
		
		$("#like").removeClass("fa fa-thumbs-o-up").addClass("fa fa-thumbs-up");
	
		$("#like").attr("data-color", "relleno");
		
		operacion = "sumar";
	}

	else{
		
		$("#like").removeClass("fa fa-thumbs-up").addClass("fa fa-thumbs-o-up");
	
		$("#like").attr("data-color", "vacio");
		
		operacion = "restar";
	}
	
	ActualizarContador(operacion);

}


function ActualizarContador(operacion){
	var idSerie = document.getElementById("serieActual").getAttribute("data-idSerie");
	var usuario = document.getElementById("usuarioActual").getAttribute("data-usuario");
	
	var url = "/ActualizarContador/" + idSerie + "/" + usuario + "/" + operacion;
	
	$.get(url, function(data){
		$("#counterLike").text(data);
	}, "json");
	
}

function anadirSerie(idSerie, nombreUsuario){
	
	var url = "/AñadirSerie/" + idSerie + "/" + nombreUsuario;
	
	$.post(url, "json").done(function(data){
		bootbox.alert(data);
	});
}

function confirmarEliminar(idSerie){
	return bootbox.confirm('¿Esta seguro de que desea eliminar la serie?', function(result) {
		if (result == true) {
			eliminarSerie(idSerie);
		}
	});
}

function eliminarSerie(id){
	var idImage = "#image" + id;
	$(idImage).attr("src", "/Images/loading.gif");
	$.ajax({
		type: "DELETE",
		url: "/EliminarSerie/" + id,
		contentType:"application/json",
		dataType:"text",
	}).done(function(){
		location.reload();
	});
}


 

