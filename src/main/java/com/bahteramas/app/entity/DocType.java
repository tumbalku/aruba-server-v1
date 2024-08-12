package com.bahteramas.app.entity;

public enum DocType {

  KGB("Kenaikan Gaji Berkala"),
  SIP("Surat Izin Penelitian"),
  SPMT("Surat PMT"),
  AKREDITASI("Akreditasi");

  private final String description;

  DocType(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }
}
