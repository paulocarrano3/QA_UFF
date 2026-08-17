package calculadora;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Suite de testes unitários para a classe {@link Calculadora}.
 *
 * Cobre casos normais, casos de borda (limites de int, zero, negativos)
 * e comportamento de overflow/exceções.
 */
@DisplayName("Testes da Calculadora")
class CalculadoraTest {

	private Calculadora calculadora;

	@BeforeEach
	void setUp() {
		calculadora = new Calculadora();
	}

	// ---------------------------------------------------------------
	// soma
	// ---------------------------------------------------------------
	@Nested
	@DisplayName("soma(a, b)")
	class Soma {

		@ParameterizedTest(name = "{0} + {1} = {2}")
		@CsvSource({
			"2, 3, 5",
			"0, 0, 0",
			"-2, -3, -5",
			"-5, 5, 0",
			"100, -50, 50"
		})
		void deveSomarCorretamente(int a, int b, int esperado) {
			assertEquals(esperado, calculadora.soma(a, b));
		}

		@Test
		@DisplayName("deve ser comutativa")
		void deveSerComutativa() {
			assertEquals(calculadora.soma(7, 4), calculadora.soma(4, 7));
		}

		@Test
		@DisplayName("deve estourar (overflow) silenciosamente com valores extremos")
		void deveEstourarComValoresExtremos() {
			// Integer.MAX_VALUE + 1 gera overflow (comportamento nativo do int em Java)
			assertEquals(Integer.MIN_VALUE, calculadora.soma(Integer.MAX_VALUE, 1));
		}
	}

	// ---------------------------------------------------------------
	// subtracao
	// ---------------------------------------------------------------
	@Nested
	@DisplayName("subtracao(a, b)")
	class Subtracao {

		@ParameterizedTest(name = "{0} - {1} = {2}")
		@CsvSource({
			"5, 3, 2",
			"0, 0, 0",
			"-5, -3, -2",
			"3, 5, -2",
			"-5, 5, -10"
		})
		void deveSubtrairCorretamente(int a, int b, int esperado) {
			assertEquals(esperado, calculadora.subtracao(a, b));
		}

		@Test
		@DisplayName("subtrair de si mesmo deve resultar em zero")
		void deveResultarEmZeroQuandoIguais() {
			assertEquals(0, calculadora.subtracao(42, 42));
		}

		@Test
		@DisplayName("deve estourar (overflow) com valores extremos")
		void deveEstourarComValoresExtremos() {
			assertEquals(Integer.MAX_VALUE, calculadora.subtracao(Integer.MIN_VALUE, 1));
		}
	}

	// ---------------------------------------------------------------
	// multiplicacao
	// ---------------------------------------------------------------
	@Nested
	@DisplayName("multiplicacao(a, b)")
	class Multiplicacao {

		@ParameterizedTest(name = "{0} * {1} = {2}")
		@CsvSource({
			"4, 3, 12",
			"0, 10, 0",
			"-4, 3, -12",
			"-4, -3, 12",
			"1, 999, 999"
		})
		void deveMultiplicarCorretamente(int a, int b, int esperado) {
			assertEquals(esperado, calculadora.multiplicacao(a, b));
		}

		@Test
		@DisplayName("multiplicar por zero deve retornar zero")
		void multiplicarPorZero() {
			assertEquals(0, calculadora.multiplicacao(Integer.MAX_VALUE, 0));
		}

		@Test
		@DisplayName("deve estourar (overflow) com valores extremos")
		void deveEstourarComValoresExtremos() {
			assertEquals(0, calculadora.multiplicacao(Integer.MIN_VALUE, 2));
		}
	}

	// ---------------------------------------------------------------
	// divisao
	// ---------------------------------------------------------------
	@Nested
	@DisplayName("divisao(a, b)")
	class Divisao {

		@ParameterizedTest(name = "{0} / {1} = {2}")
		@CsvSource({
			"10, 2, 5",
			"9, 3, 3",
			"-10, 2, -5",
			"-10, -2, 5",
			"0, 5, 0"
		})
		void deveDividirCorretamente(int a, int b, int esperado) {
			assertEquals(esperado, calculadora.divisao(a, b));
		}

		@Test
		@DisplayName("divisão inteira deve truncar o resultado (não arredondar)")
		void deveTruncarDivisaoNaoExata() {
			assertEquals(3, calculadora.divisao(10, 3)); // 3.33 -> 3
			assertEquals(-3, calculadora.divisao(-10, 3)); // -3.33 -> -3 (trunca em direção a zero)
		}

		@Test
		@DisplayName("divisão por zero deve lançar ArithmeticException")
		void deveLancarExcecaoAoDividirPorZero() {
			ArithmeticException ex = assertThrows(ArithmeticException.class,
					() -> calculadora.divisao(10, 0));
			assertEquals("/ by zero", ex.getMessage());
		}

		@Test
		@DisplayName("Integer.MIN_VALUE / -1 deve estourar (overflow), não lançar exceção")
		void deveEstourarComOverflowClassico() {
			assertEquals(Integer.MIN_VALUE, calculadora.divisao(Integer.MIN_VALUE, -1));
		}
	}

	// ---------------------------------------------------------------
	// somatoria
	// ---------------------------------------------------------------
	@Nested
	@DisplayName("somatoria(n)")
	class Somatoria {

		@ParameterizedTest(name = "somatoria({0}) = {1}")
		@CsvSource({
			"0, 0",
			"1, 1",
			"5, 15",
			"10, 55",
			"100, 5050"
		})
		void deveCalcularSomatorioCorretamente(int n, int esperado) {
			assertEquals(esperado, calculadora.somatoria(n));
		}

		@Test
		@DisplayName("n negativo: laço 'while (n >= 0)' nunca executa, retorna 0")
		void deveRetornarZeroParaNNegativo() {
			assertEquals(0, calculadora.somatoria(-1));
			assertEquals(0, calculadora.somatoria(-100));
		}

		@Test
		@DisplayName("não deve alterar o parâmetro do chamador (int é passado por valor)")
		void naoDeveTerEfeitoColateralNoChamador() {
			int n = 5;
			calculadora.somatoria(n);
			assertEquals(5, n);
		}
	}

	// ---------------------------------------------------------------
	// ehPositivo
	// ---------------------------------------------------------------
	@Nested
	@DisplayName("ehPositivo(n)")
	class EhPositivo {

		@ParameterizedTest
		@ValueSource(ints = {1, 5, 100, Integer.MAX_VALUE, 0})
		void deveRetornarTrueParaZeroOuPositivo(int n) {
			assertTrue(calculadora.ehPositivo(n));
		}

		@ParameterizedTest
		@ValueSource(ints = {-1, -5, -100, Integer.MIN_VALUE})
		void deveRetornarFalseParaNegativo(int n) {
			assertFalse(calculadora.ehPositivo(n));
		}
	}

	// ---------------------------------------------------------------
	// compara
	// ---------------------------------------------------------------
	@Nested
	@DisplayName("compara(a, b)")
	class Compara {

		@Test
		@DisplayName("deve retornar 0 quando a == b")
		void deveRetornarZeroQuandoIguais() {
			assertEquals(0, calculadora.compara(5, 5));
			assertEquals(0, calculadora.compara(0, 0));
			assertEquals(0, calculadora.compara(-3, -3));
		}

		@Test
		@DisplayName("deve retornar 1 quando a > b")
		void deveRetornarUmQuandoMaior() {
			assertEquals(1, calculadora.compara(10, 5));
			assertEquals(1, calculadora.compara(0, -5));
		}

		@Test
		@DisplayName("deve retornar -1 quando a < b")
		void deveRetornarMenosUmQuandoMenor() {
			assertEquals(-1, calculadora.compara(5, 10));
			assertEquals(-1, calculadora.compara(-5, 0));
		}

		@Test
		@DisplayName("deve funcionar corretamente nos extremos de int")
		void deveFuncionarComValoresExtremos() {
			assertEquals(1, calculadora.compara(Integer.MAX_VALUE, Integer.MIN_VALUE));
			assertEquals(-1, calculadora.compara(Integer.MIN_VALUE, Integer.MAX_VALUE));
		}
	}
}
