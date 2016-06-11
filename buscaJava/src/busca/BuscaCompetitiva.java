package busca;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiPredicate;

public class BuscaCompetitiva extends Busca {

	private int profundidade = 4;
	private Integer min = null;
	private Integer max = null;

	public BuscaCompetitiva(int profundidade) {
		if (profundidade < 3) {
			throw new IllegalArgumentException("Profundidade não pode ser menor que 3.");
		}
		this.profundidade = profundidade;
	}

	@Override
	public Nodo busca(final Estado inicial) throws Exception {
		final Nodo raiz = new Nodo(inicial, null);
		final Nodo max = encontrar(Arrays.asList(raiz), 1);
		return max;
	}

	public static void main(String[] args) {
		BuscaCompetitiva b = new BuscaCompetitiva(5);

		EHData a0 = new EHData(0, Arrays.asList(arA(), arB()));

		System.out.println(b.encontrar(Arrays.asList(new Nodo(a0, null)), 1));
	}

	public static EHData arA() {
		EHData a3 = new EHData(8, null);
		EHData b3 = new EHData(23, null);

		EHData c3 = new EHData(-47, null);
		EHData d3 = new EHData(28, null);

		EHData e3 = new EHData(-30, null);
		EHData f3 = new EHData(-37, null);

		EHData g3 = new EHData(3, null);
		EHData h3 = new EHData(-41, null);

		EHData a2 = new EHData(0, Arrays.asList(a3, b3));

		EHData b2 = new EHData(0, Arrays.asList(c3, d3));

		EHData c2 = new EHData(0, Arrays.asList(e3, f3));

		EHData d2 = new EHData(0, Arrays.asList(g3, h3));

		EHData a1 = new EHData(0, Arrays.asList(a2, b2));

		EHData b1 = new EHData(0, Arrays.asList(c2, d2));

		EHData a0 = new EHData(0, Arrays.asList(a1, b1));
		return a0;
	}

	public static EHData arB() {
		EHData a3 = new EHData(-19, null);
		EHData b3 = new EHData(4, null);

		EHData c3 = new EHData(-49, null);
		EHData d3 = new EHData(4, null);

		EHData e3 = new EHData(43, null);
		EHData f3 = new EHData(45, null);

		EHData g3 = new EHData(-26, null);
		EHData h3 = new EHData(-14, null);

		EHData a2 = new EHData(0, Arrays.asList(a3, b3));

		EHData b2 = new EHData(0, Arrays.asList(c3, d3));

		EHData c2 = new EHData(0, Arrays.asList(e3, f3));

		EHData d2 = new EHData(0, Arrays.asList(g3, h3));

		EHData a1 = new EHData(0, Arrays.asList(a2, b2));

		EHData b1 = new EHData(0, Arrays.asList(c2, d2));

		EHData a0 = new EHData(0, Arrays.asList(a1, b1));
		return a0;
	}

	public Nodo encontrar(List<Nodo> nodos, int nivel) {
		if (profundidade != nivel) {
			for (Nodo nodo : nodos) {
				final List<Nodo> sucessores = sucessores(nodo);
				if (!sucessores.isEmpty()) {
					nodo.addSucessores(sucessores);
					final Nodo novo = encontrar(sucessores, nivel + 1);
					nodo.estado = novo.estado;
				}
			}
			Nodo resultado;
			if (nivel % 2 == 0) {
				resultado = findMax(nodos);
				int resultadoH = heuristica(resultado);
				if (min == null || min > resultadoH) {
					min = resultadoH;
				}
				max = null;
			} else {
				resultado = findMin(nodos);
				int resultadoH = heuristica(resultado);
				if (max == null || max < resultadoH) {
					max = resultadoH;
				}
				min = null;
			}
			return resultado;
		}
		Nodo resultado;
		if (nivel % 2 == 0) {
			resultado = findMax(nodos);
			int resultadoH = heuristica(resultado);
			if (min == null || min > resultadoH) {
				min = resultadoH;
			}
			min = null;
		} else {
			resultado = findMin(nodos);
			int resultadoH = heuristica(resultado);
			if (max == null || max < resultadoH) {
				max = resultadoH;
			}
			min = null;
		}
		return resultado;
	}

	private Nodo findMin(final List<Nodo> nodos) {
		if (nodos.isEmpty()) {
			throw new IllegalArgumentException("O parâmetro 'nodos' não pode ser empty.");
		}

		Nodo menor = nodos.get(0);
		int menorH = heuristica(menor);
		if (max != null) {
			if (max >= menorH) {
				System.out.println("x" + max);
				System.out.println(menorH);
				return menor;
			}
		}
		for (int i = 1; i < nodos.size(); i++) {
			final Nodo nodo = nodos.get(i);
			int h = heuristica(nodo);
			if (max != null) {
				if (max >= h) {
					System.out.println("x" + max);
					System.out.println(h);
					return nodo;
				}
			}
			if (h < menorH) {
				menorH = h;
				menor = nodo;
			}
		}
		return menor;
	}

	private Nodo findMax(final List<Nodo> nodos) {
		if (nodos.isEmpty()) {
			throw new IllegalArgumentException("O parâmetro 'nodos' não pode ser empty.");
		}

		Nodo maior = nodos.get(0);
		int maiorH = heuristica(maior);
		if (min != null) {
			if (min <= maiorH) {
				System.out.println("n" + min);
				System.out.println(maiorH);
				return maior;
			}
		}
		for (int i = 1; i < nodos.size(); i++) {
			final Nodo nodo = nodos.get(i);
			int h = heuristica(nodo);
			if (min != null) {
				if (min <= h) {
					System.out.println("n" + min);
					System.out.println(h);
					return nodo;
				}
			}
			if (h > maiorH) {
				maiorH = h;
				maior = nodo;
			}
		}
		return maior;
	}

	public Nodo find(final BiPredicate<Integer, Integer> p, final List<Nodo> nodos) {
		if (nodos.isEmpty()) {
			throw new IllegalArgumentException("O parâmetro 'nodos' não pode ser empty.");
		}

		Nodo valor = nodos.get(0);
		int valorH = heuristica(valor);
		for (int i = 1; i < nodos.size(); i++) {
			final Nodo nodo = nodos.get(i);
			int h = heuristica(nodo);
			if (p.test(h, valorH)) {
				valorH = h;
				valor = nodo;
			}
		}
		return valor;
	}

	private int heuristica(Nodo nodo) {
		return cast(nodo).h();
	}

	private Heuristica cast(Nodo nodo) {
		return (Heuristica) nodo.estado;
	}
}
