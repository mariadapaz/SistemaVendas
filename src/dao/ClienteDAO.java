/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author maria
 */
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Cliente;
import util.Conexao;

public class ClienteDAO {

    public void inserir(Cliente cliente) {
        String sql = "INSERT INTO cliente (cpf, nome, bairro, rua, numero_da_casa, email) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cliente.getCpf());
            stmt.setString(2, cliente.getNome());
            stmt.setString(3, cliente.getBairro());
            stmt.setString(4, cliente.getRua());
            stmt.setInt(5, cliente.getNumeroDaCasa());
            stmt.setString(6, cliente.getEmail());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao inserir cliente: " + e.getMessage());
        }
    }

    public List<Cliente> listar() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM cliente";

        try (Connection conn = Conexao.getConexao();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setCpf(rs.getString("cpf"));
                cliente.setNome(rs.getString("nome"));
                cliente.setBairro(rs.getString("bairro"));
                cliente.setRua(rs.getString("rua"));
                cliente.setNumeroDaCasa(rs.getInt("numero_da_casa"));
                cliente.setEmail(rs.getString("email"));

                lista.add(cliente);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar clientes: " + e.getMessage());
        }

        return lista;
    }

    public void atualizar(Cliente cliente) {
        String sql = "UPDATE cliente SET nome=?, bairro=?, rua=?, numero_da_casa=?, email=? WHERE cpf=?";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getBairro());
            stmt.setString(3, cliente.getRua());
            stmt.setInt(4, cliente.getNumeroDaCasa());
            stmt.setString(5, cliente.getEmail());
            stmt.setString(6, cliente.getCpf());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar cliente: " + e.getMessage());
        }
    }

    public void deletar(String cpf) {
        String sql = "DELETE FROM cliente WHERE cpf = ?";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpf);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao deletar cliente: " + e.getMessage());
        }
    }
}