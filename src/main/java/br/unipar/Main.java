package br.unipar;
import java.sql.*;
import java.util.Scanner;

public class Main {

    private static String url = "jdbc:postgresql://localhost:5432/Exemplo1";
    private static String user = "postgres";
    private static String password = "admin123";

    public static void setDatabaseConfiguration(String dbUrl, String dbUser, String dbPassword) {
        url = dbUrl;
        user = dbUser;
        password = dbPassword;
    }

    public static void main(String[] args) {

        criarTabelaUsuario();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("---------=====MENU=====---------");
            System.out.println("Escolha uma opção:");
            System.out.println("1 - Inserir usuário");
            System.out.println("2 - Alterar usuário");
            System.out.println("3 - Listar todos os usuários");
            System.out.println("4 - Deletar usuário");
            System.out.println("5 - Deletar todos os usuários");
            System.out.println("6 - Sair");
            System.out.printf("Opção: ");

            int op = scanner.nextInt();
            scanner.nextLine();

            switch (op) {
                case 1:

                    System.out.print("Digite o username: ");
                    String username = scanner.nextLine();

                    System.out.print("Digite a senha: ");
                    String senha = scanner.nextLine();

                    System.out.print("Digite o nome: ");
                    String nome = scanner.nextLine();

                    System.out.print("Digite a data de nascimento (YYYY-MM-DD): ");
                    String dataNascimento = scanner.nextLine();

                    inserirUsuario(username, senha, nome, dataNascimento);

                    break;
                case 2:

                    System.out.print("Digite o username do usuário a ter suas informações alteradas: ");
                    String Username = scanner.nextLine();

                    System.out.print("Digite a nova senha: ");
                    String newPassword = scanner.nextLine();

                    System.out.print("Digite o novo nome: ");
                    String newNome = scanner.nextLine();

                    System.out.print("Digite a nova data de nascimento (YYYY-MM-DD): ");
                    String newDataNascimento = scanner.nextLine();

                    alterarUsuario(newPassword, newNome, newDataNascimento, Username);

                    break;
                case 3:

                    listarTodosUsuario();

                    break;
                case 4:
                    System.out.print("Digite o username do usuário a ser deletado: ");
                    String delUsername = scanner.nextLine();

                    deletarUsuario(delUsername);

                    break;
                case 5:

                    deletarTodosUsuarios();

                    break;
                case 6:
                    System.out.println("Saindo...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Opção inválida! Tente novamente!");
            }
        }

    }

    public static Connection connection() throws SQLException {

        return DriverManager.getConnection(url, user, password);
    }

    public static void criarTabelaUsuario(){
        try {
            Connection conn = connection();

            Statement statement = conn.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS Usuario ( "
                    + " codigo SERIAL PRIMARY KEY, "
                    + "username VARCHAR(50) UNIQUE NOT NULL, "
                    + "password VARCHAR(50) NOT NULL, "
                    + "nome VARCHAR(50) NOT NULL, "
                    + "nascimento DATE )";

            statement.executeUpdate(sql);

            System.out.println("TABELA USUARIO FOI CRIADA!");

        } catch (SQLException exception){
            exception.printStackTrace();
        }
    }

    public static void inserirUsuario(String username, String password, String nome, String dataNascimento){
        try {

            Connection conn = connection();

            PreparedStatement preparedStatement = conn.prepareStatement
                    ("Insert into Usuario(username, password, nome, nascimento) VALUES (?, ?, ?, ?)");

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, nome);
            preparedStatement.setDate(4, java.sql.Date.valueOf(dataNascimento));

            preparedStatement.execute();

            System.out.println("usuario: " + username + " adicionado com SUCESSO!");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void alterarUsuario(String newPassword, String newNome, String newDataNascimento, String username) {
        try {

            Connection conn = connection();

            PreparedStatement preparedStatement = conn.prepareStatement(
                    "UPDATE Usuario SET password = ?, nome = ?, nascimento = ? WHERE username = ?");

            preparedStatement.setString(1, newPassword);
            preparedStatement.setString(2, newNome);
            preparedStatement.setDate(3, java.sql.Date.valueOf(newDataNascimento));
            preparedStatement.setString(4, username);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Nome do usuário: " + username + "  atualizado com sucesso! para " + newNome);
            } else {
                System.out.println("Nenhum usuário encontrado com o username: " +username+ " fornecido.");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deletarUsuario(String username) {
        try {

            Connection conn = connection();

            PreparedStatement preparedStatement = conn.prepareStatement(
                    "DELETE FROM Usuario WHERE username = ?");

            preparedStatement.setString(1, username);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("O Usuário com username: " + username + " foi  deletado com sucesso!");
            } else {
                System.out.println("Nenhum usuário encontrado com o username: " +username+ " fornecido.");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deletarTodosUsuarios() {
        try {

            Connection conn = connection();

            PreparedStatement preparedStatement = conn.prepareStatement(
                    "DELETE FROM Usuario");

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Todos os Usuários foram deletados com sucesso!");
            } else {
                System.out.println("Nenhum usuário encontrado.");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void listarTodosUsuario(){
        try {

            Connection conn = connection();

            Statement statement = conn.createStatement();

            ResultSet result = statement.executeQuery("SELECT * FROM Usuario");

            System.out.println("--------=======LISTA DE USUARIOS=======--------");
            while(result.next()){

                System.out.printf("Codigo: ");
                System.out.printf(result.getString("codigo"));
                System.out.printf(" Username: ");
                System.out.printf(result.getString("username"));
                System.out.printf(" Nome: ");
                System.out.println(result.getString("nome"));

            }
            System.out.println("--------------====================--------------");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}