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
import model.Produto;
import model.RelatorioPorId;

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
    
    public List<Pedido> listar() {
        List<Pedido> lista = new ArrayList<>();
        String sql = "SELECT * FROM pedido";

        try (Connection conn = Conexao.getConexao();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Pedido pedido = new Pedido();
                pedido.setIdPedido(rs.getInt("id_pedido"));
                pedido.setDataDeVenda(
                rs.getTimestamp("data_de_venda") != null
                ? rs.getTimestamp("data_de_venda").toLocalDateTime()
        : null
);

                pedido.setStatus(rs.getString("status"));

                lista.add(pedido);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar pedidos: " + e.getMessage());
        }

        return lista;
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
           p.status,
           SUM(pp.preco * pp.quantidade) AS total
    FROM pedido p
    JOIN cliente c ON c.cpf = p.cpf_cliente
    JOIN funcionario f ON f.cpf = p.cpf_funcionario
    JOIN pedido_has_produto pp ON pp.pedido_id_pedido = p.id_pedido
    GROUP BY p.id_pedido, c.nome, f.nome, p.forma_pagamento,
             p.data_de_venda, p.data_de_confirmacao, p.status
    ORDER BY p.data_de_confirmacao DESC
""";


    try (Connection conn = Conexao.getConexao();
         PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {

        while (rs.next()) {
    Timestamp dataVenda = rs.getTimestamp("data_de_venda");
    Timestamp dataConfirmacao = rs.getTimestamp("data_de_confirmacao");

    lista.add(new String[] {
        String.valueOf(rs.getInt("id_pedido")),
        rs.getString("cliente"),
        rs.getString("funcionario"),
        rs.getString("forma_pagamento"),
        (dataVenda != null) ? dataVenda.toString() : "Não registrada",
        (dataConfirmacao != null) ? dataConfirmacao.toString() : "Não confirmada",
        rs.getString("status"),
        rs.getBigDecimal("total").toString()
    });
}


    } catch (SQLException e) {
        System.out.println("Erro ao gerar relatório de vendas: " + e.getMessage());
    }

    return lista;
}

    public List<String[]> listarProdutoEmFalta() {
    List<String[]> lista = new ArrayList<>();

    String sql = """
    select p.nome, p.quantidade_no_estoque, p.preco, p.tipo, p.status 
    from produto p where p.quantidade_no_estoque = 0;
""";


    try (Connection conn = Conexao.getConexao();
         PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {

        while (rs.next()) {

    lista.add(new String[] {
        rs.getString("nome"),
        rs.getString("quantidade_no_estoque"),
        rs.getBigDecimal("Preco").toString(),
        rs.getString("Tipo"),
        rs.getString("Status")
    });
}


    } catch (SQLException e) {
        System.out.println("Erro ao gerar relatório de vendas: " + e.getMessage());
    }

    return lista;
}
    
    public RelatorioPorId buscarPorId(int id) {
    String sql = """
    SELECT P.FORMA_PAGAMENTO, P.DATA_DE_VENDA, F.NOME, C.NOME, PP.PRECO, p.data_de_confirmacao 
    FROM PEDIDO p
    JOIN PEDIDO_HAS_PRODUTO PP ON PP.Pedido_ID_PEDIDO = P.ID_PEDIDO
    JOIN PRODUTO PROD ON PROD.ID_PRODUTO = PP.Produto_ID_PRODUTO
    JOIN FUNCIONARIO F ON F.CPF = P.CPF_funcionario
    JOIN CLIENTE C ON C.CPF = P.CPF_cliente
    WHERE p.id_pedido = ?
""";
    try (Connection conn = Conexao.getConexao();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        
        if (rs.next()) {
            RelatorioPorId r = new RelatorioPorId();
            Produto pp = new Produto();
            r.setForma_pagamento(rs.getString("forma_pagamento"));
            r.setData_de_venda(
                rs.getTimestamp("data_de_venda") != null
                        
                        
                        
                ? rs.getTimestamp("data_de_venda").toLocalDateTime()
        : null
);
             r.setNome_funcionario(rs.getString("nome"));
             r.setNome_cliente(rs.getString("nome"));
             r.setPreco(rs.getBigDecimal("preco"));
             r.setData_de_confirmacao(
                rs.getTimestamp("data_de_confirmacao") != null
                        
                        
                        
                ? rs.getTimestamp("data_de_confirmacao").toLocalDateTime()
        : null
);
             
            return r;
        }
    } catch (Exception e) {
        System.out.println("Erro ao buscar pedido: " + e.getMessage());
    }
    return null;
}


}
