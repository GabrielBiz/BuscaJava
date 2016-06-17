package busca;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class BuscaCompetitiva extends Busca {

	private Nodo resultado;
	private int profundidade = 2;
	private boolean aplicarPoda = true;
	private final Map<Nodo, Integer> nodoHeuristica = new HashMap<>();

	public BuscaCompetitiva(final MostraStatusConsole ms, final int profundidade) {
		super(ms);
		if (profundidade < 3) {
			throw new IllegalArgumentException("Profundidade não pode ser menor que 3.");
		}
		this.profundidade = profundidade - 1;
	}

	public BuscaCompetitiva(final MostraStatusConsole ms, final int profundidade, final boolean aplicarPoda) {
		super(ms);
		if (profundidade < 3) {
			throw new IllegalArgumentException("Profundidade não pode ser menor que 3.");
		}
		this.aplicarPoda = aplicarPoda;
		this.profundidade = profundidade - 1;
	}

	@Override
	public Nodo busca(final Estado inicial) throws Exception {
		status.inicia();
		nodoHeuristica.clear();
		final Nodo raiz = new Nodo(inicial, null);
		if (minMax(raiz)) {
			status.termina(true);
			return resultado;
		}
		status.termina(false);
		throw new RuntimeException("Não foi possível aplicar o algoritmo corretamente.");
	}

	public void vp(int i) {
		Set<Entry<Nodo, Integer>> entrySet = nodoHeuristica.entrySet();
		for (Entry<Nodo, Integer> entry : entrySet) {
			if (entry.getKey().profundidade == i) {
				System.out.print(entry.getKey() + ";");
			}
		}
		System.out.println();
	}

	// public static void main(String[] args) throws Exception {
	// BuscaCompetitiva b = new BuscaCompetitiva(50);
	// EHData e = new EHData(0, Arrays.asList(arA(), arB()));
	// b.busca(e);
	// }
	//
	// public static EHData arA() {
	// EHData a3 = new EHData(8, Collections.emptyList());
	// EHData b3 = new EHData(23, Collections.emptyList());
	//
	// EHData c3 = new EHData(-47, Collections.emptyList());
	// EHData d3 = new EHData(28, Collections.emptyList());
	//
	// EHData e3 = new EHData(-30, Collections.emptyList());
	// EHData f3 = new EHData(-37, Collections.emptyList());
	//
	// EHData g3 = new EHData(3, Collections.emptyList());
	// EHData h3 = new EHData(-41, Collections.emptyList());
	//
	// EHData a2 = new EHData(0, Arrays.asList(a3, b3));
	//
	// EHData b2 = new EHData(0, Arrays.asList(c3, d3));
	//
	// EHData c2 = new EHData(0, Arrays.asList(e3, f3));
	//
	// EHData d2 = new EHData(0, Arrays.asList(g3, h3));
	//
	// EHData a1 = new EHData(0, Arrays.asList(a2, b2));
	//
	// EHData b1 = new EHData(0, Arrays.asList(c2, d2));
	//
	// EHData a0 = new EHData(0, Arrays.asList(a1, b1));
	// return a0;
	// }
	//
	// public static EHData arB() {
	// EHData a3 = new EHData(-19, Collections.emptyList());
	// EHData b3 = new EHData(4, Collections.emptyList());
	//
	// EHData c3 = new EHData(-49, Collections.emptyList());
	// EHData d3 = new EHData(4, Collections.emptyList());
	//
	// EHData e3 = new EHData(43, Collections.emptyList());
	// EHData f3 = new EHData(45, Collections.emptyList());
	//
	// EHData g3 = new EHData(-26, Collections.emptyList());
	// EHData h3 = new EHData(-14, Collections.emptyList());
	//
	// EHData a2 = new EHData(0, Arrays.asList(a3, b3));
	//
	// EHData b2 = new EHData(0, Arrays.asList(c3, d3));
	//
	// EHData c2 = new EHData(0, Arrays.asList(e3, f3));
	//
	// EHData d2 = new EHData(0, Arrays.asList(g3, h3));
	//
	// EHData a1 = new EHData(0, Arrays.asList(a2, b2));
	//
	// EHData b1 = new EHData(0, Arrays.asList(c2, d2));
	//
	// EHData a0 = new EHData(0, Arrays.asList(a1, b1));
	// return a0;
	// }

	public boolean minMax(final Nodo nodo) {
		status.explorando(nodo, status.nroVisitados + 1);
		final boolean isFolha = isFolha(nodo);
		if (!isFolha) {
			final LinkedList<Nodo> sucessores = nodo.getSucessores();
			for (Nodo sucessor : sucessores) {
				if (minMax(sucessor)) {
					final int h = heuristica(sucessor);
					nodoHeuristica.put(nodo, h);
					if (nodo.profundidade == 0) {
						resultado = sucessor;
					}
					if (aplicarPoda && podar(nodo)) {
						break;
					}
				}
			}
		}

		final Nodo pai = nodo.pai;
		if (pai == null) {
			return !isEmpty(nodo);
		}
		if (isEmpty(pai)) {
			return isFolha || !isEmpty(nodo);
		}
		final int hPai = heuristica(pai);
		final int hNodo = heuristica(nodo);
		final int profundidadePai = pai.getProfundidade();
		return profundidadePai % 2 == 0 ? hNodo > hPai : hNodo < hPai;
	}

	public boolean podar(final Nodo nodo) {
		if (nodo == null) {
			throw new IllegalArgumentException("O parâmetro 'nodo' não pode ser nulo.");
		}
		if (isEmpty(nodo)) {
			return false;
		}
		Nodo pai = nodo.getPai();
		if (pai == null) {
			return false;
		}
		final boolean nodoIsMax = isMax(nodo);
		final int hNodo = heuristica(nodo);
		do {
			if (!isEmpty(pai)) {
				final boolean paiIsMax = isMax(pai);
				if (nodoIsMax != paiIsMax) {
					final int hPai = heuristica(pai);
					if (nodoIsMax) {
						if (hPai < hNodo) {
							return true;
						}
					} else {
						if (hPai > hNodo) {
							return true;
						}
					}
				}
			}
			pai = pai.getPai();
		} while (pai != null);
		return false;
	}

	private boolean isMax(final Nodo nodo) {
		if (nodo == null) {
			throw new IllegalArgumentException("O parâmetro 'nodo' não pode ser nulo.");
		}
		final int profundidade = nodo.getProfundidade();
		return profundidade % 2 == 0;
	}

	private boolean isFolha(final Nodo nodo) {
		if (nodo == null) {
			throw new IllegalArgumentException("O parâmetro 'nodo' não pode ser nulo.");
		}
		nodo.criarSucessores();
		return nodo.profundidade == profundidade || nodo.getSucessores().isEmpty();
	}

	private int heuristica(final Nodo nodo) {
		if (nodoHeuristica.containsKey(nodo)) {
			return nodoHeuristica.get(nodo);
		}
		final int h = cast(nodo).h();
		nodoHeuristica.put(nodo, h);
		return h;
	}

	private boolean isEmpty(final Nodo nodo) {
		return !nodoHeuristica.containsKey(nodo);
	}

	private Heuristica cast(final Nodo nodo) {
		return (Heuristica) nodo.estado;
	}
}
