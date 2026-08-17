package carrinho;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import produto.Produto;
import produto.ProdutoNaoEncontradoException;

/**
 * Suite de testes unitários para a classe {@link Carrinho}.
 *
 * Observação importante: {@link Produto#equals(Object)} compara SOMENTE
 * pelo atributo "nome" (ignora preço), não sobrescreve hashCode() e lança
 * NullPointerException/ClassCastException se comparado com null ou com
 * um objeto de outro tipo. Vários testes abaixo exercitam justamente
 * essas particularidades, pois elas afetam diretamente o comportamento
 * de addItem/removeItem (que dependem de ArrayList#remove, que por sua
 * vez depende de equals).
 */
@DisplayName("Testes do Carrinho")
class CarrinhoTest {

	private Carrinho carrinho;

	@BeforeEach
	void setUp() {
		carrinho = new Carrinho();
	}

	// ---------------------------------------------------------------
	// Estado inicial
	// ---------------------------------------------------------------
	@Nested
	@DisplayName("Estado inicial")
	class EstadoInicial {

		@Test
		@DisplayName("carrinho novo deve estar vazio")
		void carrinhoNovoDeveEstarVazio() {
			assertEquals(0, carrinho.getQtdeItems());
		}

		@Test
		@DisplayName("valor total de carrinho vazio deve ser 0.0")
		void valorTotalDeCarrinhoVazioDeveSerZero() {
			assertEquals(0.0, carrinho.getValorTotal(), 0.0001);
		}
	}

	// ---------------------------------------------------------------
	// addItem
	// ---------------------------------------------------------------
	@Nested
	@DisplayName("addItem(Produto)")
	class AddItem {

		@Test
		@DisplayName("adicionar um item deve incrementar a quantidade")
		void adicionarUmItemIncrementaQuantidade() {
			carrinho.addItem(new Produto("Caneta", 2.50));
			assertEquals(1, carrinho.getQtdeItems());
		}

		@Test
		@DisplayName("adicionar múltiplos itens deve somar corretamente o valor total")
		void adicionarMultiplosItensSomaValorTotal() {
			carrinho.addItem(new Produto("Caneta", 2.50));
			carrinho.addItem(new Produto("Caderno", 15.90));
			carrinho.addItem(new Produto("Borracha", 1.20));

			assertEquals(3, carrinho.getQtdeItems());
			assertEquals(19.60, carrinho.getValorTotal(), 0.0001);
		}

		@Test
		@DisplayName("deve permitir adicionar produtos com nomes duplicados (itens distintos na lista)")
		void devePermitirNomesDuplicados() {
			carrinho.addItem(new Produto("Caneta", 2.50));
			carrinho.addItem(new Produto("Caneta", 2.50));

			assertEquals(2, carrinho.getQtdeItems());
			assertEquals(5.00, carrinho.getValorTotal(), 0.0001);
		}

		@Test
		@DisplayName("deve aceitar preço zero ou negativo sem lançar exceção (sem validação na classe)")
		void deveAceitarPrecoZeroOuNegativo() {
			carrinho.addItem(new Produto("Brinde", 0.0));
			carrinho.addItem(new Produto("Desconto especial", -10.0));

			assertEquals(2, carrinho.getQtdeItems());
			assertEquals(-10.0, carrinho.getValorTotal(), 0.0001);
		}
	}

	// ---------------------------------------------------------------
	// removeItem
	// ---------------------------------------------------------------
	@Nested
	@DisplayName("removeItem(Produto)")
	class RemoveItem {

		@Test
		@DisplayName("remover item existente (mesma instância) deve funcionar e atualizar total")
		void deveRemoverItemExistente() throws ProdutoNaoEncontradoException {
			Produto caneta = new Produto("Caneta", 2.50);
			carrinho.addItem(caneta);
			carrinho.addItem(new Produto("Caderno", 15.90));

			carrinho.removeItem(caneta);

			assertEquals(1, carrinho.getQtdeItems());
			assertEquals(15.90, carrinho.getValorTotal(), 0.0001);
		}

		@Test
		@DisplayName("remover por 'igualdade de nome' (outra instância, mesmo nome) deve funcionar, pois equals ignora preço")
		void deveRemoverPorIgualdadeDeNomeIgnorandoPreco() throws ProdutoNaoEncontradoException {
			carrinho.addItem(new Produto("Caneta", 2.50));

			// Instância diferente, mesmo nome, preço diferente: equals() considera igual.
			Produto canetaOutraInstancia = new Produto("Caneta", 999.99);
			carrinho.removeItem(canetaOutraInstancia);

			assertEquals(0, carrinho.getQtdeItems());
		}

		@Test
		@DisplayName("remover item que não está no carrinho deve lançar ProdutoNaoEncontradoException")
		void deveLancarExcecaoAoRemoverItemInexistente() {
			carrinho.addItem(new Produto("Caneta", 2.50));

			Produto inexistente = new Produto("Mochila", 120.00);

			assertThrows(ProdutoNaoEncontradoException.class,
					() -> carrinho.removeItem(inexistente));

			// Estado do carrinho não deve ser alterado pela tentativa falha
			assertEquals(1, carrinho.getQtdeItems());
		}

		@Test
		@DisplayName("remover de carrinho vazio deve lançar ProdutoNaoEncontradoException")
		void deveLancarExcecaoAoRemoverDeCarrinhoVazio() {
			assertThrows(ProdutoNaoEncontradoException.class,
					() -> carrinho.removeItem(new Produto("Qualquer", 1.0)));
		}

		@Test
		@DisplayName("com nomes duplicados, remover deve eliminar apenas UMA ocorrência (a primeira encontrada)")
		void deveRemoverApenasUmaOcorrenciaComNomesDuplicados() throws ProdutoNaoEncontradoException {
			carrinho.addItem(new Produto("Caneta", 2.50));
			carrinho.addItem(new Produto("Caneta", 2.50));
			carrinho.addItem(new Produto("Caderno", 15.90));

			carrinho.removeItem(new Produto("Caneta", 2.50));

			assertEquals(2, carrinho.getQtdeItems());
			assertEquals(18.40, carrinho.getValorTotal(), 0.0001);
		}

		@Test
		@DisplayName("tentar remover Produto null não deve lançar NullPointerException, e sim ProdutoNaoEncontradoException")
		void removerNullDeveLancarProdutoNaoEncontrado() {
			carrinho.addItem(new Produto("Caneta", 2.50));

			// ArrayList#remove(null) procura por um elemento nulo na lista (não invoca equals),
			// então, como não há nulos na lista, o item não é encontrado.
			assertThrows(ProdutoNaoEncontradoException.class,
					() -> carrinho.removeItem(null));
		}
	}

	// ---------------------------------------------------------------
	// getValorTotal
	// ---------------------------------------------------------------
	@Nested
	@DisplayName("getValorTotal()")
	class GetValorTotal {

		@Test
		@DisplayName("deve refletir corretamente valores decimais (precisão de double)")
		void deveSomarValoresDecimaisCorretamente() {
			carrinho.addItem(new Produto("Item A", 0.10));
			carrinho.addItem(new Produto("Item B", 0.20));

			assertEquals(0.30, carrinho.getValorTotal(), 0.0001);
		}

		@Test
		@DisplayName("deve recalcular corretamente após remoções sucessivas")
		void deveRecalcularAposRemocoes() throws ProdutoNaoEncontradoException {
			Produto a = new Produto("A", 10.0);
			Produto b = new Produto("B", 20.0);
			Produto c = new Produto("C", 30.0);

			carrinho.addItem(a);
			carrinho.addItem(b);
			carrinho.addItem(c);
			assertEquals(60.0, carrinho.getValorTotal(), 0.0001);

			carrinho.removeItem(b);
			assertEquals(40.0, carrinho.getValorTotal(), 0.0001);

			carrinho.removeItem(a);
			assertEquals(30.0, carrinho.getValorTotal(), 0.0001);
		}
	}

	// ---------------------------------------------------------------
	// esvazia
	// ---------------------------------------------------------------
	@Nested
	@DisplayName("esvazia()")
	class Esvazia {

		@Test
		@DisplayName("deve zerar quantidade de itens e valor total")
		void deveZerarQuantidadeEValorTotal() {
			carrinho.addItem(new Produto("A", 10.0));
			carrinho.addItem(new Produto("B", 20.0));

			carrinho.esvazia();

			assertEquals(0, carrinho.getQtdeItems());
			assertEquals(0.0, carrinho.getValorTotal(), 0.0001);
		}

		@Test
		@DisplayName("esvaziar um carrinho já vazio não deve lançar exceção")
		void esvaziarCarrinhoVazioNaoLancaExcecao() {
			assertDoesNotThrow(() -> carrinho.esvazia());
			assertEquals(0, carrinho.getQtdeItems());
		}

		@Test
		@DisplayName("após esvaziar, deve ser possível adicionar novos itens normalmente")
		void devePermitirAdicionarAposEsvaziar() {
			carrinho.addItem(new Produto("A", 10.0));
			carrinho.esvazia();
			carrinho.addItem(new Produto("B", 25.0));

			assertEquals(1, carrinho.getQtdeItems());
			assertEquals(25.0, carrinho.getValorTotal(), 0.0001);
		}
	}

	// ---------------------------------------------------------------
	// Fluxo integrado
	// ---------------------------------------------------------------
	@Nested
	@DisplayName("Fluxos integrados")
	class FluxosIntegrados {

		@Test
		@DisplayName("ciclo completo: adicionar, remover, adicionar novamente e esvaziar")
		void cicloCompleto() throws ProdutoNaoEncontradoException {
			Produto p1 = new Produto("Livro", 45.0);
			Produto p2 = new Produto("Caneta", 3.0);

			carrinho.addItem(p1);
			carrinho.addItem(p2);
			assertEquals(2, carrinho.getQtdeItems());
			assertEquals(48.0, carrinho.getValorTotal(), 0.0001);

			carrinho.removeItem(p1);
			assertEquals(1, carrinho.getQtdeItems());
			assertEquals(3.0, carrinho.getValorTotal(), 0.0001);

			carrinho.addItem(new Produto("Mochila", 200.0));
			assertEquals(2, carrinho.getQtdeItems());
			assertEquals(203.0, carrinho.getValorTotal(), 0.0001);

			carrinho.esvazia();
			assertEquals(0, carrinho.getQtdeItems());
			assertEquals(0.0, carrinho.getValorTotal(), 0.0001);
		}
	}
}
