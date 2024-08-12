package com.bahteramas.app.service.text;

import com.bahteramas.app.entity.CivilServant;
import com.bahteramas.app.entity.Cuti;
import com.bahteramas.app.entity.CutiType;
import com.bahteramas.app.entity.User;
import com.bahteramas.app.model.request.CutiDetailRequest;
import com.bahteramas.app.model.request.CutiRequest;
import com.bahteramas.app.service.impl.IdsServiceImpl;
import com.bahteramas.app.utils.Helper;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static com.bahteramas.app.service.text.PdfUtils.*;

@Service
@AllArgsConstructor
public class CutiPdfService {

  private final IdsServiceImpl idsService;

  public void makeAnCutiReport(String id, CutiRequest request) throws FileNotFoundException, MalformedURLException {

    Cuti cuti = idsService.getCuti(id);
    User user = cuti.getUser();
    String output = String.format("cuti-pdfs/%s.pdf",id);

    PdfWriter writer = new PdfWriter(output);
    PdfDocument pdfDocument = new PdfDocument(writer);
    pdfDocument.setDefaultPageSize(PageSize.A4);

    Table header = new Table(new float[]{
            percentPerWidth(container, 2.0f / 12),
            percentPerWidth(container, 10.0f / 12),
    });

    // header title
    Table headerTitle = new Table(new float[]{percentPerWidth(container, 12f / 12)});

    headerTitle.addCell(setTextBold("PEMERINTAH PROVINSI SULAWESI TENGGARA", 12f)
            .setTextAlignment(TextAlignment.CENTER)
            .setPaddingBottom(-3.f)
            .setPaddingTop(8f)
    );
    headerTitle.addCell(setTextBold("RUMAH SAKIT UMUM DAERAH BAHTERAMAS", 16f)
            .setTextAlignment(TextAlignment.CENTER)
            .setPaddingBottom(-3.f)
    );
    headerTitle.addCell(setText("Jalan Kapten Piere Tandean No. 50 Telp. (0401) 3195611 Baruga Kendari", 9f)
            .setTextAlignment(TextAlignment.CENTER)
            .setPaddingBottom(-3.f)
    );
    headerTitle.addCell(setText("Email: admin@rsud-bahteramas.go.id Website: www.rsud-bahteramas.go.id", 9f)
            .setTextAlignment(TextAlignment.CENTER)
            .setCharacterSpacing(0.5f)
    );
    // -------------

    // header
    header.addCell(new Cell().add(image("logo-anoa-sultra.png")
                    .setWidth(95f)
                    .setHeight(70f))
            .setBorder(Border.NO_BORDER)
    );
    header.addCell(new Cell().add(headerTitle).setBorder(Border.NO_BORDER));

    // -------------------------------------------------------------------------

    Table core = new Table(new float[]{wFull});


    CutiType cutiType = CutiType.valueOf(request.getType());
    String cutiTypeName = switch (cutiType) {
      case SAKIT -> "SICS";
      case TAHUNAN -> "CT";
      case BERSALIN -> "SICB";
      case BESAR -> "CB";
      case KARENA_ALASAN_PENTING -> "SCAP";
      default -> "SI";
    };


    // core title
    core.addCell(setTextBold(String.format("SURAT IZIN %s", request.getName().toUpperCase()), 9)
            .setUnderline()
            .setTextAlignment(TextAlignment.CENTER));


    String tahun = Integer.toString(LocalDate.now().getYear());

    core.addCell(setText(String.format("No. %s/%s/%s/RSUD/%s/%s",
                request.getUnicode(),
                request.getNum(),
                cutiTypeName,
                request.getRomawi(),
                tahun),9)
            .setPadding(-2f)
            .setPaddingBottom(30f)
            .setTextAlignment(TextAlignment.CENTER));


    core.addCell(new Cell().add(bigPoint("1.",
                    String.format("Diberikan %s Kepada Pegawai Negeri Sipil:", Helper.capitalizeWords(request.getName()))))
            .setBorder(Border.NO_BORDER));

    float [] userInfo = {195f, 10, 390};
    Table tblUserInfo = new Table(userInfo).setBorder(Border.NO_BORDER);

    String rankGroup = "-";
    String position = "-";
    String workUnit = "-";
    String address = "-";
    if(Objects.nonNull(user.getCivilServant())){
      CivilServant cs = user.getCivilServant();
      rankGroup =  cs.getRank() + ", " + cs.getGroup();
      position = cs.getPosition();
      workUnit = cs.getWorkUnit();
    }
    if(Objects.nonNull(user.getAddress())){
      address = user.getAddress().getName();
    }
    Map<String, String> userDetail = new LinkedHashMap<>();
    userDetail.put("Nama", user.getName());

    if(request.getIsPegawai()){
      userDetail.put("NIP", user.getId());
    }
    userDetail.put("Pangkat / Gologan", rankGroup);
    userDetail.put("Jabatan", position);
    userDetail.put("Unit Kerja", workUnit);
    if(Objects.nonNull(request.getAddress())){
      userDetail.put("Alamat selama Cuti", request.getAddress());
    }else{
      userDetail.put("Alamat selama Cuti", address);
    }

    Table testTbl = tableDataListKeyValue(tblUserInfo, userDetail);
    core.addCell(new Cell().add(testTbl).setBorder(Border.NO_BORDER));


    // cuti detail
    Locale local = new Locale("id", "ID");
    String pattern = "dd MMMM yyyy";
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern, local);

    LocalDate cutiDateStart = cuti.getDateStart();
    LocalDate cutiDateEnd = cuti.getDateEnd();

    String dateStart = cutiDateStart.format(dateFormatter);
    String dateEnd = cutiDateEnd.format(dateFormatter);

    long daysBetween = ChronoUnit.DAYS.between(cuti.getDateStart(), cuti.getDateEnd());

    core.addCell(setText(
            String.format("Selama %d (%s) hari terhitung mulai tanggal %s s/d %s dengan ketentuan sebagai berikut:",
                    daysBetween, convert(daysBetween), dateStart, dateEnd), 9
    ).setPaddingLeft(20f).setPaddingTop(10f));

    core.addCell(new Cell().add(smallPoint("a.",
            String.format("Sebelum menjalankan %s wajib menyerahkan pekerjaannya kepada atasan langsungnya atau pejabat yang ditunjuk.", request.getName())))
            .setBorder(Border.NO_BORDER));
    if(cutiTypeName.equals("SICB")){
      core.addCell(new Cell().add(smallPoint("b.","Segera setelah persalinan yang bersangkutan supaya memberitahukan tanggal persalinan kepada pejabat yang berwenang memberikan cuti"))
              .setBorder(Border.NO_BORDER));
      core.addCell(new Cell().add(smallPoint("c.",
                      String.format("Setelah selesai menjalankan %s wajib melaporkan diri kepada atasan langsungnya dan bekerja kembali sebagai mana mestinya.", request.getName())))
              .setBorder(Border.NO_BORDER));
    }else{
      core.addCell(new Cell().add(smallPoint("b.",
                      String.format("Setelah selesai menjalankan %s wajib melaporkan diri kepada atasan langsungnya dan bekerja kembali sebagai mana mestinya.", request.getName())))
              .setBorder(Border.NO_BORDER));
    }


    core.addCell(new Cell().add(bigPoint("2.",
            String.format("Demikian Surat Izin %s ini dibuat untuk dipergunakan sebagaimana mestinya.", Helper.capitalizeWords(request.getName()))))
            .setBorder(Border.NO_BORDER));

    // Table tanda tanggan
    float [] ttdSize = {330f, 265};
    Table tblTTD = new Table(ttdSize);
    tblTTD.addCell(new Cell().setBorder(Border.NO_BORDER));
    LocalDate cutiConfirmDate = cuti.getConfirmDate();

    String formatTddDate = cutiConfirmDate.format(dateFormatter);
    String ttdDate = String.format("Kendari, %s", formatTddDate);
    // actual TTD

    //
    Table ttd = new Table(new float[]{percentPerWidth(container, 11f / 12)});
    ttd.addCell(setText(ttdDate, 9).setPaddingTop(50f));
    ttd.addCell(setTextBold("Direktur,", 8).setPaddingBottom(60f));
    ttd.addCell(setTextBold("dr. H. Hasmudin, Sp.B", 8).setUnderline());
    ttd.addCell(setText("Pembina Tk I. Gol IV/ ", 8).setPaddingTop(-3f));
    ttd.addCell(setText("NIP. 1234567880123", 8));

    tblTTD.addCell(new Cell().add(ttd).setBorder(Border.NO_BORDER));

    Table tembusan = new Table(new float[]{wFull});
    Table tembusanHeader = new Table(new float[]{70,525});

    tembusanHeader.addCell(setTextBold("Tembusan :", 8).setUnderline());
    tembusanHeader.addCell(setText("Disampaikan Kepada Yth,", 9).setPaddingLeft(-5f));
    tembusan.addCell(new Cell().add(tembusanHeader).setBorder(Border.NO_BORDER).setPaddingTop(30f));

    List<String> tembusanList = new LinkedList<>();

    if(request.getTembusan().size() != 0){
      request.getTembusan().forEach(tempD -> tembusanList.add(tempD.concat(";")));
    }
    tembusanList.add("Yang Bersangkutan Untuk diketahui;");
    tembusanList.add("Pertinggal.");

    cellDataList(tembusan, tembusanList);



    // document
    Document document = new Document(pdfDocument);
    document.add(header);
    document.add(underline(container, 0.9f , Color.BLACK).setMarginBottom(20f));
    document.add(core);
    document.add(tblTTD);
    document.add(tembusan);


    document.close();

    System.out.println("Completed");
  }
}
