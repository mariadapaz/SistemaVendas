/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author maria
 */
public class Cliente {
    private String cpf;
    private String nome;
    private String bairro;
    private String rua;
    private int numeroDaCasa;
    private String email;

    // Construtores
    public Cliente() {}

    public Cliente(String cpf, String nome, String bairro, String rua, int numeroDaCasa, String email) {
        this.cpf = cpf;
        this.nome = nome;
        this.bairro = bairro;
        this.rua = rua;
        this.numeroDaCasa = numeroDaCasa;
        this.email = email;
    }

    // Getters e Setters
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getBairro() { return bairro; }
    public void setBairro(String bairro) { this.bairro = bairro; }

    public String getRua() { return rua; }
    public void setRua(String rua) { this.rua = rua; }

    public int getNumeroDaCasa() { return numeroDaCasa; }
    public void setNumeroDaCasa(int numeroDaCasa) { this.numeroDaCasa = numeroDaCasa; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}