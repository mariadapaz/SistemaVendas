/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author maria
 */

import model.ItemCarrinho;
import util.Conexao;

import java.sql.*;
import java.util.List;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.ArrayList;
import model.Pedido;

public class PedidoDAO {

    public void fazerPedido(String cpfCliente, String cpfFuncionario, String formaPagamento, List<ItemCarrinho> itens) {
        String sqlPedido = "INSERT INTO pedido (cpf_cliente, cpf_funcionario, forma_pagamento, data_de_venda, status) VALUES (?, ?, ?, ?, ?) RETURNING id_pedido";
        String sqlItem = "INSERT INTO pedido_has_produto (pedido_id_pedido, produto_id_produto, preco, quantidade) VALUES (?, ?, ?, ?)";
        String sqlEstoque = "UPDATE produto SET quantidade_no_estoque = quantidade_no_estoque - ? WHERE id_produto = ?";

        try (Connection conn = Conexao.getConexao()) {
            conn.setAutoCommit(false);

            int idPedido;

            // Inserir pedido
            try (PreparedStatement stmtPedido = conn.prepareStatement(sqlPedido)) {
                stmtPedido.setString(1, cpfCliente);
                stmtPedido.setString(2, cpfFuncionario);
                stmtPedido.setString(3, formaPagamento);
                stmtPedido.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
                stmtPedido.setString(5, "Pendente");

                ResultSet rs = stmtPedido.executeQuery();
                rs.next();
                idPedido = rs.getInt("id_pedido");
            }

            // Inserir itens e atualizar estoque
            try (
                PreparedStatement stmtItem = conn.prepareStatement(sqlItem);
                PreparedStatement stmtEstoque = conn.prepareStatement(sqlEstoque)
            ) {
                for (ItemCarrinho item : itens) {
                    stmtItem.setInt(1, idPedido);
                    stmtItem.setInt(2, item.getProduto().getIdProduto());
                    stmtItem.setBigDecimal(3, item.getProduto().getPreco());
                    stmtItem.setInt(4, item.getQuantidade());
                    stmtItem.executeUpdate();

                    stmtEstoque.setInt(1, item.getQuantidade());
                    stmtEstoque.setInt(2, item.getProduto().getIdProduto());
                    stmtEstoque.executeUpdate();
                }
            }

            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erro ao fazer pedido: " + e.getMessage());
        }
    }
    public List<Pedido> listarPedidosPendentes() {
    List<Pedido> lista = new ArrayList<>();
    String sql = "SELECT * FROM pedido WHERE data_de_confirmacao IS NULL";

    try (Connection conn = Conexao.getConexao();
         PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {

        while (rs.next()) {
            Pedido p = new Pedido();
            p.setIdPedido(rs.getInt("id_pedido"));
            p.setCpfCliente(rs.getString("cpf_cliente"));
            p.setCpfFuncionario(rs.getString("cpf_funcionario"));
            p.setFormaPagamento(rs.getString("forma_pagamento"));
            p.setDataDeVenda(rs.getTimestamp("data_de_venda").toLocalDateTime());
            p.setStatus(rs.getString("status"));

            lista.add(p);
        }

    } catch (SQLException e) {
        System.out.println("Erro ao listar pedidos pendentes: " + e.getMessage());
    }

    return lista;
}
    public boolean confirmarPedido(int idPedido) {
    String sql = "UPDATE pedido SET data_de_confirmacao = CURRENT_TIMESTAMP, status = 'Finalizado' WHERE id_pedido = ?";

    try (Connection conn = Conexao.getConexao();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, idPedido);
        int linhasAfetadas = stmt.executeUpdate();
        return linhasAfetadas > 0;

    } catch (SQLException e) {
        System.out.println("Erro ao confirmar pedido: " + e.getMessage());
        return false;
    }
    }
    
    public boolean cancelarPedido(int idPedido) {
    String sql = "UPDATE pedido SET status = 'Cancelado' WHERE id_pedido = ?";

    try (Connection conn = Conexao.getConexao();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, idPedido);
        int linhasAfetadas = stmt.executeUpdate();
        return linhasAfetadas > 0;

    } catch (SQLException e) {
        System.out.println("Erro ao cancelar pedido: " + e.getMessage());
        return false;
    }
}
    public List<String[]> listarVendasConsolidadas() {
    List<String[]> lista = new ArrayList<>();

    String sql = """
        SELECT p.id_pedido, c.nome AS cliente, f.nome AS funcionario,
               p.forma_pagamento, p.data_de_venda, p.data_de_confirmacao,
               SUM(pp.preco * pp.quantidade) AS total
        FROM pedido p
        JOIN cliente c ON c.cpf = p.cpf_cliente
        JOIN funcionario f ON f.cpf = p.cpf_funcionario
        JOIN pedido_has_produto pp ON pp.pedido_id_pedido = p.id_pedido
        GROUP BY p.id_pedido, c.nome, f.nome, p.forma_pagamento, p.data_de_venda, p.data_de_confirmacao
        ORDER BY p.data_de_confirmacao DESC
    """;

    try (Connection conn = Conexao.getConexao();
         PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {

        while (rs.next()) {
            lista.add(new String[] {
                String.valueOf(rs.getInt("id_pedido")),
                rs.getString("cliente"),
                rs.getString("funcionario"),
                rs.getString("forma_pagamento"),
                rs.getTimestamp("data_de_venda").toString(),
                rs.getTimestamp("data_de_confirmacao").toString(),
                rs.getString("status").toString(),
                rs.getBigDecimal("total").toString()
            });
        }

    } catch (SQLException e) {
        System.out.println("Erro ao gerar relatório de vendas: " + e.getMessage());
    }

    return lista;
}



}
