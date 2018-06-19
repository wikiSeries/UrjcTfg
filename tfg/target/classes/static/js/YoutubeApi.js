$(document).ready(function(){
	$("#textoConsulta").keyup(function(event){
		if(event.keyCode === 13){
			buscarVideo();
		}
	})
})

function buscarVideo(){
	var query = document.getElementById("textoConsulta").value;
	
	$.get("https://www.googleapis.com/youtube/v3/search",{
		part:'snippet, id',
		type:'video',
		q:query,
		order: 'relevance',
		maxResults: 10,
		videoDuration: 'short',
		regionCode: 'ES',
		key:'AIzaSyA7StfLebxLwS0kpD08wroknCluZ9AFi7w'},
		mostrarVideos, 'json');
	
}

function mostrarVideos(data){
	limpiarTablonResultados();
	
	for(var i in data.items){
		var idVideo = data.items[i].id.videoId;
		crearIframe(idVideo);
	}
	
	if(data.items.length == 0){
		var parrafo = document.createElement("p");
		parrafo.className = "text-center spaceTop-25"
		var parrafoText = document.createTextNode("No se han encontrado resultados");
		
		parrafo.appendChild(parrafoText);
		document.getElementById("tablonResultados").appendChild(parrafo);
	}
}

function crearIframe(idVideo){
	
	var url = "https://www.youtube.com/embed/" + idVideo;
	
	var iframe = document.createElement("iframe");
	iframe.className = "myIframe";
	iframe.type = "text/html";
	iframe.width = "100%";
	iframe.height = "360px";
	iframe.src = url;
	iframe.frameborder = "0";
	
	var botonGuardar = document.createElement("button");
	botonGuardar.type = "submit";
	botonGuardar.className = "MyButton ";
	botonGuardar.id = idVideo;
	botonGuardar.onclick = function(){
		var idButton = "#" + idVideo;
		$(idButton).html("<i class = 'fa fa-spinner fa-spin'></i> Guardando...");
		guardarIdVideo(idVideo);
	}
	var textoBoton = document.createTextNode("Guardar");
	botonGuardar.appendChild(textoBoton);
	
	var videoContainer = document.createElement("div");
	videoContainer.className = "text-center spaceTop-25";
	
	videoContainer.appendChild(iframe);
	videoContainer.appendChild(botonGuardar);
	
	document.getElementById("tablonResultados").appendChild(videoContainer);
}

function limpiarTablonResultados(){
	var tablon = document.getElementById("tablonResultados");
	while (tablon.firstChild) {
		tablon.removeChild(tablon.firstChild);
	}
	
}

function limpiarTextoConsulta(){
	document.getElementById("textoConsulta").value = null;
}

function limpiarModal(){
	limpiarTextoConsulta();
	limpiarTablonResultados();
}

function guardarIdVideo(idVideo){
	var idSerie = document.getElementById("serieActual").getAttribute("data-idSerie");
	
	$.ajax({
		type: "PUT",
		url: "/GuardarIdVideo",
		data:  {
			id: idVideo,
			idSerie: idSerie
		},
		contentType:"application/json",
		dataType:"text"
	
	}).done(function(data){
		/*bootbox.alert(data, function(){ 
			location.reload();
		});*/
		setTimeout(function(){
			location.reload();
		}, 2000);
	});
}
