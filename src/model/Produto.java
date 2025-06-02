/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author maria
 */
import java.math.BigDecimal;

public class Produto {
    private int idProduto;
    private String nome;
    private String tipo;
    private BigDecimal preco;
    private int quantidadeNoEstoque;

    public Produto() {}

    public Produto(int idProduto, String nome, String tipo, BigDecimal preco, int quantidadeNoEstoque) {
        this.idProduto = idProduto;
        this.nome = nome;
        this.tipo = tipo;
        this.preco = preco;
        this.quantidadeNoEstoque = quantidadeNoEstoque;
    }

    public int getIdProduto() { return idProduto; }
    public void setIdProduto(int idProduto) { this.idProduto = idProduto; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public BigDecimal getPreco() { return preco; }
    public void setPreco(BigDecimal preco) { this.preco = preco; }

    public int getQuantidadeNoEstoque() { return quantidadeNoEstoque; }
    public void setQuantidadeNoEstoque(int quantidadeNoEstoque) { this.quantidadeNoEstoque = quantidadeNoEstoque; }
}