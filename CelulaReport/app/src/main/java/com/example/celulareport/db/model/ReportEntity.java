package com.example.celulareport.db.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

//BOJO to retrieve information from reports and store ou show in somewhere
@Entity(tableName = "report_table")
public class ReportEntity {


    @PrimaryKey(autoGenerate = true)
    @NonNull
    private long id;

    @NonNull
    @ColumnInfo(name = "celula")
    private String nomeCelula;

    @NonNull
    @ColumnInfo(name = "lider")
    private String nomeLider;

    @NonNull
    @ColumnInfo(name = "colider")
    private String nomeColider;

    @NonNull
    @ColumnInfo(name = "nome_anfitriao")
    private String nomeAnfitriao;

    @NonNull
    @ColumnInfo(name = "data_reuniao")
    private String dataReuniao;

    @NonNull
    @ColumnInfo(name = "n_membros")
    private String numMembros;

    @NonNull
    @ColumnInfo(name = "n_visitantes")
    private String numVisitantes;

    @NonNull
    private String oferta;

    @Nullable
    private String estudo;

    @Nullable
    private String comentarios;

    public ReportEntity(){
        comentarios = "";
        estudo = "";
    }

    public String getEstudo() {
        return estudo;
    }

    public void setEstudo(String estudo) {
        this.estudo = estudo;
    }

    @NonNull
    public String getOferta() {
        return oferta;
    }

    public void setOferta(@NonNull String oferta) {
        this.oferta = oferta;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) { this.id = id;}

    @NonNull
    public String getNomeCelula() {
        return nomeCelula;
    }

    public void setNomeCelula(@NonNull String nomeCelula) {
        this.nomeCelula = nomeCelula;
    }

    @NonNull
    public String getNomeLider() {
        return nomeLider;
    }

    public void setNomeLider(@NonNull String nomeLider) {
        this.nomeLider = nomeLider;
    }

    @NonNull
    public String getNomeColider() {
        return nomeColider;
    }

    public void setNomeColider(@NonNull String nomeColider) {
        this.nomeColider = nomeColider;
    }

    @NonNull
    public String getNomeAnfitriao() {
        return nomeAnfitriao;
    }

    public void setNomeAnfitriao(@NonNull String nomeAnfitriao) {
        this.nomeAnfitriao = nomeAnfitriao;
    }

    @NonNull
    public String getDataReuniao() {

        return dataReuniao;
    }

    public String getDataToShow(){
        String dataToShow;
        //change data to yyyy/mm/dd
        if(dataReuniao.contains("-")) {

            String dataSplit[] = dataReuniao.split("-");
            dataToShow = dataSplit[2] + "/" + dataSplit[1] + "/" + dataSplit[0];

        }else dataToShow = dataReuniao;

        return dataToShow;
    }

    public void setDataReuniao(@NonNull String dataReuniao) {

        //change data to yyyy/mm/dd
        if(dataReuniao.contains("/")) {

            String dataSplit[] = dataReuniao.split("/");
            dataReuniao = dataSplit[2] + "-" + dataSplit[1] + "-" + dataSplit[0];


        }

        this.dataReuniao = dataReuniao;
    }

    @NonNull
    public String getNumMembros() {
        return numMembros;
    }

    public void setNumMembros(@NonNull String numMembros) {
        this.numMembros = numMembros;
    }

    @NonNull
    public String getNumVisitantes() {
        return numVisitantes;
    }

    public void setNumVisitantes(@NonNull String numVisitantes) {
        this.numVisitantes = numVisitantes;
    }

    @Nullable
    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(@Nullable String comentarios) {
        this.comentarios = comentarios;
    }

}
