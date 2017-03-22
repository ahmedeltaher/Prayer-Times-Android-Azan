package com.eltaher.azan.sample;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.azan.PrayerTimes;
import com.azan.TimeCalculator;
import com.azan.types.PrayersType;

import java.util.GregorianCalendar;

import sample.azan.eltaher.com.azan.R;

import static com.azan.types.AngleCalculationType.KARACHI;
import static com.azan.types.AngleCalculationType.MUHAMMADIYAH;
import static com.azan.types.AngleCalculationType.MWL;
import static com.azan.types.AngleCalculationType.UMM_AL_QURA;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        azan();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void azan() {
        GregorianCalendar date = new GregorianCalendar();
        System.out.println(date.getTimeInMillis());
        PrayerTimes prayerTimes = new TimeCalculator().date(date).location(52.520008, 13.404954,
                0, 0).timeCalculationMethod(MUHAMMADIYAH).umElQuraRamadanAdjustment(false).calculateTimes();
        prayerTimes.setUseSecond(true);
        System.out.println("----------------------------------------");
        System.out.println("Fajr ---> " + prayerTimes.getPrayTime(PrayersType.FAJR));
        System.out.println("sunrise --->" + prayerTimes.getPrayTime(PrayersType.SUNRISE));
        System.out.println("Zuhr --->" + prayerTimes.getPrayTime(PrayersType.ZUHR));
        System.out.println("Asr --->" + prayerTimes.getPrayTime(PrayersType.ASR));
        System.out.println("Maghrib --->" + prayerTimes.getPrayTime(PrayersType.MAGHRIB));
        System.out.println("ISHA  --->" + prayerTimes.getPrayTime(PrayersType.ISHA));
        System.out.println("----------------------------------------");
    }
}
