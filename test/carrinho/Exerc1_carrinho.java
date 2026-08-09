package calculadora;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


public class CarrinhoTest {

    @Test
    public void testaAddItem() {
        Carrinho carrinho = new Carrinho();
        Produto produto = new Produto("Placa de video", 600.0);

        carrinho.addItem(produto);

        assertEquals(1, carrinho.getQtdeItems());
    }

    @Test
    public void testaGetQtdeItems() {
        Carrinho carrinho = new Carrinho();

        carrinho.addItem(new Produto("Placa mae", 350.0));
        carrinho.addItem(new Produto("Processador", 600.0));

        assertEquals(2, carrinho.getQtdeItems());
    }

    @Test
    public void testaGetValorTotal() {
        Carrinho carrinho = new Carrinho();

        carrinho.addItem(new Produto("Memoria RAM", 400.0));
        carrinho.addItem(new Produto("Fonte", 250.0));

        double resultado = carrinho.getValorTotal();

        assertEquals(650.0, resultado);
    }

    @Test
    public void testaRemoveItem() throws ProdutoNaoEncontradoException {
        Carrinho carrinho = new Carrinho();
        Produto produto = new Produto("Gabinete", 290.0);

        carrinho.addItem(produto);
        carrinho.removeItem(produto);

        assertEquals(0, carrinho.getQtdeItems());
    }

    @Test
    public void testaEsvazia() {
        Carrinho carrinho = new Carrinho();

        carrinho.addItem(new Produto("Monitor", 800.0));
        carrinho.addItem(new Produto("Teclado", 150.0));

        carrinho.esvazia();

        assertEquals(0, carrinho.getQtdeItems());
    }
}