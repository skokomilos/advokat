package com.example.berka.advokatormlite.activities.konacni;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.preference.PreferenceManager;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.PrintManager;
import android.print.pdf.PrintedPdfDocument;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.berka.advokatormlite.R;
import com.example.berka.advokatormlite.activities.BaseActivity;
import com.example.berka.advokatormlite.activities.add_points.PronadjeniSlucajActivity;
import com.example.berka.advokatormlite.adapter.SviTroskviAdapter;
import com.example.berka.advokatormlite.data.db.DatabaseHelper;
import com.example.berka.advokatormlite.model.IzracunatTrosakRadnje;
import com.example.berka.advokatormlite.model.Postupak;
import com.example.berka.advokatormlite.model.Slucaj;
import com.example.berka.advokatormlite.model.StrankaDetail;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by berka on 11-Oct-17.
 */

public class KonacniTrosakSvihRadnjiActivity extends BaseActivity {

    private static final String TAG = "KonacniTrosakSvihRadnji";
    DatabaseHelper databaseHelper;
    private Postupak postupak;
    private Slucaj slucaj;
    public static List<IzracunatTrosakRadnje> sveIzracunateRadnje;

    private String imeiprezime, adresa, mesto;

    private TextView tv_ukupna_cena;

    private int key;
    private static int ukupnaCenaSvihRadnji;

    private  List<StrankaDetail> strankaDetailsList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konacni_trosak_svih_radnji);

        getSharedPrefValues();

        slucaj = getIntent().getParcelableExtra("myDataKey");

        setupToolbar();
        loadSpinner();
        ukupnaCena();
    }

    private void getSharedPrefValues(){
        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = mPreferences.edit();

         imeiprezime = mPreferences.getString("prefName","");
         adresa = mPreferences.getString("prefAdres","");
         mesto = mPreferences.getString("prefPlace", "");
         editor.commit();
        Log.d(TAG, "getSharedPrefValues: " + imeiprezime + adresa + mesto);
    }

    public void printDocument(MenuItem item){

        PrintManager printManager = (PrintManager) this.getSystemService(Context.PRINT_SERVICE);
        String jobName = this.getString(R.string.app_name) + " Document";
        printManager.print(jobName, new MyPrintDocumentAdapter(this), null);
    }

    private void loadSlucaj(){
        key = getIntent().getExtras().getInt(PronadjeniSlucajActivity.SLUCAJ_KEY);
        try {
            slucaj = getDatabaseHelper().getSlucajDao().queryForId(key);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadSpinner(){
        final ListView listView = (ListView) findViewById(R.id.lista_svih_uradjenih_radnji);
        try {
            sveIzracunateRadnje = getDatabaseHelper().getmIzracunatTrosakRadnjeDao().queryBuilder()
                    .where()
                    .eq(IzracunatTrosakRadnje.SLUCAJ_ID, slucaj.getId())
                    .query();
            SviTroskviAdapter adapter = new SviTroskviAdapter(this, (ArrayList<IzracunatTrosakRadnje>) sveIzracunateRadnje);
            listView.setAdapter(adapter);


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void ukupnaCena(){

        tv_ukupna_cena = (TextView) findViewById(R.id.tv_ukupna_cena_svih_radnji);
        ukupnaCenaSvihRadnji = 0;

        ListIterator<IzracunatTrosakRadnje> iterator = null;
        iterator = sveIzracunateRadnje.listIterator();
        IzracunatTrosakRadnje iztr;
        while (iterator.hasNext()){
            iztr = iterator.next();
            ukupnaCenaSvihRadnji+=iztr.getCena_izracunate_jedinstvene_radnje();
        }
        tv_ukupna_cena.setText(String.valueOf(ukupnaCenaSvihRadnji));

    }

    //toolbar metode
    //5 metoda za nav/toolbar
    private void setupToolbar() {
        final ActionBar ab = getActionBarToolbar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(slucaj.getPostupak().getNazivpostupka());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.konacnalista_action, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                openDrawer();
                return true;
            case R.id.action_makepdf:
                Toast.makeText(KonacniTrosakSvihRadnjiActivity.this, "Good job", Toast.LENGTH_LONG).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int getSelfNavDrawerItem() {
        return R.id.nav_quotes;
    }

    @Override
    public boolean providesActivityToolbar() {
        return true;
    }

    public DatabaseHelper getDatabaseHelper(){
        if (databaseHelper == null) {
            databaseHelper = new DatabaseHelper(this);
        }
        return databaseHelper;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }

    public class MyPrintDocumentAdapter extends PrintDocumentAdapter {

        Context context;
        private int pageHeight;
        private int pageWidth;
        public PdfDocument myPdfDocument;
        public int totalpages = 4;
        int pages;

        public int counterForListElements = 0;

        List<IzracunatTrosakRadnje> lista = KonacniTrosakSvihRadnjiActivity.sveIzracunateRadnje;


        public MyPrintDocumentAdapter(Context context) {
            this.context = context;
        }

        @Override
        public void onLayout(PrintAttributes oldAttributes,
                             PrintAttributes newAttributes,
                             CancellationSignal cancellationSignal,
                             LayoutResultCallback callback,
                             Bundle extras) {


            myPdfDocument = new PrintedPdfDocument(context, newAttributes);

            pageHeight = newAttributes.getMediaSize().getHeightMils()/1000 * 72;
            pageWidth = newAttributes.getMediaSize().getWidthMils()/1000 * 72;


            if (cancellationSignal.isCanceled() ) {
                callback.onLayoutCancelled();
                return;
            }

            // Compute the expected number of printed pages
            pages = computePageCount(newAttributes, lista.size());

            Log.d("Broj", String.valueOf(pages));

            if (pages > 0) {
                PrintDocumentInfo.Builder builder = new PrintDocumentInfo
                        .Builder("print_output.pdf")
                        .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                        .setPageCount(pages);
                PrintDocumentInfo info = builder.build();
                callback.onLayoutFinished(info, true);
            } else {
                callback.onLayoutFailed("Page count is zero.");
            }
        }

        private int computePageCount(PrintAttributes printAttributes, int ukupanBrojStrana) {
            int itemsPerPage = 20; // default item count for portrait mode

            PrintAttributes.MediaSize pageSize = printAttributes.getMediaSize();
            if (!pageSize.isPortrait()) {
                itemsPerPage = 20;
            }

            return (int) Math.ceil((double) ukupanBrojStrana / itemsPerPage);
        }

        @Override
        public void onWrite(PageRange[] pageRanges,
                            ParcelFileDescriptor destination,
                            CancellationSignal cancellationSignal,
                            WriteResultCallback callback) {

            for (int i = 0; i < pages; i++) {
                if (pageInRange(pageRanges, i))
                {
                    PdfDocument.PageInfo newPage = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, i).create();
                    PdfDocument.Page page = myPdfDocument.startPage(newPage);

                    if (cancellationSignal.isCanceled()) {
                        callback.onWriteCancelled();
                        myPdfDocument.close();
                        myPdfDocument = null;
                        return;
                    }
                    if(i==0){
                        drawFirstPage(page, i);
                    }else {
                        drawOtherPages(page,i);
                    }

                    myPdfDocument.finishPage(page);
                }
            }

            try {
                myPdfDocument.writeTo(new FileOutputStream(destination.getFileDescriptor()));
            } catch (IOException e) {
                callback.onWriteFailed(e.toString());
                return;
            } finally {

                myPdfDocument.close();
                myPdfDocument = null;
                counterForListElements = 0;
            }
            callback.onWriteFinished(pageRanges);
        }

        private boolean pageInRange(PageRange[] pageRanges, int page)
        {
            for (int i = 0; i<pageRanges.length; i++)
            {
                if ((page >= pageRanges[i].getStart()) && (page <= pageRanges[i].getEnd()))
                    return true;
            }
            return false;
        }

        private void drawFirstPage(PdfDocument.Page page, int pagenumber) {

            Canvas canvas = page.getCanvas();
            pagenumber++; // Make sure page numbers start at 1
            int titleBaseLine = 72;
            int leftMargin = 54;
            int leftMarginForName = 100;

            Paint paint = new Paint();
            paint.setColor(Color.BLACK);


            paint.setTextSize(20);
            canvas.drawText("advokat: " + imeiprezime + " iz: " + adresa + " adresa: " + mesto, leftMargin, titleBaseLine, paint);


            try {
                strankaDetailsList = getDatabaseHelper().getmStrankaDetail().queryBuilder()
                        .where()
                        .eq(StrankaDetail.ID_SLUCAJA, slucaj.getId())
                        .query();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            //
            paint.setTextSize(20);
            for (int i = 0; i < slucaj.getBroj_stranaka(); i++) {
                canvas.drawText("Stranka: " + strankaDetailsList.get(i).getIme_i_prezime() + ", mesto stanovanja: " + strankaDetailsList.get(i).getAdresa() + ", iz: " + strankaDetailsList.get(i).getMesto(),
                        leftMarginForName,
                        titleBaseLine += 35,
                        paint);
            }

            //titleBaseLine = 142;
            Log.d("Broj", String.valueOf(titleBaseLine));
            for (int i = counterForListElements; i < lista.size(); i++) {
                if(pageHeight -35>titleBaseLine){
                    counterForListElements++;
                    paint.setTextSize(14);
                    canvas.drawText(1+i + " " + lista.get(i).getNaziv_radnje(),leftMargin,titleBaseLine+=35, paint);
                    canvas.drawText("Od " + lista.get(i).getDatum(),leftMargin + 300,titleBaseLine, paint);
                    canvas.drawText(String.valueOf(lista.get(i).getCena_izracunate_jedinstvene_radnje()),leftMargin + 450,titleBaseLine, paint);
                }
            }

            leftMargin = 350;
            if(counterForListElements==lista.size()){
                Log.d("Uspelo", "works");
                paint.setTextSize(14);
                canvas.drawText("Ukupan trosak svih radnji :" + KonacniTrosakSvihRadnjiActivity.ukupnaCenaSvihRadnji,
                        leftMargin,
                        titleBaseLine + 35,
                        paint);
            }
        }

        private void drawOtherPages(PdfDocument.Page page, int pagenumber){

            Canvas canvas = page.getCanvas();
            pagenumber++; // Make sure page numbers start at 1
            int titleBaseLine = 25;
            int leftMargin = 54;
            Paint paint = new Paint();
            paint.setColor(Color.BLACK);

            for (int i = counterForListElements; i < lista.size(); i++) {
                if(pageHeight -35>titleBaseLine){
                    counterForListElements++;
                    paint.setTextSize(14);
                    canvas.drawText(i + " " + lista.get(i).getNaziv_radnje(),leftMargin ,titleBaseLine+=35, paint);
                }
            }

            leftMargin = 350;
            if(counterForListElements==lista.size()){
                Log.d("Uspelo", "works");
                paint.setTextSize(14);
                canvas.drawText("Ukupan trosak svih radnji  :" + KonacniTrosakSvihRadnjiActivity.ukupnaCenaSvihRadnji,
                        leftMargin,
                        titleBaseLine + 35,
                        paint);
            }
        }
    }
}
