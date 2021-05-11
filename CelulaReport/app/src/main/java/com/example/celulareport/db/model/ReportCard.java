package com.example.celulareport.db.model;

import androidx.room.ColumnInfo;

//BOJO to list of reports contents that from related month
public class ReportCard {

    @ColumnInfo(name = "id")
    private long id;

    @ColumnInfo(name = "celula")
    private String nomeCelula;

    @ColumnInfo(name = "lider")
    private String nomeLider;

    @ColumnInfo(name = "data_reuniao")
    private String dataReuniao;

    public ReportCard(long id, String nomeCelula, String nomeLider, String dataReuniao){
        this.id = id;
        this.nomeCelula = nomeCelula;
        this.nomeLider = nomeLider;
        this.dataReuniao = dataReuniao;
    }


    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getNomeCelula() {
        return nomeCelula;
    }

    public void setNomeCelula(String nomeCelula) {
        this.nomeCelula = nomeCelula;
    }

    public String getNomeLider() {
        return nomeLider;
    }

    public void setNomeLider(String nomeLider) {
        this.nomeLider = nomeLider;
    }

    public String getDataReuniao() {
        //Sorted String to dd/mm/yyyy
        if(dataReuniao.contains("-")) {
            String dataSplit[] = dataReuniao.split("-");
            String sortedData = dataSplit[2] + "/" + dataSplit[1] + "/" + dataSplit[0];
            return sortedData;
        }else return dataReuniao;
    }

    public void setDataReuniao(String dataReuniao) {
        this.dataReuniao = dataReuniao;
    }
}
