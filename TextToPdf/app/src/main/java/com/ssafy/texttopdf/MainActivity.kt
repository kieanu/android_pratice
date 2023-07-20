package com.ssafy.texttopdf

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.util.Log
import android.view.Display
import android.view.LayoutInflater
import android.view.PixelCopy
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import com.itextpdf.kernel.font.PdfFontFactory
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.properties.HorizontalAlignment
import com.itextpdf.layout.properties.TextAlignment
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btn = findViewById<Button>(R.id.btn_pdf)
        val name = findViewById<EditText>(R.id.name)
        // 버튼 클릭 이벤트 핸들러
        btn.setOnClickListener {
            val text = name.text.toString()

            if (text.isNotEmpty()) {
                // createPdfDocument(this, text)
                getXmlToJpg(this, this, R.layout.activity_main, "${externalCacheDir!!.absolutePath}/output.jpg")
            }
        }
    }
    fun getXmlToJpg(activity:MainActivity, context: Context, xmlResId: Int, outPath: String): Boolean {
        // XML 파일을 뷰로 인플레이트합니다.
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(xmlResId, null)

        // 뷰의 크기를 가져옵니다.
        val displayMetrics = DisplayMetrics()

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            val display = context.getSystemService(WindowManager::class.java).currentWindowMetrics
            display.bounds.also {
                displayMetrics.widthPixels = it.width()
                displayMetrics.heightPixels = it.height()
            }
        } else {
            @Suppress("DEPRECATION")
            val display = activity.windowManager.defaultDisplay
            @Suppress("DEPRECATION")
            display.getMetrics(displayMetrics)
        }
        val viewWidth = displayMetrics.widthPixels
        val viewHeight = displayMetrics.heightPixels

        // 뷰를 비트맵으로 변환합니다.
        val bitmap = Bitmap.createBitmap(viewWidth, viewHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.WHITE)
        view.draw(canvas)

        // 비트맵을 JPG 파일로 저장합니다.
        val outStream = FileOutputStream(outPath)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream)
        outStream.close()

        return true
    }

    fun createPdfDocument(context: Context, text: String) {
        // PDF 문서를 생성할 때 사용할 파일 경로 및 이름 설정
        val filePath = File(context.getExternalFilesDir(null), "output.pdf").absolutePath
        val file = File(filePath)
        // 한글 폰트 파일 경로
        val fontPath = "res/font/nanumgothic.ttf"

        // 폰트 등록
        PdfFontFactory.register(fontPath, "nanumgothic")

        // 폰트 인코딩 설정
        val koreanFont = PdfFontFactory.createRegisteredFont("nanumgothic")

        val pdfWriter = PdfWriter(file)
        val pdfDocument = PdfDocument(pdfWriter)
        val document = Document(pdfDocument)


        pdfDocument.defaultPageSize = PageSize.A4
        document.setMargins(0f, 0f, 0f, 0f)

        val title = Paragraph("n월 교육지원금 서명").setFont(koreanFont).setBold().setFontSize(24f).setTextAlignment(TextAlignment.CENTER)

        val tables = arrayOf(
            Table(floatArrayOf(60f, 240f, 60f, 140f)).setFont(koreanFont)
                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                .addCell("소속")
                .addCell("[] 캠퍼스 [] 반")
                .addCell("성명")
                .addCell("OOO"),
            Table(floatArrayOf(60f, 440f)).setFont(koreanFont)
                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                .addCell("교육 지원금")
                .addCell("[] 은 6월 교육 일수 17일 중 지급일수[]\n 해당하는 교육지원금을 최종 확인 하였습니다."),
            Table(floatArrayOf(500f)).setFont(koreanFont)
                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                .addCell("""
                    학사시스템 > 마이캠퍼스 > 출석현황에서
                        []월 출석일 수 확인 후 지원금에 서명합니다.
                        
                                                [] 년 [] 월 [] 일
                                                
                                                성   명 : OOO  (인)
                """),
        )

        document.add(title)
        for(table in tables) document.add(table)

        try {
            // PDF 파일로 저장
            val outputStream = FileOutputStream(file)
            outputStream.flush()
            outputStream.close()

            Log.d("PDF", "PDF 파일이 생성되었습니다. 경로: $filePath")
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e("PDF", "PDF 생성 중 오류 발생")
        } finally {
            // 문서 닫기
            document.close()
        }
    }
}