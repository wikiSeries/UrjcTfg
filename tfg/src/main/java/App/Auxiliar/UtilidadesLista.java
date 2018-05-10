package App.Auxiliar;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UtilidadesLista <E>{
	
	public List<List<E>> dividirListaPorNumeroElementosEnSubLista(List<E> lista, int numeroElementosPorSubLista){
		List<List<E>> listaResultado = new ArrayList<List<E>>();
		List<E> listaAux = new ArrayList<E>();
		Iterator <E> iterator = lista.iterator();
		while(iterator.hasNext()) {
			if(listaAux.size() >= numeroElementosPorSubLista) {
				listaResultado.add((ArrayList<E>) listaAux);
				listaAux = new ArrayList<E>();
			}
			
			listaAux.add(iterator.next());
			if(!iterator.hasNext()) {
				listaResultado.add((ArrayList<E>) listaAux);
			}
		}
		
		return listaResultado;
	}
	
	//Revisar
	public List<List<E>> dividirListaPorNumeroDeSubListas(List<E> lista, int numeroSubListas){
		int numeroElementosPorSubLista = lista.size() / numeroSubListas;
		return dividirListaPorNumeroElementosEnSubLista(lista, numeroElementosPorSubLista);
	}
}
