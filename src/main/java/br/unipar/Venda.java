package br.unipar;
import java.sql.*;
import java.util.Scanner;

public class Venda {

    private static String url = "jdbc:postgresql://localhost:5432/Exemplo1";
    private static String user = "postgres";
    private static String password = "admin123";

    public static void setDatabaseConfiguration(String dbUrl, String dbUser, String dbPassword) {
        url = dbUrl;
        user = dbUser;
        password = dbPassword;
    }

    public static void main(String[] args) {

        criarTabelaVenda();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("---------=====MENU=====---------");
            System.out.println("Escolha uma opção:");
            System.out.println("1 - Inserir venda");
            System.out.println("2 - Alterar informações da venda");
            System.out.println("3 - Listar todas as vendas");
            System.out.println("4 - Deletar venda");
            System.out.println("5 - Deletar todas as vendas");
            System.out.println("6 - Sair");
            System.out.printf("Opção: ");

            int op = scanner.nextInt();
            scanner.nextLine();

            switch (op) {
                case 1:

                    System.out.print("Digite o ID do cliente: ");
                    int cliente = scanner.nextInt();

                    System.out.print("Digite o ID do produto: ");
                    int produto = scanner.nextInt();
                    scanner.nextLine();

                    inserirVenda(cliente, produto);

                    break;
                case 2:

                    System.out.print("Digite o ID da venda a ser alterada: ");
                    int idVenda = scanner.nextInt();

                    System.out.print("Digite o novo ID do cliente: ");
                    int novoCliente = scanner.nextInt();

                    System.out.print("Digite o novo ID do produto: ");
                    int novoProduto = scanner.nextInt();
                    scanner.nextLine();

                    alterarInfoVenda(novoCliente, novoProduto, idVenda);

                    break;
                case 3:

                    listarTodasVendas();

                    break;
                case 4:

                    System.out.print("Digite o ID da venda a ser deletada: ");
                    int idVendaDeletar = scanner.nextInt();
                    scanner.nextLine();

                    deletarVenda(idVendaDeletar);

                    break;
                case 5:

                    deletarTodasVendas();

                    break;
                case 6:
                    System.out.println("Saindo...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Opção inválida!");
            }
        }

    }

    public static Connection connection() throws SQLException {

        return DriverManager.getConnection(url, user, password);
    }

    public static void criarTabelaVenda() {
        try (Connection conn = connection()) {
            Statement statement = conn.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS Venda ( "
                    + " id_venda SERIAL PRIMARY KEY, "
                    + " cliente INT NOT NULL, "
                    + " produto INT NOT NULL)";
            statement.executeUpdate(sql);
            System.out.println("TABELA VENDA FOI CRIADA!");
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public static void inserirVenda(Integer cliente, Integer produto) {
        try (Connection conn = connection()) {
            PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO Venda (cliente, produto) VALUES (?, ?)");
            preparedStatement.setInt(1, cliente);
            preparedStatement.setInt(2, produto);
            preparedStatement.execute();
            System.out.println("Venda adicionada com sucesso!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void alterarInfoVenda(Integer newCliente, Integer newProduto, Integer idvenda) {
        try {
            Connection conn = connection();

            PreparedStatement preparedStatement = conn.prepareStatement(
                    "UPDATE Venda SET cliente = ?, produto = ? WHERE id_venda = ?");

            preparedStatement.setInt(1, newCliente);
            preparedStatement.setInt(2, newProduto);
            preparedStatement.setInt(3, idvenda);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("A venda ID: " + idvenda + " foi  atualizado com sucesso para: " + idvenda);
            } else {
                System.out.println("Nenhum produto encontrado com o ID: " + idvenda + " fornecido.");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deletarVenda(Integer idVenda) {
        try (Connection conn = connection()) {
            PreparedStatement preparedStatement = conn.prepareStatement("DELETE FROM Venda WHERE id_venda = ?");
            preparedStatement.setInt(1, idVenda);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("A venda com ID " + idVenda + " foi deletada com sucesso!");
            } else {
                System.out.println("Nenhuma venda deletada com o ID " + idVenda + "não encontrada!" );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deletarTodasVendas() {
        try {

            Connection conn = connection();

            PreparedStatement preparedStatement = conn.prepareStatement(
                    "DELETE FROM Venda");

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Todos os venda foram deletados com sucesso!");
            } else {
                System.out.println("Nenhum venda encontrado.");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void listarTodasVendas() {
        try (Connection conn = connection()) {
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM Venda");

            System.out.println("--------=======LISTA DE PRODUTOS=======--------");
            while(result.next()){

                System.out.printf("Id_Venda: ");
                System.out.printf(result.getString("id_venda"));
                System.out.printf(" Cliente: ");
                System.out.printf(result.getString("cliente"));
                System.out.printf(" Produto: ");
                System.out.println(result.getString("produto"));

            }
            System.out.println("--------------====================--------------");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}