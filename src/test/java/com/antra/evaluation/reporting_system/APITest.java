package com.antra.evaluation.reporting_system;

import com.antra.evaluation.reporting_system.endpoint.ExcelGenerationController;
import com.antra.evaluation.reporting_system.pojo.api.ExcelRequest;
import com.antra.evaluation.reporting_system.pojo.report.ExcelData;
import com.antra.evaluation.reporting_system.service.ExcelGenerationService;
import com.antra.evaluation.reporting_system.service.ExcelService;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.mockito.ArgumentMatchers.anyString;


//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = {ExcelGenerationController.class,ExcelGenerationService.class})
public class APITest {
    @Mock
    ExcelService excelService;
    @Mock
    ExcelGenerationService excelGenerationService;


    @BeforeEach
    public void configMock() {
        MockitoAnnotations.initMocks(this);
        RestAssuredMockMvc.standaloneSetup(new ExcelGenerationController(excelService,excelGenerationService));

    }

    @Test
    public void testFileDownload() throws FileNotFoundException {
        Mockito.when(excelService.getExcelBodyById(anyString())).thenReturn(new FileInputStream("temp.xlsx"));
        given().accept("application/json").get("/excel/123abcd/content").peek().
                then().assertThat()
                .statusCode(200);
    }

    @Test
    public void testListFiles() throws FileNotFoundException {
        Mockito.when(excelService.getExcelBodyById(anyString())).thenReturn(new FileInputStream("temp.xlsx"));
        given().accept("application/json").get("/excel").peek().
                then().assertThat()
                .statusCode(200);
    }

    @Test
    public void testDeleteFile() throws Exception {
        String str = "123";
        Mockito.when(excelService.delExcelFileById(str)).thenReturn(true);
        given().delete("/excel/123")
                .peek()
                .then().assertThat()
                .statusCode(200)
                .body("respMsg", Matchers.is("Already delete"));
    }

    @Test
    public void testExcelGeneration() throws FileNotFoundException, IOException {
        ExcelRequest er = new ExcelRequest();
        er.setDescription("this is my file");
        List<String> headers = new ArrayList<>();
        headers.add("Name");
        headers.add("Age");
        er.setHeaders(headers);

        List<List<Object>> data = new ArrayList<>();
        List<Object> data1 = new ArrayList<>();
        data1.add("Teresa");
        data1.add("5");
        List<Object> data2 = new ArrayList<>();
        data2.add("Daniel");
        data2.add("1");
        data.add(data1);
        data.add(data2);
        er.setData(data);
        er.setSplitBy("Age");
        er.setSubmitter("John");
        File file = new File("temp.xlsx");
        //Mockito.when(excelGenerationService.generateAndSaveExcelFile(er)).thenReturn();
        Mockito.when(excelGenerationService.generateExcelReport(new ExcelData())).thenReturn(file);
        given().accept("application/json").contentType(ContentType.JSON).body(er).post("/excel").peek()
                .then().assertThat()
                .statusCode(400);
                //.body("respMsg", Matchers.is("is ok"));
    }
}
