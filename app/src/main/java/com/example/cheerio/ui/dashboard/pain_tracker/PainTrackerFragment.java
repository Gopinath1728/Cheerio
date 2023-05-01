package com.example.cheerio.ui.dashboard.pain_tracker;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.cheerio.R;
import com.example.cheerio.common.Common;
import com.example.cheerio.common.MyMarkerView;
import com.example.cheerio.databinding.FragmentPainTrackerBinding;
import com.example.cheerio.models.Pain_data_model;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Utils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PainTrackerFragment extends Fragment implements OnChartValueSelectedListener {

    private LineChart chart;

    private PainTrackerViewModel painTrackerViewModel;

    private FragmentPainTrackerBinding binding;



    private ImageView add_pain, img_pain_tracker_back;

    private List<Pain_data_model> pain_data_model;

    ArrayList<Entry> values = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        painTrackerViewModel = new ViewModelProvider(this).get(PainTrackerViewModel.class);
        binding = FragmentPainTrackerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();



        img_pain_tracker_back = binding.imgPainTrackerBack;
        chart = binding.lineChart;
        add_pain = binding.addPain;


        img_pain_tracker_back.setOnClickListener(back_clicked);

        add_pain.setOnClickListener(add_pain_click);

        painTrackerViewModel.getErrorMessage().observe(getViewLifecycleOwner(), s -> Snackbar.make(root, s, Snackbar.LENGTH_SHORT).show());

        painTrackerViewModel.getPainMutableList().observe(getViewLifecycleOwner(), pain_data_modelList -> {
            if (pain_data_modelList != null) {

                for (int i = 0; i < pain_data_modelList.size(); i++) {
                    values.add(new Entry(Integer.parseInt(pain_data_modelList.get(i).getDay()), Integer.parseInt(pain_data_modelList.get(i).getPain_intensity())));
                }


                chart.setBackgroundColor(Color.WHITE);
                chart.getDescription().setEnabled(false);
                chart.setTouchEnabled(true);

                chart.setOnChartValueSelectedListener(this);
                chart.setDrawGridBackground(false);

                MyMarkerView mv = new MyMarkerView(getContext(), R.layout.custom_marker_view);
                mv.setChartView(chart);
                chart.setMarker(mv);

                chart.setDragEnabled(true);
                chart.setScaleEnabled(true);
                chart.setPinchZoom(true);

                XAxis xAxis;
                {
                    xAxis = chart.getXAxis();
                    xAxis.setAxisMaximum(30f);
                    xAxis.setAxisMinimum(1f);
                }

                YAxis yAxis;
                {
                    yAxis = chart.getAxisLeft();

                    chart.getAxisRight().setEnabled(false);

                    yAxis.setAxisMaximum(10f);
                    yAxis.setAxisMinimum(0f);
                }


                LineDataSet set;

                if (chart.getData() != null &&
                        chart.getData().getDataSetCount() > 0) {
                    set = (LineDataSet) chart.getData().getDataSetByIndex(0);
                    set.setValues(values);
                    set.notifyDataSetChanged();
                    chart.getData().notifyDataChanged();
                    chart.notifyDataSetChanged();
                } else {
                    set = new LineDataSet(values, "");

                    set.setDrawIcons(false);
                    set.enableDashedLine(10f, 5f, 0f);
                    set.setColor(Color.BLACK);
                    set.setCircleColor(Color.BLACK);
                    set.setLineWidth(1f);
                    set.setCircleRadius(3f);
                    set.setDrawCircleHole(false);
                    set.setFormLineWidth(1f);
                    set.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
                    set.setFormSize(15.f);
                    set.setValueTextSize(9f);
                    set.enableDashedHighlightLine(10f, 5f, 0f);

                    set.setDrawFilled(true);
                    set.setFillFormatter(new IFillFormatter() {
                        @Override
                        public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                            return chart.getAxisLeft().getAxisMinimum();
                        }
                    });

                    // set color of filled area
                    if (Utils.getSDKInt() >= 18) {
                        // drawables only supported on api level 18 and above
                        Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.fade_red);
                        set.setFillDrawable(drawable);
                    } else {
                        set.setFillColor(Color.BLACK);
                    }

                    ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                    dataSets.add(set); // add the data sets

                    // create a data object with the data sets
                    LineData data = new LineData(dataSets);

                    // set data
                    chart.setData(data);
                }

                chart.animateX(150);
                Legend l = chart.getLegend();
                l.setForm(Legend.LegendForm.LINE);

            }
        });


        return root;
    }

    View.OnClickListener back_clicked = v -> {
        getActivity().onBackPressed();
    };

    View.OnClickListener add_pain_click = v -> {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View itemView = LayoutInflater.from(getContext()).inflate(R.layout.pain_record_dialog, null);
        builder.setView(itemView);
        RadioGroup pain_radio_group = (itemView).findViewById(R.id.pain_radio_group);
        EditText edt_pain_intensity = (itemView).findViewById(R.id.edt_pain_intensity);
        EditText pain_duration = (itemView).findViewById(R.id.pain_duration);
        Button btn_submit_pain = (itemView).findViewById(R.id.btn_submit_pain);
        Button btn_cancel_pain = (itemView).findViewById(R.id.btn_cancel_pain);
        AlertDialog dialog = builder.create();
        dialog.show();

        Pain_data_model pain_data_model = new Pain_data_model();

        btn_submit_pain.setOnClickListener(v1 -> {
            if (pain_radio_group.getCheckedRadioButtonId() == -1) {
                Snackbar.make(v1, "Please select a pain location", Snackbar.LENGTH_SHORT).show();
            } else {
                RadioButton radioButton = (itemView).findViewById(pain_radio_group.getCheckedRadioButtonId());
                pain_data_model.setPain_location(radioButton.getText().toString().trim());

                if (edt_pain_intensity.getText().toString().equals("") || Integer.parseInt(edt_pain_intensity.getText().toString()) > 10)
                    edt_pain_intensity.setError("Invalid input !");
                else {
                    pain_data_model.setPain_intensity(edt_pain_intensity.getText().toString());
                    if (pain_duration.getText().toString().equals("") || Integer.parseInt(pain_duration.getText().toString()) > 24)
                        pain_duration.setError("Invalid input !");
                    else {
                        pain_data_model.setPain_duration(pain_duration.getText().toString());
                        pain_data_model.setDay(new SimpleDateFormat("dd", Locale.getDefault()).format(new Date()));
                        pain_data_model.setMonth(new SimpleDateFormat("MMM", Locale.getDefault()).format(new Date()));
                        pain_data_model.setYear(new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date()));

                        FirebaseFirestore.getInstance().collection("Users")
                                .document(Common.user_model.getUid())
                                .collection("Pain Dairy")
                                .add(pain_data_model)
                                .addOnFailureListener(e -> {
                                    Snackbar.make(v1, e.getMessage(), Snackbar.LENGTH_SHORT).show();
                                })
                                .addOnSuccessListener(documentReference -> {
                                    dialog.dismiss();
                                });
                    }
                }
            }
        });

        btn_cancel_pain.setOnClickListener(v12 -> {
            dialog.dismiss();
        });

    };


    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Log.i("Entry selected", e.toString());
        Log.i("LOW HIGH", "low: " + chart.getLowestVisibleX() + ", high: " + chart.getHighestVisibleX());
        Log.i("MIN MAX", "xMin: " + chart.getXChartMin() + ", xMax: " + chart.getXChartMax() + ", yMin: " + chart.getYChartMin() + ", yMax: " + chart.getYChartMax());
    }

    @Override
    public void onNothingSelected() {
        Log.i("Nothing selected", "Nothing selected.");

    }
}