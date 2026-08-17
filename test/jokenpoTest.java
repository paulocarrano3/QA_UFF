import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class jokenpoTest {

    private final Main main = new Main();

    @Test
    void testeEmpate() {
        assertEquals(0, main.jogar(1, 1));
        assertEquals(0, main.jogar(2, 2));
        assertEquals(0, main.jogar(3, 3));
    }

    @Test
    void vitoriaJogador1() {
        assertEquals(1, main.jogar(1, 2));
    }

    @Test
    void vitoriaJogador2() {
        assertEquals(2, main.jogar(2, 1));
    }

    @Test
    void retornaMenosUm() {
        assertEquals(-1, main.jogar(0, 1));
        assertEquals(-1, main.jogar(1, 4));
        assertEquals(-1, main.jogar(0, 0));
    }
}