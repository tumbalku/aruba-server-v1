package com.bahteramas.app.entity;

public enum CutiType {
  SAKIT("Sakit"),
  TAHUNAN("Tahunan"),
  BERSALIN("Bersalin"),
  BESAR("Besar"),
  KARENA_ALASAN_PENTING("Karena Alasan Penting");

  private final String description;

  CutiType(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }



}