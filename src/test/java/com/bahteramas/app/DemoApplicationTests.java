package com.bahteramas.app;

import com.bahteramas.app.entity.Cuti;
import com.bahteramas.app.repository.CutiRepository;
import com.bahteramas.app.service.impl.IdsServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Locale;

@SpringBootTest
class DemoApplicationTests {

	private final IdsServiceImpl idsService;
	private final CutiRepository cutiRepository;

	@Autowired
	DemoApplicationTests(IdsServiceImpl idsService, CutiRepository cutiRepository) {
		this.idsService = idsService;
		this.cutiRepository = cutiRepository;
	}

	@BeforeEach
	void setUp() {
		Cuti cuti = new Cuti();
		cuti.setId("simple-cuti-id");
		cuti.setMessage("simple-message");
		cutiRepository.save(cuti);
	}

	@Test
	void simpleTest() {
		Cuti cuti = idsService.getCuti("simple-cuti-id");

		Assertions.assertNotNull(cuti);
	}

	@Test
	void dateTest() {
		Locale local = new Locale("id", "ID");
		String pattern = "dd MMMM yyyy";
		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, local);

		LocalDate date = LocalDate.now();
		String dateStart = dateFormat.format(date);
		System.out.println(dateStart);

	}
}
