import br.unipar.Main;
import br.unipar.Cliente;
import br.unipar.Produto;
import br.unipar.Venda;
import org.junit.jupiter.api.*;
import java.sql.*;
import static org.junit.jupiter.api.Assertions.*;

public class Teste {

    @BeforeAll
    public static void setUp() {

        Main.setDatabaseConfiguration("jdbc:postgresql://localhost:5432/Exemplo1", "postgres", "admin123");
        Main.criarTabelaUsuario();

        Cliente.setDatabaseConfiguration("jdbc:postgresql://localhost:5432/Exemplo1", "postgres", "admin123");
        Cliente.criarTabelaCliente();

        Produto.setDatabaseConfiguration("jdbc:postgresql://localhost:5432/Exemplo1", "postgres", "admin123");
        Produto.criarTabelaProduto();

        Venda.setDatabaseConfiguration("jdbc:postgresql://localhost:5432/Exemplo1", "postgres", "admin123");
        Venda.criarTabelaVenda();

    }

    @AfterEach
    public void tearDownVenda() {

        try (Connection conn = Venda.connection()) {
            Statement statement = conn.createStatement();
            statement.executeUpdate("DELETE FROM Venda");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    public void tearDownUsuario() {

        try (Connection conn = Main.connection()) {
            Statement statement = conn.createStatement();
            statement.executeUpdate("DELETE FROM Usuario");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    public void tearDownCliente() {

        try (Connection conn = Cliente.connection()) {
            Statement statement = conn.createStatement();
            statement.executeUpdate("DELETE FROM Cliente");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    public void tearDownProduto() {

        try (Connection conn = Produto.connection()) {
            Statement statement = conn.createStatement();
            statement.executeUpdate("DELETE FROM Produto");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testInserirUsuario() {

        Main.inserirUsuario("taffe", "12345", "Fabio", "1890-01-01");

        try (Connection conn = Main.connection()) {
            Statement statement = conn.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT * FROM Usuario WHERE username = 'taffe'");

            assertTrue(resultSet.next());
            assertEquals("Fabio", resultSet.getString("nome"));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testAlterarUsuario() {

        Main.inserirUsuario("taffe", "12345", "Fabio", "1890-01-01");
        Main.alterarUsuario("54321", "Cleito", "2000-01-20", "taffe");

        try (Connection conn = Main.connection()) {
            Statement statement = conn.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT * FROM Usuario WHERE username = 'taffe'");

            assertTrue(resultSet.next());
            assertEquals("Cleito", resultSet.getString("nome"));
            assertEquals("54321", resultSet.getString("password"));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testDeletarUsuario() {

        Main.inserirUsuario("taffe", "12345", "Fabio", "1890-01-01");
        Main.deletarUsuario("taffe");

        try (Connection conn = Main.connection()) {
            Statement statement = conn.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT * FROM Usuario WHERE username = 'taffe'");

            assertFalse(resultSet.next());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testListarTodosUsuario() {

        Main.inserirUsuario("taffe", "12345", "Fabio", "1890-01-01");
        Main.inserirUsuario("doe", "67890", "John", "1990-01-01");

        try {
            Main.listarTodosUsuario();
        } catch (Exception e) {
            fail("A listagem de usuários falhou.");
        }
    }

    @Test
    public void testInserirCliente() {

        Cliente.inserirCliente("taffe", "12345678910");

        try (Connection conn = Cliente.connection()) {
            Statement statement = conn.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT * FROM Cliente WHERE nome = 'taffe'");

            assertTrue(resultSet.next());
            assertEquals("12345678910", resultSet.getString("cpf"));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testAlterarInfoCliente() {

        Cliente.inserirCliente("taffe", "12345678910");
        Cliente.alterarInfoCliente("cleito", "10987654321", "taffe");

        try (Connection conn = Cliente.connection()) {
            Statement statement = conn.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT * FROM Cliente WHERE nome = 'cleito'");

            assertTrue(resultSet.next());
            assertEquals("10987654321", resultSet.getString("cpf"));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testDeletarCliente() {

        Cliente.inserirCliente("taffe", "12345678910");
        Cliente.deletarCliente("taffe");

        try (Connection conn = Cliente.connection()) {
            Statement statement = conn.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT * FROM Cliente WHERE nome = 'taffe'");

            assertFalse(resultSet.next());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testListarTodosCliente() {

        Cliente.inserirCliente("taffe", "12345678910");
        Cliente.inserirCliente("doe", "76153692583");

        try {
            Cliente.listarTodosCliente();
        } catch (Exception e) {
            fail("A listagem de clientes falhou.");
        }
    }

    @Test
    public void testInserirProduto() {

        Produto.inserirProduto("Prego", "17x27", 25);

        try (Connection conn = Produto.connection()) {
            Statement statement = conn.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT * FROM Produto WHERE nome = 'Prego'");

            assertTrue(resultSet.next());
            assertEquals("17x27", resultSet.getString("descricao"));
            assertEquals(25, resultSet.getInt("valor"));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testAlterarInfoProduto() {

        Produto.inserirProduto("Prego", "17x27", 25);
        Produto.alterarInfoProduto("Parafuso", "12x12", 40, "Prego");

        try (Connection conn = Produto.connection()) {
            Statement statement = conn.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT * FROM Produto WHERE nome = 'Parafuso'");

            assertTrue(resultSet.next());
            assertEquals("12x12", resultSet.getString("descricao"));
            assertEquals(40, resultSet.getInt("valor"));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testDeletarProduto() {

        Produto.inserirProduto("Prego", "17x27", 25);
        Produto.deletarProduto("Prego");

        try (Connection conn = Produto.connection()) {
            Statement statement = conn.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT * FROM Produto WHERE nome = 'Prego'");

            assertFalse(resultSet.next());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testListarTodosProdutos() {

        Produto.inserirProduto("Prego", "17x27", 25);
        Produto.inserirProduto("Parafuso", "12x12", 40);

        try {
            Produto.listarTodosProdutos();
        } catch (Exception e) {
            fail("A listagem de produtos falhou.");

        }
    }

    @Test
    public void testInserirVenda() {

        Venda.inserirVenda(1, 1);

        try (Connection conn = Venda.connection()) {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Venda WHERE Cliente = 1 AND Produto = 1");
            assertTrue(resultSet.next());
            assertEquals("1", resultSet.getString("Cliente"));
            assertEquals("1", resultSet.getString("Produto"));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testAlterarVenda() {

        Venda.inserirVenda(1, 1);
        Venda.alterarInfoVenda(3,4,1);

        try (Connection conn = Venda.connection()) {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Venda WHERE id_venda = 1");
            assertFalse(resultSet.next());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testDeletarVenda() {
        Venda.inserirVenda(1, 1);
        Venda.deletarVenda(1);

        try (Connection conn = Venda.connection()) {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Venda WHERE id_venda = 1");
            assertFalse(resultSet.next());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testListarTodasVendas() {
        Venda.inserirVenda(1, 1);
        Venda.inserirVenda(2, 2);

        try {
            Venda.listarTodasVendas();
        } catch (Exception e) {
            fail("A listagem de vendas falhou.");

        }
    }
}