package busca;

import java.util.List;

public class EHData implements Estado, Heuristica {
	public int h;
	public List<? extends Estado> sucessores;

	public EHData(int h, List<? extends Estado> sucessores) {
		this.h = h;
		this.sucessores = sucessores;
	}

	public int h() {
		// TODO Auto-generated method stub
		return h;
	}

	public String getDescricao() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean ehMeta() {
		// TODO Auto-generated method stub
		return false;
	}

	public int custo() {
		// TODO Auto-generated method stub
		return 0;
	}

	public List<Estado> sucessores() {
		// TODO Auto-generated method stub
		return (List<Estado>) sucessores;
	}

	@Override
	public String toString() {
		return h + "";
	}
}