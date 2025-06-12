/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 *
 * @author maria
 */
public class RelatorioPorId {
    private String forma_pagamento;
    LocalDateTime data_de_venda;
    private String nome_funcionario;
    private String nome_cliente;
    private BigDecimal preco;
    LocalDateTime data_de_confirmacao;

    public RelatorioPorId() {}
    
    public RelatorioPorId(String forma_pagamento, LocalDateTime data_de_venda, String nome_funcionario, String nome_cliente, BigDecimal preco, LocalDateTime data_de_confirmacao) {
        this.forma_pagamento = forma_pagamento;
        this.data_de_venda = data_de_venda;
        this.nome_funcionario = nome_funcionario;
        this.nome_cliente = nome_cliente;
        this.preco = preco;
        this.data_de_confirmacao = data_de_confirmacao;
        
    }


    public String getNome_funcionario() {
        return nome_funcionario;
    }

    public void setNome_funcionario(String nome_funcionario) {
        this.nome_funcionario = nome_funcionario;
    }

    public String getNome_cliente() {
        return nome_cliente;
    }

    public void setNome_cliente(String nome_cliente) {
        this.nome_cliente = nome_cliente;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public String getForma_pagamento() {
        return forma_pagamento;
    }

    public void setForma_pagamento(String forma_pagamento) {
        this.forma_pagamento = forma_pagamento;
    }

    public LocalDateTime getData_de_venda() {
        return data_de_venda;
    }

    public void setData_de_venda(LocalDateTime data_de_venda) {
        this.data_de_venda = data_de_venda;
    }

    public LocalDateTime getData_de_confirmacao() {
        return data_de_confirmacao;
    }

    public void setData_de_confirmacao(LocalDateTime data_de_confirmacao) {
        this.data_de_confirmacao = data_de_confirmacao;
    }
    
    
    
}
