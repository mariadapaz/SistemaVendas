/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author maria
 */
import model.Produto;
import util.Conexao;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {

    public void inserir(Produto produto) {
        String sql = "INSERT INTO produto (nome, tipo, preco, quantidade_no_estoque) VALUES (?, ?, ?, ?)";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getTipo());
            stmt.setBigDecimal(3, produto.getPreco());
            stmt.setInt(4, produto.getQuantidadeNoEstoque());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao inserir produto: " + e.getMessage());
        }
    }

    public List<Produto> listar() {
        List<Produto> lista = new ArrayList<>();
        String sql = "SELECT * FROM produto";

        try (Connection conn = Conexao.getConexao();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Produto produto = new Produto();
                produto.setIdProduto(rs.getInt("id_produto"));
                produto.setNome(rs.getString("nome"));
                produto.setTipo(rs.getString("tipo"));
                produto.setPreco(rs.getBigDecimal("preco"));
                produto.setQuantidadeNoEstoque(rs.getInt("quantidade_no_estoque"));

                lista.add(produto);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar produtos: " + e.getMessage());
        }

        return lista;
    }

    public void atualizar(Produto produto) {
        String sql = "UPDATE produto SET nome=?, tipo=?, preco=?, quantidade_no_estoque=? WHERE id_produto=?";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getTipo());
            stmt.setBigDecimal(3, produto.getPreco());
            stmt.setInt(4, produto.getQuantidadeNoEstoque());
            stmt.setInt(5, produto.getIdProduto());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar produto: " + e.getMessage());
        }
    }

    public void deletar(int idProduto) {
        String sql = "DELETE FROM produto WHERE id_produto = ?";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idProduto);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao deletar produto: " + e.getMessage());
        }
    }
    
    public Produto buscarPorId(int id) {
    String sql = "SELECT * FROM produto WHERE id_produto = ?";
    try (Connection conn = Conexao.getConexao();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            Produto p = new Produto();
            p.setIdProduto(rs.getInt("id_produto"));
            p.setNome(rs.getString("nome"));
            p.setTipo(rs.getString("tipo"));
            p.setPreco(rs.getBigDecimal("preco"));
            p.setQuantidadeNoEstoque(rs.getInt("quantidade_no_estoque"));
            return p;
        }
    } catch (Exception e) {
        System.out.println("Erro ao buscar produto: " + e.getMessage());
    }
    return null;
}

}
