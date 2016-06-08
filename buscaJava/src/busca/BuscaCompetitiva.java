package busca;

import java.util.LinkedList;
import java.util.List;

public class BuscaCompetitiva extends Busca {

	private int profundidade = 3;

	public BuscaCompetitiva(int profundidade) {
		if (profundidade < 3) {
			throw new IllegalArgumentException("Profundidade não pode ser menor que 3.");
		}
		this.profundidade = profundidade;
	}

	@Override
	public Nodo busca(final Estado inicial) throws Exception {
		final Nodo raiz = new Nodo(inicial, null);
		criarArvore(raiz);
		return null;
	}

	private void criarArvore(final Nodo raiz) {
		final LinkedList<Nodo> abertos = new LinkedList<Nodo>();
		abertos.add(raiz);

		int count = 1;
		while (!(parar || abertos.isEmpty()) && count < profundidade) {
			final Nodo n = abertos.removeFirst();
			abertos.addAll(sucessores(n));
		}
	}

	private int findMin(final List<Nodo> nodos) {
		if (nodos.isEmpty()) {
			throw new IllegalArgumentException("O parâmetro 'nodos' não pode ser empty.");
		}

		int menor = cast(nodos.get(0)).h();
		for (int i = 1; i < nodos.size(); i++) {
			int h = cast(nodos.get(i)).h();
			if (h < menor) {
				menor = h;
			}
		}

		return menor;
	}

	private int findMax(final List<Nodo> nodos) {
		if (nodos.isEmpty()) {
			throw new IllegalArgumentException("O parâmetro 'nodos' não pode ser empty.");
		}

		int maior = cast(nodos.get(0)).h();
		for (int i = 1; i < nodos.size(); i++) {
			int h = cast(nodos.get(i)).h();
			if (h > maior) {
				maior = h;
			}
		}

		return maior;
	}

	private Heuristica cast(Nodo nodo) {
		return (Heuristica) nodo.estado;
	}
}
