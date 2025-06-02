/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;


import java.sql.Connection;

public class TesteConexao {
    public static void main(String[] args) {
        System.out.println("▶️ Iniciando teste de conexão...");

        try {
            Connection conn = Conexao.getConexao();
            if (conn != null) {
                System.out.println("✅ Conexão estabelecida com sucesso!");
                conn.close();
            } else {
                System.out.println("❌ Falha na conexão.");
            }
        } catch (Exception e) {
            System.out.println("❌ Erro ao conectar:");
            e.printStackTrace();
        }
    }
}
