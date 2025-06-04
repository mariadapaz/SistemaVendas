/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.time.LocalDateTime;

public class Pedido {
    private int idPedido;
    private String cpfCliente;
    private String cpfFuncionario;
    private String formaPagamento;
    private LocalDateTime dataDeVenda;
    private LocalDateTime dataDeConfirmacao;
    private String status;

    public Pedido() {}

    public Pedido(int idPedido, String cpfCliente, String cpfFuncionario, String formaPagamento,
                  LocalDateTime dataDeVenda, LocalDateTime dataDeConfirmacao) {
        this.idPedido = idPedido;
        this.cpfCliente = cpfCliente;
        this.cpfFuncionario = cpfFuncionario;
        this.formaPagamento = formaPagamento;
        this.dataDeVenda = dataDeVenda;
        this.dataDeConfirmacao = dataDeConfirmacao;
        this.status = "Pendente";
    }

    // Getters e Setters
    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public String getCpfCliente() {
        return cpfCliente;
    }

    public void setCpfCliente(String cpfCliente) {
        this.cpfCliente = cpfCliente;
    }

    public String getCpfFuncionario() {
        return cpfFuncionario;
    }

    public void setCpfFuncionario(String cpfFuncionario) {
        this.cpfFuncionario = cpfFuncionario;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public LocalDateTime getDataDeVenda() {
        return dataDeVenda;
    }

    public void setDataDeVenda(LocalDateTime dataDeVenda) {
        this.dataDeVenda = dataDeVenda;
    }

    public LocalDateTime getDataDeConfirmacao() {
        return dataDeConfirmacao;
    }

    public void setDataDeConfirmacao(LocalDateTime dataDeConfirmacao) {
        this.dataDeConfirmacao = dataDeConfirmacao;
    }
    
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    
}
