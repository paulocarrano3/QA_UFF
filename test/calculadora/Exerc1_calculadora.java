package calculadora;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


public class CalculadoraTest {

    @Test
    public void testaSoma() {
        Calculadora calc = new Calculadora();

        int resultado = calc.soma(30, 20);

        assertEquals(50, resultado);
    }

    @Test
    public void testaSubtracao() {
        Calculadora calc = new Calculadora();

        int resultado = calc.subtracao(19, 9);

        assertEquals(10, resultado);
    }

    @Test
    public void testaMultiplicacao() {
        Calculadora calc = new Calculadora();

        int resultado = calc.multiplicacao(7, 7);

        assertEquals(49, resultado);
    }

    @Test
    public void testaDivisao() {
        Calculadora calc = new Calculadora();

        int resultado = calc.divisao(60, 15);

        assertEquals(4, resultado);
    }

    @Test
    public void testaSomatoria() {
        Calculadora calc = new Calculadora();

        int resultado = calc.somatoria(5);

        assertEquals(15, resultado);
    }

    @Test
    public void testaEhPositivo() {
        Calculadora calc = new Calculadora();

        boolean resultado = calc.ehPositivo(10);

        assertTrue(resultado);
    }

    @Test
    public void testaCompara() {
        Calculadora calc = new Calculadora();

        int resultado = calc.compara(3, 8);

        assertEquals(-1, resultado);
    }
}