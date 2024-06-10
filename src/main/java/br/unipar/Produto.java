package br.unipar;
import java.sql.*;
import java.util.Scanner;

public class Produto {

    private static String url = "jdbc:postgresql://localhost:5432/Exemplo1";
    private static String user = "postgres";
    private static String password = "admin123";

    public static void setDatabaseConfiguration(String dbUrl, String dbUser, String dbPassword) {
        url = dbUrl;
        user = dbUser;
        password = dbPassword;
    }

    public static void main(String[] args) {

        criarTabelaProduto();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("---------=====MENU=====---------");
            System.out.println("Escolha uma opção:");
            System.out.println("1 - Inserir produto");
            System.out.println("2 - Alterar informações do produto");
            System.out.println("3 - Listar todos os produtos");
            System.out.println("4 - Deletar produto");
            System.out.println("5 - Deletar todos os produtos");
            System.out.println("6 - Sair");
            System.out.printf("Opção: ");

            int op = scanner.nextInt();
            scanner.nextLine();
            switch (op) {
                case 1:

                    System.out.print("Digite o nome do produto: ");
                    String nome = scanner.nextLine();

                    System.out.print("Digite a descrição do produto: ");
                    String descricao = scanner.nextLine();

                    System.out.print("Digite o valor do produto: ");
                    int valor = scanner.nextInt();
                    scanner.nextLine();

                    inserirProduto(nome, descricao, valor);

                    break;
                case 2:

                    System.out.print("Digite o nome do produto a ser alterado: ");
                    String nomeProduto = scanner.nextLine();

                    System.out.print("Digite o novo nome: ");
                    String novoNome = scanner.nextLine();

                    System.out.print("Digite a nova descrição: ");
                    String novaDescricao = scanner.nextLine();

                    System.out.print("Digite o novo valor: ");
                    int novoValor = scanner.nextInt();
                    scanner.nextLine();

                    alterarInfoProduto(novoNome, novaDescricao, novoValor, nomeProduto);

                    break;
                case 3:

                    listarTodosProdutos();

                    break;
                case 4:

                    System.out.print("Digite o nome do produto a ser deletado: ");
                    String nomeProdutoDeletar = scanner.nextLine();

                    deletarProduto(nomeProdutoDeletar);

                    break;
                case 5:

                    deletarTodosProdutos();

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

    public static void criarTabelaProduto(){
        try {
            Connection conn = connection();

            Statement statement = conn.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS Produto ( "
                    + " id_produto SERIAL PRIMARY KEY, "
                    + "nome VARCHAR(100) NOT NULL, "
                    + "descricao VARCHAR(200) NOT NULL,"
                    + "valor INTEGER NOT NULL )";

            statement.executeUpdate(sql);

            System.out.println("TABELA produto FOI CRIADA!");

        } catch (SQLException exception){
            exception.printStackTrace();
        }
    }

    public static void inserirProduto(String nome, String descricao, Integer valor){
        try {

            Connection conn = connection();

            PreparedStatement preparedStatement = conn.prepareStatement
                    ("Insert into Produto(nome, descricao, valor) VALUES (?, ?, ?)");

            preparedStatement.setString(1, nome);
            preparedStatement.setString(2, descricao);
            preparedStatement.setInt(3, valor);

            preparedStatement.execute();

            System.out.println("Produto: " + nome + " adicionado com sucesso!");


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void alterarInfoProduto(String newNome, String newDescricao, Integer newValor, String nome) {
        try {

            Connection conn = connection();

            PreparedStatement preparedStatement = conn.prepareStatement(
                    "UPDATE Produto SET nome = ?, descricao = ?, valor = ? WHERE nome = ?");

            preparedStatement.setString(1, newNome);
            preparedStatement.setString(2, newDescricao);
            preparedStatement.setInt(3, newValor);
            preparedStatement.setString(4, nome);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Nome do produto: " + nome + "  atualizado com sucesso para: " + newNome);
            } else {
                System.out.println("Nenhum produto encontrado com o nome: " + nome + " fornecido.");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deletarProduto(String nome) {
        try {

            Connection conn = connection();

            PreparedStatement preparedStatement = conn.prepareStatement(
                    "DELETE FROM Produto WHERE nome = ?");

            preparedStatement.setString(1, nome);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("O Produto com nome: " + nome + " foi  deletado com sucesso!");
            } else {
                System.out.println("Nenhum produto encontrado com o nome: " + nome + " fornecido.");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deletarTodosProdutos() {
        try {

            Connection conn = connection();

            PreparedStatement preparedStatement = conn.prepareStatement(
                    "DELETE FROM Produto");

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Todos os produto foram deletados com sucesso!");
            } else {
                System.out.println("Nenhum produto encontrado.");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void listarTodosProdutos(){
        try {

            Connection conn = connection();

            Statement statement = conn.createStatement();

            ResultSet result = statement.executeQuery("SELECT * FROM Produto");

            System.out.println("--------=======LISTA DE PRODUTOS=======--------");
            while(result.next()){

                System.out.printf("Id_Produto: ");
                System.out.printf(result.getString("id_produto"));
                System.out.printf(" Nome: ");
                System.out.printf(result.getString("nome"));
                System.out.printf(" Descricao: ");
                System.out.printf(result.getString("descricao"));
                System.out.printf(" Valor do Produto: ");
                System.out.println(result.getString("valor"));

            }
            System.out.println("--------------====================--------------");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}