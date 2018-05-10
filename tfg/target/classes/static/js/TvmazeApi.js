function buscarListaSeries(){
	var consulta = document.getElementById("tituloSerie").value;
	$.get("https://api.tvmaze.com/search/shows?q="+consulta, mostrarResultadosBusqueda, "json");
}

function mostrarResultadosBusqueda(resultado){
	limpiarTablon("tablonResultados");
	
	if(resultado.length > 0){
		for(var i in resultado){
			if(resultado[i].show.image != null){
				/*var id = resultado[i].show.id;
				var titulo = resultado[i].show.name;
				var generos = resultado[i].show.genres;
				var fechaEstreno = resultado[i].show.premiered;
				var puntuacion = resultado[i].show.rating.average;
				var urlImage = resultado[i].show.image.medium;
				var descripcion = resultado[i].show.summary;
							
				var urlImageSecure = urlImage.includes("https") ? urlImage : urlImage.replace("http", "https");*/
				
				var serie = resultado[i].show;
				mostrarImagen(serie);
			}				
		}
	}
	else{
		var contenedorMensaje = document.createElement("div");
		contenedorMensaje.className = "col-lg-12 text-center spaceTop-25";
		
		var parrafo = document.createElement("p");
		var texto = document.createTextNode("No se han encontrado resultados");
		
		parrafo.appendChild(texto);
		contenedorMensaje.appendChild(parrafo);
		
		document.getElementById("tablonResultados").appendChild(contenedorMensaje);
	}
	
}

function limpiarTablon(idTablon){
	var tablon = document.getElementById(idTablon);
	while (tablon.firstChild) {
		tablon.removeChild(tablon.firstChild);
	}
	
}	
function mostrarImagen(serie){
	
	/*var generos = "";
	for(var i in cadenaGeneros){
		if(i == cadenaGeneros.length - 1){
			generos += cadenaGeneros[i];
		}
		else{
			generos += cadenaGeneros[i] + ",";
		}
	}*/

	var textoBoton = document.createTextNode("Guardar");
	var botonGuardar = document.createElement("button");
	botonGuardar.type = "submit";
	botonGuardar.className = "MyButton spaceTop-5";
	botonGuardar.onclick = function(){
		
		$.ajax({
			type: "POST",
			url: "/GuardarSerieApi",
			data: JSON.stringify(serie/*{
				idSerie: idSerie,
				titulo: titulo,
				generos: generos,
				estreno: estreno,
				puntuacion: puntuacion,
				urlImagen: urlImagen,
				descripcion: descripcion
				
			}*/),
			contentType:"application/json",
			dataType:"text",
		}).done(function(data){
			if(data != "error"){
				completarDatosSerie(data);
			}
			else{
				alert("Error al guardar Serie.");
			}
			
		});
		
	}
	var urlImage = serie.image.medium;
	var urlImageSecure = urlImage.includes("https") ? urlImage : urlImage.replace("http", "https");

	var imagen = document.createElement("img");
	imagen.src = urlImageSecure;
	
	var columna = document.createElement("div");
	columna.className = "col-lg-3 spaceTop-25 text-center";
	
	botonGuardar.appendChild(textoBoton);

	columna.appendChild(imagen);
	columna.appendChild(botonGuardar);
	document.getElementById("tablonResultados").appendChild(columna);
}

function completarDatosSerie(idSerieApi){
	$.get("https://api.tvmaze.com/shows/" + idSerieApi + "?embed[]=cast&embed[]=seasons&embed[]=episodes", completarDatos, "json");
}

function completarDatos(serie){
	
	var dialog = bootbox.dialog({
	    message: '<p class="text-center"><i class="fa fa-spin fa-spinner"></i> Guardando serie...</p>',
	    closeButton: false
	});
	
	$.ajax({
		type: "POST", 
		url: "/GuardarDatosSerie",
		data: JSON.stringify({
			idSerie: serie.id,
			elenco: serie._embedded.cast,
			temporadas: serie._embedded.seasons,
			episodios: serie._embedded.episodes
		}),
		contentType: "application/json",
	}).done(function(){
		setTimeout(function(){
			dialog.modal('hide');
		}, 5000);
	});
}


