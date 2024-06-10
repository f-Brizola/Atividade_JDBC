package br.unipar;
import java.sql.*;
import java.util.Scanner;

public class Cliente {

    private static String url = "jdbc:postgresql://localhost:5432/Exemplo1";
    private static String user = "postgres";
    private static String password = "admin123";

    public static void setDatabaseConfiguration(String dbUrl, String dbUser, String dbPassword) {
        url = dbUrl;
        user = dbUser;
        password = dbPassword;
    }

    public static void main(String[] args) {

        criarTabelaCliente();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("---------=====MENU=====---------");
            System.out.println("Escolha uma opção:");
            System.out.println("1 - Inserir cliente");
            System.out.println("2 - Alterar informações do cliente");
            System.out.println("3 - Listar todos os clientes");
            System.out.println("4 - Deletar cliente");
            System.out.println("5 - Deletar todos os clientes");
            System.out.println("6 - Sair");
            System.out.printf("Opção: ");

            int op = scanner.nextInt();
            scanner.nextLine();

            switch (op) {
                case 1:

                    System.out.print("Digite o nome do cliente: ");
                    String nome = scanner.nextLine();

                    System.out.print("Digite o CPF do cliente: ");
                    String cpf = scanner.nextLine();

                    inserirCliente(nome, cpf);

                    break;
                case 2:

                    System.out.print("Digite o nome do cliente a ser alterado: ");
                    String Nome = scanner.nextLine();

                    System.out.print("Digite o novo nome: ");
                    String newNome = scanner.nextLine();

                    System.out.print("Digite o novo CPF: ");
                    String newCpf = scanner.nextLine();

                    alterarInfoCliente(newNome, newCpf, Nome);

                    break;
                case 3:

                    listarTodosCliente();

                    break;
                case 4:

                    System.out.print("Digite o nome do cliente a ser deletado: ");
                    String delNome = scanner.nextLine();

                    deletarCliente(delNome);

                    break;
                case 5:

                    deletarTodosClientes();

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

    public static void criarTabelaCliente(){
        try {
            Connection conn = connection();

            Statement statement = conn.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS Cliente ( "
                    + " id_cliente SERIAL PRIMARY KEY, "
                    + "nome VARCHAR(100) NOT NULL, "
                    + "CPF VARCHAR(11) NOT NULL )";

            statement.executeUpdate(sql);

            System.out.println("TABELA CLIENTE FOI CRIADA!");

        } catch (SQLException exception){
            exception.printStackTrace();
        }
    }

    public static void inserirCliente(String nome, String cpf){
        try {

            Connection conn = connection();

            PreparedStatement preparedStatement = conn.prepareStatement
                    ("Insert into Cliente(nome, cpf) VALUES (?, ?)");

            preparedStatement.setString(1, nome);
            preparedStatement.setString(2, cpf);

            preparedStatement.execute();

            System.out.println("Cliente: " + nome + " adicionado com sucesso!");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void alterarInfoCliente(String newNome, String newCpf, String nome) {
        try {

            Connection conn = connection();

            PreparedStatement preparedStatement = conn.prepareStatement(
                    "UPDATE Cliente SET nome = ?, cpf = ? WHERE nome = ?");

            preparedStatement.setString(1, newNome);
            preparedStatement.setString(2, newCpf);
            preparedStatement.setString(3, nome);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Nome do cliente: " + nome + "  atualizado com sucesso para: " + newNome);
            } else {
                System.out.println("Nenhum cliente encontrado com o nome: " + nome + " fornecido.");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deletarCliente(String nome) {
        try {

            Connection conn = connection();

            PreparedStatement preparedStatement = conn.prepareStatement(
                    "DELETE FROM Cliente WHERE nome = ?");

            preparedStatement.setString(1, nome);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("O Cliente com nome: " + nome + " foi  deletado com sucesso!");
            } else {
                System.out.println("Nenhum cliente encontrado com o nome: " + nome + " fornecido.");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deletarTodosClientes() {
        try {

            Connection conn = connection();

            PreparedStatement preparedStatement = conn.prepareStatement(
                    "DELETE FROM Cliente");

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Todos os clientes foram deletados com sucesso!");
            } else {
                System.out.println("Nenhum cliente encontrado.");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void listarTodosCliente(){
        try {

            Connection conn = connection();

            Statement statement = conn.createStatement();

            ResultSet result = statement.executeQuery("SELECT * FROM Cliente");

            System.out.println("--------=======LISTA DE CLIENTES=======--------");
            while(result.next()){

                System.out.printf("Id_Cliente: ");
                System.out.printf(result.getString("id_cliente"));
                System.out.printf(" Nome: ");
                System.out.printf(result.getString("nome"));
                System.out.printf(" CPF: ");
                System.out.println(result.getString("cpf"));

            }
            System.out.println("--------------====================--------------");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}