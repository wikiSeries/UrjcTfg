function mostrar(id) {
    var elemento = document.getElementById(id);
    if (elemento.style.display === "none") {
    	elemento.style.display = "block";
    } else {
    	elemento.style.display = "none";
    }
}

function cancelar(id){
	var elementId = "#" + id;
	$(elementId).find("input").each(function (){
		var element = $(this);
		var type = element.attr("type");
		if(type != "submit"){
			element.val("");
		}
	});
	$(elementId).removeClass("in active");
}

function actualizarListas(accion, idEpisodio, nombreUsuario){
	var idOrigen = "#" + idEpisodio + "E";
	var idDestino = "#";
	var idBoton = "#" + idEpisodio + "Mover";
	
	var funcion = "marcarEpisodio";
	var texto = "<span class = '";
	var titulo = "marcar como";
	var claseEliminar;
	var claseAñadir;
	
	if(accion == "Visto"){
		idDestino += "dev-tableV"
		funcion += "('Pendiente','";
		texto += "glyphicon glyphicon-eye-close'></span> Pendiente";
		titulo += " pendiente";
		claseEliminar = "btn-success";
		claseAñadir = "btn-danger";
	}
	else{
		idDestino += "dev-tableP"
		funcion += "('Visto','"; 
		texto += "glyphicon glyphicon-eye-open'></span> Visto"
		titulo += " visto";
		claseEliminar = "btn-danger";
		claseAñadir = "btn-success";
	}
	
	funcion += idEpisodio + "','" + nombreUsuario +"')";
		
	$(idBoton).attr("onclick", funcion);
	$(idBoton).html(texto);
    $(idBoton).attr('title', titulo);
    $(idBoton).removeClass(claseEliminar).addClass(claseAñadir);
	$(idOrigen).prependTo($(idDestino).children().first());
}
