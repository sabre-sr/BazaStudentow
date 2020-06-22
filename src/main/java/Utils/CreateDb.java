package Utils;


import Models.Dziekanat;
import Services.BazaDanych;

import javax.swing.*;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class CreateDb {
    public static boolean newDB() {
        Connection conn = null;
        String path = "jdbc:sqlite:baza.db";
        try {
            conn = DriverManager.getConnection(path);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        assert conn != null;
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement("create table studenci\n" +
                    "(\n" +
                    "    id           integer      not null\n" +
                    "        constraint studenci_pk\n" +
                    "            primary key autoincrement,\n" +
                    "    imienazwisko text,\n" +
                    "    passwordhash VARCHAR(512) not null,\n" +
                    "    salt         BINARY(16)   not null,\n" +
                    "    pesel        text         not null,\n" +
                    "    rokstudiow   int          not null,\n" +
                    "    nralbumu     int          not null\n" +
                    ");\n" +
                    "\n" +
                    "create unique index studenci_id_uindex\n" +
                    "    on studenci (id);\n" +
                    "\n" +
                    "create unique index studenci_nralbumu_uindex\n" +
                    "    on studenci (nralbumu);\n" +
                    "\n");

        ps.execute();
        ps = conn.prepareStatement("create table prowadzacy\n" +
                "(\n" +
                "    id           INTEGER      not null\n" +
                "        constraint prowadzacy_pk\n" +
                "            primary key autoincrement,\n" +
                "    imienazwisko text         not null,\n" +
                "    passwordhash VARCHAR(512) not null,\n" +
                "    przedmiot    text         not null,\n" +
                "    salt         BINARY(16)\n" +
                ");\n" +
                "\n");
        ps.execute();
        ps = conn.prepareStatement("create table dziekanat\n" +
                "(\n" +
                "    id           INTEGER      not null\n" +
                "        constraint dziekanat_pk\n" +
                "            primary key autoincrement,\n" +
                "    imienazwisko text         not null,\n" +
                "    passwordhash VARCHAR(512) not null,\n" +
                "    salt         BINARY(16)\n" +
                ");\n" +
                "\n" +
                "create unique index dziekanat_id_uindex\n" +
                "    on dziekanat (id);\n" +
                "\n");
        ps.execute();
        ps = conn.prepareStatement("create table przedmioty\n" +
                "(\n" +
                "    id          integer\n" +
                "        constraint przedmioty_pk\n" +
                "            primary key autoincrement,\n" +
                "    nazwa       text,\n" +
                "    nazwatabeli text\n" +
                ");\n" +
                "\n" +
                "create unique index przedmioty_id_uindex\n" +
                "    on przedmioty (id);\n" +
                "\n" +
                "create unique index przedmioty_nazwa_uindex\n" +
                "    on przedmioty (nazwa);\n" +
                "\n" +
                "create unique index przedmioty_nazwatablicy_uindex\n" +
                "    on przedmioty (nazwatabeli);\n" +
                "\n");
            ps.execute();
            conn.close();
            String haslo = "[r, o, o, t]";
            BazaDanych.bazaDanych.addDziekanat(new Dziekanat("root"), haslo.toCharArray());
            JOptionPane.showMessageDialog(null, "Baza danych została utworzona pomyślnie. Dane dostępu do dziekanatu: \n Login: root \n Hasło: root");
        } catch (SQLException | InvalidKeySpecException | NoSuchAlgorithmException throwables) {
            throwables.printStackTrace();
            return false;
        }
        return true;
    }
}
