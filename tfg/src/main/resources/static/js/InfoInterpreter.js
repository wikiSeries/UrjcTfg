var TFG = (function($, Promise){
	
	var DATA = (function(){
		
		var promiseLoadSections = function(name){
			return new Promise(function(resolve, reject){
				$.ajax({
					url: 'https://es.wikipedia.org/w/api.php',
					data: { action: 'parse', prop: 'sections', page: name, format: 'json' },
					dataType: 'jsonp',
					success: function(data){
						return resolve(data);
					},
					error: function(){
						var error = new Error("Se ha producido un error al traer las secciones de la pagina de wikipedia");
						return reject(error);
					}
				});
			});
		}
		
		function checkAndResolveAmbiguity(result){
			
			var sections = result.parse.sections;	
			var thereIsAmbiguity = (sections.length === 0);
			
			if(thereIsAmbiguity){				
				return resolveAmbiguity(result);				
			}
			else{
				return sections;
			}
		}
		
		function resolveAmbiguity(result){
			var nameM = result.parse.title + " (actor)";
			var nameF = result.parse.title + " (actriz)";
			
			var pM = promiseLoadSections(nameM)
				.then(function(data){
					return data;
				});
				
			var pF = promiseLoadSections(nameF)
				.then(function(data){	
					return data;
				});
			
			return Promise.all([pM, pF])
				.then(function(results){
					dataM = results[0];
					dataF = results[1];
					
					if(dataM.error != null && dataF.error != null){
						var error = new Error("No se han encontrado resultados");
						throw error;
					}
					else if(dataM.error == null){
						return dataM.parse.sections;
					}
					else{
						return dataF.parse.sections;
					}
				});
		}
		
		function GetEntitySection(data){
			var nameSection = data.parse.sections[0].line;
			var textSection = data.parse.text["*"];

			var entitySection = {
				key: nameSection,
				value: textSection
			};
		   
			return entitySection;
		}
		
		function GetInfoSections(sections){
			var promisesColl = [];
			
			for(i = 0; i < sections.length; i++){
				
				var title = sections[i].fromtitle;
				var indexSection = sections[i].index;
				
				promisesColl.push(promiseGetInfoSections(title, indexSection)
					.then(function(data){
						return GetEntitySection(data);
					}));			   
			}
			
			return Promise.all(promisesColl)
				.then(function(datas){
					return datas;
				});
			
		}		
		
		var promiseGetInfoSections = function(title, indexSection){
			
			return new Promise(function(resolve, reject){
				$.ajax({
					url: 'https://es.wikipedia.org/w/api.php',
					data: { action: 'parse', section: indexSection, prop: 'text|sections', page: title, format: 'json' },
					dataType: 'jsonp',
					success: function(data){
						return resolve(data);
					},
					error: function(){
						var error = new Error("Se ha producido un error al intentar traer la informacion de la seccion " + indexSection + " del/la interprete " + title);
						return reject(error);
					}
				});
			});
			
		}
			
		function parseEntitiesSections(entitiesSections){
			
			var sections = {};
			
			for(i = 0; i < entitiesSections.length; i++){
				var nameSection = entitiesSections[i].key;
				var textSection = entitiesSections[i].value;
				
				sections[nameSection] = textSection;
			}
			
			return sections;
			
		}
		
			
		return{
			Load: function(name){
				return promiseLoadSections(name)
					.then(checkAndResolveAmbiguity)
					.then(GetInfoSections)
					.then(function(data){
						return parseEntitiesSections(data);
					});
					
			}
		}
	})();
	
	var VIEW = (function(){
		
		var viewConfig = {
			"idElement" : "#tablonInfoInterpreter",
			"idElementLoading": "#iconLoading",
			"messageError": "Aun no hay registrado contenido adicional acerca del actor/actriz seleccionado",
			"sectionToShow" : [
				"Vida personal",
				"Vida privada",
				"Biografía",
				"Filmografía"
			]
		}
		
		return{
			showInfo: function(infoSections){
				$(viewConfig.idElementLoading).css("display", "none");
				for(i = 0; i < viewConfig.sectionToShow.length; i++){
					$(viewConfig.idElement).append(infoSections[viewConfig.sectionToShow[i]]);
				}
			},
			
			showMessageError: function(){
				$(viewConfig.idElementLoading).css("display", "none");
				$(viewConfig.idElement).append("<p>" + viewConfig.messageError + "</p>");

			}
			
		}
		
	})();
	
	function InfoInterpreterFromWikiPedia(name){

		this.InfoInterpreter = {};
		this.InfoInterpreter.Loaded = false;
		this.InfoInterpreter.Name = name;
		this.InfoInterpreter.Sections;
		
		var infoInterpreter = this.InfoInterpreter;
		
		var dataInit = DATA.Load(name)
			.then(function(data){
				infoInterpreter.Sections = data; 
				infoInterpreter.Loaded = true;
				VIEW.showInfo(data);
			})
			.catch(function(error){
				VIEW.showMessageError();
				console.error(error);
			});
		
		return infoInterpreter;
	}
	
	return {
		InfoInterpreter: function(nameInterpreter){
			return new InfoInterpreterFromWikiPedia(nameInterpreter);
		}
	}
	
})($, Promise);